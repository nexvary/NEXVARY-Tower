package com.nexvary.imsi;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.widget.*;
import org.json.*;
import java.io.File;
import java.util.*;

public class MainActivity extends Activity {
    private static final int REQ = 90;
    private static final int MAX_HISTORY = 180;
    private static final long LIVE_INTERVAL_MS = 8_000L;

    private TelephonyManager tel;
    private TextView liveState, monitorStatusPill, infoCellId, infoEnodeB, infoBand, infoTech, infoOperator;
    private Button liveButton;
    private AnomalyGaugeView anomalyGauge;
    private CircularMeasureView gaugeRsrp, gaugeRsrq, gaugeSinr, gaugeRssi, gaugeNetwork;
    private CellChangeView changeMap;
    private SecureStore store;
    private ScanRecord latest;
    private boolean liveMonitoring = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable liveTick = new Runnable() {
        @Override public void run() {
            if (!liveMonitoring) return;
            if (hasPermissions()) scan(true);
            handler.postDelayed(this, LIVE_INTERVAL_MS);
        }
    };

    @Override public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        android.view.View screen = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(UiKit.withBottomNav(this, screen, 0));
        tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        store = new SecureStore(this);

        anomalyGauge   = screen.findViewById(R.id.anomalyGauge);
        gaugeRsrp      = screen.findViewById(R.id.gaugeRsrp);
        gaugeRsrq      = screen.findViewById(R.id.gaugeRsrq);
        gaugeSinr      = screen.findViewById(R.id.gaugeSinr);
        gaugeRssi      = screen.findViewById(R.id.gaugeRssi);
        gaugeNetwork   = screen.findViewById(R.id.gaugeNetwork);
        changeMap      = screen.findViewById(R.id.changeMap);
        liveState      = screen.findViewById(R.id.liveState);
        monitorStatusPill = screen.findViewById(R.id.monitorStatusPill);
        infoCellId     = screen.findViewById(R.id.infoCellId);
        infoEnodeB     = screen.findViewById(R.id.infoEnodeB);
        infoBand       = screen.findViewById(R.id.infoBand);
        infoTech       = screen.findViewById(R.id.infoTech);
        infoOperator   = screen.findViewById(R.id.infoOperator);
        liveButton     = screen.findViewById(R.id.liveButton);

        screen.findViewById(R.id.scanButton).setOnClickListener(v -> ensureAndScan(false));
        liveButton.setOnClickListener(v -> toggleLiveMonitoring());
        screen.findViewById(R.id.baselineButton).setOnClickListener(v -> promptBaseline());
        screen.findViewById(R.id.reportButton).setOnClickListener(v -> exportReport());

        // Company links (TG07)
        screen.findViewById(R.id.linkFacebook).setOnClickListener(v -> LinkHelper.openUrl(this, LinkHelper.URL_FACEBOOK));
        screen.findViewById(R.id.linkX).setOnClickListener(v -> LinkHelper.openUrl(this, LinkHelper.URL_X));
        screen.findViewById(R.id.linkYoutube).setOnClickListener(v -> LinkHelper.openUrl(this, LinkHelper.URL_YOUTUBE));
        screen.findViewById(R.id.linkWebsite).setOnClickListener(v -> LinkHelper.openUrl(this, LinkHelper.URL_WEBSITE));
        screen.findViewById(R.id.linkEmail).setOnClickListener(v -> LinkHelper.openEmail(this));

        // Init gauges with N/A
        boolean ar = LanguageUtil.current(this).equals("ar");
        gaugeRsrp.setMeasure("N/A", "dBm", "RSRP", Color.rgb(90, 120, 148), 0f);
        gaugeRsrq.setMeasure("N/A", "dB",  "RSRQ", Color.rgb(90, 120, 148), 0f);
        gaugeSinr.setMeasure("N/A", "dB",  "SINR", Color.rgb(90, 120, 148), 0f);
        gaugeRssi.setMeasure("N/A", "dBm", "RSSI", Color.rgb(90, 120, 148), 0f);
        gaugeNetwork.setNetworkType(ar ? "—" : "—", Color.rgb(90, 120, 148));

