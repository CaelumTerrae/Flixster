package me.caelumterrae.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public final String API_BASE_URL = "https://api.themoviedb.org/3";

    AsyncHttpClient client;
    String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        client = new AsyncHttpClient();

        // temporary test video id -- TODO replace with movie trailer video id

        videoId = "tKodtNFpzBA";

        String id = getIntent().getExtras().getString("id");

        getVideoId(id);
        // resolve the player view from the layout

    }

    public void getVideoId(String id){
        String URL = API_BASE_URL + "/movie/" + id + "/videos";

        RequestParams params = new RequestParams();

        params.put("api_key", getString(R.string.api_key));



        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results here.
                try {
                    JSONArray results = response.getJSONArray("results");
                    videoId = results.optString(0, "tKodtNFpzBA");

                    YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

                    // initialize with API key stored in secrets.xml
                    playerView.initialize(getString(R.string.youtube_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(videoId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                        }
                    });

                } catch (JSONException e) {
                    logError("Failure while attempting to get video", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure to get video", throwable, true);
            }
        });

    }

    private void logError(String message, Throwable error, boolean alertUser) {
        Log.e("BOI", message, error);

        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
