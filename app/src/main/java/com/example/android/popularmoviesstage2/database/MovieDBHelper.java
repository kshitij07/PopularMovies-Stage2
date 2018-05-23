package com.example.android.popularmoviesstage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kshitij on 20-05-2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Favorite.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieDatabase.Tables.MOVIE + " (" +
                MovieDatabase.MovieColumns.MOVIE_ID + " INTEGER PRIMARY KEY, " +
                MovieDatabase.MovieColumns.MOVIE_TITLE + " TEXT, " +
                MovieDatabase.MovieColumns.MOVIE_RELEASE_DATE + " TEXT, " +
                MovieDatabase.MovieColumns.MOVIE_DURATION + " INTEGER, " +
                MovieDatabase.MovieColumns.MOVIE_RATING + " REAL, " +
                MovieDatabase.MovieColumns.MOVIE_POSTER_PATH + " TEXT, " +
                MovieDatabase.MovieColumns.MOVIE_BACKDROP_PATH + " TEXT" + ")";
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
