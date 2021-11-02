package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpGetClientIT {

  @Test
  public void get() throws ServerRequestException {
    byte[] result = HttpGetClient.get("https://www.speedtest.net/speedtest-servers-static.php");
    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.length > 0);
  }

  @Test
  public void partialGetDownloadData() throws ServerRequestException {
    TransferTestResult result = HttpGetClient.partialGetDownloadData("http://sp1.telekom.sk:8080/speedtest/upload.php/random350x350.jpg", 0);
    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getBytes() > 0);
    Assertions.assertTrue(result.getDurationInMs() > 0);
  }

}