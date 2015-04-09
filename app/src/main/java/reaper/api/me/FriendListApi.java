package reaper.api.me;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import reaper.api.constants.CacheKeys;
import reaper.api.constants.Uri;
import reaper.api.core.BasicJsonParser;
import reaper.api.core.ModelFactory;
import reaper.api.model.user.User;
import reaper.common.http.HttpRequestTask;
import reaper.common.http.UrlBuilder;

/**
 * Created by Aditya on 07-04-2015.
 */
public class FriendListApi extends HttpRequestTask
{
    public FriendListApi(Context context)
    {
        super(UrlBuilder.build(Uri.MY_FRIENDS_LIST), context, CacheKeys.MY_FRIENDS_LIST);
    }

    @Override
    protected List<User> doInBackground(Void... params)
    {
        String jsonResponse = (String) super.doInBackground(params);

        if (jsonResponse != null)
        {
            List<String> myFriendsList = BasicJsonParser.getList(jsonResponse, "my_friends_list");
            return ModelFactory.createModelList(myFriendsList, User.class);
        }
        else
        {
            return new ArrayList<>();
        }
    }
}
