package at.bernhardangerer.speedtestclient.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpPostClientTest {

    @Test
    public void partialPostUploadDataInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> HttpPostClient.partialPostUploadData(null, 0, null));
    }

    @Test
    public void postBodyWithSharedDataInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> HttpPostClient.postBodyWithSharedData(null, null));
    }

}
