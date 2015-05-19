package reaper.app.fragment.event.dialogfragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalTime;

import reaper.R;

/**
 * Created by harsh on 12-05-2015.
 */
public class SelectTimeDialogFragment extends DialogFragment implements View.OnClickListener {

    private  SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private Button done;
    private StartDateTimeTabFragment startDateTimeTabFragment;
    private EndDateTimeTabFragment endDateTimeTabFragment;
    private SelectedTimeCommunicator selectedTimeCommunicator;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_select_datetime, container, false);
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        done = (Button) view.findViewById(R.id.bSelectTimeDialogFragmentDone);
        viewPager = (ViewPager) view.findViewById(R.id.vpDialogFragmentSelectTime);
        viewPager.setAdapter(sectionsPagerAdapter);

        startDateTimeTabFragment = new StartDateTimeTabFragment();
        endDateTimeTabFragment = new EndDateTimeTabFragment();
        done.setOnClickListener(this);

        return view;
    }

    public void setSelectedTimeCommunicator(SelectedTimeCommunicator selectedTimeCommunicator){
        this.selectedTimeCommunicator = selectedTimeCommunicator;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bSelectTimeDialogFragmentDone){
            DateTime startDateTime = startDateTimeTabFragment.getTime();
            DateTime endDateTime = endDateTimeTabFragment.getTime();

            if(endDateTime.isBefore(startDateTime)){
               endDateTime = endDateTime.withDate(startDateTime.toLocalDate()).withTime(LocalTime.MIDNIGHT).plusDays(1);
            }

            selectedTimeCommunicator.setTime(startDateTime, endDateTime);
            dismiss();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return startDateTimeTabFragment;
            }

            if(position == 1){
                return endDateTimeTabFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Start Date Time";
                case 1:
                    return "End Date Time";
            }
            return null;
        }

    }
}
