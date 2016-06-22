package com.xAd;

import com.xAd.service.ConnectorService;
import com.xAd.service.DeviceTypeService;
import com.xAd.service.ETLService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main class to start processing files.
 */
public class xAdProcessor {

    private static final Logger logger = LogManager.getLogger(xAdProcessor.class);

    public static void main(String[] args) throws IOException {
        int threadCount = 5;
        // should ideally be in the shell script
        if (args.length != 2) {
            logger.info("Thread Count not given. Using default value " + threadCount);
        } else if (args[0].equalsIgnoreCase("-p")) {
            threadCount = Integer.parseInt(args[1]);
            logger.info("Thread Count given. Using value " + threadCount);
        } else if (args[0].equalsIgnoreCase("-h")) {
            System.out.print("usage: java -jar target/xAd-1.0-SNAPSHOT-jar-with-dependencies.jar [-p threadCount]");
            return;
        }

        checkDirectories();
        ConnectorService connectorService = ConnectorService.init(Constants.CONNECTORTYPE_PATH);
        DeviceTypeService deviceTypeService = DeviceTypeService.init(Constants.DEVICETYPE_PATH);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        getFilesToProcess().stream().forEach(file -> executorService.execute(new ETLService(connectorService,
                                                                                            deviceTypeService,
                                                                                            file)));
        executorService.shutdown();
    }

    private static void checkDirectories() throws IOException {
        Consumer<Path> supplier = (value) -> {
            throw new RuntimeException("Directory is required");
        };
        Consumer<Path> creatorFn = (path) -> {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException("Couldnt create directory.");
            }
        };
        checkDirectory(Constants.CLICKS_PATH, supplier);
        checkDirectory(Constants.IMPS_PATH, supplier);
        checkDirectory(Constants.DEVICETYPE_PATH, supplier);
        checkDirectory(Constants.DIMENSIONS_PATH, supplier);

        checkDirectory(Constants.OUTPUT_PATH, creatorFn);
    }

    private static void checkDirectory(final String directory, final Consumer<Path> action) throws IOException {
        Path directoryPath = FileSystems.getDefault().getPath(directory);
        if (Files.notExists(directoryPath)) {
            logger.debug(directory + " does not exist. Creating a new one to proceed");
            action.accept(directoryPath);
        }
    }

    public static List<String> getFilesToProcess() throws IOException {
        Stream<Path> files = Files.walk(FileSystems.getDefault().getPath(Constants.CLICKS_PATH),
                                        FileVisitOption.FOLLOW_LINKS);
        return files.filter(file -> new File(String.valueOf(file)).isFile())
            .map(filePath -> filePath.getFileName().toString()).collect(Collectors.toList());
    }
}
