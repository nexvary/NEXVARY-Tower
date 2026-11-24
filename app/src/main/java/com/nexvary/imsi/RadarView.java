package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class RadarView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float sweep = 0f;
    private int risk = 0;
    public RadarView(Context c, AttributeSet a) { super(c,a); p.setStrokeWidth(2f); }
    public void setRisk(int value) { risk = Math.max(0, Math.min(100,value)); invalidate(); }
    @Override protected void onDraw(Canvas c) {
        super.onDraw(c); float w=getWidth(), h=getHeight(), cx=w/2f, cy=h/2f, r=Math.min(w,h)*.42f;
        p.setStyle(Paint.Style.FILL); p.setColor(Color.rgb(3,10,18)); c.drawCircle(cx,cy,r,p);
        p.setStyle(Paint.Style.STROKE); p.setColor(Color.rgb(24,77,112));
        for(int i=1;i<=4;i++) c.drawCircle(cx,cy,r*i/4f,p);
        for(int i=0;i<8;i++){ double a=i*Math.PI/4; c.drawLine(cx,cy,cx+(float)Math.cos(a)*r,cy+(float)Math.sin(a)*r,p); }
        p.setColor(Color.rgb(255,36,72)); p.setStrokeWidth(4f); double a=Math.toRadians(sweep); c.drawLine(cx,cy,cx+(float)Math.cos(a)*r,cy+(float)Math.sin(a)*r,p);
        p.setStyle(Paint.Style.FILL); p.setColor(Color.argb(110,255,36,72)); Path wedge=new Path(); wedge.moveTo(cx,cy); for(int d=0;d<=28;d+=2){ double wa=Math.toRadians(sweep-d); wedge.lineTo(cx+(float)Math.cos(wa)*r,cy+(float)Math.sin(wa)*r);} wedge.close(); c.drawPath(wedge,p);
        p.setColor(risk>65?Color.rgb(255,36,72):risk>30?Color.rgb(255,181,63):Color.rgb(85,240,138));
        for(int i=0;i<5;i++){ double t=(i*1.73+0.6); float rr=r*(.25f+((i*23+risk)%65)/100f); c.drawCircle(cx+(float)Math.cos(t)*rr,cy+(float)Math.sin(t)*rr,5f+(i%2)*2,p); }
        p.setColor(Color.WHITE); p.setTextAlign(Paint.Align.CENTER); p.setTypeface(Typeface.DEFAULT_BOLD); p.setTextSize(38f); c.drawText(risk+"",cx,cy+12,p);
        p.setTextSize(13f); p.setColor(Color.rgb(114,200,255)); c.drawText("RISK SCORE",cx,cy+36,p);
        sweep=(sweep+1.7f)%360f; postInvalidateDelayed(30);
    }
}
