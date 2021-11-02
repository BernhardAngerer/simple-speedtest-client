package at.bernhardangerer.speedtestclient.util;

import at.bernhardangerer.speedtestclient.exception.UnsupportedUnitException;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

  @Test
  public void calculateDistance() throws UnsupportedUnitException {
    double result = Util.calculateDistance(40.7128, 74.0059, 51.5074, 0.1278, DistanceUnit.KILOMETER); // New York / London
    Assertions.assertEquals(5570.215609963611d, result, 1d);
  }

  @Test
  public void calculateMbitsPerSecond() {
    double result = Util.calculateMbitsPerSecond(1000000, 1000L);
    Assertions.assertEquals(8d, result);
  }

  @Test
  public void getConfigProperty() {
    String result = Util.getConfigProperty("DistanceUnit.default");
    Assertions.assertNotNull(result);
  }

}