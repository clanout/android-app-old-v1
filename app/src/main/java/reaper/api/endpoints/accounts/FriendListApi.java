package reaper.api.endpoints.accounts;

import android.content.Context;

import java.util.List;

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
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        String jsonResponse = (String) super.doInBackground(params);
        if (jsonResponse != null)
        {
            List<String> friends = BasicJsonParser.getList(jsonResponse, "friend_list");
            List<User> friendList = ModelFactory.createModelList(friends, User.class);
            return friendList;

        }
        else
        {
            return null;
        }
    }
}
