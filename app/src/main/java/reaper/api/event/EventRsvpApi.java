package reaper.api.event;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.api.model.event.EventRsvpStatus;
import reaper.common.http.HttpRequestRunnable;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventRsvpApi extends HttpRequestRunnable
{
    public EventRsvpApi(Context context, String id, EventRsvpStatus rsvpStatus)
    {
        super(UrlBuilder.build(Uri.EVENT_RSVP), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("event_id", id);
        postData.put("rsvp_status", String.valueOf(rsvpStatus));

        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse)
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_RSVP_EDIT_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure()
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_RSVP_EDIT_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
