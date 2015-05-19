package reaper.api.endpoints.event;

import android.content.Context;

import java.util.Map;

import reaper.api.constants.Uri;
import reaper.api.model.core.BasicJsonParser;
import reaper.api.model.core.ModelFactory;
import reaper.api.model.event.Event;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 18-05-2015.
 */
public class EventEditApi extends HttpRequestTask {

    public EventEditApi(Context context, Map<String, String> postdata) {
        super(UrlBuilder.build(Uri.EVENT_EDIT), context);

        postdata.put("_URI", Uri.EVENT_EDIT);
        setPostData(postdata);
    }

    @Override
    protected Event doInBackground(Void... params) {
        String jsonResponse = (String) super.doInBackground(params);

        if (jsonResponse != null) {
            String event = BasicJsonParser.getValue(jsonResponse, "event");
            return ModelFactory.create(event, Event.class);
        } else {
            return null;
        }
    }
}
