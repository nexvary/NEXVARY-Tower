package com.nexvary.imsi;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.telephony.*;
import android.view.WindowManager;
import android.widget.*;
import org.json.*;
import java.io.File;
import java.util.*;

public class MainActivity extends Activity {
    private static final int REQ = 90;
    private static final int MAX_HISTORY = 180;
    private static final long LIVE_INTERVAL_MS = 8_000L;

    private TelephonyManager tel;
    private TextView riskTitle, riskSummary, operator, radio, serving, neighbors, signal, historyText, baselineText, whyFlagged, liveState, neighborState;
    private Button liveButton;
    private RadarView radar;
    private CellChangeView changeMap;
    private SignalTimelineView signalTimeline;
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
        ScrollView rootScroll = screen.findViewById(R.id.rootScroll);
        rootScroll.setClipToPadding(false);
        tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        store = new SecureStore(this);

        radar = findViewById(R.id.radar);
        changeMap = findViewById(R.id.changeMap);
        signalTimeline = findViewById(R.id.signalTimeline);
        riskTitle = findViewById(R.id.riskTitle);
        riskSummary = findViewById(R.id.riskSummary);
        operator = findViewById(R.id.operator);
        radio = findViewById(R.id.radio);
        serving = findViewById(R.id.serving);
        neighbors = findViewById(R.id.neighbors);
        signal = findViewById(R.id.signal);
        historyText = findViewById(R.id.historyText);
        baselineText = findViewById(R.id.baselineText);
        whyFlagged = findViewById(R.id.whyFlagged);
        liveState = findViewById(R.id.liveState);
        neighborState = findViewById(R.id.neighborState);
        liveButton = findViewById(R.id.liveButton);

