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

import java.util.HashMap;
import java.util.Map;

import reaper.api.event.EventCreateApi;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 06-04-2015.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener
{
    private ImageView eventIcon;
    private Button cancel, create, lock;
    private EditText title, description, location, time, date, eventType;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventIcon = (ImageView) view.findViewById(R.id.ivCreateEventIcon);
        cancel = (Button) view.findViewById(R.id.bCreateEventCancel);
        create = (Button) view.findViewById(R.id.bCreateEventCreate);
        lock = (Button) view.findViewById(R.id.bCreateEventLock);
        title = (EditText) view.findViewById(R.id.etCreateEventTitle);
        description = (EditText) view.findViewById(R.id.etCreateEventDescription);
        location = (EditText) view.findViewById(R.id.etCreateEventLocation);
        time = (EditText) view.findViewById(R.id.etCreateEventTime);
        date = (EditText) view.findViewById(R.id.etCreateEventDate);
        eventType = (EditText) view.findViewById(R.id.etCreateEventType);

        cancel.setOnClickListener(this);
        create.setOnClickListener(this);
        lock.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.bCreateEventCancel){

        }
        if(view.getId() == R.id.bCreateEventCreate){
            Map<String, String> postData = new HashMap<>();

            ApiThread apiThread = new ApiThread(getActivity(), postData);
            apiThread.start();

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.flMainActivity, new InviteUsersFragment(), "Invite users");
            fragmentTransaction.commit();
        }

        if(view.getId() == R.id.bCreateEventLock){
            Toast.makeText(getActivity(), "This event has been locked", Toast.LENGTH_LONG).show();
        }
    }

    private class ApiThread extends EventCreateApi
    {
        public ApiThread(Context context, Map<String, String> postData)
        {
            super(context, postData);
        }
    }
}
