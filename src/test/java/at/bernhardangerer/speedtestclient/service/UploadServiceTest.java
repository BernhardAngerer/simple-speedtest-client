package at.bernhardangerer.speedtestclient.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class UploadServiceTest {

    @Test
    public void generateDataString() {
        final String result = UploadService.generateDataString(32768);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(32768, result.length());
        Assertions.assertTrue(result.startsWith("content1=0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

}
