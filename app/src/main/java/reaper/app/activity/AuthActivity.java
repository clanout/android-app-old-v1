package reaper.app.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Map;

import reaper.R;
import reaper.api.endpoints.login.LoginApi;
import reaper.api.endpoints.login.LoginValidationApi;
import reaper.app.fragment.login.LoginFragment;
import reaper.app.service.FacebookService;
import reaper.app.service.LocationService;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 10-04-2015.
 */
public class AuthActivity extends FragmentActivity
{
    private FragmentManager manager;
    private FacebookService facebookService;
    private LoginApiTask loginApiTask;
    private LoginValidationApiTask loginValidationApiTask;

    public static final String LOCATION_UPDATED = "location_updated";
    public static final String LOCATION_NOT_UPDATED = "location_not_updated";

    private String locationUpdateTag;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_auth);

        manager = getSupportFragmentManager();

        LocationService locationService = new LocationService(this);
        Map<String, Double> locationMap = locationService.getUserLocation();

        if (locationMap != null)
        {
            locationUpdateTag = LOCATION_NOT_UPDATED;
        }

        String sessionId = AppPreferences.get(this, Constants.AppPreferenceKeys.SESSION_ID);
        if (sessionId == null)
        {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            String token = accessToken.getToken();
            Log.d("APP", "Auth activity : session id is null");
            loginApiTask = new LoginApiTask(token);
            loginApiTask.execute();
        }
        else
        {
            Log.d("APP", "Auth activity session id  = " + sessionId);
            loginValidationApiTask = new LoginValidationApiTask();
            loginValidationApiTask.execute();
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (loginApiTask != null)
        {
            loginApiTask.cancel(true);
        }

        if (loginValidationApiTask != null)
        {
            loginValidationApiTask.cancel(true);
        }
    }

    public void gotoLoginFragment(int status, ArrayList<String> missingPermissions)
    {

        if (manager == null)
        {
            manager = getSupportFragmentManager();
        }

        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setData(status, missingPermissions);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.flAuthActivity, loginFragment, "Login");
        fragmentTransaction.commit();
    }

    public void gotoMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class LoginValidationApiTask extends LoginValidationApi
    {
        public LoginValidationApiTask()
        {
            super(AuthActivity.this);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            Boolean isLoggedIn = (Boolean) o;

            Log.d("APP", "AuthActivity: session_id validity = " + isLoggedIn);

            if (isLoggedIn)
            {
                gotoMainActivity();
            }
            else
            {
                loginApiTask = new LoginApiTask(AccessToken.getCurrentAccessToken().getToken());
                loginApiTask.execute();
            }
        }
    }

    class LoginApiTask extends LoginApi
    {
        public LoginApiTask(String accessToken)
        {
            super(AuthActivity.this, accessToken);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            String sessionId = (String) o;
            Log.d("APP", "AuthActivity: session_id = " + sessionId);
            if (sessionId != null)
            {
                AppPreferences.set(AuthActivity.this, Constants.AppPreferenceKeys.SESSION_ID, sessionId);
                gotoMainActivity();
            }
            else
            {
                gotoLoginFragment(FacebookService.SESSION_ID_NULL, new ArrayList<String>());
            }
        }
    }
}
