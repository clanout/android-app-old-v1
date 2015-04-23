package reaper.api.model.event;

import java.util.Comparator;

/**
 * Created by Aditya on 23-04-2015.
 */
public class EventRelevanceComparator implements Comparator<Event>
{
    private double coefficientFriendCount;
    private double coefficientInviterCount;

    public EventRelevanceComparator(double coefficientFriendCount, double coefficientInviterCount)
    {
        this.coefficientFriendCount = coefficientFriendCount;
        this.coefficientInviterCount = coefficientInviterCount;
    }

    @Override
    public int compare(Event event, Event event2)
    {
        Double importanceEvent1 = ((coefficientFriendCount*event.getFriendCount()) + (coefficientInviterCount*event.getInviterCount()));
        Double importanceEvent2 = ((coefficientFriendCount*event2.getFriendCount()) + (coefficientInviterCount*event2.getInviterCount()));
        return importanceEvent1.compareTo(importanceEvent2);
    }
}
