package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.popularmoviesstage2.adapter.FragmentTabsAdapter;
import com.example.android.popularmoviesstage2.adapter.MoviesListAdapter;
import com.example.android.popularmoviesstage2.fragment.FavoriteMoviesFragment;
import com.example.android.popularmoviesstage2.fragment.PopularMoviesFragment;
import com.example.android.popularmoviesstage2.fragment.TopRatedMoviesFragment;
import com.example.android.popularmoviesstage2.helper.Constants;
import com.example.android.popularmoviesstage2.model.Movie;

/**
 * Created by Kshitij on 20-05-2018.
 */

public class MoviesActivity extends AppCompatActivity implements MoviesListAdapter.OnMovieClickListener {
    private final String TAG = MoviesActivity.class.getName();

    private int type = Constants.MOVIES_GENERAL;

    private PopularMoviesFragment popularMoviesFragment;
    private TopRatedMoviesFragment topRatedMoviesFragment;
    private FavoriteMoviesFragment favoriteMoviesFragment;

    private int movieId;
    private String movieTitle;

    private int genreId;
    private String genre;

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
        }

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(Constants.INTENT_EXTRA_TYPE, Constants.MOVIES_GENERAL);
            if (type == Constants.MOVIES_GENRE) {
                genreId = intent.getIntExtra(Constants.BUNDLE_GENRE_ID, 0);
                genre = intent.getStringExtra(Constants.BUNDLE_GENRE);
            }
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        initFragments(savedInstanceState);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager() {
        FragmentTabsAdapter adapter = new FragmentTabsAdapter(getSupportFragmentManager());
        switch (type) {
            case Constants.MOVIES_GENERAL: {
                adapter.addFragment(popularMoviesFragment, getString(R.string.popular));
                adapter.addFragment(topRatedMoviesFragment, getString(R.string.top_rated));
                adapter.addFragment(favoriteMoviesFragment, getString(R.string.favorites));
                viewPager.setOffscreenPageLimit(3);
                break;
            }
        }
        viewPager.setAdapter(adapter);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            loadFromSavedInstanceState(savedInstanceState);
            return;
        }

        switch (type) {
            case Constants.MOVIES_GENERAL: {
                popularMoviesFragment = new PopularMoviesFragment();
                topRatedMoviesFragment = new TopRatedMoviesFragment();
                favoriteMoviesFragment = new FavoriteMoviesFragment();
                break;
            }
        }
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {
        switch (type) {
            case Constants.MOVIES_GENERAL: {
                popularMoviesFragment = (PopularMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, PopularMoviesFragment.TAG);
                topRatedMoviesFragment = (TopRatedMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, TopRatedMoviesFragment.TAG);
                favoriteMoviesFragment = (FavoriteMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, FavoriteMoviesFragment.TAG);
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        switch (type) {
            case Constants.MOVIES_GENERAL: {
                getSupportFragmentManager().putFragment(outState, PopularMoviesFragment.TAG, popularMoviesFragment);
                getSupportFragmentManager().putFragment(outState, TopRatedMoviesFragment.TAG, topRatedMoviesFragment);
                getSupportFragmentManager().putFragment(outState, FavoriteMoviesFragment.TAG, favoriteMoviesFragment);
                break;
            }
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_MOVIE, movie);
            startActivity(intent);
    }
}
