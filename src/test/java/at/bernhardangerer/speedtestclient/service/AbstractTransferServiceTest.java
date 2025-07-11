package at.bernhardangerer.speedtestclient.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractTransferServiceTest {

    @Test
    public void testTransferInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> AbstractTransferService.testTransfer(null, 0));
    }

}
