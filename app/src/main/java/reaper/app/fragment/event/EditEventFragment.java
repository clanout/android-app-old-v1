package reaper.app.fragment.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reaper.R;
import reaper.api.endpoints.event.EventDeleteApi;
import reaper.api.endpoints.event.EventEditApi;
import reaper.api.endpoints.event.EventSuggestionsApi;
import reaper.api.model.core.BasicJsonParser;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventCategory;
import reaper.api.model.event.EventDetails;
import reaper.api.model.event.Suggestion;
import reaper.app.activity.MainActivity;
import reaper.app.fragment.event.dialogfragment.ChangeEventCategoryTypeDialogFragment;
import reaper.app.fragment.event.dialogfragment.SelectTimeDialogFragment;
import reaper.app.fragment.event.dialogfragment.SelectedTimeCommunicator;
import reaper.app.fragment.home.HomeFragment;
import reaper.app.list.event.EventSuggestionsAdapter;
import reaper.app.service.EventUtils;
import reaper.app.service.LocationService;
import reaper.common.http.HttpRequest;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by harsh on 18-05-2015.
 */
public class EditEventFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, SelectedTimeCommunicator, EventSuggestionsAdapter.EventSuggestionsClickListener, MenuItem.OnMenuItemClickListener {

    private ImageView eventIcon;
    private TextView eventTitle, startDateTimeTextView, endDateTimeTextView, noSuggestionsMessage;
    private LinearLayout timingsWithClock, suggestionsBox;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText description;
    private RecyclerView recyclerView;
    private Button done, cancel;
    private EventSuggestionsAdapter eventSuggestionsAdapter;

    private SelectTimeDialogFragment selectTimeDialogFragment;
    private FragmentManager fragmentManager;

    private List<Suggestion> suggestionList;
    private boolean isDoneClicked;
    private boolean isPlaceDetailsTaskRunning;
    private Event.Type eventType;
    private EventCategory eventCategory;
    private String placeId;
    private Event oldEvent;
    private EventDetails oldEventDetails;

    private EditEventApiTask editEventApiTask;
    private EventSuggestionsApiTask eventSuggestionsApiTask;
    private GooglePlaceDetailsTask googlePlaceDetailsTask;
    private EventDeleteApiThread eventDeleteApiThread;

    private DateTime startDateTime, endDateTime;
    private DateTimeFormatter dateFormatter, timeFormatter;

    private static final String APIKEY = "AIzaSyDBX362r-1isovteBR3tGN3QQtDcQn-jyg";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    private String locationZone;
    private String locationName;
    private String longitude;
    private String latitude;

    private String userLatitude, userLongitude, userZone;

    private boolean isEventFinalised;

