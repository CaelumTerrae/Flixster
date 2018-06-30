package me.caelumterrae.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //keep track of the baseurl for images
    String imageBaseUrl;
    //poster size of images
    String posterSize;
    String backdropSize;



    public Config(JSONObject jsonObject) throws JSONException{
        JSONObject images = jsonObject.getJSONObject("images");
        //put image base url in proper
        imageBaseUrl = images.getString("secure_base_url");
        // get the poster size as well
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // TODO refactor the fallback to R.values
        posterSize = posterSizeOptions.optString(3, "w342");
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }

    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
