package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.model.DownloadSetting;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class DownloadService extends AbstractTransferService {
    private static final int[] SIZES = new int[]{350, 500, 750, 1000, 1500, 2000, 2500, 3000, 3500, 4000};

    public static TransferTestResult testDownload(String serverUrl, DownloadSetting settings, Callback callback)
            throws InterruptedException, MissingResultException {
        if (serverUrl != null && settings != null && callback != null) {
            final List<String> urls = generateUrls(serverUrl, settings.getThreadsPerUrl());
            final long timeoutTime = System.currentTimeMillis() + settings.getTestLength() * 1000L;
            final List<Callable<TransferTestResult>> callables = new ArrayList<>();
            for (String url : urls) {
                callables.add(new DownloadTask(url, timeoutTime, callback));
            }
            return testTransfer(callables, settings.getThreadsPerUrl() * 2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    static List<String> generateUrls(String serverUrl, int threadsPerUrl) {
        if (serverUrl != null && threadsPerUrl > 0) {
            final List<String> urls = new ArrayList<>();
            for (int size : SIZES) {
                for (int iter = 0; iter < threadsPerUrl; iter++) {
                    urls.add(String.format("%s/random%sx%s.jpg", serverUrl, size, size));
                }
            }
            return urls;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
