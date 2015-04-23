package reaper.api.model.event;

import java.util.Comparator;

/**
 * Created by Aditya on 23-04-2015.
 */
public class EventDateTimeComparator implements Comparator<Event>
{
    @Override
    public int compare(Event event, Event event2)
    {
        return event.getStartTime().compareTo(event2.getStartTime());
    }
}
