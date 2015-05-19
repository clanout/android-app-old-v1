package reaper.api.endpoints.event;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.api.model.core.BasicJsonParser;
import reaper.api.model.core.ModelFactory;
import reaper.api.model.event.Suggestion;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 13-05-2015.
 */
public class EventSuggestionsApi extends HttpRequestTask {

    public EventSuggestionsApi(Context context, Map<String, String> postData) {
        super(UrlBuilder.build(Uri.EVENT_SUGGESTIONS), context);

        postData.put("_URI", Uri.EVENT_SUGGESTIONS);
        setPostData(postData);
    }

    @Override
    protected List<Suggestion> doInBackground(Void... params) {
        String jsonResponse = (String) super.doInBackground(params);

        if (jsonResponse != null)
        {
            List<String> eventSuggestionsList = BasicJsonParser.getList(jsonResponse, "recommendations");
            List<Suggestion> suggestions = ModelFactory.createModelList(eventSuggestionsList, Suggestion.class);
            return suggestions;
        }
        else
        {
            return new ArrayList<>();
        }
    }
}
