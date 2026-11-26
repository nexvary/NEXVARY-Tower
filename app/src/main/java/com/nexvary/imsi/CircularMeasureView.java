package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/** Small circular dial for a single measurement (RSRP/RSRQ/SINR/RSSI/network). TG02. */
public class CircularMeasureView extends View {
    private final Paint arc = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint track = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textP = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String value = "—", unit = "", label = "";
    private int color = Color.rgb(41, 241, 139);
    private float fraction = 0f; // 0-1 for arc sweep

    public CircularMeasureView(Context c, AttributeSet a) {
        super(c, a);
        arc.setStyle(Paint.Style.STROKE);
        arc.setStrokeCap(Paint.Cap.ROUND);
        track.setStyle(Paint.Style.STROKE);
        track.setStrokeCap(Paint.Cap.ROUND);
        track.setColor(Color.rgb(14, 40, 60));
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setAntiAlias(true);
    }

    /** Set a dBm-style value. min/max define the arc range. */
    public void setMeasure(String val, String unitStr, String lbl, int colorVal, float frac) {
        value = val; unit = unitStr; label = lbl; color = colorVal;
        fraction = Math.max(0f, Math.min(1f, frac));
        arc.setColor(color);
        invalidate();
    }

    public void setNetworkType(String gen, int colorVal) {
        value = gen; unit = ""; label = "";
        color = colorVal; fraction = 1f;
        arc.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w = getWidth(), h = getHeight();
        float sw = w * 0.10f;
        arc.setStrokeWidth(sw); track.setStrokeWidth(sw);
        float margin = sw / 2f + 2f;
        RectF oval = new RectF(margin, margin, w - margin, h - margin * 1.4f);
        c.drawArc(oval, 150f, 240f, false, track);
        c.drawArc(oval, 150f, 240f * fraction, false, arc);

        float cx = w / 2f, cy = (oval.top + oval.bottom) / 2f + 2f;
        textP.setTypeface(Typeface.DEFAULT_BOLD);
        textP.setTextSize(w * 0.22f);
        textP.setColor(Color.WHITE);
        c.drawText(value, cx, cy + w * 0.07f, textP);
        if (!unit.isEmpty()) {
            textP.setTextSize(w * 0.14f);
            textP.setColor(Color.rgb(130, 160, 185));
            textP.setTypeface(Typeface.DEFAULT);
            c.drawText(unit, cx, cy + w * 0.26f, textP);
        }
        // Label below arc
        if (!label.isEmpty()) {
            textP.setTextSize(w * 0.14f);
            textP.setColor(Color.rgb(90, 120, 148));
            textP.setTypeface(Typeface.DEFAULT);
            c.drawText(label, cx, h - 2f, textP);
        }
    }
}
