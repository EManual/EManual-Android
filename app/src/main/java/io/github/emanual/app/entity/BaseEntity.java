package io.github.emanual.app.entity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: jayin
 * Date: 2/1/16
 */
public class BaseEntity implements Serializable {
    public static <T extends BaseEntity> T createByJSON(String json, Class<T> cls) {
        Gson gson = new Gson();
        try {
            T t = gson.fromJson(json, cls);
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public static <T extends BaseEntity> List<T> createByJSONArray(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(createByJSON(jsonArray.getJSONObject(i).toString(), cls)
                );
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
