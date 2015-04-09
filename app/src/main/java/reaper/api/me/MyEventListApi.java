package reaper.api.me;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import reaper.api.constants.CacheKeys;
import reaper.api.constants.Uri;
import reaper.api.core.BasicJsonParser;
import reaper.api.core.ModelFactory;
import reaper.api.model.event.Event;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 07-04-2015.
 */
public class MyEventListApi extends HttpRequestTask
{
    public MyEventListApi(Context context)
    {
        super(UrlBuilder.build(Uri.MY_EVENTS), context, CacheKeys.MY_EVENT_SUMMARY_LIST);
    }

    @Override
    protected List<Event> doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);

        if (jsonResponse != null)
        {
            List<String> myEventSummaryListJson = BasicJsonParser.getList(jsonResponse, "my_event_summary_list");
            return ModelFactory.createModelList(myEventSummaryListJson, Event.class);
        }
        else
        {
            return new ArrayList<>();
        }
    }
}
