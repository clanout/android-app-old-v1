package reaper.app.fragment.event;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.event.EventDetailsApi;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventDetails;
import reaper.app.activity.MainActivity;
import reaper.app.fragment.chat.ChatFragment;
import reaper.app.list.event.EventDetailsAdapter;
import reaper.app.service.EventUtils;

/**
 * Created by Aditya on 06-04-2015.
 */
public class EventDetailsFragment extends Fragment implements View.OnClickListener, EventDetailsAdapter.ClickListener, MenuItem.OnMenuItemClickListener {
    private Event event;
    private EventDetails eventDetails;

    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noUsersMessage;

    private ImageView eventIcon;
    private TextView title, description, location, startDateTime, endDateTime;
    private Button rsvp, invite, chat;
    private RecyclerView recyclerView;

    private EventDetailsAdapter eventDetailsAdapter;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        eventIcon = (ImageView) view.findViewById(R.id.ivEventDetailsIcon);
        title = (TextView) view.findViewById(R.id.tvEventDetailsTitle);
        description = (TextView) view.findViewById(R.id.tvEventDetailsDescription);
        location = (TextView) view.findViewById(R.id.tvEventDetailsLocation);
        startDateTime = (TextView) view.findViewById(R.id.tvEventDetailsStartDateTime);
        endDateTime = (TextView) view.findViewById(R.id.tvEventDetailsEndDateTime);
        rsvp = (Button) view.findViewById(R.id.bEventDetailsRsvp);
        invite = (Button) view.findViewById(R.id.bEventDetailsInvite);
        chat = (Button) view.findViewById(R.id.bEventDetailsChat);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEventDetails);
        mainContent = (LinearLayout) view.findViewById(R.id.llEventdetailsUsers);
        noUsersMessage = (TextView) view.findViewById(R.id.tvEventDetailsNoUsers);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noUsersMessage.setVisibility(View.GONE);

        eventDetails = null;

        invite.setText("Invite People");
        chat.setText("Chat");

        if (event.getRsvp() == Event.RSVP.YES) {
            rsvp.setText("Going");
        }

        if (event.getRsvp() == Event.RSVP.NO) {
            rsvp.setText("Not Going");
        }

        if (event.getRsvp() == Event.RSVP.MAYBE) {
            rsvp.setText("Maybe");
        }

        invite.setOnClickListener(this);
        rsvp.setOnClickListener(this);
        chat.setOnClickListener(this);
        location.setOnClickListener(this);

        eventIcon.setImageResource(R.drawable.ic_local_bar_black_48dp);
        title.setText(event.getTitle());
        location.setText(event.getLocation().getName());

        DateTime start = event.getStartTime();
        DateTime end = event.getEndTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd");
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

        startDateTime.setText(start.toString(timeFormatter) + ", " + start.toString(dateFormatter));
        endDateTime.setText(end.toString(timeFormatter) + ", " + end.toString(dateFormatter));

        fragmentManager = getActivity().getSupportFragmentManager();

        initRecyclerView();

        EventDetails eventDetails1 = new EventDetails();
        eventDetails1.setDescription("Please be there by 6pm. Pets not allowed. This is a bring your own booze party.");

        List<EventDetails.Attendee> attendees = new ArrayList<>();
        EventDetails.Attendee attendee1 = new EventDetails.Attendee();
        EventDetails.Attendee attendee2 = new EventDetails.Attendee();
        EventDetails.Attendee attendee3 = new EventDetails.Attendee();

        attendee1.setName("Harsh Pokharna");
        attendee2.setName("Aditya Prasad");
        attendee3.setName("Gaurav Kunwar");

        attendee1.setFriend(true);
        attendee2.setFriend(false);
        attendee3.setFriend(true);

        attendee1.setId("1");
        attendee2.setId("2");
        attendee3.setId("3");

        attendee1.setInviter(true);
        attendee2.setInviter(true);
        attendee3.setInviter(false);

        attendee1.setRsvp(Event.RSVP.MAYBE);
        attendee2.setRsvp(Event.RSVP.MAYBE);
        attendee3.setRsvp(Event.RSVP.YES);

        attendees.add(attendee1);
        attendees.add(attendee2);
        attendees.add(attendee3);

        eventDetails1.setAttendees(attendees);
        description.setText(eventDetails1.getDescription());
        refreshRecyclerView(eventDetails1.getAttendees());

    }

    private void initRecyclerView() {
        eventDetailsAdapter = new EventDetailsAdapter(getActivity(), new ArrayList<EventDetails.Attendee>());
        eventDetailsAdapter.setClickListener(this);
        recyclerView.setAdapter(eventDetailsAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView(List<EventDetails.Attendee> attendeeList) {
        eventDetailsAdapter = new EventDetailsAdapter(getActivity(), attendeeList);
        eventDetailsAdapter.setClickListener(this);
        recyclerView.setAdapter(eventDetailsAdapter);

        if (attendeeList.size() == 0) {
            noUsersMessage.setText("No users to show");
            noUsersMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        } else {
            mainContent.setVisibility(View.VISIBLE);
            noUsersMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (apiTask != null) {
            apiTask.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("App Name");
    }

    @Override
    public void onResume() {
        super.onResume();

//        apiTask = new ApiTask(getActivity(), event.getId());
//        apiTask.execute();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(true);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAddPhone).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setOnMenuItemClickListener(this);
        }


    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvEventDetailsLocation) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + event.getLocation().getY() + "," + event.getLocation().getX()));
            startActivity(intent);
        }

        if (view.getId() == R.id.bEventDetailsRsvp) {
            PopupMenu filterMenu = new PopupMenu(getActivity(), rsvp);
            filterMenu.getMenuInflater().inflate(R.menu.popup_rsvp, filterMenu.getMenu());

            filterMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.itemGoing) {
                        rsvp.setText(menuItem.getTitle().toString());
                    }

                    if (menuItem.getItemId() == R.id.itemMaybe) {
                        rsvp.setText(menuItem.getTitle().toString());
                    }

                    if (menuItem.getItemId() == R.id.itemNotGoing) {
                        rsvp.setText(menuItem.getTitle().toString());
                    }
                    return true;
                }
            });

            filterMenu.show();
        }

        if (view.getId() == R.id.bEventDetailsChat) {

            if (EventUtils.canViewChat(event)) {

            } else {
                Toast.makeText(getActivity(), "Only people who are going/maybe to the event can view chat", Toast.LENGTH_LONG).show();
            }

            if(fragmentManager == null){
                fragmentManager = getActivity().getSupportFragmentManager();
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flMainActivity, new ChatFragment(), "Chat");
            fragmentTransaction.commit();

        }

        if (view.getId() == R.id.bEventDetailsInvite) {

            if (EventUtils.canInviteFriends(event)) {

            } else {
                Toast.makeText(getActivity(), "Only people who are going to the event can invite friends", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void ItemClicked(View view, int position) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        if (EventUtils.canEdit(event)) {

            if (eventDetails == null) {
                Toast.makeText(getActivity(), "Fetching data. Please try again in a minute", Toast.LENGTH_LONG).show();
            } else {

                if (fragmentManager == null) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EditEventFragment editEventFragment = new EditEventFragment();
                editEventFragment.setOldEvent(event, eventDetails);
                fragmentTransaction.replace(R.id.flMainActivity, editEventFragment, "Edit Event");
                fragmentTransaction.commit();
            }
        } else {

            if (event.getRsvp() != Event.RSVP.YES) {
                Toast.makeText(getActivity(), "Only people who are going to the event can edit it", Toast.LENGTH_LONG).show();
            }

            if (event.getRsvp() == Event.RSVP.YES) {

                if (event.isFinalized()) {
                    Toast.makeText(getActivity(), "The event is finalised", Toast.LENGTH_LONG).show();
                }
            }
        }
        return false;
    }

    private class ApiTask extends EventDetailsApi {
        public ApiTask(Context context, String id) {
            super(context, id);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                eventDetails = (EventDetails) o;

                description.setText(eventDetails.getDescription());

                refreshRecyclerView(eventDetails.getAttendees());

            } catch (Exception e) {

            }
        }
    }
}
