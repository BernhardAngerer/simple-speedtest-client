package at.bernhardangerer.speedtestclient.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DownloadServiceTest {

  @Test
  public void generateUrls() {
  	List<String> result = DownloadService.generateUrls("http://speedtest.ban.at:8080", 4);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(40, result.size());
    Assertions.assertTrue(result.contains("http://speedtest.ban.at:8080/random1000x1000.jpg"));
  }

}	