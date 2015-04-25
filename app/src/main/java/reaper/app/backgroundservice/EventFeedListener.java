package reaper.app.backgroundservice;

import java.util.List;

import reaper.api.model.event.Event;

/**
 * Created by Aditya on 25-04-2015.
 */
public interface EventFeedListener
{
    public void onEventFeedUpdate(List<Event> events);
}
