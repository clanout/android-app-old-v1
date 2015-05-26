package reaper.app.list.event;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventCategory;

/**
 * Created by reaper on 04-04-2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>
{
    private LayoutInflater inflater;
    private Context context;
    private EventSummaryItemClickListener eventSummaryItemClickListener;
    private RsvpClickListener rsvpClickListener;

    private List<Event> data;
    private List<SwipedState> itemSwipedStates;

    private enum SwipedState
    {
        SHOWING_PRIMARY_CONTENT(0),
        SHOWING_SECONDARY_CONTENT(1);

        private int position;

        private SwipedState(int position)
        {
            this.position = position;
        }

        public int getPosition()
        {
            return position;
        }

    }

    public EventListAdapter(Context context, List<Event> data)
    {

        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

        itemSwipedStates = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
        {
            itemSwipedStates.add(SwipedState.SHOWING_PRIMARY_CONTENT);
        }
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        // Create a new view which is basically just a ViewPager in this case
        ViewPager v = (ViewPager) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event_summary, parent, false);

        //Perhaps the first most crucial part. The ViewPager loses its width information when it is put
        //inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
        //screen. The height must be provided as a fixed value.
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        v.getLayoutParams().width = displayMetrics.widthPixels;
        v.requestLayout();

        EventListViewHolder vh = new EventListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, final int position)
    {
        final ViewPager viewPager = (ViewPager) holder.itemView;

        ((ViewPager) holder.itemView).setCurrentItem(itemSwipedStates.get(position).getPosition());

        ((ViewPager) holder.itemView).setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            int previousPagePosition = 0;

            @Override
            public void onPageScrolled(int pagePosition, float positionOffset, int positionOffsetPixels)
            {
                if (pagePosition == previousPagePosition)
                {
                    return;
                }

                switch (pagePosition)
                {
                    case 0:
                        itemSwipedStates.set(position, SwipedState.SHOWING_PRIMARY_CONTENT);
                        break;
                    case 1:
                        itemSwipedStates.set(position, SwipedState.SHOWING_SECONDARY_CONTENT);
                        break;

                }
                previousPagePosition = pagePosition;
            }

            @Override
            public void onPageSelected(int pagePosition)
            {
                //This method keep incorrectly firing as the RecyclerView scrolls.
                //Use the one above instead
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });


        Event currentItem = data.get(position);

        if(currentItem.getTitle().length() <= 25){
            holder.title.setText(currentItem.getTitle());
        }else{
            holder.title.setText(currentItem.getTitle().substring(0,22) + "...");
        }


        DateTime dateTime = currentItem.getStartTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd");
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

        if (currentItem.getLocation().getName().isEmpty() || currentItem.getLocation().getName() == null)
        {
            holder.timeLocation.setText(dateTime.toString(timeFormatter) + ", (Location Not Specified)");
        }
        else
        {
            if(currentItem.getLocation().getName().length() <= 23) {
                holder.timeLocation.setText(dateTime.toString(timeFormatter) + ", " + currentItem.getLocation().getName());
            }else{
                holder.timeLocation.setText(dateTime.toString(timeFormatter) + ", " + currentItem.getLocation().getName().substring(0,22) + "..");
            }
        }

        if (currentItem.getFriendCount() == 0)
        {
            holder.attendees.setText("No friends are going");
        }
        else if (currentItem.getFriendCount() == 1)
        {
            holder.attendees.setText("1 friend is going");
        }
        else
        {
            holder.attendees.setText(currentItem.getFriendCount() + " friends are going");
        }

        holder.date.setText(dateTime.toString(dateFormatter));

        if (currentItem.getRsvp() == Event.RSVP.YES)
        {
            holder.rsvpIcon.setVisibility(View.VISIBLE);
            holder.rsvpIcon.setImageResource(R.drawable.ic_check_black_24dp);
        }
        else if (currentItem.getRsvp() == Event.RSVP.MAYBE)
        {
            holder.rsvpIcon.setVisibility(View.VISIBLE);
            holder.rsvpIcon.setImageResource(R.drawable.ic_help_black_24dp);
        }
        else
        {
            holder.rsvpIcon.setVisibility(View.VISIBLE);
            holder.rsvpIcon.setImageResource(R.drawable.ic_close_black_24dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.GENERAL))) {
            holder.eventIcon.setImageResource(R.drawable.ic_event_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.EAT_OUT))) {
            holder.eventIcon.setImageResource(R.drawable.ic_local_restaurant_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.DRINKS))) {
            holder.eventIcon.setImageResource(R.drawable.ic_local_bar_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.CAFE))) {
            holder.eventIcon.setImageResource(R.drawable.ic_local_cafe_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.MOVIES))) {
            holder.eventIcon.setImageResource(R.drawable.ic_local_movies_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.OUTDOORS))) {
            holder.eventIcon.setImageResource(R.drawable.ic_directions_bike_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.PARTY))) {
            holder.eventIcon.setImageResource(R.drawable.ic_location_city_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.LOCAL_EVENTS))) {
            holder.eventIcon.setImageResource(R.drawable.ic_local_attraction_black_36dp);
        }

        if(currentItem.getCategory().equals(String.valueOf(EventCategory.SHOPPING))) {
            holder.eventIcon.setImageResource(R.drawable.ic_local_mall_black_36dp);
        }


        if (Math.random() < 0.5)
        {
            holder.chatIcon.setVisibility(View.VISIBLE);
            holder.chatIcon.setImageResource(R.drawable.ic_chat_black_18dp);
        }
        else
        {
            holder.chatIcon.setVisibility(View.INVISIBLE);
        }

        if (Math.random() < 0.5)
        {
            holder.updatesIcon.setVisibility(View.VISIBLE);
            holder.updatesIcon.setImageResource(R.drawable.ic_info_black_18dp);
        }
        else
        {
            holder.updatesIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void setEventSummaryItemClickListener(EventSummaryItemClickListener eventSummaryItemClickListener)
    {
        this.eventSummaryItemClickListener = eventSummaryItemClickListener;
    }

    public void setRsvpClickListener(RsvpClickListener rsvpClickListener)
    {
        this.rsvpClickListener = rsvpClickListener;
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class EventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CardView cardView;
        private ImageView eventIcon, rsvpIcon, chatIcon, updatesIcon;
        private TextView title, timeLocation, attendees, date;
        private Button going, mayBe, notGoing;

        public EventListViewHolder(View itemView)
        {
            super(itemView);


            final ViewPager viewPager = (ViewPager) itemView;

            EventListViewPagerAdapter adapter = new EventListViewPagerAdapter();
            viewPager.setAdapter(adapter);

            cardView = (CardView) itemView.findViewById(R.id.primaryContentCardView);
            cardView.setOnClickListener(this);

            eventIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventIcon);
            rsvpIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventRsvp);
            chatIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventChat);
            updatesIcon = (ImageView) itemView.findViewById(R.id.ivListItemEventUpdates);
            title = (TextView) itemView.findViewById(R.id.tvListItemEventTitle);
            timeLocation = (TextView) itemView.findViewById(R.id.tvListItemEventTimeLocation);
            attendees = (TextView) itemView.findViewById(R.id.tvListItemEventAttendees);
            date = (TextView) itemView.findViewById(R.id.tvListItemEventDate);
            going = (Button) itemView.findViewById(R.id.bListItemEventGoing);
            mayBe = (Button) itemView.findViewById(R.id.bListItemEventMaybe);
            notGoing = (Button) itemView.findViewById(R.id.bListItemEventNotGoing);

            going.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (rsvpClickListener != null)
                    {
                        rsvpIcon.setVisibility(View.VISIBLE);
                        rsvpIcon.setImageResource(R.drawable.ic_check_black_24dp);
                        itemSwipedStates.set(getPosition(), SwipedState.SHOWING_PRIMARY_CONTENT);
                        viewPager.setCurrentItem(itemSwipedStates.get(getPosition()).getPosition());

                        rsvpClickListener.onRsvpButtonClicked(view, 0, getPosition());
                    }
                }
            });

            mayBe.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (rsvpClickListener != null)
                    {
                        rsvpIcon.setVisibility(View.VISIBLE);
                        rsvpIcon.setImageResource(R.drawable.ic_help_black_24dp);
                        itemSwipedStates.set(getPosition(), SwipedState.SHOWING_PRIMARY_CONTENT);
                        viewPager.setCurrentItem(itemSwipedStates.get(getPosition()).getPosition());

                        rsvpClickListener.onRsvpButtonClicked(view, 1, getPosition());
                    }
                }
            });

            notGoing.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (rsvpClickListener != null)
                    {
                        rsvpIcon.setVisibility(View.VISIBLE);
                        rsvpIcon.setImageResource(R.drawable.ic_close_black_24dp);
                        itemSwipedStates.set(getPosition(), SwipedState.SHOWING_PRIMARY_CONTENT);
                        viewPager.setCurrentItem(itemSwipedStates.get(getPosition()).getPosition());

                        rsvpClickListener.onRsvpButtonClicked(view, 2, getPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v)
        {
            if (eventSummaryItemClickListener != null)
            {
                eventSummaryItemClickListener.onEventSummaryItemClicked(v, getPosition());

            }
        }
    }

    public interface EventSummaryItemClickListener
    {
        public void onEventSummaryItemClicked(View view, int position);
    }

    public interface RsvpClickListener
    {
        public void onRsvpButtonClicked(View view, int buttonId, int position);
    }

}
