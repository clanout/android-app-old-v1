package reaper.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import reaper.local.reaper.R;

/**
 * Created by reaper on 05-04-2015.
 */
public class CalendarDialogFragment extends DialogFragment implements View.OnClickListener {

    private Calendar calendar;
    private CalendarPickerView calendarPickerView;
    private Button cancel, done;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_calendar, null);
        calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);

        calendarPickerView = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendarPickerView.init(today, calendar.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        cancel = (Button) view.findViewById(R.id.bCalendarCancel);
        done = (Button) view.findViewById(R.id.bcalendarDone);

        cancel.setOnClickListener(this);
        done.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
