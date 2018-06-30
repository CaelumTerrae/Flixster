package me.caelumterrae.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.flixster.models.Config;
import me.caelumterrae.flixster.models.Movie;

public class MovieListActivity extends AppCompatActivity {
    //constants
    //base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tags for logging from this class
    public final static String TAG = "MovieListActivity7";
    // list of currently playing movies
    ArrayList<Movie> movies;

    AsyncHttpClient client;


    RecyclerView rvMovies;
    MovieAdapter adapter;
    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize the AsyncHTTPClient();
        client = new AsyncHttpClient();
        // initialize the list of currently available movies
        movies = new ArrayList<>();

        adapter = new MovieAdapter(movies);

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        getConfiguration();
    }


    // get the list of currently playing movies from the API
    private void getMovies() {
        String url = API_BASE_URL + "/movie/now_playing";

        RequestParams params = new RequestParams();

        params.put(API_KEY_PARAM, getString(R.string.api_key));

        // execute a GET request expecting the JSON object response

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results here.
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        adapter.notifyItemInserted(movies.size() - 1);
                    }

                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    logError("Failure while parsing movie response", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure to gather movies", throwable, true);
            }
        });

    }

    // get the configuration from the API

    private void getConfiguration() {
        String url = API_BASE_URL + "/configuration";
        // set the parameter for the request

        RequestParams params = new RequestParams();

        params.put(API_KEY_PARAM, getString(R.string.api_key));

        // execute a GET request expecting a JSON object response.
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                    Log.i(TAG, String.format("Success in loading configuration with imageBaseUrl%s and posterSize %s", config.getImageBaseUrl(), config.getPosterSize()));
                    adapter.setConfig(config);
                    getMovies();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to gather configuration", throwable, true);
            }
        });
    }

    private void logError(String message, Throwable error, boolean alertUser) {
        Log.e(TAG, message, error);

        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
