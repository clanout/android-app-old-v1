package reaper.app.root;

import android.app.Application;
import android.util.Log;

import reaper.common.cache.Cache;

/**
 * Created by Aditya on 22-04-2015.
 */
public class Reaper extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("APP", "App started...");
        init();
    }

    protected void init()
    {
        Cache.init(this);
    }
}
