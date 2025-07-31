package at.bernhardangerer.speedtestclient.controller;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public final class SpeedtestControllerIT {

    @Test
    public void runSpeedTest() throws SpeedtestException {
        final SpeedtestResult result = SpeedtestController.runSpeedTest(DistanceUnit.KILOMETER, true, true, false, false, null);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getLatency());
        Assertions.assertNotNull(result.getDownload());
        Assertions.assertNotNull(result.getUpload());
        Assertions.assertTrue(result.getStartTime().isBefore(LocalDateTime.now()));
        Assertions.assertTrue(result.getEndTime().isBefore(LocalDateTime.now()));
        Assertions.assertNull(result.getShareUrl());
    }

}
