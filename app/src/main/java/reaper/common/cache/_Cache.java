package reaper.common.cache;

import android.content.Context;

/**
 * Created by harsh.pokharna on 2/7/2015.
 */
public class _Cache
{
    private static boolean isValid(Context context, String timestampKey)
    {
//        try
//        {
//            String cacheFile = Constants.SharedPreferences.CACHE;
//            SharedPreferences cachePreferences = context.getSharedPreferences(cacheFile, Context.MODE_PRIVATE);
//
//            String timestampStr = cachePreferences.getString(timestampKey, null);
//            Long storedTimestamp = Long.parseLong(timestampStr);
//            Long currentTimestamp = System.currentTimeMillis();
//
//            if ((currentTimestamp - storedTimestamp) <= Constants.Cache.CACHE_TIMESTAMP_LIMIT && (currentTimestamp - storedTimestamp) >= 0)
//            {
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
        return false;
    }

    private static void updateTimestamp(Context context, String timestampKey)
    {
//        try
//        {
//            String cacheFile = Constants.SharedPreferences.CACHE;
//            SharedPreferences cachePreferences = context.getSharedPreferences(cacheFile, Context.MODE_PRIVATE);
//
//            SharedPreferences.Editor editor = cachePreferences.edit();
//
//            editor.remove(timestampKey);
//            editor.putString(timestampKey, String.valueOf(System.currentTimeMillis()));
//
//            editor.commit();
//        }
//        catch (Exception e)
//        {
//        }
    }

    private static String getTimestampKey(String key)
    {
//        return Constants.Cache.TIMESTAMP_PREFIX + key;
        return null;
    }

    public static String get(Context context, String key)
    {
//        try
//        {
//            if (isValid(context, getTimestampKey(key)))
//            {
//                String cacleFile = Constants.SharedPreferences.CACHE;
//                SharedPreferences cachePreferences = context.getSharedPreferences(cacleFile, Context.MODE_PRIVATE);
//                return cachePreferences.getString(key, null);
//            }
//        }
//        catch (Exception e)
//        {
//        }
        return null;
    }

    public static void set(Context context, String key, String value)
    {
//        try
//        {
//            String cacheFile = Constants.SharedPreferences.CACHE;
//            SharedPreferences cachePreferences = context.getSharedPreferences(cacheFile, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = cachePreferences.edit();
//
//            editor.remove(key);
//            editor.putString(key, value);
//            editor.commit();
//
//            // Update timestamp
//            updateTimestamp(context, getTimestampKey(key));
//        }
//        catch (Exception e)
//        {
//        }
    }

    public static void delete(Context context, String key)
    {
//        try
//        {
//            String cacheFile = Constants.SharedPreferences.CACHE;
//            SharedPreferences cachePreferences = context.getSharedPreferences(cacheFile, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = cachePreferences.edit();
//            editor.remove(key);
//            editor.commit();
//        }
//        catch (Exception e)
//        {
//        }
    }
}