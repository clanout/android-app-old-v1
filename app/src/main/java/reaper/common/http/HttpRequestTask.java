package reaper.common.http;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import reaper.common.cache.Cache;

public class HttpRequestTask extends AsyncTask<Void, Void, Object>
{
    private String url;
    private Context context;
    private Map<String, String> postData;
    private String cacheKey;
    private boolean isCached;

    public HttpRequestTask(String url, Context context, String cacheKey)
    {
        super();
        this.url = url;
        this.context = context;
        this.postData = new HashMap<>();
        this.cacheKey = cacheKey;
        this.isCached = true;
    }

    public void setPostData(Map<String, String> postData)
    {
        this.postData = postData;
    }

    public void disableCaching()
    {
        isCached = false;
    }


    @Override
    protected Object doInBackground(Void... params)
    {
        if (isCached)
        {
            String jsonResponse = Cache.get(context, cacheKey);

            if (jsonResponse == null)
            {
                HttpRequest request = new HttpRequest();

                try
                {
                    jsonResponse = request.sendRequest(url, postData, context);
                    Cache.set(context, cacheKey, jsonResponse);
                }
                catch (HttpServerError httpServerError)
                {
                    jsonResponse = null;
                }
            }
            return jsonResponse;
        }
        else
        {
            String jsonResponse = null;
            HttpRequest request = new HttpRequest();

            try
            {
                jsonResponse = request.sendRequest(url, postData, context);
                Cache.set(context, cacheKey, jsonResponse);
            }
            catch (HttpServerError httpServerError)
            {
                jsonResponse = null;
            }
            return jsonResponse;
        }
    }

}
