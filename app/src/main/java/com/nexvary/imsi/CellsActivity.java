package com.nexvary.imsi;

import android.app.*;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.*;
import android.view.*;
import android.widget.*;
import java.util.*;

/** Dedicated cell details screen. TG05/TG06. */
public class CellsActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(2, 7, 13));
        LinearLayout page = UiKit.page(this);
        UiKit.applySystemBars(page);
        scroll.addView(page);
        setContentView(UiKit.withBottomNav(this, scroll, 1));
        buildUi(page);
    }

    private void buildUi(LinearLayout page) {
        boolean ar = LanguageUtil.current(this).equals("ar");
        page.addView(UiKit.eyebrow(this, ar ? "الخلايا" : "CELLS"));
        page.addView(UiKit.title(this, ar ? "معلومات الخلايا" : "Cell Information"));
        page.addView(UiKit.gap(this, 12));

        TelephonyManager tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        List<CellInfo> cells = null;
        try {
            cells = tel.getAllCellInfo();
        } catch (SecurityException ignored) {}

        if (cells == null || cells.isEmpty()) {
            page.addView(UiKit.body(this, ar ? "لا تتوفر بيانات خلايا. تحقق من الصلاحيات وخدمات الموقع." :
                    "No cell data available. Check permissions and Location services."));
            return;
        }

        for (CellInfo ci : cells) {
            page.addView(buildCellCard(ci, ar));
            page.addView(UiKit.gap(this, 10));
        }
    }

    private View buildCellCard(CellInfo ci, boolean ar) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.panel);
        int p = UiKit.dp(this, 14);
        card.setPadding(p, p, p, p);
        boolean serving = ci.isRegistered();
        String typeLabel = serving ? (ar ? "خلية الخدمة" : "Serving") : (ar ? "جارة" : "Neighbor");
        int typeColor = serving ? Color.rgb(41, 241, 139) : Color.rgb(23, 151, 255);

        TextView header = new TextView(this);
        header.setText(typeLabel);
        header.setTextColor(typeColor);
        header.setTextSize(11f);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(header);

        addRow(card, ar ? "النوع" : "Type", networkType(ci));
        addRow(card, "Cell ID", cellId(ci));
        addRow(card, ar ? "النطاق" : "Band", band(ci));
        addRow(card, ar ? "الإشارة" : "Signal", signal(ci));
        addRow(card, ar ? "RSRQ / الجودة" : "RSRQ / Quality", quality(ci));
        addRow(card, "SINR", sinr(ci));
        addRow(card, "MCC/MNC", mccMnc(ci));
        return card;
    }

    private void addRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        int v = UiKit.dp(this, 4);
        row.setPadding(0, v, 0, v);
        TextView lv = UiKit.body(this, label + ": ");
        lv.setTextColor(Color.rgb(90, 120, 148));
        lv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(lv);
        TextView vv = UiKit.body(this, value);
        vv.setTextColor(Color.rgb(244, 248, 252));
        row.addView(vv);
        parent.addView(row);
    }

    private String networkType(CellInfo ci) {
        if (ci instanceof CellInfoLte) return "LTE (4G)";
        if (ci instanceof CellInfoNr) return "NR (5G)";
        if (ci instanceof CellInfoWcdma) return "WCDMA (3G)";
        if (ci instanceof CellInfoGsm) return "GSM (2G)";
        return "Unknown";
    }

    private String cellId(CellInfo ci) {
        try {
            if (ci instanceof CellInfoLte) return String.valueOf(((CellInfoLte) ci).getCellIdentity().getCi());
            if (ci instanceof CellInfoNr) return String.valueOf(((CellInfoNr) ci).getCellIdentity().getNci());
            if (ci instanceof CellInfoWcdma) return String.valueOf(((CellInfoWcdma) ci).getCellIdentity().getCid());
            if (ci instanceof CellInfoGsm) return String.valueOf(((CellInfoGsm) ci).getCellIdentity().getCid());
        } catch (Exception ignored) {}
        return "—";
    }

    private String band(CellInfo ci) {
        try {
            if (ci instanceof CellInfoLte) {
                int earfcn = ((CellInfoLte) ci).getCellIdentity().getEarfcn();
                return earfcn != Integer.MAX_VALUE ? "EARFCN " + earfcn : "—";
            }
            if (ci instanceof CellInfoNr) {
                int arfcn = ((CellInfoNr) ci).getCellIdentity().getNrarfcn();
                return arfcn != Integer.MAX_VALUE ? "NR-ARFCN " + arfcn : "—";
            }
        } catch (Exception ignored) {}
        return "—";
    }

    private String signal(CellInfo ci) {
        try {
            if (ci instanceof CellInfoLte) {
                int dbm = ((CellInfoLte) ci).getCellSignalStrength().getRsrp();
                return dbm != Integer.MAX_VALUE ? dbm + " dBm (RSRP)" : "N/A";
            }
            if (ci instanceof CellInfoNr) {
                int dbm = ((CellInfoNr) ci).getCellSignalStrength().getSsRsrp();
                return dbm != Integer.MAX_VALUE ? dbm + " dBm (SS-RSRP)" : "N/A";
            }
            if (ci instanceof CellInfoWcdma) {
                int dbm = ((CellInfoWcdma) ci).getCellSignalStrength().getDbm();
                return dbm != Integer.MAX_VALUE ? dbm + " dBm" : "N/A";
            }
            if (ci instanceof CellInfoGsm) {
                int dbm = ((CellInfoGsm) ci).getCellSignalStrength().getDbm();
                return dbm != Integer.MAX_VALUE ? dbm + " dBm" : "N/A";
            }
        } catch (Exception ignored) {}
        return "N/A";
    }

    private String quality(CellInfo ci) {
        try {
            if (ci instanceof CellInfoLte) {
                int q = ((CellInfoLte) ci).getCellSignalStrength().getRsrq();
                return q != Integer.MAX_VALUE ? q + " dB" : "N/A";
            }
        } catch (Exception ignored) {}
        return "N/A";
    }

    private String sinr(CellInfo ci) {
        try {
            if (ci instanceof CellInfoLte) {
                int s = ((CellInfoLte) ci).getCellSignalStrength().getRssnr();
                return s != Integer.MAX_VALUE ? s + " dB" : "N/A";
            }
            if (ci instanceof CellInfoNr) {
                int s = ((CellInfoNr) ci).getCellSignalStrength().getSsSinr();
                return s != Integer.MAX_VALUE ? s + " dB" : "N/A";
            }
        } catch (Exception ignored) {}
        return "N/A";
    }

    private String mccMnc(CellInfo ci) {
        try {
            if (ci instanceof CellInfoLte) {
                CellIdentityLte id = ((CellInfoLte) ci).getCellIdentity();
                String mcc = id.getMccString(), mnc = id.getMncString();
                if (mcc != null && mnc != null) return mcc + "/" + mnc;
            }
        } catch (Exception ignored) {}
        return "—";
    }
}
