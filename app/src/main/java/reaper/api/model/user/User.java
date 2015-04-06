package reaper.api.model.user;

import reaper.api.core.Model;

/**
 * Created by Aditya on 06-04-2015.
 */
public class User implements Model
{
    private String id;
    private String firstname;
    private String lastname;
    private boolean isBlocked;
    private boolean isSubscribed;
    private boolean isFriend;
    public int profilePic;

    public String getId()
    {
        return id;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public boolean isBlocked()
    {
        return isBlocked;
    }

    public boolean isSubscribed()
    {
        return isSubscribed;
    }

    public boolean isFriend()
    {
        return isFriend;
    }

    public int getProfilePic()
    {
        return profilePic;
    }
}
