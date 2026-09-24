package at.bernhardangerer.speedtestclient.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractHttpClient {

    static HttpURLConnection createConnection(final URL url, final String requestMethod) throws IOException {
        if (url != null && requestMethod != null) {
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            return conn;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
