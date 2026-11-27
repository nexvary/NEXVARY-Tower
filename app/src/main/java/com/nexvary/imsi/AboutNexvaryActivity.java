package com.nexvary.imsi;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/** About Nexvary Inc. - premium bilingual company profile. TG07/TG08. Owner-approved 2026-11-27. */
public class AboutNexvaryActivity extends Activity {

    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        SecureScreenUtil.apply(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.BLACK);
        scroll.setClipToPadding(false);
        LinearLayout page = UiKit.page(this);
        UiKit.applySystemBars(page);
        scroll.addView(page);
        setContentView(UiKit.withBottomNav(this, scroll, 4));
        buildUi(page);
    }

    @Override public void onBackPressed() { finish(); }

    private void buildUi(LinearLayout page) {
        boolean ar = LanguageUtil.current(this).equals("ar");
        page.addView(UiKit.backButton(this));

        // Header with logo and title
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setBackgroundResource(R.drawable.panel_silver);
        int hp = UiKit.dp(this, 16);
        header.setPadding(hp, hp, hp, hp);
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        hlp.bottomMargin = UiKit.dp(this, 16);
        header.setLayoutParams(hlp);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.nexvary_logo);
        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        header.addView(logo, new LinearLayout.LayoutParams(UiKit.dp(this, 60), UiKit.dp(this, 60)));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(UiKit.dp(this, 12), 0, 0, 0);
        info.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView nameView = new TextView(this);
        nameView.setText("NEXVARY INC.");
        nameView.setTextColor(Color.WHITE);
        nameView.setTextSize(18f);
        nameView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        info.addView(nameView);

        TextView tag = new TextView(this);
        tag.setText("Defense Software \u00b7 Research \u00b7 Advanced Technology");
        tag.setTextColor(Color.rgb(0, 176, 255));
        tag.setTextSize(10f);
        info.addView(tag);
        header.addView(info);
        page.addView(header);

        // Description card with Arabic + English
        page.addView(UiKit.eyebrow(this, ar ? "\u0639\u0646 \u0627\u0644\u0634\u0631\u0643\u0629" : "ABOUT THE COMPANY"));
        page.addView(UiKit.gap(this, 8));

        LinearLayout descCard = new LinearLayout(this);
        descCard.setOrientation(LinearLayout.VERTICAL);
        descCard.setBackgroundResource(R.drawable.panel);
        int dp16 = UiKit.dp(this, 16);
        descCard.setPadding(dp16, dp16, dp16, dp16);
        LinearLayout.LayoutParams dclp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dclp.bottomMargin = UiKit.dp(this, 16);
        descCard.setLayoutParams(dclp);

        // Owner-approved Arabic description (verbatim)
        TextView arDesc = new TextView(this);
        arDesc.setText("\u0634\u0631\u0643\u0629 \u062a\u0643\u0646\u0648\u0644\u0648\u062c\u064a\u0629 \u0645\u062a\u062e\u0635\u0635\u0629 \u0641\u064a \u062a\u0637\u0648\u064a\u0631 \u0627\u0644\u0628\u0631\u0645\u062c\u064a\u0627\u062a \u0627\u0644\u062f\u0641\u0627\u0639\u064a\u0629\u060c \u0648\u0627\u0644\u0623\u0645\u0646 \u0627\u0644\u0631\u0642\u0645\u064a\u060c \u0648\u0627\u0644\u0628\u062d\u062b \u0627\u0644\u0639\u0644\u0645\u064a\u060c \u0648\u0627\u0644\u0630\u0643\u0627\u0621 \u0627\u0644\u0627\u0635\u0637\u0646\u0627\u0639\u064a\u060c \u0648\u062a\u0642\u0646\u064a\u0627\u062a \u062a\u062d\u0644\u064a\u0644 \u0627\u0644\u0628\u064a\u0627\u0646\u0627\u062a.\n\n\u062a\u0639\u0645\u0644 \u0627\u0644\u0634\u0631\u0643\u0629 \u0639\u0644\u0649 \u062a\u0637\u0648\u064a\u0631 \u062d\u0644\u0648\u0644 \u062a\u0642\u0646\u064a\u0629 \u0645\u0628\u062a\u0643\u0631\u0629 \u0644\u062a\u0639\u0632\u064a\u0632 \u0627\u0644\u0623\u0645\u0646 \u0648\u0627\u0644\u062e\u0635\u0648\u0635\u064a\u0629 \u0648\u0627\u0644\u0648\u0639\u064a \u0628\u0627\u0644\u0645\u062e\u0627\u0637\u0631 \u0627\u0644\u0631\u0642\u0645\u064a\u0629\u060c \u0648\u062f\u0639\u0645 \u0627\u0644\u0628\u062d\u062b \u0648\u0627\u0644\u062a\u0637\u0648\u064a\u0631 \u0641\u064a \u0645\u062c\u0627\u0644\u0627\u062a \u0627\u0644\u062a\u0643\u0646\u0648\u0644\u0648\u062c\u064a\u0627 \u0627\u0644\u0645\u062a\u0642\u062f\u0645\u0629.");
        arDesc.setTextColor(Color.rgb(192, 192, 192));
        arDesc.setTextSize(14f);
        arDesc.setTextDirection(View.TEXT_DIRECTION_RTL);
        arDesc.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        arDesc.setLineSpacing(0, 1.5f);
        descCard.addView(arDesc);

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(30, 30, 30));
        LinearLayout.LayoutParams divlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        divlp.topMargin = UiKit.dp(this, 12);
        divlp.bottomMargin = UiKit.dp(this, 12);
        divider.setLayoutParams(divlp);
        descCard.addView(divider);

        // English description
        TextView enDesc = new TextView(this);
        enDesc.setText("Nexvary Inc. is a technology company specializing in defensive software development, digital security, scientific research, artificial intelligence, and data analytics.\n\nThe company develops innovative technological solutions to enhance security, privacy, and awareness of digital risks, while supporting research and development in advanced technology.");
        enDesc.setTextColor(Color.rgb(168, 183, 201));
        enDesc.setTextSize(14f);
        enDesc.setLineSpacing(0, 1.5f);
        descCard.addView(enDesc);
        page.addView(descCard);

        // Contact links section
        page.addView(UiKit.eyebrow(this, ar ? "\u062a\u0648\u0627\u0635\u0644 \u0645\u0639\u0646\u0627" : "CONTACT & LINKS"));
        page.addView(UiKit.gap(this, 10));

        page.addView(contactCard(ar, R.drawable.ic_social_website,
            ar ? "\u0627\u0644\u0645\u0648\u0642\u0639" : "Website", "nexvary.com",
            Color.rgb(0, 176, 255), v -> LinkHelper.openUrl(this, LinkHelper.URL_WEBSITE)));

        page.addView(contactCard(ar, R.drawable.ic_social_facebook,
            "Facebook", "facebook.com/nexvary",
            Color.rgb(24, 119, 242), v -> LinkHelper.openUrl(this, LinkHelper.URL_FACEBOOK)));

        page.addView(contactCard(ar, R.drawable.ic_social_youtube,
            "YouTube", "@NexvaryInc",
            Color.rgb(255, 0, 0), v -> LinkHelper.openUrl(this, LinkHelper.URL_YOUTUBE)));

        page.addView(contactCard(ar, R.drawable.ic_social_x,
            "X", "@Nexvary",
            Color.WHITE, v -> LinkHelper.openUrl(this, LinkHelper.URL_X)));

        page.addView(contactCard(ar, R.drawable.ic_social_email,
            ar ? "\u0627\u0644\u0628\u0631\u064a\u062f" : "Email", "info@nexvary.com",
            Color.rgb(255, 179, 0), v -> LinkHelper.openEmail(this)));

        page.addView(UiKit.gap(this, 20));
        TextView legal = UiKit.body(this, ar
            ? "NEXVARY Tower Guard \u0623\u062f\u0627\u0629 \u062f\u0641\u0627\u0639\u064a\u0629 \u062e\u0627\u0635\u0629. \u0627\u0644\u0646\u062a\u0627\u0626\u062c \u0645\u0624\u0634\u0631\u0627\u062a \u0648\u0644\u064a\u0633\u062a \u0625\u062b\u0628\u0627\u062a\u064b\u0627 \u0642\u0627\u0637\u0639\u064b\u0627."
            : "NEXVARY Tower Guard is a defensive, offline-only tool. Results are risk indicators, not conclusive proof.");
        legal.setTextColor(Color.rgb(55, 70, 85));
        legal.setTextSize(11f);
        page.addView(legal);
    }

    private View contactCard(boolean ar, int iconRes, String title, String subtitle,
                             int accentColor, View.OnClickListener cl) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setBackgroundResource(R.drawable.panel);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(cl);
        int p = UiKit.dp(this, 14);
        card.setPadding(p, p, p, p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = UiKit.dp(this, 8);
        card.setLayoutParams(lp);

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        card.addView(icon, new LinearLayout.LayoutParams(UiKit.dp(this, 36), UiKit.dp(this, 36)));

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(UiKit.dp(this, 12), 0, 0, 0);
        col.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setTextColor(Color.WHITE);
        tv1.setTextSize(14f);
        tv1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(subtitle);
        tv2.setTextColor(accentColor);
        tv2.setTextSize(12f);
        col.addView(tv2);
        card.addView(col);

        ImageView arr = new ImageView(this);
        arr.setImageResource(R.drawable.ic_back);
        arr.setScaleX(ar ? 1f : -1f);
        arr.setAlpha(0.4f);
        card.addView(arr, new LinearLayout.LayoutParams(UiKit.dp(this, 20), UiKit.dp(this, 20)));
        return card;
    }
}
