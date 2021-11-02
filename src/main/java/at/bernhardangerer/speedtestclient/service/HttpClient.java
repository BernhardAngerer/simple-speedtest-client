package at.bernhardangerer.speedtestclient.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

  static HttpURLConnection createConnection(URL url, String requestMethod) throws IOException {
    if (url != null && requestMethod != null) {
      HttpURLConnection conn;
      conn = (HttpURLConnection) url.openConnection();
      conn.setUseCaches(false);
      conn.setRequestMethod(requestMethod);
      conn.setRequestProperty("User-Agent", "speedtest-client");
      conn.setRequestProperty("Connection", "Keep-Alive");
      conn.setRequestProperty("Cache-Control", "no-cache");
      return conn;
    } else {
      throw new IllegalArgumentException();
    }
  }
}
