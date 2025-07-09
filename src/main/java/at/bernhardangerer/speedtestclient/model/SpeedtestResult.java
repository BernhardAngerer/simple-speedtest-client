package at.bernhardangerer.speedtestclient.model;

public final class SpeedtestResult {
    private long timestamp;
    private Client client;
    private Server server;
    private LatencyTestResult latency;
    private TransferTestResult download;
    private TransferTestResult upload;
    private String shareUrl;

    public SpeedtestResult(long timestamp, Client client, Server server, LatencyTestResult latency, TransferTestResult download,
                           TransferTestResult upload, String shareUrl) {
        this.timestamp = timestamp;
        this.client = client;
        this.server = server;
        this.latency = latency;
        this.download = download;
        this.upload = upload;
        this.shareUrl = shareUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String toString() {
        return "SpeedtestResult {"
                + "timestamp=" + timestamp
                + ", client=" + client
                + ", server=" + server
                + ", latency=" + latency
                + ", download=" + download
                + ", upload=" + upload
                + ", shareUrl='" + shareUrl + '\''
                + '}';
    }

}
