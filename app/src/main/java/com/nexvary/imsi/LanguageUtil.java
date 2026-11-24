package com.nexvary.imsi;

import android.content.*;
import android.content.res.Configuration;
import java.util.Locale;

public final class LanguageUtil {
    private static final String PREF="nexvary_ui";
    private static final String KEY="language";
    private LanguageUtil(){}
    public static String current(Context c){ return c.getSharedPreferences(PREF,Context.MODE_PRIVATE).getString(KEY,"en"); }
    public static void set(Context c,String lang){ c.getSharedPreferences(PREF,Context.MODE_PRIVATE).edit().putString(KEY,lang).apply(); apply(c); }
    public static void toggle(Context c){ set(c, current(c).equals("ar")?"en":"ar"); }
    public static void apply(Context c){
        String lang=current(c); Locale locale=new Locale(lang); Locale.setDefault(locale);
        Configuration config=new Configuration(c.getResources().getConfiguration());
        config.setLocale(locale); config.setLayoutDirection(locale);
        c.getResources().updateConfiguration(config,c.getResources().getDisplayMetrics());
    }
}
