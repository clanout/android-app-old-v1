package reaper.app.list.event;

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
 * Created by reaper on 04-04-2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>{

    private LayoutInflater inflater;
    List<Event> data = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;

    public EventListAdapter(Context context, List<Event> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_event_list, parent, false);
        EventListViewHolder eventListViewHolder = new EventListViewHolder(view);
        return eventListViewHolder;
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        Event currentItem = data.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.location.setText(currentItem.getLocation().getName());
        holder.attendees.setText(currentItem.getAttendeeCount() + " friends are going");
        holder.date.setText(currentItem.getStartDate() + "-" + currentItem.getEndDate());
        holder.time.setText(currentItem.getStartTime() + "-" + currentItem.getEndTime());
        holder.eventIcon.setImageResource(currentItem.getEventIconId());
        holder.statusIcon.setImageResource(currentItem.getStatusIconId());
 //       holder.notification.setText(currentItem.getNotificationCount());
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView eventIcon, statusIcon;
        TextView title, location, attendees, date, time, notification;

        public EventListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            eventIcon = (ImageView) itemView.findViewById(R.id.ivCustomRowEventListEventIcon);
            statusIcon = (ImageView) itemView.findViewById(R.id.ivCustomRowEventListStatusIcon);
            title = (TextView) itemView.findViewById(R.id.tvCustomRowEventListTitle);
            location = (TextView) itemView.findViewById(R.id.tvCustomRowEventListLocation);
            attendees = (TextView) itemView.findViewById(R.id.tvCustomRowEventListAttendees);
            date = (TextView) itemView.findViewById(R.id.tvCustomRowEventListDate);
            time = (TextView) itemView.findViewById(R.id.tvCustomRowEventListTime);
            notification = (TextView) itemView.findViewById(R.id.tvCustomRowEventListNotifications);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{

        public void itemClicked(View view, int position);
    }
}