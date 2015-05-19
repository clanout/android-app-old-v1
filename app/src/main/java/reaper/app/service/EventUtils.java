package reaper.app.service;

import reaper.api.model.event.Event;

/**
 * Created by harsh on 14-05-2015.
 */
public class EventUtils {

    public static String userId = "1";

    public static Boolean canEdit(Event event){

        if ((event.getRsvp() == Event.RSVP.YES && !event.isFinalized()) || (userId.equals(event.getOrganizerId()))){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean canViewChat(Event event){
        if(event.getRsvp() == Event.RSVP.YES || event.getRsvp() == Event.RSVP.MAYBE){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean canInviteFriends(Event event){

        if(event.getRsvp() == Event.RSVP.YES){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean canDeleteEvent(Event event){

        if(userId.equals(event.getOrganizerId())){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean canFinaliseEvent(Event event){

        if(userId.equals(event.getOrganizerId())){
            return true;
        }else{
            return false;
        }
    }

}
