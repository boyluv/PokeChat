package com.example.tuanle.chatapplication.Utils;

import android.app.Activity;
import android.content.Context;

import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.Data.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by spikewuller on 5/27/17.
 */

public class CoreManager {
    private static CoreManager _instance;

    private Activity currentActivity;
    private Gson gson;
    private User user;

    public CoreManager() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static CoreManager getInstance() {
        if (_instance == null) {
            _instance = new CoreManager();
        }

        return _instance;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public User getUser() {
        if (user == null && isLogined(currentActivity)) {
            user = gson.fromJson(PreferenceUtils.getStringPref(ExtraKey.USER_INFO, null), User.class);
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        PreferenceUtils.saveStringPref(ExtraKey.USER_INFO, gson.toJson(user));
    }

    public void logOut() {
        PreferenceUtils.remove(ExtraKey.USER_INFO);
    }

    public boolean isLogined(Context context) {
        return PreferenceUtils.isExist(context, ExtraKey.USER_INFO);
    }
}