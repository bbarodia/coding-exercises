package com.xAd.service;

import com.xAd.Constants;
import com.xAd.TokenIndex;
import com.xAd.data.Record;
import com.google.gson.Gson;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes one file from imps and clicks each to produce one result output
 */
public class ETLService implements Runnable {

    private final ConnectorService connectorService;
    private final DeviceTypeService deviceTypeService;
    private static final Logger logger = LogManager.getLogger(ETLService.class);
    private final String fileName;
    private final Gson gson = new Gson();
    private final StopWatch timer = new StopWatch();

    public ETLService(final ConnectorService connectorService, final DeviceTypeService deviceTypeService,
                      final String fileName) {
        this.connectorService = connectorService;
        this.deviceTypeService = deviceTypeService;
        this.fileName = fileName;
    }

    public void run() {
        timer.start();
        logger.info("Hour " + fileName.replace(".csv", "") + " ETL start");
        String impsPath = Constants.IMPS_PATH + "/" + fileName;
        BufferedReader impsReader, clicksReader;
        String clicksPath = Constants.CLICKS_PATH + "/" + fileName;
        String outputPathString = Constants.OUTPUT_PATH + "/" + getOutputFileName(fileName);
        try {
            impsReader = new BufferedReader(new FileReader(impsPath));
            if (Files.notExists(FileSystems.getDefault().getPath(clicksPath), LinkOption.NOFOLLOW_LINKS)) {
                logger.error("Could not find file brother at " + clicksPath);
                return;
            }
            Path outputPath = FileSystems.getDefault().getPath(outputPathString);
            if (Files.notExists(outputPath)) {
                outputPath = Files.createFile(outputPath);
            }
            clicksReader = new BufferedReader(new FileReader(clicksPath));
            String lineStr;
            int lineNum = 1;
            List<String> linesToWrite = new ArrayList<>(0);
            while ((lineStr = readFilesInParallel(impsReader, clicksReader)) != null) {
                String values[] = lineStr.split(",");
                if (values.length < TokenIndex.CLICKS_COUNT.getIndex()) {
                    logger.error("Insufficient values at line " + lineNum);
                    continue;
                }
                if (!values[TokenIndex.CLICKS_TID.getIndex()].equalsIgnoreCase(values[TokenIndex.IMPS_TID.getIndex()])) {
                    logger.error("Transactions do not match at line " + lineNum);
                    continue;
                }
                logger.trace("Read line " + lineStr);
                Record record = new Record(values[TokenIndex.IMPS_TS.getIndex()],
                                           values[TokenIndex.CLICKS_TID.getIndex()],
                                           connectorService
                                               .getConnectorById(values[TokenIndex.IMPS_CONN_TYPE.getIndex()]),
                                           deviceTypeService.getDeviceTypeById(
                                               values[TokenIndex.IMPS_DEVICE_TYPE.getIndex()]),
                                           Integer.parseInt(values[TokenIndex.IMPS_COUNT.getIndex()]),
                                           Integer.parseInt(values[TokenIndex.CLICKS_COUNT.getIndex()]));
                lineNum++;
                String json = gson.toJson(record);
                logger.trace(json);
                linesToWrite.add(json);
            }
            Files.write(outputPath, linesToWrite);
            impsReader.close();
            clicksReader.close();
            timer.stop();
            logger.info("Hour " + fileName.replace(".csv", "") + " complete, elapsed time: " + timer.getTime() /
                                                                                               1000 + "s");
        } catch (IOException ex) {
            logger.error("Hour " + fileName.replace(".csv", "") + " completed in error, elapsed time: "
                         + timer.getTime() / 1000);
            logger.error(ex.getMessage());
        }
    }

    private String getOutputFileName(final String fileName) {
        return fileName.replace(".csv", ".json");
    }

    public static String readFilesInParallel(final BufferedReader reader1, final BufferedReader reader2)
        throws IOException {
        String line1 = reader1.readLine();
        String line2 = reader2.readLine();
        if (line1 != null && line2 != null) {
            return line1 + "," + line2;
        }
        return null;
    }
}
