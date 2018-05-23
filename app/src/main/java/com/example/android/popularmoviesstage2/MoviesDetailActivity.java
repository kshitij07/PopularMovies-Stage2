package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmoviesstage2.fragment.AboutMovieFragment;
import com.example.android.popularmoviesstage2.fragment.MovieDetailFragment;
import com.example.android.popularmoviesstage2.fragment.MovieReviewsFragment;

import java.util.List;

/**
 * Created by Kshitij on 23-05-2018.
 */

public class MoviesDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0, l = fragments.size(); i < l; i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof MovieDetailFragment
                        || fragment instanceof AboutMovieFragment
                        || fragment instanceof MovieReviewsFragment) {
                    return;
                }
            }
        }

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_movie_detail, movieDetailFragment)
                .commit();
    }
}
