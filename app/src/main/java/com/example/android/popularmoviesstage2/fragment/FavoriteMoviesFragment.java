package com.example.android.popularmoviesstage2.fragment;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.popularmoviesstage2.database.MovieDatabase;
import com.example.android.popularmoviesstage2.model.Movie;

import java.util.ArrayList;

/**
 * Created by Kshitij on 19-05-2018.
 */

public class FavoriteMoviesFragment extends MoviesListFragment {
    public static final String TAG = FavoriteMoviesFragment.class.getName();

    public FavoriteMoviesFragment() {}

    @Override
    protected void loadMovies() {
        new AsyncDbTask().execute();
    }

    /**
     * Load movies from favorites.
     */
    private class AsyncDbTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            ArrayList<Movie> movies = new ArrayList<>();
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            MovieDatabase.MovieEntry.CONTENT_URI,
                            new String[]{
                                    MovieDatabase.MovieColumns.MOVIE_ID,
                                    MovieDatabase.MovieColumns.MOVIE_TITLE,
                                    MovieDatabase.MovieColumns.MOVIE_RELEASE_DATE,
                                    MovieDatabase.MovieColumns.MOVIE_DURATION,
                                    MovieDatabase.MovieColumns.MOVIE_RATING,
                                    MovieDatabase.MovieColumns.MOVIE_POSTER_PATH,
                                    MovieDatabase.MovieColumns.MOVIE_BACKDROP_PATH
                            },
                            null, null, null
                    );
            if (cursor == null) {
                return null;
            }

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_TITLE));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_RELEASE_DATE));
                int duration = cursor.getInt(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_DURATION));
                double rating = cursor.getDouble(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_RATING));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_POSTER_PATH));
                String backdropPath = cursor.getString(cursor.getColumnIndex(MovieDatabase.MovieColumns.MOVIE_BACKDROP_PATH));
                movies.add(new Movie(id, title, releaseDate, duration, rating, posterPath, backdropPath));
            }
            cursor.close();
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            addFavoriteMovies(movies);
        }
    }
}
