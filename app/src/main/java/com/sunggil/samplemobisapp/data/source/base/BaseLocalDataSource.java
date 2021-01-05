package com.sunggil.samplemobisapp.data.source.base;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class BaseLocalDataSource {
    private SharedPreferences preferences = null;

    public BaseLocalDataSource(Context context) {
        preferences = context.getSharedPreferences("MOBIS_PREFERENCE", Context.MODE_PRIVATE);
    }

    public <T> void putSharedPreference(HashMap<String, T> map) {
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : map.keySet()) {
            if (map.get(key) instanceof String) {
                editor.putString(key, (String) map.get(key));
            } else if (map.get(key) instanceof Integer) {
                editor.putInt(key, (Integer) map.get(key));
            } else if (map.get(key) instanceof Boolean) {
                editor.putBoolean(key, (Boolean) map.get(key));
            }
        }
        editor.apply();
    }

    public String getSharedPreferenceString(String key, String def) {
        return preferences.getString(key, def);
    }

    public int getSharedPreferenceInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public boolean getSharedPreferenceBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }
}
