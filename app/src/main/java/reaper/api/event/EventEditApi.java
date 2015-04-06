package reaper.api.event;

import android.content.Context;

import java.util.Map;

import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventEditApi extends HttpRequestTask
{
    public EventEditApi(Context context, Map<String, String> postData)
    {
        super(UrlBuilder.build(Uri.EVENT_EDIT), context, null);
        disableCaching();
        setPostData(postData);
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
