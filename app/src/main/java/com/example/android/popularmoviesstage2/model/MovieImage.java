package com.example.android.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kshitij on 19-05-2018.
 */

public class MovieImage implements Parcelable {
    @Expose
    @SerializedName("file_path")
    private String filePath;

    @Expose
    @SerializedName("width")
    private int width;

    @Expose
    @SerializedName("height")
    private int height;

    public MovieImage() {
        filePath = "";
    }

    private MovieImage(Parcel source) {
        filePath = source.readString();
        width = source.readInt();
        height = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    public static final Parcelable.Creator<MovieImage> CREATOR = new Creator<MovieImage>() {
        @Override
        public MovieImage createFromParcel(Parcel source) {
            return new MovieImage(source);
        }

        @Override
        public MovieImage[] newArray(int size) {
            return new MovieImage[size];
        }
    };

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
