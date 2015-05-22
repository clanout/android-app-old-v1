package reaper.app.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.event.LocalFriendsApi;
import reaper.api.model.event.EventDetails;
import reaper.api.model.user.User;
import reaper.app.activity.MainActivity;
import reaper.app.list.inviteusers.InviteFriendsAdapter;

/**
 * Created by harsh on 13-05-2015.
 */
public class InviteFacebookFriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout mainContent;
    private TextView noFriendsMessage;

    private InviteFriendsAdapter inviteFriendsAdapter;
    private List<User> friends;
    private List<EventDetails.Invitee> invitees;

    private LocalFriendsTask localFriendsTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_facebook_friends, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvInviteFacebookFriends);
        mainContent = (LinearLayout) view.findViewById(R.id.llInviteFacebookFriends);
        noFriendsMessage = (TextView) view.findViewById(R.id.tvInviteFacebookFriendsNoUsers);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noFriendsMessage.setVisibility(View.GONE);

        friends = new ArrayList<>();

        initRecyclerView();

    }

    @Override
    public void onResume() {
        super.onResume();

        localFriendsTask = new LocalFriendsTask(getActivity(), "Bangalore");
        localFriendsTask.execute();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAddPhone).setVisible(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (localFriendsTask != null) {
            localFriendsTask.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("Invite Friends");
    }

    public void setInvitees(List<EventDetails.Invitee> invitees){
        this.invitees = invitees;
    }

    private void initRecyclerView() {
        // Set adapter to recycler view
        inviteFriendsAdapter = new InviteFriendsAdapter(getActivity(), invitees, friends);
        recyclerView.setAdapter(inviteFriendsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView() {
        inviteFriendsAdapter = new InviteFriendsAdapter(getActivity(), invitees, friends);
        recyclerView.setAdapter(inviteFriendsAdapter);

        if (friends.size() == 0) {
            noFriendsMessage.setText("No events to show");
            noFriendsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        } else {
            mainContent.setVisibility(View.VISIBLE);
            noFriendsMessage.setVisibility(View.GONE);
        }
    }


    private class LocalFriendsTask extends LocalFriendsApi {

        public LocalFriendsTask(Context context, String zone) {
            super(context, zone);
        }

        @Override
        protected void onPostExecute(Object o) {

          try{
            if(o == null){
                friends = new ArrayList<>();
                refreshRecyclerView();
            }else {
                friends = (List<User>) o;
                refreshRecyclerView();
            }
        } catch (Exception e) {
            friends = new ArrayList<>();
            refreshRecyclerView();
        }
        }
    }
}
