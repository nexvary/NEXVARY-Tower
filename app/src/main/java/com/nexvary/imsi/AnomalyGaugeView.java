package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/** Circular 0–100 anomaly score gauge (TG04). */
public class AnomalyGaugeView extends View {
    private final Paint arc = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint track = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textP = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int score = 0;
    private String label = "";

    public AnomalyGaugeView(Context c, AttributeSet a) {
        super(c, a);
        arc.setStyle(Paint.Style.STROKE);
        arc.setStrokeCap(Paint.Cap.ROUND);
        track.setStyle(Paint.Style.STROKE);
        track.setStrokeCap(Paint.Cap.ROUND);
        track.setColor(Color.rgb(14, 40, 60));
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setTypeface(Typeface.DEFAULT_BOLD);
        textP.setAntiAlias(true);
    }

    public void setScore(int s) {
        score = Math.max(0, Math.min(100, s));
        label = score >= 65 ? (isAr() ? "مستوى مرتفع" : "High") :
                score >= 30 ? (isAr() ? "مستوى متوسط" : "Medium") :
                              (isAr() ? "مستوى منخفض" : "Low");
        invalidate();
    }

    private boolean isAr() {
        return getContext().getResources().getConfiguration().locale.getLanguage().equals("ar");
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w = getWidth(), h = getHeight();
        float sw = w * 0.09f;
        arc.setStrokeWidth(sw);
        track.setStrokeWidth(sw);
        float margin = sw / 2f + 4f;
        RectF oval = new RectF(margin, margin, w - margin, h - margin);
        // Track (270° arc from 135° to 45°)
        c.drawArc(oval, 135f, 270f, false, track);
        // Score arc
        int color = score >= 65 ? Color.rgb(255, 42, 66) :
                    score >= 30 ? Color.rgb(255, 181, 63) :
                                  Color.rgb(41, 241, 139);
        arc.setColor(color);
        float sweep = 270f * score / 100f;
        c.drawArc(oval, 135f, sweep, false, arc);

        float cx = w / 2f, cy = h / 2f;
        // Score number
        textP.setTextSize(h * 0.28f);
        textP.setColor(color);
        c.drawText(score + "", cx, cy + h * 0.10f, textP);
        // "/ 100"
        textP.setTextSize(h * 0.11f);
        textP.setColor(Color.rgb(90, 120, 148));
        c.drawText("/ 100", cx, cy + h * 0.26f, textP);
        // Label
        textP.setTextSize(h * 0.11f);
        textP.setColor(color);
        RectF labelBg = new RectF(cx - w * 0.32f, cy + h * 0.30f, cx + w * 0.32f, cy + h * 0.47f);
        Paint bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        bg.setColor(color & 0x33FFFFFF);
        c.drawRoundRect(labelBg, 20f, 20f, bg);
        c.drawText(label, cx, cy + h * 0.43f, textP);
        // Disclaimer
        textP.setTextSize(h * 0.085f);
        textP.setColor(Color.rgb(90, 120, 148));
        String dis = isAr() ? "مؤشر احتمالي وليس إثباتًا" : "Probabilistic indicator only";
        c.drawText(dis, cx, h - 6f, textP);
    }
}
