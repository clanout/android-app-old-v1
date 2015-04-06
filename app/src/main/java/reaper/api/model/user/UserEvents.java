package reaper.api.model.user;

import java.util.List;

import reaper.api.core.Model;
import reaper.api.model.event.Event;

/**
 * Created by Aditya on 06-04-2015.
 */
public class UserEvents implements Model
{
    private String id;
    private List<Event> goingEvents;
    private List<Event> maybeEvents;

    public String getId()
    {
        return id;
    }

    public List<Event> getGoingEvents()
    {
        return goingEvents;
    }

    public List<Event> getMaybeEvents()
    {
        return maybeEvents;
    }
}
