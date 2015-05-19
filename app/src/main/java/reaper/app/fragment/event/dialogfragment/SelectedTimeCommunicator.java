package reaper.app.fragment.event.dialogfragment;

import org.joda.time.DateTime;

/**
 * Created by harsh on 12-05-2015.
 */
public interface SelectedTimeCommunicator {

    public void setTime(DateTime startDateTime, DateTime endDateTime);
}
