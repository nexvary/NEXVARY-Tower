package com.nexvary.imsi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

public final class UiKit {
    private UiKit() {}

    public static int dp(Context c, int n) {
        return Math.round(n * c.getResources().getDisplayMetrics().density);
    }

    public static LinearLayout page(Context c) {
        LinearLayout x = new LinearLayout(c);
        x.setOrientation(LinearLayout.VERTICAL);
        x.setPadding(dp(c, 16), dp(c, 16), dp(c, 16), dp(c, 32));
        x.setBackgroundColor(Color.BLACK);
        return x;
    }

    public static void applySystemBars(View root) {
        final int left = root.getPaddingLeft(), top = root.getPaddingTop(),
                right = root.getPaddingRight(), bottom = root.getPaddingBottom();
        root.setOnApplyWindowInsetsListener((v, insets) -> {
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
            v.setPadding(left + l, top + t, right + r, bottom + b);
            return insets;
        });
        root.requestApplyInsets();
    }

    public static TextView title(Context c, String s) {
        TextView v = text(c, s, 22, Color.WHITE);
        v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return v;
    }

    public static TextView eyebrow(Context c, String s) {
        TextView v = text(c, s, 11, Color.rgb(192, 192, 192));
        v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        v.setLetterSpacing(.08f);
        return v;
    }

    public static TextView body(Context c, String s) {
        TextView v = text(c, s, 14, Color.rgb(168, 183, 201));
        v.setLineSpacing(0, 1.18f);
        return v;
    }

    public static TextView text(Context c, String s, int sp, int color) {
        TextView v = new TextView(c);
        v.setText(s);
        v.setTextSize(sp);
        v.setTextColor(color);
        v.setPadding(0, dp(c, 4), 0, dp(c, 4));
        return v;
    }

    public static Button button(Context c, String s) {
        Button b = new Button(c);
        b.setText(s);
        b.setTextColor(Color.rgb(192, 192, 192));
        b.setBackgroundResource(R.drawable.button_secondary);
        b.setMinHeight(dp(c, 52));
        b.setPadding(dp(c, 12), dp(c, 9), dp(c, 12), dp(c, 9));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(c, 8);
        b.setLayoutParams(lp);
        return b;
    }

    public static View gap(Context c, int h) {
        Space s = new Space(c);
        s.setLayoutParams(new LinearLayout.LayoutParams(1, dp(c, h)));
        return s;
    }

    /** Add a back button row to the top of a page (for secondary/detail screens). */
    public static View backButton(Activity a) {
        boolean ar = LanguageUtil.current(a).equals("ar");
        LinearLayout row = new LinearLayout(a);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(a, 4), 0, dp(a, 12));

        ImageView arrow = new ImageView(a);
        arrow.setImageResource(R.drawable.ic_back);
        if (ar) arrow.setScaleX(-1f); // RTL: flip arrow direction
        row.addView(arrow, new LinearLayout.LayoutParams(dp(a, 32), dp(a, 32)));

        TextView lbl = text(a, ar ? "رجوع" : "Back", 14, Color.rgb(192, 192, 192));
        lbl.setPadding(dp(a, 8), 0, 0, 0);
        row.addView(lbl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(v -> a.finish());
        return row;
    }

    /** Bottom nav container. tabs: 0=Dashboard, 1=Cells, 2=History, 3=Reports, 4=More */
    public static View withBottomNav(Activity a, View content, int selected) {
        LinearLayout root = new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        root.setClipToPadding(false);
        applySystemBars(root);

        FrameLayout body = new FrameLayout(a);
        body.addView(content, new FrameLayout.LayoutParams(-1, -1));
        root.addView(body, new LinearLayout.LayoutParams(-1, 0, 1f));

        LinearLayout nav = new LinearLayout(a);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(a, 2), dp(a, 4), dp(a, 2), dp(a, 4));
        nav.setBackgroundColor(Color.rgb(5, 5, 5));

        boolean ar = LanguageUtil.current(a).equals("ar");
        root.setLayoutDirection(ar ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        addNavItem(a, nav, R.drawable.ic_nav_home,     ar ? "الرئيسية" : "Dashboard", 0, selected, MainActivity.class);
        addNavItem(a, nav, R.drawable.ic_nav_cells,    ar ? "الخلايا" : "Cells",      1, selected, CellsActivity.class);
        addNavItem(a, nav, R.drawable.ic_nav_history,  ar ? "السجل" : "History",      2, selected, HistoryActivity.class);
        addNavItem(a, nav, R.drawable.ic_nav_reports,  ar ? "التقارير" : "Reports",   3, selected, ReportsActivity.class);
        addNavItem(a, nav, R.drawable.ic_nav_info,     ar ? "المزيد" : "More",        4, selected, MoreActivity.class);

        nav.setMinimumHeight(dp(a, 68));
        root.addView(nav, new LinearLayout.LayoutParams(-1, -2));
        return root;
    }

    private static void addNavItem(Activity a, LinearLayout nav, int iconRes, String label,
                                   int index, int selected, Class<?> target) {
        LinearLayout item = new LinearLayout(a);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        item.setPadding(dp(a, 2), dp(a, 4), dp(a, 2), dp(a, 4));

        boolean isSelected = index == selected;
        int textColor = isSelected ? Color.rgb(0, 230, 118) : Color.rgb(100, 120, 140);

        ImageView icon = new ImageView(a);
        icon.setImageResource(iconRes);
        // No color filter — preserve multicolor icons; dim non-selected via alpha
        icon.setAlpha(isSelected ? 1.0f : 0.55f);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        icon.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        item.addView(icon, new LinearLayout.LayoutParams(dp(a, 26), dp(a, 26)));

        TextView tv = text(a, label, 9, textColor);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, dp(a, 2), 0, 0);
        item.addView(tv, new LinearLayout.LayoutParams(-1, -2));
        tv.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        item.setContentDescription(label);
        item.setSelected(isSelected);
        item.setFocusable(true);

        item.setOnClickListener(v -> {
            if (index == selected) return;
            Intent i = new Intent(a, target);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            a.startActivity(i);
        });
        nav.addView(item, new LinearLayout.LayoutParams(0, -1, 1f));
    }
}
