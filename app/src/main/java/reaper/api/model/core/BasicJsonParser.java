package reaper.api.model.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicJsonParser
{
    private static Gson gson = (new GsonBuilder()).registerTypeAdapter(DateTime.class, new DateTimeParser()).create();

    public static String getValue(String json, String key)
    {
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(json);
        if (root.isJsonObject())
        {
            JsonObject rootObject = root.getAsJsonObject();
            JsonElement element = rootObject.get(key);
            if (element != null)
            {
                if (element.isJsonPrimitive())
                {
                    return element.getAsString();
                }
                else
                {
                    return element.toString();
                }
            }
        }
        return null;
    }

    public static List<String> getList(String json, String key)
    {
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(json);
        if (root.isJsonObject())
        {
            JsonObject rootObject = root.getAsJsonObject();
            JsonElement element = rootObject.get(key);
            if (element != null)
            {
                if (element.isJsonArray())
                {
                    JsonArray array = element.getAsJsonArray();
                    List<String> list = new ArrayList<>();
                    Iterator<JsonElement> elementIterator = array.iterator();
                    while (elementIterator.hasNext())
                    {
                        JsonElement listEntry = elementIterator.next();
                        if (listEntry.isJsonPrimitive())
                        {
                            list.add(listEntry.getAsString());
                        }
                        else
                        {
                            list.add(listEntry.toString());
                        }
                    }

                    return list;
                }
            }
        }
        return new ArrayList<>();
    }
}
