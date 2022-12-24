package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class HttpPostClient extends HttpClient {
  private final static String POST = "POST";

  public static TransferTestResult partialPostUploadData(String urlString, long timeoutTime, String dataString) throws ServerRequestException {
    if (urlString != null  && dataString != null) {
      final int maxBufferSize = Integer.parseInt(Util.getConfigProperty("Upload.maxBufferSize"));
      int bytesSent = 0;
      try (final InputStream is = new ByteArrayInputStream(dataString.getBytes())) {
        final HttpURLConnection conn = createConnection(new URL(urlString), POST);
        conn.setChunkedStreamingMode(maxBufferSize);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length", Integer.toString(dataString.length()));
        final long startTime = System.currentTimeMillis();
        final DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

        int bytesAvailable = is.available();
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];
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

  public static String postBodyWithSharedData(String urlString, String encodedBody) throws ServerRequestException {
    if (urlString != null && encodedBody != null) {
      try {
        final HttpURLConnection conn = createConnection(new URL(urlString), POST);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length", Integer.toString(encodedBody.length()));
        conn.setRequestProperty("Referer", "http://c.speedtest.net/flash/speedtest.swf");
        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (final OutputStream os = conn.getOutputStream();
             final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
          writer.write(encodedBody);
          writer.flush();
          try (final InputStream is = conn.getInputStream()) {
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
