package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Callback;

import java.util.concurrent.Callable;

public final class UploadTask implements Callable<TransferTestResult> {
    private final String url;
    private final long timeoutTime;
    private final String dataString;
    private final Callback callback;

    public UploadTask(String url, long timeoutTime, String dataString, Callback callback) {
        this.url = url;
        this.timeoutTime = timeoutTime;
        this.dataString = dataString;
        this.callback = callback;
    }

    @Override
    public TransferTestResult call() throws ServerRequestException {
        final TransferTestResult result = HttpPostClient.partialPostUploadData(url, timeoutTime, dataString);
        callback.execute();
        return result;
    }

}
