package com.nexvary.imsi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AboutActivity extends Activity {
    private static final String WEBSITE = "https://nexvary.com";

    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        SecureScreenUtil.apply(this);
        boolean ar = LanguageUtil.current(this).equals("ar");

        ScrollView s = new ScrollView(this);
        s.setClipToPadding(false);
        LinearLayout p = UiKit.page(this);
        s.addView(p);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.nexvary_logo);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UiKit.dp(this, 82), UiKit.dp(this, 82));
        logo.setLayoutParams(lp);
        p.addView(logo);

        p.addView(UiKit.backButton(this)); p.addView(UiKit.eyebrow(this, "NEXVARY · EXECUTIVE TECHNOLOGY PROTECTION"));
        p.addView(UiKit.title(this, ar ? "حول التطبيق" : "About"));
        p.addView(UiKit.body(this, ar
                ? "NEXVARY Tower Guard\nالإصدار 0.7.0\n\nأداة دفاعية لتحليل مؤشرات الشبكة الخلوية التي يتيحها Android، ومقارنة تغيرات هوية الخلية ونوع الشبكة والإشارة والخلايا المجاورة. النتائج مؤشرات مخاطر وليست إثباتًا قاطعًا على وجود أو غياب برج خلوي مزيف أو IMSI Catcher.\n\nجميع عمليات التحليل والتخزين تتم محليًا على الجهاز في هذا الإصدار."
                : "NEXVARY Tower Guard\nVersion 0.7.0\n\nA defensive utility for reviewing Android-exposed cellular indicators and comparing changes in serving-cell identity, radio generation, signal and neighboring cells. Results are risk indicators, not conclusive proof of the presence or absence of a rogue cell site or IMSI catcher.\n\nAnalysis and storage are local to the device in this release."));

        p.addView(UiKit.body(this, ar ? "الموقع الرسمي\n" + WEBSITE : "Official website\n" + WEBSITE));
        Button site = UiKit.button(this, ar ? "فتح موقع NEXVARY" : "OPEN NEXVARY WEBSITE");
        site.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE));
            startActivity(i);
        });
        p.addView(site);

        setContentView(UiKit.withBottomNav(this,s,4));
    }
}
