package com.xAd.data;

public class DeviceType extends Type{
    public DeviceType(final int id, final String name) {
        super(id, name);
    }


    public static DeviceType fromType(final Type connectorType) {
        return new DeviceType(connectorType.getId(), connectorType.getName());
    }

    public static DeviceType getDefault() {
        return new DeviceType(0, "NA");
    }
}
