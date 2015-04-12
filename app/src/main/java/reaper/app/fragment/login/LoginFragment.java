package reaper.app.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import reaper.R;
import reaper.api.login.LoginApi;
import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 10-04-2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
{
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult)
        {
            AccessToken accessToken = loginResult.getAccessToken();
            Log.d("APP", accessToken.getToken());

            ApiTask apiTask = new ApiTask(getActivity(), accessToken.getToken());
            apiTask.execute();
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
        LoginButton facebookLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("user_friends", "public_profile", "email");
        facebookLoginButton.setFragment(this);
        facebookLoginButton.registerCallback(callbackManager, facebookCallback);

        facebookLoginButton.setOnClickListener(this);
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

    }

    class ApiTask extends LoginApi
    {
        public ApiTask(Context context, String accessToken)
        {
            super(context, accessToken);
        }

        @Override
        protected void onPostExecute(Object o)
        {
            String sessionId = (String) o;
            if(sessionId != null)
            {
                AppPreferences.set(getActivity(), Constants.AppPreferenceKeys.SESSION_ID, sessionId);
                Log.d("APP", "Login Successful " + sessionId);
            }
        }
    }
}
