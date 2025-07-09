package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.model.UploadSetting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class UploadServiceIT {

    @Test
    public void testUpload() throws InterruptedException, MissingResultException {
        final TransferTestResult result = UploadService.testUpload("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php",
                new UploadSetting(4, 15, 4, 5), 8, () -> {
                });
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getRateInMbps() > 0.0);
        Assertions.assertTrue(result.getBytes() > 0);
        Assertions.assertTrue(result.getDurationInMs() > 0);
    }

}
