package reaper.app.fragment.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.app.list.accounts.AccountsAdapter;
import reaper.app.list.accounts.AccountsListItem;

/**
 * Created by Aditya on 07-04-2015.
 */
public class AccountsFragment extends Fragment implements AccountsAdapter.ClickListener
{
    private ImageView profilePicture;
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    private List<AccountsListItem> accountList;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        profilePicture = (ImageView) view.findViewById(R.id.ivAccountProfilePic);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvAccounts);

        accountsAdapter = new AccountsAdapter(getActivity(), getdata());
        accountsAdapter.setListener(this);
        recyclerView.setAdapter(accountsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    public List<AccountsListItem> getdata()
    {
        accountList = new ArrayList<>();
        int[] icons = {R.mipmap.ic_action_accept, R.mipmap.ic_action_important, R.drawable.ic_chat_black_18dp};
        String[] titles = {"Manage Friends", "My Past Events", "Invite People Through Whatsapp"};

        for (int i = 0; i < icons.length && i < titles.length; i++)
        {
            AccountsListItem current = new AccountsListItem();

            current.iconId = icons[i];
            current.title = titles[i];

            accountList.add(current);
        }
        return accountList;
    }

    @Override
    public void itemClicked(View view, int position)
    {
        if (position == 0)
        {
            if(fragmentManager == null){
                fragmentManager = getActivity().getSupportFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flMainActivity, new MyFriendsFragment(), "FriendsList");
            fragmentTransaction.commit();
        }
        else
        {
            Toast.makeText(getActivity(), "Item at position " + position + "was clicked", Toast.LENGTH_LONG).show();
        }
    }
}
