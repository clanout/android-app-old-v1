package reaper.app.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import reaper.R;
import reaper.api.endpoints.event.EventCreateApi;
import reaper.api.model.event.EventCategory;
import reaper.api.model.event.EventType;
import reaper.app.activity.MainActivity;

/**
 * Created by Aditya on 06-04-2015.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener
{
    private ImageView eventIcon;
    private Button create;
    private EditText title, description, location, startDateTime, endDateTime, type;
    private EventType eventType;
    private EventCategory eventCategory;
    private ApiTask apiTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventIcon = (ImageView) view.findViewById(R.id.ivCreateEventIcon);
        create = (Button) view.findViewById(R.id.bCreateEventCreate);
        title = (EditText) view.findViewById(R.id.etCreateEventTitle);
        description = (EditText) view.findViewById(R.id.etCreateEventDescription);
        location = (EditText) view.findViewById(R.id.etCreateEventLocation);
        startDateTime = (EditText) view.findViewById(R.id.etCreateEventStartDateTime);
        endDateTime = (EditText) view.findViewById(R.id.etCreateEventEndDateTime);
        type = (EditText) view.findViewById(R.id.etCreateEventType);

        create.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventIcon.setImageResource(R.drawable.ic_local_bar_black_48dp);
        type.setText("PUBLIC");
    }

    @Override
    public void onPause() {
        super.onPause();

        if(apiTask != null){
            apiTask.cancel(true);
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

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
        }


    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.bCreateEventCreate){

            if((title.getText().toString() == null) || title.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Title can't be null", Toast.LENGTH_LONG).show();
                return;
            }

            if((startDateTime.getText().toString() == null) || startDateTime.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "StartDateTime can't be null", Toast.LENGTH_LONG).show();
                return;
            }

            Map<String, String> postdata = new HashMap<>();
            postdata.put("type", type.getText().toString());
            postdata.put("category", String.valueOf(eventCategory));
            postdata.put("title", title.getText().toString());
            postdata.put("description", description.getText().toString());
            postdata.put("location_latitude", "76.32");
            postdata.put("location_longitude", "26.32");
            postdata.put("location_name", "ABC");
            postdata.put("location_zone", "Bangalore");
            postdata.put("start_time", DateTime.now().toString());
            postdata.put("end_time", DateTime.now().plusHours(2).toString());

            apiTask = new ApiTask(getActivity(), postdata);
            apiTask.execute();

        }
    }

    public void setInfo(EventType eventType,EventCategory eventCategory){
        this.eventCategory = eventCategory;
        this.eventType = eventType;
    }

    private class ApiTask extends EventCreateApi
    {
        public ApiTask(Context context, Map<String, String> postData)
        {
            super(context, postData);
        }

        @Override
        protected void onPostExecute(Object o) {
             super.onPostExecute(o);

            String eventId;
            try {
                eventId = (String) o;

                if(eventId == null || eventId.isEmpty()){
                    Toast.makeText(getActivity(), "Event could not be created", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), eventId, Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                Toast.makeText(getActivity(), "Event could not be created", Toast.LENGTH_LONG).show();
            }
        }
    }
}
