package reaper.app.fragment.accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.accounts.FriendBlockApi;
import reaper.api.endpoints.accounts.FriendListApi;
import reaper.api.model.user.User;
import reaper.app.activity.MainActivity;
import reaper.app.list.accounts.MyFriendsAdapter;

/**
 * Created by Aditya on 07-05-2015.
 */
public class MyFriendsFragment extends Fragment implements View.OnClickListener, MyFriendsAdapter.FriendListItemClickListener, SearchView.OnCloseListener, SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private Button makechanges;
    private LinearLayout mainContent;
    private TextView noFriendsMessage;
    private FragmentManager fragmentManager;
    private MyFriendsAdapter adapter;
    private List<User> friendsList;
    private List<User> visibleFriends;

    private SearchView searchView;
    private MenuItem searchItem;
    private ApiTask apiTask;
    List<String> blockedUsers;
    List<String> unblockedUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvMyFriendsList);
        makechanges = (Button) view.findViewById(R.id.bMyFriendsMakeChanges);
        mainContent = (LinearLayout) view.findViewById(R.id.llMyFriendsFragmentMainContent);
        noFriendsMessage = (TextView) view.findViewById(R.id.tvMyFriendsNoFriends);

        makechanges.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainContent.setVisibility(View.VISIBLE);
        noFriendsMessage.setVisibility(View.GONE);

        makechanges.setVisibility(View.GONE);

        searchItem = ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();

        friendsList = new ArrayList<>();
        blockedUsers = new ArrayList<>();
        unblockedUsers = new ArrayList<>();
        resetVisibleFriends();

        initRecyclerView();

    }

    public void resetVisibleFriends()
    {
        visibleFriends = new ArrayList<>();
        for (User user : friendsList)
        {
            visibleFriends.add(user);
        }

    }

    private void initRecyclerView() {

        adapter = new MyFriendsAdapter(getActivity(), visibleFriends);
        adapter.setFriendListItemClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView() {
        adapter = new MyFriendsAdapter(getActivity(), visibleFriends);
        adapter.setFriendListItemClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (visibleFriends.size() == 0) {
            noFriendsMessage.setText("No friends to show");
            noFriendsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        } else {
            mainContent.setVisibility(View.VISIBLE);
            noFriendsMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        apiTask = new ApiTask(getActivity());
        apiTask.execute();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity)getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity)getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity)getActivity()).getMenu().findItem(R.id.abbHome).setVisible(true);
            ((MainActivity)getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(true);
            ((MainActivity)getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (apiTask != null) {
            apiTask.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("My Friends");
    }

    @Override
    public void onClick(View view) {

        ApiThread apiThread = new ApiThread(getActivity(), blockedUsers, unblockedUsers);
        apiThread.start();

        if(fragmentManager == null){
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flMainActivity, new AccountsFragment(), "AccountsFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onFriendListItemClicked(View view, int position) {

        User selectedUser = visibleFriends.get(position);
        makechanges.setVisibility(View.VISIBLE);

        if(selectedUser.isBlocked()){
            selectedUser.setBlocked(false);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivMyFriendsListBlockStatus);
            imageView.setImageResource(R.drawable.ic_action_not_important);
            if(blockedUsers.contains(selectedUser.getId())){
                blockedUsers.remove(selectedUser.getId());
            }
            unblockedUsers.add(selectedUser.getId());
        }else {
            selectedUser.setBlocked(true);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivMyFriendsListBlockStatus);
            imageView.setImageResource(R.drawable.ic_action_important);
            if(unblockedUsers.contains(selectedUser.getId())){
                unblockedUsers.remove(selectedUser.getId());
            }
            blockedUsers.add(selectedUser.getId());
        }



    }

    public void resetSearch()
    {
        try
        {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (Exception e)
        {
        }
        searchView.onActionViewCollapsed();
    }

    @Override
    public boolean onClose()
    {
        resetSearch();
        resetVisibleFriends();
        refreshRecyclerView();
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        visibleFriends = new ArrayList<>();
        if (!newText.isEmpty())
        {
            for (User user : friendsList)
            {
                String name = user.getName();
                if (name.toLowerCase().contains(newText.toLowerCase()))
                {
                    visibleFriends.add(user);
                }
            }
        }
        else
        {
            resetVisibleFriends();
        }
        refreshRecyclerView();
        return false;
    }

    private class ApiTask extends FriendListApi {

        public ApiTask(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Object o) {

            try {
                if (o == null) {
                    friendsList = new ArrayList<>();
                } else {
                    friendsList = (List<User>) o;
                }
                resetSearch();
                resetVisibleFriends();
                refreshRecyclerView();
                resetSearch();
            } catch (Exception e) {

            }
        }
    }

    private class ApiThread extends FriendBlockApi {

        public ApiThread(Context context, List<String> blockedUsers, List<String> unblockedUsers) {
            super(context, blockedUsers, unblockedUsers);
        }
    }
}
