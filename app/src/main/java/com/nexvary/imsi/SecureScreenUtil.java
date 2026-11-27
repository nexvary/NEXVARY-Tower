package com.nexvary.imsi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.WindowManager;

/**
 * Manages FLAG_SECURE based on user preference.
 * Default: screenshots allowed (FLAG_SECURE disabled).
 * User can enable screenshot prevention in Privacy settings.
 */
public final class SecureScreenUtil {
    private static final String PREF = "nexvary_ui";
    static final String KEY_SCREENSHOT_PREVENTION = "screenshot_prevention";

    private SecureScreenUtil() {}

    /** Apply FLAG_SECURE only if the user opted in to screenshot prevention. */
    public static void apply(Activity a) {
        SharedPreferences prefs = a.getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        boolean prevent = prefs.getBoolean(KEY_SCREENSHOT_PREVENTION, false);
        if (prevent) {
            a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    public static boolean isEnabled(Activity a) {
        return a.getSharedPreferences(PREF, Activity.MODE_PRIVATE)
                .getBoolean(KEY_SCREENSHOT_PREVENTION, false);
    }

    public static void setEnabled(Activity a, boolean enabled) {
        a.getSharedPreferences(PREF, Activity.MODE_PRIVATE).edit()
                .putBoolean(KEY_SCREENSHOT_PREVENTION, enabled).apply();
    }
}