    private static ArrayList resultList, resultListPlaceId;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        eventIcon = (ImageView) view.findViewById(R.id.ivEditEventIcon);
        done = (Button) view.findViewById(R.id.bEditEventDone);
        cancel = (Button) view.findViewById(R.id.bEditEventCancel);
        eventTitle = (TextView) view.findViewById(R.id.tvEditEventTitle);
        description = (EditText) view.findViewById(R.id.etEditEventDescription);
        timingsWithClock = (LinearLayout) view.findViewById(R.id.llEditEventSelectTimeWithClock);
        startDateTimeTextView = (TextView) view.findViewById(R.id.tvEditEventStartDateTime);
        endDateTimeTextView = (TextView) view.findViewById(R.id.tvEditEventEndDateTime);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvEditEvent);
        suggestionsBox = (LinearLayout) view.findViewById(R.id.llEditEventSuggestions);
        noSuggestionsMessage = (TextView) view.findViewById(R.id.tvEditEventNoSuggestions);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.actvEditEvent);
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item_autocomplete));
        autoCompleteTextView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        suggestionsBox.setVisibility(View.VISIBLE);
        noSuggestionsMessage.setVisibility(View.GONE);

        eventIcon.setImageResource(R.drawable.ic_local_bar_black_48dp);
        eventTitle.setText(oldEvent.getTitle());

        dateFormatter = DateTimeFormat.forPattern("MMM dd");
        timeFormatter = DateTimeFormat.forPattern("HH:mm");
        startDateTime = oldEvent.getStartTime();
        endDateTime = oldEvent.getEndTime();
        startDateTimeTextView.setText(oldEvent.getStartTime().toString(timeFormatter) + ", " + oldEvent.getStartTime().toString(dateFormatter));
        endDateTimeTextView.setText(oldEvent.getEndTime().toString(timeFormatter) + ", " + oldEvent.getEndTime().toString(dateFormatter));

        autoCompleteTextView.setText(oldEvent.getLocation().getName());
        description.setText(oldEventDetails.getDescription());
        eventType = oldEvent.getType();

        try {
            eventCategory = EventCategory.valueOf(oldEvent.getCategory());
        } catch (Exception e) {
            eventCategory = EventCategory.GENERAL;
        }

        latitude = String.valueOf(oldEvent.getLocation().getY());
        longitude = String.valueOf(oldEvent.getLocation().getY());
        locationName = oldEvent.getLocation().getName();
        locationZone = oldEvent.getLocation().getZone();

        userLatitude = AppPreferences.get(getActivity(), Constants.Location.LATITUDE);
        userLongitude = AppPreferences.get(getActivity(), Constants.Location.LONGITUDE);
        userZone = AppPreferences.get(getActivity(), Constants.Location.ZONE);


        timingsWithClock.setOnClickListener(this);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        selectTimeDialogFragment = new SelectTimeDialogFragment();
        selectTimeDialogFragment.setSelectedTimeCommunicator(this);

        suggestionList = new ArrayList<>();
        isDoneClicked = false;
        isPlaceDetailsTaskRunning = false;

        fragmentManager = getActivity().getSupportFragmentManager();

        isEventFinalised = oldEvent.isFinalized();

        initRecyclerView();
    }

    public void setOldEvent(Event oldEvent, EventDetails oldEventDetails) {
        this.oldEvent = oldEvent;
        this.oldEventDetails = oldEventDetails;
    }

    private void initRecyclerView() {
        // Set adapter to recycler view
        eventSuggestionsAdapter = new EventSuggestionsAdapter(getActivity(), suggestionList);
        eventSuggestionsAdapter.setEventSuggestionsClickListener(this);

        recyclerView.setAdapter(eventSuggestionsAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView() {
        eventSuggestionsAdapter = new EventSuggestionsAdapter(getActivity(), suggestionList);
        eventSuggestionsAdapter.setEventSuggestionsClickListener(this);

        recyclerView.setAdapter(eventSuggestionsAdapter);

        if (suggestionList.size() == 0) {
            noSuggestionsMessage.setText("No suggestions to show");
            noSuggestionsMessage.setVisibility(View.VISIBLE);
            suggestionsBox.setVisibility(View.GONE);

        } else {
            suggestionsBox.setVisibility(View.VISIBLE);
            noSuggestionsMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (editEventApiTask != null) {
            editEventApiTask.cancel(true);
        }

        if (eventSuggestionsApiTask != null) {
            eventSuggestionsApiTask.cancel(true);
        }

        if (googlePlaceDetailsTask != null) {
            googlePlaceDetailsTask.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("Edit Event");
    }

    @Override
    public void onResume() {
        super.onResume();

        Map<String, String> postdata = new HashMap<>();
        postdata.put("category", String.valueOf(eventCategory));
        postdata.put("latitude", userLatitude);
        postdata.put("longitude", userLongitude);

        eventSuggestionsApiTask = new EventSuggestionsApiTask(getActivity(), postdata);
        eventSuggestionsApiTask.execute();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAddPhone).setVisible(false);

            if (EventUtils.canDeleteEvent(oldEvent)) {
                ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setVisible(true);
                ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setOnMenuItemClickListener(this);
                ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setVisible(true);
                ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setOnMenuItemClickListener(this);

            }
        }


    }


    public ArrayList autoComplete(String input) {

        HttpURLConnection httpURLConnection = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder stringBuilder = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            stringBuilder.append("?key=" + APIKEY);
            stringBuilder.append("&location=" + userLatitude + "," + userLongitude);
            stringBuilder.append("&type=food");
            stringBuilder.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(stringBuilder.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = inputStreamReader.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (Exception e) {
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.d("APP", predsJsonArray.toString());
            // Extract the Place descriptions from the results
            resultListPlaceId = new ArrayList<>(predsJsonArray.length());
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }

            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultListPlaceId.add(predsJsonArray.getJSONObject(i).getString("place_id"));
            }
        } catch (JSONException e) {
        }
        return resultList;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bEditEventDone) {

            if (isPlaceDetailsTaskRunning) {
                isDoneClicked = true;
            } else {
                isDoneClicked = false;
                sendEditEventRequest();
            }

        }

        if (view.getId() == R.id.bEditEventCancel) {

            if (fragmentManager == null) {
                fragmentManager = getActivity().getSupportFragmentManager();
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
            eventDetailsFragment.setEvent(oldEvent);
            fragmentTransaction.replace(R.id.flMainActivity, eventDetailsFragment, "Event Details");
            fragmentTransaction.commit();

        }

        if (view.getId() == R.id.llEditEventSelectTimeWithClock) {

            if (fragmentManager == null) {
                fragmentManager = getActivity().getSupportFragmentManager();
            }

            selectTimeDialogFragment.show(fragmentManager, "Select time");

        }

    }


    @Override
    public void setTime(DateTime startDateTime, DateTime endDateTime) {

        if (startDateTime == null || endDateTime == null) {
            return;
        } else {

            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;

            startDateTimeTextView.setText(startDateTime.toString(timeFormatter) + ", " + startDateTime.toString(dateFormatter));
            endDateTimeTextView.setText(endDateTime.toString(timeFormatter) + ", " + endDateTime.toString(dateFormatter));

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        placeId = (String) resultListPlaceId.get(position);
        String url = PLACE_DETAILS_URL + "placeid=" + placeId + "&key=" + APIKEY;

        googlePlaceDetailsTask = new GooglePlaceDetailsTask(url);
        isPlaceDetailsTaskRunning = true;
        googlePlaceDetailsTask.execute();

    }

    private void sendEditEventRequest() {

        if ((eventTitle.getText().toString() == null) || eventTitle.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Title can't be null", Toast.LENGTH_LONG).show();
            return;
        }

        if (startDateTimeTextView.getText().toString() == null || startDateTimeTextView.getText().toString().isEmpty() || endDateTimeTextView.getText().toString() == null || endDateTimeTextView.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Timings can't be null", Toast.LENGTH_LONG).show();
            return;
        }

        if (latitude == null || latitude.isEmpty() || longitude == null || longitude.isEmpty()) {

            locationZone = userZone;
            latitude = null;
            longitude = null;
            locationName = null;
        } else {

            LocationService locationService = new LocationService(getActivity());
            locationZone = locationService.getZoneFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }

        Map<String, String> postdata = new HashMap<>();
        postdata.put("event_id", String.valueOf(oldEvent.getId()));

            postdata.put("description", description.getText().toString());

        if (!String.valueOf(oldEvent.getLocation().getY()).equals(latitude) || !String.valueOf(oldEvent.getLocation().getX()).equals(longitude)) {
            postdata.put("location_latitude", latitude);
            postdata.put("location_longitude", longitude);
            postdata.put("location_name", locationName);
            postdata.put("location_zone", locationZone);
        }

        if (startDateTime != oldEvent.getStartTime() || endDateTime != oldEvent.getEndTime()) {
            postdata.put("start_time", startDateTime.toString());
            postdata.put("end_time", endDateTime.toString());
        }

        if (oldEvent.isFinalized() != isEventFinalised) {
            postdata.put("is_finalised", String.valueOf(isEventFinalised));
        }

        editEventApiTask = new EditEventApiTask(getActivity(), postdata);
        editEventApiTask.execute();


    }


    @Override
    public void onEventSuggestionsClicked(View view, int position) {
        autoCompleteTextView.setText(suggestionList.get(position).getName());

        locationName = suggestionList.get(position).getName();
        latitude = suggestionList.get(position).getLatitude();
        longitude = suggestionList.get(position).getLongitude();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.abbFinaliseEvent) {

            if (isEventFinalised) {
                isEventFinalised = false;
                Toast.makeText(getActivity(), "The event is now not finalised", Toast.LENGTH_LONG).show();
            } else {
                isEventFinalised = true;
                Toast.makeText(getActivity(), "The event is now finalised", Toast.LENGTH_LONG).show();
            }
        }

        if (item.getItemId() == R.id.abbDeleteEvent) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            eventDeleteApiThread = new EventDeleteApiThread(getActivity(), oldEvent.getId());
                            eventDeleteApiThread.start();

                            if (fragmentManager == null) {
                                fragmentManager = getActivity().getSupportFragmentManager();
                            }

                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flMainActivity, new HomeFragment(), "Home");
                            fragmentTransaction.commit();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return false;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autoComplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }

    private class EventSuggestionsApiTask extends EventSuggestionsApi {

        public EventSuggestionsApiTask(Context context, Map<String, String> postData) {
            super(context, postData);
        }

        @Override
        protected void onPostExecute(Object o) {
            try {

                if (o == null) {
                    suggestionList = new ArrayList<>();
                    refreshRecyclerView();
                } else {
                    suggestionList = (List<Suggestion>) o;
                    refreshRecyclerView();
                }
            } catch (Exception e) {
                suggestionList = new ArrayList<>();
                refreshRecyclerView();
            }
        }
    }

    private class GooglePlaceDetailsTask extends AsyncTask<Void, Void, String> {

        private String url;

        private GooglePlaceDetailsTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpRequest httpRequest = new HttpRequest();

            try {
                String jsonResponse = httpRequest.sendGetRequest(url);

                String status = BasicJsonParser.getValue(jsonResponse, "status");

                if (status.equalsIgnoreCase("OK")) {
                    return jsonResponse;
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s == null) {
                locationName = null;
                latitude = null;
                longitude = null;
                locationZone = null;
            } else {

                try {
                    String result = BasicJsonParser.getValue(s, "result");

                    locationName = BasicJsonParser.getValue(result, "name");

                    String location = BasicJsonParser.getValue(BasicJsonParser.getValue(result, "geometry"), "location");
                    latitude = BasicJsonParser.getValue(location, "lat");
                    longitude = BasicJsonParser.getValue(location, "lng");
                } catch (Exception e) {
                    locationName = null;
                    latitude = null;
                    longitude = null;
                    locationZone = null;
                }
            }

            isPlaceDetailsTaskRunning = false;

            if (isDoneClicked) {
                sendEditEventRequest();
            }
        }
    }

    private class EditEventApiTask extends EventEditApi {
        public EditEventApiTask(Context context, Map<String, String> postData) {
            super(context, postData);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Event event;
            try {
                event = (Event) o;

                if (event == null) {
                    Toast.makeText(getActivity(), "Event could not be edited", Toast.LENGTH_LONG).show();
                } else {

                    if (fragmentManager == null) {
                        fragmentManager = getActivity().getSupportFragmentManager();
                    }

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    eventDetailsFragment.setEvent(event);
                    fragmentTransaction.replace(R.id.flMainActivity, eventDetailsFragment, "Details Event");
                    fragmentTransaction.commit();
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Event could not be created", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class EventDeleteApiThread extends EventDeleteApi {

        public EventDeleteApiThread(Context context, String eventId) {
            super(context, eventId);
        }
    }
}
