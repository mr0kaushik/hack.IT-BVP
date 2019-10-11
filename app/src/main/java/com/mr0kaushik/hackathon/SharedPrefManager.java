/*
 *
 *  * Created by Deepak Kaushik
 *  * Copyright (c) 2019. All right reserved.
 *  * Last Modified 15/9/19 10:13 PM
 *
 */

package com.mr0kaushik.hackathon;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String TAG = "SharedPrefManager";
    public static final String KEY_USER_ID = "user_id";
    private Context ctx;
    private static SharedPrefManager instance;

    public static final String SHARED_PREF_NAME = "hackathon";


    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setUserID(String userID) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userID);
        editor.apply();
    }

    public String getProfileURL(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        StringBuilder url = new StringBuilder();
        url.append("");
        String profileUrl = sharedPreferences.getString(KEY_USER_ID, null);
        if (profileUrl != null && profileUrl.length()>0) {
            url.append(profileUrl);
        }
        return url.toString();
    }


}