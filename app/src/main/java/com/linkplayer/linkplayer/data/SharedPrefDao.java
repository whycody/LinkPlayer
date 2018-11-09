package com.linkplayer.linkplayer.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefDao {

    private SharedPreferences sharedPreferences;
    private final String PREFERENCES = "preferences";

    public SharedPrefDao(Context context){
        sharedPreferences =  context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveBooleanValue(String name, boolean value){
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean(name, value);
        preferencesEditor.apply();
    }

    public boolean getBooleanValue(String value){
        return sharedPreferences.getBoolean(value, false);
    }
}
