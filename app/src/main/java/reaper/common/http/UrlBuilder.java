package reaper.common.http;

import reaper.conf.Constants;

/**
 * Created by harsh.pokharna on 2/7/2015.
 */
public class UrlBuilder
{
    public static String build(String uri)
    {
        return Constants.Servers.APP + uri;
    }
}
