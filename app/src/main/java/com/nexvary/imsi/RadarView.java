package com.nexvary.imsi;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/** Displays only the calculated score, not inferred tower positions. */
public class RadarView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int risk = -1;
    public RadarView(Context c, AttributeSet a) { super(c,a); }
    public void setRisk(int value) { risk = value < 0 ? -1 : Math.min(100,value); invalidate(); }
    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        float cx=getWidth()/2f, cy=getHeight()/2f, r=Math.min(getWidth(),getHeight())*.38f;
        RectF arc=new RectF(cx-r,cy-r,cx+r,cy+r);
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*.10f); p.setStrokeCap(Paint.Cap.ROUND);
        p.setColor(Color.rgb(49,55,65)); c.drawArc(arc,135,270,false,p);
        if(risk>=0){p.setColor(risk>=70?Color.rgb(255,36,72):risk>=40?Color.rgb(255,181,63):Color.rgb(85,240,138)); c.drawArc(arc,135,270*risk/100f,false,p);}
        p.setStyle(Paint.Style.FILL); p.setTextAlign(Paint.Align.CENTER); p.setTypeface(Typeface.DEFAULT_BOLD); p.setTextSize(r*.50f); p.setColor(Color.WHITE);
        c.drawText(risk<0?"—":Integer.toString(risk),cx,cy+r*.12f,p);
        p.setTextSize(r*.18f); p.setColor(Color.rgb(170,190,210));
        c.drawText(LanguageUtil.current(getContext()).equals("ar")?"درجة الشذوذ":"ANOMALY SCORE",cx,cy+r*.46f,p);
    }
}
