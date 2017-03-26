package com.codepath.apps.twitterapp.models;

/**
 * Created by Saranu on 3/21/17.
 */

import android.os.Parcelable;

import com.codepath.apps.twitterapp.dao.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;


@Table(database = TwitterDatabase.class)
@Parcel(analyze = {User.class})
public class User extends BaseModel implements Parcelable{

    @Column
    public String name;

    @Column
    public String profileImageUrl;

    @Column
    public String createdAt;

    @Column
    public String profileImageUrlHttps;

    @PrimaryKey
    public long id;

    @Column
    public String description;

    @Column
    public Boolean verified;

    @Column
    public String screenName;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public void setProfileImageUrlHttps(String profileImageUrlHttps) {
        this.profileImageUrlHttps = profileImageUrlHttps;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName =  screenName;
    }


    public User() {
        super();
    }
    protected User(android.os.Parcel in) {
        name = in.readString();
        profileImageUrl = in.readString();
        createdAt = in.readString();
        profileImageUrlHttps = in.readString();
        id = in.readLong();
        description = in.readString();
        screenName = in.readString();
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(profileImageUrl);
        dest.writeString(createdAt);
        dest.writeString(profileImageUrlHttps);
        dest.writeLong(id);
        dest.writeString(description);
        dest.writeString(screenName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(android.os.Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
