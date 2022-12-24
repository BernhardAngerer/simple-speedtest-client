package at.bernhardangerer.speedtestclient.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public final class DownloadServiceTest {

  @Test
  public void generateUrls() {
    final List<String> result = DownloadService.generateUrls("http://speedtest.ban.at:8080", 4);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(40, result.size());
    Assertions.assertTrue(result.contains("http://speedtest.ban.at:8080/random1000x1000.jpg"));
  }

}
