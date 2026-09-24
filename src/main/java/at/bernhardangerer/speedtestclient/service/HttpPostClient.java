package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class HttpPostClient extends AbstractHttpClient {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FORM_URLENCODED = "application/x-www-form-urlencoded";

    private HttpPostClient() {
    }

    public static TransferTestResult partialPostUploadData(final String urlString, final long timeoutTime, final String dataString)
            throws ServerRequestException {
        if (urlString != null && dataString != null) {
            final int maxBufferSize = Integer.parseInt(Objects.requireNonNull(Util.getConfigProperty("Upload.maxBufferSize")));
            int bytesSent = 0;
            try (InputStream is = new ByteArrayInputStream(dataString.getBytes(StandardCharsets.UTF_8))) {
                final HttpRequest request = createRequestBuilder(URI.create(urlString))
                        .header(CONTENT_TYPE, FORM_URLENCODED)
                        .POST(HttpRequest.BodyPublishers.ofString(dataString))
                        .build();
                final long startTime = System.currentTimeMillis();

                int bytesAvailable = is.available();
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                final byte[] buffer = new byte[bufferSize];
                int bytesRead = 1;
                while (bytesRead > 0) {
                    if (timeoutTime > 0 && System.currentTimeMillis() > timeoutTime) {
                        break;
                    }
                    bytesAvailable = is.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = is.read(buffer, 0, bufferSize);
                    if (bytesRead > 0) {
                        bytesSent = bytesSent + bytesRead;
                    }
                }
                try {
                    HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());
                } catch (IOException e) {
                    // Ignore connection closed / EOF errors if data was already sent to speedtest server
                    if (bytesSent == 0) {
                        throw e;
                    }
                }
                return new TransferTestResult(bytesSent, System.currentTimeMillis() - startTime);
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String postBodyWithSharedData(final String urlString, final String encodedBody) throws ServerRequestException {
        if (urlString != null && encodedBody != null) {
            try {
                final HttpRequest request = createRequestBuilder(URI.create(urlString))
                        .header("Referer", "http://c.speedtest.net/flash/speedtest.swf")
                        .header(CONTENT_TYPE, FORM_URLENCODED)
                        .POST(HttpRequest.BodyPublishers.ofString(encodedBody))
                        .build();
                final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                return response.body();
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
