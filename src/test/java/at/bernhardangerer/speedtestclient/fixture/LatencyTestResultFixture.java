package at.bernhardangerer.speedtestclient.fixture;

import at.bernhardangerer.speedtestclient.model.LatencyTestResult;

public final class LatencyTestResultFixture {

    private LatencyTestResultFixture() {
    }

    public static LatencyTestResult create() {
        return new LatencyTestResult(12.3, 34.7);
    }
}
