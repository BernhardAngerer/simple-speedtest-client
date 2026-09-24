package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.LatencyTestResult;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public final class LatencyService {
    private static final Logger logger = LogManager.getLogger(LatencyService.class);
    private static final String TEST_FILE = "/latency.txt?x=";
    private static final String EXPECTED_BODY = "test=test\n";
    private static final String INVALID_SERVER_MAP = "Invalid server map";

    private LatencyService() {
    }

    public static List<Long> testLatency(final String serverUrl, final int limit) throws ServerRequestException {
        if (serverUrl != null && limit > 0) {
            final List<Long> latencies = new ArrayList<>();
            for (int iter = 0; iter < limit; iter++) {
                final String testUrl = serverUrl + TEST_FILE + System.currentTimeMillis();
                final long startTimestamp = System.currentTimeMillis();
                try {
                    final byte[] bytes = HttpGetClient.get(testUrl);
                    final long totalTime = System.currentTimeMillis() - startTimestamp;
                    if (bytes != null && new String(bytes, StandardCharsets.UTF_8).equals(EXPECTED_BODY)) {
                        latencies.add(totalTime / 2);
                    }
                } catch (ServerRequestException e) {
                    logger.warn("Latency request failed for URL {}: {}", testUrl, e.getMessage());
                }
            }
            if (!latencies.isEmpty()) {
                return latencies;
            } else {
                throw new ServerRequestException("Failed to retrieve latency results for server: " + serverUrl);
            }
        } else {
            throw new IllegalArgumentException("Invalid server URL or limit");
        }
    }

    public static Map<Server, LatencyTestResult> findServerLatencies(final Map<Double, Server> serverMap) throws MissingResultException {
        if (serverMap != null && !serverMap.isEmpty()) {
            final int testsPerServer = Integer.parseInt(Objects.requireNonNull(Util.getConfigProperty("Latency.testsPerServer.maxNumber")));
            final Map<Server, LatencyTestResult> results = new HashMap<>();
            for (final Entry<Double, Server> entry : serverMap.entrySet()) {
                if (entry != null && entry.getValue() != null) {
                    try {
                        final List<Long> latencies = testLatency(entry.getValue().getUrl(), testsPerServer);
                        results.put(entry.getValue(), new LatencyTestResult(calculateAverage(latencies), entry.getKey()));
                    } catch (ServerRequestException | MissingResultException | IllegalArgumentException e) {
                        logger.error("Error evaluating server latency for {}: {}", entry.getValue().getHost(), e.getMessage(), e);
                    }
                }
            }
            if (!results.isEmpty()) {
                return results;
            } else {
                throw new MissingResultException("Empty map for latency tests");
            }
        } else {
            throw new IllegalArgumentException(INVALID_SERVER_MAP);
        }
    }

    public static Map.Entry<Server, LatencyTestResult> getFastestServer(final Map<Double, Server> serverMap) throws MissingResultException {
        if (serverMap != null && !serverMap.isEmpty()) {
            return findServerLatencies(serverMap).entrySet().stream()
                    .min(Comparator.comparing(entry -> entry.getValue().getLatency()))
                    .orElseThrow(MissingResultException::new);
        } else {
            throw new IllegalArgumentException(INVALID_SERVER_MAP);
        }
    }

    public static double calculateAverage(final List<Long> list) throws MissingResultException {
        if (list != null && !list.isEmpty()) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .mapToLong(Long::longValue)
                    .average()
                    .orElseThrow(() -> new MissingResultException("Unable to calculate average"));
        } else {
            throw new IllegalArgumentException("Invalid List of Latency");
        }
    }
}
