package reaper.api.event;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.CacheKeys;
import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.cache.Cache;
import reaper.common.http.HttpRequestRunnable;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventDeleteApi extends HttpRequestRunnable
{
    public EventDeleteApi(Context context, String id)
    {
        super(UrlBuilder.build(Uri.EVENT_DELETE), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("event_id", id);

        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse)
    {
        Cache.delete(context, CacheKeys.EVENT_SUMMARY_LIST);

        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_DELETE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure()
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_DELETE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
