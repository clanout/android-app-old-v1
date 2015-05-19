package reaper.app.fragment.event.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

import reaper.R;

/**
 * Created by harsh on 12-05-2015.
 */
public class StartDateTimeTabFragment extends Fragment {

    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_datetime_tab, container, false);
        datePicker = (DatePicker) view.findViewById(R.id.dpStartDateTime);
        timePicker = (TimePicker) view.findViewById(R.id.tpStartDateTime);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        datePicker.setMinDate(System.currentTimeMillis() - 1000);

    }

    public DateTime getTime() {

        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1;
        int date = datePicker.getDayOfMonth();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String timeZone = DateTime.now().toString(DateTimeFormat.forPattern("Z"));

        StringBuilder formattedDate = new StringBuilder();
        formattedDate.append(year);
        formattedDate.append("-");
        formattedDate.append(String.format("%02d", month));
        formattedDate.append("-");
        formattedDate.append(String.format("%02d", date));
        formattedDate.append("T");
        formattedDate.append(String.format("%02d", hour));
        formattedDate.append(":");
        formattedDate.append(String.format("%02d", minute));
        formattedDate.append(":00.000");
        formattedDate.append(timeZone);
        DateTime dateTime = DateTime.parse(formattedDate.toString());


        return dateTime;
    }
}
