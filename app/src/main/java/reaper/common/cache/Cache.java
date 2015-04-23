package reaper.common.cache;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import reaper.conf.Constants;

/**
 * Created by Aditya on 22-04-2015.
 */
public class Cache implements Serializable
{
    private static Cache instance;

    private HashMap<String, Object> data;

    private Cache()
    {
        data = new HashMap<>();
    }

    public static Cache getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException();
        }

        return instance;
    }

    public void put(String key, Object value)
    {
        data.put(key, value);
    }

    public Object get(String key)
    {
        return data.get(key);
    }

    public void remove(String key)
    {
        data.remove(key);
    }

    public static void init(Context context)
    {
        try
        {
            File file = context.getFileStreamPath(Constants.Cache.CACHE_FILE);
            if (file.exists())
            {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                instance = (Cache) inputStream.readObject();
                inputStream.close();
            }
            else
            {
                instance = new Cache();
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            Log.d("APP", "Cache error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void commit(Context context)
    {
        try
        {
            File file = context.getFileStreamPath(Constants.Cache.CACHE_FILE);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file, false));
            outputStream.writeObject(instance);
            outputStream.close();
        }
        catch (IOException e)
        {
            Log.d("APP", e.getMessage());
            e.printStackTrace();
        }
    }
}
