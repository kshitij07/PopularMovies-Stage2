package com.example.android.popularmoviesstage2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.popularmoviesstage2.fragment.BackdropMovieImageFragment;
import com.example.android.popularmoviesstage2.model.MovieImage;

import java.util.ArrayList;

/**
 * Created by Kshitij on 19-05-2018.
 */

public class BackdropMovieImagesAdapter extends FragmentStatePagerAdapter {
    private ArrayList<MovieImage> movieImages = new ArrayList<>();

    public BackdropMovieImagesAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addMovieImages(ArrayList<MovieImage> movieImages) {
        this.movieImages.clear();
        this.movieImages.addAll(movieImages);
    }

    @Override
    public Fragment getItem(int position) {
        return BackdropMovieImageFragment.getInstance(movieImages.get(position).getFilePath());
    }

    @Override
    public int getCount() {
        return movieImages.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
