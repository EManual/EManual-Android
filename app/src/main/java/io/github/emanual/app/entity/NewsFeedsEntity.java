package io.github.emanual.app.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class NewsFeedsEntity implements Serializable {
    private String path;
    private String name;
    private String rname;
    private String description;

    public static NewsFeedsEntity create(String json) {
        return new Gson().fromJson(json, NewsFeedsEntity.class);
    }

    public static List<NewsFeedsEntity> createNewsFeedsObjects(String json) throws Exception{
        Type collectionType = new TypeToken<List<NewsFeedsEntity>>() {
        }.getType();
        List<NewsFeedsEntity> result = null;

        try{
            //json格式不一定对，应为网络原因
            result = new Gson().fromJson(json, collectionType);
            return result;
        }catch (Exception e){
            throw e;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
