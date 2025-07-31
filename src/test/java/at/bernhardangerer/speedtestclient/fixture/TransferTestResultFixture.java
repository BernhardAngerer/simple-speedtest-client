package at.bernhardangerer.speedtestclient.fixture;

import at.bernhardangerer.speedtestclient.model.TransferTestResult;

public final class TransferTestResultFixture {

    private TransferTestResultFixture() {
    }

    public static TransferTestResult createDownload() {
        return new TransferTestResult(94.25, 11800000, 15000L);
    }

    public static TransferTestResult createUpload() {
        return new TransferTestResult(26.48, 3300000, 10000L);
    }
}
