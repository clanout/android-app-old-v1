package reaper.app.fragment.accounts;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.app.activity.MainActivity;
import reaper.app.list.accounts.AccountsAdapter;
import reaper.app.list.accounts.AccountsListItem;

/**
 * Created by Aditya on 07-04-2015.
 */
public class AccountsFragment extends Fragment implements AccountsAdapter.ClickListener
{
    private ImageView profilePicture;
    private TextView username;
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    private List<AccountsListItem> accountList;
    private FragmentManager fragmentManager;

    private MenuItem accountsItem, homeItem, createEventItem, editEventItem, searchItem;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        profilePicture = (ImageView) view.findViewById(R.id.ivAccountProfilePic);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvAccounts);
        username = (TextView) view.findViewById(R.id.tvAccountsUsername);

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

        username.setText("Harsh Pokharna");

        accountsItem = ((MainActivity)getActivity()).getMenu().findItem(R.id.abbAccounts);
        homeItem = ((MainActivity)getActivity()).getMenu().findItem(R.id.abbHome);
        createEventItem = ((MainActivity)getActivity()).getMenu().findItem(R.id.abbCreateEvent);
        editEventItem = ((MainActivity)getActivity()).getMenu().findItem(R.id.abbEditEvent);
        searchItem = ((MainActivity)getActivity()).getMenu().findItem(R.id.abbSearch);
    }

    @Override
    public void onResume() {
        super.onResume();

        accountsItem.setVisible(false);
        createEventItem.setVisible(false);
        homeItem.setVisible(true);
        editEventItem.setVisible(false);
        searchItem.setVisible(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("Accounts");
    }

    public List<AccountsListItem> getdata()
    {
        accountList = new ArrayList<>();
        int[] icons = {R.drawable.ic_action_group, R.drawable.ic_action_event, R.drawable.ic_action_add_person};
        String[] titles = {"Friends", "Archived Events", "Invite Through Whatsapp"};

        for (int i = 0; i < icons.length && i < titles.length; i++)
        {
            AccountsListItem current = new AccountsListItem();

            current.iconId = icons[i];
            current.title = titles[i];

            accountList.add(current);
        }
        return accountList;
    }

    private boolean appInstalledOrNot(String uri)
    {
        PackageManager packageManager = getActivity().getPackageManager();
        boolean app_installed = false;
        try
        {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed ;
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

        if(position == 2)
        {
            boolean isWhatsappInstalled = appInstalledOrNot("com.whatsapp");
            if(isWhatsappInstalled){
                ComponentName componentName = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Checking");
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Whatsapp not installed on this device", Toast.LENGTH_LONG).show();
            }
        }

    }
}
