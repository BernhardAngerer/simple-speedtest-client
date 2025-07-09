package at.bernhardangerer.speedtestclient;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.*;
import at.bernhardangerer.speedtestclient.service.*;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SpeedtestController {

  public static SpeedtestResult runSpeedTest() throws SpeedtestException {
    return runSpeedTest(DistanceUnit.fromAbbreviation(Util.getConfigProperty("DistanceUnit.default")),
        true, true, false, false, null);
  }

  public static SpeedtestResult runSpeedTest(DistanceUnit distanceUnit, boolean testDownload, boolean testUpload,
                                             boolean generateShareURL) throws SpeedtestException {
    return runSpeedTest(distanceUnit, testDownload, testUpload, generateShareURL, false, null);
  }

  public static SpeedtestResult runSpeedTest(DistanceUnit distanceUnit, boolean testDownload, boolean testUpload,
                                             boolean generateShareURL, Server dedicatedServer) throws SpeedtestException {
    return runSpeedTest(distanceUnit, testDownload, testUpload, generateShareURL, false, dedicatedServer);
  }

  static SpeedtestResult runSpeedTest(final DistanceUnit distanceUnit, final boolean testDownload, final boolean testUpload,
                                      final boolean generateShareURL, final boolean generateOutput, final Server dedicatedServer)
          throws SpeedtestException {
    if (distanceUnit != null) {
      try {
        if (generateOutput) {
          System.out.println("Retrieving speedtest.net configuration...");
        }
        final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
        if (generateOutput) {
          System.out.printf("Testing from %s (%s)...\n", configSetting.getClient().getIsp(), configSetting.getClient().getIp());
        }

        final List<Server> serverList;
        if (dedicatedServer == null) {
          if (generateOutput) {
            System.out.println("Retrieving speedtest.net server list...");
          }
          serverList = ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerURL());
        } else {
          serverList = List.of(dedicatedServer);
        }
        final Map<Double, Server> closestServers = ServerSettingsService.findClosestServers(configSetting.getClient().getLat(),
                configSetting.getClient().getLon(), Integer.parseInt(Objects.requireNonNull(Util.getConfigProperty("ClosestServers.maxNumber"))),
                distanceUnit, serverList);

        if (generateOutput && dedicatedServer == null) {
          System.out.println("Selecting best server based on ping...");
        }
        final Map.Entry<Server, LatencyTestResult> fastestServer = LatencyService.getFastestServer(closestServers);
        if (generateOutput) {
          System.out.printf("Hosted by %s (%s) [%,.2f %s]: %,.2f ms\n",
                  fastestServer.getKey().getSponsor(), fastestServer.getKey().getName(),
                  fastestServer.getValue().getDistance(), distanceUnit.getAbbreviation(), fastestServer.getValue().getLatency());
        }

        TransferTestResult downloadResult = null;
        if (testDownload) {
          if (generateOutput) {
            System.out.print("Testing download speed");
          }
          downloadResult = DownloadService.testDownload(fastestServer.getKey().getUrl(),
              configSetting.getDownload(), generateOutput ? Util::printDot : () -> {});
          if (generateOutput) {
            System.out.printf("\nDownload: %,.2f Mbits/s\n", downloadResult.getRateInMbps());
          }
        }

        TransferTestResult uploadResult = null;
        if (testUpload) {
          if (generateOutput) {
            System.out.print("Testing upload speed");
          }
          uploadResult = UploadService.testUpload(fastestServer.getKey().getUrl(),
              configSetting.getUpload(), (downloadResult != null && downloadResult.getRateInMbps() > 0.1) ? 8 :
                  configSetting.getUpload().getThreads(),
              generateOutput ? Util::printDot : () -> {});
          if (generateOutput) {
            System.out.printf("\nUpload: %,.2f Mbits/s\n", uploadResult.getRateInMbps());
          }
        }

        String shareURL = null;
        if (generateShareURL && uploadResult != null && downloadResult != null) {
          shareURL = ShareUrlService.createShareURL(fastestServer.getKey().getId(), fastestServer.getValue().getLatency(),
              uploadResult.getRateInMbps(), downloadResult.getRateInMbps());
          if (generateOutput) {
            System.out.println("Share results: " + shareURL);
          }
        }

        return new SpeedtestResult(System.currentTimeMillis(), configSetting.getClient(), fastestServer.getKey(),
            fastestServer.getValue(), downloadResult, uploadResult, shareURL);
      } catch (Exception e) {
        if (generateOutput) {
          System.out.println("Something went wrong");
        }
        throw new SpeedtestException(e);
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

}
