package com.codepath.apps.twitterEnhanced.models;

import android.os.Parcelable;

import com.codepath.apps.twitterEnhanced.dao.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;


/**
 * Created by Saranu on 3/24/17.
 */

@Table(database = TwitterDatabase.class)
@Parcel(analyze = {Draft.class})
public class Draft extends BaseModel implements Parcelable{

    @Column
    @PrimaryKey
    private String draftText;

    public String getDraftText() {
        return draftText;
    }

    public void setDraftText(String draftText) {
        this.draftText = draftText;
    }

    public Draft(android.os.Parcel in) {
        draftText = in.readString();
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(draftText);
    }

    public Draft() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Draft> CREATOR = new Creator<Draft>() {
        @Override
        public Draft createFromParcel(android.os.Parcel in) {
            return new Draft(in);
        }

        @Override
        public Draft[] newArray(int size) {
            return new Draft[size];
        }
    };
}
