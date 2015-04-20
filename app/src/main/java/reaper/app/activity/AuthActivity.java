package reaper.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import reaper.R;
import reaper.api.login.LoginValidationApi;
import reaper.app.fragment.login.LoginFragment;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 10-04-2015.
 */
public class AuthActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Log.d("APP", "oncreate Auth Activity");

        String sessionId = AppPreferences.get(this, Constants.AppPreferenceKeys.SESSION_ID);
        if (sessionId == null)
        {
            Log.d("APP", "Session id is null");
            gotoLoginFragment();
        }
        else
        {
            Log.d("APP", "Api task called auth activity");
            ApiTask apiTask = new ApiTask();
            apiTask.execute();
        }
    }

    public void gotoLoginFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAuthActivity, new LoginFragment(), "Login");
        fragmentTransaction.commit();
    }

    public void gotoHomeFragment()
    {
        Log.d("APP", "Already Logged in");
    }

    private class ApiTask extends LoginValidationApi
    {
        public ApiTask()
        {
            super(AuthActivity.this);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            Boolean isLoggedIn = (Boolean) o;
            Log.d("APP", String.valueOf(isLoggedIn));
            if (isLoggedIn)
            {
                gotoHomeFragment();
            }
            else
            {
                gotoLoginFragment();
            }
        }
    }
}
