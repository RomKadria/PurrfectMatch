package com.example.purrfectmatch.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String EMAIL = "email";
    static final String PASSWORD = "password";
    static final String LOGIN = "login";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setEmail(Context ctx, String email) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public static String getEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(EMAIL, "");
    }

    public static String getPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(PASSWORD, "");
    }

    public static void setPassword(Context ctx, String password) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public static boolean getLogin(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(LOGIN, false);
    }

    public static void setLogin(Context ctx, boolean loginStatus) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(LOGIN, loginStatus);
        editor.commit();
    }

    public static void clearAll(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
