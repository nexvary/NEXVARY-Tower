package com.nexvary.imsi;
public final class MeasurementPolicyTest {
    private static void check(boolean expected, long now, long recorded, long maxAge) {
        if (MeasurementPolicy.isRecent(now, recorded, maxAge) != expected) throw new AssertionError("Timestamp check failed");
    }
    public static void main(String[] args) {
        check(true, 100000, 90000, 30000);
        check(true, 100000, 70000, 30000);
        check(false, 100000, 69999, 30000);
        check(false, 100000, 100001, 30000);
        check(false, 100000, 0, 30000);
        check(false, 100000, -1, 30000);
        check(false, 100000, 99999, -1);
        check(true, 100000, 100000, 0);
        System.out.println("8 timestamp policy cases passed");
    }
}
