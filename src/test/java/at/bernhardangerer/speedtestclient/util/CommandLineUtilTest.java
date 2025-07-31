package at.bernhardangerer.speedtestclient.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.DEDICATED_SERVER_HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.FORMAT;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.LIST_SERVER_HOSTS;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_DOWNLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_UPLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.OUTPUT_FORMAT;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.SHARE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CommandLineUtilTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testValidCommandLine() {
        final Options options = new Options();
        options.addOption("h", "dedicatedServerHost", true, "Dedicated server host to run the tests against");

        final String[] args = {"-h", "server1.example.com"};

        final CommandLine line = CommandLineUtil.getCommandLine(options, args);

        assertNotNull(line);
        assertTrue(line.hasOption("h"));
        assertEquals("server1.example.com", line.getOptionValue("h"));

        final String output = outContent.toString();
        assertEquals("", output);
    }

    @Test
    public void testInvalidCommandLine() {
        final Options options = new Options();
        options.addOption("h", "dedicatedServerHost", true, "Dedicated server host to run the tests against");

        final String[] args = {"-x", "unknown"};

        final CommandLine line = CommandLineUtil.getCommandLine(options, args);

        assertNull(line);

        final String output = outContent.toString();
        final String expectedOutput = "usage: Optional parameters:\n"
                + " -h,--dedicatedServerHost <arg>   Dedicated server host to run the tests\n"
                + "                                  against\n";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCreateOptionsContainsExpectedOptions() {
        final Options options = CommandLineUtil.createOptions();

        assertNotNull(options);
        assertEquals(6, options.getOptions().size());
        assertNotNull(options.getOption("nd"));
        assertNotNull(options.getOption("nu"));
        assertNotNull(options.getOption("s"));
        assertNotNull(options.getOption("h"));
        assertNotNull(options.getOption("l"));
        assertNotNull(options.getOption("of"));

        assertEquals(NO_DOWNLOAD, options.getOption("nd").getLongOpt());
        assertEquals(NO_UPLOAD, options.getOption("nu").getLongOpt());
        assertEquals(SHARE, options.getOption("s").getLongOpt());
        assertEquals(DEDICATED_SERVER_HOST, options.getOption("h").getLongOpt());
        assertEquals(LIST_SERVER_HOSTS, options.getOption("l").getLongOpt());
        assertEquals(OUTPUT_FORMAT, options.getOption("of").getLongOpt());

        assertEquals(HOST, options.getOption("h").getArgName());
        assertTrue(options.getOption("h").hasArg());

        assertEquals(FORMAT, options.getOption("of").getArgName());
        assertTrue(options.getOption("of").hasArg());
    }

    @Test
    void shouldPrintHelpWhenInvalidOptionGiven() {
        final String[] args = {"--unknownOption"};
        final Options options = CommandLineUtil.createOptions();
        final String expectedOutput = "usage: Optional parameters:\n"
                + " -h,--dedicatedServerHost <HOST>   Dedicated server host to run the tests\n"
                + "                                   against\n"
                + " -l,--listServerHosts              Provide a list of server hosts to run\n"
                + "                                   the tests against\n"
                + " -nd,--noDownload                  Do not perform download test\n"
                + " -nu,--noUpload                    Do not perform upload test\n"
                + " -of,--outputFormat <FORMAT>       Output format of results (default:\n"
                + "                                   console)\n"
                + "                                   Available formats:\n"
                + "                                   console — human-readable output to the\n"
                + "                                   console\n"
                + "                                   json    — machine-readable JSON format\n"
                + "                                   xml     — machine-readable XML format\n"
                + "                                   csv     — comma-separated values format\n"
                + " -s,--share                        Generate and provide an URL to the\n"
                + "                                   speedtest.net share results image\n";

        final CommandLine cmd = CommandLineUtil.getCommandLine(options, args);

        assertNull(cmd);
        final String output = outContent.toString();
        assertEquals(expectedOutput, output);
    }
}
