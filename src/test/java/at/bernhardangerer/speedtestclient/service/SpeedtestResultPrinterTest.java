package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.fixture.SpeedtestResultFixture;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpeedtestResultPrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void printJsonShouldOutputValidJsonWhenSpeedtestResult() {
        final SpeedtestResult result = SpeedtestResultFixture.create();

        SpeedtestResultPrinter.printJson(result);

        final String expectedResult = """
                {
                  "startTime" : "2025-07-28T18:40:20",
                  "endTime" : "2025-07-28T18:40:50",
                  "client" : {
                    "ipAddress" : "93.184.216.34",
                    "lat" : 52.52,
                    "lon" : 13.405,
                    "isp" : "ExampleISP",
                    "ispRating" : 4.3,
                    "isoAlpha2CountryCode" : "DE"
                  },
                  "server" : {
                    "url" : "http://speedtest1.example.com/speedtest",
                    "lat" : 48.2082,
                    "lon" : 16.3738,
                    "city" : "Vienna",
                    "country" : "Austria",
                    "isoAlpha2CountryCode" : "AT",
                    "sponsor" : "ExampleNet, GmbH",
                    "id" : 101,
                    "host" : "server1.example.com"
                  },
                  "latency" : {
                    "latency" : 12.3,
                    "distance" : 34.7
                  },
                  "download" : {
                    "rateInMbps" : 94.25,
                    "bytes" : 11800000,
                    "durationInMs" : 15000
                  },
                  "upload" : {
                    "rateInMbps" : 26.48,
                    "bytes" : 3300000,
                    "durationInMs" : 10000
                  },
                  "shareUrl" : "http://share.url"
                }
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printJsonShouldOutputValidJsonWhenSpeedtestResultIsSimpleObject() {
        final SpeedtestResult result = new SpeedtestResult();

        SpeedtestResultPrinter.printJson(result);

        final String expectedResult = """
                {
                  "startTime" : null,
                  "endTime" : null,
                  "client" : null,
                  "server" : null,
                  "latency" : null,
                  "download" : null,
                  "upload" : null,
                  "shareUrl" : null
                }
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printJsonShouldNotPrintWhenResultIsNull() {
        SpeedtestResultPrinter.printJson(null);

        final String output = outContent.toString();
        assertEquals("", output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printXmlShouldOutputValidXmlWhenSpeedtestResult() {
        final SpeedtestResult result = SpeedtestResultFixture.create();

        SpeedtestResultPrinter.printXml(result);

        final String expectedResult = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <speedtestResult>
                    <client ip="93.184.216.34" lat="52.52" lon="13.405" isp="ExampleISP" isprating="4.3" country="DE"/>
                    <download>
                        <bytes>11800000</bytes>
                        <durationInMs>15000</durationInMs>
                        <rateInMbps>94.25</rateInMbps>
                    </download>
                    <endTime/>
                    <latency>
                        <distance>34.7</distance>
                        <latency>12.3</latency>
                    </latency>
                    <server url="http://speedtest1.example.com/speedtest" \
                lat="48.2082" lon="16.3738" name="Vienna" \
                country="Austria" cc="AT" sponsor="ExampleNet, GmbH" id="101" host="server1.example.com"/>
                    <shareUrl>http://share.url</shareUrl>
                    <startTime/>
                    <upload>
                        <bytes>3300000</bytes>
                        <durationInMs>10000</durationInMs>
                        <rateInMbps>26.48</rateInMbps>
                    </upload>
                </speedtestResult>
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printXmlShouldOutputValidXmlWhenSpeedtestResultIsSimpleObject() {
        final SpeedtestResult result = new SpeedtestResult();

        SpeedtestResultPrinter.printXml(result);

        final String expectedResult = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <speedtestResult/>
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printXmlShouldNotPrintWhenResultIsNull() {
        SpeedtestResultPrinter.printXml(null);

        final String output = outContent.toString();
        assertEquals("", output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printCsvShouldOutputValidCsvWhenSpeedtestResult() {
        final SpeedtestResult result = SpeedtestResultFixture.create();

        SpeedtestResultPrinter.printCsv(result, Constant.COMMA);

        final String expectedResult = """
                startTime,endTime,clientIp,clientLat,clientLon,clientIsp,clientIspRating,clientCountry,serverId,\
                serverCity,serverHost,serverCountry,serverLat,serverLon,serverSponsor,serverUrl,latencyMs,distanceKm,\
                downloadMbps,downloadBytes,downloadDurationMs,uploadMbps,uploadBytes,uploadDurationMs,shareUrl
                2025-07-28T18:40:20,2025-07-28T18:40:50,93.184.216.34,52.520000,13.405000,ExampleISP,4.300000,DE,101,\
                Vienna,server1.example.com,Austria,48.208200,16.373800,"ExampleNet, GmbH",\
                http://speedtest1.example.com/speedtest,12.300000,34.700000,94.250000,11800000,15000,26.480000,\
                3300000,10000,http://share.url
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printCsvShouldOutputValidCsvWhenSpeedtestResultIsSimpleObject() {
        final SpeedtestResult result = new SpeedtestResult();

        SpeedtestResultPrinter.printCsv(result, Constant.COMMA);

        final String expectedResult = """
                startTime,endTime,clientIp,clientLat,clientLon,clientIsp,clientIspRating,clientCountry,serverId,\
                serverCity,serverHost,serverCountry,serverLat,serverLon,serverSponsor,serverUrl,latencyMs,distanceKm,\
                downloadMbps,downloadBytes,downloadDurationMs,uploadMbps,uploadBytes,uploadDurationMs,shareUrl
                ,,,,,,,,,,,,,,,,,,,,,,,,
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printCsvShouldNotPrintWhenResultIsNull() {
        SpeedtestResultPrinter.printCsv(null, Constant.COMMA);

        final String output = outContent.toString();
        assertEquals("", output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

}
