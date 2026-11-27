package com.nexvary.imsi;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/**
 * Security Knowledge Center — bilingual offline educational section. TG11.
 * Topics: cellular fundamentals, interception technology, Tower Guard internals,
 * alert interpretation, user protection, and references.
 */
public class SecurityAwarenessActivity extends Activity {

    @Override
    public void onCreate(Bundle b) {
        LanguageUtil.apply(this);
        super.onCreate(b);
        SecureScreenUtil.apply(this);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.BLACK);
        scroll.setClipToPadding(false);

        LinearLayout page = UiKit.page(this);
        page.setPadding(UiKit.dp(this, 16), UiKit.dp(this, 12),
                UiKit.dp(this, 16), UiKit.dp(this, 12));
        scroll.addView(page);

        setContentView(UiKit.withBottomNav(this, scroll, 4));
        buildUi(page);
    }

    @Override public void onBackPressed() { finish(); }


    private boolean ar() {
        return LanguageUtil.current(this).equals("ar");
    }

    private void buildUi(LinearLayout page) {
        boolean ar = ar();
        page.addView(UiKit.backButton(this));
        page.addView(UiKit.eyebrow(this, ar ? "\u0645\u0631\u0643\u0632 \u0627\u0644\u0648\u0639\u064a \u0627\u0644\u0623\u0645\u0646\u064a" : "SECURITY KNOWLEDGE CENTER"));
        page.addView(UiKit.title(this, ar ? "\u0627\u0644\u062a\u0648\u0639\u064a\u0629 \u0627\u0644\u0623\u0645\u0646\u064a\u0629" : "Security Awareness"));
        page.addView(UiKit.body(this, ar
            ? "\u062f\u0644\u064a\u0644 \u062a\u0639\u0644\u064a\u0645\u064a \u0634\u0627\u0645\u0644 \u0644\u0641\u0647\u0645 \u0627\u0644\u0634\u0628\u0643\u0627\u062a \u0627\u0644\u062e\u0644\u0648\u064a\u0629\u060c \u062a\u0642\u0646\u064a\u0627\u062a \u0627\u0644\u0627\u0639\u062a\u0631\u0627\u0636\u060c \u0648\u0643\u064a\u0641\u064a\u0629 \u0639\u0645\u0644 Tower Guard. \u062c\u0645\u064a\u0639 \u0627\u0644\u0645\u062d\u062a\u0648\u064a\u0627\u062a \u0645\u062a\u0627\u062d\u0629 \u0628\u062f\u0648\u0646 \u0627\u062a\u0635\u0627\u0644."
            : "A comprehensive offline guide to cellular networks, interception technology, and how Tower Guard works."));
        page.addView(UiKit.gap(this, 8));

        addSection(page, R.drawable.ic_skc_cellular,
            ar ? "\u0623\u0633\u0627\u0633\u064a\u0627\u062a \u0627\u0644\u0634\u0628\u0643\u0627\u062a \u0627\u0644\u062e\u0644\u0648\u064a\u0629" : "Cellular Network Fundamentals",
            new String[][]{
                {ar ? "\u0643\u064a\u0641 \u062a\u062a\u0648\u0627\u0635\u0644 \u0627\u0644\u0647\u0648\u0627\u062a\u0641 \u0645\u0639 \u0627\u0644\u0623\u0628\u0631\u0627\u062c" : "How phones communicate with towers",
                 ar ? "\u064a\u062a\u0648\u0627\u0635\u0644 \u0647\u0627\u062a\u0641\u0643 \u0645\u0639 \u0627\u0644\u0634\u0628\u0643\u0629 \u0639\u0628\u0631 \u0645\u062d\u0637\u0627\u062a \u0627\u0644\u0642\u0627\u0639\u062f\u0629 (eNodeB \u0641\u064a 4G\u060c gNodeB \u0641\u064a 5G). \u064a\u0646\u0636\u0645 \u0627\u0644\u0647\u0627\u062a\u0641 \u0625\u0644\u0649 \u0627\u0644\u062e\u0644\u064a\u0629 \u0630\u0627\u062a \u0623\u0642\u0648\u0649 \u0625\u0634\u0627\u0631\u0629. \u064a\u064f\u062e\u0635\u0651\u0635 \u0644\u0643\u0644 \u0627\u062a\u0635\u0627\u0644 TMSI \u0628\u062f\u0644\u0627\u064b \u0645\u0646 IMSI.\n\n\u0641\u064a 4G/5G\u060c \u0627\u0644\u0645\u0635\u0627\u062f\u0642\u0629 \u0645\u062a\u0628\u0627\u062f\u0644\u0629 \u2014 \u062e\u0627\u0635\u064a\u0629 \u063a\u0627\u0626\u0628\u0629 \u0641\u064a 2G."
                    : "Your phone connects to base stations (eNodeB in 4G, gNodeB in 5G), joining the cell with the strongest signal. The network assigns a temporary TMSI instead of broadcasting the IMSI.\n\nIn 4G/5G, mutual authentication (AKA) means the network proves its identity to the handset too \u2014 a property absent from 2G."},
                {ar ? "\u0627\u0644\u0641\u0648\u0627\u0631\u0642 \u0628\u064a\u0646 2G \u0648 3G \u0648 4G \u0648 5G" : "Differences between 2G, 3G, 4G and 5G",
                 ar ? "2G: \u0644\u0627 \u0645\u0635\u0627\u062f\u0642\u0629 \u0645\u062a\u0628\u0627\u062f\u0644\u0629\u060c \u062a\u0634\u0641\u064a\u0631 \u0636\u0639\u064a\u0641.\n3G: \u0623\u0636\u0627\u0641 \u0645\u0635\u0627\u062f\u0642\u0629 \u0645\u062a\u0628\u0627\u062f\u0644\u0629 \u0644\u0643\u0646 \u0639\u0631\u0636\u0629 \u0644\u062e\u0641\u0636 \u0627\u0644\u062c\u064a\u0644.\n4G LTE: \u0645\u0635\u0627\u062f\u0642\u0629 \u0643\u0627\u0645\u0644\u0629 AKA-LTE\u060c \u062a\u0634\u0641\u064a\u0631 \u0642\u0648\u064a.\n5G NR: \u064a\u0636\u064a\u0641 SUCI \u0644\u062a\u0634\u0641\u064a\u0631 IMSI. \u0623\u0642\u0648\u0649 \u062e\u0635\u0648\u0635\u064a\u0629."
                    : "2G (GSM): No mutual auth, cipher can be downgraded by a rogue station.\n3G (UMTS): Added mutual auth but susceptible to downgrade attacks.\n4G LTE: Full mutual auth (AKA-LTE), strong ciphering.\n5G NR: Adds SUCI protecting IMSI from passive sniffing. Strongest privacy profile."},
                {ar ? "\u0627\u0644\u062e\u0644\u0627\u064a\u0627 \u0627\u0644\u062e\u0627\u062f\u0645\u0629 \u0648\u0627\u0644\u0645\u062c\u0627\u0648\u0631\u0629" : "Serving cells and neighboring cells",
                 ar ? "\u062e\u0644\u064a\u0629 \u0627\u0644\u062e\u062f\u0645\u0629: \u0627\u0644\u0628\u0631\u062c \u0627\u0644\u0645\u062a\u0635\u0644 \u0628\u0647 \u062d\u0627\u0644\u064a\u0627\u064b ('registered' \u0641\u064a Android).\n\u062e\u0644\u0627\u064a\u0627 \u0645\u062c\u0627\u0648\u0631\u0629: \u0623\u0628\u0631\u0627\u062c \u0642\u0631\u064a\u0628\u0629 \u0644\u0644\u062a\u062c\u0648\u0627\u0644. \u0627\u062e\u062a\u0641\u0627\u0624\u0647\u0627 \u0627\u0644\u0645\u0641\u0627\u062c\u0626 \u0645\u0624\u0634\u0631 \u064a\u0631\u0635\u062f\u0647 Tower Guard."
                    : "Serving cell: the tower your phone is currently registered to (Android 'registered'). Neighboring cells: nearby towers for handover preparation. Sudden disappearance of neighbors is an indicator Tower Guard monitors."},
                {ar ? "\u0645\u0639\u0631\u0651\u0641\u0627\u062a \u0627\u0644\u062e\u0644\u064a\u0629" : "Cell identifiers: PCI, TAC, MCC, MNC",
                 ar ? "MCC: \u0631\u0645\u0632 \u0627\u0644\u062f\u0648\u0644\u0629. MNC: \u0631\u0645\u0632 \u0627\u0644\u0645\u0634\u063a\u0651\u0644. TAC/LAC: \u0645\u0646\u0637\u0642\u0629 \u062a\u063a\u0637\u064a\u0629. Cell ID: \u0645\u0639\u0631\u0651\u0641 \u0641\u0631\u064a\u062f \u0644\u0644\u062e\u0644\u064a\u0629. PCI: \u0645\u0639\u0631\u0651\u0641 \u0641\u064a\u0632\u064a\u0627\u0626\u064a (0\u2013503 \u0641\u064a LTE) \u2014 \u0644\u064a\u0633 \u0641\u0631\u064a\u062f\u0627\u064b \u0639\u0627\u0644\u0645\u064a\u0627\u064b."
                    : "MCC: country code. MNC: operator code. TAC/LAC: tracking area code. Cell ID: unique within tracking area. PCI (Physical Cell ID): physical layer id (0\u2013503 in LTE), NOT globally unique \u2014 reused across thousands of towers."},
                {ar ? "\u0642\u064a\u0627\u0633\u0627\u062a \u0627\u0644\u0625\u0634\u0627\u0631\u0629: RSRP, RSRQ, RSSI, SINR" : "Signal measurements: RSRP, RSRQ, RSSI, SINR",
                 ar ? "RSRP: \u0642\u0648\u0629 \u0625\u0634\u0627\u0631\u0629 \u0627\u0644\u0645\u0631\u062c\u0639 (dBm). \u0646\u0637\u0627\u0642 \u2212140 \u0625\u0644\u0649 \u221244.\nRSRQ: \u062c\u0648\u062f\u0629 \u0627\u0644\u0625\u0634\u0627\u0631\u0629 (dB).\nRSSI: \u0645\u062c\u0645\u0648\u0639 \u0627\u0644\u0637\u0627\u0642\u0629 \u0634\u0627\u0645\u0644 \u0627\u0644\u062a\u062f\u062e\u0644.\nSINR: \u0646\u0633\u0628\u0629 \u0627\u0644\u0625\u0634\u0627\u0631\u0629/\u0627\u0644\u062a\u062f\u062e\u0644+\u0627\u0644\u0636\u0648\u0636\u0627\u0621 (dB).\n\u0644\u0627 \u062a\u0639\u0646\u064a \u0647\u0630\u0647 \u0627\u0644\u0642\u064a\u0627\u0633\u0627\u062a \u0648\u062d\u062f\u0647\u0627 \u0648\u062c\u0648\u062f \u0627\u0639\u062a\u0631\u0627\u0636."
                    : "RSRP: reference signal received power (dBm), range \u2212140 to \u221244.\nRSRQ: signal quality ratio (dB), good above \u221210.\nRSSI: total wideband power including interference.\nSINR: signal-to-interference+noise ratio (dB), higher is better.\nNo single measurement value definitively indicates an IMSI catcher."}
            });

        addSection(page, R.drawable.ic_skc_interception,
            ar ? "\u062a\u0642\u0646\u064a\u0627\u062a \u0627\u0644\u0627\u0639\u062a\u0631\u0627\u0636 \u0627\u0644\u062e\u0644\u0648\u064a" : "Cellular Interception Technologies",
            new String[][]{
                {ar ? "\u0623\u062c\u0647\u0632\u0629 IMSI Catcher" : "IMSI catchers and cell-site simulators",
                 ar ? "IMSI Catcher \u0647\u0648 \u0628\u0631\u062c \u062e\u0644\u0648\u064a \u0645\u0632\u064a\u0641 \u064a\u0633\u062a\u063a\u0644 \u0628\u0631\u0648\u062a\u0648\u0643\u0648\u0644\u0627\u062a \u0627\u0644\u0634\u0628\u0643\u0629 \u0644\u0625\u062c\u0628\u0627\u0631 \u0627\u0644\u0647\u0648\u0627\u062a\u0641 \u0639\u0644\u0649 \u0627\u0644\u0627\u062a\u0635\u0627\u0644 \u0628\u0647 \u0628\u062f\u0644\u0627\u064b \u0645\u0646 \u0627\u0644\u0634\u0628\u0643\u0629 \u0627\u0644\u062d\u0642\u064a\u0642\u064a\u0629.\n\n\u062a\u0646\u0628\u064a\u0647: \u0627\u0644\u0645\u0624\u0634\u0631\u0627\u062a \u0627\u062d\u062a\u0645\u0627\u0644\u064a\u0629. \u0644\u0627 \u064a\u062b\u0628\u062a \u0623\u064a \u062a\u0646\u0628\u064a\u0647 \u0648\u062c\u0648\u062f \u0645\u062b\u0644 \u0647\u0630\u0647 \u0627\u0644\u0623\u062c\u0647\u0632\u0629."
                    : "An IMSI catcher (Stingray / cell-site simulator) impersonates a legitimate base station to force devices to connect to it.\n\nCapabilities vary: some capture IMSI/IMEI, some downgrade the radio to 2G. All descriptions are for educational awareness only. No Tower Guard alert constitutes proof that such a device is present."},
                {ar ? "\u0628\u0631\u0627\u0645\u062c \u0627\u0644\u0631\u0627\u062f\u064a\u0648 \u0627\u0644\u0645\u0639\u0631\u0651\u0641\u0629 \u0628\u0627\u0644\u0628\u0631\u0645\u062c\u064a\u0627\u062a (SDR)" : "Software Defined Radio (SDR)",
                 ar ? "SDR \u062c\u0647\u0627\u0632 \u0627\u0633\u062a\u0642\u0628\u0627\u0644 \u0628\u0631\u0645\u062c\u064a\u060c \u064a\u062a\u064a\u062d \u0627\u0644\u0627\u0633\u062a\u0642\u0628\u0627\u0644 \u0639\u0644\u0649 \u0646\u0637\u0627\u0642\u0627\u062a \u0648\u0627\u0633\u0639\u0629.\n\u0633\u0644\u0628\u064a: \u0628\u062f\u0648\u0646 \u0625\u0631\u0633\u0627\u0644\u060c \u063a\u064a\u0631 \u0645\u0631\u0626\u064a \u0644\u0644\u0634\u0628\u0643\u0629.\n\u0646\u0634\u0637: \u0625\u0631\u0633\u0627\u0644 \u0625\u0634\u0627\u0631\u0627\u062a\u060c \u064a\u062a\u0637\u0644\u0628 \u062a\u0631\u062e\u064a\u0635\u0627\u064b.\n\nSDR \u0623\u062f\u0627\u0629 \u0628\u062d\u062b \u0634\u0631\u0639\u064a\u0629. \u0630\u0643\u0631\u0647 \u0647\u0646\u0627 \u0644\u0623\u063a\u0631\u0627\u0636 \u062a\u0648\u0639\u0648\u064a\u0629."
                    : "SDR replaces hardware radio components with software. Passive SDR receives without transmitting \u2014 invisible to the network. Active SDR can implement rogue base stations but requires transmission licenses. SDR itself is a legitimate research tool."},
                {ar ? "\u0627\u0644\u0627\u0639\u062a\u0631\u0627\u0636 \u0627\u0644\u0633\u0644\u0628\u064a \u0645\u0642\u0627\u0628\u0644 \u0627\u0644\u0646\u0634\u0637" : "Passive vs. active interception",
                 ar ? "\u0633\u0644\u0628\u064a: \u0628\u062f\u0648\u0646 \u0625\u0631\u0633\u0627\u0644\u060c \u0635\u0639\u0628 \u0627\u0644\u0643\u0634\u0641.\n\u0646\u0634\u0637: \u064a\u062a\u0637\u0644\u0628 \u0627\u0646\u062a\u062d\u0627\u0644\u0627\u064b \u0648\u064a\u062e\u0644\u0651\u0641 \u0622\u062b\u0627\u0631\u0627\u064b: \u062a\u063a\u064a\u064a\u0631 \u0627\u0644\u062e\u0644\u064a\u0629\u060c \u062a\u0631\u0627\u062c\u0639 \u0627\u0644\u062c\u064a\u0644\u060c \u0634\u0630\u0648\u0630\u0627\u062a \u0627\u0644\u0625\u0634\u0627\u0631\u0629.\n\nTower Guard \u064a\u0631\u0635\u062f \u0645\u0624\u0634\u0631\u0627\u062a \u0627\u0644\u0627\u0639\u062a\u0631\u0627\u0636 \u0627\u0644\u0646\u0634\u0637 \u0641\u0642\u0637."
                    : "Passive: monitoring without transmitting, leaves no trace on the target device.\nActive: requires transmitting, leaves observable indicators: unexpected cell changes, generation downgrade, signal anomalies.\nTower Guard can only observe indicators consistent with active interception."},
                {ar ? "\u0645\u062e\u0627\u0637\u0631 \u062e\u0641\u0636 \u062c\u064a\u0644 \u0627\u0644\u0634\u0628\u0643\u0629" : "Network downgrade risks",
                 ar ? "\u062e\u0641\u0636 4G/5G \u0625\u0644\u0649 2G \u064a\u064f\u0636\u0639\u0651\u0641 \u0627\u0644\u062a\u0634\u0641\u064a\u0631 \u0648\u0627\u0644\u0645\u0635\u0627\u062f\u0642\u0629. \u0644\u0643\u0646 \u0623\u0633\u0628\u0627\u0628\u0627\u064b \u0645\u0634\u0631\u0648\u0639\u0629 \u0643\u062b\u064a\u0631\u0629 \u062a\u0646\u062a\u062c \u0646\u0641\u0633 \u0627\u0644\u0623\u062b\u0631: \u0636\u0639\u0641 \u0627\u0644\u062a\u063a\u0637\u064a\u0629\u060c \u0635\u064a\u0627\u0646\u0629\u060c \u062a\u062e\u0641\u064a\u0641 \u062f\u0627\u062e\u0644 \u0627\u0644\u0645\u0628\u0627\u0646\u064a. \u062f\u0631\u062c\u0629 Tower Guard \u0627\u062d\u062a\u0645\u0627\u0644\u064a\u0629."
                    : "Downgrading from 4G/5G to 2G reduces ciphering and authentication protections. Many legitimate causes produce the same result: edge-of-coverage, maintenance, indoor attenuation. Tower Guard scores this probabilistically, not conclusively."},
                {ar ? "\u0641\u0631\u0642 \u0627\u0639\u062a\u0631\u0627\u0636 \u0627\u0644\u0634\u0628\u0643\u0629 \u0648\u0628\u0631\u0627\u0645\u062c \u0627\u0644\u062a\u062c\u0633\u0633" : "Cellular interception vs. device-level spyware",
                 ar ? "\u0627\u0639\u062a\u0631\u0627\u0636 \u0627\u0644\u0634\u0628\u0643\u0629: \u064a\u0633\u062a\u0647\u062f\u0641 \u0627\u0644\u0631\u0627\u062f\u064a\u0648. E2E \u064a\u062d\u0645\u064a \u0645\u0646\u0647.\n\u0628\u0631\u0627\u0645\u062c \u0627\u0644\u062a\u062c\u0633\u0633: \u062a\u062a\u062c\u0627\u0648\u0632 \u0627\u0644\u0634\u0628\u0643\u0629\u060c \u062a\u0642\u0631\u0623 \u0627\u0644\u0628\u064a\u0627\u0646\u0627\u062a \u0645\u0628\u0627\u0634\u0631\u0629\u060c E2E \u0644\u0627 \u064a\u062d\u0645\u064a \u0645\u0646\u0647\u0627.\n\nTower Guard \u064a\u0631\u0635\u062f \u0645\u0624\u0634\u0631\u0627\u062a \u0627\u0644\u0634\u0628\u0643\u0629 \u0641\u0642\u0637. \u0644\u0627 \u064a\u0643\u062a\u0634\u0641 \u0628\u0631\u0627\u0645\u062c \u0627\u0644\u062a\u062c\u0633\u0633."
                    : "Cellular interception targets the radio link. E2E encryption protects content even if the network is compromised.\nDevice-level spyware bypasses the network layer entirely, reading data from application memory. No network-layer protection addresses this. Tower Guard monitors network indicators only."}
            });

        addSection(page, R.drawable.ic_skc_towerguard,
            ar ? "\u0643\u064a\u0641 \u064a\u0639\u0645\u0644 Tower Guard" : "How Tower Guard Works",
            new String[][]{
                {ar ? "\u0645\u0631\u0627\u0642\u0628\u0629 \u062a\u063a\u064a\u064a\u0631 \u0627\u0644\u062e\u0644\u064a\u0629 \u0627\u0644\u062e\u0627\u062f\u0645\u0629" : "Cell change monitoring",
                 ar ? "\u064a\u0633\u062c\u0651\u0644 \u0641\u064a \u0643\u0644 \u0641\u062d\u0635: \u0645\u0639\u0631\u0651\u0641 \u0627\u0644\u062e\u0644\u064a\u0629 (Cell ID)\u060c \u062c\u064a\u0644 \u0627\u0644\u0634\u0628\u0643\u0629\u060c \u0627\u0644\u0645\u0634\u063a\u0651\u0644. \u062a\u063a\u064a\u064a\u0631 \u0645\u062a\u0643\u0631\u0631 \u0641\u064a \u0645\u0648\u0642\u0639 \u062b\u0627\u0628\u062a \u0623\u0648 \u0645\u0635\u062d\u0648\u0628 \u0628\u062a\u0631\u0627\u062c\u0639 \u062c\u064a\u0644 + \u0634\u0630\u0648\u0630 \u0625\u0634\u0627\u0631\u0629 \u064a\u0631\u0641\u0639 \u0627\u0644\u062f\u0631\u062c\u0629."
                    : "Records on each scan: Cell ID, radio generation, operator. Repeated cell churn from a stationary location, or a change with simultaneous generation downgrade and signal anomaly, raises the score."},
                {ar ? "\u0627\u0646\u062a\u0642\u0627\u0644\u0627\u062a \u062c\u064a\u0644 \u0627\u0644\u0634\u0628\u0643\u0629" : "Radio generation transitions",
                 ar ? "\u064a\u062a\u0627\u0628\u0639 \u062a\u0633\u0644\u0633\u0644 \u0623\u062c\u064a\u0627\u0644 \u0627\u0644\u0634\u0628\u0643\u0629. \u062a\u0631\u0627\u062c\u0639 \u0645\u0646 4G/5G \u0625\u0644\u0649 2G: \u0623\u0639\u0644\u0649 \u0648\u0632\u0646 (+14). \u0638\u0647\u0648\u0631 2G \u0645\u062a\u0643\u0631\u0631 \u0641\u064a \u0622\u062e\u0631 5 \u0641\u062d\u0648\u0635: \u0648\u0632\u0646 \u0625\u0636\u0627\u0641\u064a (+6)."
                    : "Tracks the sequence of network generations. Downgrade to 2G: highest single-transition weight (+14). Repeated 2G presence across the last 5 scans: additional weight (+6)."},
                {ar ? "\u0634\u0630\u0648\u0630\u0627\u062a \u0627\u0644\u0625\u0634\u0627\u0631\u0629" : "Signal anomalies",
                 ar ? "\u062a\u063a\u064a\u064a\u0631 \u062d\u0627\u062f \u0641\u064a RSRP (\u0623\u0643\u062b\u0631 \u0645\u0646 25 dBm): \u0648\u0632\u0646 \u0645\u062a\u0648\u0633\u0637 (+8). \u0627\u0644\u062a\u063a\u064a\u064a\u0631 + \u062a\u063a\u064a\u064a\u0631 \u0627\u0644\u062e\u0644\u064a\u0629 \u0645\u0639\u0627\u064b: \u0648\u0632\u0646 \u0623\u0639\u0644\u0649 (+6 \u0625\u0636\u0627\u0641\u064a)."
                    : "Sharp RSRP change (> 25 dBm between scans): moderate weight (+8). Combined with cell-identity change in the same transition: additional weight (+6)."},
                {ar ? "\u062e\u0637\u0648\u0637 \u0627\u0644\u0623\u0633\u0627\u0633 \u0648\u062f\u0631\u062c\u0629 \u0627\u0644\u0634\u0630\u0648\u0630" : "Baselines and anomaly scoring",
                 ar ? "\u062d\u0641\u0638 \u0628\u0635\u0645\u0629 \u0645\u0648\u062b\u0648\u0642\u0629 \u064a\u0636\u064a\u0641 14 \u0646\u0642\u0637\u0629 \u0625\u0630\u0627 \u0644\u0645 \u062a\u062a\u0637\u0627\u0628\u0642 \u0627\u0644\u0641\u062d\u0635 \u0627\u0644\u062d\u0627\u0644\u064a. \u0645\u062c\u0645\u0648\u0639 \u0627\u0644\u0646\u0642\u0627\u0637 \u0645\u062d\u062f\u0651\u062f \u0628\u0640 100.\n\u062e\u0636\u0631\u0627\u0621 (0\u201329): \u0644\u0627 \u0645\u0624\u0634\u0631\u0627\u062a. \u0623\u0645\u0628\u0631 (30\u201364): \u062a\u0633\u062a\u062d\u0642 \u0645\u0631\u0627\u062c\u0639\u0629. \u062d\u0645\u0631\u0627\u0621 (65+): \u062a\u0636\u0627\u0641\u0631 \u0645\u0624\u0634\u0631\u0627\u062a."
                    : "A saved baseline adds 14 points if the current scan does not match it. Total capped at 100.\nGreen (0\u201329): no significant indicators. Amber (30\u201364): worth reviewing. Red (65+): multiple high-weight indicators. Score is probabilistic, not conclusive."},
                {ar ? "\u0627\u0644\u0633\u062c\u0644 \u0648\u0627\u0644\u062a\u0642\u0627\u0631\u064a\u0631" : "Local reports and history",
                 ar ? "\u0643\u0644 \u0641\u062d\u0635 \u0645\u062e\u0632\u0651\u0646 \u0645\u0634\u0641\u0651\u0631\u0627\u064b \u0628\u0640 AES-GCM + Android Keystore. \u0623\u0642\u0635\u0649 \u062a\u062e\u0632\u064a\u0646: 180 \u0641\u062d\u0635\u0627\u064b. \u062a\u0635\u062f\u064a\u0631 PDF \u0645\u062a\u0627\u062d. \u0644\u0627 \u064a\u0648\u062c\u062f \u0625\u0630\u0646 INTERNET."
                    : "Each scan stored encrypted (AES-GCM + Android Keystore). Max 180 scans. PDF export available from Reports screen. No INTERNET permission used or needed."}
            });

        addSection(page, R.drawable.ic_skc_alerts,
            ar ? "\u0641\u0647\u0645 \u0627\u0644\u062a\u0646\u0628\u064a\u0647\u0627\u062a \u0627\u0644\u0623\u0645\u0646\u064a\u0629" : "Understanding Security Alerts",
            new String[][]{
                {ar ? "\u0633\u0644\u0648\u0643 \u0637\u0628\u064a\u0639\u064a \u0645\u0642\u0627\u0628\u0644 \u0623\u0646\u0645\u0627\u0637 \u0645\u0634\u0628\u0648\u0647\u0629" : "Normal vs. suspicious patterns",
                 ar ? "\u0637\u0628\u064a\u0639\u064a: \u062a\u063a\u064a\u064a\u0631 \u062e\u0644\u064a\u0629 \u0639\u0646\u062f \u0627\u0644\u062a\u0646\u0642\u0644 \u2022 \u062a\u0631\u0627\u062c\u0639 \u0641\u064a \u0645\u0646\u0627\u0637\u0642 \u0636\u0639\u064a\u0641\u0629 \u2022 \u062a\u0642\u0644\u0628\u0627\u062a RSRP \u062f\u0627\u062e\u0644 \u0645\u0628\u0627\u0646\u064a.\n\u062c\u062f\u064a\u0631 \u0628\u0627\u0644\u0645\u0631\u0627\u062c\u0639\u0629: \u062a\u063a\u064a\u064a\u0631 \u0645\u062a\u0643\u0631\u0631 \u0641\u064a \u0645\u0648\u0642\u0639 \u062b\u0627\u0628\u062a \u2022 \u062a\u0631\u0627\u062c\u0639 2G + \u062a\u063a\u064a\u064a\u0631 \u062e\u0644\u064a\u0629 + \u0634\u0630\u0648\u0630 \u0625\u0634\u0627\u0631\u0629 \u2022 \u0627\u062e\u062a\u0641\u0627\u0621 \u062c\u0645\u064a\u0639 \u0627\u0644\u062c\u064a\u0631\u0627\u0646 \u2022 \u062a\u063a\u064a\u064a\u0631 \u0627\u0644\u0645\u0634\u063a\u0651\u0644."
                    : "Normal: cell changes while moving \u2022 2G/3G fallback in weak coverage \u2022 RSRP variation near buildings.\nWorth reviewing: repeated cell churn from stationary location \u2022 sudden 2G downgrade + cell change + signal anomaly \u2022 all neighbors disappearing simultaneously \u2022 operator name change."},
                {ar ? "\u0623\u0633\u0628\u0627\u0628 \u0645\u0634\u0631\u0648\u0639\u0629 \u0644\u0644\u0634\u0630\u0648\u0630\u0627\u062a" : "Legitimate causes of anomalies",
                 ar ? "\u2022 \u0636\u0639\u0641 \u0627\u0644\u062a\u063a\u0637\u064a\u0629: \u062a\u0630\u0628\u0630\u0628 \u0628\u064a\u0646 \u062e\u0644\u064a\u062a\u064a\u0646.\n\u2022 \u0635\u064a\u0627\u0646\u0629 \u0627\u0644\u0623\u0628\u0631\u0627\u062c: \u062a\u062f\u0646\u0651 \u0645\u0624\u0642\u062a.\n\u2022 \u0627\u0632\u062f\u062d\u0627\u0645 \u0627\u0644\u0634\u0628\u0643\u0629: \u064a\u064f\u0636\u0639\u0651\u0641 RSRQ \u0648 SINR.\n\u2022 \u0627\u0644\u062a\u0646\u0642\u0644 \u0628\u064a\u0646 \u0645\u0646\u0627\u0637\u0642 \u0627\u0644\u062a\u063a\u0637\u064a\u0629.\n\u2022 \u062a\u062e\u0641\u064a\u0641 \u0627\u0644\u0625\u0634\u0627\u0631\u0629 \u062f\u0627\u062e\u0644 \u0627\u0644\u0645\u0628\u0627\u0646\u064a.\n\u2022 \u062a\u0639\u0637\u0651\u0644 \u0628\u0631\u062c \u0648\u0627\u0646\u062a\u0642\u0627\u0644 \u0644\u0628\u0631\u062c \u0623\u0628\u0639\u062f."
                    : "\u2022 Weak coverage: oscillation between cells at boundaries.\n\u2022 Tower maintenance: temporary service degradation.\n\u2022 Network congestion: degrades RSRQ and SINR.\n\u2022 Travel between coverage areas: multiple transitions.\n\u2022 Indoor attenuation: thick walls, metallic structures.\n\u2022 Local tower failure: service falls to a distant cell."},
                {ar ? "\u062a\u062d\u0630\u064a\u0631\u0627\u062a \u0648\u0642\u064a\u0648\u062f \u0645\u0647\u0645\u0629" : "Important limitations and disclaimers",
                 ar ? "\u0644\u0627 \u064a\u0633\u062a\u0637\u064a\u0639 Tower Guard:\n\u2022 \u0625\u062b\u0628\u0627\u062a \u0648\u062c\u0648\u062f IMSI Catcher \u0628\u0634\u0643\u0644 \u0642\u0627\u0637\u0639.\n\u2022 \u0627\u0643\u062a\u0634\u0627\u0641 \u0628\u0631\u0627\u0645\u062c \u0627\u0644\u062a\u062c\u0633\u0633 \u0639\u0644\u0649 \u0645\u0633\u062a\u0648\u0649 \u0627\u0644\u062c\u0647\u0627\u0632.\n\u2022 \u0631\u0635\u062f \u0627\u0644\u0627\u0639\u062a\u0631\u0627\u0636 \u0627\u0644\u0633\u0644\u0628\u064a.\n\u2022 \u062a\u062d\u062f\u064a\u062f \u0645\u0643\u0627\u0646 \u0623\u064a \u062c\u0647\u0627\u0632 \u0645\u0634\u0628\u0648\u0647.\n\n\u0627\u0644\u062a\u0637\u0628\u064a\u0642 \u0623\u062f\u0627\u0629 \u0648\u0639\u064a \u0648\u0644\u064a\u0633 \u0646\u0638\u0627\u0645 \u0643\u0634\u0641 \u0642\u0627\u0637\u0639."
                    : "Tower Guard cannot:\n\u2022 Prove conclusively that an IMSI catcher is present.\n\u2022 Detect device-level spyware.\n\u2022 Detect passive radio monitoring.\n\u2022 Determine location of any suspicious device.\nThis application is an awareness tool, not a definitive detection system."}
            });

        addSection(page, R.drawable.ic_skc_protection,
            ar ? "\u0647\u0640 \u2014 \u062d\u0645\u0627\u064a\u0629 \u0627\u0644\u0645\u0633\u062a\u062e\u062f\u0645" : "User Protection",
            new String[][]{
                {ar ? "\u062d\u0627\u0641\u0638 \u0639\u0644\u0649 \u062a\u062d\u062f\u064a\u062b Android" : "Keep Android updated",
                 ar ? "\u062a\u062d\u062f\u064a\u062b\u0627\u062a \u0623\u0645\u0646\u064a\u0629 \u0634\u0647\u0631\u064a\u0629 \u062a\u064f\u0635\u062d\u0651\u062d \u062b\u063a\u0631\u0627\u062a \u0641\u064a \u0637\u0628\u0642\u0629 \u0627\u0644\u0627\u062a\u0635\u0627\u0644\u0627\u062a. \u062a\u0623\u062e\u0631 \u0627\u0644\u062a\u062d\u062f\u064a\u062b \u064a\u064f\u0628\u0642\u064a \u0627\u0644\u062c\u0647\u0627\u0632 \u0639\u0631\u0636\u0629 \u0644\u062b\u063a\u0631\u0627\u062a \u0645\u0639\u0644\u0646\u0629."
                    : "Monthly Android security patches address vulnerabilities in radio firmware, modem layer, and system libraries. Keeping the OS updated closes known publicly disclosed vulnerabilities."},
                {ar ? "\u062a\u0639\u0637\u064a\u0644 2G \u062d\u064a\u062b\u0645\u0627 \u0623\u0645\u0643\u0646" : "Disable 2G where supported",
                 ar ? "Android 12+: \u0627\u0644\u0625\u0639\u062f\u0627\u062f\u0627\u062a > \u0627\u0644\u0634\u0628\u0643\u0629 > \u0634\u0628\u0643\u0629 \u0627\u0644\u0647\u0627\u062a\u0641 > \u0627\u0644\u0633\u0645\u0627\u062d \u0628\u0640 2G (\u062a\u0639\u0637\u064a\u0644). \u064a\u0645\u0646\u0639 \u0623\u062d\u062f \u0623\u0633\u0627\u0644\u064a\u0628 \u0627\u0644\u062e\u0641\u0636 \u0627\u0644\u0631\u0626\u064a\u0633\u064a\u0629."
                    : "Android 12+: Settings \u2192 Network & Internet \u2192 SIMs \u2192 Allow 2G (disable). Eliminates one of the most common radio-layer downgrade attack vectors."},
                {ar ? "\u0627\u0633\u062a\u062e\u062f\u0645 \u062a\u0637\u0628\u064a\u0642\u0627\u062a E2E" : "Use end-to-end encrypted communications",
                 ar ? "E2E \u064a\u062d\u0645\u064a \u0645\u062d\u062a\u0648\u0649 \u0627\u062a\u0635\u0627\u0644\u0627\u062a\u0643 \u062d\u062a\u0649 \u0644\u0648 \u0643\u0627\u0646\u062a \u0637\u0628\u0642\u0629 \u0627\u0644\u0634\u0628\u0643\u0629 \u0645\u062e\u062a\u0631\u0642\u0629. \u0645\u0644\u0627\u062d\u0638\u0629: E2E \u064a\u062d\u0645\u064a \u0627\u0644\u0645\u062d\u062a\u0648\u0649 \u0641\u0642\u0637\u060c \u0644\u064a\u0633 \u0627\u0644\u0628\u064a\u0627\u0646\u0627\u062a \u0627\u0644\u0648\u0635\u0641\u064a\u0629."
                    : "E2E encryption protects communication content even if the radio link is compromised. Note: E2E protects content, not metadata (who communicates with whom, when)."},
                {ar ? "\u0631\u0627\u062c\u0639 \u062a\u063a\u064a\u064a\u0631\u0627\u062a \u0627\u0644\u0634\u0628\u0643\u0629 \u063a\u064a\u0631 \u0627\u0644\u0645\u062a\u0648\u0642\u0639\u0629" : "Review unexpected network changes",
                 ar ? "\u062f\u0631\u062c\u0629 \u0623\u0645\u0628\u0631/\u062d\u0645\u0631\u0627\u0621 \u0645\u062a\u0643\u0631\u0631\u0629: \u062a\u062d\u0642\u0651\u0642 \u0645\u0646 \u0623\u0633\u0628\u0627\u0628 \u0645\u0634\u0631\u0648\u0639\u0629 \u2022 \u0627\u062d\u0641\u0638 \u062e\u0637 \u0623\u0633\u0627\u0633 \u2022 \u0631\u0627\u062c\u0639 \u0627\u0644\u0633\u062c\u0644 \u0639\u0628\u0631 \u0627\u0644\u0632\u0645\u0646 \u2022 \u062a\u0648\u0627\u0635\u0644 \u0645\u0639 \u0627\u0644\u0645\u0634\u063a\u0651\u0644 \u0625\u0630\u0627 \u0627\u0633\u062a\u0645\u0631\u062a \u0627\u0644\u0623\u0646\u0645\u0627\u0637."
                    : "Repeated amber/red scores: check for legitimate explanations \u2022 save a trusted baseline \u2022 compare results over time in History \u2022 contact mobile operator if unexplained patterns persist across days."},
                {ar ? "\u062a\u062c\u0646\u0651\u0628 \u0627\u0633\u062a\u062e\u0644\u0627\u0635 \u0646\u062a\u0627\u0626\u062c \u0645\u0646 \u0642\u064a\u0627\u0633 \u0648\u0627\u062d\u062f" : "Avoid drawing conclusions from a single measurement",
                 ar ? "\u0642\u064a\u0627\u0633 \u0645\u0646\u0641\u0631\u062f \u0644\u0627 \u064a\u0639\u0646\u064a \u0648\u062c\u0648\u062f \u062a\u0647\u062f\u064a\u062f. \u0631\u0627\u0642\u0628 \u0627\u0644\u0623\u0646\u0645\u0627\u0637 \u0639\u0628\u0631 \u0627\u0644\u0632\u0645\u0646."
                    : "A single elevated scan is not meaningful in isolation. Context and patterns over time are more informative than any single reading."},
                {ar ? "\u062a\u0648\u0627\u0635\u0644 \u0645\u0639 \u0645\u0634\u063a\u0651\u0644 \u0627\u0644\u0634\u0628\u0643\u0629" : "Contact your mobile operator",
                 ar ? "\u0625\u0630\u0627 \u0627\u0633\u062a\u0645\u0631\u062a \u0627\u0644\u0623\u0646\u0645\u0627\u0637 \u0627\u0644\u0634\u0627\u0630\u0629 \u0648\u063a\u064a\u0631 \u0627\u0644\u0645\u064f\u0641\u0633\u0651\u0631\u0629\u060c \u062a\u0648\u0627\u0635\u0644 \u0645\u0639 \u0627\u0644\u0645\u0634\u063a\u0651\u0644. \u064a\u0645\u0644\u0643\u0648\u0646 \u0628\u064a\u0627\u0646\u0627\u062a \u0627\u0644\u0634\u0628\u0643\u0629 \u0627\u0644\u062d\u0642\u064a\u0642\u064a\u0629."
                    : "If anomalous patterns persist and cannot be explained, contact your mobile operator. They have access to real network-side data and can verify service status and tower configuration."}
            });

        addSection(page, R.drawable.ic_skc_references,
            ar ? "\u0645\u0631\u0627\u062c\u0639 \u0639\u0644\u0645\u064a\u0629 \u0648\u062a\u0642\u0646\u064a\u0629" : "Scientific and Technical References",
            new String[][]{
                {ar ? "\u0645\u0639\u0627\u064a\u064a\u0631 3GPP" : "3GPP Standards",
                 ar ? "3GPP: \u0647\u064a\u0626\u0629 \u0645\u0639\u0627\u064a\u064a\u0631 LTE \u0648 NR \u0627\u0644\u062f\u0648\u0644\u064a\u0629.\n\u2022 TS 36.211: \u0642\u0646\u0648\u0627\u062a LTE \u0627\u0644\u0641\u064a\u0632\u064a\u0627\u0626\u064a\u0629.\n\u2022 TS 38.211: \u0642\u0646\u0648\u0627\u062a 5G NR.\n\u2022 TS 33.401: \u0623\u0645\u0627\u0646 LTE-EPC.\n\u2022 TS 33.501: \u0623\u0645\u0627\u0646 5G.\n\u0645\u062a\u0627\u062d\u0629 \u0645\u062c\u0627\u0646\u0627\u064b \u0639\u0644\u0649 3gpp.org."
                    : "3GPP (3rd Generation Partnership Project): international standards body for LTE and NR.\n\u2022 TS 36.211: LTE physical channels\n\u2022 TS 38.211: NR (5G) physical channels\n\u2022 TS 33.401: LTE-EPC security architecture\n\u2022 TS 33.501: 5G security architecture\nAll available free at 3gpp.org."},
                {ar ? "GSMA \u0648\u062a\u0648\u062b\u064a\u0642 Android" : "GSMA and Android documentation",
                 ar ? "GSMA: FS.11 (\u062b\u063a\u0631\u0627\u062a SS7)\u060c FS.19 (\u062b\u063a\u0631\u0627\u062a Diameter).\nAndroid:\n\u2022 android.telephony.CellInfo \u2014 API \u0631\u0633\u0645\u064a\u0629.\n\u2022 android.telephony.TelephonyManager \u2014 \u062d\u0627\u0644\u0629 \u0627\u0644\u0634\u0628\u0643\u0629.\n\u0645\u062a\u0627\u062d \u0639\u0644\u0649 developer.android.com."
                    : "GSMA: FS.11 (SS7 vulnerabilities), FS.19 (Diameter security).\nAndroid docs:\n\u2022 android.telephony.CellInfo \u2014 official cell information API\n\u2022 android.telephony.TelephonyManager \u2014 network state API\nAvailable at developer.android.com."},
                {ar ? "\u0623\u0628\u062d\u0627\u062b \u0645\u0648\u062b\u0648\u0642\u0629" : "Recognized cellular security research",
                 ar ? "\u2022 Strobel (2007): 'IMSI Catcher' (Ruhr-Universit\u00e4t Bochum) \u2014 \u0648\u0631\u0642\u0629 \u0623\u0643\u0627\u062f\u064a\u0645\u064a\u0629 \u0623\u0633\u0627\u0633\u064a\u0629.\n\u2022 EFF: \u0642\u0627\u0639\u062f\u0629 \u0645\u0639\u0631\u0641\u064a\u0629 \u062d\u0648\u0644 \u0623\u062c\u0647\u0632\u0629 \u0627\u0644\u062a\u062a\u0628\u0639.\n\u2022 CISA: \u062a\u0646\u0628\u064a\u0647\u0627\u062a \u0623\u0645\u0646 \u0627\u0644\u0627\u062a\u0635\u0627\u0644\u0627\u062a.\n\u2022 NIST SP 800-187: \u0625\u0631\u0634\u0627\u062f\u0627\u062a \u0623\u0645\u0627\u0646 LTE.\n\n\u0644\u0623\u063a\u0631\u0627\u0636 \u062a\u0648\u062b\u064a\u0642\u064a\u0629 \u0641\u0642\u0637."
                    : "\u2022 Strobel (2007): 'IMSI Catcher' (Ruhr-Universit\u00e4t Bochum) \u2014 foundational academic description.\n\u2022 EFF (Electronic Frontier Foundation): knowledge base on cell-site simulator tracking.\n\u2022 CISA: advisories on critical communications security.\n\u2022 NIST SP 800-187: LTE network security guidelines.\nCited for educational attribution. Verify at original sources."}
            });

        page.addView(UiKit.gap(this, 8));
        TextView footer = UiKit.body(this, ar
            ? "\u062c\u0645\u064a\u0639 \u0627\u0644\u0645\u062d\u062a\u0648\u064a\u0627\u062a \u0644\u0644\u062a\u0648\u0639\u064a\u0629 \u0627\u0644\u062a\u0639\u0644\u064a\u0645\u064a\u0629 \u0641\u0642\u0637. \u0644\u0627 \u062a\u064f\u0645\u062b\u0651\u0644 \u0625\u0631\u0634\u0627\u062f\u0627\u062a \u0644\u0627\u0639\u062a\u0631\u0627\u0636 \u0623\u064a \u0627\u062a\u0635\u0627\u0644."
            : "All content in this section is for general educational awareness only. It does not constitute operational guidance for intercepting any communication.");
        footer.setTextColor(Color.rgb(184, 200, 216));
        footer.setTextSize(14f);
        page.addView(footer);
        page.addView(UiKit.gap(this, 12));
    }

    private void addSection(LinearLayout page, int iconRes,
                            String title, String[][] articles) {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(android.view.Gravity.CENTER_VERTICAL);
        int hp = UiKit.dp(this, 10);
        header.setPadding(hp, UiKit.dp(this, 6), hp, UiKit.dp(this, 6));
        header.setMinimumHeight(UiKit.dp(this, 48));
        header.setBackgroundResource(R.drawable.panel);

        int bSize = UiKit.dp(this, 32);
        ImageView badgeView = new ImageView(this);
        badgeView.setImageResource(iconRes);
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(bSize, bSize);
        blp.setMarginEnd(UiKit.dp(this, 10));
        badgeView.setLayoutParams(blp);
        header.addView(badgeView);

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(14f);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        header.addView(titleView);

        int chevSize = UiKit.dp(this, 20);
        ImageView arrowView = new ImageView(this);
        arrowView.setImageResource(R.drawable.ic_chevron_down);
        arrowView.setLayoutParams(new LinearLayout.LayoutParams(chevSize, chevSize));
        header.addView(arrowView);

        LinearLayout articleContainer = new LinearLayout(this);
        articleContainer.setOrientation(LinearLayout.VERTICAL);
        articleContainer.setVisibility(View.GONE);
        int cp = UiKit.dp(this, 4);
        articleContainer.setPadding(cp, 0, cp, cp);
        articleContainer.setBackgroundColor(Color.rgb(6, 6, 6));

        for (String[] article : articles) {
            addArticle(articleContainer, article[0], article[1]);
        }

        final boolean[] expanded = {false};
        header.setOnClickListener(v -> {
            expanded[0] = !expanded[0];
            articleContainer.setVisibility(expanded[0] ? View.VISIBLE : View.GONE);
            arrowView.setRotation(expanded[0] ? 180f : 0f);
        });
        header.setClickable(true);
        header.setFocusable(true);

        page.addView(header);
        page.addView(articleContainer);
        page.addView(UiKit.gap(this, 6));
    }

    private void addArticle(LinearLayout container, String articleTitle, String body) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setMinimumHeight(UiKit.dp(this, 48));
        int rp = UiKit.dp(this, 10);
        row.setPadding(rp, UiKit.dp(this, 8), rp, UiKit.dp(this, 8));

        View dot = new View(this);
        int dotSize = UiKit.dp(this, 5);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(dotSize, dotSize);
        dlp.topMargin = UiKit.dp(this, 4);
        dlp.setMarginEnd(UiKit.dp(this, 8));
        dot.setLayoutParams(dlp);
        dot.setBackgroundColor(Color.rgb(0, 176, 255));
        row.addView(dot);

        TextView titleView = new TextView(this);
        titleView.setText(articleTitle);
        titleView.setTextColor(Color.rgb(210, 225, 240));
        titleView.setTextSize(13f);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(titleView);

        int cs = UiKit.dp(this, 18);
        ImageView chevron = new ImageView(this);
        chevron.setImageResource(R.drawable.ic_chevron_right);
        chevron.setLayoutParams(new LinearLayout.LayoutParams(cs, cs));
        row.addView(chevron);

        TextView bodyView = new TextView(this);
        bodyView.setText(body);
        bodyView.setTextColor(Color.rgb(160, 180, 200));
        bodyView.setTextSize(13f);
        bodyView.setLineSpacing(0, 1.4f);
        int bp = UiKit.dp(this, 10);
        bodyView.setPaddingRelative(bp + UiKit.dp(this, 13), UiKit.dp(this, 4), bp, UiKit.dp(this, 8));
        bodyView.setVisibility(View.GONE);
        bodyView.setBackgroundColor(Color.rgb(4, 4, 4));

        final boolean[] open = {false};
        row.setOnClickListener(v -> {
            open[0] = !open[0];
            bodyView.setVisibility(open[0] ? View.VISIBLE : View.GONE);
            chevron.setImageResource(open[0] ? R.drawable.ic_chevron_down
                    : R.drawable.ic_chevron_right);
        });
        row.setClickable(true);
        row.setFocusable(true);

        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        separator.setBackgroundColor(Color.rgb(18, 18, 18));

        container.addView(row);
        container.addView(bodyView);
        container.addView(separator);
    }
}
