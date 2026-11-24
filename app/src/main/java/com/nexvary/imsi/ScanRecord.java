package com.nexvary.imsi;

import org.json.JSONObject;

public final class ScanRecord {
    public long at;
    public String operator, radio, cell, neighborSignature, reasons;
    public int neighbors, dbm, quality, risk;

    public ScanRecord(long at, String operator, String radio, String cell, int neighbors, int dbm, int quality, int risk, String neighborSignature, String reasons) {
        this.at = at;
        this.operator = operator;
        this.radio = radio;
        this.cell = cell;
        this.neighbors = neighbors;
        this.dbm = dbm;
        this.quality = quality;
        this.risk = risk;
        this.neighborSignature = neighborSignature == null ? "" : neighborSignature;
        this.reasons = reasons == null ? "" : reasons;
    }

    public JSONObject json() {
        JSONObject o = new JSONObject();
        try {
            o.put("at", at);
            o.put("operator", operator);
            o.put("radio", radio);
            o.put("cell", cell);
            o.put("neighbors", neighbors);
            o.put("dbm", dbm);
            o.put("quality", quality);
            o.put("risk", risk);
            o.put("neighborSignature", neighborSignature);
            o.put("reasons", reasons);
        } catch (Exception ignored) {}
        return o;
    }

    public static ScanRecord from(JSONObject o) {
        return new ScanRecord(
                o.optLong("at"),
                o.optString("operator"),
                o.optString("radio"),
                o.optString("cell"),
                o.optInt("neighbors"),
                o.optInt("dbm", -999),
                o.optInt("quality", -999),
                o.optInt("risk"),
                o.optString("neighborSignature", ""),
                o.optString("reasons", "")
        );
    }
}
