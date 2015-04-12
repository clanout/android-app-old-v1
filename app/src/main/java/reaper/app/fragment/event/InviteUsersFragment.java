package reaper.app.fragment.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.me.FriendListApi;
import reaper.api.model.user.User;
import reaper.app.list.inviteusers.InviteUsersListAdapter;

/**
 * Created by Aditya on 06-04-2015.
 */
public class InviteUsersFragment extends Fragment implements View.OnClickListener, InviteUsersListAdapter.ClickListener
{
    private ApiTask apiTask;

    private LinearLayout mainContent;
    private TextView noFriendsMessage;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private InviteUsersListAdapter inviteUsersListAdapter;

    private List<User> userList = new ArrayList<>();

    private Button invite, skip;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_invite_people, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvInvitePeopleList);
        invite = (Button) view.findViewById(R.id.bInvitePeople);
        skip = (Button) view.findViewById(R.id.bSkipInvitePeople);
        mainContent = (LinearLayout) view.findViewById(R.id.llInvitePeopleFragmentMainContent);
        noFriendsMessage = (TextView) view.findViewById(R.id.tvInvitePeopleNoFriends);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlInvitePeopleFragment);

        inviteUsersListAdapter = new InviteUsersListAdapter(getActivity(), userList);
        recyclerView.setAdapter(inviteUsersListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        invite.setOnClickListener(this);
        skip.setOnClickListener(this);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noFriendsMessage.setVisibility(View.GONE);

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

        if (apiTask != null)
        {
            apiTask.cancel(true);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getActionBar().setTitle("Invite Users");
    }

    private void initRecyclerView()
    {
        // Set adapter to recycler view
        inviteUsersListAdapter = new InviteUsersListAdapter(getActivity(), userList);
        inviteUsersListAdapter.setClickListener(this);

        recyclerView.setAdapter(inviteUsersListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView()
    {
        inviteUsersListAdapter = new InviteUsersListAdapter(getActivity(), userList);
        inviteUsersListAdapter.setClickListener(this);

        recyclerView.setAdapter(inviteUsersListAdapter);

        if (userList.size() == 0)
        {
            noFriendsMessage.setText("No friends to show");
            noFriendsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
        else
        {
            mainContent.setVisibility(View.VISIBLE);
            noFriendsMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view)
    {

    }

    @Override
    public void itemClicked(View view, int position)
    {

    }

    private class ApiTask extends FriendListApi
    {
        public ApiTask()
        {
            super(InviteUsersFragment.this.getActivity());
        }

        @Override
        protected void onPostExecute(Object o)
        {
            try
            {
                userList = (List<User>) o;
                refreshRecyclerView();
                swipeRefreshLayout.setRefreshing(false);
            }
            catch (Exception e)
            {
            }
        }
    }
}
