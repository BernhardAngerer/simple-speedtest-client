package at.bernhardangerer.speedtestclient.model;

public final class LatencyTestResult {
    private Double latency;
    private Double distance;

    public LatencyTestResult(Double latency, Double distance) {
        this.latency = latency;
        this.distance = distance;
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
        return "LatencyTestResult {"
                + ", latency=" + latency
                + ", distance=" + distance
                + '}';
    }

}
