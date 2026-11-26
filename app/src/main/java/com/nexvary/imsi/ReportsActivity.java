package com.nexvary.imsi;

import android.app.*;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.io.File;
import java.util.*;

/** Reports screen: PDF export and local scan log. TG06. */
public class ReportsActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(2, 7, 13));
        LinearLayout page = UiKit.page(this);
        UiKit.applySystemBars(page);
        scroll.addView(page);
        setContentView(UiKit.withBottomNav(this, scroll, 3));
        buildUi(page);
    }

    private void buildUi(LinearLayout page) {
        boolean ar = LanguageUtil.current(this).equals("ar");
        page.addView(UiKit.eyebrow(this, ar ? "التقارير" : "REPORTS"));
        page.addView(UiKit.title(this, ar ? "التقارير المحلية" : "Local Reports"));
        page.addView(UiKit.gap(this, 12));

        Button pdfBtn = UiKit.button(this, ar ? "تصدير تقرير PDF" : "EXPORT PDF REPORT");
        pdfBtn.setTextColor(Color.WHITE);
        pdfBtn.setBackgroundResource(R.drawable.button_primary);
        pdfBtn.setOnClickListener(v -> exportPdf(ar));
        page.addView(pdfBtn);
        page.addView(UiKit.gap(this, 10));

        page.addView(UiKit.eyebrow(this, ar ? "السجل المحلي" : "LOCAL LOG"));
        page.addView(UiKit.gap(this, 6));

        SecureStore store = new SecureStore(this);
        List<ScanRecord> history = loadHistory(store);
        if (history.isEmpty()) {
            page.addView(UiKit.body(this, ar ? "لا يوجد سجل فحص محلي." : "No local scan history."));
        } else {
            for (int i = history.size() - 1; i >= Math.max(0, history.size() - 20); i--) {
                page.addView(buildScanRow(history.get(i), ar));
            }
        }
    }

    private View buildScanRow(ScanRecord r, boolean ar) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setBackgroundResource(R.drawable.panel);
        int p = UiKit.dp(this, 12);
        row.setPadding(p, UiKit.dp(this, 8), p, UiKit.dp(this, 8));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = UiKit.dp(this, 6);
        row.setLayoutParams(lp);

        String time = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", r.at).toString();
        TextView tv = new TextView(this);
        tv.setText(time + " · " + r.radio + " · " + r.cell + " · " + (ar ? "خطر" : "Risk") + ": " + r.risk);
        tv.setTextColor(r.risk >= 65 ? Color.rgb(255, 42, 66) : r.risk >= 30 ? Color.rgb(255, 181, 63) : Color.rgb(41, 241, 139));
        tv.setTextSize(13f);
        row.addView(tv);
        if (r.reasons != null && !r.reasons.isEmpty()) {
            TextView rv = new TextView(this);
            rv.setText(r.reasons);
            rv.setTextColor(Color.rgb(90, 120, 148));
            rv.setTextSize(11f);
            row.addView(rv);
        }
        return row;
    }

    @SuppressWarnings("unchecked")
    private List<ScanRecord> loadHistory(SecureStore store) {
        try {
            String raw = store.get("history", "[]");
            if (raw == null || raw.isEmpty()) return new ArrayList<>();
            org.json.JSONArray arr = new org.json.JSONArray(raw);
            List<ScanRecord> out = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) out.add(ScanRecord.from(arr.getJSONObject(i)));
            return out;
        } catch (Exception e) { return new ArrayList<>(); }
    }

    private void exportPdf(boolean ar) {
        try {
            SecureStore store = new SecureStore(this);
            List<ScanRecord> h = loadHistory(store);
            List<String> baselines = new ArrayList<>();
            try {
                String raw = store.get("baselines", "[]");
                if (raw != null && !raw.isEmpty()) {
                    org.json.JSONArray arr = new org.json.JSONArray(raw);
                    for (int i = 0; i < arr.length(); i++) baselines.add(arr.getString(i));
                }
            } catch (Exception ignored) {}
            File f = ReportExporter.create(this, h, baselines);
            startActivity(ReportExporter.share(this, f));
        } catch (Exception e) {
            Toast.makeText(this, ar ? "تعذّر إنشاء التقرير" : "Could not create report", Toast.LENGTH_LONG).show();
        }
    }
}
