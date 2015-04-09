package reaper.app.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import reaper.api.event.EventDetailsApi;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventDetails;
import reaper.api.model.event.EventType;
import reaper.api.model.user.User;
import reaper.app.list.event.EventDetailsAdapter;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 06-04-2015.
 */
public class EventDetailsFragment extends Fragment implements View.OnClickListener, EventDetailsAdapter.ClickListener
{
    private Event event;

    private EventDetails eventDetails;

    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noUsersMessage;

    ImageView eventIcon;
    TextView title, description, location, date, time, type, lockStatus;
    Button invite, chat;
    private RecyclerView recyclerView;

    private EventDetailsAdapter eventDetailsAdapter;

    List<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        eventIcon = (ImageView) view.findViewById(R.id.ivEventDetailsIcon);
        title = (TextView) view.findViewById(R.id.tvEventDetailsTitle);
        description = (TextView) view.findViewById(R.id.tvEventDetailsDescription);
        location = (TextView) view.findViewById(R.id.tvEventDetailsLocation);
        date = (TextView) view.findViewById(R.id.tvEventDetailsDate);
        time = (TextView) view.findViewById(R.id.tvEventDetailsTime);
        type = (TextView) view.findViewById(R.id.tvEvenDetailsType);
        lockStatus = (TextView) view.findViewById(R.id.tvEventDetailsLockStatus);
        invite = (Button) view.findViewById(R.id.bEventDetailsInviteUsers);
        chat = (Button) view.findViewById(R.id.bEventDetailsChat);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEventDetails);
        mainContent = (LinearLayout) view.findViewById(R.id.llEventDetailsMainContent);
        noUsersMessage = (TextView) view.findViewById(R.id.tvEventDetailsNoUsers);

        ImageView iconRsvpStatus = new ImageView(getActivity());
        iconRsvpStatus.setImageResource(R.mipmap.ic_action_accept);

        FloatingActionButton rsvpStatus = new FloatingActionButton.Builder(getActivity())
                .setContentView(iconRsvpStatus)
                .build();

        rsvpStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(), "FAB Rsvp was clicked", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noUsersMessage.setVisibility(View.GONE);


        invite.setOnClickListener(this);
        chat.setOnClickListener(this);

        invite.setEnabled(false);

        apiTask = new ApiTask(getActivity(), event.getId());
        apiTask.execute();

        eventIcon.setImageResource(event.getEventIconId());
        title.setText(event.getTitle());
        location.setText(event.getLocation().getName());
        date.setText(event.getStartDate() + "-" + event.getEndDate());
        time.setText(event.getStartTime() + "-" + event.getEndTime());
        type.setText(event.getType().toString());

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        eventDetailsAdapter = new EventDetailsAdapter(getActivity(), userList);
        eventDetailsAdapter.setClickListener(this);
        recyclerView.setAdapter(eventDetailsAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView(List<User> userList)
    {
        eventDetailsAdapter = new EventDetailsAdapter(getActivity(), userList);
        eventDetailsAdapter.setClickListener(this);
        recyclerView.setAdapter(eventDetailsAdapter);

        if (userList.size() == 0)
        {
            noUsersMessage.setText("No users to show");
            noUsersMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
        else
        {
            mainContent.setVisibility(View.VISIBLE);
            noUsersMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (apiTask != null)
        {
            apiTask.cancel(true);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getActionBar().setTitle(event.getTitle());
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }


    @Override
    public void onClick(View view)
    {

    }

    @Override
    public void ItemClicked(View view, int position)
    {

    }

    private class ApiTask extends EventDetailsApi
    {
        public ApiTask(Context context, String id)
        {
            super(context, id);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);

            try
            {
                eventDetails = (EventDetails) o;
                description.setText(eventDetails.getDescription());

                for (User user : eventDetails.getGoingUsers())
                {
                    userList.add(user);
                }

                for (User user : eventDetails.getMaybeUsers())
                {
                    userList.add(user);
                }

                refreshRecyclerView(userList);

                if (event.isOrganizer())
                {
                    invite.setEnabled(true);
                }
                else
                {
                    if (!eventDetails.isFinalized() && (event.getType() == EventType.PUBLIC))
                    {
                        invite.setEnabled(true);
                    }
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
