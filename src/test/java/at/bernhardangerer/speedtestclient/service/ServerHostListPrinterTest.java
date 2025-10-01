package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.fixture.ServerFixture;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerHostListPrinterTest {

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
    void printJsonShouldOutputValidJsonWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printJson(result);

        final String expectedResult = """
                [ {
                  "url" : "http://speedtest1.example.com/speedtest",
                  "lat" : 48.2082,
                  "lon" : 16.3738,
                  "city" : "Vienna",
                  "country" : "Austria",
                  "isoAlpha2CountryCode" : "AT",
                  "sponsor" : "ExampleNet, GmbH",
                  "id" : 101,
                  "host" : "server1.example.com"
                }, {
                  "url" : "http://speedtest2.example.com/speedtest",
                  "lat" : 47.3769,
                  "lon" : 8.5417,
                  "city" : "Zurich",
                  "country" : "Switzerland",
                  "isoAlpha2CountryCode" : "CH",
                  "sponsor" : "SwissISP",
                  "id" : 102,
                  "host" : "server2.example.com"
                }, {
                  "url" : "http://speedtest3.example.com/speedtest",
                  "lat" : 52.52,
                  "lon" : 13.405,
                  "city" : "Berlin",
                  "country" : "Germany",
                  "isoAlpha2CountryCode" : "DE",
                  "sponsor" : "GermanBroadband",
                  "id" : 103,
                  "host" : "server3.example.com"
                } ]
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printJsonShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printJson(result);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printJsonShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printJson(null);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printXmlShouldOutputValidXmlWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printXml(result);

        final String expectedResult = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <servers>
                    <server url="http://speedtest1.example.com/speedtest" lat="48.2082" lon="16.3738" \
                name="Vienna" country="Austria" cc="AT" sponsor="ExampleNet, GmbH" id="101" \
                host="server1.example.com"/>
                    <server url="http://speedtest2.example.com/speedtest" lat="47.3769" lon="8.5417" \
                name="Zurich" country="Switzerland" cc="CH" sponsor="SwissISP" id="102" \
                host="server2.example.com"/>
                    <server url="http://speedtest3.example.com/speedtest" lat="52.52" lon="13.405" \
                name="Berlin" country="Germany" cc="DE" sponsor="GermanBroadband" id="103" \
                host="server3.example.com"/>
                </servers>
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printXmlShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printXml(result);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printXmlShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printXml(null);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printCsvShouldOutputValidCsvWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printCsv(result, Constant.COMMA);

        final String expectedResult = """
                id,sponsor,city,country,countryCode,host,url,lat,lon
                101,"ExampleNet, GmbH",Vienna,Austria,AT,server1.example.com,\
                http://speedtest1.example.com/speedtest,48.208200,16.373800
                102,SwissISP,Zurich,Switzerland,CH,server2.example.com,\
                http://speedtest2.example.com/speedtest,47.376900,8.541700
                103,GermanBroadband,Berlin,Germany,DE,server3.example.com,\
                http://speedtest3.example.com/speedtest,52.520000,13.405000
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printCsvShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printCsv(result, Constant.COMMA);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printCsvShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printCsv(null, Constant.COMMA);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printHumanReadableShouldOutputValidTextWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printHumanReadable(result);

        final String expectedResult = """
                List of valid server hosts:
                server1.example.com
                server2.example.com
                server3.example.com
                """;

        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedResult);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printHumanReadableShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printHumanReadable(result);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }

    @Test
    void printHumanReadableShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printHumanReadable(null);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        assertThat(errContent.toString()).isEqualToNormalizingNewlines("No servers available.\n");
    }
}
