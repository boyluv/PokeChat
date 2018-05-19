package com.example.tuanle.chatapplication.Utils.Data;


/**
 * Created by spikewuller on 7/26/17.
 */

public class User {
    private String token;
    private String email;
    private boolean hasProfile;
    private boolean isActivated;
    private int profileId;

    public User(String token, String email, boolean hasProfile, boolean isActivated, int profileId) {
        this.token = token;
        this.email = email;
        this.hasProfile = hasProfile;
        this.isActivated = isActivated;
        this.profileId = profileId;
    }


    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public boolean hasProfile() {
        return hasProfile;
    }

    public String getToken() {
        return token;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivationStatus(boolean newStatus) {
        this.isActivated = newStatus;
    }

    public String getEmail() {
        return email;
    }

    public int getProfileId() {
        return profileId;
    }
}