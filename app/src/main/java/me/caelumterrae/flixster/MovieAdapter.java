package me.caelumterrae.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.flixster.models.Config;
import me.caelumterrae.flixster.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    ArrayList<Movie> movies;

    Config config;

    Context context;

    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the movie at the specified positions
        Movie movie = movies.get(i);
        // populate the view
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        // determine current orientation to make changes
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        String imageUrl = "";
        if (isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(),movie.getPosterPath());
        } else {
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        int placeHolderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage: viewHolder.ivBackroundImage;


        //Load image using GLIDE
        Glide.with(context)
                .load(imageUrl)
                .apply(
                        RequestOptions.placeholderOf(placeHolderId)
                                .error(placeHolderId)
                        .fitCenter()
                )
                .apply( RequestOptions.bitmapTransform(new RoundedCornersTransformation(30, 0)))
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        ImageView ivPosterImage;
        ImageView ivBackroundImage;
        TextView tvTitle;
        TextView tvOverview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackroundImage = itemView.findViewById(R.id.ivBackroundImage);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvOverview.setMovementMethod(new ScrollingMovementMethod());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);

                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                context.startActivity(intent);
            }

        }
    }
}
