package reaper.api.endpoints.accounts;

import android.content.Context;

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
 * Created by harsh on 08-05-2015.
 */
public abstract class FriendListApi extends HttpRequestTask {


    public FriendListApi(Context context) {
        super(UrlBuilder.build(Uri.MY_FRIENDS_LIST), context);

        Map<String, String> postData = new HashMap<>();
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
            return null;
        }
    }
}
