package com.xAd;

import org.testng.annotations.Test;

import java.io.IOException;

public class TestFileUtilTest {
    @Test
    public void testFileTester() throws IOException {
        FileTester.createTestData(null, null);
    }
}
