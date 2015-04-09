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

import reaper.api.model.user.User;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 08-04-2015.
 */
public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder>
{
    private LayoutInflater inflater;
    List<User> data = Collections.emptyList();
    private ClickListener clickListener;

    public EventDetailsAdapter(Context context, List<User> data)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public EventDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.custom_row_event_details, parent, false);
        EventDetailsViewHolder eventDetailsViewHolder = new EventDetailsViewHolder(view);
        return eventDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(EventDetailsViewHolder holder, int position)
    {
        User current = data.get(position);
        holder.name.setText(current.getFirstname() + " " + current.getLastname());
        holder.pic.setImageResource(current.getProfilePic());
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class EventDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView pic;
        TextView name;

        public EventDetailsViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            pic = (ImageView) itemView.findViewById(R.id.ivCustomRowEventDetailsUserPic);
            name = (TextView) itemView.findViewById(R.id.tvCustomRowEventDetailsUserName);
        }

        @Override
        public void onClick(View view)
        {
            if(clickListener != null){
                clickListener.ItemClicked(view, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void ItemClicked(View view, int position);
    }
}
