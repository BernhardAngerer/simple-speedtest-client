package at.bernhardangerer.speedtestclient.fixture;

import at.bernhardangerer.speedtestclient.model.Client;

public final class ClientFixture {

    private ClientFixture() {
    }

    public static Client create() {
        return new Client(
                "93.184.216.34",
                52.5200,
                13.4050,
                "ExampleISP",
                4.3,
                "DE"
        );
    }
}
