package com.xAd;

public enum TokenIndex {
    IMPS_TS(0),
    IMPS_TID(1),
    IMPS_CONN_TYPE(2),
    IMPS_DEVICE_TYPE(3),
    IMPS_COUNT(4),
    CLICKS_TS(5),
    CLICKS_TID(6),
    CLICKS_COUNT(7);

    private final int index;

    TokenIndex(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
