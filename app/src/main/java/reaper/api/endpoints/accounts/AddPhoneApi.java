package reaper.api.endpoints.accounts;

import android.content.Context;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import reaper.api.constants.HandlerMessageStatusCode;
import reaper.api.constants.Uri;
import reaper.common.http.HttpRequestThread;
import reaper.common.http.UrlBuilder;

/**
 * Created by harsh on 22-05-2015.
 */
public class AddPhoneApi extends HttpRequestThread {

    public AddPhoneApi(Context context, String phoneNumber) {
        super(UrlBuilder.build(Uri.ADD_PHONE), context);

        Map<String, String> postData = new HashMap<>();

        postData.put("phone", phoneNumber);
        postData.put("_URI", Uri.FRIEND_BLOCK);

        setPostData(postData);
    }

    @Override
    public void onSuccess(String jsonResponse) {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.ADD_PHONE_SUCCESS;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }

    @Override
    public void onFailure() {
        if(handler!= null){
            Message message = Message.obtain();
            message.what = HandlerMessageStatusCode.ADD_PHONE_FAILURE;
            handler.sendMessageAtFrontOfQueue(message);
        }
    }
}
