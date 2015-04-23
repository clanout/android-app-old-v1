package reaper.app.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Set;

import reaper.R;
import reaper.app.activity.MainActivity;
import reaper.app.fragment.login.LoginFragment;

/**
 * Created by Aditya on 22-04-2015.
 */
public class FacebookService
{
    public static final int ACCESS_TOKEN_INVALID = 0;
    public static final int ACCESS_TOKEN_PERMISSIONS_INVALID = 1;
    public static final int SESSION_ID_NULL = 2;

    private Context context;
    private FragmentManager manager;

    public FacebookService(Context context, FragmentManager manager)
    {
        if (!FacebookSdk.isInitialized())
        {
            throw new IllegalStateException("Facebook SDK not initialized");
        }
        this.context = context;
        this.manager = manager;
    }

    public String getAccessToken()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired())
        {
            Log.d("APP", "Access token null");
            gotoLoginFragment(ACCESS_TOKEN_INVALID, new ArrayList<String>());
        }
        else
        {
            Log.d("APP", "Access token = " + accessToken.getToken());
            ArrayList<String> missingPermissions = getMissingPermissions(accessToken);
            if (missingPermissions.size() > 0)
            {
                Log.d("APP", "Access token has missing permissions = " + missingPermissions);
                gotoLoginFragment(ACCESS_TOKEN_PERMISSIONS_INVALID, missingPermissions);
            }
            else
            {
                Log.d("APP", "Access token has no missing permissions");
                return accessToken.getToken();
            }
        }

        return null;
    }

    private ArrayList<String> getMissingPermissions(AccessToken accessToken)
    {
        ArrayList<String> missingPermissions = new ArrayList<>();
        Set<String> grantedPermissions = accessToken.getPermissions();

        if (!grantedPermissions.contains("email"))
        {
            missingPermissions.add("email");
        }

        if (!grantedPermissions.contains("user_friends"))
        {
            missingPermissions.add("user_friends");
        }

        return missingPermissions;
    }

    private void gotoLoginFragment(int status, ArrayList<String> missingPermissions)
    {
        Log.d("APP", "Access token status" + status + " missing permissions" + missingPermissions.toString());
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setData(status, missingPermissions);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.flAuthActivity, loginFragment, "Login");
        fragmentTransaction.commit();
    }
}
