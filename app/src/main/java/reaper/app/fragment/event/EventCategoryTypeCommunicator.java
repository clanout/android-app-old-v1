package reaper.app.fragment.event;

import reaper.api.model.event.Event;
import reaper.api.model.event.EventCategory;

/**
 * Created by harsh on 14-05-2015.
 */
public interface EventCategoryTypeCommunicator {

    public void onEventCategoryTypeChanged(Event.Type eventType, EventCategory eventCategory);
}
