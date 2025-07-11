package at.bernhardangerer.speedtestclient.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.DEDICATED_SERVER_HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.LIST_SERVER_HOSTS;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_DOWNLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_UPLOAD;
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
        assertNotNull(options.getOption("nd"));
        assertNotNull(options.getOption("nu"));
        assertNotNull(options.getOption("s"));
        assertNotNull(options.getOption("h"));
        assertNotNull(options.getOption("l"));

        assertEquals(NO_DOWNLOAD, options.getOption("nd").getLongOpt());
        assertEquals(NO_UPLOAD, options.getOption("nu").getLongOpt());
        assertEquals(SHARE, options.getOption("s").getLongOpt());
        assertEquals(DEDICATED_SERVER_HOST, options.getOption("h").getLongOpt());
        assertEquals(LIST_SERVER_HOSTS, options.getOption("l").getLongOpt());

        assertEquals("HOST", options.getOption("h").getArgName());
        assertTrue(options.getOption("h").hasArg());
    }
}
