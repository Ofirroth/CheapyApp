package com.example.cheapy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.MutableLiveData;

public class Cheapy extends Application {

    public static SharedPreferences preferences;

    public static MutableLiveData<String> urlServer;
    SharedPreferences.Editor editor;

    public static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = preferences.edit();
        urlServer = new MutableLiveData<>("http://10.0.2.2:5001");
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }
    public void set(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
    public void setString(String key, String value) {
        preferences.edit().putString(key, value);
        preferences.edit().apply();
    }

    public String get(String key) {
        return preferences.getString(key, "");
    }
}
