package co.yosola.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    public static final String POSTERS_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_WIDTH = "w185/";
    private static final String TAG = MovieAdapter.class.getSimpleName();
    final private MovieAdapterOnClickHandler mClickHandler;
    private ArrayList<Movie> mMovies;

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        mClickHandler = movieAdapterOnClickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.grid_movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        final Movie movieItem = mMovies.get(position);
        String posterUrl = POSTERS_BASE_URL + POSTER_WIDTH + movieItem.getmMoviePosterPath();
        Picasso.get().load(posterUrl)
                .placeholder(R.drawable.moviedefaultscreen)
                .into(holder.mPoster);

        //Setting text view title
        holder.textView.setText(movieItem.getmMovieTitle());
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        } else {
            return mMovies.size();
        }
    }

    public void setPosterData(ArrayList<Movie> movies) {
        if (this.mMovies != null) {
            this.mMovies.clear();
            this.mMovies = movies;
            notifyDataSetChanged();
        }
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie clickedItem);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPoster;
        public final TextView textView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.movie_title);
        }

        @Override
        public void onClick(View view) {
            Movie movie = mMovies.get(getAdapterPosition());
            mClickHandler.onClick(movie);
        }
    }

}
