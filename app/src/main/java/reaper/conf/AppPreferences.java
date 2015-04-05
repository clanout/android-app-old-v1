package reaper.conf;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by harsh.pokharna on 1/1/2015.
 */
public class AppPreferences
{
    public static String get(Context context, String key)
    {
        try
        {
            String cookieSharedPreferencesFile = Constants.SharedPreferences.APP;
            SharedPreferences appPreferences = context.getSharedPreferences(cookieSharedPreferencesFile, Context.MODE_PRIVATE);
            return appPreferences.getString(key, null);
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public static void set(Context context, String key, String value)
    {
        try
        {
            String cookieSharedPreferencesFile = Constants.SharedPreferences.APP;
            SharedPreferences appPreferences = context.getSharedPreferences(cookieSharedPreferencesFile, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = appPreferences.edit();

            editor.remove(key);
            editor.putString(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
        }
    }
}
