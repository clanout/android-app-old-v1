package reaper.api.endpoints.event;

import android.content.Context;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reaper.api.constants.CacheKeys;
import reaper.api.constants.Uri;
import reaper.api.model.core.BasicJsonParser;
import reaper.api.model.core.ModelFactory;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventRelevanceComparator;
import reaper.common.cache.Cache;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 06-04-2015.
 */
public abstract class EventListApi extends HttpRequestTask
{
    public EventListApi(Context context, String zone)
    {
        super(UrlBuilder.build(Uri.EVENT_SUMMARY_LIST), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("zone", zone);
        postData.put("_URI", Uri.EVENT_SUMMARY_LIST);

        setPostData(postData);
    }

    @Override
    protected List<Event> doInBackground(Void... params)
    {
        Cache cache = Cache.getInstance();
        Map<String, Event> eventMap = (Map<String, Event>) cache.get(CacheKeys.EVENT_SUMMARY_LIST);

        if (eventMap == null)
        {
            String jsonResponse = (String) super.doInBackground(params);
            if (jsonResponse != null)
            {
                List<String> eventSummaryListJson = BasicJsonParser.getList(jsonResponse, "events");
                List<Event> events = ModelFactory.createModelList(eventSummaryListJson, Event.class);

                eventMap = new HashMap<>();
                Map<String, DateTime> eventTimestamp = new HashMap<>();
                DateTime now = DateTime.now();
                for (Event event : events)
                {
                    eventMap.put(event.getId(), event);
                    eventTimestamp.put(event.getId(), now);
                }

                cache.put(CacheKeys.EVENT_SUMMARY_LIST, eventMap);
                cache.put(CacheKeys.EVENT_SUMMARY_LIST_LAST_UPDATED, eventTimestamp);
            }
            else
            {
                eventMap = new HashMap<>();
                cache.remove(CacheKeys.EVENT_SUMMARY_LIST_LAST_UPDATED);
            }
        }

        List<Event> events = new ArrayList<>(eventMap.values());
        Collections.sort(events, new EventRelevanceComparator(0.4,0.6));
        return events;
    }
}
