package com.sunggil.samplemobisapp.data.source.base;

import java.util.HashMap;

public interface BaseSource {
    <T> void putPreference(HashMap<String, T> map);

    String getPreferenceString(String key, String def);

    int getPreferenceInt(String key, int def);

    boolean getPreferenceBoolean(String key, boolean def);
}
