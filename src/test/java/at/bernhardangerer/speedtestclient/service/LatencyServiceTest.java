package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public final class LatencyServiceTest {

    @Test
    public void calculateAverage() throws MissingResultException {
        Double result;

        result = LatencyService.calculateAverage(Arrays.asList(2L, 4L, 9L));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(5d, result);

        result = LatencyService.calculateAverage(Arrays.asList(2L, 4L, null));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3d, result);

        Assertions.assertThrows(IllegalArgumentException.class, () -> LatencyService.calculateAverage(new ArrayList<>()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> LatencyService.calculateAverage(null));
    }

}
