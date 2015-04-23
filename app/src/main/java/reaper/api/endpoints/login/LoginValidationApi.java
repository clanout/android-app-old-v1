package reaper.api.endpoints.login;

import android.content.Context;

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
        super(UrlBuilder.build(Uri.AUTHENTICATION_VALIDATION), context);
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
