package reaper.api.event;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 07-04-2015.
 */
public class EventInviteUsersApi extends HttpRequestThread
{
    public EventInviteUsersApi(Context context, String eventId, List<String> userId)
    {
        super(UrlBuilder.build(Uri.EVENT_INVITE), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("event_id", eventId);
        postData.put("user_id", String.valueOf(userId));

        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse)
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_INVITE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure()
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_INVITE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
