package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.model.DownloadSetting;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DownloadServiceIT {

  @Test
  public void testDownload() throws InterruptedException, MissingResultException {
  	TransferTestResult result = DownloadService.testDownload("http://speedtest.nessus.at:8080/speedtest",
        new DownloadSetting(5, 2), () -> {});
    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getRateInMbps() > 0.0);
    Assertions.assertTrue(result.getBytes() > 0);
    Assertions.assertTrue(result.getDurationInMs() > 0);
  }

}	