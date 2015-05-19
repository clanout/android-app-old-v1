package reaper.api.endpoints.event;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.api.model.event.Event;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 13-05-2015.
 */
public class EventUpdateRsvpApi extends HttpRequestThread {


    public EventUpdateRsvpApi(Context context, String eventId, Event.RSVP rsvp) {
        super(UrlBuilder.build(Uri.EVENT_RSVP), context);

        Map<String, String> postdata = new HashMap<>();
        postdata.put("event_id", eventId);
        postdata.put("rsvp_status", String.valueOf(rsvp));
        postdata.put("_URI", Uri.EVENT_RSVP);
        setPostData(postdata);
    }

    @Override
    public void onSuccess(String jsonResponse) {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_RSVP_EDIT_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure() {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.EVENT_RSVP_EDIT_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
