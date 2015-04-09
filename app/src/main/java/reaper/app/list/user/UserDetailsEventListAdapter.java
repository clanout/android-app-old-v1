package reaper.app.list.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import reaper.api.model.event.Event;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 08-04-2015.
 */
public class UserDetailsEventListAdapter extends RecyclerView.Adapter<UserDetailsEventListAdapter.UserDetailsEventListViewHolder>
{
    private LayoutInflater inflater;
    List<Event> data = Collections.emptyList();

    public UserDetailsEventListAdapter(Context context, List<Event> data)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public UserDetailsEventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.custom_row_user_details_event_list, parent, false);
        UserDetailsEventListViewHolder userDetailsEventListViewHolder = new UserDetailsEventListViewHolder(view);
        return userDetailsEventListViewHolder;
    }

    @Override
    public void onBindViewHolder(UserDetailsEventListViewHolder holder, int position)
    {
        Event currentItem = data.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.location.setText(currentItem.getLocation().getName());
        holder.attendees.setText(currentItem.getAttendeeCount() + " friends are going");
        holder.date.setText(currentItem.getStartDate() + "-" + currentItem.getEndDate());
        holder.time.setText(currentItem.getStartTime() + "-" + currentItem.getEndTime());
        holder.eventIcon.setImageResource(currentItem.getEventIconId());
        holder.statusIcon.setImageResource(currentItem.getStatusIconId());
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class UserDetailsEventListViewHolder extends RecyclerView.ViewHolder
    {
        ImageView eventIcon, statusIcon;
        TextView title, location, attendees, date, time;

        public UserDetailsEventListViewHolder(View itemView)
        {
            super(itemView);
            eventIcon = (ImageView) itemView.findViewById(R.id.ivCustomRowUserDetailsEventListEventIcon);
            statusIcon = (ImageView) itemView.findViewById(R.id.ivCustomRowUserDetailsEventListStatusIcon);
            title = (TextView) itemView.findViewById(R.id.tvCustomRowUserDetailsEventListTitle);
            location = (TextView) itemView.findViewById(R.id.tvCustomRowUserDetailsEventListLocation);
            attendees = (TextView) itemView.findViewById(R.id.tvCustomRowUserDetailsEventListAttendees);
            date = (TextView) itemView.findViewById(R.id.tvCustomRowUserDetailsEventListDate);
            time = (TextView) itemView.findViewById(R.id.tvCustomRowUserDetailsEventListTime);
        }
    }
}
