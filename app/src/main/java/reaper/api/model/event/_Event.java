package reaper.api.model.event;

import org.joda.time.DateTime;

import reaper.api.model.core.Model;

/**
 * Created by Aditya on 06-04-2015.
 */
public class _Event implements Model
{
    private String id;
    private String title;
    private EventType type;
    private String category;
    private boolean isFinalized;
    private DateTime startTime;
    private DateTime endTime;
    private String organizerId;
    private EventLocation location;
    private int attendeeCount;
    private int friendCount;
    private int inviterCount;
    private EventRsvpStatus rsvpStatus;
    private String chatId;
    private DateTime lastUpdated;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public EventType getType()
    {
        return type;
    }

    public void setType(EventType type)
    {
        this.type = type;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public boolean isFinalized()
    {
        return isFinalized;
    }

    public void setFinalized(boolean isFinalized)
    {
        this.isFinalized = isFinalized;
    }

    public DateTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(DateTime startTime)
    {
        this.startTime = startTime;
    }

    public DateTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(DateTime endTime)
    {
        this.endTime = endTime;
    }

    public String getOrganizerId()
    {
        return organizerId;
    }

    public void setOrganizerId(String organizerId)
    {
        this.organizerId = organizerId;
    }

    public EventLocation getLocation()
    {
        return location;
    }

    public void setLocation(EventLocation location)
    {
        this.location = location;
    }

    public int getAttendeeCount()
    {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount)
    {
        this.attendeeCount = attendeeCount;
    }

    public int getFriendCount()
    {
        return friendCount;
    }

    public void setFriendCount(int friendCount)
    {
        this.friendCount = friendCount;
    }

    public int getInviterCount()
    {
        return inviterCount;
    }

    public void setInviterCount(int inviterCount)
    {
        this.inviterCount = inviterCount;
    }

    public EventRsvpStatus getRsvpStatus()
    {
        return rsvpStatus;
    }

    public void setRsvpStatus(EventRsvpStatus rsvpStatus)
    {
        this.rsvpStatus = rsvpStatus;
    }

    public String getChatId()
    {
        return chatId;
    }

    public void setChatId(String chatId)
    {
        this.chatId = chatId;
    }

    public DateTime getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }
}
