package reaper.api.model.event;

import reaper.R;
import reaper.api.core.Model;

/**
 * Created by Aditya on 06-04-2015.
 */
public class Event implements Model
{
    private String id;
    private String title;
    private EventType type;
    private EventCategory category;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private EventLocation location;
    private int attendeeCount;
    private int friendCount;
    private EventRsvpStatus rsvpStatus;
    private boolean isOrganizer;
    private boolean isInvited;
    private int notificationCount;
    private int eventIconId;
    private int statusIconId;

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public EventType getType()
    {
        return type;
    }

    public EventCategory getCategory()
    {
        return category;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public EventLocation getLocation()
    {
        return location;
    }

    public int getAttendeeCount()
    {
        return attendeeCount;
    }

    public int getFriendCount()
    {
        return friendCount;
    }

    public EventRsvpStatus getRsvpStatus()
    {
        return rsvpStatus;
    }

    public boolean isOrganizer()
    {
        return isOrganizer;
    }

    public boolean isInvited()
    {
        return isInvited;
    }

    public int getNotificationCount()
    {
        return notificationCount;
    }

    public int getEventIconId(){
        return R.drawable.ic_launcher;
    }

    public int getStatusIconId(){
        return R.drawable.ic_launcher;
    }
}
