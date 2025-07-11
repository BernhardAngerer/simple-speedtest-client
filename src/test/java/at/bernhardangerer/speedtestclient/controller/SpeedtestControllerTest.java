package at.bernhardangerer.speedtestclient.controller;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.Client;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import at.bernhardangerer.speedtestclient.model.DownloadSetting;
import at.bernhardangerer.speedtestclient.model.LatencyTestResult;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.model.UploadSetting;
import at.bernhardangerer.speedtestclient.service.ConfigSettingsService;
import at.bernhardangerer.speedtestclient.service.DownloadService;
import at.bernhardangerer.speedtestclient.service.LatencyService;
import at.bernhardangerer.speedtestclient.service.ServerSettingsService;
import at.bernhardangerer.speedtestclient.service.ShareUrlService;
import at.bernhardangerer.speedtestclient.service.UploadService;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class SpeedtestControllerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConfigSetting configSetting;
    private Client client;
    private DownloadSetting downloadSetting;
    private UploadSetting uploadSetting;
    private Server server;
    private LatencyTestResult latencyResult;
    private TransferTestResult downloadResult;
    private TransferTestResult uploadResult;

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(outContent));

        client = mock(Client.class);
        when(client.getIsp()).thenReturn("Test ISP");
        when(client.getIpAddress()).thenReturn("127.0.0.1");
        when(client.getLat()).thenReturn(40.0);
        when(client.getLon()).thenReturn(-74.0);

        downloadSetting = mock(DownloadSetting.class);
        when(downloadSetting.getThreadsPerUrl()).thenReturn(4);

        uploadSetting = mock(UploadSetting.class);
        when(uploadSetting.getThreads()).thenReturn(2);

        configSetting = mock(ConfigSetting.class);
        when(configSetting.getClient()).thenReturn(client);
        when(configSetting.getDownload()).thenReturn(downloadSetting);
        when(configSetting.getUpload()).thenReturn(uploadSetting);

        server = mock(Server.class);
        when(server.getSponsor()).thenReturn("Test Sponsor");
        when(server.getName()).thenReturn("Test Server");
        when(server.getUrl()).thenReturn("http://testserver.com");
        when(server.getId()).thenReturn(123);

        latencyResult = mock(LatencyTestResult.class);
        when(latencyResult.getLatency()).thenReturn(10.5);
        when(latencyResult.getDistance()).thenReturn(12.3);

        downloadResult = mock(TransferTestResult.class);
        when(downloadResult.getRateInMbps()).thenReturn(50.0);

        uploadResult = mock(TransferTestResult.class);
        when(uploadResult.getRateInMbps()).thenReturn(20.0);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testRunSpeedTestSuccessful() throws Exception {
        try (
                MockedStatic<ConfigSettingsService> configSettingsMock = Mockito.mockStatic(ConfigSettingsService.class);
                MockedStatic<ServerSettingsService> serverSettingsMock = Mockito.mockStatic(ServerSettingsService.class);
                MockedStatic<LatencyService> latencyMock = Mockito.mockStatic(LatencyService.class);
                MockedStatic<DownloadService> downloadMock = Mockito.mockStatic(DownloadService.class);
                MockedStatic<UploadService> uploadMock = Mockito.mockStatic(UploadService.class);
                MockedStatic<ShareUrlService> shareMock = Mockito.mockStatic(ShareUrlService.class);
                MockedStatic<Util> utilMock = Mockito.mockStatic(Util.class)
        ) {
            configSettingsMock.when(ConfigSettingsService::requestSetting).thenReturn(configSetting);
            serverSettingsMock.when(() -> ServerSettingsService.requestServerList(4)).thenReturn(List.of(server));
            serverSettingsMock.when(() ->
                            ServerSettingsService.findClosestServers(eq(40.0), eq(-74.0), anyInt(), eq(DistanceUnit.KILOMETER), anyList()))
                    .thenReturn(Map.of(12.3, server));
            latencyMock.when(() -> LatencyService.getFastestServer(anyMap()))
                    .thenReturn(Map.entry(server, latencyResult));
            downloadMock.when(() -> DownloadService.testDownload(any(), eq(downloadSetting), any()))
                    .thenReturn(downloadResult);
            uploadMock.when(() -> UploadService.testUpload(any(), eq(uploadSetting), anyInt(), any()))
                    .thenReturn(uploadResult);
            shareMock.when(() -> ShareUrlService.createShareUrl(anyInt(), anyDouble(), anyDouble(), anyDouble()))
                    .thenReturn("http://share.url");

            utilMock.when(() -> Util.getConfigProperty("ClosestServers.maxNumber")).thenReturn("3");

            final SpeedtestResult result = SpeedtestController.runSpeedTest(DistanceUnit.KILOMETER, true, true, true, true, null);

            assertNotNull(result);
            assertEquals(client, result.getClient());
            assertEquals(server, result.getServer());
            assertEquals(latencyResult, result.getLatency());
            assertEquals(downloadResult, result.getDownload());
            assertEquals(uploadResult, result.getUpload());
            assertEquals("http://share.url", result.getShareUrl());

            final String output = outContent.toString();
            final String expectedOutput = "Retrieving speedtest.net configuration...\n"
                    + "Testing from Test ISP (127.0.0.1)...\n"
                    + "Retrieving speedtest.net server list...\n"
                    + "Selecting best server based on ping...\n"
                    + "Hosted by Test Sponsor (Test Server) [12,30 km]: 10,50 ms\n"
                    + "Testing download speed\n"
                    + "Download: 50,00 Mbits/s\n"
                    + "Testing upload speed\n"
                    + "Upload: 20,00 Mbits/s\n"
                    + "Share results: http://share.url\n";
            assertEquals(expectedOutput, output);
        }
    }

    @Test
    public void testRunSpeedTestInvalidDistanceUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> SpeedtestController.runSpeedTest(null, true, true, true, false, null));
    }

    @Test
    public void testRunSpeedTestExceptionHandling() {
        try (MockedStatic<ConfigSettingsService> configSettingsMock = Mockito.mockStatic(ConfigSettingsService.class)) {
            configSettingsMock.when(ConfigSettingsService::requestSetting).thenThrow(new RuntimeException("Failure"));
            assertThrows(SpeedtestException.class,
                    () -> SpeedtestController.runSpeedTest(DistanceUnit.KILOMETER, true, false, false, false, null));
        }
    }
}
