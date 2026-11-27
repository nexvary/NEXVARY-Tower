package com.nexvary.imsi;

import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/** More tab: settings, language, security center, about. TG06/TG08. */
public class MoreActivity extends Activity {

    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        SecureScreenUtil.apply(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.BLACK);
        LinearLayout page = UiKit.page(this);
        UiKit.applySystemBars(page);
        scroll.addView(page);
        setContentView(UiKit.withBottomNav(this, scroll, 4));
        buildUi(page);
    }

    private void buildUi(LinearLayout page) {
        boolean ar = LanguageUtil.current(this).equals("ar");

        page.addView(UiKit.eyebrow(this, ar ? "المزيد" : "MORE"));
        page.addView(UiKit.title(this, ar ? "الإعدادات والخيارات" : "Settings & Options"));
        page.addView(UiKit.gap(this, 16));

        // Language toggle
        Button lang = UiKit.button(this, ar ? "English / عربية" : "العربية / EN");
        lang.setOnClickListener(v -> { LanguageUtil.toggle(this); recreate(); });
        page.addView(lang);

        // Privacy setting — screenshot prevention toggle
        addScreenshotToggle(page, ar);

        // Trusted baselines
        Button bases = UiKit.button(this, ar ? "خطوط الأساس الموثوقة" : "TRUSTED BASELINES");
        bases.setOnClickListener(v -> startActivity(new Intent(this, BaselinesActivity.class)));
        page.addView(bases);

        // Privacy policy
        Button privacy = UiKit.button(this, ar ? "سياسة الخصوصية" : "PRIVACY POLICY");
        privacy.setOnClickListener(v -> startActivity(new Intent(this, PrivacyActivity.class)));
        page.addView(privacy);

        page.addView(UiKit.gap(this, 20));
        page.addView(UiKit.eyebrow(this, ar ? "معرفة وتوعية" : "KNOWLEDGE & AWARENESS"));
        page.addView(UiKit.gap(this, 8));

        // Security Knowledge Center
        Button skc = UiKit.button(this, ar ? "مركز الوعي الأمني" : "SECURITY KNOWLEDGE CENTER");
        skc.setOnClickListener(v -> startActivity(new Intent(this, SecurityAwarenessActivity.class)));
        page.addView(skc);

        page.addView(UiKit.gap(this, 20));
        page.addView(UiKit.eyebrow(this, ar ? "المعلومات" : "INFORMATION"));
        page.addView(UiKit.gap(this, 8));

        // About Nexvary Inc. (renamed from User Guide)
        Button about = UiKit.button(this, ar ? "عن Nexvary Inc." : "About Nexvary Inc.");
        about.setTextColor(Color.rgb(0, 176, 255));
        about.setOnClickListener(v -> startActivity(new Intent(this, AboutNexvaryActivity.class)));
        page.addView(about);

        page.addView(UiKit.gap(this, 24));
        TextView footer = UiKit.body(this,
            ar ? "لا يوجد إذن INTERNET. المراقبة في الواجهة فقط. المحتوى مشفر بـ AES-GCM."
               : "No INTERNET permission. Monitoring foreground-only. History encrypted AES-GCM.");
        footer.setTextColor(Color.rgb(55, 70, 85));
        footer.setTextSize(12f);
        page.addView(footer);
    }

    private void addScreenshotToggle(LinearLayout page, boolean ar) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.panel);
        int p = UiKit.dp(this, 14);
        row.setPadding(p, p, p, p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = UiKit.dp(this, 8);
        row.setLayoutParams(lp);

        TextView lbl = new TextView(this);
        lbl.setText(ar ? "منع لقطات الشاشة" : "Prevent Screenshots");
        lbl.setTextColor(Color.rgb(192, 192, 192));
        lbl.setTextSize(14f);
        lbl.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(lbl);

        CheckBox cb = new CheckBox(this);
        cb.setChecked(SecureScreenUtil.isEnabled(this));
        cb.setOnCheckedChangeListener((v, checked) -> {
            SecureScreenUtil.setEnabled(this, checked);
            SecureScreenUtil.apply(this);
        });
        row.addView(cb);
        page.addView(row);
    }
}
