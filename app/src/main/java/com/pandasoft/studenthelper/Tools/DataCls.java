package com.pandasoft.studenthelper.Tools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class DataCls {
    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}
