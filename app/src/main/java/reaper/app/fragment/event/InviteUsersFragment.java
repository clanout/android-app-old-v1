package reaper.app.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.event.InviteUsersApi;
import reaper.api.model.event.EventDetails;
import reaper.app.activity.MainActivity;
import reaper.app.fragment.home.HomeFragment;

/**
 * Created by harsh on 13-05-2015.
 */
public class InviteUsersFragment extends Fragment implements View.OnClickListener {

    private InviteFriendsPagerAdapter inviteFriendsPagerAdapter;
    private ViewPager viewPager;
    private Button invite, skip;
    private InviteFacebookFriendsFragment inviteFacebookFriendsFragment;
    private InvitePhoneContactsFragment invitePhoneContactsFragment;

    private FragmentManager fragmentManager;

    private InviteUsersThread inviteUsersThread;

    private String eventId;
    private List<EventDetails.Invitee> invitees;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_users, container, false);

        inviteFriendsPagerAdapter = new InviteFriendsPagerAdapter(getChildFragmentManager());

        invite = (Button) view.findViewById(R.id.bInviteUsersFragmentInvite);
        skip = (Button) view.findViewById(R.id.bInviteUsersFragmentSkip);
        viewPager = (ViewPager) view.findViewById(R.id.vpInviteUsers);
        viewPager.setAdapter(inviteFriendsPagerAdapter);

        inviteFacebookFriendsFragment = new InviteFacebookFriendsFragment();
        invitePhoneContactsFragment = new InvitePhoneContactsFragment();

        invite.setOnClickListener(this);
        skip.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();

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
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("Invite Friends");
    }

    public void setInvitees(List<EventDetails.Invitee> invitees){
        this.invitees = invitees;
    }

    public void setEventId(String eventId){
        this.eventId = eventId;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bInviteUsersFragmentSkip){


        }

        if(v.getId() == R.id.bInviteUsersFragmentInvite){

            inviteUsersThread = new InviteUsersThread(getActivity(), new ArrayList<String>(), new ArrayList<String>(), eventId);
            inviteUsersThread.start();
        }

        if(fragmentManager == null){
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flMainActivity, new HomeFragment(), "Home Fragment");
        fragmentTransaction.commit();
    }

    public class InviteFriendsPagerAdapter extends FragmentPagerAdapter {

        public InviteFriendsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0){
                return inviteFacebookFriendsFragment;
            }

            if(position == 1){
                return invitePhoneContactsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Facebook Friends";
                case 1:
                    return "Phone Contacts";
            }
            return null;
        }
    }

    private class InviteUsersThread extends InviteUsersApi {

        public InviteUsersThread(Context context, List<String> userIds, List<String> phoneNumbers, String eventId) {
            super(context, userIds, phoneNumbers, eventId);
        }
    }
}
