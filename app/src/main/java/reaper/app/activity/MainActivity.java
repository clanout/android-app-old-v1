package reaper.app.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.List;

import reaper.R;
import reaper.api.model.event.Event;
import reaper.app.backgroundservice.EventFeedListener;
import reaper.app.backgroundservice.EventFeedService;
import reaper.app.fragment.home.HomeFragment;
import reaper.common.cache.Cache;

/**
 * Created by reaper on 05-04-2015.
 */
public class MainActivity extends FragmentActivity implements EventFeedListener
{
    private EventFeedService.EventFeedBinder binder = null;

    private ServiceConnection eventFeedConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            Log.d("APP", "activity connected");
            binder = (EventFeedService.EventFeedBinder) iBinder;
            binder.setEventFeedListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            binder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("APP", "activity created");

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


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flMainActivity, new HomeFragment(), "Home");
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        Intent intent = new Intent(this, EventFeedService.class);
//        bindService(intent, eventFeedConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Cache.commit(this);
        Log.d("APP", "onStop");
        Intent intent = new Intent(this, EventFeedService.class);
        stopService(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("APP", "onPause");
    }

    @Override
    public void onEventFeedUpdate(List<Event> events)
    {
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
