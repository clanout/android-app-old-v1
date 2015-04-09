package reaper.api.event;

import android.content.Context;
import android.os.Message;

import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventCreateApi extends HttpRequestThread
{
    public EventCreateApi(Context context, Map<String, String> postData)
    {
        super(UrlBuilder.build(Uri.EVENT_CREATE), context);
        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse)
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_CREATE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure()
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_CREATE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
