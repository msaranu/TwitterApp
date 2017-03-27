package com.codepath.apps.twitterapp.models;

/**
 * Created by Saranu on 3/24/17.
 */


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;



public class Entities implements Parcelable {

    private List<Media> media = null;

    protected Entities(Parcel in) {
    }

    public static final Creator<Entities> CREATOR = new Creator<Entities>() {
        @Override
        public Entities createFromParcel(Parcel in) {
            return new Entities(in);
        }

        @Override
        public Entities[] newArray(int size) {
            return new Entities[size];
        }
    };


    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
