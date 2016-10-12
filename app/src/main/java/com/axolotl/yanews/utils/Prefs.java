package com.axolotl.yanews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.axolotl.yanews.R;


public class Prefs {

    private Context mContext;

    public Prefs(Context context) {
        mContext = context;
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    public long getLastCheckTime() {
        return getPrefs().getLong(mContext.getString(R.string.check_key), 0);
    }

    public void updateCheckTime() {
        getPrefs().edit().putLong(mContext.getString(R.string.check_key), System.currentTimeMillis()).apply();
    }

    public String getAuthToken() {
        return getPrefs().getString(mContext.getString(R.string.auth_token_pref), null);
    }

    public void setAuthToken(String token) {
        getPrefs().edit().putString(mContext.getString(R.string.auth_token_pref), token).apply();
    }

    public void setDisplayName(String displayName) {
        getPrefs().edit().putString(
                mContext.getString(R.string.user_display_name_pref), displayName).apply();
    }

    public String getDisplayName() {
        return getPrefs().getString(mContext.getString(R.string.user_display_name_pref), null);
    }

    public void removeAuthToken() {
        getPrefs().edit().remove(mContext.getString(R.string.auth_token_pref)).apply();
    }
}
