package reaper.api.event;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import reaper.api.core.BasicJsonParser;
import reaper.api.constants.CacheKeys;
import reaper.api.core.ModelFactory;
import reaper.api.constants.Uri;
import reaper.api.model.event.Event;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventListApi extends HttpRequestTask
{
    public EventListApi(Context context)
    {
        super(UrlBuilder.build(Uri.EVENT_SUMMARY_LIST), context, CacheKeys.EVENT_SUMMARY_LIST);
    }

    @Override
    protected List<Event> doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);
        if (jsonResponse != null)
        {
            List<String> eventSummaryListJson = BasicJsonParser.getList(jsonResponse, "event_summary_list");
            return ModelFactory.createModelList(eventSummaryListJson, Event.class);
        }
        else
        {
            return new ArrayList<>();
        }
    }
}
