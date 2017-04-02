package com.codepath.apps.twitterEnhanced.models;

/**
 * Created by Saranu on 4/1/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Statuses implements Parcelable{


    private List<Tweet> statuses = null;

    protected Statuses(Parcel in) {
        statuses = in.createTypedArrayList(Tweet.CREATOR);
    }

    public static final Creator<Statuses> CREATOR = new Creator<Statuses>() {
        @Override
        public Statuses createFromParcel(Parcel in) {
            return new Statuses(in);
        }

        @Override
        public Statuses[] newArray(int size) {
            return new Statuses[size];
        }
    };

    public List<Tweet> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Tweet> statuses) {
        this.statuses = statuses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(statuses);
    }
}