package reaper.api.endpoints.accounts;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.api.model.user.User;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 10-05-2015.
 */
public class FriendBlockApi extends HttpRequestThread {

    public FriendBlockApi(Context context, List<String> blockedUsers, List<String> unblockedUsers) {
        super(UrlBuilder.build(Uri.FRIEND_BLOCK), context);

        Map<String, String> postData = new HashMap<>();

        Gson gson = new Gson();
        postData.put("blocked_users", gson.toJson(blockedUsers));
        postData.put("unblocked_users", gson.toJson(unblockedUsers));
        postData.put("_URI", Uri.FRIEND_BLOCK);

        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse) {

        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.FRIEND_BLOCK_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure() {

        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.FRIEND_BLOCK_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
