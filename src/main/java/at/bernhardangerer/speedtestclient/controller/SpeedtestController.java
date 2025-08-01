package at.bernhardangerer.speedtestclient.controller;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import at.bernhardangerer.speedtestclient.model.LatencyTestResult;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.service.ConfigSettingsService;
import at.bernhardangerer.speedtestclient.service.DownloadService;
import at.bernhardangerer.speedtestclient.service.LatencyService;
import at.bernhardangerer.speedtestclient.service.ServerSettingsService;
import at.bernhardangerer.speedtestclient.service.ShareUrlService;
import at.bernhardangerer.speedtestclient.service.UploadService;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SpeedtestController {

    private SpeedtestController() {
    }

    public static SpeedtestResult runSpeedTest() throws SpeedtestException {
        return runSpeedTest(DistanceUnit.fromAbbreviation(Util.getConfigProperty("DistanceUnit.default")),
                true, true, false, false, null);
    }

    public static SpeedtestResult runSpeedTest(final DistanceUnit distanceUnit, final boolean testDownload, final boolean testUpload,
                                               final boolean generateShareUrl) throws SpeedtestException {
        return runSpeedTest(distanceUnit, testDownload, testUpload, generateShareUrl, false, null);
    }

    public static SpeedtestResult runSpeedTest(final DistanceUnit distanceUnit, final boolean testDownload, final boolean testUpload,
                                               final boolean generateShareUrl, final Server dedicatedServer) throws SpeedtestException {
        return runSpeedTest(distanceUnit, testDownload, testUpload, generateShareUrl, false, dedicatedServer);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    static SpeedtestResult runSpeedTest(final DistanceUnit distanceUnit, final boolean testDownload, final boolean testUpload,
                                        final boolean generateShareUrl, final boolean consoleOutput, final Server dedicatedServer)
            throws SpeedtestException {
        if (distanceUnit != null) {
            try {
                final LocalDateTime startTime = LocalDateTime.now();
                if (consoleOutput) {
                    System.out.println("Retrieving speedtest.net configuration...");
                }
                final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
                if (consoleOutput) {
                    System.out.printf("Testing from %s (%s, %s)...\n", configSetting.getClient().getIsp(),
                            configSetting.getClient().getIpAddress(), configSetting.getClient().getIsoAlpha2CountryCode());
                }

                final List<Server> serverList;
                if (dedicatedServer == null) {
                    if (consoleOutput) {
                        System.out.println("Retrieving speedtest.net server list...");
                    }
                    serverList = ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerUrl());
                } else {
                    serverList = List.of(dedicatedServer);
                }
                final Map<Double, Server> closestServers = ServerSettingsService.findClosestServers(configSetting.getClient().getLat(),
                        configSetting.getClient().getLon(),
                        Integer.parseInt(Objects.requireNonNull(Util.getConfigProperty("ClosestServers.maxNumber"))),
                        distanceUnit, serverList);

                if (consoleOutput && dedicatedServer == null) {
                    System.out.println("Selecting best server based on ping...");
                }
                final Map.Entry<Server, LatencyTestResult> fastestServer = LatencyService.getFastestServer(closestServers);
                if (consoleOutput) {
                    System.out.printf("Hosted by %s (%s, %s) [%,.2f %s]: %,.2f ms\n",
                            fastestServer.getKey().getSponsor(), fastestServer.getKey().getCity(),
                            fastestServer.getKey().getIsoAlpha2CountryCode(), fastestServer.getValue().getDistance(),
                            distanceUnit.getAbbreviation(), fastestServer.getValue().getLatency());
                }

                TransferTestResult downloadResult = null;
                if (testDownload) {
                    if (consoleOutput) {
                        System.out.print("Testing download speed");
                    }
                    downloadResult = DownloadService.testDownload(fastestServer.getKey().getUrl(),
                            configSetting.getDownload(), consoleOutput ? Util::printDot : () -> {
                            });
                    if (consoleOutput) {
                        System.out.printf("\nDownload: %,.2f Mbits/s\n", downloadResult.getRateInMbps());
                    }
                }

                TransferTestResult uploadResult = null;
                if (testUpload) {
                    if (consoleOutput) {
                        System.out.print("Testing upload speed");
                    }
                    uploadResult = UploadService.testUpload(fastestServer.getKey().getUrl(),
                            configSetting.getUpload(), (downloadResult != null && downloadResult.getRateInMbps() > 0.1)
                                    ? 8 : configSetting.getUpload().getThreads(),
                            consoleOutput ? Util::printDot : () -> {
                            });
                    if (consoleOutput) {
                        System.out.printf("\nUpload: %,.2f Mbits/s\n", uploadResult.getRateInMbps());
                    }
                }

                String shareUrl = null;
                if (generateShareUrl && uploadResult != null && downloadResult != null) {
                    shareUrl = ShareUrlService.createShareUrl(fastestServer.getKey().getId(), fastestServer.getValue().getLatency(),
                            uploadResult.getRateInMbps(), downloadResult.getRateInMbps());
                    if (consoleOutput) {
                        System.out.println("Share results: " + shareUrl);
                    }
                }

                return new SpeedtestResult(startTime, LocalDateTime.now(), configSetting.getClient(), fastestServer.getKey(),
                        fastestServer.getValue(), downloadResult, uploadResult, shareUrl);
            } catch (Exception e) {
                if (consoleOutput) {
                    System.err.println("Something went wrong");
                }
                throw new SpeedtestException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
