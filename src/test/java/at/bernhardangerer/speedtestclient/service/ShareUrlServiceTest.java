package at.bernhardangerer.speedtestclient.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ShareUrlServiceTest {

    @Test
    public void generateMd5Hash() {
        final String result = ShareUrlService.generateMd5Hash("25-50-20-297aae72");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("0221927558e7a25bbb25891a552fbd60", result);
    }
}
