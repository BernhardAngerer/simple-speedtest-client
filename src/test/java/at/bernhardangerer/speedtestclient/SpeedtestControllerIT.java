package at.bernhardangerer.speedtestclient;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpeedtestControllerIT {

  @Test
  public void runSpeedTest() throws SpeedtestException {
    SpeedtestResult result = SpeedtestController.runSpeedTest(DistanceUnit.KILOMETER, true, true, false, false);
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getLatency());
    Assertions.assertNotNull(result.getDownload());
    Assertions.assertNotNull(result.getUpload());
    Assertions.assertTrue(result.getTimestamp() <= System.currentTimeMillis());
    Assertions.assertNull(result.getShareURL());
  }

}