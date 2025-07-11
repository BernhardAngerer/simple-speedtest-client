package at.bernhardangerer.speedtestclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class TransferTestResult {
    private double rateInMbps;
    private int bytes;
    private long durationInMs;

    public TransferTestResult(int bytes, long durationInMs) {
        this.bytes = bytes;
        this.durationInMs = durationInMs;
    }

}
