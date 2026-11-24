package com.nexvary.imsi;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

public final class UiKit {
    private UiKit(){}
    public static int dp(Context c,int n){ return Math.round(n*c.getResources().getDisplayMetrics().density); }
    public static LinearLayout page(Context c){ LinearLayout x=new LinearLayout(c); x.setOrientation(LinearLayout.VERTICAL); x.setPadding(dp(c,20),dp(c,28),dp(c,20),dp(c,36)); x.setBackgroundColor(Color.rgb(3,8,17)); return x; }
    public static void applySystemBars(View root){
        final int left=root.getPaddingLeft(), top=root.getPaddingTop(), right=root.getPaddingRight(), bottom=root.getPaddingBottom();
        root.setOnApplyWindowInsetsListener((v,insets)->{
            int l, t, r, b;
            if (android.os.Build.VERSION.SDK_INT >= 30) {
                android.graphics.Insets bars = insets.getInsets(android.view.WindowInsets.Type.systemBars());
                l = bars.left; t = bars.top; r = bars.right; b = bars.bottom;
            } else {
                l = insets.getSystemWindowInsetLeft();
                t = insets.getSystemWindowInsetTop();
                r = insets.getSystemWindowInsetRight();
                b = insets.getSystemWindowInsetBottom();
            }
            v.setPadding(left+l, top+t, right+r, bottom+b);
            return insets;
        });
        root.requestApplyInsets();
    }
    public static TextView title(Context c,String s){ TextView v=text(c,s,28,Color.WHITE); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); return v; }
    public static TextView eyebrow(Context c,String s){ TextView v=text(c,s,12,Color.rgb(63,186,255)); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setLetterSpacing(.08f); return v; }
    public static TextView body(Context c,String s){ TextView v=text(c,s,14,Color.rgb(168,183,201)); v.setLineSpacing(0,1.18f); return v; }
    public static TextView text(Context c,String s,int sp,int color){ TextView v=new TextView(c); v.setText(s); v.setTextSize(sp); v.setTextColor(color); v.setPadding(0,dp(c,6),0,dp(c,6)); return v; }
    public static Button button(Context c,String s){ Button b=new Button(c); b.setText(s); b.setTextColor(Color.WHITE); b.setBackgroundResource(R.drawable.button_secondary); b.setMinHeight(dp(c,54)); b.setPadding(dp(c,12),dp(c,10),dp(c,12),dp(c,10)); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,LinearLayout.LayoutParams.WRAP_CONTENT); lp.topMargin=dp(c,10); b.setLayoutParams(lp); return b; }
    public static View gap(Context c,int h){ Space s=new Space(c); s.setLayoutParams(new LinearLayout.LayoutParams(1,dp(c,h))); return s; }
    public static View withBottomNav(Activity a, View content, int selected){
        LinearLayout root=new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(3,8,17));
        root.setClipToPadding(false);
        applySystemBars(root);

        FrameLayout body=new FrameLayout(a);
        body.addView(content,new FrameLayout.LayoutParams(-1,-1));
        root.addView(body,new LinearLayout.LayoutParams(-1,0,1f));

        LinearLayout nav=new LinearLayout(a);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(a,4),dp(a,5),dp(a,4),dp(a,5));
        nav.setBackgroundColor(Color.rgb(4,20,30));

        addNavItem(a,nav,R.drawable.ic_nav_home, LanguageUtil.current(a).equals("ar")?"الرئيسية":"Home",0,selected,MainActivity.class);
        addNavItem(a,nav,R.drawable.ic_nav_history, LanguageUtil.current(a).equals("ar")?"السجل":"History",1,selected,HistoryActivity.class);
        addNavItem(a,nav,R.drawable.ic_nav_baseline, LanguageUtil.current(a).equals("ar")?"الخطوط":"Baselines",2,selected,BaselinesActivity.class);
        addNavItem(a,nav,R.drawable.ic_nav_privacy, LanguageUtil.current(a).equals("ar")?"الخصوصية":"Privacy",3,selected,PrivacyActivity.class);
        addNavItem(a,nav,R.drawable.ic_nav_info, LanguageUtil.current(a).equals("ar")?"حول":"About",4,selected,AboutActivity.class);

        root.addView(nav,new LinearLayout.LayoutParams(-1,dp(a,74)));
        return root;
    }

    private static void addNavItem(Activity a, LinearLayout nav, int iconRes, String label, int index, int selected, Class<?> target){
        LinearLayout item=new LinearLayout(a);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setPadding(dp(a,2),dp(a,3),dp(a,2),dp(a,2));
        int color=index==selected?Color.rgb(41,241,139):Color.rgb(145,165,183);

        ImageView icon=new ImageView(a);
        icon.setImageResource(iconRes);
        icon.setColorFilter(color);
        item.addView(icon,new LinearLayout.LayoutParams(dp(a,28),dp(a,28)));

        TextView text=text(a,label,10,color);
        text.setGravity(Gravity.CENTER);
        text.setPadding(0,dp(a,3),0,0);
        item.addView(text,new LinearLayout.LayoutParams(-1,dp(a,24)));

        item.setOnClickListener(v->{
            if(index==selected) return;
            Intent i=new Intent(a,target);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            a.startActivity(i);
        });
        nav.addView(item,new LinearLayout.LayoutParams(0,-1,1f));
    }

}
