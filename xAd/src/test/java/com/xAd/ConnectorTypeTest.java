package com.xAd;

import com.xAd.service.ConnectorService;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ConnectorTypeTest {

    @Test
    public void testConnectors() throws IOException {
        ConnectorService connectorService = ConnectorService.init(Constants.CONNECTORTYPE_PATH);
        Assert.assertEquals(connectorService.getConnectorById("1").get().getName(), "DESKTOP") ;
    }

}
