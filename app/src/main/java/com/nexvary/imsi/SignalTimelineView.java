package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import java.util.*;

/**
 * Privacy-preserving local signal chart. It visualizes only samples already held
 * in the encrypted local scan history. No geographic coordinates are used.
 */
public class SignalTimelineView extends View {
    private final Paint grid = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint signal = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint quality = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<ScanRecord> records = Collections.emptyList();

    public SignalTimelineView(Context c, AttributeSet a) {
        super(c, a);
        grid.setColor(Color.rgb(22, 50, 73));
        grid.setStrokeWidth(1f);
        signal.setColor(Color.rgb(21, 151, 255));
        signal.setStyle(Paint.Style.STROKE);
        signal.setStrokeWidth(4f);
        signal.setStrokeCap(Paint.Cap.ROUND);
        signal.setStrokeJoin(Paint.Join.ROUND);
        quality.setColor(Color.rgb(255, 36, 72));
        quality.setStyle(Paint.Style.STROKE);
        quality.setStrokeWidth(3f);
        quality.setStrokeCap(Paint.Cap.ROUND);
        text.setColor(Color.rgb(142, 164, 184));
        text.setTextSize(24f);
    }

    public void setRecords(List<ScanRecord> values) {
        if (values == null) values = Collections.emptyList();
        int from = Math.max(0, values.size() - 40);
        records = new ArrayList<>(values.subList(from, values.size()));
        invalidate();
    }

    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w = getWidth(), h = getHeight();
        float left = 42f, right = w - 18f, top = 32f, bottom = h - 38f;
        for (int i = 0; i <= 4; i++) {
            float y = top + (bottom - top) * i / 4f;
            c.drawLine(left, y, right, y, grid);
        }
        c.drawText("dBm", left, 24f, text);
        c.drawText("quality", Math.max(left + 80f, right - 110f), 24f, text);
        if (records.size() < 2) {
            c.drawText("Run live monitoring to build a timeline", left, h / 2f, text);
            return;
        }
        Path pSignal = new Path();
        Path pQuality = new Path();
        boolean hasS = false, hasQ = false;
        for (int i = 0; i < records.size(); i++) {
            ScanRecord r = records.get(i);
            float x = left + (right - left) * i / Math.max(1, records.size() - 1);
            if (r.dbm != -999) {
                float y = map(r.dbm, -125f, -35f, bottom, top);
                if (!hasS) { pSignal.moveTo(x, y); hasS = true; } else pSignal.lineTo(x, y);
            }
            if (r.quality != -999) {
                float y = map(r.quality, -30f, 0f, bottom, top);
                if (!hasQ) { pQuality.moveTo(x, y); hasQ = true; } else pQuality.lineTo(x, y);
            }
        }
        if (hasS) c.drawPath(pSignal, signal);
        if (hasQ) c.drawPath(pQuality, quality);
    }

    private float map(float value, float inMin, float inMax, float outMin, float outMax) {
        float v = Math.max(inMin, Math.min(inMax, value));
        return outMin + (v - inMin) * (outMax - outMin) / (inMax - inMin);
    }
}
