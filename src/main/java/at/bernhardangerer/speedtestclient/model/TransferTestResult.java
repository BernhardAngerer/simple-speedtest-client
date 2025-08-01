package at.bernhardangerer.speedtestclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TransferTestResult {

    private Double rateInMbps;
    private Integer bytes;
    private Long durationInMs;

    public TransferTestResult(final Integer bytes, final Long durationInMs) {
        this.bytes = bytes;
        this.durationInMs = durationInMs;
    }

}
