package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.graphics.pdf.PdfDocument;
import android.content.Intent;
import android.net.Uri;
import java.io.*;
import java.text.DateFormat;
import java.util.*;

public final class ReportExporter {
    public static File create(Context c, List<ScanRecord> history, List<String> baselines) throws Exception {
        File dir = new File(c.getCacheDir(), "reports");
        if (!dir.exists() && !dir.mkdirs()) throw new IOException("Cannot create report directory");
        File f = new File(dir, "NEXVARY-IMSI-Report-" + System.currentTimeMillis() + ".pdf");
        PdfDocument doc = new PdfDocument();
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        int pageNo = 1, idx = 0;
        while (idx < Math.max(1, history.size())) {
            PdfDocument.Page page = doc.startPage(new PdfDocument.PageInfo.Builder(1240, 1754, pageNo++).create());
            Canvas cv = page.getCanvas();
            cv.drawColor(Color.rgb(5, 11, 18));
            p.setColor(Color.WHITE); p.setTextSize(52); p.setFakeBoldText(true); cv.drawText("NEXVARY IMSI DETECTOR", 70, 95, p);
            p.setFakeBoldText(false); p.setTextSize(22); p.setColor(Color.rgb(17, 177, 255)); cv.drawText("LOCAL CELLULAR ANOMALY REPORT · v0.3", 70, 135, p);
            p.setColor(Color.rgb(180, 198, 214)); p.setTextSize(18); cv.drawText("Generated: " + DateFormat.getDateTimeInstance().format(new Date()), 70, 175, p);
            cv.drawText("Baselines stored: " + baselines.size() + "   ·   History entries: " + history.size(), 70, 205, p);
            p.setColor(Color.rgb(255, 90, 104)); p.setTextSize(17); cv.drawText("Important: anomaly indicators are not proof of an IMSI catcher.", 70, 240, p);
            int y = 300, count = 0;
            while (idx < history.size() && count < 13) {
                ScanRecord r = history.get(idx++);
                p.setColor(r.risk >= 70 ? Color.rgb(255, 42, 66) : Color.rgb(220, 232, 242)); p.setTextSize(20); p.setFakeBoldText(true);
                cv.drawText("Risk " + r.risk + "/100  ·  " + r.radio + "  ·  " + r.operator, 70, y, p);
                p.setFakeBoldText(false); p.setColor(Color.rgb(135, 158, 178)); p.setTextSize(16);
                cv.drawText(new Date(r.at).toString(), 70, y + 28, p);
                cv.drawText(trim(r.cell, 92), 70, y + 54, p);
                String metrics = "Neighbors: " + r.neighbors + "   Signal: " + (r.dbm == -999 ? "—" : r.dbm + " dBm") + "   Quality: " + (r.quality == -999 ? "—" : r.quality + " dB");
                cv.drawText(metrics, 70, y + 80, p);
                y += 108; count++;
            }
            if (history.isEmpty()) { p.setColor(Color.rgb(180, 198, 214)); p.setTextSize(22); cv.drawText("No scan history has been recorded yet.", 70, 330, p); idx = 1; }
            p.setColor(Color.rgb(90, 115, 136)); p.setTextSize(15); cv.drawText("NEXVARY · Defensive diagnostic report · Data generated and stored locally on this device", 70, 1690, p);
            doc.finishPage(page);
        }
        try (FileOutputStream os = new FileOutputStream(f)) { doc.writeTo(os); }
        doc.close();
        return f;
    }
    private static String trim(String s, int max) { return s == null ? "—" : (s.length() <= max ? s : s.substring(0, max - 1) + "…"); }
    public static Intent share(Context c, File f) {
        Uri u = ReportFileProvider.uriFor(c, f);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("application/pdf");
        i.putExtra(Intent.EXTRA_STREAM, u);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return Intent.createChooser(i, "Share NEXVARY report");
    }
}
