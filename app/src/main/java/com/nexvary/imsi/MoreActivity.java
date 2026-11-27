package com.nexvary.imsi;

import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/** More tab: settings, language, security centre, about. TG06/TG08. */
public class MoreActivity extends Activity {

    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        SecureScreenUtil.apply(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.BLACK);
        LinearLayout page = UiKit.page(this);
        page.setPadding(UiKit.dp(this, 16), UiKit.dp(this, 12),
                UiKit.dp(this, 16), UiKit.dp(this, 12));
        scroll.addView(page);
        setContentView(UiKit.withBottomNav(this, scroll, 4));
        buildUi(page);
    }

    private void buildUi(LinearLayout page) {
        boolean ar = LanguageUtil.current(this).equals("ar");

        page.addView(UiKit.eyebrow(this, ar ? "\u0627\u0644\u0645\u0632\u064a\u062f" : "MORE"));
        page.addView(UiKit.title(this, ar ? "\u0627\u0644\u0625\u0639\u062f\u0627\u062f\u0627\u062a \u0648\u0627\u0644\u062e\u064a\u0627\u0631\u0627\u062a" : "Settings & Options"));
        page.addView(UiKit.gap(this, 10));

        // ---- SETTINGS section ----
        addSectionLabel(page, ar ? "\u0627\u0644\u0625\u0639\u062f\u0627\u062f\u0627\u062a" : "SETTINGS");

        // Language toggle card
        addCardRow(page, R.drawable.ic_more_language,
                ar ? "English / \u0639\u0631\u0628\u064a\u0629" : "\u0627\u0644\u0639\u0631\u0628\u064a\u0629 / EN",
                this::switchLanguage);

        // Screenshot prevention switch
        addScreenshotSwitch(page, ar);

        // Trusted baselines
        addCardRow(page, R.drawable.ic_more_baselines,
                ar ? "\u062e\u0637\u0648\u0637 \u0627\u0644\u0623\u0633\u0627\u0633 \u0627\u0644\u0645\u0648\u062b\u0648\u0642\u0629" : "Trusted Baselines",
                () -> startActivity(new Intent(this, BaselinesActivity.class)));

        page.addView(UiKit.gap(this, 6));

        // ---- RESOURCES section ----
        addSectionLabel(page, ar ? "\u0645\u0648\u0627\u0631\u062f" : "RESOURCES");

        // Privacy policy
        addCardRow(page, R.drawable.ic_more_privacy,
                ar ? "\u0633\u064a\u0627\u0633\u0629 \u0627\u0644\u062e\u0635\u0648\u0635\u064a\u0629" : "Privacy Policy",
                () -> startActivity(new Intent(this, PrivacyActivity.class)));

        // Security Knowledge Center
        addCardRow(page, R.drawable.ic_more_skc,
                ar ? "\u0645\u0631\u0643\u0632 \u0627\u0644\u0648\u0639\u064a \u0627\u0644\u0623\u0645\u0646\u064a" : "Security Knowledge Center",
                () -> startActivity(new Intent(this, SecurityAwarenessActivity.class)));

        page.addView(UiKit.gap(this, 6));

        // ---- INFORMATION section ----
        addSectionLabel(page, ar ? "\u0645\u0639\u0644\u0648\u0645\u0627\u062a" : "INFORMATION");

        // Company contact
        addCardRow(page, R.drawable.ic_more_about,
                ar ? "\u062a\u0648\u0627\u0635\u0644 \u0645\u0639 Nexvary Inc." : "Contact Nexvary Inc.",
                () -> startActivity(new Intent(this, AboutNexvaryActivity.class)));

        page.addView(UiKit.gap(this, 8));

        TextView footer = UiKit.body(this,
                ar ? "\u0644\u0627 \u064a\u0648\u062c\u062f \u0625\u0630\u0646 INTERNET. \u0627\u0644\u0645\u0631\u0627\u0642\u0628\u0629 \u0641\u064a \u0627\u0644\u0648\u0627\u062c\u0647\u0629 \u0641\u0642\u0637. \u0627\u0644\u0633\u062c\u0644 \u0645\u0634\u0641\u0651\u0631 AES-GCM."
                   : "No INTERNET permission. Monitoring foreground-only. History encrypted AES-GCM.");
        footer.setTextColor(Color.rgb(152, 169, 187));
        footer.setTextSize(12f);
        page.addView(footer);
        page.addView(UiKit.gap(this, 6));
    }

    private void switchLanguage() {
        LanguageUtil.toggle(this);
        // Rebuild the task so previously opened tabs also use the new locale.
        Intent dashboard = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivities(new Intent[]{dashboard, new Intent(this, MoreActivity.class)});
    }

    private void addSectionLabel(LinearLayout page, String text) {
        TextView lbl = new TextView(this);
        lbl.setText(text);
        lbl.setTextColor(Color.rgb(90, 110, 130));
        lbl.setTextSize(11f);
        lbl.setTypeface(null, android.graphics.Typeface.BOLD);
        lbl.setAllCaps(true);
        int h = UiKit.dp(this, 4);
        int v = UiKit.dp(this, 2);
        lbl.setPadding(h, v, h, v);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = UiKit.dp(this, 4);
        lbl.setLayoutParams(lp);
        page.addView(lbl);
    }

    private void addCardRow(LinearLayout page, int iconRes, String label, Runnable action) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.panel);
        int ph = UiKit.dp(this, 12);
        int pv = UiKit.dp(this, 6);
        row.setPadding(ph, pv, ph, pv);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = UiKit.dp(this, 4);
        row.setLayoutParams(lp);
        row.setMinimumHeight(UiKit.dp(this, 48));

        int s = UiKit.dp(this, 24);
        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(s, s);
        ilp.setMarginEnd(UiKit.dp(this, 12));
        icon.setLayoutParams(ilp);
        row.addView(icon);

        TextView lbl = new TextView(this);
        lbl.setText(label);
        lbl.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        lbl.setTextColor(Color.rgb(192, 192, 192));
        lbl.setTextSize(14f);
        lbl.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        row.addView(lbl);

        ImageView chevron = new ImageView(this);
        chevron.setImageResource(R.drawable.ic_chevron_right);
        chevron.setLayoutParams(new LinearLayout.LayoutParams(UiKit.dp(this, 20), UiKit.dp(this, 20)));
        row.addView(chevron);

        row.setOnClickListener(v -> action.run());
        row.setClickable(true);
        row.setFocusable(true);
        page.addView(row);
    }

    private void addScreenshotSwitch(LinearLayout page, boolean ar) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.panel);
        int ph = UiKit.dp(this, 12);
        row.setPadding(ph, 0, ph, 0);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = UiKit.dp(this, 4);
        row.setLayoutParams(lp);
        row.setMinimumHeight(UiKit.dp(this, 48));

        int s = UiKit.dp(this, 24);
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_more_screenshot);
        LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(s, s);
        ilp.setMarginEnd(UiKit.dp(this, 12));
        icon.setLayoutParams(ilp);
        row.addView(icon);

        Switch sw = new Switch(this);
        sw.setText(ar ? "منع لقطات الشاشة" : "Prevent Screenshots");
        sw.setTextColor(Color.rgb(192, 192, 192));
        sw.setTextSize(14f);
        sw.setMinHeight(UiKit.dp(this, 48));
        sw.setSwitchPadding(UiKit.dp(this, 12));
        sw.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        sw.setChecked(SecureScreenUtil.isEnabled(this));
        sw.setOnCheckedChangeListener((v, checked) -> {
            SecureScreenUtil.setEnabled(this, checked);
            SecureScreenUtil.apply(this);
        });
        row.addView(sw);
        page.addView(row);
    }
}
