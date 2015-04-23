package reaper.api.model.user;

import java.util.List;

import reaper.api.model.core.Model;
import reaper.api.model.event._Event;

/**
 * Created by Aditya on 06-04-2015.
 */
public class UserEvents implements Model
{
    private String id;
    private List<_Event> goingEvents;
    private List<_Event> maybeEvents;

    public String getId()
    {
        return id;
    }

    public List<_Event> getGoingEvents()
    {
        return goingEvents;
    }

    public List<_Event> getMaybeEvents()
    {
        return maybeEvents;
    }
}
