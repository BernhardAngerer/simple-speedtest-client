package at.bernhardangerer.speedtestclient.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public abstract class AbstractHttpClient {
    protected static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    static HttpRequest.Builder createRequestBuilder(final URI uri) {
        if (uri != null) {
            return HttpRequest.newBuilder(uri)
                    .header("User-Agent", USER_AGENT)
                    .header("Cache-Control", "no-cache");
        } else {
            throw new IllegalArgumentException();
        }
    }
}
