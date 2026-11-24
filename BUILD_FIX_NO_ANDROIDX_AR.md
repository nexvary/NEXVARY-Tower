# إصلاح البناء — نسخة بدون AndroidX

تم حذف اعتماد `androidx.core:core:1.15.0` بالكامل واستبدال وظيفتيه بواجهات Android الأصلية المتاحة لأن `minSdk=29`:

- Safe Area / system bars: باستخدام `WindowInsets` الأصلي.
- مشاركة تقارير PDF: باستخدام `ReportFileProvider` محلي مخصص للقراءة فقط.

لم يتم تغيير منطق تحليل الشبكة أو حساب المخاطر أو الصلاحيات.

## البناء
افتح المشروع في Android Studio، انتظر Gradle Sync، ثم Generate APKs. لا تضغط AGP Upgrade Assistant.
