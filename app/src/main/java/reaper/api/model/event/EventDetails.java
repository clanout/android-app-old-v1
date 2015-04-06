package reaper.api.model.event;

import java.util.List;

import reaper.api.core.Model;
import reaper.api.model.user.User;

/**
 * Created by Aditya on 06-04-2015.
 */
public class EventDetails implements Model
{
    private String id;
    private String description;
    private boolean isFinalized;
    private List<User> goingUsers;
    private List<User> maybeUsers;
    private List<User> invitedUsers;

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isFinalized()
    {
        return isFinalized;
    }

    public List<User> getGoingUsers()
    {
        return goingUsers;
    }

    public List<User> getMaybeUsers()
    {
        return maybeUsers;
    }

    public List<User> getInvitedUsers()
    {
        return invitedUsers;
    }
}
