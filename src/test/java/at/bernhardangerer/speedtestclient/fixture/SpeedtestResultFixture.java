package at.bernhardangerer.speedtestclient.fixture;

import at.bernhardangerer.speedtestclient.model.SpeedtestResult;

import java.time.LocalDateTime;

public final class SpeedtestResultFixture {

    private SpeedtestResultFixture() {
    }

    public static SpeedtestResult create() {
        final SpeedtestResult result = new SpeedtestResult();
        result.setStartTime(LocalDateTime.of(2025, 7, 28, 18, 40, 20));
        result.setEndTime(LocalDateTime.of(2025, 7, 28, 18, 40, 50));
        result.setClient(ClientFixture.create());
        result.setServer(ServerFixture.create());
        result.setLatency(LatencyTestResultFixture.create());
        result.setDownload(TransferTestResultFixture.createDownload());
        result.setUpload(TransferTestResultFixture.createUpload());
        result.setShareUrl("http://share.url");

        return result;
    }
}
