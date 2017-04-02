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
@Parcel(analyze = {Media.class})
public class Media extends BaseModel implements Parcelable{

    @Column
    @PrimaryKey
    private long id;

    @Column
    private String mediaUrl;

    @Column
    private String expandedUrl;

    @Column
    private String type;

    protected Media(android.os.Parcel in) {
        id = in.readLong();
        mediaUrl = in.readString();
        expandedUrl = in.readString();
        type = in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(android.os.Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Media(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(mediaUrl);
        dest.writeString(expandedUrl);
        dest.writeString(type);
    }
}
