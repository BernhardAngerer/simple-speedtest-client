package at.bernhardangerer.speedtestclient;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.*;
import at.bernhardangerer.speedtestclient.service.*;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpeedtestControllerTest {

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
        client = mock(Client.class);
        when(client.getIsp()).thenReturn("Test ISP");
        when(client.getIp()).thenReturn("127.0.0.1");
        when(client.getLat()).thenReturn(40.0);
        when(client.getLon()).thenReturn(-74.0);

        downloadSetting = mock(DownloadSetting.class);
        when(downloadSetting.getThreadsPerURL()).thenReturn(4);

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

    @Test
    public void testRunSpeedTestSuccessful() throws Exception {
        try (
                final MockedStatic<ConfigSettingsService> configSettingsMock = Mockito.mockStatic(ConfigSettingsService.class);
                final MockedStatic<ServerSettingsService> serverSettingsMock = Mockito.mockStatic(ServerSettingsService.class);
                final MockedStatic<LatencyService> latencyMock = Mockito.mockStatic(LatencyService.class);
                final MockedStatic<DownloadService> downloadMock = Mockito.mockStatic(DownloadService.class);
                final MockedStatic<UploadService> uploadMock = Mockito.mockStatic(UploadService.class);
                final MockedStatic<ShareUrlService> shareMock = Mockito.mockStatic(ShareUrlService.class);
                final MockedStatic<Util> utilMock = Mockito.mockStatic(Util.class)
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
            shareMock.when(() -> ShareUrlService.createShareURL(anyInt(), anyDouble(), anyDouble(), anyDouble()))
                    .thenReturn("http://share.url");

            utilMock.when(() -> Util.getConfigProperty("ClosestServers.maxNumber")).thenReturn("3");

            final SpeedtestResult result = SpeedtestController.runSpeedTest(DistanceUnit.KILOMETER, true, true, true, true, null);

            assertNotNull(result);
            assertEquals(client, result.getClient());
            assertEquals(server, result.getServer());
            assertEquals(latencyResult, result.getLatency());
            assertEquals(downloadResult, result.getDownload());
            assertEquals(uploadResult, result.getUpload());
            assertEquals("http://share.url", result.getShareURL());
        }
    }

    @Test
    public void testRunSpeedTestInvalidDistanceUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> SpeedtestController.runSpeedTest(null, true, true, true, false, null));
    }

    @Test
    public void testRunSpeedTestExceptionHandling() {
        try (final MockedStatic<ConfigSettingsService> configSettingsMock = Mockito.mockStatic(ConfigSettingsService.class)) {
            configSettingsMock.when(ConfigSettingsService::requestSetting).thenThrow(new RuntimeException("Failure"));
            assertThrows(SpeedtestException.class,
                    () -> SpeedtestController.runSpeedTest(DistanceUnit.KILOMETER, true, false, false, false, null));
        }
    }
}