        refreshChangeMap();
    }

    @Override protected void onPause() {
        super.onPause();
        if (liveMonitoring) stopLiveMonitoring(t("Paused", "متوقف مؤقتًا"));
    }

    private boolean hasPermissions() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void ensureAndScan(boolean fromLive) {
        List<String> missing = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            missing.add(Manifest.permission.ACCESS_FINE_LOCATION);
            missing.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) missing.add(Manifest.permission.READ_PHONE_STATE);
        if (!missing.isEmpty()) { requestPermissions(missing.toArray(new String[0]), REQ); return; }
        scan(fromLive);
    }

    @Override public void onRequestPermissionsResult(int r, String[] p, int[] g) {
        super.onRequestPermissionsResult(r, p, g);
        if (r == REQ) {
            boolean ok = g.length > 0;
            for (int x : g) if (x != PackageManager.PERMISSION_GRANTED) ok = false;
            if (ok) scan(false);
        }
    }

    private void toggleLiveMonitoring() {
        if (liveMonitoring) { stopLiveMonitoring(t("Live monitoring stopped", "توقفت المراقبة المباشرة")); return; }
        if (!hasPermissions()) { ensureAndScan(false); return; }
        liveMonitoring = true;
        liveButton.setText(t("STOP", "إيقاف"));
        liveState.setText(t("LIVE · foreground-only · every 8s", "مباشر · في الواجهة · كل 8 ث"));
        monitorStatusPill.setText(t("Active · local only", "نشطة (محلية فقط)"));
        monitorStatusPill.setTextColor(Color.rgb(41, 241, 139));
        handler.removeCallbacks(liveTick);
        handler.post(liveTick);
    }

    private void stopLiveMonitoring(String state) {
        liveMonitoring = false;
        handler.removeCallbacks(liveTick);
        if (liveButton != null) liveButton.setText(t("LIVE", "مباشر"));
        if (liveState != null) liveState.setText(state);
        if (monitorStatusPill != null) {
            monitorStatusPill.setText(t("Monitoring off", "المراقبة متوقفة"));
            monitorStatusPill.setTextColor(Color.rgb(142, 164, 184));
        }
    }

    private void scan(boolean fromLive) {
        try {
            String op = safe(tel.getNetworkOperatorName());
            String rat = networkName(tel.getDataNetworkType());
            List<CellInfo> cells = tel.getAllCellInfo();
            int registered = 0, neigh = 0, bestDbm = -999, bestQuality = -999;
            int rsrp = Integer.MAX_VALUE, rsrq = Integer.MAX_VALUE, sinr = Integer.MAX_VALUE, rssi = Integer.MAX_VALUE;
            String servingId = "—", servingCell = "—", servingEnodeB = "—", servingBand = "—";
            List<String> neighborIds = new ArrayList<>();

            if (cells != null) for (CellInfo c : cells) {
                int dbm = signalDbm(c);
                int q = signalQuality(c);
                if (dbm > bestDbm) bestDbm = dbm;
                if (q != -999 && (bestQuality == -999 || q > bestQuality)) bestQuality = q;
                if (c.isRegistered()) {
                    registered++;
                    servingId = identity(c);
                    // Extract individual measurements for gauges
                    if (c instanceof CellInfoLte) {
                        CellSignalStrengthLte s = ((CellInfoLte) c).getCellSignalStrength();
                        CellIdentityLte id = ((CellInfoLte) c).getCellIdentity();
                        rsrp = s.getRsrp();
                        rsrq = s.getRsrq();
                        try { sinr = s.getRssnr(); } catch (Exception ignored) {}
                        try { rssi = s.getRssi(); } catch (Exception ignored) {}
                        servingCell = String.valueOf(id.getCi());
                        try { servingEnodeB = String.valueOf(id.getEarfcn()); } catch (Exception ignored) {}
                        int earfcn = id.getEarfcn();
                        servingBand = earfcn != Integer.MAX_VALUE ? "EARFCN " + earfcn : "LTE";
                    } else if (c instanceof CellInfoNr) {
                        CellSignalStrengthNr s = (CellSignalStrengthNr) ((CellInfoNr) c).getCellSignalStrength();
                        CellIdentityNr id = (CellIdentityNr) ((CellInfoNr) c).getCellIdentity();
                        try { rsrp = s.getSsRsrp(); rsrq = s.getSsRsrq(); sinr = s.getSsSinr(); } catch (Exception ignored) {}
                        servingCell = String.valueOf(id.getNci());
                        servingBand = "5G NR";
                    }
                } else {
                    neigh++;
                    neighborIds.add(identity(c));
                }
            }

            // Update circular measurement gauges (TG02)
            updateGaugeRsrp(rsrp);
            updateGaugeRsrq(rsrq);
            updateGaugeSinr(sinr);
            updateGaugeRssi(rssi);
            updateGaugeNetwork(rat);

            // Update cell info panel (TG05)
            infoCellId.setText("Cell ID: " + servingCell);
            infoEnodeB.setText("eNodeB: " + servingEnodeB);
            infoBand.setText("Band: " + servingBand);
            infoTech.setText("Tech: " + rat);
            infoOperator.setText("Op: " + (op.isEmpty() ? "—" : op));

            Collections.sort(neighborIds);
            String neighborSignature = compactSignature(neighborIds);
            List<ScanRecord> old = loadHistory();
            ScanRecord previous = old.isEmpty() ? null : old.get(old.size() - 1);
            List<String> bases = loadBaselines();

            Assessment a = assess(op, rat, servingId, neigh, registered, bestDbm, bestQuality, neighborSignature, previous, bases);
            latest = new ScanRecord(System.currentTimeMillis(), op, rat, servingId, neigh, bestDbm, bestQuality, a.score, neighborSignature, reasonsForStorage(a.reasons));
            old.add(latest);
            while (old.size() > MAX_HISTORY) old.remove(0);
            saveHistory(old);

            anomalyGauge.setScore(a.score);
            refreshChangeMap();

            if (rat.contains("2G") && (previous == null || !previous.radio.contains("2G")) && !fromLive) show2GWarning();
        } catch (SecurityException e) {
            if (liveMonitoring) stopLiveMonitoring(t("Live monitoring stopped · permission unavailable", "توقف · الصلاحية غير متاحة"));
        }
    }

    private void updateGaugeRsrp(int rsrp) {
        if (rsrp == Integer.MAX_VALUE || rsrp == -999) {
            gaugeRsrp.setMeasure("N/A", "dBm", "RSRP", Color.rgb(90, 120, 148), 0f); return;
        }
        float f = Math.max(0f, Math.min(1f, (rsrp + 140f) / 96f));
        int c = rsrp >= -80 ? Color.rgb(41,241,139) : rsrp >= -100 ? Color.rgb(255,181,63) : Color.rgb(255,42,66);
        gaugeRsrp.setMeasure(String.valueOf(rsrp), "dBm", "RSRP", c, f);
    }

    private void updateGaugeRsrq(int rsrq) {
        if (rsrq == Integer.MAX_VALUE || rsrq == -999) {
            gaugeRsrq.setMeasure("N/A", "dB", "RSRQ", Color.rgb(90, 120, 148), 0f); return;
        }
        float f = Math.max(0f, Math.min(1f, (rsrq + 20f) / 17f));
        int c = rsrq >= -10 ? Color.rgb(41,241,139) : rsrq >= -15 ? Color.rgb(255,181,63) : Color.rgb(255,42,66);
        gaugeRsrq.setMeasure(String.valueOf(rsrq), "dB", "RSRQ", c, f);
    }

    private void updateGaugeSinr(int sinr) {
        if (sinr == Integer.MAX_VALUE || sinr == -999) {
            gaugeSinr.setMeasure("N/A", "dB", "SINR", Color.rgb(90, 120, 148), 0f); return;
        }
        float f = Math.max(0f, Math.min(1f, (sinr + 20f) / 50f));
        int c = sinr >= 13 ? Color.rgb(41,241,139) : sinr >= 0 ? Color.rgb(255,181,63) : Color.rgb(255,42,66);
        gaugeSinr.setMeasure(String.valueOf(sinr), "dB", "SINR", c, f);
    }

    private void updateGaugeRssi(int rssi) {
        if (rssi == Integer.MAX_VALUE || rssi == -999) {
            gaugeRssi.setMeasure("N/A", "dBm", "RSSI", Color.rgb(90, 120, 148), 0f); return;
        }
        float f = Math.max(0f, Math.min(1f, (rssi + 130f) / 86f));
        int c = rssi >= -65 ? Color.rgb(41,241,139) : rssi >= -85 ? Color.rgb(255,181,63) : Color.rgb(255,42,66);
        gaugeRssi.setMeasure(String.valueOf(rssi), "dBm", "RSSI", c, f);
    }

    private void updateGaugeNetwork(String rat) {
        String gen = rat.contains("5G") ? "5G" : rat.contains("4G") ? "4G" : rat.contains("3G") ? "3G" : rat.contains("2G") ? "2G" : "—";
        int c = rat.contains("5G") ? Color.rgb(23,151,255) : rat.contains("4G") ? Color.rgb(41,241,139) : rat.contains("3G") ? Color.rgb(255,181,63) : Color.rgb(255,42,66);
        String lbl = rat.contains("LTE") ? "LTE" : rat.contains("NR") ? "NR" : "";
        gaugeNetwork.setMeasure(gen, lbl, t("Network","الشبكة"), c, 1f);
    }

    private Assessment assess(String op, String rat, String servingId, int neigh, int registered, int bestDbm, int quality, String neighborSignature, ScanRecord prev, List<String> baselines) {
        int risk = 0;
        List<String> reasons = new ArrayList<>();
        if (rat.contains("2G")) { risk += 30; reasons.add("30 · active radio is 2G/GSM"); }
        if (registered == 0) { risk += 18; reasons.add("18 · no registered serving cell was exposed by Android"); }
        if (registered > 1) { risk += 7; reasons.add("7 · multiple cells are marked registered"); }
        if (neigh == 0) { risk += 9; reasons.add("9 · no neighboring cells were reported"); }
        if (bestDbm > -55 && bestDbm != -999) { risk += 5; reasons.add("5 · unusually strong cellular signal (context-dependent)"); }
        if (quality != -999 && quality < -20) { risk += 3; reasons.add("3 · poor signal-quality metric despite available service"); }
        String current = op + "|" + rat + "|" + servingId;
        boolean matched = false;
        for (String bs : baselines) { String[] parts = bs.split("\\|", 2); if (parts.length == 2 && parts[1].equals(current)) { matched = true; break; } }
        if (!baselines.isEmpty() && !matched) { risk += 14; reasons.add("14 · current fingerprint does not match a saved trusted baseline"); }
        String neighborNote = "Neighbor comparison: not enough history yet.";
        if (prev != null) {
            if (!prev.cell.equals(servingId)) { risk += 5; reasons.add("5 · serving-cell identity changed since the previous scan"); }
            if (!prev.operator.equals(op)) { risk += 15; reasons.add("15 · operator identity changed since the previous scan"); }
            if (isDowngrade(prev.radio, rat)) { risk += 14; reasons.add("14 · radio generation downgraded from " + prev.radio + " to " + rat); }
            if (prev.dbm != -999 && bestDbm != -999 && Math.abs(bestDbm - prev.dbm) >= 25) { risk += 8; reasons.add("8 · signal changed by at least 25 dB between scans"); }
            if (!prev.neighborSignature.isEmpty() && !neighborSignature.isEmpty() && !prev.neighborSignature.equals(neighborSignature)) {
                int delta = Math.abs(neigh - prev.neighbors);
                risk += delta >= 3 ? 8 : 4;
                reasons.add((delta >= 3 ? "8" : "4") + " · neighboring-cell set changed between scans");
                neighborNote = "Neighbor comparison: changed · " + prev.neighbors + " → " + neigh;
            } else if (!neighborSignature.isEmpty()) {
                neighborNote = "Neighbor comparison: stable · " + neigh + " visible";
            }
        }
        List<ScanRecord> recent = loadHistory();
        int from = Math.max(0, recent.size() - 5);
        Set<String> recentCells = new HashSet<>();
        Set<String> recentRadios = new HashSet<>();
        int recent2G = 0;
        for (int i = from; i < recent.size(); i++) {
            ScanRecord r = recent.get(i);
            if (r.cell != null && !r.cell.isEmpty() && !r.cell.equals("—")) recentCells.add(r.cell);
            if (r.radio != null && !r.radio.isEmpty()) { recentRadios.add(r.radio); if (r.radio.contains("2G")) recent2G++; }
        }
        if (!servingId.equals("—")) recentCells.add(servingId);
        recentRadios.add(rat);
        if (recentCells.size() >= 4) { risk += 12; reasons.add("12 · repeated serving-cell churn across the recent scan window"); }
        else if (recentCells.size() == 3) { risk += 6; reasons.add("6 · multiple serving-cell identities appeared in the recent scan window"); }
        if (recentRadios.size() >= 3) { risk += 7; reasons.add("7 · radio generation changed repeatedly across recent scans"); }
        if (rat.contains("2G") && recent2G >= 1) { risk += 6; reasons.add("6 · repeated 2G presence in the recent scan window"); }
        if (prev != null && !prev.cell.equals(servingId) && prev.dbm != -999 && bestDbm != -999 && Math.abs(bestDbm - prev.dbm) >= 25) {
            risk += 6; reasons.add("6 · cell identity and signal level changed sharply together");
        }
        return new Assessment(Math.min(risk, 100), reasons, neighborNote);
    }

    private boolean isDowngrade(String oldRadio, String newRadio) { return generationRank(newRadio) < generationRank(oldRadio) && generationRank(newRadio) > 0; }
    private int generationRank(String r) { if (r.contains("5G")) return 4; if (r.contains("4G")) return 3; if (r.contains("3G")) return 2; if (r.contains("2G")) return 1; return 0; }

    private String reasonsForStorage(List<String> reasons) {
        if (reasons == null || reasons.isEmpty()) return "No material anomaly indicators triggered.";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < reasons.size(); i++) { if (i > 0) b.append(" | "); b.append(reasons.get(i)); }
        return b.toString();
    }

    private void promptBaseline() {
        if (latest == null) { Toast.makeText(this, t("Run a scan first","شغّل فحصًا أولًا"), Toast.LENGTH_SHORT).show(); return; }
        final EditText input = new EditText(this);
        input.setHint("e.g. Office, Home, HQ");
        new AlertDialog.Builder(this)
                .setTitle(t("Name trusted baseline","اسم خط الأساس الموثوق"))
                .setMessage(t("This fingerprint is encrypted and stored only on this device.","البصمة مشفرة ومحفوظة على هذا الجهاز فقط."))
                .setView(input)
                .setNegativeButton(t("Cancel","إلغاء"), null)
                .setPositiveButton(t("Save","حفظ"), (d, w) -> saveBaseline(input.getText().toString()))
                .show();
    }

    private void saveBaseline(String name) {
        name = name.trim();
        if (name.isEmpty()) name = "Baseline " + (loadBaselines().size() + 1);
        String fingerprint = latest.operator + "|" + latest.radio + "|" + latest.cell;
        List<String> b = loadBaselines();
        b.add(name.replace("|", "-") + "|" + fingerprint);
        while (b.size() > 12) b.remove(0);
        saveBaselines(b);
        Toast.makeText(this, t("Trusted baseline saved locally","تم حفظ خط الأساس محليًا"), Toast.LENGTH_SHORT).show();
    }

    private void exportReport() {
        try {
            File f = ReportExporter.create(this, loadHistory(), loadBaselines());
            startActivity(ReportExporter.share(this, f));
        } catch (Exception e) {
            Toast.makeText(this, t("Could not create report","تعذّر إنشاء التقرير"), Toast.LENGTH_LONG).show();
        }
    }

    private void show2GWarning() {
        new AlertDialog.Builder(this)
                .setTitle(t("2G fallback detected","رُصد تراجع إلى 2G"))
                .setMessage(t("The active data radio reports 2G/GSM. Unexpected 2G fallback is one anomaly worth reviewing.","الشبكة النشطة تُبلّغ عن 2G/GSM. التراجع غير المتوقع إلى 2G مؤشر يستحق المراجعة."))
                .setPositiveButton(t("Understood","فهمت"), null)
                .show();
    }

    private void refreshChangeMap() {
        changeMap.setRecords(loadHistory());
    }

    private List<ScanRecord> loadHistory() {
        List<ScanRecord> out = new ArrayList<>();
        try { JSONArray a = new JSONArray(store.get("history", "[]")); for (int i = 0; i < a.length(); i++) out.add(ScanRecord.from(a.getJSONObject(i))); } catch (Exception ignored) {}
        return out;
    }
    private void saveHistory(List<ScanRecord> h) { JSONArray a = new JSONArray(); for (ScanRecord r : h) a.put(r.json()); store.put("history", a.toString()); }
    private List<String> loadBaselines() { List<String> o = new ArrayList<>(); try { JSONArray a = new JSONArray(store.get("baselines", "[]")); for (int i = 0; i < a.length(); i++) o.add(a.optString(i)); } catch (Exception ignored) {} return o; }
    private void saveBaselines(List<String> b) { JSONArray a = new JSONArray(); for (String s : b) a.put(s); store.put("baselines", a.toString()); }
    private String safe(String s) { return s == null ? "" : s; }
    private int signalDbm(CellInfo c) {
        if (c instanceof CellInfoLte) return ((CellInfoLte) c).getCellSignalStrength().getDbm();
        if (c instanceof CellInfoNr) return ((CellInfoNr) c).getCellSignalStrength().getDbm();
        if (c instanceof CellInfoWcdma) return ((CellInfoWcdma) c).getCellSignalStrength().getDbm();
        if (c instanceof CellInfoGsm) return ((CellInfoGsm) c).getCellSignalStrength().getDbm();
        if (c instanceof CellInfoCdma) return ((CellInfoCdma) c).getCellSignalStrength().getDbm();
        return -999;
    }
    private int signalQuality(CellInfo c) {
        try { if (c instanceof CellInfoLte) return ((CellInfoLte) c).getCellSignalStrength().getRsrq(); } catch (Exception ignored) {}
        return -999;
    }
    private String identity(CellInfo c) {
        if (c instanceof CellInfoLte) { CellIdentityLte i = ((CellInfoLte) c).getCellIdentity(); return "LTE CI " + i.getCi() + " · TAC " + i.getTac() + " · " + i.getMccString() + "/" + i.getMncString(); }
        if (c instanceof CellInfoNr) { CellIdentityNr i = (CellIdentityNr) ((CellInfoNr) c).getCellIdentity(); return "5G NCI " + i.getNci() + " · TAC " + i.getTac() + " · " + i.getMccString() + "/" + i.getMncString(); }
        if (c instanceof CellInfoWcdma) { CellIdentityWcdma i = ((CellInfoWcdma) c).getCellIdentity(); return "3G CID " + i.getCid() + " · LAC " + i.getLac() + " · " + i.getMccString() + "/" + i.getMncString(); }
        if (c instanceof CellInfoGsm) { CellIdentityGsm i = ((CellInfoGsm) c).getCellIdentity(); return "2G CID " + i.getCid() + " · LAC " + i.getLac() + " · " + i.getMccString() + "/" + i.getMncString(); }
        return c.getClass().getSimpleName();
    }
    private String compactSignature(List<String> ids) { if (ids == null || ids.isEmpty()) return ""; StringBuilder b = new StringBuilder(); int max = Math.min(12, ids.size()); for (int i = 0; i < max; i++) { if (i > 0) b.append(";"); b.append(ids.get(i)); } return Integer.toHexString(b.toString().hashCode()) + ":" + ids.size(); }
    private String networkName(int t) {
        switch (t) {
            case TelephonyManager.NETWORK_TYPE_NR: return "5G NR";
            case TelephonyManager.NETWORK_TYPE_LTE: return "4G LTE";
            case TelephonyManager.NETWORK_TYPE_HSPAP: case TelephonyManager.NETWORK_TYPE_HSPA: case TelephonyManager.NETWORK_TYPE_UMTS: return "3G";
            case TelephonyManager.NETWORK_TYPE_GPRS: case TelephonyManager.NETWORK_TYPE_EDGE: case TelephonyManager.NETWORK_TYPE_GSM: return "2G";
            default: return "Unknown";
        }
    }
    private String t(String en, String ar) { return LanguageUtil.current(this).equals("ar") ? ar : en; }

    private static final class Assessment {
        final int score; final List<String> reasons; final String neighborNote;
        Assessment(int s, List<String> r, String n) { score = s; reasons = r; neighborNote = n; }
    }
}
