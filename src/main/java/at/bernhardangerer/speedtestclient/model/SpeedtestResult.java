package at.bernhardangerer.speedtestclient.model;

public class SpeedtestResult {
  private long timestamp;
  private LatencyTestResult latency;
  private TransferTestResult download;
  private TransferTestResult upload;
  private String shareURL;

  public SpeedtestResult(long timestamp, LatencyTestResult latency, TransferTestResult download, TransferTestResult upload, String shareURL) {
    this.timestamp = timestamp;
    this.latency = latency;
    this.download = download;
    this.upload = upload;
    this.shareURL = shareURL;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public LatencyTestResult getLatency() {
    return latency;
  }

  public void setLatency(LatencyTestResult latency) {
    this.latency = latency;
  }

  public TransferTestResult getDownload() {
    return download;
  }

  public void setDownload(TransferTestResult download) {
    this.download = download;
  }

  public TransferTestResult getUpload() {
    return upload;
  }

  public void setUpload(TransferTestResult upload) {
    this.upload = upload;
  }

  public String getShareURL() {
    return shareURL;
  }

  public void setShareURL(String shareURL) {
    this.shareURL = shareURL;
  }

  @Override
  public String toString() {
    return "SpeedtestResult{" +
        "timestamp=" + timestamp +
        ", latency=" + latency +
        ", download=" + download +
        ", upload=" + upload +
        ", shareURL=" + shareURL +
        '}';
  }

}
