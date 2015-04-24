package reaper.app.list.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import reaper.R;
import reaper.api.model.event.Event;

/**
 * Created by reaper on 04-04-2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>
{
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    private List<Event> data;

    public EventListAdapter(Context context, List<Event> data)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item_event_summary, parent, false);
        EventListViewHolder eventListViewHolder = new EventListViewHolder(view);
        return eventListViewHolder;
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position)
    {
        Event currentItem = data.get(position);
        holder.title.setText(currentItem.getTitle());

        DateTime dateTime = currentItem.getStartTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd");
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

        if (currentItem.getLocation().getName().isEmpty() || currentItem.getLocation().getName() == null)
        {
            holder.timeLocation.setText(dateTime.toString(timeFormatter) + ", (Location Not Specified)");
        }
        else
        {
            holder.timeLocation.setText(dateTime.toString(timeFormatter) + ", " + currentItem.getLocation().getName());
        }

        if (currentItem.getFriendCount() == 0)
        {
            holder.attendees.setText("No friends are going");
        }
        else
        {

            if (currentItem.getFriendCount() == 1)
            {
                holder.attendees.setText("1 friend is going");
            }
            else
            {
                holder.attendees.setText(currentItem.getFriendCount() + " friends are going");
                holder.date.setText(dateTime.toString(dateFormatter));
            }
        }
        if (currentItem.getRsvp() == Event.RSVP.YES)
        {
            holder.rsvpIcon.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }

        if (currentItem.getRsvp() == Event.RSVP.MAYBE)
        {
            holder.rsvpIcon.setImageResource(R.drawable.ic_help_black_24dp);
        }

        if (currentItem.getCategory() == "bar")
        {
            holder.eventIcon.setImageResource(R.drawable.ic_local_bar_black_36dp);
        }

        holder.chatIcon.setImageResource(R.drawable.ic_chat_black_18dp);
        holder.updatesIcon.setImageResource(R.drawable.ic_info_black_18dp);
    }

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class EventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView eventIcon, rsvpIcon, chatIcon, updatesIcon;
        TextView title, timeLocation, attendees, date;

        public EventListViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);

            eventIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventIcon);
            rsvpIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventRsvp);
            chatIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventChat);
            updatesIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventUpdates);
            title = (TextView) itemView.findViewById(R.id.tvListItemEventTitle);
            timeLocation = (TextView) itemView.findViewById(R.id.tvListItemEventTimeLocation);
            attendees = (TextView) itemView.findViewById(R.id.tvListItemEventAttendees);
            date = (TextView) itemView.findViewById(R.id.tvListItemEventDate);
        }

        @Override
        public void onClick(View v)
        {
            if (clickListener != null)
            {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener
    {
        public void itemClicked(View view, int position);
    }
}