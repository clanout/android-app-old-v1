package reaper.api;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import reaper.api.core.BasicJsonParser;
import reaper.api.core.CacheKeys;
import reaper.api.core.ModelFactory;
import reaper.api.core.Uri;
import reaper.api.model.event.EventSummary;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public class EventSummaryListApi extends HttpRequestTask
{
    public EventSummaryListApi(Context context)
    {
        super(UrlBuilder.build(Uri.EVENT_SUMMARY_LIST), context, CacheKeys.EVENT_SUMMARY_LIST);
    }

    @Override
    protected List<EventSummary> doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);
        if (jsonResponse != null)
        {
            List<String> eventSummaryListJson = BasicJsonParser.getList(jsonResponse, "event_summary_list");
            return ModelFactory.createModelList(eventSummaryListJson, EventSummary.class);
        }
        else
        {
            return new ArrayList<>();
        }
    }
}
