package reaper.conf;

/**
 * Created by Aditya on 05-04-2015.
 */
public class Constants
{
    public static class Servers
    {
        public static final String APP = "http://localhost:8080/";
    }

    public static class SharedPreferences
    {
        public static final String APP = "PREFERENCES_APP";
        public static final String CACHE = "PREFERENCES_CACHE";
    }

    public static class AppPreferenceKeys
    {
        public static final String SESSION_ID = "SESSION_ID";
    }

    public static class Cache
    {
        public static final long CACHE_TIMESTAMP_LIMIT = 86400000;
        public static final String TIMESTAMP_PREFIX = "TIMESTAMP_";
    }

    public static class Http
    {
        public static final int CONNECTION_TIMEOUT = 3000;
        public static final int READ_TIMEOUT = 10000;
    }
}
