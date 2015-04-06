package reaper.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import reaper.api.event.EventListApi;
import reaper.api.model.event.Event;
import reaper.app.list.event.EventListAdapter;
import reaper.local.reaper.R;

/**
 * Created by reaper on 04-04-2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, EventListAdapter.ClickListener
{
    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noRequestsMessage;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView eventList;
    private EventListAdapter eventListAdapter;
    private List<Event> eventList = new ArrayList<>();

    private Button pickDate, pickTime;

    private static final String TAG_FILTER_LOCATION = "filter_location";
    private static final String TAG_FILTER_TIME = "filter_time";
    private static final String TAG_FILTER_DATE = "filter_date";
    private static final String TAG_FILTER_MY_EVENTS = "filter_my_events";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        eventList = (RecyclerView) view.findViewById(R.id.rvEventsList);
        pickDate = (Button) view.findViewById(R.id.bDatePicker);
        pickTime = (Button) view.findViewById(R.id.bTimePicker);
        mainContent = (LinearLayout) view.findViewById(R.id.llHomefragmentMainContent);
        noRequestsMessage = (TextView) view.findViewById(R.id.tvHomeFragmentNoEvents);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlHomeFragment);

        //Floating Action Button for filter
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_action_new);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(imageView)
                .build();

        //Filter icons for location, time, date, myevents
        ImageView imageViewLocation = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_action_new);

        ImageView imageViewTime = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_action_important);

        ImageView imageViewDate = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_action_accept);

        ImageView imageViewMyEvents = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_launcher);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        SubActionButton filterLocation = itemBuilder.setContentView(imageViewLocation).build();
        SubActionButton filterTime = itemBuilder.setContentView(imageViewTime).build();
        SubActionButton filterDate = itemBuilder.setContentView(imageViewDate).build();
        SubActionButton filterMyEvents = itemBuilder.setContentView(imageViewMyEvents).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(filterLocation)
                .addSubActionView(filterTime)
                .addSubActionView(filterDate)
                .addSubActionView(filterMyEvents)
                .attachTo(actionButton)
                .build();

        filterLocation.setTag(TAG_FILTER_LOCATION);
        filterDate.setTag(TAG_FILTER_DATE);
        filterTime.setTag(TAG_FILTER_TIME);
        filterMyEvents.setTag(TAG_FILTER_MY_EVENTS);

        filterLocation.setOnClickListener(this);
        filterTime.setOnClickListener(this);
        filterDate.setOnClickListener(this);
        filterMyEvents.setOnClickListener(this);

        pickDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
                calendarDialogFragment.show(fragmentManager, "Calendar");
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showTimePicker();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noRequestsMessage.setVisibility(View.GONE);

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
        getActivity().getActionBar().setTitle("Events");
    }

    private void initRecyclerView(){
        // Set adapter to recycler view
        eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListAdapter.setClickListener(this);

        eventList.setAdapter(eventListAdapter);

        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView()
    {
        eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListAdapter.setClickListener(this);

        eventList.setAdapter(eventListAdapter);

        if (eventList.size() == 0)
        {
            noRequestsMessage.setText("No events to show");
            noRequestsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
        else
        {
            mainContent.setVisibility(View.VISIBLE);
            noRequestsMessage.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v)
    {

        if (v.getTag().equals(TAG_FILTER_DATE))
        {
            Toast.makeText(getActivity(), TAG_FILTER_DATE, Toast.LENGTH_LONG).show();
        }
        if (v.getTag().equals(TAG_FILTER_LOCATION))
        {
            Toast.makeText(getActivity(), TAG_FILTER_LOCATION, Toast.LENGTH_LONG).show();
        }
        if (v.getTag().equals(TAG_FILTER_TIME))
        {
            Toast.makeText(getActivity(), TAG_FILTER_TIME, Toast.LENGTH_LONG).show();
        }
        if (v.getTag().equals(TAG_FILTER_MY_EVENTS))
        {
            Toast.makeText(getActivity(), TAG_FILTER_MY_EVENTS, Toast.LENGTH_LONG).show();
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

    public void showTimePicker()
    {
        // Inflate your custom layout containing 2 DatePickers
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_time_picker, null);

        // Define your date pickers
        final TimePicker tpStartTime = (TimePicker) customView.findViewById(R.id.tpStartTime);
        final TimePicker tpEndTime = (TimePicker) customView.findViewById(R.id.tpEndTime);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customView); // Set the view of the dialog to your custom layout
        builder.setTitle("Select start and end time");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        builder.create().show();
    }

    private class ApiTask extends EventListApi
    {
        public ApiTask()
        {
            super(HomeFragment.this.getActivity());
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
