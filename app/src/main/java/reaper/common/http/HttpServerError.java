package reaper.common.http;

/**
 * Created by Aditya on 05-04-2015.
 */
public class HttpServerError extends Exception
{
    public HttpServerError()
    {
        super("Server Error");
    }
}
