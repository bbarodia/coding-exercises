package com.xAd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {

    public static String readEntireFile(final String filePath) throws IOException {
        String input = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                input = input + line;
            }
        }
        return input;
    }



}
