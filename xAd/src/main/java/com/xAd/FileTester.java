package com.xAd;

import com.xAd.service.ConnectorService;
import com.xAd.service.DeviceTypeService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileTester {

    private static int startDate = 20160515;
    private static int endDate = 20161015;
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    static String clicksFormat = new String("%d,%s,%d");
    static String impsFormat = new String("%d,%s,%d,%d,%d");

    private static final Logger logger = LogManager.getLogger(FileTester.class);

    public static void createTestData(final ConnectorService connectorService,
                                      final DeviceTypeService deviceTypeService) throws IOException {
        SessionIdentifierGenerator sessionIdentifierGenerator = new SessionIdentifierGenerator();
        for (int i = startDate; i <= endDate; i++) {
            Date date = getDateFromInt(i);
            String fileName = getFileNameFromDate(date);
            Path clicksfile = Paths.get(Constants.CLICKS_PATH + "/" + fileName);
            Path impsfile = Paths.get(Constants.IMPS_PATH + "/" + fileName);

            List<String> impsLinesList = new ArrayList<>();
            List<String> clicksLinesList = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                long ts = (long) (date.getTime() + j * Math.random() * 300);
                String id = sessionIdentifierGenerator.nextSessionId();
                int clicksCount = (int) (Math.random() * 1000);
                int impsCount = (int) (Math.random() * 3000);
                int connectorType = (int) (((Math.random() * 10) % 3 + 1));
                int deviceType = (int) (((Math.random() * 10) % 5 + 1));
                String clicksLine = String.format(clicksFormat, ts, id, clicksCount);
                String impsLine = String.format(impsFormat, ts, id, connectorType, deviceType, impsCount);
                impsLinesList.add(impsLine);
                clicksLinesList.add(clicksLine);
            }
            Path clicks = clicksfile;
            if (Files.notExists(clicksfile)) {
                clicks = Files.createFile(clicksfile);
            }
            Files.write(clicks, clicksLinesList);
            Path imps = impsfile;
            if (Files.notExists(impsfile)) {
                Files.createFile(impsfile);
            }
            Files.write(imps, impsLinesList);
        }
    }

    private static String getFileNameFromDate(final Date date) {
        return df.format(date) + ".csv";
    }

    private static Date getDateFromInt(int date) {
        String dateStr = "" + date;
        try {
            Date dateObj = formatter.parse(dateStr);
            return dateObj;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

final class SessionIdentifierGenerator {

    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}