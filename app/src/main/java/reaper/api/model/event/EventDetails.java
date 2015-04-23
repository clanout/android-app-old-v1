package reaper.api.model.event;

import java.util.List;

import reaper.api.model.core.Model;

/**
 * Created by Aditya on 06-04-2015.
 */
public class EventDetails implements Model
{
    public static class Attendee{
        private String id;
        private String name;
        private boolean isFriend;
        private EventRsvpStatus rsvp;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public boolean isFriend()
        {
            return isFriend;
        }

        public void setFriend(boolean isFriend)
        {
            this.isFriend = isFriend;
        }

        public EventRsvpStatus getRsvp()
        {
            return rsvp;
        }

        public void setRsvp(EventRsvpStatus rsvp)
        {
            this.rsvp = rsvp;
        }
    }

    public static class InviteListuser
    {
        private String id;
        private String name;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    private String id;
    private String description;
    private List<Attendee> attendees;
    private List<InviteListuser> invited;
    private List<InviteListuser> inviters;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Attendee> getAttendees()
    {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees)
    {
        this.attendees = attendees;
    }

    public List<InviteListuser> getInvited()
    {
        return invited;
    }

    public void setInvited(List<InviteListuser> invited)
    {
        this.invited = invited;
    }

    public List<InviteListuser> getInviters()
    {
        return inviters;
    }

    public void setInviters(List<InviteListuser> inviters)
    {
        this.inviters = inviters;
    }
}
