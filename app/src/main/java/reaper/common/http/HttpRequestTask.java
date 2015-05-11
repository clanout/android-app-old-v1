package reaper.common.http;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import reaper.conf.AppPreferences;
import reaper.conf.Constants;

public abstract class HttpRequestTask extends AsyncTask<Void, Void, Object>
{
    protected String url;
    private Map<String, String> postData;
    protected Context context;

    public HttpRequestTask(String url, Context context)
    {
        super();
        this.url = url;
        this.context = context;
        this.postData = new HashMap<>();
    }

    public void setPostData(Map<String, String> postData)
    {
        this.postData = postData;
    }

    @Override
    protected Object doInBackground(Void... params)
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
        return jsonResponse;
    }
}


