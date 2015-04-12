package reaper.app.fragment.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.me.MyEventListApi;
import reaper.api.model.event.Event;
import reaper.app.fragment.event.EventDetailsFragment;
import reaper.app.list.event.EventListAdapter;

/**
 * Created by Aditya on 07-04-2015.
 */
public class MyEventsFragment extends Fragment implements EventListAdapter.ClickListener
{
    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noEventsMessage;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private List<Event> eventList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvMyEventsList);
        mainContent = (LinearLayout) view.findViewById(R.id.llMyEventsMainContent);
        noEventsMessage = (TextView) view.findViewById(R.id.tvMyEventsNoEvents);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlMyEvents);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noEventsMessage.setVisibility(View.GONE);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary_material_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                apiTask = new ApiTask();
                apiTask.execute();
            }
        });

        initRecyclerView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        swipeRefreshLayout.setRefreshing(true);
        apiTask = new ApiTask();
        apiTask.execute();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if(apiTask!=null){
            apiTask.cancel(true);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getActionBar().setTitle("My Events");
    }

    private void initRecyclerView(){
        // Set adapter to recycler view
        eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListAdapter.setClickListener(this);

        recyclerView.setAdapter(eventListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView()
    {
        eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListAdapter.setClickListener(this);

        recyclerView.setAdapter(eventListAdapter);

        if (eventList.size() == 0)
        {
            noEventsMessage.setText("No events to show");
            noEventsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
        else
        {
            mainContent.setVisibility(View.VISIBLE);
            noEventsMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void itemClicked(View view, int position)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        eventDetailsFragment.setEvent(eventList.get(position));
        fragmentTransaction.replace(R.id.flMainActivity, eventDetailsFragment, "Event Details");
        fragmentTransaction.commit();
    }

    private class ApiTask extends MyEventListApi
    {
        public ApiTask()
        {
            super(MyEventsFragment.this.getActivity());
        }

        @Override
        protected void onPostExecute(Object o)
        {
            try
            {
                eventList = (List<Event>) o;
                refreshRecyclerView();
                swipeRefreshLayout.setRefreshing(false);
            }
            catch (Exception e)
            {
            }
        }
    }
}