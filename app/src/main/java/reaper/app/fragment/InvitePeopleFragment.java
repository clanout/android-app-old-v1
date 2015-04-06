package reaper.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reaper.api.model.user.User;
import reaper.app.list.inviteusers.InviteUsersListAdapter;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 06-04-2015.
 */
public class InvitePeopleFragment extends Fragment implements View.OnClickListener
{
    private LinearLayout mainContent;
    private TextView noRequestsMessage;

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
        noRequestsMessage = (TextView) view.findViewById(R.id.tvInvitePeopleNoFriends);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlInvitePeopleFragment);

        inviteUsersListAdapter = new InviteUsersListAdapter(getActivity(), userList);
        recyclerView.setAdapter(inviteUsersListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        invite.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view)
    {

    }
}