        findViewById(R.id.scanButton).setOnClickListener(v -> ensureAndScan(false));
        liveButton.setOnClickListener(v -> toggleLiveMonitoring());
        findViewById(R.id.baselineButton).setOnClickListener(v -> promptBaseline());
        findViewById(R.id.historyButton).setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.manageBaselinesButton).setOnClickListener(v -> startActivity(new Intent(this, BaselinesActivity.class)));
        findViewById(R.id.privacyButton).setOnClickListener(v -> startActivity(new Intent(this, PrivacyActivity.class)));
        findViewById(R.id.aboutButton).setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        findViewById(R.id.languageButton).setOnClickListener(v -> { LanguageUtil.toggle(this); recreate(); });
        findViewById(R.id.reportButton).setOnClickListener(v -> exportReport());
        findViewById(R.id.clearHistoryButton).setOnClickListener(v -> confirmClear());
        refreshLocalPanels();
    }

    @Override protected void onPause() {
        super.onPause();
        // Deliberately foreground-only: no covert/background cellular monitoring.
        if (liveMonitoring) stopLiveMonitoring("Paused while app is not visible");
    }

    private boolean hasPermissions() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void ensureAndScan(boolean fromLive) {
        List<String> missing = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) missing.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) missing.add(Manifest.permission.READ_PHONE_STATE);
        if (!missing.isEmpty()) {
            requestPermissions(missing.toArray(new String[0]), REQ);
            return;
        }
        scan(fromLive);
    }

    @Override public void onRequestPermissionsResult(int r, String[] p, int[] g) {
        super.onRequestPermissionsResult(r, p, g);
        if (r == REQ) {
            boolean ok = g.length > 0;
            for (int x : g) if (x != PackageManager.PERMISSION_GRANTED) ok = false;
            if (ok) scan(false);
            else {
                riskTitle.setText(t("Permissions required","الصلاحيات مطلوبة"));
                riskSummary.setText(t("Cell telemetry requires Phone and precise Location permission on Android. NEXVARY does not upload your location or cell history.","يتطلب الوصول إلى بيانات الخلايا صلاحية الهاتف والموقع الدقيق في Android. لا يرفع NEXVARY موقعك أو سجل الخلايا."));
            }
        }
    }

    private void toggleLiveMonitoring() {
        if (liveMonitoring) {
            stopLiveMonitoring("Live monitoring stopped");
            return;
        }
        if (!hasPermissions()) {
            ensureAndScan(false);
            return;
        }
        liveMonitoring = true;
        liveButton.setText(t("STOP LIVE","إيقاف المباشر"));
        liveState.setText(t("LIVE · foreground-only · every 8 seconds","مباشر · في الواجهة فقط · كل 8 ثوانٍ"));
        handler.removeCallbacks(liveTick);
        handler.post(liveTick);
    }

    private void stopLiveMonitoring(String state) {
        liveMonitoring = false;
        handler.removeCallbacks(liveTick);
        if (liveButton != null) liveButton.setText(t("START LIVE","مراقبة مباشرة"));
        if (liveState != null) liveState.setText(state);
    }

    private void scan(boolean fromLive) {
        try {
            String op = safe(tel.getNetworkOperatorName());
            String rat = networkName(tel.getDataNetworkType());
            List<CellInfo> cells = tel.getAllCellInfo();
            int registered = 0, neigh = 0, bestDbm = -999, bestQuality = -999;
            String servingId = "—";
            List<String> neighborIds = new ArrayList<>();

            if (cells != null) for (CellInfo c : cells) {
                int dbm = signalDbm(c);
                int q = signalQuality(c);
                if (dbm > bestDbm) bestDbm = dbm;
                if (q != -999 && (bestQuality == -999 || q > bestQuality)) bestQuality = q;
                if (c.isRegistered()) {
                    registered++;
                    servingId = identity(c);
                } else {
                    neigh++;
                    neighborIds.add(identity(c));
                }
            }
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

            operator.setText(t("Operator: ","المشغل: ") + (op.isEmpty() ? t("Unknown","غير معروف") : op));
            radio.setText(t("Radio: ","الشبكة: ") + rat);
            serving.setText(t("Serving cell: ","الخلية الحالية: ") + servingId);
            neighbors.setText(t("Neighbor cells: ","الخلايا المجاورة: ") + neigh);
            signal.setText(t("Signal: ","الإشارة: ") + (bestDbm == -999 ? "—" : bestDbm + " dBm") + (bestQuality == -999 ? "" : " · quality " + bestQuality + " dB"));
            radar.setRisk(a.score);
            whyFlagged.setText(a.reasons.isEmpty() ? t("No material anomaly indicators were triggered by this scan.","لم يفعّل هذا الفحص مؤشرات شذوذ جوهرية.") : joinReasons(a.reasons));
            neighborState.setText(a.neighborNote);

            if (a.score >= 70) {
                riskTitle.setText(t("Elevated anomaly risk","مستوى شذوذ مرتفع"));
                riskSummary.setText(t("Several independent indicators changed at the same time. This is not proof of an IMSI catcher; repeat scans and consider professional RF/TSCM validation if the pattern persists.","تغيرت عدة مؤشرات مستقلة في الوقت نفسه. هذا ليس إثباتًا لوجود IMSI Catcher؛ أعد الفحص وفكر في تحقق RF/TSCM مهني إذا استمر النمط."));
            } else if (a.score >= 40) {
                riskTitle.setText(t("Review recommended","يوصى بالمراجعة"));
                riskSummary.setText(t("Multiple moderate indicators deserve comparison over time. Review the reason list, trusted baselines and cell-transition timeline before drawing conclusions.","ظهرت عدة مؤشرات متوسطة تستحق المقارنة عبر الزمن. راجع الأسباب وخطوط الأساس ومسار تغير الخلايا قبل استخلاص نتيجة."));
            } else if (a.score >= 20) {
                riskTitle.setText(t("Low-level variance","تباين منخفض"));
                riskSummary.setText(t("A small number of weak indicators were observed. Normal mobility, congestion and carrier optimization can produce similar changes.","ظهرت مؤشرات ضعيفة قليلة. الحركة الطبيعية والازدحام وتحسينات شركة الاتصالات قد تنتج تغيرات مماثلة."));
            } else {
                riskTitle.setText(t("No strong anomaly observed","لا يوجد شذوذ قوي ظاهر"));
                riskSummary.setText(t("This snapshot shows no strong anomaly. A normal score cannot rule out sophisticated cell-site simulators.","لا يظهر هذا القياس شذوذًا قويًا. النتيجة الطبيعية لا تستبعد محاكيات الأبراج المتقدمة."));
            }

            refreshLocalPanels();
            if (rat.contains("2G") && (previous == null || !previous.radio.contains("2G")) && !fromLive) show2GWarning();
        } catch (SecurityException e) {
            riskTitle.setText(t("Permission unavailable","الصلاحية غير متاحة"));
            riskSummary.setText(t("Android restricted cellular telemetry. Check app permissions and Location services.","قيّد Android بيانات الشبكة الخلوية. تحقق من صلاحيات التطبيق وخدمات الموقع."));
            if (liveMonitoring) stopLiveMonitoring("Live monitoring stopped · permission unavailable");
        }
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
        for (String b : baselines) {
            String[] parts = b.split("\\|", 2);
            if (parts.length == 2 && parts[1].equals(current)) { matched = true; break; }
        }
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
                neighborNote = "Neighbor comparison: changed · count " + prev.neighbors + " → " + neigh;
            } else if (!neighborSignature.isEmpty()) {
                neighborNote = "Neighbor comparison: stable against previous scan · " + neigh + " visible";
            }
        }
        // v0.6: short-window anomaly correlation. A single cell change is common; repeated churn is weighted only when it persists.
        List<ScanRecord> recent = loadHistory();
        int from = Math.max(0, recent.size() - 5);
        Set<String> recentCells = new HashSet<>();
        Set<String> recentRadios = new HashSet<>();
        int recent2G = 0;
        for (int i = from; i < recent.size(); i++) {
            ScanRecord r = recent.get(i);
            if (r.cell != null && !r.cell.isEmpty() && !r.cell.equals("—")) recentCells.add(r.cell);
            if (r.radio != null && !r.radio.isEmpty()) {
                recentRadios.add(r.radio);
                if (r.radio.contains("2G")) recent2G++;
            }
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

    private boolean isDowngrade(String oldRadio, String newRadio) {
        return generationRank(newRadio) < generationRank(oldRadio) && generationRank(newRadio) > 0;
    }
    private int generationRank(String r) {
        if (r.contains("5G")) return 4;
        if (r.contains("4G")) return 3;
        if (r.contains("3G")) return 2;
        if (r.contains("2G")) return 1;
        return 0;
    }

    private String joinReasons(List<String> reasons) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < reasons.size(); i++) {
            if (i > 0) b.append("\n");
            b.append("• ").append(reasons.get(i));
        }
        return b.toString();
    }

    private String reasonsForStorage(List<String> reasons) {
        if (reasons == null || reasons.isEmpty()) return "No material anomaly indicators triggered.";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < reasons.size(); i++) {
            if (i > 0) b.append(" | ");
            b.append(reasons.get(i));
        }
        return b.toString();
    }

    private void promptBaseline() {
        if (latest == null) { Toast.makeText(this, t("Run a scan first","شغّل فحصًا أولًا"), Toast.LENGTH_SHORT).show(); return; }
        final EditText input = new EditText(this);
        input.setHint("e.g. Office, Home, HQ");
        new AlertDialog.Builder(this)
                .setTitle("Name trusted baseline")
                .setMessage("This label and the current cellular fingerprint are encrypted and stored only on this device.")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (d, w) -> saveBaseline(input.getText().toString()))
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
        refreshLocalPanels();
        Toast.makeText(this, t("Trusted baseline saved locally","تم حفظ خط الأساس محليًا"), Toast.LENGTH_SHORT).show();
    }

    private void exportReport() {
        try {
            File f = ReportExporter.create(this, loadHistory(), loadBaselines());
            startActivity(ReportExporter.share(this, f));
        } catch (Exception e) {
            Toast.makeText(this, "Could not create report", Toast.LENGTH_LONG).show();
        }
    }

    private void confirmClear() {
        new AlertDialog.Builder(this)
                .setTitle("Clear local history?")
                .setMessage("This removes scan history from encrypted local storage. Trusted baselines are kept.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Clear", (d, w) -> {
                    store.remove("history");
                    latest = null;
                    whyFlagged.setText("No scan is selected. Run a scan to see exactly why a result was flagged.");
                    refreshLocalPanels();
                }).show();
    }

    private void show2GWarning() {
        new AlertDialog.Builder(this)
                .setTitle("2G fallback detected")
                .setMessage("The active data radio reports 2G/GSM. A downgrade can be legitimate, but unexpected fallback is one anomaly worth reviewing—especially where 4G/5G is normally available.")
                .setPositiveButton("Understood", null)
                .show();
    }

    private void refreshLocalPanels() {
        List<ScanRecord> h = loadHistory();
        changeMap.setRecords(h);
        signalTimeline.setRecords(h);
        if (h.isEmpty()) historyText.setText("No local scan history yet.");
        else {
            ScanRecord r = h.get(h.size() - 1);
            int changes = 0;
            for (int i = 1; i < h.size(); i++) if (!h.get(i).cell.equals(h.get(i - 1).cell)) changes++;
            historyText.setText(h.size() + " encrypted scans · " + changes + " cell transitions · latest risk " + r.risk + "/100");
        }
        List<String> b = loadBaselines();
        baselineText.setText(b.isEmpty() ? "No trusted baselines saved." : b.size() + " trusted baselines saved locally.");
    }

    private List<ScanRecord> loadHistory() {
        List<ScanRecord> out = new ArrayList<>();
        try {
            JSONArray a = new JSONArray(store.get("history", "[]"));
            for (int i = 0; i < a.length(); i++) out.add(ScanRecord.from(a.getJSONObject(i)));
        } catch (Exception ignored) {}
        return out;
    }

    private void saveHistory(List<ScanRecord> h) {
        JSONArray a = new JSONArray();
        for (ScanRecord r : h) a.put(r.json());
        store.put("history", a.toString());
    }

    private List<String> loadBaselines() {
        List<String> o = new ArrayList<>();
        try {
            JSONArray a = new JSONArray(store.get("baselines", "[]"));
            for (int i = 0; i < a.length(); i++) o.add(a.optString(i));
        } catch (Exception ignored) {}
        return o;
    }

    private void saveBaselines(List<String> b) {
        JSONArray a = new JSONArray();
        for (String s : b) a.put(s);
        store.put("baselines", a.toString());
    }

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
        try {
            if (c instanceof CellInfoLte) return ((CellInfoLte) c).getCellSignalStrength().getRsrq();
            if (c instanceof CellInfoNr) return ((android.telephony.CellSignalStrengthNr) ((CellInfoNr) c).getCellSignalStrength()).getSsRsrq();
        } catch (Exception ignored) {}
        return -999;
    }

    private String identity(CellInfo c) {
        if (c instanceof CellInfoLte) {
            CellIdentityLte i = ((CellInfoLte) c).getCellIdentity();
            return "LTE CI " + i.getCi() + " · TAC " + i.getTac() + " · " + i.getMccString() + "/" + i.getMncString();
        }
        if (c instanceof CellInfoNr) {
            CellIdentityNr i = (CellIdentityNr) ((CellInfoNr) c).getCellIdentity();
            return "5G NCI " + i.getNci() + " · TAC " + i.getTac() + " · " + i.getMccString() + "/" + i.getMncString();
        }
        if (c instanceof CellInfoWcdma) {
            CellIdentityWcdma i = ((CellInfoWcdma) c).getCellIdentity();
            return "3G CID " + i.getCid() + " · LAC " + i.getLac() + " · " + i.getMccString() + "/" + i.getMncString();
        }
        if (c instanceof CellInfoGsm) {
            CellIdentityGsm i = ((CellInfoGsm) c).getCellIdentity();
            return "2G CID " + i.getCid() + " · LAC " + i.getLac() + " · " + i.getMccString() + "/" + i.getMncString();
        }
        return c.getClass().getSimpleName();
    }

    private String compactSignature(List<String> ids) {
        if (ids == null || ids.isEmpty()) return "";
        StringBuilder b = new StringBuilder();
        int max = Math.min(12, ids.size());
        for (int i = 0; i < max; i++) {
            if (i > 0) b.append(";");
            b.append(ids.get(i));
        }
        return Integer.toHexString(b.toString().hashCode()) + ":" + ids.size();
    }

    private String networkName(int t) {
        switch (t) {
            case TelephonyManager.NETWORK_TYPE_NR: return "5G NR";
            case TelephonyManager.NETWORK_TYPE_LTE: return "4G LTE";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_UMTS: return "3G";
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GSM: return "2G GSM";
            default: return "Unknown";
        }
    }

    private String t(String en, String ar) { return LanguageUtil.current(this).equals("ar") ? ar : en; }

    private static final class Assessment {
        final int score;
        final List<String> reasons;
        final String neighborNote;
        Assessment(int score, List<String> reasons, String neighborNote) {
            this.score = score;
            this.reasons = reasons;
            this.neighborNote = neighborNote;
        }
    }
}
