package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ShareUrlServiceIT {

    @Test
    public void createShareUrl() throws MissingResultException, ServerRequestException {
        String result;
        result = ShareUrlService.createShareUrl(9720, 20.28d, 25.54d, 50.82d);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.startsWith("http://www.speedtest.net/result/"));
        Assertions.assertTrue(result.endsWith(".png"));
        Assertions.assertFalse(result.contains("null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> ShareUrlService.createShareUrl(0, 20.28d, 25.54d, 50.82d));

        result = ShareUrlService.createShareUrl(9720, 0d, 25.54d, 50.82d);
        Assertions.assertNotNull(result);

        Assertions.assertThrows(IllegalArgumentException.class, () -> ShareUrlService.createShareUrl(9720, 20.28d, 0d, 50.82d));

        Assertions.assertThrows(IllegalArgumentException.class, () -> ShareUrlService.createShareUrl(9720, 20.28d, 25.54d, 0d));
    }
}
