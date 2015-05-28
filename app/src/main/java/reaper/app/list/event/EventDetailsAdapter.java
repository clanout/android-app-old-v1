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

import reaper.R;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventDetails;
import reaper.api.model.user.User;

/**
 * Created by Aditya on 08-04-2015.
 */
public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder> {
    private LayoutInflater inflater;
    List<EventDetails.Attendee> data = Collections.emptyList();
    private ClickListener clickListener;

    public EventDetailsAdapter(Context context, List<EventDetails.Attendee> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public EventDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_event_details, parent, false);
        EventDetailsViewHolder eventDetailsViewHolder = new EventDetailsViewHolder(view);
        return eventDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(EventDetailsViewHolder holder, int position) {
        EventDetails.Attendee current = data.get(position);
        holder.name.setText(current.getName());
        holder.userPic.setImageResource(R.drawable.ic_person_black_36dp);

        if (current.getRsvp() == Event.RSVP.YES) {
            holder.rsvpIcon.setImageResource(R.drawable.ic_check_black_18dp);
        }

        if (current.getRsvp() == Event.RSVP.MAYBE) {
            holder.rsvpIcon.setImageResource(R.drawable.ic_close_black_18dp);
        }

        if (current.isInviter()) {
            holder.invitedYouIcon.setVisibility(View.VISIBLE);
            holder.invitedYouIcon.setImageResource(R.drawable.ic_mail_black_18dp);
        } else {
            holder.invitedYouIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EventDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userPic, invitedYouIcon, rsvpIcon;
        TextView name;

        public EventDetailsViewHolder(View itemView) {
            super(itemView);
            userPic = (ImageView) itemView.findViewById(R.id.ivEventDetailsListItemUserPic);
            invitedYouIcon = (ImageView) itemView.findViewById(R.id.ivEventDetailsListItemInvitedYouIcon);
            rsvpIcon = (ImageView) itemView.findViewById(R.id.ivEventDetailsListItemRsvpIcon);
            name = (TextView) itemView.findViewById(R.id.tvEventDetailsListItemUserName);

            invitedYouIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.ivEventDetailsListItemInvitedYouIcon) {
                if (clickListener != null) {
                    clickListener.ItemClicked(view, getPosition());
                }
            }
        }
    }

    public interface ClickListener {
        public void ItemClicked(View view, int position);
    }
}
