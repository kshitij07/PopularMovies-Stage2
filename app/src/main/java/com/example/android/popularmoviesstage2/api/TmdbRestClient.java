package com.example.android.popularmoviesstage2.api;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.network.MoviesApi;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kshitij on 17-05-2018.
 */

public class TmdbRestClient {
    private static String BASE_URL = "https://api.themoviedb.org/3/";

    private MoviesApi.PopularMovies popularMovies;
    private MoviesApi.TopRatedMovies topRatedMovies;

    private MoviesApi.MovieDetails movieDetails;
    private MoviesApi.MovieDetailVideos movieVideos;
    private MoviesApi.MovieDetailReviews movieReviews;

    private MoviesApi.MovieBackdropImages movieBackdropImages;

    private Retrofit retrofit;

    private static TmdbRestClient instance = null;

    private TmdbRestClient() {
        initializeRetrofit();
    }

    public static TmdbRestClient getInstance() {
        if (instance == null) {
            instance = new TmdbRestClient();
        }
        return instance;
    }

    public MoviesApi.PopularMovies getPopularMoviesImpl() {
        if (popularMovies == null) {
            popularMovies = retrofit.create(MoviesApi.PopularMovies.class);
        }
        return popularMovies;
    }

    public MoviesApi.TopRatedMovies getTopRatedMoviesImpl() {
        if (topRatedMovies == null) {
            topRatedMovies = retrofit.create(MoviesApi.TopRatedMovies.class);
        }
        return topRatedMovies;
    }

    public MoviesApi.MovieDetails getMovieDetailsImpl() {
        if (movieDetails == null) {
            movieDetails = retrofit.create(MoviesApi.MovieDetails.class);
        }
        return movieDetails;
    }

    public MoviesApi.MovieDetailVideos getMovieVidoesImpl() {
        if (movieVideos == null) {
            movieVideos = retrofit.create(MoviesApi.MovieDetailVideos.class);
        }
        return movieVideos;
    }

    public MoviesApi.MovieDetailReviews getMovieReviewsImpl() {
        if (movieReviews == null) {
            movieReviews = retrofit.create(MoviesApi.MovieDetailReviews.class);
        }
        return movieReviews;
    }

    public MoviesApi.MovieBackdropImages getMovieBackdropImagesImpl() {
        if (movieBackdropImages == null) {
            movieBackdropImages = retrofit.create(MoviesApi.MovieBackdropImages.class);
        }
        return movieBackdropImages;
    }
    private void initializeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url()
                                .newBuilder()
                                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                                .build();
                        Request.Builder builder = request.newBuilder()
                                .url(url)
                                .method(request.method(), request.body());
                        request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
