package com.nexvary.imsi;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;

public class OnboardingActivity extends Activity {
    private static final String PREF="nexvary_ui", DONE="onboarding_done";
    @Override public void onCreate(Bundle b){
        LanguageUtil.apply(this); super.onCreate(b); SecureScreenUtil.apply(this);
        if(getSharedPreferences(PREF,MODE_PRIVATE).getBoolean(DONE,false)){ openMain(); return; }
        render();
    }
    private void render(){
        boolean ar=LanguageUtil.current(this).equals("ar");
        ScrollView scroll=new ScrollView(this); UiKit.applySystemBars(scroll); scroll.setClipToPadding(false); LinearLayout p=UiKit.page(this); scroll.addView(p);
        ImageView logo=new ImageView(this); logo.setImageResource(R.drawable.nexvary_logo); LinearLayout.LayoutParams ilp=new LinearLayout.LayoutParams(UiKit.dp(this,84),UiKit.dp(this,84)); logo.setLayoutParams(ilp); p.addView(logo);
        p.addView(UiKit.eyebrow(this,"NEXVARY LABS · PRIVATE CELLULAR AWARENESS"));
        p.addView(UiKit.title(this,ar?"مرحبًا بك في NEXVARY IMSI Detector":"Welcome to NEXVARY IMSI Detector"));
        p.addView(UiKit.body(this,ar?"أداة دفاعية تساعدك على ملاحظة التغيّرات غير المعتادة في بيئة الشبكة الخلوية. التطبيق لا يستطيع إثبات وجود IMSI Catcher بصورة قطعية، لكنه يجمع عدة مؤشرات للمراجعة.":"A defensive tool for noticing unusual changes in the cellular environment. It cannot conclusively prove an IMSI catcher is present; it combines several indicators for review."));
        p.addView(UiKit.gap(this,10));
        p.addView(UiKit.eyebrow(this,ar?"الخصوصية أولًا":"PRIVACY FIRST"));
        p.addView(UiKit.body(this,ar?"• لا توجد صلاحية INTERNET.\n• لا نقرأ IMSI أو IMEI.\n• السجل وخطوط الأساس مشفرة محليًا.\n• المراقبة المباشرة تعمل فقط أثناء ظهور التطبيق.":"• No INTERNET permission.\n• No IMSI or IMEI collection.\n• History and baselines are encrypted locally.\n• Live monitoring runs only while the app is visible."));
        p.addView(UiKit.eyebrow(this,ar?"لماذا نطلب صلاحية الموقع؟":"WHY LOCATION PERMISSION?"));
        p.addView(UiKit.body(this,ar?"يفرض Android صلاحية الموقع الدقيق على كثير من واجهات CellInfo حتى عندما لا يقوم التطبيق بجمع إحداثيات GPS. لا يخزن التطبيق موقعك الجغرافي.":"Android gates much CellInfo telemetry behind precise Location permission even when an app does not collect GPS coordinates. NEXVARY does not store your geographic location."));
        Button lang=UiKit.button(this,ar?"English":"العربية"); lang.setOnClickListener(v->{LanguageUtil.toggle(this); recreate();}); p.addView(lang);
        Button go=UiKit.button(this,ar?"متابعة":"CONTINUE"); go.setOnClickListener(v->{getSharedPreferences(PREF,MODE_PRIVATE).edit().putBoolean(DONE,true).apply(); openMain();}); p.addView(go);
        setContentView(scroll);
    }
    private void openMain(){ startActivity(new Intent(this,MainActivity.class)); finish(); }
}
