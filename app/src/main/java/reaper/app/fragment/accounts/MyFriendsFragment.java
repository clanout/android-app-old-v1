package reaper.app.fragment.accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import reaper.R;
import reaper.api.endpoints.accounts.FriendListApi;
import reaper.api.model.user.User;
import reaper.app.list.accounts.MyFriendsAdapter;

/**
 * Created by Aditya on 07-05-2015.
 */
public class MyFriendsFragment extends Fragment implements View.OnClickListener, MyFriendsAdapter.FriendListItemClickListener {
    private RecyclerView recyclerView;
    private Button makechanges;
    private LinearLayout mainContent;
    private TextView noFriendsMessage;
    private FragmentManager fragmentManager;
    private MyFriendsAdapter adapter;
    private List<User> friendsList;

    private ApiTask apiTask;

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
        mainContent.setVisibility(View.VISIBLE);
        noFriendsMessage.setVisibility(View.GONE);

        makechanges.setVisibility(View.GONE);

        fragmentManager = getActivity().getSupportFragmentManager();

        friendsList = new ArrayList<>();

        initRecyclerView();
    }

    private void initRecyclerView() {

        adapter = new MyFriendsAdapter(getActivity(), friendsList);
        adapter.setFriendListItemClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void refreshRecyclerView() {
        adapter = new MyFriendsAdapter(getActivity(), friendsList);
        adapter.setFriendListItemClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (friendsList.size() == 0) {
            noFriendsMessage.setText("No friends to show");
            noFriendsMessage.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
            makechanges.setVisibility(View.GONE);
        } else {
            mainContent.setVisibility(View.VISIBLE);
            noFriendsMessage.setVisibility(View.GONE);
            makechanges.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        apiTask = new ApiTask(getActivity());
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
        getActivity().getActionBar().setTitle("My Friends");
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFriendListItemClicked(View view, int position) {

    }

    private class ApiTask extends FriendListApi {

        public ApiTask(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Object o) {

            try {
                friendsList = (List<User>) o;
                refreshRecyclerView();
            } catch (Exception e) {

            }
        }
    }
}
