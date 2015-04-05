package reaper.list.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import reaper.local.reaper.R;

/**
 * Created by reaper on 04-04-2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>{

    private LayoutInflater inflater;
    List<EventListItem> data = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;

    public EventListAdapter(Context context, List<EventListItem> data) {
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
        EventListItem currentItem = data.get(position);
        holder.title.setText(currentItem.title);
        holder.location.setText(currentItem.location);
        holder.attendees.setText(currentItem.attendees);
        holder.date.setText(currentItem.date);
        holder.time.setText(currentItem.time);
        holder.eventIcon.setImageResource(currentItem.eventIconId);
        holder.statusIcon.setImageResource(currentItem.statusIconId);
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
        TextView title, location, attendees, date, time;

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