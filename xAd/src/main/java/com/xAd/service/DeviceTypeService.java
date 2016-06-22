package com.xAd.service;

import com.xAd.FileUtil;
import com.xAd.data.DeviceType;
import com.xAd.data.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeviceTypeService {

    private static Map<Integer, DeviceType> deviceTypes;
    private static DeviceTypeService deviceTypeservice;
    private static final Logger logger = LogManager.getLogger(DeviceTypeService.class);

    private DeviceTypeService(final List<Type> deviceTypes) {
        this.deviceTypes = deviceTypes.stream().collect(Collectors.toMap(Type::getId,
                                                                         deviceType -> DeviceType
                                                                             .fromType(deviceType)));
    }

    public static DeviceTypeService init(final String deviceTypePath) throws IOException {
        if (deviceTypeservice != null) {
            return DeviceTypeService.deviceTypeservice;
        }
        String fileContents = FileUtil.readEntireFile(deviceTypePath);

        deviceTypeservice = new DeviceTypeService(Type.readTypesFromJson(fileContents));
        return deviceTypeservice;
    }

    public Optional<DeviceType> getDeviceTypeById(final String value) {
        return Optional.ofNullable(deviceTypes.get(Integer.parseInt(value)));
    }
}
