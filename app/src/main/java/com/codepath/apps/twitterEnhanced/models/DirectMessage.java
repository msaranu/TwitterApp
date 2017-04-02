package com.codepath.apps.twitterEnhanced.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Saranu on 4/2/17.
 */

public class DirectMessage  implements Parcelable{
        private String createdAt;
        private Entities entities;
        private long id;
        private String idStr;
        private long recipientId;
        private String recipientScreenName;
        private long senderId;
        private String senderScreenName;
        private String text;
        private Sender sender;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    protected DirectMessage(Parcel in) {
        createdAt = in.readString();
        entities = in.readParcelable(Entities.class.getClassLoader());
        id = in.readLong();
        idStr = in.readString();
        recipientId = in.readLong();
        recipientScreenName = in.readString();
        senderId = in.readLong();
        senderScreenName = in.readString();
        text = in.readString();
    }

    public static final Creator<DirectMessage> CREATOR = new Creator<DirectMessage>() {
        @Override
        public DirectMessage createFromParcel(Parcel in) {
            return new DirectMessage(in);
        }

        @Override
        public DirectMessage[] newArray(int size) {
            return new DirectMessage[size];
        }
    };

    public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Entities getEntities() {
            return entities;
        }

        public void setEntities(Entities entities) {
            this.entities = entities;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getIdStr() {
            return idStr;
        }

        public void setIdStr(String idStr) {
            this.idStr = idStr;
        }
        public long getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(long recipientId) {
            this.recipientId = recipientId;
        }

        public String getRecipientScreenName() {
            return recipientScreenName;
        }

        public void setRecipientScreenName(String recipientScreenName) {
            this.recipientScreenName = recipientScreenName;
        }

        public long getSenderId() {
            return senderId;
        }

        public void setSenderId(long senderId) {
            this.senderId = senderId;
        }

        public String getSenderScreenName() {
            return senderScreenName;
        }

        public void setSenderScreenName(String senderScreenName) {
            this.senderScreenName = senderScreenName;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeParcelable(entities, flags);
        dest.writeLong(id);
        dest.writeString(idStr);
        dest.writeLong(recipientId);
        dest.writeString(recipientScreenName);
        dest.writeLong(senderId);
        dest.writeString(senderScreenName);
        dest.writeString(text);
    }
}