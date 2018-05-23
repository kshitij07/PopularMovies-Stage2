package com.example.android.popularmoviesstage2.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kshitij on 17-05-2018.
 */

public class MovieDatabase {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public interface Tables {
        String MOVIE = "movie";
    }

    public interface MovieColumns {
        String MOVIE_ID = "_id";
        String MOVIE_TITLE = "title";
        String MOVIE_RELEASE_DATE = "date";
        String MOVIE_DURATION = "duration";
        String MOVIE_RATING = "rating";
        String MOVIE_BACKDROP_PATH = "backdrop";
        String MOVIE_POSTER_PATH = "poster";
    }

    public static final class MovieEntry implements BaseColumns, MovieColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    private MovieDatabase() {
        throw new AssertionError("No instances");
    }
}

