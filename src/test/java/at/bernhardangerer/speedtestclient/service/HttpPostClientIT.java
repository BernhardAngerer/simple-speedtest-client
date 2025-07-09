package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class HttpPostClientIT {

    @Test
    public void postBodyWithSharedData() throws ServerRequestException {
        final String result = HttpPostClient.postBodyWithSharedData("https://www.speedtest.net/api/api.php",
                "serverid=9720&hash=0221927558e7a25bbb25891a552fbd60&ping=25&download=20&upload=50&accuracy=1");
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }
}
