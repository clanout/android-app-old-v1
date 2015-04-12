package reaper.app.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.model.event.Event;
import reaper.api.model.user.User;
import reaper.api.model.user.UserEvents;
import reaper.api.user.UserEventsApi;
import reaper.app.list.user.UserDetailsEventListAdapter;

/**
 * Created by Aditya on 07-04-2015.
 */
public class UserDetailsFragment extends Fragment
{
    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noEventsMessage;

    User user;
    UserEvents userEvents;

    private ImageView profilePicture;
    private TextView name;
    private RecyclerView recyclerView;

    private UserDetailsEventListAdapter userDetailsEventListAdapter;

    List<Event> userEventList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        profilePicture = (ImageView) view.findViewById(R.id.ivUserDetailsIcon);
        name = (TextView) view.findViewById(R.id.tvUserDetailsName);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvUserDetailsEventList);
        mainContent = (LinearLayout) view.findViewById(R.id.lluserDetailsMainContent);
        noEventsMessage = (TextView) view.findViewById(R.id.tvUserDetailsNoEvents);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noEventsMessage.setVisibility(View.GONE);

        apiTask = new ApiTask(getActivity(), user.getId());
        apiTask.execute();

        profilePicture.setImageResource(user.getProfilePic());
        name.setText(user.getFirstname() + " " + user.getLastname());

        initRecyclerView();

    }

    private void initRecyclerView()
    {
        userDetailsEventListAdapter = new UserDetailsEventListAdapter(getActivity(), userEventList);
        recyclerView.setAdapter(userDetailsEventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView(List<Event> userEventList)
    {
        userDetailsEventListAdapter = new UserDetailsEventListAdapter(getActivity(), userEventList);

        recyclerView.setAdapter(userDetailsEventListAdapter);

        if (userEventList.size() == 0)
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
        getActivity().getActionBar().setTitle(user.getFirstname() + " " + user.getLastname());
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    private class ApiTask extends UserEventsApi
    {
        public ApiTask(Context context, String id)
        {
            super(context, id);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);

            try
            {
                userEvents = (UserEvents) o;

                userEventList = new ArrayList<>();
                for(Event event : userEvents.getGoingEvents()){
                    userEventList.add(event);
                }

                for(Event event : userEvents.getMaybeEvents()){
                    userEventList.add(event);
                }

                refreshRecyclerView(userEventList);
            }
            catch (Exception e)
            {

            }
        }
    }
}

