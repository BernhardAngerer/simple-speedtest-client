package at.bernhardangerer.speedtestclient.util;

import at.bernhardangerer.speedtestclient.exception.UnsupportedUnitException;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class UtilTest {

    @Test
    public void calculateDistance() throws UnsupportedUnitException {
        final double result = Util.calculateDistance(40.7128, 74.0059, 51.5074, 0.1278, DistanceUnit.KILOMETER); // New York / London
        assertEquals(5570.215609963611d, result, 1d);
    }

    @Test
    public void calculateDistanceInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> Util.calculateDistance(40.7128, 74.0059, 51.5074, 0.1278, null));
    }

    @Test
    public void calculateMbps() {
        final double result = Util.calculateMbps(1000000, 1000L);
        assertEquals(8d, result);
    }

    @Test
    public void getConfigProperty() {
        final String result = Util.getConfigProperty("DistanceUnit.default");
        assertNotNull(result);
    }

    @Test
    public void getConfigPropertyInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> Util.getConfigProperty(null));
    }

    @Test
    void getQueryParams() {
        final String paramString = "hash_key_id=1ad059ffb&resultid=17959022583&date=7/10/2025&time=6:58 PM&rating=0";
        final Map<String, String> result = Util.getQueryParams(paramString);
        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.containsKey("resultid"));
        assertEquals("17959022583", result.get("resultid"));
    }

    @Test
    public void getQueryParamsInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> Util.getQueryParams(null));
    }

    @Test
    void getQueryParamsWithDuplicatedKey() {
        final String paramString = "hash_key_id=1ad059ffb&resultid=17959022583&date=7/10/2025&time=6:58 PM&rating=0&resultid=123";
        final Map<String, String> result = Util.getQueryParams(paramString);
        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.containsKey("resultid"));
        assertEquals("17959022583", result.get("resultid"));
    }
}
