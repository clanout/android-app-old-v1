package reaper.app.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.event.EventListApi;
import reaper.api.model.event.Event;
import reaper.app.list.event.EventListAdapter;
import reaper.common.cache.Cache;

/**
 * Created by reaper on 04-04-2015.
 */
public class HomeFragment extends Fragment implements EventListAdapter.ClickListener
{
    private FragmentManager fragmentManager;
    private ApiTask apiTask;

    private List<Event> eventList;

    private LinearLayout mainContent;
    private TextView noEventsMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private FloatingActionButton createEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEventsList);
        mainContent = (LinearLayout) view.findViewById(R.id.llHomefragmentMainContent);
        noEventsMessage = (TextView) view.findViewById(R.id.tvHomeFragmentNoEvents);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlHomeFragment);

        ImageView iconCreate = new ImageView(getActivity());
        iconCreate.setImageResource(R.drawable.ic_launcher);

        createEvent = new FloatingActionButton.Builder(getActivity())
                .setContentView(iconCreate)
                .build();


        createEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (fragmentManager == null)
                {
                    fragmentManager = getActivity().getSupportFragmentManager();
                }

                Log.d("APP", "[CLICK] Create Event");
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noEventsMessage.setVisibility(View.GONE);

        fragmentManager = getActivity().getSupportFragmentManager();

        eventList = new ArrayList<>();

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

        if (apiTask != null)
        {
            apiTask.cancel(true);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getActionBar().setTitle("Events");
    }

    private void initRecyclerView()
    {
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
        if (fragmentManager == null)
        {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
//        eventDetailsFragment.setEvent(eventList.get(position));
//        fragmentTransaction.replace(R.id.flMainActivity, eventDetailsFragment, "_Event Details");
//        fragmentTransaction.commit();

        Log.d("APP", "Event = " + eventList.get(position).getId());
    }

    private class ApiTask extends EventListApi
    {
        public ApiTask()
        {
            super(HomeFragment.this.getActivity(), "Bangalore");
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
