package reaper.api.endpoints.login;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.api.model.core.BasicJsonParser;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;
import reaper.conf.Constants;

/**
 * Created by Aditya on 10-04-2015.
 */
public class LoginApi extends HttpRequestTask
{
    public LoginApi(Context context, String accessToken)
    {
        super(UrlBuilder.build(Uri.AUTHENTICATION), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("access_token", accessToken);

        setPostData(postData);
    }

    @Override
    protected String doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);

        if (jsonResponse != null)
        {
            String cookie = BasicJsonParser.getValue(jsonResponse, Constants.AppPreferenceKeys.SESSION_ID);
            return cookie;
        }
        else
        {
            return null;
        }
    }
}
