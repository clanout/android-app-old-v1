package reaper.api.core;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ModelFactory
{
    public static <M> M create(String json, Class<M> modelClass)
    {
        if (json == null || modelClass == null)
        {
            return null;
        }

        Gson gson = new Gson();
        M model = null;
        try
        {
            model = gson.fromJson(json, modelClass);
        }
        catch (Exception e)
        {
            Log.e("ModelFactory", e.getMessage());
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
