package at.bernhardangerer.speedtestclient.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.DEDICATED_SERVER_HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.FORMAT;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.LIST_SERVER_HOSTS;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_DOWNLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_UPLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.OUTPUT_FORMAT;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.SHARE;
import static org.assertj.core.api.Assertions.assertThat;
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
    public void testValidCommandLine() throws IOException {
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
    public void testInvalidCommandLine() throws IOException {
        final Options options = new Options();
        options.addOption("h", "dedicatedServerHost", true, "Dedicated server host to run the tests against");

        final String[] args = {"-x", "unknown"};

        final CommandLine line = CommandLineUtil.getCommandLine(options, args);

        assertNull(line);

        final String expectedOutput = """
                 usage:  Optional parameters:

                             Options                              Description             \s
                 -h, --dedicatedServerHost <arg>     Dedicated server host to run the tests
                                                      against                             \s

                """;
        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
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
    void shouldPrintHelpWhenInvalidOptionGiven() throws IOException {
        final String[] args = {"--unknownOption"};
        final Options options = CommandLineUtil.createOptions();
        final String expectedOutput = """
                 usage:  Optional parameters:

                             Options                               Description            \s
                 -nd, --noDownload                    Do not perform download test        \s
                 -nu, --noUpload                      Do not perform upload test          \s
                 -s, --share                          Generate and provide an URL to the  \s
                                                       speedtest.net share results image  \s
                 -h, --dedicatedServerHost <HOST>     Dedicated server host to run the    \s
                                                       tests against                      \s
                 -l, --listServerHosts                Provide a list of server hosts to run
                                                       the tests against                  \s
                 -of, --outputFormat <FORMAT>         Output format of results (default:  \s
                                                       console)                           \s
                                                       Available formats:                 \s
                                                       console — human-readable output to \s
                                                       the console                        \s
                                                       json    — machine-readable JSON    \s
                                                       format                             \s
                                                       xml     — machine-readable XML     \s
                                                       format                             \s
                                                       csv     — comma-separated values   \s
                                                       format                             \s

                """;

        final CommandLine cmd = CommandLineUtil.getCommandLine(options, args);

        assertNull(cmd);
        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
    }
}
