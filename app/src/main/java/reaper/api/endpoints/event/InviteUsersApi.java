package reaper.api.endpoints.event;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 13-05-2015.
 */
public class InviteUsersApi extends HttpRequestThread {

    public InviteUsersApi(Context context, List<String> userIds, List<String> phoneNumbers, String eventId) {
        super(UrlBuilder.build(Uri.EVENT_INVITE), context);

        Gson gson = new Gson();

        Map<String, String> postdata = new HashMap<>();
        postdata.put("event_id", eventId);
        postdata.put("friends_invited",gson.toJson(userIds));
        postdata.put("contacts_invited", gson.toJson(phoneNumbers));

        setPostData(postdata);
    }

    @Override
    public void onSuccess(String jsonResponse) {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_INVITE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }

    }

    @Override
    public void onFailure() {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_INVITE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }

    }
}
