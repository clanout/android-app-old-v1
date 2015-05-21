package reaper.api.model.chat;

import reaper.api.model.core.Model;

/**
 * Created by harsh on 21-05-2015.
 */
public class ChatMessage implements Model {

    private long id;
    private boolean isMe;
    private String message;
    private String senderName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
