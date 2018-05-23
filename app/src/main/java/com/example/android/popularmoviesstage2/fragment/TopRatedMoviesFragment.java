package com.example.android.popularmoviesstage2.fragment;

import com.example.android.popularmoviesstage2.api.TmdbRestClient;
import com.example.android.popularmoviesstage2.helper.Constants;
import com.example.android.popularmoviesstage2.model.MoviesList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kshitij on 19-05-2018.
 */

public class TopRatedMoviesFragment extends MoviesListFragment {
    public static final String TAG = TopRatedMoviesFragment.class.getName();

    public TopRatedMoviesFragment() {}

    @Override
    protected void loadMovies() {
        super.loadMovies();
        Call<MoviesList> call = TmdbRestClient.getInstance()
                .getTopRatedMoviesImpl()
                .getTopRatedMovies(getPage());
        Callback<MoviesList> callback = new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (!response.isSuccessful()) {
                    retrievalError(Constants.SERVER_ERROR);
                    return;
                }
                setTotalPages(response.body().getTotalPages());
                addMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                retrievalError(Constants.NETWORK_ERROR);
            }
        };
        call.enqueue(callback);
    }
}
