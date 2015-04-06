package reaper.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import reaper.api.model.event.Event;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 06-04-2015.
 */
public class EventDetailsFragment extends Fragment
{
    TextView message;
    Event event;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        message = (TextView) view.findViewById(R.id.tvEventDetails);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        message.setText(event.getTitle());
    }

    public void setEvent(Event event){
        this.event = event;
    }
}
