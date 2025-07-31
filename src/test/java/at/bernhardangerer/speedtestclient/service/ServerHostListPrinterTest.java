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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        final String expectedResult = "[ {\n"
                + "  \"url\" : \"http://speedtest1.example.com/speedtest\",\n"
                + "  \"lat\" : 48.2082,\n"
                + "  \"lon\" : 16.3738,\n"
                + "  \"city\" : \"Vienna\",\n"
                + "  \"country\" : \"Austria\",\n"
                + "  \"isoAlpha2CountryCode\" : \"AT\",\n"
                + "  \"sponsor\" : \"ExampleNet, GmbH\",\n"
                + "  \"id\" : 101,\n"
                + "  \"host\" : \"server1.example.com\"\n"
                + "}, {\n"
                + "  \"url\" : \"http://speedtest2.example.com/speedtest\",\n"
                + "  \"lat\" : 47.3769,\n"
                + "  \"lon\" : 8.5417,\n"
                + "  \"city\" : \"Zurich\",\n"
                + "  \"country\" : \"Switzerland\",\n"
                + "  \"isoAlpha2CountryCode\" : \"CH\",\n"
                + "  \"sponsor\" : \"SwissISP\",\n"
                + "  \"id\" : 102,\n"
                + "  \"host\" : \"server2.example.com\"\n"
                + "}, {\n"
                + "  \"url\" : \"http://speedtest3.example.com/speedtest\",\n"
                + "  \"lat\" : 52.52,\n"
                + "  \"lon\" : 13.405,\n"
                + "  \"city\" : \"Berlin\",\n"
                + "  \"country\" : \"Germany\",\n"
                + "  \"isoAlpha2CountryCode\" : \"DE\",\n"
                + "  \"sponsor\" : \"GermanBroadband\",\n"
                + "  \"id\" : 103,\n"
                + "  \"host\" : \"server3.example.com\"\n"
                + "} ]\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printJsonShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printJson(result);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printJsonShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printJson(null);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printXmlShouldOutputValidXmlWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printXml(result);

        final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<servers>\n"
                + "    <server url=\"http://speedtest1.example.com/speedtest\" lat=\"48.2082\" lon=\"16.3738\" "
                + "name=\"Vienna\" country=\"Austria\" cc=\"AT\" sponsor=\"ExampleNet, GmbH\" id=\"101\" "
                + "host=\"server1.example.com\"/>\n"
                + "    <server url=\"http://speedtest2.example.com/speedtest\" lat=\"47.3769\" lon=\"8.5417\" "
                + "name=\"Zurich\" country=\"Switzerland\" cc=\"CH\" sponsor=\"SwissISP\" id=\"102\" "
                + "host=\"server2.example.com\"/>\n"
                + "    <server url=\"http://speedtest3.example.com/speedtest\" lat=\"52.52\" lon=\"13.405\" "
                + "name=\"Berlin\" country=\"Germany\" cc=\"DE\" sponsor=\"GermanBroadband\" id=\"103\" "
                + "host=\"server3.example.com\"/>\n"
                + "</servers>\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printXmlShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printXml(result);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printXmlShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printXml(null);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printCsvShouldOutputValidCsvWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printCsv(result, Constant.COMMA);

        final String expectedResult = "id,sponsor,city,country,countryCode,host,url,lat,lon\n"
                + "101,\"ExampleNet, GmbH\",Vienna,Austria,AT,server1.example.com,"
                + "http://speedtest1.example.com/speedtest,48.208200,16.373800\n"
                + "102,SwissISP,Zurich,Switzerland,CH,server2.example.com,"
                + "http://speedtest2.example.com/speedtest,47.376900,8.541700\n"
                + "103,GermanBroadband,Berlin,Germany,DE,server3.example.com,"
                + "http://speedtest3.example.com/speedtest,52.520000,13.405000\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printCsvShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printCsv(result, Constant.COMMA);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printCsvShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printCsv(null, Constant.COMMA);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printHumanReadableShouldOutputValidTextWhenServerList() {
        final List<Server> result = ServerFixture.createList();

        ServerHostListPrinter.printHumanReadable(result);

        final String expectedResult = "List of valid server hosts:\n"
                + "server1.example.com\n"
                + "server2.example.com\n"
                + "server3.example.com\n";

        final String output = outContent.toString();
        assertEquals(expectedResult, output);
        final String error = errContent.toString();
        assertTrue(error.isEmpty());
    }

    @Test
    void printHumanReadableShouldNotPrintWhenServerListIsEmpty() {
        final List<Server> result = Collections.emptyList();

        ServerHostListPrinter.printHumanReadable(result);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }

    @Test
    void printHumanReadableShouldNotPrintWhenServerListIsNull() {
        ServerHostListPrinter.printHumanReadable(null);

        final String output = outContent.toString();
        assertTrue(output.isEmpty());
        final String error = errContent.toString();
        assertEquals("No servers available.\n", error);
    }
}
