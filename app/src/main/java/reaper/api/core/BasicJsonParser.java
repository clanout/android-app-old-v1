package reaper.api.core;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicJsonParser
{
    public static String getValue(String json, String key)
    {
        if (json == null || key == null)
        {
            return null;
        }

        String result = null;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try
        {
            rootNode = mapper.readTree(json);
            JsonNode value = rootNode.path(key);
            result = (((Object) value)).toString();
        }
        catch (IOException e)
        {
            return null;
        }

        return result;
    }

    public static List<String> getList(String json, String key)
    {
        if (json == null || key == null)
        {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try
        {
            rootNode = mapper.readTree(json);
        }
        catch (IOException e)
        {
            return null;
        }

        List<String> valueList = new ArrayList<String>();
        try
        {
            Iterator<JsonNode> list = rootNode.get(key).getElements();
            while (list.hasNext())
            {
                JsonNode node = list.next();
                valueList.add((((Object) node)).toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return valueList;
    }
}
