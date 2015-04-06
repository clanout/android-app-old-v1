package reaper.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import reaper.local.reaper.R;

/**
 * Created by Aditya on 06-04-2015.
 */
public class CreateEventFragment extends Fragment
{
    TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        message = (TextView) view.findViewById(R.id.tvCreateEvent);
        return view;
    }
}
