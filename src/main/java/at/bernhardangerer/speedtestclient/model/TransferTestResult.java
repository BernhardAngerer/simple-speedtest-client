package at.bernhardangerer.speedtestclient.model;

public class TransferTestResult {
  private double rateInMbitS;
  private int bytes;
  private long durationInMs;

  public TransferTestResult(int bytes, long durationInMs) {
    this.bytes = bytes;
    this.durationInMs = durationInMs;
  }

  public TransferTestResult(double rateInMbitS, int bytes, long durationInMs) {
    this.rateInMbitS = rateInMbitS;
    this.bytes = bytes;
    this.durationInMs = durationInMs;
  }

  public double getRateInMbitS() {
    return rateInMbitS;
  }

  public void setRateInMbitS(double rateInMbitS) {
    this.rateInMbitS = rateInMbitS;
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
        "mbitPerSecond=" + rateInMbitS +
        ", bytes=" + bytes +
        ", durationInMs=" + durationInMs +
        '}';
  }

}
