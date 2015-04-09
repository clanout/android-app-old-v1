package reaper.app.fragment.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reaper.app.list.accounts.AccountsAdapter;
import reaper.app.list.accounts.AccountsListItem;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 07-04-2015.
 */
public class AccountsFragment extends Fragment implements AccountsAdapter.ClickListener
{
    private ImageView profilePicture;
    private TextView name;

    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    private List<AccountsListItem> accountList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        profilePicture = (ImageView) view.findViewById(R.id.ivAccountProfilePic);
        name = (TextView) view.findViewById(R.id.tvAccountsName);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvAccounts);

        accountsAdapter = new AccountsAdapter(getActivity(),getdata());
        accountsAdapter.setListener(this);
        recyclerView.setAdapter(accountsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public List<AccountsListItem> getdata(){
        accountList = new ArrayList<>();
        int[] icons = {R.mipmap.ic_action_accept, R.mipmap.ic_action_important};
        String[] titles = {"My friends", "My Events"};

        for(int i=0; i<icons.length && i<titles.length; i++){
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
        Toast.makeText(getActivity(), "Item at position " + position + "was clicked", Toast.LENGTH_LONG).show();
    }
}
