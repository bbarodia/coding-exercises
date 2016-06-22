package com.xAd.data;

public class ConnectorType extends Type{

    public ConnectorType(final int id, final String name) {
        super(id, name);
    }


    public static ConnectorType fromType(final Type connectorType) {
        return new ConnectorType(connectorType.getId(), connectorType.getName());
    }

    public static ConnectorType getDefault() {
        return new ConnectorType(0, "NA");
    }
}

