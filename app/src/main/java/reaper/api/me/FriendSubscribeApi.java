package reaper.api.me;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 07-04-2015.
 */
public class FriendSubscribeApi extends HttpRequestThread
{
    public FriendSubscribeApi(Context context, String id, boolean isSubscribed)
    {
        super(UrlBuilder.build(Uri.FRIEND_SUBSCRIBE), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("id", id);
        postData.put("is_subscribed", String.valueOf(isSubscribed));

        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse)
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.FRIEND_SUBSCRIBE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure()
    {
        if (handler != null)
        {
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.FRIEND_SUBSCRIBE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
