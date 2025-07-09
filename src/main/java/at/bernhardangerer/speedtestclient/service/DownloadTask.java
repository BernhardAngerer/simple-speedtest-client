package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Callback;

import java.util.concurrent.Callable;

public final class DownloadTask implements Callable<TransferTestResult> {
    private final String url;
    private final long timeoutTime;
    private final Callback callback;

    public DownloadTask(String url, long timeoutTime, Callback callback) {
        this.url = url;
        this.timeoutTime = timeoutTime;
        this.callback = callback;
    }

    @Override
    public TransferTestResult call() throws ServerRequestException {
        final TransferTestResult result = HttpGetClient.partialGetDownloadData(url, timeoutTime);
        callback.execute();
        return result;
    }

}
