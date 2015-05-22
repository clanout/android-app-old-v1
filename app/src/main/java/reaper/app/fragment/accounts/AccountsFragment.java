package reaper.app.fragment.accounts;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.endpoints.accounts.AddPhoneApi;
import reaper.app.activity.MainActivity;
import reaper.app.list.accounts.AccountsAdapter;
import reaper.app.list.accounts.AccountsListItem;
import reaper.app.service.PhoneUtils;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 07-04-2015.
 */
public class AccountsFragment extends Fragment implements AccountsAdapter.ClickListener {
    private ImageView profilePicture;
    private TextView username;
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    private List<AccountsListItem> accountList;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();

        username.setText("Harsh Pokharna");

    }

    @Override
    public void onResume() {
        super.onResume();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(true);
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
        getActivity().getActionBar().setTitle("Accounts");
    }

    public List<AccountsListItem> getdata() {
        accountList = new ArrayList<>();
        int[] icons = {R.drawable.ic_action_group, R.drawable.ic_action_phone, R.drawable.ic_action_add_person};
        String[] titles = {"Friends", "Phone Number", "Invite Through Whatsapp"};

        for (int i = 0; i < icons.length && i < titles.length; i++) {
            AccountsListItem current = new AccountsListItem();

            current.iconId = icons[i];
            current.title = titles[i];

            accountList.add(current);
        }

        return accountList;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager packageManager = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void itemClicked(View view, int position) {
        if (position == 0) {
            if (fragmentManager == null) {
                fragmentManager = getActivity().getSupportFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flMainActivity, new MyFriendsFragment(), "FriendsList");
            fragmentTransaction.commit();
        }

        if (position == 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle("Add Phone Number");

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_add_phone, null);
            builder.setView(dialogView);

            final EditText phoneNumber = (EditText) dialogView.findViewById(R.id.etAddPhone);
            Button add = (Button) dialogView.findViewById(R.id.bAddPhone);

            final String oldPhoneNumber = PhoneUtils.getNumber(AppPreferences.get(getActivity(), Constants.AppPreferenceKeys.MY_PHONE_NUMBER));
            if (oldPhoneNumber == null || oldPhoneNumber.isEmpty()) {
                phoneNumber.setText("");
            } else {
                phoneNumber.setText(oldPhoneNumber);
            }

            final AlertDialog alertDialog = builder.create();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (phoneNumber.getText().toString().equals(oldPhoneNumber)) {
                        Toast.makeText(getActivity(), "There is no change in the phone number", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String parsedPhone = PhoneUtils.parsePhone(phoneNumber.getText().toString(), Constants.DEFAULT_COUNTRY_CODE);
                    if (parsedPhone == null) {
                        Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_LONG).show();
                    } else {
                        AddPhoneApi addPhoneApi = new AddPhoneApi(getActivity(), parsedPhone);
                        addPhoneApi.start();

                        AppPreferences.set(getActivity(), Constants.AppPreferenceKeys.MY_PHONE_NUMBER, parsedPhone);
                        alertDialog.cancel();
                    }
                }
            });

            alertDialog.show();


        }

        if (position == 2) {
            boolean isWhatsappInstalled = appInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {
                ComponentName componentName = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Checking");
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Whatsapp not installed on this device", Toast.LENGTH_LONG).show();
            }
        }


    }
}
