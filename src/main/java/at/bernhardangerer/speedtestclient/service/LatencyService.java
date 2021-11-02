package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.LatencyTestResult;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class LatencyService {
  private final static Logger logger = LogManager.getLogger(LatencyService.class);
  private static final String TEST_FILE = "/latency.txt?x=";
  private static final String EXPECTED_BODY = "test=test\n";

  public static List<Long> testLatency(String serverUrl, int limit) throws ServerRequestException {
    if (serverUrl != null && limit > 0) {
      final List<Long> latencies = new ArrayList<>();
      for (int i = 0; i < limit; i++) {
        String testUrl = serverUrl + TEST_FILE + System.currentTimeMillis();
        long startTimestamp = System.currentTimeMillis();
        byte[] bytes = HttpGetClient.get(testUrl);
        long totalTime = System.currentTimeMillis() - startTimestamp;
        if (bytes != null && new String(bytes, StandardCharsets.UTF_8).equals(EXPECTED_BODY)) {
          latencies.add(totalTime / 2);
        }
      }
      return latencies;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static List<LatencyTestResult> findServerLatencies(Map<Double, Server> serverMap) throws MissingResultException {
    if (serverMap != null && !serverMap.isEmpty()) {
      final int testsPerServer = Integer.parseInt(Util.getConfigProperty("Latency.testsPerServer.maxNumber"));
      final List<LatencyTestResult> results = new ArrayList<>();
      for (Entry<Double, Server> entry : serverMap.entrySet()) {
        if (entry != null) {
          try {
            results.add(new LatencyTestResult(entry.getValue(), calculateAverage(
                testLatency(entry.getValue().getUrl(), testsPerServer)), entry.getKey()));
          } catch (ServerRequestException | MissingResultException e) {
            logger.error(e.getMessage(), e);
          }
        }
      }
      if (!results.isEmpty()) {
        return results;
      } else {
        throw new MissingResultException("Empty list for latency tests");
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static LatencyTestResult getFastestServer(Map<Double, Server> serverMap, DistanceUnit distanceUnit) throws MissingResultException {
    if (serverMap != null && distanceUnit != null && !serverMap.isEmpty()) {
      return findServerLatencies(serverMap).stream()
          .min(Comparator.comparing(LatencyTestResult::getLatency))
          .orElseThrow(MissingResultException::new);
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static double calculateAverage(List<Long> list) throws MissingResultException {
    if (list != null && !list.isEmpty()) {
      if (list.stream()
          .filter(Objects::nonNull)
          .mapToLong(Long::longValue)
          .average().isPresent()) {
        return list.stream()
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .average()
            .getAsDouble();
      } else {
        throw new MissingResultException("Unable to calculate average");
      }
    } else {
      throw new IllegalArgumentException();
    }
  }
}
