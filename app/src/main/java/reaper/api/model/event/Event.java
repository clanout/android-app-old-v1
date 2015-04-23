package reaper.api.model.event;

import org.joda.time.DateTime;

import java.io.Serializable;

import reaper.api.model.core.Model;

public class Event implements Model
{
    public static enum Type implements Serializable
    {
        PUBLIC(0),
        INVITE_ONLY(1);

        private int code;

        private Type(int code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }

        public static Type fromCode(int code)
        {
            if (code == 0)
            {
                return PUBLIC;
            }
            else
            {
                return INVITE_ONLY;
            }
        }
    }

    public static enum RSVP implements Serializable
    {
        YES,
        MAYBE,
        NO
    }

    public static class Location implements Serializable
    {
        private Double x;
        private Double y;
        private String name;
        private String zone;

        public Double getX()
        {
            return x;
        }

        public void setX(double x)
        {
            this.x = x;
        }

        public Double getY()
        {
            return y;
        }

        public void setY(double y)
        {
            this.y = y;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getZone()
        {
            return zone;
        }

        public void setZone(String zone)
        {
            this.zone = zone;
        }
    }

    private String id;
    private String title;
    private Type type;
    private String category;
    private boolean isFinalized;
    private DateTime startTime;
    private DateTime endTime;
    private String organizerId;
    private Location location;
    private int attendeeCount;
    private String chatId;
    private DateTime lastUpdated;

    private RSVP rsvp;
    private int friendCount;
    private int inviterCount;

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

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
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

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
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

    public RSVP getRsvp()
    {
        return rsvp;
    }

    public void setRsvp(RSVP rsvp)
    {
        this.rsvp = rsvp;
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

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof Event))
        {
            return false;
        }
        else
        {
            Event other = (Event) o;
            if (id.equals(other.id))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public String toString()
    {
        return title;
    }
}
