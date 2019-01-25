package co.yosola.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


    private static final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> movieList;

    //I need this to set the ListitemClickLister;
    private final ListItemClickLister listItemClickLister;


    public interface ListItemClickLister{
        void onListItemClick(Movie movie);
    }

    public MovieAdapter(ArrayList<Movie> movieList, ListItemClickLister listItemClickLister){
        this.movieList = movieList;
        this.listItemClickLister = listItemClickLister;
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

        final Movie movieItem = movieList.get(position);
        String posterUrl = movieItem.getmMoviePosterPath();

//        Log.d(TAG, posterUrl);
        Picasso.get().load(posterUrl)
                .placeholder(R.drawable.moviedefaultscreen)
                .into(holder.mPoster);

        //Setting text view title
        holder.textView.setText(movieItem.getmMovieTitle());
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
    }

    public void setPosterData(ArrayList<Movie> movies) {
        if (this.movieList != null) {
            this.movieList.clear();
            this.movieList = movies;
            notifyDataSetChanged();
        }
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
            int adapterPosition = getAdapterPosition();
            Movie movie = movieList.get(adapterPosition);
            listItemClickLister.onListItemClick(movie);
        }
    }

}
