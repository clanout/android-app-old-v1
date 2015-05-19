package reaper.api.endpoints.event;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reaper.api.constants.Uri;
import reaper.api.model.core.BasicJsonParser;
import reaper.api.model.core.ModelFactory;
import reaper.api.model.user.User;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 14-05-2015.
 */
public class LocalFriendsApi extends HttpRequestTask {

    public LocalFriendsApi(Context context, String zone) {
        super(UrlBuilder.build(Uri.MY_FRIENDS_LIST), context);

        Map<String, String> postData = new HashMap<>();
        postData.put("zone", zone);
        postData.put("_URI", Uri.MY_FRIENDS_LIST);

        setPostData(postData);
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        String jsonResponse = (String) super.doInBackground(params);
        if (jsonResponse != null)
        {
            List<String> friends = BasicJsonParser.getList(jsonResponse, "friends");
            List<User> friendList = ModelFactory.createModelList(friends, User.class);
            return friendList;

        }
        else
        {
            return new ArrayList<>();
        }
    }
}
