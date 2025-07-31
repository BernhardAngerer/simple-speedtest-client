package at.bernhardangerer.speedtestclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LatencyTestResult {

    private Double latency;
    private Double distance;

}
