/*
 *
 *  * Created by Deepak Kaushik
 *  * Copyright (c) 2019. All right reserved.
 *  * Last Modified 15/9/19 10:13 PM
 *
 */

package com.mr0kaushik.hackathon;

import android.content.Context;

public class SharedPrefManager {

    public static final String TAG = "SharedPrefManager";
    public static final String KEY_EMAIL = "mobile_number";
    private static final String KEY_NAME = "has_visited";
    private static final String KEY_DOB = "user_lat";
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

    /*public void userLogin(String token, String mobile_number){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_MOBILE, mobile_number);
        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        return token != null && token.length() > 0;
    }


    *//*public String getMobileNumber(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String number = sharedPreferences.getString(KEY_MOBILE, null);
        if (number != null && number.length()>0){
            return number;
        }
        return null;
    }*//*

    public void logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOBILE, null);
        editor.putString(KEY_TOKEN, null);
        editor.putString(KEY_USER_PROFILE_URL, null);
        editor.putString(KEY_USER_LAT, null);
        editor.putString(KEY_USER_LNG, null);
        editor.apply();
        ctx.startActivity(new Intent(ctx, WelcomeActivity.class));
    }

    public boolean hasVisitedWalkThrough(){
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_VISITED, false);
    }

    public void setVisited(Boolean visited){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEY_VISITED, visited);
        editor.apply();

    }

    public String getTokenValue(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if ( token != null && token.length()>0) {
            return token;
        }
        return null;
    }

    public void setUserLatLng(double lat, double lng){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_LAT, String.valueOf(lat));
        editor.putString(KEY_USER_LNG, String.valueOf(lng));
        editor.apply();
    }

    public LatLng getUserLatLng(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        double lat = Double.valueOf(sharedPreferences.getString(KEY_USER_LAT, String.valueOf(-1)));
        double lng = Double.valueOf(sharedPreferences.getString(KEY_USER_LNG, String.valueOf(-1)));

        return (new LatLng(lat, lng));
    }

    public String getProfileURL(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        StringBuilder url = new StringBuilder();
        url.append("");
        String profileUrl = sharedPreferences.getString(KEY_USER_PROFILE_URL, null);
        if (profileUrl != null && profileUrl.length()>0) {
            url.append(sharedPreferences.getString(KEY_USER_PROFILE_URL, null));
        }
        return url.toString();
    }

    public void setProfileURL(String photo_url) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_PROFILE_URL, photo_url);
        editor.apply();
    }*/
}