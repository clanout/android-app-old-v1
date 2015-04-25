package reaper.api.model.event;

import java.util.Comparator;

/**
 * Created by Aditya on 23-04-2015.
 */
public class EventRelevanceComparator implements Comparator<Event>
{
    private double coefficientFriendCount;
    private double coefficientInviterCount;
    private static final double ORGANISER_CONSTANT = 3000;
    private static final double GOING_CONSTANT = 2000;
    private static final double MAYBE_CONSTANT = 1000;


    public EventRelevanceComparator(double coefficientFriendCount, double coefficientInviterCount)
    {
        this.coefficientFriendCount = coefficientFriendCount;
        this.coefficientInviterCount = coefficientInviterCount;
    }

    @Override
    public int compare(Event event, Event event2)
    {
        Double importanceEvent1 = ((coefficientFriendCount * event.getFriendCount()) + (coefficientInviterCount * event.getInviterCount())) + getConstant(event);
        Double importanceEvent2 = ((coefficientFriendCount * event2.getFriendCount()) + (coefficientInviterCount * event2.getInviterCount())) + getConstant(event2);
        return importanceEvent2.compareTo(importanceEvent1);
    }

    private double getConstant(Event event)
    {
        double constant = 0;
        if (event.getRsvp() == Event.RSVP.YES)
        {
            constant = GOING_CONSTANT;
        }
        else if (event.getRsvp() == Event.RSVP.MAYBE)
        {
            constant = MAYBE_CONSTANT;
        }

        return  constant;
    }
}
