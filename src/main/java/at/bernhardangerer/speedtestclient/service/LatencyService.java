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

public final class LatencyService {
  private static final Logger logger = LogManager.getLogger(LatencyService.class);
  private static final String TEST_FILE = "/latency.txt?x=";
  private static final String EXPECTED_BODY = "test=test\n";

  public static List<Long> testLatency(String serverUrl, int limit) throws ServerRequestException {
    if (serverUrl != null && limit > 0) {
      final List<Long> latencies = new ArrayList<>();
      for (int i = 0; i < limit; i++) {
        final String testUrl = serverUrl + TEST_FILE + System.currentTimeMillis();
        final long startTimestamp = System.currentTimeMillis();
        final byte[] bytes = HttpGetClient.get(testUrl);
        final long totalTime = System.currentTimeMillis() - startTimestamp;
        if (bytes != null && new String(bytes, StandardCharsets.UTF_8).equals(EXPECTED_BODY)) {
          latencies.add(totalTime / 2);
        }
      }
      return latencies;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static Map<Server, LatencyTestResult> findServerLatencies(Map<Double, Server> serverMap) throws MissingResultException {
    if (serverMap != null && !serverMap.isEmpty()) {
      final int testsPerServer = Integer.parseInt(Util.getConfigProperty("Latency.testsPerServer.maxNumber"));
      final Map<Server, LatencyTestResult> results = new HashMap<>();
      for (final Entry<Double, Server> entry : serverMap.entrySet()) {
        if (entry != null) {
          try {
            results.put(entry.getValue(), new LatencyTestResult(calculateAverage(
                testLatency(entry.getValue().getUrl(), testsPerServer)), entry.getKey()));
          } catch (ServerRequestException | MissingResultException e) {
            logger.error(e.getMessage(), e);
          }
        }
      }
      if (!results.isEmpty()) {
        return results;
      } else {
        throw new MissingResultException("Empty map for latency tests");
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static Map.Entry<Server, LatencyTestResult> getFastestServer(Map<Double, Server> serverMap, DistanceUnit distanceUnit) throws MissingResultException {
    if (serverMap != null && distanceUnit != null && !serverMap.isEmpty()) {
      return findServerLatencies(serverMap).entrySet().stream()
          .min(Comparator.comparing(o -> o.getValue().getLatency()))
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
