package reaper.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import reaper.local.reaper.R;
import reaper.list.event.EventListAdapter;
import reaper.list.event.EventListItem;

/**
 * Created by reaper on 04-04-2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, EventListAdapter.ClickListener{

    private RecyclerView eventList;
    private Button pickDate, pickTime;
    private EventListAdapter eventListAdapter;
    private static final String TAG_FILTER_LOCATION = "filter_location";
    private static final String TAG_FILTER_TIME = "filter_time";
    private static final String TAG_FILTER_DATE = "filter_date";
    private static final String TAG_FILTER_MY_EVENTS = "filter_my_events";
    List<EventListItem> eventListData = new ArrayList<EventListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        eventList = (RecyclerView) view.findViewById(R.id.rvEventsList);
        pickDate = (Button) view.findViewById(R.id.bDatePicker);
        pickTime = (Button) view.findViewById(R.id.bTimePicker);

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

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
                calendarDialogFragment.show(fragmentManager, "Calendar");
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Set adapter to recycler view
        eventListAdapter = new EventListAdapter(getActivity(), getEventList());
        eventListAdapter.setClickListener(this);
        eventList.setAdapter(eventListAdapter);

        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private List<EventListItem> getEventList(){
        EventListItem eventListItem = new EventListItem();
        eventListItem.title = "Pub Crawl";
        eventListItem.location = "Koramangala";
        eventListItem.attendees = "11 friends are going";
        eventListItem.date = "9 April";
        eventListItem.time = "5pm - 11pm";
        eventListItem.eventIconId = R.mipmap.ic_action_important;
        eventListItem.statusIconId = R.mipmap.ic_action_accept;
        eventListData.add(eventListItem);
        return eventListData;
    }

    @Override
    public void onClick(View v) {

        if(v.getTag().equals(TAG_FILTER_DATE)){
            Toast.makeText(getActivity(), TAG_FILTER_DATE, Toast.LENGTH_LONG).show();
        }
        if(v.getTag().equals(TAG_FILTER_LOCATION)){
            Toast.makeText(getActivity(), TAG_FILTER_LOCATION, Toast.LENGTH_LONG).show();
        }
        if(v.getTag().equals(TAG_FILTER_TIME)){
            Toast.makeText(getActivity(), TAG_FILTER_TIME, Toast.LENGTH_LONG).show();
        }
        if(v.getTag().equals(TAG_FILTER_MY_EVENTS)){
            Toast.makeText(getActivity(), TAG_FILTER_MY_EVENTS, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        Toast.makeText(getActivity(), "item at " + position + " was clicked", Toast.LENGTH_LONG).show();
    }

    public void showTimePicker() {
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        builder.create().show();
    }
}
