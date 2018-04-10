package com.example1.archi.assign5;

import android.net.sip.SipErrorCode;

import java.io.Serializable;


public class Channel implements Serializable{
    private String googleID;
    private String twitterID;
    private String facebookID;
    private String youtubeID;

    public Channel() {
    }

    public Channel(String googleID, String twitterID, String facebookID, String youtubeID) {
        this.googleID = googleID;
        this.twitterID = twitterID;
        this.facebookID = facebookID;
        this.youtubeID = youtubeID;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getTwitterID() {
        return twitterID;
    }

    public void setTwitterID(String twitterID) {
        this.twitterID = twitterID;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getYoutubeID() {
        return youtubeID;
    }

    public void setYoutubeID(String youtubeID) {
        this.youtubeID = youtubeID;
    }

    @Override
    public String toString() {
        return "Google ID "+googleID
                +"\n Facebook "+facebookID
                +"\n twitter "+twitterID
                +"\n youtube "+youtubeID;

    }
}