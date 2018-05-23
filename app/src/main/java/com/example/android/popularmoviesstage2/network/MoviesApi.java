package com.example.android.popularmoviesstage2.network;

import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.model.MovieImages;
import com.example.android.popularmoviesstage2.model.MovieReviews;
import com.example.android.popularmoviesstage2.model.MovieVideos;
import com.example.android.popularmoviesstage2.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Kshitij on 19-05-2018.
 */

public class MoviesApi {
    public interface PopularMovies {
        @GET("movie/popular")
        Call<MoviesList> getPopularMovies(@Query("page") Integer page);
    }

    public interface TopRatedMovies {
        @GET("movie/top_rated")
        Call<MoviesList> getTopRatedMovies(@Query("page") Integer page);
    }

    public interface MovieDetails {
        @GET("movie/{id}")
        Call<Movie> getMovieDetails(@Path("id") Integer id);
    }

    public interface MovieDetailVideos {
        @GET("movie/{id}/videos")
        Call<MovieVideos> getMovieVideos(@Path("id") Integer id);
    }

    public interface MovieDetailReviews {
        @GET("movie/{id}/reviews")
        Call<MovieReviews> getMovieReviews(@Path("id") Integer id, @Query("page") Integer page);
    }

    public interface MovieBackdropImages {
        @GET("movie/{id}/images")
        Call<MovieImages> getMovieBackdropImages(@Path("id") Integer id);
    }
}
