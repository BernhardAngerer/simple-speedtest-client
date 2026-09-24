package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public final class HttpGetClient extends AbstractHttpClient {
    private static final String INVALID_URL_STRING = "Invalid URL String";

    private HttpGetClient() {
    }

    public static TransferTestResult partialGetDownloadData(final String urlString, final long timeoutTime) throws ServerRequestException {
        if (urlString != null) {
            int bytesReceived = 0;
            try {
                final HttpRequest request = createRequestBuilder(URI.create(urlString)).GET().build();
                final long startTime = System.currentTimeMillis();
                final HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
                try (InputStream is = response.body()) {
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
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException(INVALID_URL_STRING);
        }
    }

    public static byte[] get(final String urlString) throws ServerRequestException {
        if (urlString != null) {
            try {
                final HttpRequest request = createRequestBuilder(URI.create(urlString)).GET().build();
                final HttpResponse<byte[]> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
                return response.body();
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException(INVALID_URL_STRING);
        }
    }

}
