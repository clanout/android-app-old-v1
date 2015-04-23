package reaper.api.model.event;

import java.util.Comparator;

/**
 * Created by Aditya on 23-04-2015.
 */
public class EventDistanceComparator implements Comparator<Event>
{
    private double x;
    private double y;

    public EventDistanceComparator(double longitude, double latitude)
    {
        this.x = longitude;
        this.y = latitude;
    }

    @Override
    public int compare(Event event, Event event2)
    {
        Event.Location location = event.getLocation();
        Double distance = Math.sqrt(Math.pow(location.getX() - x, 2) + Math.pow(location.getY() - y, 2));

        Event.Location location2 = event2.getLocation();
        Double distance2 = Math.sqrt(Math.pow(location2.getX() - x, 2) + Math.pow(location2.getY() - y, 2));

        return distance.compareTo(distance2);
    }
}
