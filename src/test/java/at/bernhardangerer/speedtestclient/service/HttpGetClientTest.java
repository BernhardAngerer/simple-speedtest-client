package at.bernhardangerer.speedtestclient.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpGetClientTest {

    @Test
    public void partialGetDownloadDataInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> HttpGetClient.partialGetDownloadData(null, 0));
    }

    @Test
    public void getInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> HttpGetClient.get(null));
    }

}
