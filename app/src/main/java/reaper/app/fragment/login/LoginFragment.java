package reaper.app.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import reaper.R;
import reaper.api.endpoints.login.LoginApi;
import reaper.api.endpoints.login.LoginValidationApi;
import reaper.app.activity.MainActivity;
import reaper.app.service.FacebookService;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 10-04-2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
{
    private LoginApiTask loginApiTask;
    private LoginValidationApiTask loginValidationApiTask;

    private int status;
    private ArrayList<String> missingPermissions;

    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult)
        {
            AccessToken accessToken = loginResult.getAccessToken();
            Log.d("APP", "Login Fragment: onsuccess  = " + accessToken);
            validatePermissions(accessToken);

            if (validatePermissions(accessToken))
            {
                Log.d("APP", "Login Fragment: permissions ok");
                String sessionId = AppPreferences.get(getActivity(), Constants.AppPreferenceKeys.SESSION_ID);
                if (sessionId == null)
                {
                    Log.d("APP", "LoginFragment: session_id null");
                    loginApiTask = new LoginApiTask(accessToken.getToken());
                    loginApiTask.execute();
                }
                else
                {
                    Log.d("APP", "LoginFragment: validating session_id");
                    loginValidationApiTask = new LoginValidationApiTask();
                    loginValidationApiTask.execute();
                }
            }
        }

        @Override
        public void onCancel()
        {
        }

        @Override
        public void onError(FacebookException e)
        {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);

        Log.d("APP", "Login Fragment: status  = " + status);

        if (status == FacebookService.ACCESS_TOKEN_INVALID || status == FacebookService.SESSION_ID_NULL)
        {
            if (status == FacebookService.SESSION_ID_NULL)
            {
                LoginManager.getInstance().logOut();
            }
            facebookLoginButton = (LoginButton) view.findViewById(R.id.login_button);
            facebookLoginButton.setReadPermissions("user_friends", "public_profile", "email");
            facebookLoginButton.setFragment(this);
            facebookLoginButton.registerCallback(callbackManager, facebookCallback);
            facebookLoginButton.setOnClickListener(this);
            facebookLoginButton.setVisibility(View.VISIBLE);
        }

        if (status == FacebookService.ACCESS_TOKEN_PERMISSIONS_INVALID)
        {
            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(this, missingPermissions);
        }

    }

    public boolean validatePermissions(AccessToken accessToken)
    {
        Set<String> permissions = accessToken.getPermissions();
        Set<String> declinedPermissions = new HashSet<>();
        if (!permissions.contains("email"))
        {
            declinedPermissions.add("email");
        }
        if (!permissions.contains("user_friends"))
        {
            declinedPermissions.add("user_friends");
        }

        if (accessToken.isExpired() || declinedPermissions.size() > 0)
        {
            LoginManager.getInstance().logInWithReadPermissions(this, declinedPermissions);
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view)
    {
        facebookLoginButton.setVisibility(View.GONE);
    }

    @Override
    public void onPause()
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

    public void gotoMainActivity()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void setData(int status, ArrayList<String> missingPermissions)
    {
        this.status = status;
        this.missingPermissions = missingPermissions;
    }

    class LoginApiTask extends LoginApi
    {
        public LoginApiTask(String accessToken)
        {
            super(getActivity(), accessToken);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            String sessionId = (String) o;
            if (sessionId != null)
            {
                AppPreferences.set(getActivity(), Constants.AppPreferenceKeys.SESSION_ID, sessionId);
                gotoMainActivity();
            }
            else
            {
                Toast.makeText(getActivity(), R.string.message_server_error_relogin, Toast.LENGTH_LONG).show();

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.setData(FacebookService.SESSION_ID_NULL, new ArrayList<String>());
                fragmentTransaction.replace(R.id.flAuthActivity, loginFragment, "Re-Login");
                fragmentTransaction.commit();
            }
        }
    }

    private class LoginValidationApiTask extends LoginValidationApi
    {
        public LoginValidationApiTask()
        {
            super(getActivity());
        }

        @Override
        protected void onPostExecute(Object o)
        {
            Boolean isLoggedIn = (Boolean) o;

            Log.d("APP", "LoginFragment: session_id validity = " + isLoggedIn);

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
}
