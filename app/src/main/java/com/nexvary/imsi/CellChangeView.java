package com.nexvary.imsi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import java.util.*;

public class CellChangeView extends View {
    private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); private List<ScanRecord> records=Collections.emptyList();
    public CellChangeView(Context c,AttributeSet a){super(c,a);}
    public void setRecords(List<ScanRecord> r){records=new ArrayList<>(r); invalidate();}
    @Override protected void onDraw(Canvas c){super.onDraw(c); float w=getWidth(),h=getHeight();
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(2);p.setColor(Color.rgb(24,91,132));c.drawRoundRect(2,2,w-2,h-2,18,18,p);
        if(records.size()<2){p.setStyle(Paint.Style.FILL);p.setTextSize(26);p.setColor(Color.rgb(111,135,154));c.drawText("Scan history builds the cell-change map",22,h/2,p);return;}
        int n=Math.min(records.size(),12); float step=(w-44)/(n-1f), yMid=h/2f; String prev="";
        for(int i=0;i<n;i++){ ScanRecord r=records.get(records.size()-n+i); float x=22+i*step; boolean changed=i>0&&!r.cell.equals(prev);
            p.setStyle(Paint.Style.FILL); p.setColor(changed?Color.rgb(255,42,66):Color.rgb(17,177,255)); c.drawCircle(x,yMid+(float)Math.sin(i*.8)*30,changed?9:6,p);
            if(i>0){p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);p.setColor(changed?Color.rgb(255,42,66):Color.rgb(17,177,255));float px=22+(i-1)*step;c.drawLine(px,yMid+(float)Math.sin((i-1)*.8)*30,x,yMid+(float)Math.sin(i*.8)*30,p);} prev=r.cell;
        }
    }
}
