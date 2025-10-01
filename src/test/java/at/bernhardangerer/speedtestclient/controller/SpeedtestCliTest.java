package at.bernhardangerer.speedtestclient.controller;

import at.bernhardangerer.speedtestclient.fixture.ServerFixture;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import at.bernhardangerer.speedtestclient.model.DownloadSetting;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.service.ConfigSettingsService;
import at.bernhardangerer.speedtestclient.service.ServerSettingsService;
import at.bernhardangerer.speedtestclient.service.SpeedtestCliService;
import at.bernhardangerer.speedtestclient.service.SpeedtestResultPrinter;
import at.bernhardangerer.speedtestclient.type.OutputFormat;
import at.bernhardangerer.speedtestclient.util.CommandLineUtil;
import at.bernhardangerer.speedtestclient.util.Constant;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class SpeedtestCliTest {

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
    void testListServerHostsOptionWithDefaultOutput() throws ParseException {
        final String[] args = {"--listServerHosts"};
        final CommandLine cmd = setupCommandLineMock(null);
        final List<Server> servers = ServerFixture.createList();

        final String expectedOutput = """
                List of valid server hosts:
                server1.example.com
                server2.example.com
                server3.example.com
                """;

        withCommonStaticMocks(args, cmd, servers, () -> {
            SpeedtestCli.main(args);
            assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
            assertTrue(errContent.toString().isEmpty());
        });
    }

    @Test
    void testListServerHostsOptionWithConsoleOutput() throws ParseException {
        final String[] args = {"--listServerHosts", "--outputFormat console"};
        final CommandLine cmd = setupCommandLineMock("console");
        final List<Server> servers = ServerFixture.createList();

        final String expectedOutput = """
                List of valid server hosts:
                server1.example.com
                server2.example.com
                server3.example.com
                """;

        withCommonStaticMocks(args, cmd, servers, () -> {
            SpeedtestCli.main(args);
            assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
            assertTrue(errContent.toString().isEmpty());
        });
    }

    @Test
    void testListServerHostsOptionWithJsonOutput() throws ParseException {
        final String[] args = {"--listServerHosts", "--outputFormat json"};
        final CommandLine cmd = setupCommandLineMock("json");
        final List<Server> servers = ServerFixture.createList();

        final String expectedOutput = """
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

        withCommonStaticMocks(args, cmd, servers, () -> {
            SpeedtestCli.main(args);
            assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
            assertTrue(errContent.toString().isEmpty());
        });
    }

    @Test
    void testListServerHostsOptionWithXmlOutput() throws ParseException {
        final String[] args = {"--listServerHosts", "--outputFormat xml"};
        final CommandLine cmd = setupCommandLineMock("xml");
        final List<Server> servers = ServerFixture.createList();

        final String expectedOutput = """
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

        withCommonStaticMocks(args, cmd, servers, () -> {
            SpeedtestCli.main(args);
            assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
            assertTrue(errContent.toString().isEmpty());
        });
    }

    @Test
    void testListServerHostsOptionWithCsvOutput() throws ParseException {
        final String[] args = {"--listServerHosts", "--outputFormat csv"};
        final CommandLine cmd = setupCommandLineMock("csv");
        final List<Server> servers = ServerFixture.createList();

        final String expectedOutput = """
                id,sponsor,city,country,countryCode,host,url,lat,lon
                101,"ExampleNet, GmbH",Vienna,Austria,AT,server1.example.com,\
                http://speedtest1.example.com/speedtest,48.208200,16.373800
                102,SwissISP,Zurich,Switzerland,CH,server2.example.com,\
                http://speedtest2.example.com/speedtest,47.376900,8.541700
                103,GermanBroadband,Berlin,Germany,DE,server3.example.com,\
                http://speedtest3.example.com/speedtest,52.520000,13.405000
                """;

        withCommonStaticMocks(args, cmd, servers, () -> {
            SpeedtestCli.main(args);
            assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
            assertTrue(errContent.toString().isEmpty());
        });
    }

    @Test
    void testValidDedicatedServerHost() throws Exception {
        final String[] args = {"--dedicatedServerHost", "server1.example.com"};

        final CommandLine mockCmd = mock(CommandLine.class);
        when(mockCmd.hasOption("dedicatedServerHost")).thenReturn(true);
        when(mockCmd.getParsedOptionValue("dedicatedServerHost")).thenReturn("server1.example.com");

        final ConfigSetting mockConfig = mock(ConfigSetting.class);
        final DownloadSetting mockDownloadSetting = mock(DownloadSetting.class);
        when(mockDownloadSetting.getThreadsPerUrl()).thenReturn(1);
        when(mockConfig.getDownload()).thenReturn(mockDownloadSetting);

        final Server mockServer1 = mock(Server.class);
        when(mockServer1.getHost()).thenReturn("server1.example.com");
        final Server mockServer2 = mock(Server.class);
        when(mockServer2.getHost()).thenReturn("server2.example.com");

        try (MockedStatic<CommandLineUtil> mockedUtil = mockStatic(CommandLineUtil.class);
             MockedStatic<ConfigSettingsService> configMock = mockStatic(ConfigSettingsService.class);
             MockedStatic<ServerSettingsService> serverMock = mockStatic(ServerSettingsService.class);
             MockedStatic<SpeedtestController> controllerMock = mockStatic(SpeedtestController.class)) {

            mockedUtil.when(() -> CommandLineUtil.getCommandLine(any(), eq(args))).thenReturn(mockCmd);
            mockedUtil.when(CommandLineUtil::createOptions).thenReturn(null);

            configMock.when(ConfigSettingsService::requestSetting).thenReturn(mockConfig);
            serverMock.when(() -> ServerSettingsService.requestServerList(1))
                    .thenReturn(List.of(mockServer1, mockServer2));

            final ArgumentCaptor<Server> serverCaptor = ArgumentCaptor.forClass(Server.class);
            controllerMock.when(() -> SpeedtestController.runSpeedTest(any(), anyBoolean(), anyBoolean(), anyBoolean(),
                            anyBoolean(), serverCaptor.capture()))
                    .thenAnswer(invocation -> null);

            SpeedtestCli.main(args);

            final String output = outContent.toString();
            assertTrue(output.isEmpty());
            final String error = errContent.toString();
            assertTrue(error.isEmpty());

            final Server capturedServer = serverCaptor.getValue();
            assertNotNull(capturedServer);
            assertEquals("server1.example.com", capturedServer.getHost());

            controllerMock.verify(() ->
                    SpeedtestController.runSpeedTest(any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any()), times(1));
        }
    }

    @Test
    void testInvalidDedicatedServerHost() throws Exception {
        final String[] args = {"--dedicatedServerHost", "invalid-host"};

        final CommandLine mockCmd = mock(CommandLine.class);
        when(mockCmd.hasOption("dedicatedServerHost")).thenReturn(true);
        when(mockCmd.getParsedOptionValue("dedicatedServerHost")).thenReturn("invalid-host");

        final ConfigSetting mockConfig = mock(ConfigSetting.class);
        final DownloadSetting mockDownloadSetting = mock(DownloadSetting.class);
        when(mockDownloadSetting.getThreadsPerUrl()).thenReturn(1);
        when(mockConfig.getDownload()).thenReturn(mockDownloadSetting);

        final Server mockServer1 = mock(Server.class);
        when(mockServer1.getHost()).thenReturn("server1.example.com");
        final Server mockServer2 = mock(Server.class);
        when(mockServer2.getHost()).thenReturn("server2.example.com");

        try (MockedStatic<CommandLineUtil> mockedUtil = mockStatic(CommandLineUtil.class);
             MockedStatic<ConfigSettingsService> configMock = mockStatic(ConfigSettingsService.class);
             MockedStatic<ServerSettingsService> serverMock = mockStatic(ServerSettingsService.class);
             MockedStatic<SpeedtestController> controllerMock = mockStatic(SpeedtestController.class)) {

            mockedUtil.when(() -> CommandLineUtil.getCommandLine(any(), eq(args))).thenReturn(mockCmd);
            mockedUtil.when(CommandLineUtil::createOptions).thenReturn(null);

            configMock.when(ConfigSettingsService::requestSetting).thenReturn(mockConfig);
            serverMock.when(() -> ServerSettingsService.requestServerList(1))
                    .thenReturn(List.of(mockServer1, mockServer2));

            SpeedtestCli.main(args);

            final String output = outContent.toString();
            assertTrue(output.isEmpty());
            final String error = errContent.toString();
            assertTrue(error.contains("The provided host is not in the list of valid server hosts!"));

            controllerMock.verify(() ->
                    SpeedtestController.runSpeedTest(any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any()), never());
        }
    }

    @Test
    void shouldThrowExceptionForConsoleOutputFormat() {
        final SpeedtestResult result = new SpeedtestResult();

        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                SpeedtestCliService.processSpeedtestResult(result, OutputFormat.CONSOLE)
        );

        assertEquals("Console output should not be passed to processSpeedtestResult()", ex.getMessage());
    }

    @Test
    void shouldCallPrintJsonForJsonOutputFormat() {
        final SpeedtestResult result = new SpeedtestResult();

        try (MockedStatic<SpeedtestResultPrinter> mockedPrinter = mockStatic(SpeedtestResultPrinter.class)) {
            SpeedtestCliService.processSpeedtestResult(result, OutputFormat.JSON);

            mockedPrinter.verify(() -> SpeedtestResultPrinter.printJson(result));
        }
    }

    @Test
    void shouldCallPrintXmlForXmlOutputFormat() {
        final SpeedtestResult result = new SpeedtestResult();

        try (MockedStatic<SpeedtestResultPrinter> mockedPrinter = mockStatic(SpeedtestResultPrinter.class)) {
            SpeedtestCliService.processSpeedtestResult(result, OutputFormat.XML);

            mockedPrinter.verify(() -> SpeedtestResultPrinter.printXml(result));
        }
    }

    @Test
    void shouldCallPrintCsvForCsvOutputFormat() {
        final SpeedtestResult result = new SpeedtestResult();

        try (MockedStatic<SpeedtestResultPrinter> mockedPrinter = mockStatic(SpeedtestResultPrinter.class)) {
            SpeedtestCliService.processSpeedtestResult(result, OutputFormat.CSV);

            mockedPrinter.verify(() -> SpeedtestResultPrinter.printCsv(result, Constant.COMMA));
        }
    }

    private void withCommonStaticMocks(final String[] args, final CommandLine cmd, final List<Server> servers, final Runnable testLogic) {
        final ConfigSetting mockConfig = mock(ConfigSetting.class);
        final DownloadSetting mockDownload = mock(DownloadSetting.class);
        when(mockDownload.getThreadsPerUrl()).thenReturn(1);
        when(mockConfig.getDownload()).thenReturn(mockDownload);

        final Options options = CommandLineUtil.createOptions();

        try (MockedStatic<CommandLineUtil> mockedUtil = mockStatic(CommandLineUtil.class);
             MockedStatic<ConfigSettingsService> configMock = mockStatic(ConfigSettingsService.class);
             MockedStatic<ServerSettingsService> serverMock = mockStatic(ServerSettingsService.class);
             MockedStatic<SpeedtestController> controllerMock = mockStatic(SpeedtestController.class)) {

            mockedUtil.when(() -> CommandLineUtil.getCommandLine(options, args)).thenReturn(cmd);
            mockedUtil.when(CommandLineUtil::createOptions).thenReturn(options);
            configMock.when(ConfigSettingsService::requestSetting).thenReturn(mockConfig);
            serverMock.when(() -> ServerSettingsService.requestServerList(1)).thenReturn(servers);

            testLogic.run();

            controllerMock.verify(() ->
                    SpeedtestController.runSpeedTest(any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any()), never());
        }
    }

    private CommandLine setupCommandLineMock(final String outputFormat) throws ParseException {
        final CommandLine mockCmd = mock(CommandLine.class);
        when(mockCmd.hasOption("listServerHosts")).thenReturn(true);

        if (outputFormat != null) {
            when(mockCmd.hasOption("outputFormat")).thenReturn(true);
            when(mockCmd.getParsedOptionValue("outputFormat")).thenReturn(outputFormat);
        } else {
            when(mockCmd.hasOption("outputFormat")).thenReturn(false);
        }

        return mockCmd;
    }

}
