package reaper.app.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.event.EventListApi;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventDateTimeComparator;
import reaper.api.model.event.EventDistanceComparator;
import reaper.api.model.event.EventRelevanceComparator;
import reaper.app.activity.MainActivity;
import reaper.app.fragment.event.EventDetailsFragment;
import reaper.app.list.event.EventListAdapter;

/**
 * Created by reaper on 04-04-2015.
 */
public class HomeFragment extends Fragment implements EventListAdapter.EventSummaryItemClickListener, View.OnClickListener, EventListAdapter.RsvpClickListener {
    private FragmentManager fragmentManager;
    private ApiTask apiTask;

    private List<Event> eventList;

    private LinearLayout mainContent;
    private TextView noEventsMessage;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private Button filter, sort;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("APP", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEventsList);
        mainContent = (LinearLayout) view.findViewById(R.id.llHomefragmentMainContent);
        noEventsMessage = (TextView) view.findViewById(R.id.tvHomeFragmentNoEvents);
        filter = (Button) view.findViewById(R.id.bHomeFragmentFilter);
        sort = (Button) view.findViewById(R.id.bHomeFragmentSort);

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

        apiTask = new ApiTask();
        apiTask.execute();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(true);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(true);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
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

                    if (menuItem.getItemId() == R.id.itemStartTime) {
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

    }

    private class ApiTask extends EventListApi {
        public ApiTask() {
            super(HomeFragment.this.getActivity(), "Bangalore");
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                eventList = (List<Event>) o;
                refreshRecyclerView();
            } catch (Exception e) {
            }
        }
    }
}
