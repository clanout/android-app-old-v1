package reaper.app.list.event;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import reaper.R;

/**
 * Created by Aditya on 25-04-2015.
 */
public class EventListViewPagerAdapter extends PagerAdapter
{
    public Object instantiateItem(ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.primaryContentCardView;
                break;
            case 1:
                resId = R.id.secondaryContentFrameLayout;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == (View) object;
    }
}
