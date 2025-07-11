package at.bernhardangerer.speedtestclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class LatencyTestResult {
    private Double latency;
    private Double distance;

}
