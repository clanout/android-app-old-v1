package reaper.api.endpoints.event;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 11-05-2015.
 */
public class EventCreateApi extends HttpRequestTask {

    public EventCreateApi(Context context, Map<String, String> postdata) {
        super(UrlBuilder.build(Uri.EVENT_CREATE), context);

        postdata.put("_URI", Uri.EVENT_CREATE);
        setPostData(postdata);
    }

    @Override
    protected String doInBackground(Void... params) {
        String jsonResponse = (String) super.doInBackground(params);

        if(jsonResponse == null){
            return null;
        }else{
            return  jsonResponse;
        }
    }
}
