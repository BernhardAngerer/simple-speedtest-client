package at.bernhardangerer.speedtestclient.model;

public class LatencyTestResult {
  private Server server;
  private Double latency;
  private Double distance;

  public LatencyTestResult(Server server, Double latency, Double distance) {
    this.server = server;
    this.latency = latency;
    this.distance = distance;
  }

  public Server getServer() {
    return server;
  }

  public void setServer(Server server) {
    this.server = server;
  }

  public Double getLatency() {
    return latency;
  }

  public void setLatency(Double latency) {
    this.latency = latency;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  @Override
  public String toString() {
    return "LatencyTestResult{" +
        "server=" + server +
        ", latency=" + latency +
        ", distance=" + distance +
        '}';
  }

}
