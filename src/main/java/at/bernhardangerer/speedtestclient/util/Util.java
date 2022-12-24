package at.bernhardangerer.speedtestclient.util;

import at.bernhardangerer.speedtestclient.exception.UnsupportedUnitException;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Util {
  private static final Logger logger = LogManager.getLogger(Util.class);

  public static double calculateDistance(double lat1, double lon1, double lat2, double lon2, DistanceUnit distanceUnit) throws UnsupportedUnitException {
    if (distanceUnit != null) {
      if ((lat1 == lat2) && (lon1 == lon2)) {
        return 0d;
      } else {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515; // miles
        switch (distanceUnit) {
          case MILE:
            return dist;
          case KILOMETER:
            return dist * 1.609344;
          case NAUTICAL_MILE:
            return dist * 0.8684;
          default: throw new UnsupportedUnitException("DistanceUnit " + distanceUnit.name() + " not supported!");
        }
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static void printDot() {
    System.out.print(".");
  }

  public static Map<String, String> getQueryParams(String paramString) throws UnsupportedEncodingException {
    if (paramString != null) {
      Map<String, String> params = new HashMap<>();
      for (String param : paramString.split("&")) {
        String[] pair = param.split("=");
        String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name());
        String value = "";
        if (pair.length > 1) {
          value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name());
        }
        params.put(key, value);
      }
      return params;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static double calculateMbps(int bytes, long timeInMs) {
    return (bytes * 8.0) / (timeInMs * 1000.0);
  }

  public static String getConfigProperty(String key) {
    if (key != null) {
      final String resource = "config.properties";
      try (final InputStream is = Util.class.getClassLoader().getResourceAsStream(resource)) {
        Properties prop = new Properties();
        prop.load(is);
        return prop.getProperty(key);
      } catch (Exception e) {
        logger.error("Loading key \"" + key + "\" from property file \"" + resource + "\" was not possible!", e);
        return null;
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

}
