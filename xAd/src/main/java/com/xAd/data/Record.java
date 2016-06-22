package com.xAd.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class Record {

    private static TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    public Record(final String unixTs, final String transaction_id,
                  final Optional<ConnectorType> connection_type,
                  final Optional<DeviceType> device_type,
                  final int imps, final int clicks) {
        this.iso8601_timestamp = convertToISo8601(unixTs);
        this.transaction_id = transaction_id;
        this.connection_type = connection_type.orElse(ConnectorType.getDefault()).getName();
        this.device_type = device_type.orElse(DeviceType.getDefault()).getName();
        this.imps = imps;
        this.clicks = clicks;
    }

    private String convertToISo8601(final String unixTs) {
        return df.format(new Date(Long.valueOf(unixTs)));
    }

    private String iso8601_timestamp;
    private String transaction_id;
    private String connection_type;
    private String device_type;
    private int imps;
    private int clicks;

    public Record() {

    }
}
