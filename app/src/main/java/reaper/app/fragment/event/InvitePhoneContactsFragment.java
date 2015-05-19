package reaper.app.fragment.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import reaper.R;
import reaper.api.model.event.EventDetails;
import reaper.api.model.user.User;
import reaper.app.list.inviteusers.InviteFriendsAdapter;

/**
 * Created by harsh on 13-05-2015.
 */
public class InvitePhoneContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private InviteFriendsAdapter inviteFriendsAdapter;
    private List<User> phoneContacts;
    private List<EventDetails.Invitee> invitees;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_phone_contacts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvInvitePhoneContacts);
        inviteFriendsAdapter = new InviteFriendsAdapter(getActivity(), invitees, phoneContacts);
        recyclerView.setAdapter(inviteFriendsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

}
