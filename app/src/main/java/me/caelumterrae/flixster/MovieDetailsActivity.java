package me.caelumterrae.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.caelumterrae.flixster.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.ivBackgroundImage) ImageView ivBackgroundImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        float voteAverage = movie.getVoterAverage().floatValue();
        voteAverage = voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage;
        rbVoteAverage.setRating(voteAverage);
        setUpImageViewListener();
        //Load image using GLIDE

    }

    private void setUpImageViewListener() {
        ivBackgroundImage.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);

                i.putExtra(String.class.getSimpleName(), movie.getId());
                startActivityForResult(i, 20);
            }
        });
    }
}
