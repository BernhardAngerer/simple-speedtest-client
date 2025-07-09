package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.LatencyTestResult;
import at.bernhardangerer.speedtestclient.model.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LatencyServiceIT {

    @Test
    public void testLatency() throws ServerRequestException {
        final List<Long> result = LatencyService.testLatency("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 3);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.get(0) > 0);
    }

    @Test
    public void findServerLatencies() throws MissingResultException {
        final Map<Double, Server> servers = new HashMap<>();
        servers.put(1D, new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.6800, 17.6500,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 1, "gyor-speedtest.zt.hu:8080"));
        servers.put(2D, new Server("http://speedtest.zeg.tarr.hu:8080/speedtest/upload.php", 47.6900, 17.6600,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 2, "gyor-speedtest.zt.hu:8080"));
        servers.put(3D, new Server("http://speedtest.slovanet.sk:8080/speedtest/upload.php", 47.7000, 17.6700,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 3, "gyor-speedtest.zt.hu:8080"));
        servers.put(4D, new Server("http://speedtest.microsystem.hu:8080/speedtest/upload.php", 47.7100, 17.6800,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 4, "gyor-speedtest.zt.hu:8080"));
        servers.put(5D, new Server("http://speedtest.szerverplex.hu:8080/speedtest/upload.php", 47.7100, 17.6900,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 5, "gyor-speedtest.zt.hu:8080"));

        final Map<Server, LatencyTestResult> result = LatencyService.findServerLatencies(servers);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(5, result.size());
    }

    @Test
    public void getFastestServer() throws MissingResultException {
        final Map<Double, Server> servers = new HashMap<>();
        servers.put(1D, new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.6800, 17.6500,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 1, "gyor-speedtest.zt.hu:8080"));
        servers.put(2D, new Server("http://speedtest.zeg.tarr.hu:8080/speedtest/upload.php", 47.6900, 17.6600,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 2, "gyor-speedtest.zt.hu:8080"));
        servers.put(3D, new Server("http://speedtest.slovanet.sk:8080/speedtest/upload.php", 47.7000, 17.6700,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 3, "gyor-speedtest.zt.hu:8080"));
        servers.put(4D, new Server("http://speedtest.microsystem.hu:8080/speedtest/upload.php", 47.7100, 17.6800,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 4, "gyor-speedtest.zt.hu:8080"));
        servers.put(5D, new Server("http://speedtest.szerverplex.hu:8080/speedtest/upload.php", 47.7100, 17.6900,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 5, "gyor-speedtest.zt.hu:8080"));

        final Map.Entry<Server, LatencyTestResult> result = LatencyService.getFastestServer(servers);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getValue().getLatency());
        Assertions.assertNotNull(result.getValue().getDistance());
    }

}
