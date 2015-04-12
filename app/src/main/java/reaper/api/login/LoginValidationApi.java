package reaper.api.login;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 11-04-2015.
 */
public class LoginValidationApi extends HttpRequestTask
{
    public LoginValidationApi(Context context)
    {
        super(UrlBuilder.build(Uri.AUTHENTICATION_VALIDATION), context, null);
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);
        if (jsonResponse != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
