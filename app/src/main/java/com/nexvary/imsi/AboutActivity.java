package com.nexvary.imsi;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;

public class AboutActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        boolean ar = LanguageUtil.current(this).equals("ar");
        ScrollView scroll = new ScrollView(this);
        LinearLayout page = UiKit.page(this);
        scroll.addView(page);
        Button back = UiKit.button(this, ar ? "رجوع" : "Back");
        back.setOnClickListener(v -> finish());
        page.addView(back);
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.nexvary_logo);
        logo.setContentDescription("Nexvary Inc.");
        page.addView(logo, new LinearLayout.LayoutParams(UiKit.dp(this,100),UiKit.dp(this,100)));
        page.addView(UiKit.title(this, "NEXVARY INC."));
        page.addView(UiKit.eyebrow(this,"TECHNOLOGY · RESEARCH · INNOVATION"));
        page.addView(UiKit.title(this, "NEXVARY Tower Guard"));
        page.addView(UiKit.body(this, (ar ? "الإصدار " : "Version ") + BuildConfig.VERSION_NAME));
        page.addView(UiKit.body(this, ar ? "أداة دفاعية لتحليل مؤشرات الشذوذ في الشبكات الخلوية. لا تثبت النتائج وجود أو غياب IMSI Catcher. التحليل محلي، وتفتح الروابط التالية في تطبيق خارجي عند اختيارها." : "Defensive cellular anomaly awareness. Results cannot prove the presence or absence of an IMSI catcher. Analysis is local; links open externally only when selected."));
        link(page, ar ? "الموقع الرسمي" : "Official website", "https://nexvary.com", false);
        link(page, ar ? "عن الشركة" : "Company", "https://nexvary.com/about", false);
        link(page, ar ? "الدعم والتواصل" : "Contact and support", "https://nexvary.com/contact", false);
        link(page, ar ? "البريد الإلكتروني" : "Email", "mailto:info@nexvary.com", true);
        link(page, ar ? "الأبحاث والابتكار" : "Research and innovation", "https://nexvary.com/research", false);
        link(page, ar ? "تطبيقات الشركة" : "Company applications", "https://nexvary.com/apps", false);
        link(page, "GitHub", "https://github.com/nexvary", false);
        link(page, ar ? "خصوصية الموقع الإلكتروني" : "Website privacy policy", "https://nexvary.com/privacy", false);
        Button privacy = UiKit.button(this, ar ? "سياسة خصوصية التطبيق" : "App privacy policy");
        privacy.setOnClickListener(v -> startActivity(new Intent(this, PrivacyActivity.class)));
        page.addView(privacy);
        page.addView(UiKit.body(this,"© 2026 Nexvary Inc. · Wyoming, United States"));
        setContentView(UiKit.withBottomNav(this, scroll, 4));
    }
    private void link(LinearLayout page, String label, String uri, boolean mail) {
        Button button = UiKit.button(this, label);
        button.setOnClickListener(v -> {
            try { startActivity(new Intent(mail ? Intent.ACTION_SENDTO : Intent.ACTION_VIEW, Uri.parse(uri))); }
            catch (ActivityNotFoundException | SecurityException e) {
                Toast.makeText(this, LanguageUtil.current(this).equals("ar") ? "لا يوجد تطبيق مناسب لفتح الرابط" : "No app available to open this link", Toast.LENGTH_LONG).show();
            }
        });
        page.addView(button);
    }
}
