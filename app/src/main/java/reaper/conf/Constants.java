package reaper.conf;

/**
 * Created by Aditya on 05-04-2015.
 */
public class Constants
{
    public static class Servers
    {
        public static final String APP = "http://192.168.0.158:8080/v1.0/";
    }

    public static class SharedPreferences
    {
        public static final String APP = "PREFERENCES_APP";
    }

    public static class AppPreferenceKeys
    {
        public static final String SESSION_ID = "_SESSIONID";
    }

    public static class Location
    {
        public static final String ZONE = "ZONE";
        public static final String LATITUDE = "LATITUDE";
        public static final String LONGITUDE = "LONGITUDE";
    }

    public static class Cache
    {
        public static final String CACHE_FILE = "reaper_cache";
    }

    public static class Http
    {
        public static final String SESSION_ID = "_SESSIONID";
        public static final int CONNECTION_TIMEOUT = 3000;
        public static final int READ_TIMEOUT = 10000;
    }
}
