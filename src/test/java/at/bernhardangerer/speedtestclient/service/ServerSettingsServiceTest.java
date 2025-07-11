package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ServerSettingsServiceTest {

    @Test
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    public void getServersFromXml() throws ParsingException, MissingResultException {
        final List<Server> result = ServerSettingsService.getServersFromXml(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<settings>\n"
                + "<servers>\n"
                + "<server url=\"http://gyor-speedtest.zt.hu:8080/speedtest/upload.php\" lat=\"47.6875\" lon=\"17.6504\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"9720\"  host=\"gyor-speedtest.zt.hu:8080\" />\n"
                + "<server url=\"http://speedtest.zeg.tarr.hu:8080/speedtest/upload.php\" lat=\"46.8417\" lon=\"16.8416\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"8384\"  host=\"speedtest.zeg.tarr.hu:8080\" />\n"
                + "<server url=\"http://speedtest.jztkft.hu:8080/speedtest/upload.php\" lat=\"47.4979\" lon=\"19.0402\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"24446\"  host=\"speedtest.jztkft.hu:8080\" />\n"
                + "<server url=\"http://speedtest.microsystem.hu:8080/speedtest/upload.php\" lat=\"47.5000\" lon=\"19.0500\" "
                + "name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"36406\"  host=\"speedtest.microsystem.hu:8080\" />\n"
                + "<server url=\"http://speedtest.szerverplex.hu:8080/speedtest/upload.php\" lat=\"47.5000\" lon=\"19.0500\" "
                + "name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"4246\"  host=\"speedtest.szerverplex.hu:8080\" />\n"
                + "<server url=\"http://speedtest2.opcnet.hu:8080/speedtest/upload.php\" lat=\"47.5000\" lon=\"19.0500\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"22794\"  host=\"speedtest2.opcnet.hu:8080\" />\n"
                + "<server url=\"http://vh1.speedtest.einfra.hu:8080/speedtest/upload.php\" lat=\"47.5000\" lon=\"19.0500\" "
                + "name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"41561\"  host=\"vh1.speedtest.einfra.hu:8080\" />\n"
                + "<server url=\"http://nk-speedtest.zt.hu:8080/speedtest/upload.php\" lat=\"46.4500\" lon=\"16.9833\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"9743\"  host=\"nk-speedtest.zt.hu:8080\" />\n"
                + "<server url=\"http://speedtest.szd.tarr.hu:8080/speedtest/upload.php\" lat=\"46.3560\" lon=\"18.7038\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"4099\"  host=\"speedtest.szd.tarr.hu:8080\" />\n"
                + "<server url=\"http://pecs-speedtest.zt.hu:8080/speedtest/upload.php\" lat=\"46.0727\" lon=\"18.2323\" name=\"a city\" "
                + "country=\"Hungary\" cc=\"HU\" sponsor=\"a sponsor\" id=\"9742\"  host=\"pecs-speedtest.zt.hu:8080\" />\n"
                + "</servers>\n"
                + "</settings>").getBytes());
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(10, result.size());
    }

    @Test
    public void getServersFromXmlInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> ServerSettingsService.getServersFromXml(null));
    }

    @Test
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    public void findClosestServers() throws MissingResultException {
        Map<Double, Server> result;
        final List<Server> serverList = new ArrayList<>();
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.6800, 17.6500,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 1, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.6900, 17.6600,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 2, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7000, 17.6700,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 3, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7100, 17.6800,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 4, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7100, 17.6900,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 5, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7200, 17.7000,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 6, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7300, 17.7100,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 7, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7400, 17.7200,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 8, "gyor-speedtest.zt.hu:8080"));
        serverList.add(new Server("http://gyor-speedtest.zt.hu:8080/speedtest/upload.php", 47.7500, 17.7300,
                "Gyor", "Hungary", "HU", "ZNET Telekom Zrt.", 9, "gyor-speedtest.zt.hu:8080"));

        result = ServerSettingsService.findClosestServers(47.6700, 17.6400, 5, DistanceUnit.KILOMETER, serverList);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(5, result.size());
        Assertions.assertTrue(result.values().stream().allMatch(server -> server.getId() >= 1 && server.getId() <= 5));

        result = ServerSettingsService.findClosestServers(47.7500, 17.7300, 3, DistanceUnit.KILOMETER, serverList);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.values().stream().allMatch(server -> server.getId() >= 7 && server.getId() <= 9));
    }

    @Test
    public void findClosestServersInvalidParameter() {
        assertThrows(IllegalArgumentException.class,
                () -> ServerSettingsService.findClosestServers(47.6700, 17.6400, 5, DistanceUnit.KILOMETER, null));
    }

    @Test
    public void requestServerListInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> ServerSettingsService.requestServerList(0));
    }

}
