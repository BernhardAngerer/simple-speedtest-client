package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@XmlRootElement(name = "speedtestResult")
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class SpeedtestResult {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Client client;
    private Server server;
    private LatencyTestResult latency;
    private TransferTestResult download;
    private TransferTestResult upload;
    private String shareUrl;

}
