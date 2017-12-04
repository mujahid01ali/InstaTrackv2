package com.example.mujahid.instatrackv2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mujahid on 11/18/2017.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String Mypref = "MyPref";
    private static final String key_user = "phone";
    private static final String key_gId = "groupId";
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean login(String phone) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_user, phone);
        editor.commit();
        editor.apply();
        return true;
    }
    public boolean groupId(String id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_gId, id);
        editor.commit();
        editor.apply();
        return true;
    }

    public boolean isLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(key_user, null) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void logOut() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();


    }

    public String getPhoneNumber() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key_user, null);

    }

    public String getGroupId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key_gId, null);

    }
}
