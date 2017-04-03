package com.codepath.apps.twitterEnhanced.models;

/**
 * Created by Saranu on 3/21/17.
 */

import android.os.Parcelable;

import com.codepath.apps.twitterEnhanced.dao.TwitterDatabase;
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

    @Column
    public String profileBackgroundImageUrl;

    @PrimaryKey
    public long id;

    @Column
    public String description;

    @Column
    public Boolean verified;

    @Column
    public String screenName;

    @Column
    private String location;

    @Column
    private boolean _protected;

    @Column
    private long followersCount;

    @Column
    private long friendsCount;

    @Column
    private long favouritesCount;

    public long cursorID =-1;


    public long getCursorID() {
        return cursorID;
    }

    public void setCursorID(long cursorID) {
        this.cursorID = cursorID;
    }

    public long getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(long friendsCount) {
        this.friendsCount = friendsCount;
    }

    protected User(android.os.Parcel in) {
        name = in.readString();
        profileImageUrl = in.readString();
        createdAt = in.readString();
        profileImageUrlHttps = in.readString();
        id = in.readLong();
        description = in.readString();
        screenName = in.readString();
        location = in.readString();
        _protected = in.readByte() != 0;
        followersCount = in.readLong();
        friendsCount = in.readLong();
        favouritesCount = in.readLong();
        profileBackgroundImageUrl = in.readString();
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean is_protected() {
        return _protected;
    }

    public void set_protected(boolean _protected) {
        this._protected = _protected;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(long favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

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


    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
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

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeString(location);
        dest.writeByte((byte) (_protected ? 1 : 0));
        dest.writeLong(followersCount);
        dest.writeLong(friendsCount);
        dest.writeLong(favouritesCount);
        dest.writeString(profileBackgroundImageUrl);
    }


    /* protected User(android.os.Parcel in) {
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

*/
}
