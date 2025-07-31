package at.bernhardangerer.speedtestclient.fixture;

import at.bernhardangerer.speedtestclient.model.Server;

import java.util.List;

public final class ServerFixture {

    private ServerFixture() {
    }

    public static Server create() {
        return new Server(
                "http://speedtest1.example.com/speedtest",
                48.2082,
                16.3738,
                "Vienna",
                "Austria",
                "AT",
                "ExampleNet, GmbH",
                101,
                "server1.example.com"
        );
    }

    public static List<Server> createList() {
        final Server server2 = new Server(
                "http://speedtest2.example.com/speedtest",
                47.3769,
                8.5417,
                "Zurich",
                "Switzerland",
                "CH",
                "SwissISP",
                102,
                "server2.example.com"
        );

        final Server server3 = new Server(
                "http://speedtest3.example.com/speedtest",
                52.5200,
                13.4050,
                "Berlin",
                "Germany",
                "DE",
                "GermanBroadband",
                103,
                "server3.example.com"
        );

        return List.of(create(), server2, server3);
    }
}
