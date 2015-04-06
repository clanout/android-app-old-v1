package reaper.api.event;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.api.core.BasicJsonParser;
import reaper.api.core.ModelFactory;
import reaper.api.model.event.EventDetails;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventDetailsApi extends HttpRequestTask
{
    public EventDetailsApi(Context context, String id)
    {
        super(UrlBuilder.build(Uri.EVENT_DETAILS), context, null);
        disableCaching();

        Map<String, String> postData = new HashMap<>();
        postData.put("event_id", id);

        setPostData(postData);
    }

    @Override
    protected EventDetails doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);
        if (jsonResponse != null)
        {
            String eventDetails = BasicJsonParser.getValue(jsonResponse, "event_details");
            return ModelFactory.create(eventDetails, EventDetails.class);
        }
        else
        {
            return null;
        }
    }
}
