package at.bernhardangerer.speedtestclient.model;

public class TransferTestResult {
  private double rateInMbps;
  private int bytes;
  private long durationInMs;

  public TransferTestResult(int bytes, long durationInMs) {
    this.bytes = bytes;
    this.durationInMs = durationInMs;
  }

  public TransferTestResult(double rateInMbps, int bytes, long durationInMs) {
    this.rateInMbps = rateInMbps;
    this.bytes = bytes;
    this.durationInMs = durationInMs;
  }

  public double getRateInMbps() {
    return rateInMbps;
  }

  public void setRateInMbps(double rateInMbps) {
    this.rateInMbps = rateInMbps;
  }

  public int getBytes() {
    return bytes;
  }

  public void setBytes(int bytes) {
    this.bytes = bytes;
  }

  public long getDurationInMs() {
    return durationInMs;
  }

  public void setDurationInMs(long durationInMs) {
    this.durationInMs = durationInMs;
  }

  @Override
  public String toString() {
    return "TransferTestResult{" +
        "rateInMbps=" + rateInMbps +
        ", bytes=" + bytes +
        ", durationInMs=" + durationInMs +
        '}';
  }

}
