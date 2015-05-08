package reaper.app.fragment.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import reaper.R;

/**
 * Created by Aditya on 07-05-2015.
 */
public class MyFriendsFragment extends Fragment implements View.OnClickListener
{
    private RecyclerView recyclerView;
    private Button makechanges;
    private LinearLayout mainContent;
    private TextView noFriendsMessage;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvMyFriendsList);
        makechanges = (Button) view.findViewById(R.id.bMyFriendsMakeChanges);
        mainContent = (LinearLayout) view.findViewById(R.id.llMyFriendsFragmentMainContent);
        noFriendsMessage = (TextView) view.findViewById(R.id.tvMyFriendsNoFriends);

        makechanges.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        mainContent.setVisibility(View.VISIBLE);
        noFriendsMessage.setVisibility(View.GONE);

        makechanges.setVisibility(View.GONE);

        fragmentManager = getActivity().getSupportFragmentManager();

    }

    @Override
    public void onClick(View view)
    {

    }
}
