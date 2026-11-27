package com.nexvary.imsi;

import android.app.*;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/** More tab: settings, baselines, language, links, about. TG06/TG07/TG08. */
public class MoreActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(2, 7, 13));
        LinearLayout page = UiKit.page(this);
        UiKit.applySystemBars(page);
        scroll.addView(page);
        setContentView(UiKit.withBottomNav(this, scroll, 4));
        buildUi(page);
    }

    private void buildUi(LinearLayout page) {
        boolean ar = LanguageUtil.current(this).equals("ar");

        page.addView(UiKit.eyebrow(this, ar ? "المزيد" : "MORE"));
        page.addView(UiKit.title(this, ar ? "الإعدادات والمعلومات" : "Settings & Info"));
        page.addView(UiKit.gap(this, 16));

        // Language toggle
        Button lang = UiKit.button(this, ar ? "English / عربية" : "العربية / EN");
        lang.setOnClickListener(v -> { LanguageUtil.toggle(this); recreate(); });
        page.addView(lang);

        // Trusted baselines
        Button bases = UiKit.button(this, ar ? "خطوط الأساس الموثوقة" : "TRUSTED BASELINES");
        bases.setOnClickListener(v -> startActivity(new android.content.Intent(this, BaselinesActivity.class)));
        page.addView(bases);

        // Privacy policy
        Button privacy = UiKit.button(this, ar ? "سياسة الخصوصية" : "PRIVACY POLICY");
        privacy.setOnClickListener(v -> startActivity(new android.content.Intent(this, PrivacyActivity.class)));
        page.addView(privacy);

        // User guide placeholder
        Button guide = UiKit.button(this, ar ? "دليل المستخدم" : "USER GUIDE");
        guide.setOnClickListener(v -> startActivity(new android.content.Intent(this, AboutActivity.class)));
        page.addView(guide);

        page.addView(UiKit.gap(this, 20));
        page.addView(UiKit.eyebrow(this, ar ? "تواصل مع نيكسفاري" : "CONTACT NEXVARY INC."));
        page.addView(UiKit.gap(this, 8));

        // Company links
        page.addView(linkRow(ar));

        page.addView(UiKit.gap(this, 16));
        page.addView(UiKit.eyebrow(this, ar ? "عن التطبيق" : "ABOUT"));
        Button about = UiKit.button(this, ar ? "عن التطبيق والتوعية الأمنية" : "About & Security Awareness");
        about.setOnClickListener(v -> startActivity(new android.content.Intent(this, SecurityAwarenessActivity.class)));
        page.addView(about);

        page.addView(UiKit.gap(this, 20));
        TextView footer = UiKit.body(this, ar ?
            "لا يوجد إذن INTERNET. المراقبة في الواجهة فقط. المحتوى مشفر بـ AES-GCM." :
            "No INTERNET permission. Monitoring foreground-only. History encrypted with AES-GCM.");
        footer.setTextColor(Color.rgb(70, 95, 115));
        page.addView(footer);
    }

    private View linkRow(boolean ar) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.panel);
        int p = UiKit.dp(this, 14);
        row.setPadding(p, p, p, p);

        addLink(row, "🌐", ar ? "الموقع" : "Website", v -> LinkHelper.openUrl(this, LinkHelper.URL_WEBSITE), Color.rgb(23, 151, 255));
        addLink(row, "f", "Facebook", v -> LinkHelper.openUrl(this, LinkHelper.URL_FACEBOOK), Color.rgb(24, 119, 242));
        addLink(row, "▶", "YouTube", v -> LinkHelper.openUrl(this, LinkHelper.URL_YOUTUBE), Color.rgb(255, 0, 0));
        addLink(row, "✕", "X", v -> LinkHelper.openUrl(this, LinkHelper.URL_X), Color.WHITE);
        addLink(row, "✉", ar ? "البريد" : "Email", v -> LinkHelper.openEmail(this), Color.rgb(255, 181, 63));
        return row;
    }

    private void addLink(LinearLayout row, String icon, String label, View.OnClickListener cl, int color) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        item.setOnClickListener(cl);

        TextView ic = new TextView(this);
        ic.setText(icon);
        ic.setTextColor(color);
        ic.setTextSize(22f);
        ic.setGravity(Gravity.CENTER);
        item.addView(ic);

        TextView lbl = new TextView(this);
        lbl.setText(label);
        lbl.setTextColor(Color.rgb(90, 120, 148));
        lbl.setTextSize(10f);
        lbl.setGravity(Gravity.CENTER);
        item.addView(lbl);
        row.addView(item);
    }
}
