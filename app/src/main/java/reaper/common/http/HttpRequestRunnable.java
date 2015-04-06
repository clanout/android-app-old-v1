package reaper.common.http;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class HttpRequestRunnable implements Runnable
{
    protected String url;
    protected Context context;
    protected Map<String, String> postData;
    protected Handler handler;

    public HttpRequestRunnable(String url, Context context)
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
            jsonResponse = request.sendRequest(url, postData, context);
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
