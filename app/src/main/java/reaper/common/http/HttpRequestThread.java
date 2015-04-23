package reaper.common.http;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class HttpRequestThread extends Thread
{
    protected String url;
    protected Context context;
    protected Map<String, String> postData;
    protected Handler handler;

    public HttpRequestThread(String url, Context context)
    {
        this.url = url;
        this.context = context;
        this.postData = new HashMap<>();
        this.handler = null;
    }

    public void setPostData(Map<String, String> postData)
    {
        this.postData = postData;
    }

    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void run()
    {
        String jsonResponse = null;
        HttpRequest request = new HttpRequest();
        try
        {
            String sessionId = AppPreferences.get(context, Constants.AppPreferenceKeys.SESSION_ID);

            if (sessionId != null)
            {
                postData.put(Constants.Http.SESSION_ID, sessionId);
            }

            jsonResponse = request.sendRequest(url, postData);
        }
        catch (HttpServerError httpServerError)
        {
            jsonResponse = null;
        }

        if (jsonResponse == null)
        {
            onFailure();
        }
        else
        {
            onSuccess(jsonResponse);
        }
    }

    public abstract void onSuccess(String jsonResponse);

    public abstract void onFailure();
}
