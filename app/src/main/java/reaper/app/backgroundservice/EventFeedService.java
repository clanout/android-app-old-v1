package reaper.app.backgroundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Aditya on 25-04-2015.
 */
public class EventFeedService extends Service
{
    private EventFeedListener eventFeedListener;
    private IBinder binder = new EventFeedBinder();
    private Thread worker;

    public class EventFeedBinder extends Binder
    {
        public void setEventFeedListener(EventFeedListener listener)
        {
            Log.d("APP", "Listener set");
            EventFeedService.this.eventFeedListener = listener;
        }
    }

    private class Work implements Runnable
    {
        @Override
        public void run()
        {
            for (int i = 0; i < 60; i++)
            {
                if (eventFeedListener != null)
                {
                    eventFeedListener.onEventFeedUpdate(null);
                }
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("APP", "Service created");
        worker = new Thread(new Work());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("APP", "Service started");
        worker.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d("APP", "Service bound");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        Log.d("APP", "Service unbound");
        eventFeedListener = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent)
    {
        super.onRebind(intent);
        Log.d("APP", "Service rebound");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("APP", "Service destroyed");
    }
}
