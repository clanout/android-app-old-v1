package reaper.api.endpoints.event;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 19-05-2015.
 */
public class EventDeleteApi extends HttpRequestThread {

    public EventDeleteApi(Context context, String eventId) {
        super(UrlBuilder.build(Uri.EVENT_DELETE), context);

        Map<String, String> postdata = new HashMap<>();
        postdata.put("event_id", eventId);
        postdata.put("_URI", Uri.EVENT_DELETE);
        setPostData(postdata);
    }

    @Override
    public void onSuccess(String jsonResponse) {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_DELETE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure() {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_DELETE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
