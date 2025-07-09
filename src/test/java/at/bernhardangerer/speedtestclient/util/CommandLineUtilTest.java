package at.bernhardangerer.speedtestclient.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testValidCommandLine() {
        final Options options = new Options();
        options.addOption("f", "file", true, "Input file");

        final String[] args = {"-f", "test.txt"};

        final CommandLine line = CommandLineUtil.getCommandLine(options, args);

        assertNotNull(line);
        assertTrue(line.hasOption("f"));
        assertEquals("test.txt", line.getOptionValue("f"));
    }

    @Test
    public void testInvalidCommandLine() {
        final Options options = new Options();
        options.addOption("f", "file", true, "Input file");

        final String[] args = {"-x", "unknown"};

        final CommandLine line = CommandLineUtil.getCommandLine(options, args);

        assertNull(line);
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
