package com.xAd.service;

import com.xAd.FileUtil;
import com.xAd.data.ConnectorType;
import com.xAd.data.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConnectorService {

    private static Map<Integer, ConnectorType> connectorTypes;
    private static ConnectorService connectorService;
    private static final Logger logger = LogManager.getLogger(ConnectorService.class);

    private ConnectorService(final List<Type> connectorTypes) {
        this.connectorTypes = connectorTypes.stream()
            .collect(Collectors.toMap(connectorType1 -> connectorType1.getId(),
                                      connectorType -> ConnectorType.fromType(connectorType)));

    }

    public static ConnectorService init(final String connectorTypePath) throws IOException {
        if (connectorService != null) {
            return ConnectorService.connectorService;
        }
        String fileContents = FileUtil.readEntireFile(connectorTypePath);
        ConnectorService.connectorService = new ConnectorService(Type.readTypesFromJson(fileContents));
        return connectorService;
    }

    public Optional<ConnectorType> getConnectorById(final String value) {
        return Optional.ofNullable(connectorTypes.get(Integer.parseInt(value)));
    }
}
