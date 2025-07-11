package at.bernhardangerer.speedtestclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class SpeedtestResult {
    private long timestamp;
    private Client client;
    private Server server;
    private LatencyTestResult latency;
    private TransferTestResult download;
    private TransferTestResult upload;
    private String shareUrl;

}
