package reaper.api.user;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.api.core.BasicJsonParser;
import reaper.api.core.ModelFactory;
import reaper.api.model.user.UserEvents;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 07-04-2015.
 */
public class UserEventsApi extends HttpRequestTask
{
    public UserEventsApi(Context context, String id)
    {
        super(UrlBuilder.build(Uri.USER_EVENTS), context, null);
        disableCaching();

        Map<String, String> postData = new HashMap<>();
        postData.put("user_id", id);

        setPostData(postData);
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);

        if(jsonResponse!=null){
            String userEvents = BasicJsonParser.getValue(jsonResponse, "user_events");
            return ModelFactory.create(userEvents, UserEvents.class);
        }else{
            return null;
        }
    }
}
