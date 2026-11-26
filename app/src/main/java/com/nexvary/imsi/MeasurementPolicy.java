package com.nexvary.imsi;
/** Timestamp validity for readings and short history comparisons. */
public final class MeasurementPolicy {
    private MeasurementPolicy() {}
    public static boolean isRecent(long now, long recorded, long maxAge) {
        return recorded > 0 && now >= recorded && maxAge >= 0 && now - recorded <= maxAge;
    }
}
