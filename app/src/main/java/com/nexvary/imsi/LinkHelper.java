package com.nexvary.imsi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/** Centralized company links opened via external Android intents. No INTERNET permission used. TG07. */
public final class LinkHelper {
    private LinkHelper() {}

    // Owner-verified 2026-11-27
    public static final String URL_WEBSITE  = "https://nexvary.com/";
    public static final String URL_FACEBOOK = "https://www.facebook.com/share/14p9krEn5ij/";
    public static final String URL_YOUTUBE  = "https://www.youtube.com/@NexvaryInc";
    public static final String URL_X        = "https://x.com/Nexvary";
    public static final String EMAIL        = "info@nexvary.com";

    public static void openUrl(Activity a, String url) {
        try {
            a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Toast.makeText(a, url, Toast.LENGTH_LONG).show();
        }
    }

    public static void openEmail(Activity a) {
        try {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + EMAIL));
            a.startActivity(i);
        } catch (Exception e) {
            Toast.makeText(a, EMAIL, Toast.LENGTH_LONG).show();
        }
    }
}
