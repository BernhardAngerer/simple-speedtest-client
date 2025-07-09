package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public final class HttpGetClient extends AbstractHttpClient {
    private static final String GET = "GET";

    public static TransferTestResult partialGetDownloadData(String urlString, long timeoutTime) throws ServerRequestException {
        if (urlString != null) {
            int bytesReceived = 0;
            try {
                final HttpURLConnection conn = createConnection(new URL(urlString), GET);
                final long startTime = System.currentTimeMillis();
                try (InputStream is = conn.getInputStream()) {
                    final byte[] buffer =
                            new byte[Integer.parseInt(Objects.requireNonNull(Util.getConfigProperty("Download.maxBufferSize")))];
                    int bytesRead = 1;
                    while (bytesRead > 0) {
                        if (timeoutTime > 0 && System.currentTimeMillis() > timeoutTime) {
                            break;
                        }
                        bytesRead = is.read(buffer);
                        if (bytesRead > 0) {
                            bytesReceived = bytesReceived + bytesRead;
                        }
                    }
                    return new TransferTestResult(bytesReceived, System.currentTimeMillis() - startTime);
                }
            } catch (IOException e) {
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] get(String urlString) throws ServerRequestException {
        if (urlString != null) {
            try {
                final HttpURLConnection conn = createConnection(new URL(urlString), GET);
                try (InputStream is = conn.getInputStream()) {
                    return IOUtils.toByteArray(is);
                }
            } catch (IOException e) {
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
