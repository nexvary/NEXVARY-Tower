# NEXVARY IMSI Detector v0.6 RC1 — Verification Notes

- تم الحفاظ على واجهة v0.5 والقوائم السفلية دون إعادة تصميم.
- تم اعتماد أيقونة NEXVARY IMSI Detector الجديدة كـ launcher icon عادية وAdaptive Icon.
- تم رفع versionCode إلى 6 وversionName إلى 0.6.0-rc1.
- تم توسيع Risk Score بترابط مؤشرات قصيرة المدى: تكرار تغير الخلية، تقلب جيل الشبكة، تكرار 2G، والتغير الحاد المتزامن في الخلية والإشارة.
- أسباب التقييم تحفظ مع كل فحص في التخزين المحلي المشفر وتظهر داخل تفاصيل السجل.
- لا يوجد INTERNET permission ولا AndroidX dependency.
- فحص XML: ناجح لجميع ملفات الموارد.
- فحص بنية Java: لا توجد أقواس غير متوازنة في الملفات المعدلة.
- لم يتم تنفيذ Android Gradle build داخل بيئة التسليم لعدم توفر Android SDK الكامل هنا؛ يلزم Generate APKs في Android Studio على الجهاز.

مهم: المؤشرات استرشادية ولا تثبت بمفردها وجود IMSI Catcher.
