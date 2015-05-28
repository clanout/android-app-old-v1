package reaper.app.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.phillipcalvin.iconbutton.IconButton;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.event.EventListApi;
import reaper.api.endpoints.event.EventUpdateRsvpApi;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventDateTimeComparator;
import reaper.api.model.event.EventDistanceComparator;
import reaper.api.model.event.EventRelevanceComparator;
import reaper.app.activity.MainActivity;
import reaper.app.fragment.event.EventDetailsFragment;
import reaper.app.list.event.EventListAdapter;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by reaper on 04-04-2015.
 */
public class HomeFragment extends Fragment implements EventListAdapter.EventSummaryItemClickListener, View.OnClickListener, EventListAdapter.RsvpClickListener {
    private FragmentManager fragmentManager;
    private EventListApiTask eventListApiTask;
    private RsvpUpdateThread rsvpUpdateThread;
    private List<Event> eventList;
    private LinearLayout mainContent;
    private TextView noEventsMessage;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private IconButton filter, sort;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEventsList);
        mainContent = (LinearLayout) view.findViewById(R.id.llHomefragmentMainContent);
        noEventsMessage = (TextView) view.findViewById(R.id.tvHomeFragmentNoEvents);
        filter = (IconButton) view.findViewById(R.id.bHomeFragmentFilter);
        sort = (IconButton) view.findViewById(R.id.bHomeFragmentSort);

        sort.setOnClickListener(this);
        filter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noEventsMessage.setVisibility(View.GONE);

        filter.setVisibility(View.GONE);
        sort.setVisibility(View.GONE);

        filter.setText("All Events");
        sort.setText("Relevance");

        fragmentManager = getActivity().getSupportFragmentManager();

        eventList = new ArrayList<>();

        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        eventListApiTask = new EventListApiTask();
        eventListApiTask.execute();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(true);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(true);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setVisible(false);

            if(AppPreferences.get(getActivity(), Constants.AppPreferenceKeys.MY_PHONE_NUMBER) == null) {
                ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAddPhone).setVisible(true);
            }else{
                ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAddPhone).setVisible(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (eventListApiTask != null) {
            eventListApiTask.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("App Name");
    }

    private void initRecyclerView() {
        // Set adapter to recycler view
        eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListAdapter.setEventSummaryItemClickListener(this);
        eventListAdapter.setRsvpClickListener(this);

        recyclerView.setAdapter(eventListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView() {
        eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListAdapter.setEventSummaryItemClickListener(this);
        eventListAdapter.setRsvpClickListener(this);

        recyclerView.setAdapter(eventListAdapter);

        if (eventList.size() == 0) {
            noEventsMessage.setText("No events to show");
            noEventsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
            filter.setVisibility(View.GONE);
            sort.setVisibility(View.GONE);
        } else {
            mainContent.setVisibility(View.VISIBLE);
            noEventsMessage.setVisibility(View.GONE);
            filter.setVisibility(View.VISIBLE);
            sort.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEventSummaryItemClicked(View view, int position) {
        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        eventDetailsFragment.setEvent(eventList.get(position));
        fragmentTransaction.replace(R.id.flMainActivity, eventDetailsFragment, "Event Details");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bHomeFragmentFilter) {
            PopupMenu filterMenu = new PopupMenu(getActivity(), filter);
            filterMenu.getMenuInflater().inflate(R.menu.popup_filter_menu, filterMenu.getMenu());

            filterMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.itemToday) {
                        filter.setText(menuItem.getTitle().toString());

                        refreshRecyclerView();
                    }

                    if (menuItem.getItemId() == R.id.itemAllEvents) {
                        filter.setText(menuItem.getTitle().toString());

                        refreshRecyclerView();
                    }
                    return true;
                }
            });

            filterMenu.show();
        }

        if (view.getId() == R.id.bHomeFragmentSort) {
            PopupMenu sortMenu = new PopupMenu(getActivity(), sort);
            sortMenu.getMenuInflater().inflate(R.menu.popup_sort_menu, sortMenu.getMenu());

            sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.itemRelevance) {
                        sort.setText(menuItem.getTitle().toString());
                        Collections.sort(eventList, new EventRelevanceComparator(0.4, 0.6));
                        refreshRecyclerView();
                    }

                    if (menuItem.getItemId() == R.id.itemStartTime) {
                        sort.setText(menuItem.getTitle().toString());
                        Collections.sort(eventList, new EventDateTimeComparator());
                        refreshRecyclerView();
                    }

                    if (menuItem.getItemId() == R.id.itemDistance) {
                        sort.setText(menuItem.getTitle().toString());
                        Collections.sort(eventList, new EventDistanceComparator(77, 23));
                        refreshRecyclerView();
                    }

                    return true;
                }
            });

            sortMenu.show();

        }
    }

    @Override
    public void onRsvpButtonClicked(View view, int buttonId, int position) {
        if(buttonId == 0){
            rsvpUpdateThread = new RsvpUpdateThread(getActivity(), eventList.get(position).getId(), Event.RSVP.YES);
            rsvpUpdateThread.start();

            eventList.get(position).setRsvp(Event.RSVP.YES);
        }

        if(buttonId == 1){
            rsvpUpdateThread = new RsvpUpdateThread(getActivity(), eventList.get(position).getId(), Event.RSVP.MAYBE);
            rsvpUpdateThread.start();

            eventList.get(position).setRsvp(Event.RSVP.MAYBE);
        }

        if(buttonId == 2){
            rsvpUpdateThread = new RsvpUpdateThread(getActivity(), eventList.get(position).getId(), Event.RSVP.NO);
            rsvpUpdateThread.start();

            eventList.get(position).setRsvp(Event.RSVP.NO);
        }
    }

    private class EventListApiTask extends EventListApi {
        public EventListApiTask() {
            super(HomeFragment.this.getActivity(), AppPreferences.get(HomeFragment.this.getActivity(), Constants.Location.ZONE));
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
               // eventList = (List<Event>) o;

                Event event1 = new Event();
                Event event2 = new Event();
                Event event3 = new Event();
                Event event4 = new Event();
                Event event5 = new Event();
                Event event6 = new Event();

                event1.setAttendeeCount(4);
                event1.setCategory("GENERAL");
                event1.setChatId("1");
                event1.setEndTime(DateTime.now().plus(3));
                event1.setFinalized(true);
                event1.setFriendCount(2);
                event1.setId("4");
                event1.setInviterCount(2);
                event1.setLastUpdated(DateTime.now());
                event1.setOrganizerId("3");
                event1.setRsvp(Event.RSVP.YES);
                event1.setStartTime(DateTime.now().minus(4));
                event1.setTitle("Music Concert at ABC at 9:00 pm tommorrow");
                event1.setType(Event.Type.PUBLIC);
                Event.Location location1 = new Event.Location();
                location1.setName("Arbor Brewing Company");
                location1.setX(32);
                location1.setY(45);
                location1.setZone("Bangalore");
                event1.setLocation(location1);

                event2.setAttendeeCount(5);
                event2.setCategory("EAT_OUT");
                event2.setChatId("2");
                event2.setEndTime(DateTime.now().plus(5));
                event2.setFinalized(false);
                event2.setFriendCount(4);
                event2.setId("5");
                event2.setInviterCount(1);
                event2.setLastUpdated(DateTime.now());
                event2.setOrganizerId("4");
                event2.setRsvp(Event.RSVP.YES);
                event2.setStartTime(DateTime.now().minus(4));
                event2.setTitle("Rock Night");
                event2.setType(Event.Type.PUBLIC);
                Event.Location location2 = new Event.Location();
                location2.setName("Vapour, IndiraNagar");
                location2.setX(32);
                location2.setY(45);
                location2.setZone("Bangalore");
                event2.setLocation(location2);

                event3.setAttendeeCount(9);
                event3.setCategory("DRINKS");
                event3.setChatId("3");
                event3.setEndTime(DateTime.now().plus(1));
                event3.setFinalized(false);
                event3.setFriendCount(5);
                event3.setId("6");
                event3.setInviterCount(0);
                event3.setLastUpdated(DateTime.now());
                event3.setOrganizerId("3");
                event3.setRsvp(Event.RSVP.MAYBE);
                event3.setStartTime(DateTime.now().minus(4));
                event3.setTitle("Party at Aditya's Place");
                event3.setType(Event.Type.INVITE_ONLY);
                Event.Location location3 = new Event.Location();
                location3.setName("Arekere, Mico Layout Bus Stand");
                location3.setX(32);
                location3.setY(45);
                location3.setZone("Bangalore");
                event3.setLocation(location3);

                event4.setAttendeeCount(3);
                event4.setCategory("CAFE");
                event4.setChatId("4");
                event4.setEndTime(DateTime.now().plus(6));
                event4.setFinalized(false);
                event4.setFriendCount(2);
                event4.setId("6");
                event4.setInviterCount(2);
                event4.setLastUpdated(DateTime.now());
                event4.setOrganizerId("6");
                event4.setRsvp(Event.RSVP.NO);
                event4.setStartTime(DateTime.now().minus(4));
                event4.setTitle("Lunch at McDonalds");
                event4.setType(Event.Type.PUBLIC);
                Event.Location location4 = new Event.Location();
                location4.setName("McDonalds, JP Nagar");
                location4.setX(32);
                location4.setY(45);
                location4.setZone("Bangalore");
                event4.setLocation(location4);

                event5.setAttendeeCount(5);
                event5.setCategory("MOVIES");
                event5.setChatId("7");
                event5.setEndTime(DateTime.now().plus(5));
                event5.setFinalized(true);
                event5.setFriendCount(4);
                event5.setId("9");
                event5.setInviterCount(1);
                event5.setLastUpdated(DateTime.now());
                event5.setOrganizerId("3");
                event5.setRsvp(Event.RSVP.MAYBE);
                event5.setStartTime(DateTime.now().minus(4));
                event5.setTitle("Fjghdsghsdgndsngodsgndsojgnsjgndsjgnkjfdgnkfngfdjngkjfdngkjjfdngkljfdngkjnfdlgknfdkljgnkjfdlngkjlfdngkjfndgkjnfdgkjjnfdgknfdklgn");
                event5.setType(Event.Type.PUBLIC);
                Event.Location location5 = new Event.Location();
                location5.setName("National Games Village, Koramangala, kjfghdshgfkdsgshgkdhfbgkfbdgkjbfdkgbfdkgjfdbgkfbgkfsbgkbfskgbsjlg");
                location5.setX(32);
                location5.setY(45);
                location5.setZone("Bangalore");
                event5.setLocation(location5);

                event6.setAttendeeCount(5);
                event6.setCategory("OUTDOORS");
                event6.setChatId("2");
                event6.setEndTime(DateTime.now().plus(5));
                event6.setFinalized(false);
                event6.setFriendCount(4);
                event6.setId("5");
                event6.setInviterCount(1);
                event6.setLastUpdated(DateTime.now());
                event6.setOrganizerId("4");
                event6.setRsvp(Event.RSVP.YES);
                event6.setStartTime(DateTime.now().minus(4));
                event6.setTitle("Rock Night at Vapour tonight");
                event6.setType(Event.Type.PUBLIC);
                Event.Location location6 = new Event.Location();
                location6.setName("");
                location6.setX(32);
                location6.setY(45);
                location6.setZone("Bangalore");
                event6.setLocation(location6);

                eventList.add(event1);
                eventList.add(event2);
                eventList.add(event3);
                eventList.add(event4);
                eventList.add(event5);
                eventList.add(event6);

                refreshRecyclerView();
            } catch (Exception e) {
            }
        }
    }

    private class RsvpUpdateThread extends EventUpdateRsvpApi {

        public RsvpUpdateThread(Context context, String eventId, Event.RSVP rsvp) {
            super(context, eventId, rsvp);
        }
    }
}
