package co.yosola.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {


    private List<Movie> mMovieList;
    private Context mContext;

    private OnItemClickListener onItemClickListener;



    public MovieAdapter(Context context, List<Movie> movies) {
        this.mMovieList = movies;
        this.mContext = context;
    }

    public void setData(List<Movie> movieList) {
        if (this.mMovieList != null) {
            this.mMovieList.clear();
            this.mMovieList = movieList;
            notifyDataSetChanged();
        }
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.grid_movie_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieAdapterViewHolder customViewHolder, int i) {
        final Movie movieItem = mMovieList.get(i);

        //Download image using picasso library
        if (!TextUtils.isEmpty(movieItem.getmMoviePosterPath())) {
            Picasso.get().load(movieItem.getmMoviePosterPath())
                    .placeholder(R.drawable.moviedefaultscreen)
                    .into(customViewHolder.imageView);
        }

        //Setting text view title
        customViewHolder.textView.setText(movieItem.getmMovieTitle());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(movieItem);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
        customViewHolder.textView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return (null != mMovieList ? mMovieList.size() : 0);
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.movie_poster);
            this.textView = (TextView) view.findViewById(R.id.movie_poster);
        }

    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
