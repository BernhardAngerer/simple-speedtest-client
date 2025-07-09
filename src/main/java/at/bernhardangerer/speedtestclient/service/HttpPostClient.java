package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class HttpPostClient extends AbstractHttpClient {
    public static final String CONTENT_LENGTH = "Content-Length";
    private static final String POST = "POST";

    private HttpPostClient() {
    }

    public static TransferTestResult partialPostUploadData(String urlString, long timeoutTime, String dataString)
            throws ServerRequestException {
        if (urlString != null && dataString != null) {
            final int maxBufferSize = Integer.parseInt(Objects.requireNonNull(Util.getConfigProperty("Upload.maxBufferSize")));
            int bytesSent = 0;
            try (InputStream is = new ByteArrayInputStream(dataString.getBytes())) {
                final HttpURLConnection conn = createConnection(new URL(urlString), POST);
                conn.setChunkedStreamingMode(maxBufferSize);
                conn.setDoOutput(true);
                conn.setRequestProperty(CONTENT_LENGTH, Integer.toString(dataString.length()));
                final long startTime = System.currentTimeMillis();
                final DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                int bytesAvailable = is.available();
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                final byte[] buffer = new byte[bufferSize];
                int bytesRead = 1;
                while (bytesRead > 0) {
                    if (timeoutTime > 0 && System.currentTimeMillis() > timeoutTime) {
                        break;
                    }
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = is.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = is.read(buffer, 0, bufferSize);
                    if (bytesRead > 0) {
                        bytesSent = bytesSent + bytesRead;
                    }
                }
                dos.flush();
                dos.close();
                return new TransferTestResult(bytesSent, System.currentTimeMillis() - startTime);
            } catch (IOException e) {
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("checkstyle:NestedTryDepth")
    public static String postBodyWithSharedData(String urlString, String encodedBody) throws ServerRequestException {
        if (urlString != null && encodedBody != null) {
            try {
                final HttpURLConnection conn = createConnection(new URL(urlString), POST);
                conn.setDoOutput(true);
                conn.setRequestProperty(CONTENT_LENGTH, Integer.toString(encodedBody.length()));
                conn.setRequestProperty("Referer", "http://c.speedtest.net/flash/speedtest.swf");
                conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                try (OutputStream os = conn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
                    writer.write(encodedBody);
                    writer.flush();
                    try (InputStream is = conn.getInputStream()) {
                        return IOUtils.toString(is, StandardCharsets.UTF_8);
                    }
                }
            } catch (IOException e) {
                throw new ServerRequestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
