package com.nexvary.imsi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Entirely offline defensive education, not interception guidance. */
public class SecurityAwarenessActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        LanguageUtil.apply(this);
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        boolean ar = LanguageUtil.current(this).equals("ar");
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout page = UiKit.page(this);
        scroll.addView(page);
        page.addView(UiKit.eyebrow(this, ar ? "مركز المعرفة الأمنية" : "SECURITY KNOWLEDGE CENTER"));
        page.addView(UiKit.title(this, ar ? "عن التطبيق والتوعية الأمنية" : "About & Security Awareness"));
        page.addView(UiKit.body(this, ar
                ? "NEXVARY Tower Guard أداة توعية دفاعية محلية ترصد بعض تغيرات الشبكة التي يتيح نظام Android الاطلاع عليها. لا تثبت وجود جهاز اعتراض ولا تضمن غيابه."
                : "NEXVARY Tower Guard is an offline defensive awareness tool. It reviews cellular changes exposed by Android. It cannot prove that interception is present or absent."));
        article(page, ar ? "كيف تعمل الشبكات الخلوية؟" : "How cellular networks work",
                ar ? "يتصل الهاتف بخلية تخدمه، وقد يرصد خلايا مجاورة. يشير MCC إلى بلد الشبكة وMNC إلى المشغل، بينما تستخدم TAC وPCI ومعرّفات الخلايا لتحديد مناطق التتبع والخلية أو الإشارة الفيزيائية وفق نوع الشبكة. تختلف الحقول المتاحة بين 2G و3G و4G و5G."
                   : "A phone connects to a serving cell and may observe neighboring cells. MCC identifies the country and MNC the operator. TAC, PCI and cell identities describe tracking areas, physical-layer identities and cells, depending on radio technology. Available fields differ between 2G, 3G, 4G and 5G.");
        article(page, ar ? "قراءة القياسات" : "Understanding radio measurements",
                ar ? "RSRP يقيس قدرة الإشارة المرجعية بالديسيبل ملي واط (dBm)، وRSRQ يقيس جودة الإشارة بالديسيبل (dB). SINR نسبة الإشارة إلى التداخل والضوضاء بالديسيبل، وRSSI مؤشر القدرة المستقبلة بوحدة dBm. المعاني الدقيقة وإتاحة القيم تعتمد على تقنية الراديو؛ N/A يعني أن القياس غير متاح، وليس صفرًا."
                   : "RSRP is reference-signal received power in dBm; RSRQ is a quality ratio in dB. SINR is signal-to-interference-plus-noise ratio in dB. RSSI represents received power in dBm. Interpretation and availability are technology-specific; N/A means unavailable, not zero.");
        article(page, ar ? "أجهزة محاكاة الأبراج وIMSI Catcher" : "Cell-site simulators and IMSI catchers",
                ar ? "قد تحاكي بعض أنظمة الاعتراض محطة قاعدة لاستدراج الأجهزة إلى التفاعل معها. تختلف القدرات بحسب التقنية والجيل الخلوي. لا تستطيع التطبيقات الاعتيادية إثبات وجود هذه الأنظمة من تغيّر الإشارة وحده."
                   : "Some interception systems imitate a cellular base station to induce device interaction. Their capabilities depend on network generation and configuration. A regular phone app cannot establish their presence from a signal change alone.");
        article(page, ar ? "ما هو SDR؟" : "What is software-defined radio (SDR)?",
                ar ? "الراديو المعرّف برمجيًا SDR هو نظام تنفّذ فيه البرمجيات وظائف كانت تعتمد سابقًا على دوائر إلكترونية متخصصة. يستخدم في التعليم والبحث والاختبار المشروع. وجود SDR لا يعني أنه جهاز اعتراض؛ وقد لا تظهر المراقبة الراديوية السلبية في قياسات الهاتف إطلاقًا."
                   : "SDR performs radio functions in software rather than only dedicated circuitry. It has legitimate research, education and testing uses. An SDR is not inherently an interception device, and passive radio reception may leave no observable signature on the phone.");
        article(page, ar ? "المراقبة السلبية والاعتراض النشط" : "Passive monitoring versus active interception",
                ar ? "تستمع المراقبة السلبية إلى الإشارات دون أن تبث محليًا. أما انتحال محطة قاعدة فيتضمن سلوكًا نشطًا يختلف حسب النظام. لا يستطيع Tower Guard تحديد أجهزة SDR القريبة أو الكشف عن برامج التجسس المثبتة على الهاتف."
                   : "Passive monitoring receives radio signals without transmitting locally. A rogue base station uses active radio behavior. Tower Guard cannot locate nearby SDR devices or detect on-device spyware.");
        article(page, ar ? "كيف يحسب التطبيق مؤشر الشذوذ؟" : "How the anomaly indicator works",
                ar ? "يربط التطبيق بين تغير هوية الخلية، وتبدل جيل الشبكة، وظهور 2G المتكرر، وتغير قوة الإشارة والخلايا المجاورة، ويقارن بعض النتائج بسجل الفحص وخطوط الأساس المحلية. النقاط مؤشرات سياقية، وليست احتمال اعتراض محسوبًا علميًا. أخضر منخفض، كهرماني متوسط، وأحمر مرتفع."
                   : "The app correlates serving-cell changes, radio generation transitions, repeated 2G exposure, signal changes, neighboring cells and local baselines. Its score is a contextual heuristic, not a scientifically calibrated probability of interception. Green means low, amber medium, red high.");
        article(page, ar ? "لماذا قد يظهر إنذار طبيعي؟" : "Why ordinary conditions can trigger alerts",
                ar ? "ضعف التغطية داخل المباني، الحركة بين مناطق التغطية، ازدحام الشبكة، أعمال صيانة الأبراج، والتبديل الطبيعي بين الأجيال قد تؤدي إلى أنماط شاذة. لا تعتمد على قراءة منفردة للحكم على أمان الاتصال."
                   : "Indoor attenuation, moving between cells, congestion, network maintenance and normal radio switching can produce unusual patterns. Never treat one alert as definitive evidence.");
        article(page, ar ? "إرشادات الحماية" : "Practical protection",
                ar ? "حدّث Android باستمرار. عطّل 2G إن كان هاتفك يتيح ذلك دون الإضرار بحاجتك إلى الاتصال. استخدم تطبيقات ذات تشفير شامل بين الطرفين، وراجع التغيرات المتكررة في بيئة مألوفة، واستشر مشغل الاتصالات عند استمرار مشكلات الشبكة."
                   : "Keep Android updated. Disable 2G if supported and appropriate for your connectivity needs. Use end-to-end encrypted communications, review repeated changes in familiar locations, and contact your carrier about persistent network issues.");
        article(page, ar ? "المراجع التقنية" : "Technical references",
                ar ? "3GPP: مواصفات شبكات الراديو الخلوية (3gpp.org). GSMA: مواد أمن شبكات الهاتف المحمول (gsma.com). Android Developers: TelephonyManager وCellInfo وCellSignalStrength (developer.android.com). راجع النسخ الرسمية للحصول على تعريفات الحقول والقيود بحسب واجهة النظام."
                   : "3GPP cellular radio specifications (3gpp.org); GSMA mobile network security material (gsma.com); Android Developers references for TelephonyManager, CellInfo and CellSignalStrength (developer.android.com). Consult the official specifications for measurement semantics and API availability.");
        setContentView(UiKit.withBottomNav(this, scroll, 4));
    }

    private void article(LinearLayout parent, String heading, String body) {
        TextView title = UiKit.eyebrow(this, heading);
        title.setTextSize(16f);
        title.setPadding(0, UiKit.dp(this, 20), 0, UiKit.dp(this, 4));
        parent.addView(title);
        TextView explanation = UiKit.body(this, body);
        explanation.setTextColor(Color.rgb(190, 207, 224));
        parent.addView(explanation);
    }
}
