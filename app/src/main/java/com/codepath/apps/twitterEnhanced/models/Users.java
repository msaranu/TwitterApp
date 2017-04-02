package com.codepath.apps.twitterEnhanced.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Saranu on 4/1/17.
 */


public class Users implements Parcelable{

    private List<User> users = null;
    private long nextCursor;
    private String nextCursorStr;
    private long previousCursor;
    private String previousCursorStr;

    protected Users(Parcel in) {
        users = in.createTypedArrayList(User.CREATOR);
        nextCursor = in.readLong();
        nextCursorStr = in.readString();
        previousCursor = in.readLong();
        previousCursorStr = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(long nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getNextCursorStr() {
        return nextCursorStr;
    }

    public void setNextCursorStr(String nextCursorStr) {
        this.nextCursorStr = nextCursorStr;
    }

    public long getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(long previousCursor) {
        this.previousCursor = previousCursor;
    }

    public String getPreviousCursorStr() {
        return previousCursorStr;
    }

    public void setPreviousCursorStr(String previousCursorStr) {
        this.previousCursorStr = previousCursorStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(users);
        dest.writeLong(nextCursor);
        dest.writeString(nextCursorStr);
        dest.writeLong(previousCursor);
        dest.writeString(previousCursorStr);
    }
}
