package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.fixture.SpeedtestResultFixture;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

        final String expectedResult = "{\n"
                + "  \"startTime\" : \"2025-07-28T18:40:20\",\n"
                + "  \"endTime\" : \"2025-07-28T18:40:50\",\n"
                + "  \"client\" : {\n"
                + "    \"ipAddress\" : \"93.184.216.34\",\n"
                + "    \"lat\" : 52.52,\n"
                + "    \"lon\" : 13.405,\n"
                + "    \"isp\" : \"ExampleISP\",\n"
                + "    \"ispRating\" : 4.3,\n"
                + "    \"isoAlpha2CountryCode\" : \"DE\"\n"
                + "  },\n"
                + "  \"server\" : {\n"
                + "    \"url\" : \"http://speedtest1.example.com/speedtest\",\n"
                + "    \"lat\" : 48.2082,\n"
                + "    \"lon\" : 16.3738,\n"
                + "    \"city\" : \"Vienna\",\n"
                + "    \"country\" : \"Austria\",\n"
                + "    \"isoAlpha2CountryCode\" : \"AT\",\n"
                + "    \"sponsor\" : \"ExampleNet, GmbH\",\n"
                + "    \"id\" : 101,\n"
                + "    \"host\" : \"server1.example.com\"\n"
                + "  },\n"
                + "  \"latency\" : {\n"
                + "    \"latency\" : 12.3,\n"
                + "    \"distance\" : 34.7\n"
                + "  },\n"
                + "  \"download\" : {\n"
                + "    \"rateInMbps\" : 94.25,\n"
                + "    \"bytes\" : 11800000,\n"
                + "    \"durationInMs\" : 15000\n"
                + "  },\n"
                + "  \"upload\" : {\n"
                + "    \"rateInMbps\" : 26.48,\n"
                + "    \"bytes\" : 3300000,\n"
                + "    \"durationInMs\" : 10000\n"
                + "  },\n"
                + "  \"shareUrl\" : \"http://share.url\"\n"
                + "}\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printJsonShouldOutputValidJsonWhenSpeedtestResultIsSimpleObject() {
        final SpeedtestResult result = new SpeedtestResult();

        SpeedtestResultPrinter.printJson(result);

        final String expectedResult = "{\n"
                + "  \"startTime\" : null,\n"
                + "  \"endTime\" : null,\n"
                + "  \"client\" : null,\n"
                + "  \"server\" : null,\n"
                + "  \"latency\" : null,\n"
                + "  \"download\" : null,\n"
                + "  \"upload\" : null,\n"
                + "  \"shareUrl\" : null\n"
                + "}\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
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

        final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<speedtestResult>\n"
                + "    <client ip=\"93.184.216.34\" lat=\"52.52\" lon=\"13.405\" isp=\"ExampleISP\" isprating=\"4.3\" country=\"DE\"/>\n"
                + "    <download>\n"
                + "        <bytes>11800000</bytes>\n"
                + "        <durationInMs>15000</durationInMs>\n"
                + "        <rateInMbps>94.25</rateInMbps>\n"
                + "    </download>\n"
                + "    <endTime/>\n"
                + "    <latency>\n"
                + "        <distance>34.7</distance>\n"
                + "        <latency>12.3</latency>\n"
                + "    </latency>\n"
                + "    <server url=\"http://speedtest1.example.com/speedtest\" "
                + "lat=\"48.2082\" lon=\"16.3738\" name=\"Vienna\" "
                + "country=\"Austria\" cc=\"AT\" sponsor=\"ExampleNet, GmbH\" id=\"101\" host=\"server1.example.com\"/>\n"
                + "    <shareUrl>http://share.url</shareUrl>\n"
                + "    <startTime/>\n"
                + "    <upload>\n"
                + "        <bytes>3300000</bytes>\n"
                + "        <durationInMs>10000</durationInMs>\n"
                + "        <rateInMbps>26.48</rateInMbps>\n"
                + "    </upload>\n"
                + "</speedtestResult>\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printXmlShouldOutputValidXmlWhenSpeedtestResultIsSimpleObject() {
        final SpeedtestResult result = new SpeedtestResult();

        SpeedtestResultPrinter.printXml(result);

        final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<speedtestResult/>\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
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

        final String expectedResult = "startTime,endTime,clientIp,clientLat,clientLon,clientIsp,clientIspRating,clientCountry,serverId,"
                + "serverCity,serverHost,serverCountry,serverLat,serverLon,serverSponsor,serverUrl,latencyMs,distanceKm,"
                + "downloadMbps,downloadBytes,downloadDurationMs,uploadMbps,uploadBytes,uploadDurationMs,shareUrl\n"
                + "2025-07-28T18:40:20,2025-07-28T18:40:50,93.184.216.34,52.520000,13.405000,ExampleISP,4.300000,DE,101,"
                + "Vienna,server1.example.com,Austria,48.208200,16.373800,\"ExampleNet, GmbH\","
                + "http://speedtest1.example.com/speedtest,12.300000,34.700000,94.250000,11800000,15000,26.480000,"
                + "3300000,10000,http://share.url\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printCsvShouldOutputValidCsvWhenSpeedtestResultIsSimpleObject() {
        final SpeedtestResult result = new SpeedtestResult();

        SpeedtestResultPrinter.printCsv(result, Constant.COMMA);

        final String expectedResult = "startTime,endTime,clientIp,clientLat,clientLon,clientIsp,clientIspRating,clientCountry,serverId,"
                + "serverCity,serverHost,serverCountry,serverLat,serverLon,serverSponsor,serverUrl,latencyMs,distanceKm,"
                + "downloadMbps,downloadBytes,downloadDurationMs,uploadMbps,uploadBytes,uploadDurationMs,shareUrl\n"
                + ",,,,,,,,,,,,,,,,,,,,,,,,\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
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
