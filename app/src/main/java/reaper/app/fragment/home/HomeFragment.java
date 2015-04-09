package reaper.app.fragment.home;

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

import java.util.ArrayList;
import java.util.List;

import reaper.api.event.EventListApi;
import reaper.api.model.event.Event;
import reaper.app.fragment.event.EventDetailsFragment;
import reaper.app.list.event.EventListAdapter;
import reaper.local.reaper.R;

/**
 * Created by reaper on 04-04-2015.
 */
public class HomeFragment extends Fragment implements EventListAdapter.ClickListener
{
    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noEventsMessage;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private List<Event> eventList = new ArrayList<>();

    private Button pickDate, pickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEventsList);
        pickDate = (Button) view.findViewById(R.id.bDatePicker);
        pickTime = (Button) view.findViewById(R.id.bTimePicker);
        mainContent = (LinearLayout) view.findViewById(R.id.llHomefragmentMainContent);
        noEventsMessage = (TextView) view.findViewById(R.id.tvHomeFragmentNoEvents);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlHomeFragment);

        ImageView iconCreate = new ImageView(getActivity());
        iconCreate.setImageResource(R.drawable.ic_launcher);

        FloatingActionButton createEvent = new FloatingActionButton.Builder(getActivity())
                .setContentView(iconCreate)
                .build();


        createEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(), "FAB Create was clicked", Toast.LENGTH_LONG).show();
            }
        });


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
        noEventsMessage.setVisibility(View.GONE);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary_material_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                apiTask = new ApiTask();
                apiTask.enableRefreshing();
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        eventDetailsFragment.setEvent(eventList.get(position));
        fragmentTransaction.replace(R.id.flMainActivity, eventDetailsFragment, "Event Details");
        fragmentTransaction.commit();
    }

    public void showTimePicker()
    {
        // Inflate your custom layout containing 2 TimePickers
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_time_picker, null);

        // Define your time pickers
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
