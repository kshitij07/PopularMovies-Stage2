package com.example.android.popularmoviesstage2.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.android.popularmoviesstage2.MoviesActivity;
import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapter.VideosListAdapter;
import com.example.android.popularmoviesstage2.api.TmdbRestClient;
import com.example.android.popularmoviesstage2.database.MovieDatabase;
import com.example.android.popularmoviesstage2.helper.Constants;
import com.example.android.popularmoviesstage2.helper.StateHandler;
import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.model.MovieReview;
import com.example.android.popularmoviesstage2.model.MovieReviews;
import com.example.android.popularmoviesstage2.model.MovieVideo;
import com.example.android.popularmoviesstage2.model.MovieVideos;
import com.example.android.popularmoviesstage2.model.MoviesList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kshitij on 19-05-2018.
 */

public class MovieDetailFragment extends Fragment
        implements VideosListAdapter.OnVideoClickListener {
    public static final String TAG = MovieDetailFragment.class.getName();

    private Movie movie;

    private boolean isFavorite;
    private FloatingActionButton favoriteButton;

    private TextView duration;
    private TextView rating;

    private RecyclerView genresRecyclerView;

    private TextView tagLine;
    private TextView overview;
    private TextView readMore;

    private VideosListAdapter videosListAdapter;
    private RecyclerView videosRecyclerView;

    private TextView reviewAuthor;
    private TextView reviewContent;
    private TextView reviewReadAll;

    private final String BACKDROP_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private final String POSTER_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    private final String TMDB_MOVIE_URL = "https://www.themoviedb.org/movie/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);
        }

        if (getArguments() != null) {
            movie = getArguments().getParcelable(Constants.BUNDLE_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(Constants.BUNDLE_MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initToolbarAndFAB(view);
        initMovieDetail(view);
        initMovieImages(view);
        initAboutMovie(view);
        initMovieVideos(view);
        initMovieReviews(view);

        return view;
    }

    private void initToolbarAndFAB(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());

        favoriteButton = (FloatingActionButton) view.findViewById(R.id.button_favorite);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FavoriteTogglerTask().execute();
            }
        });
        new FavoriteCheckerTask().execute();
    }

    private void initMovieDetail(View view) {
        TextView title = (TextView) view.findViewById(R.id.text_view_title);
        title.setText(movie.getTitle());

        TextView releaseDate = (TextView) view.findViewById(R.id.text_view_release_date);
        releaseDate.setText(movie.getReleaseDate());

        rating = (TextView) view.findViewById(R.id.text_view_rating);
        String voteAverage = Double.toString(movie.getVoteAverage());
        rating.setText(voteAverage);

        duration = (TextView) view.findViewById(R.id.text_view_duration);
    }

    private void initMovieImages(View view) {
        ImageView backdropImage = (ImageView) view.findViewById(R.id.image_view_backdrop);
        ImageView posterImage = (ImageView) view.findViewById(R.id.image_view_poster);

        Glide.with(view.getContext())
                .load(BACKDROP_IMAGE_URL + movie.getBackdropPath())
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.drawable.image_placeholder)
                .into(backdropImage);

        Glide.with(view.getContext())
                .load(POSTER_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(posterImage);
    }

    private void initAboutMovie(View view) {
        overview = (TextView) view.findViewById(R.id.text_view_overview);
        overview.setText(movie.getOverview());
        tagLine = (TextView) view.findViewById(R.id.text_view_tag_line);
        readMore = (TextView) view.findViewById(R.id.text_view_read_more);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_ID, movie.getId());
                bundle.putString(Constants.BUNDLE_TITLE, movie.getTitle());
                bundle.putString(Constants.BUNDLE_TAG_LINE, movie.getTagLine());
                bundle.putString(Constants.BUNDLE_OVERVIEW, movie.getOverview());

                AboutMovieFragment aboutMovieFragment = new AboutMovieFragment();
                aboutMovieFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_movie_detail, aboutMovieFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        readMore.setVisibility(View.INVISIBLE);
    }

    private void initMovieVideos(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videosListAdapter = new VideosListAdapter(MovieDetailFragment.this);
        videosRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_videos_list);
        videosRecyclerView.setLayoutManager(linearLayoutManager);
        videosRecyclerView.setAdapter(videosListAdapter);
        videosRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.spacing_genre)));
    }

    private void initMovieReviews(View view) {
        reviewAuthor = (TextView) view.findViewById(R.id.text_view_review_author);
        reviewContent = (TextView) view.findViewById(R.id.text_view_review_content);
        reviewReadAll = (TextView) view.findViewById(R.id.text_view_review_read_all);
        reviewReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_ID, movie.getId());
                bundle.putString(Constants.BUNDLE_TITLE, movie.getTitle());
                bundle.putLong(Constants.BUNDLE_VOTE_COUNT, movie.getVoteCount());
                bundle.putDouble(Constants.BUNDLE_VOTE_AVERAGE, movie.getVoteAverage());

                MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
                movieReviewsFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_movie_detail, movieReviewsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadMovieDetails();
        loadMovieVideos();
        loadMovieReviews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_MOVIE, movie);
    }

    private void loadMovieDetails() {
        if (movie.getTagLine() != null) {
            updateUI();
            return;
        }
        Call<Movie> call = TmdbRestClient.getInstance()
                .getMovieDetailsImpl()
                .getMovieDetails(movie.getId());
        Callback<Movie> callback = new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    movie.setTagLine(StateHandler.handleMovieDetailState(getContext(), Constants.SERVER_ERROR));

                } else {
                    movie.setDuration(response.body().getDuration());
                    movie.setTagLine(response.body().getTagLine());
                    movie.setOverview(response.body().getOverview());
                    movie.setVoteCount(response.body().getVoteCount());
                    movie.setVoteAverage(response.body().getVoteAverage());
                }
                update();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movie.setTagLine(StateHandler.handleMovieDetailState(getContext(), Constants.NETWORK_ERROR));
                update();
            }

            private void update() {
                updateUI();
            }
        };
        call.enqueue(callback);
    }

    private void updateUI() {
        setupAboutMovieView();
    }

    private void setupAboutMovieView() {
        String runtime = Integer.toString(movie.getDuration()) + " minutes";
        duration.setText(runtime);

        rating.setText(String.valueOf(movie.getVoteAverage()));
        if (movie.getTagLine() != null && movie.getTagLine().length() > 0) {
            tagLine.setText(movie.getTagLine());
        }
        overview.setText(movie.getOverview());
        if (overview != null || overview.length() > 0) {
            readMore.setVisibility(View.VISIBLE);
        }
    }

    private void loadMovieVideos() {
        if (movie.getMovieVideos() != null &&
                movie.getMovieVideos().size() > 0) {
            setupMovieVideos();
            return;
        }

        Call<MovieVideos> call = TmdbRestClient.getInstance()
                .getMovieVidoesImpl()
                .getMovieVideos(movie.getId());
        Callback<MovieVideos> callback = new Callback<MovieVideos>() {
            private MovieVideo video;

            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                if (!response.isSuccessful()) {
                    video = StateHandler.handleMovieVideoState(getContext(), Constants.SERVER_ERROR);

                } else if (response.body().getVideos().size() == 0) {
                    video = StateHandler.handleMovieVideoState(getContext(), Constants.NONE);

                } else {
                    movie.setMovieVideos(response.body().getVideos());
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                video = StateHandler.handleMovieVideoState(getContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                if (video != null) {
                    ArrayList<MovieVideo> videos = new ArrayList<>();
                    videos.add(video);
                    video = null;

                    movie.setMovieVideos(videos);
                }
                setupMovieVideos();
            }
        };
        call.enqueue(callback);
    }

    @Override
    public void onVideoClick(MovieVideo movieVideo) {

        if (!movieVideo.getSite().equalsIgnoreCase(Constants.YOUTUBE)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.URI_YOUTUBE_BROWSER + movieVideo.getKey()));
        startActivity(intent);
    }

    private void setupMovieVideos() {
        videosListAdapter.setVideos(movie.getMovieVideos());
        videosListAdapter.notifyDataSetChanged();
        videosRecyclerView.setHasFixedSize(true);
    }

    private void loadMovieReviews() {
        if (movie.getMovieReviews() != null &&
                movie.getMovieReviews().size() > 0) {
            setupMovieReviews();
            return;
        }

        Call<MovieReviews> call = TmdbRestClient.getInstance()
                .getMovieReviewsImpl()
                .getMovieReviews(movie.getId(), 1);
        Callback<MovieReviews> callback = new Callback<MovieReviews>() {
            private MovieReview review;

            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                if (!response.isSuccessful()) {
                    review = StateHandler.handleMovieReviewState(getContext(), Constants.SERVER_ERROR);

                } else if (response.body().getMovieReviews().size() == 0) {
                    review = StateHandler.handleMovieReviewState(getContext(), Constants.NONE);

                } else {
                    review = response.body().getMovieReviews().get(0);
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                review = StateHandler.handleMovieReviewState(getContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                ArrayList<MovieReview> movieReviews = new ArrayList<>();
                movieReviews.add(review);
                review = null;

                movie.setMovieReviews(movieReviews);
                setupMovieReviews();
            }
        };
        call.enqueue(callback);
    }

    private void setupMovieReviews() {
        reviewAuthor.setText(movie.getMovieReviews().get(0).getAuthor());
        reviewContent.setText(movie.getMovieReviews().get(0).getContent());
        if (reviewContent.getText().toString()
                .compareTo(getString(R.string.no_reviews)) == 0) {
            reviewReadAll.setVisibility(View.INVISIBLE);
            reviewReadAll.setOnClickListener(null);
        }
    }

    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                return;
            }
            outRect.left = spacing;
        }
    }

    private class FavoriteCheckerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            MovieDatabase.MovieEntry.CONTENT_URI,
                            new String[]{MovieDatabase.MovieColumns.MOVIE_ID },
                            MovieDatabase.MovieColumns.MOVIE_ID + " = ?",
                            new String[]{ String.valueOf(movie.getId()) },
                            null
                    );
            boolean isExists = cursor != null && cursor.getCount() == 1;
            if (cursor != null) {
                cursor.close();
            }
            return isExists;
        }

        @Override
        protected void onPostExecute(Boolean isExists) {
            super.onPostExecute(isExists);
            if (isExists) {
                favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
            }
            isFavorite = isExists;
        }
    }

    private class FavoriteTogglerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isSuccessful;
            if (isFavorite) {
                isSuccessful = getContext().getContentResolver()
                        .delete(
                                MovieDatabase.MovieEntry.CONTENT_URI,
                                MovieDatabase.MovieColumns.MOVIE_ID + " = ?",
                                new String[]{ String.valueOf(movie.getId()) }
                        ) == 1;
            } else {
                isSuccessful = getContext().getContentResolver()
                        .insert(MovieDatabase.MovieEntry.CONTENT_URI, getContentValues()) != null;
            }
            return isSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);
            if (!isSuccessful) {
                showToast(getString(R.string.op_failed));
                return;
            }
            isFavorite = !isFavorite;
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
                showToast(getString(R.string.movie_added));
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
                showToast(getString(R.string.movie_removed));
            }
        }

        private ContentValues getContentValues() {
            ContentValues values = new ContentValues();
            values.put(MovieDatabase.MovieColumns.MOVIE_ID, movie.getId());
            values.put(MovieDatabase.MovieColumns.MOVIE_TITLE, movie.getTitle());
            values.put(MovieDatabase.MovieColumns.MOVIE_RELEASE_DATE, movie.getReleaseDate());
            values.put(MovieDatabase.MovieColumns.MOVIE_DURATION, movie.getDuration());
            values.put(MovieDatabase.MovieColumns.MOVIE_RATING, movie.getVoteAverage());
            values.put(MovieDatabase.MovieColumns.MOVIE_POSTER_PATH, movie.getPosterPath());
            values.put(MovieDatabase.MovieColumns.MOVIE_BACKDROP_PATH, movie.getBackdropPath());
            return values;
        }
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
