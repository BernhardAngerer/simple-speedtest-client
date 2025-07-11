package at.bernhardangerer.speedtestclient.controller;

import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import at.bernhardangerer.speedtestclient.model.DownloadSetting;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.service.ConfigSettingsService;
import at.bernhardangerer.speedtestclient.service.ServerSettingsService;
import at.bernhardangerer.speedtestclient.util.CommandLineUtil;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testListServerHostsOption() {
        final String[] args = {"--listServerHosts"};

        final CommandLine mockCmd = mock(CommandLine.class);
        when(mockCmd.hasOption("listServerHosts")).thenReturn(true);

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
            final String expectedOutput = "List of valid server hosts:\n" +
                    "server1.example.com\n" +
                    "server2.example.com\n";
            assertEquals(expectedOutput, output);

            controllerMock.verify(() ->
                    SpeedtestController.runSpeedTest(any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any()), never());
        }
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
            assertEquals("", output);

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
            assertTrue(output.contains("The provided host is not in the list of valid server hosts!"));

            controllerMock.verify(() ->
                    SpeedtestController.runSpeedTest(any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any()), never());
        }
    }
}
