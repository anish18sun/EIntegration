package com.hypervisor.eintegrator.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dexter on 15/3/17.
 */
public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "eintegrators-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LANGUAGE = "language";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clear() {
        editor.clear().commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public String getLanguage() {
        return pref.getString(LANGUAGE, null);
    }

    public void setLanguage(String language) {
        editor.putString(LANGUAGE, language);
        editor.commit();
    }

    public String getBhamashahId() {
        return pref.getString(Constants.BHAMASHAH_ID, null);
    }

    public void setBhamashahId(String bhamashahId) {
        editor.putString(Constants.BHAMASHAH_ID, bhamashahId);
        editor.commit();
    }

    public void setPaymentMade() {
        editor.putString("payment", "success");
        editor.commit();
    }

    public String getPaymentMade() {
        return pref.getString("payment", null);
    }

}