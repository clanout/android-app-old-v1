package reaper.api.model.core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ModelFactory
{
    private static Gson gson = (new GsonBuilder()).registerTypeAdapter(DateTime.class, new DateTimeParser()).create();

    public static <M> M create(String json, Class<M> modelClass)
    {
        if (json == null || modelClass == null)
        {
            return null;
        }

        M model = null;
        try
        {
            model = gson.fromJson(json, modelClass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return model;
    }

    public static <M> List<M> createModelList(List<String> jsonArray, Class<M> modelClass)
    {
        if (jsonArray == null || modelClass == null)
        {
            return null;
        }

        List<M> modelList = new ArrayList<M>();
        for (String json : jsonArray)
        {
            M model = create(json, modelClass);
            modelList.add(model);
        }

        return modelList;
    }
}
