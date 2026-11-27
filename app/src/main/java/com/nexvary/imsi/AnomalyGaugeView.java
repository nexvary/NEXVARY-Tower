package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * Circular 0–100 anomaly score gauge (TG04).
 * Draws score number, risk classification and disclaimer with no overlap
 * by using Paint.FontMetrics to stack elements top-to-bottom.
 */
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
        textP.setAntiAlias(true);
    }

    public void setScore(int s) {
        score = Math.max(0, Math.min(100, s));
        label = score >= 65 ? (isAr() ? "خطر مرتفع"   : "High Risk")   :
                score >= 30 ? (isAr() ? "خطر متوسط"   : "Medium Risk") :
                              (isAr() ? "خطر منخفض"   : "Low Risk");
        invalidate();
    }

    private boolean isAr() {
        return getContext().getResources().getConfiguration().locale.getLanguage().equals("ar");
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;

        // --- Color ---
        int color = score >= 65 ? Color.rgb(255, 42, 66)  :
                    score >= 30 ? Color.rgb(255, 181, 63) :
                                  Color.rgb(41, 241, 139);

        // --- Arc geometry ---
        // Reserve bottom strip for disclaimer; arc fits in remaining height
        float disReserve = Math.min(h * 0.14f, 16f);
        float arcAvail   = h - disReserve;
        float arcSize    = Math.min(w * 0.92f, arcAvail * 0.96f);
        float sw         = arcSize * 0.095f;
        arc.setStrokeWidth(sw);
        track.setStrokeWidth(sw);

        float arcLeft = (w - arcSize) / 2f;
        float arcTop  = sw / 2f + 1f;
        RectF oval = new RectF(
                arcLeft  + sw / 2f,
                arcTop,
                arcLeft  + arcSize - sw / 2f,
                arcTop   + arcSize - sw);

        c.drawArc(oval, 135f, 270f, false, track);
        arc.setColor(color);
        c.drawArc(oval, 135f, 270f * score / 100f, false, arc);

        float cx     = w / 2f;
        float arcCy  = oval.centerY();
        float innerR = oval.width()  / 2f;

        // --- Score number ---
        float scoreSize = innerR * 0.70f;
        textP.setTypeface(Typeface.DEFAULT_BOLD);
        textP.setTextSize(scoreSize);
        textP.setColor(color);
        Paint.FontMetrics fm = textP.getFontMetrics();
        // Center score just above arc center so sub-text fits below
        float scoreBaseline = arcCy - innerR * 0.10f - (fm.ascent + fm.descent) / 2f;
        c.drawText(score + "", cx, scoreBaseline, textP);
        float scoreBottom = scoreBaseline + fm.descent;

        // --- "/100" sub-label ---
        float subSize = scoreSize * 0.36f;
        textP.setTypeface(Typeface.DEFAULT);
        textP.setTextSize(subSize);
        textP.setColor(Color.rgb(90, 120, 148));
        fm = textP.getFontMetrics();
        float subBaseline = scoreBottom + 2f - fm.ascent;
        float subBottom   = subBaseline + fm.descent;
        c.drawText("/ 100", cx, subBaseline, textP);

        // --- Classification label ---
        float labelSize = Math.max(subSize * 0.95f, 8f);
        textP.setTextSize(labelSize);
        textP.setColor(color);
        fm = textP.getFontMetrics();
        float labelBaseline = subBottom + 4f - fm.ascent;
        float labelBottom   = labelBaseline + fm.descent;

        // Semi-transparent pill background if room allows
        float disTop = (h - disReserve) + 2f;
        if (labelBottom + 3f < disTop) {
            float textW = textP.measureText(label);
            Paint bg = new Paint(Paint.ANTI_ALIAS_FLAG);
            bg.setColor(color & 0x2AFFFFFF);
            float pad = 5f;
            c.drawRoundRect(
                    cx - textW / 2f - pad, labelBaseline + fm.ascent - 1f,
                    cx + textW / 2f + pad, labelBottom + 2f, 8f, 8f, bg);
        }
        c.drawText(label, cx, labelBaseline, textP);

        // --- Disclaimer (only if it won't overlap label) ---
        float disSize = Math.max(7f, Math.min(w * 0.075f, 11f));
        textP.setTypeface(Typeface.DEFAULT);
        textP.setTextSize(disSize);
        textP.setColor(Color.rgb(80, 108, 130));
        fm = textP.getFontMetrics();
        float disBl = h - 2f;
        if (disBl + fm.ascent > labelBottom + 2f) {
            String dis = isAr() ? "مؤشر احتمالي" : "Probabilistic";
            c.drawText(dis, cx, disBl, textP);
        }
    }
}
