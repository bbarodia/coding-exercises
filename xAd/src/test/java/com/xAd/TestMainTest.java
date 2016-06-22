package com.xAd;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestMainTest {

    @Test
    public void testFileRead() throws IOException {
        File path = new File("in/resources/testFile");
        List<String> lines = Arrays.asList("The first line", "The second line");
        Path file = Paths.get(path.getPath());
        path.getParentFile().mkdirs();
        Files.createFile(file);
        Files.write(file, lines, Charset.forName("UTF-8"));
        String testContents = lines.stream().collect(Collectors.joining(""));
        FileWriter fw = new FileWriter(path);
        fw.write(testContents);
        fw.flush();
        String contents = FileUtil.readEntireFile(path.getPath());
        Assert.assertEquals(contents, testContents);
        Files.delete(file);
        Files.deleteIfExists(path.getParentFile().toPath());
    }

}
