package reaper.common.http;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import reaper.conf.Constants;

/**
 * Created by Aditya on 05-04-2015.
 */
public class HttpRequest
{
    public String sendRequest(String url, Map<String, String> postData) throws HttpServerError
    {
        if (postData == null)
        {
            postData = new HashMap<>();
        }

        postData.put("_ME", "9320369679");

        // URL
        URL urlObject = null;

        HttpURLConnection connection = null;
        try
        {
            urlObject = new URL(url);

            // Open Http Connection
            connection = (HttpURLConnection) urlObject.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(Constants.Http.CONNECTION_TIMEOUT);
            connection.setReadTimeout(Constants.Http.READ_TIMEOUT);

            // Set post data
            String postJson = processPostData(postData);
            if (postJson != null)
            {
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);

                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                os.write(postJson);
                os.close();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                // Process Response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder result = new StringBuilder();
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }

                return result.toString();
            }
            else
            {
                throw new HttpServerError();
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (ProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        return null;
    }

    private String processPostData(Map<String, String> postData)
    {
        if (postData.size() == 0)
        {
            return null;
        }
        return (new Gson()).toJson(postData);
    }
}
