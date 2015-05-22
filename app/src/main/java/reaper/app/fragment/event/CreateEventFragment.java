package reaper.app.fragment.event;

import android.content.Context;
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
import reaper.api.endpoints.event.EventCreateApi;
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
import reaper.app.list.event.EventSuggestionsAdapter;
import reaper.app.service.LocationService;
import reaper.common.http.HttpRequest;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 06-04-2015.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener, SelectedTimeCommunicator, AdapterView.OnItemClickListener, EventSuggestionsAdapter.EventSuggestionsClickListener, EventCategoryTypeCommunicator {

    private ImageView eventIcon;
    private Button create;
    private EditText title, description;
    private LinearLayout timings, timingsWithClock;
    private Event.Type eventType;
    private EventCategory eventCategory;
    private TextView startDateTimeTextView, endDateTimeTextView;
    private RecyclerView recyclerView;
    private EventSuggestionsAdapter eventSuggestionsAdapter;
    private List<Suggestion> suggestionList;

    private LinearLayout suggestionsBox;
    private TextView noSuggestionsMessage;

    private FragmentManager fragmentManager;
    private CreateEventApiTask createEventApiTask;
    private EventSuggestionsApiTask eventSuggestionsApiTask;

    private DateTime startDateTime, endDateTime;
    private SelectTimeDialogFragment selectTimeDialogFragment;
    private AutoCompleteTextView autoCompleteTextView;
    private String placeId;

    private boolean isPlaceDetailsTaskRunning, isCreateEventButtonClicked;

    private static final String APIKEY = "AIzaSyDBX362r-1isovteBR3tGN3QQtDcQn-jyg";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    private static ArrayList resultList, resultListPlaceId;
    private GooglePlaceDetailsTask googlePlaceDetailsTask;

    private String locationZone;
    private String locationName;
    private String longitude;
    private String latitude;

    private static String userLatitude, userLongitude, userZone;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventIcon = (ImageView) view.findViewById(R.id.ivCreateEventIcon);
        create = (Button) view.findViewById(R.id.bCreateEventCreate);
        title = (EditText) view.findViewById(R.id.etCreateEventTitle);
        description = (EditText) view.findViewById(R.id.etCreateEventDescription);
        timings = (LinearLayout) view.findViewById(R.id.llCreateEventSelectTime);
        timingsWithClock = (LinearLayout) view.findViewById(R.id.llCreateEventSelectTimeWithClock);
        startDateTimeTextView = (TextView) view.findViewById(R.id.tvCreateEventStartDateTime);
        endDateTimeTextView = (TextView) view.findViewById(R.id.tvCreateEventEndDateTime);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvCreateEvent);
        suggestionsBox = (LinearLayout) view.findViewById(R.id.llCreateEventSuggestions);
        noSuggestionsMessage = (TextView) view.findViewById(R.id.tvCreateEventNoSuggestions);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.actvCreateEvent);
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item_autocomplete));
        autoCompleteTextView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        suggestionsBox.setVisibility(View.VISIBLE);
        noSuggestionsMessage.setVisibility(View.GONE);

        eventIcon.setOnClickListener(this);
        timings.setOnClickListener(this);
        timingsWithClock.setOnClickListener(this);
        create.setOnClickListener(this);

        eventIcon.setImageResource(R.drawable.ic_local_bar_black_48dp);
        timingsWithClock.setVisibility(View.GONE);

        selectTimeDialogFragment = new SelectTimeDialogFragment();
        selectTimeDialogFragment.setSelectedTimeCommunicator(this);

        suggestionList = new ArrayList<>();
        isCreateEventButtonClicked = false;
        isPlaceDetailsTaskRunning = false;

        fragmentManager = getActivity().getSupportFragmentManager();

        userLatitude = AppPreferences.get(getActivity(), Constants.Location.LATITUDE);
        userLongitude = AppPreferences.get(getActivity(), Constants.Location.LONGITUDE);
        userZone = AppPreferences.get(getActivity(), Constants.Location.ZONE);

        initRecyclerView();
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

        if (createEventApiTask != null) {
            createEventApiTask.cancel(true);
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
        getActivity().getActionBar().setTitle("Create Event");
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
        }


    }

    public ArrayList autoComplete(String input) {

        HttpURLConnection httpURLConnection = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder stringBuilder = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            stringBuilder.append("?key=" + APIKEY);
            stringBuilder.append("&location=" + userLatitude + "," + userLongitude);
            stringBuilder.append("&radius=10000");
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
    public void onEventSuggestionsClicked(View view, int position) {

        autoCompleteTextView.setText(suggestionList.get(position).getName());

        locationName = suggestionList.get(position).getName();
        latitude = suggestionList.get(position).getLatitude();
        longitude = suggestionList.get(position).getLongitude();
    }

    @Override
    public void onEventCategoryTypeChanged(Event.Type eventType, EventCategory eventCategory) {

        this.eventType = eventType;
        this.eventCategory = eventCategory;

        Map<String, String> postdata = new HashMap<>();
        postdata.put("category", String.valueOf(this.eventCategory));
        postdata.put("latitude", userLatitude);
        postdata.put("longitude", userLongitude);

        eventSuggestionsApiTask = new EventSuggestionsApiTask(getActivity(), postdata);
        eventSuggestionsApiTask.execute();
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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bCreateEventCreate) {

            if (isPlaceDetailsTaskRunning) {
                isCreateEventButtonClicked = true;
            } else {
                isCreateEventButtonClicked = false;
                sendCreateEventRequest();
            }

        }

        if (view.getId() == R.id.llCreateEventSelectTime) {

            if (fragmentManager == null) {
                fragmentManager = getActivity().getSupportFragmentManager();
            }

            selectTimeDialogFragment.show(fragmentManager, "Select time");

        }

        if (view.getId() == R.id.llCreateEventSelectTimeWithClock) {

            if (fragmentManager == null) {
                fragmentManager = getActivity().getSupportFragmentManager();
            }

            selectTimeDialogFragment.show(fragmentManager, "Select time");

        }

        if(view.getId() == R.id.ivCreateEventIcon){

            if(eventSuggestionsApiTask != null){
                eventSuggestionsApiTask.cancel(true);
            }

            ChangeEventCategoryTypeDialogFragment changeEventCategoryTypeDialogFragment = new ChangeEventCategoryTypeDialogFragment();
            changeEventCategoryTypeDialogFragment.setEventCategoryTypeCommunicator(this);
            changeEventCategoryTypeDialogFragment.setOldCategoryType(eventType, eventCategory);


            if(fragmentManager == null){
                fragmentManager = getActivity().getSupportFragmentManager();
            }
            changeEventCategoryTypeDialogFragment.show(fragmentManager,"Change Category Type");
        }
    }

    public void setInfo(Event.Type eventType, EventCategory eventCategory) {
        this.eventCategory = eventCategory;
        this.eventType = eventType;
    }

    @Override
    public void setTime(DateTime startDateTime, DateTime endDateTime) {

        if (startDateTime == null || endDateTime == null) {
            return;
        } else {

            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;

            timings.setVisibility(View.GONE);
            timingsWithClock.setVisibility(View.VISIBLE);

            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd");
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

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

    private void sendCreateEventRequest() {

        if ((title.getText().toString() == null) || title.getText().toString().isEmpty()) {
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

            try {
                locationZone = locationService.getZoneFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }catch(Exception e){
                locationZone = userZone;
            }
        }

        Map<String, String> postdata = new HashMap<>();
        postdata.put("type", String.valueOf(eventType));
        postdata.put("category", String.valueOf(eventCategory));
        postdata.put("title", title.getText().toString());
        postdata.put("description", description.getText().toString());
        postdata.put("location_latitude", latitude);
        postdata.put("location_longitude", longitude);
        postdata.put("location_name", locationName);
        postdata.put("location_zone", locationZone);
        postdata.put("start_time", startDateTime.toString());
        postdata.put("end_time", endDateTime.toString());

        createEventApiTask = new CreateEventApiTask(getActivity(), postdata);
        createEventApiTask.execute();


    }

    private class CreateEventApiTask extends EventCreateApi {
        public CreateEventApiTask(Context context, Map<String, String> postData) {
            super(context, postData);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            String eventId;
            try {
                eventId = (String) o;

                if (eventId == null || eventId.isEmpty()) {
                    Toast.makeText(getActivity(), "Event could not be created", Toast.LENGTH_LONG).show();
                } else {

                    if (fragmentManager == null) {
                        fragmentManager = getActivity().getSupportFragmentManager();
                    }

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    InviteUsersFragment inviteUsersFragment = new InviteUsersFragment();
                    inviteUsersFragment.setEventId(eventId);
                    inviteUsersFragment.setInvitees(new ArrayList<EventDetails.Invitee>());
                    fragmentTransaction.replace(R.id.flMainActivity, inviteUsersFragment, "Invite Users");
                    fragmentTransaction.commit();
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Event could not be created", Toast.LENGTH_LONG).show();
            }
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

                if(status.equalsIgnoreCase("OK")){
                    return  jsonResponse;
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(s == null){
                locationName = null;
                latitude = null;
                longitude = null;
                locationZone = null;
            }else{

                try {
                    String result = BasicJsonParser.getValue(s, "result");

                    locationName = BasicJsonParser.getValue(result, "name");

                    String location = BasicJsonParser.getValue(BasicJsonParser.getValue(result, "geometry"), "location");
                    latitude = BasicJsonParser.getValue(location, "lat");
                    longitude = BasicJsonParser.getValue(location, "lng");
                }catch(Exception e){
                    locationName = null;
                    latitude = null;
                    longitude = null;
                    locationZone = null;
                }
            }

            isPlaceDetailsTaskRunning = false;

            if(isCreateEventButtonClicked){
                sendCreateEventRequest();
            }
        }
    }
}
