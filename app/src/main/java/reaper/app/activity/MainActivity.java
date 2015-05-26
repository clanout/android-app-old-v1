package reaper.app.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alertdialogpro.AlertDialogPro;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.List;

import reaper.R;
import reaper.api.endpoints.accounts.AddPhoneApi;
import reaper.api.model.event.Event;
import reaper.app.backgroundservice.EventFeedListener;
import reaper.app.backgroundservice.EventFeedService;
import reaper.app.fragment.accounts.AccountsFragment;
import reaper.app.fragment.event.dialogfragment.CreateEventDialogFragment;
import reaper.app.fragment.home.HomeFragment;
import reaper.app.service.PhoneUtils;
import reaper.common.cache.Cache;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by reaper on 05-04-2015.
 */
public class MainActivity extends FragmentActivity implements EventFeedListener {
    private EventFeedService.EventFeedBinder binder = null;
    private FragmentManager fragmentManager;
    private Menu menu;

    private ServiceConnection eventFeedConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (EventFeedService.EventFeedBinder) iBinder;
            binder.setEventFeedListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        LocationService locationService = new LocationService(this);
//
//        Map<String, Double> map = locationService.getUserLocation();
//
//        if(map != null){
//            String zone = locationService.getUserZone(map);
//
//            Log.d("APP", "MAIN ACTIVITY zone" + zone);
//        }
//
//        Intent intent = new Intent(this, EventFeedService.class);
//        startService(intent);

        AppPreferences.set(this, Constants.Location.LATITUDE, "12.9738");
        AppPreferences.set(this, Constants.Location.LONGITUDE, "77.6119");
        AppPreferences.set(this, Constants.Location.ZONE, "Bengaluru");
        AppPreferences.set(this, Constants.AppPreferenceKeys.ME, "9320369679");

        fragmentManager = getSupportFragmentManager();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flMainActivity, new HomeFragment(), "Home");
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent intent = new Intent(this, EventFeedService.class);
//        bindService(intent, eventFeedConnection, BIND_AUTO_CREATE);
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Cache.commit(this);
        Log.d("APP", "onStop");
        Intent intent = new Intent(this, EventFeedService.class);
        stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("APP", "onPause");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_button, menu);
        this.menu = menu;
        menu.findItem(R.id.abbAccounts).setVisible(true);
        menu.findItem(R.id.abbCreateEvent).setVisible(true);
        menu.findItem(R.id.abbHome).setVisible(false);
        menu.findItem(R.id.abbEditEvent).setVisible(false);
        menu.findItem(R.id.abbSearch).setVisible(false);
        menu.findItem(R.id.abbDeleteEvent).setVisible(false);
        menu.findItem(R.id.abbFinaliseEvent).setVisible(false);

        if (AppPreferences.get(this, Constants.AppPreferenceKeys.MY_PHONE_NUMBER) == null) {
            menu.findItem(R.id.abbAddPhone).setVisible(true);
        } else {
            menu.findItem(R.id.abbAddPhone).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.abbAccounts) {
            if (fragmentManager == null) {
                fragmentManager = getSupportFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flMainActivity, new AccountsFragment(), "Accounts");
            fragmentTransaction.commit();
            return true;
        }

        if (item.getItemId() == R.id.abbHome) {
            if (fragmentManager == null) {
                fragmentManager = getSupportFragmentManager();
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.flMainActivity, new HomeFragment(), "HomeFragment");
            transaction.commit();
        }

        if (item.getItemId() == R.id.abbCreateEvent) {
            if (fragmentManager == null) {
                fragmentManager = getSupportFragmentManager();
            }
            DialogFragment createEventDialog = new CreateEventDialogFragment();
            createEventDialog.show(fragmentManager, "Create Event Dialog");

        }

        if (item.getItemId() == R.id.abbAddPhone) {

            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title("Add Phone Number");
            builder.autoDismiss(true);
            builder.theme(Theme.LIGHT);

            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_add_phone, null);
            builder.customView(dialogView, true);

            final EditText phoneNumber = (EditText) dialogView.findViewById(R.id.etAddPhone);
            Button add = (Button) dialogView.findViewById(R.id.bAddPhone);

            final MaterialDialog alertDialog = builder.build();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String parsedPhone = PhoneUtils.parsePhone(phoneNumber.getText().toString(), Constants.DEFAULT_COUNTRY_CODE);
                    if (parsedPhone == null) {
                        Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_LONG).show();
                    } else {

                        AddPhoneApi addPhoneApi = new AddPhoneApi(MainActivity.this, parsedPhone);
                        addPhoneApi.start();

                        AppPreferences.set(MainActivity.this, Constants.AppPreferenceKeys.MY_PHONE_NUMBER, parsedPhone);

                        getMenu().findItem(R.id.abbAddPhone).setVisible(false);

                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(dialogView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        alertDialog.cancel();
                    }
                }
            });

            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onEventFeedUpdate(List<Event> events) {
        Log.d("APP", "Event feed updated");
    }

    //    private class LocationAsyncTask extends AsyncTask
//    {
//
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        LocationListener locationListener;
//        @Override
//        protected Object doInBackground(Object[] objects)
//        {
//
//            locationListener = new LocationListener()
//        {
//            @Override
//            public void onLocationChanged(Location location)
//            {
//                Log.d("APP", "Listening");
//
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//
//                Log.d("APP", latitude + " " + longitude);
//
//                AppPreferences.set(MainActivity.this, Constants.Location.LATITUDE, String.valueOf(latitude));
//                AppPreferences.set(MainActivity.this, Constants.Location.LONGITUDE, String.valueOf(longitude));
//
//
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle)
//            {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s)
//            {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s)
//            {
//
//            }
//        };
//
//
//            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//            {
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//            }
//            else
//            {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            }
//
//
//        }
//    }
}
