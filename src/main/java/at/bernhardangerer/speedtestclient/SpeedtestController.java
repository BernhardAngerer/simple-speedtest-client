package at.bernhardangerer.speedtestclient;

import at.bernhardangerer.speedtestclient.exception.SpeedtestException;
import at.bernhardangerer.speedtestclient.model.*;
import at.bernhardangerer.speedtestclient.service.*;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;

import java.util.List;
import java.util.Map;

public class SpeedtestController {

  public static SpeedtestResult runSpeedTest() throws SpeedtestException {
    return runSpeedTest(DistanceUnit.fromAbbreviation(Util.getConfigProperty("DistanceUnit.default")),
        true, true, false, false);
  }

  public static SpeedtestResult runSpeedTest(DistanceUnit distanceUnit, boolean testDownload, boolean testUpload,
                                             boolean generateShareURL) throws SpeedtestException {
    return runSpeedTest(distanceUnit, testDownload, testUpload, generateShareURL, false);
  }

  static SpeedtestResult runSpeedTest(DistanceUnit distanceUnit, boolean testDownload, boolean testUpload,
                                      boolean generateShareURL, boolean generateOutput) throws SpeedtestException {
    if (distanceUnit != null) {
      try {
        if (generateOutput) {
          System.out.println("Retrieving speedtest.net configuration...");
        }
        final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
        if (generateOutput) {
          System.out.printf("Testing from %s (%s)...\n", configSetting.getClient().getIsp(), configSetting.getClient().getIp());
        }

        if (generateOutput) {
          System.out.println("Retrieving speedtest.net server list...");
        }
        final List<Server> serverList = ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerURL());
        final Map<Double, Server> closestServers = ServerSettingsService.findClosestServers(configSetting.getClient().getLat(),
            configSetting.getClient().getLon(), Integer.parseInt(Util.getConfigProperty("ClosestServers.maxNumber")),
            distanceUnit, serverList);

        if (generateOutput) {
          System.out.println("Selecting best server based on ping...");
        }
        final Map.Entry<Server, LatencyTestResult> fastestServer = LatencyService.getFastestServer(closestServers, distanceUnit);
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
            System.out.printf("\nDownload: %,.2f Mbits/s\n", downloadResult.getRateInMbitS());
          }
        }

        TransferTestResult uploadResult = null;
        if (testUpload) {
          if (generateOutput) {
            System.out.print("Testing upload speed");
          }
          uploadResult = UploadService.testUpload(fastestServer.getKey().getUrl(),
              configSetting.getUpload(), (downloadResult != null && downloadResult.getRateInMbitS() > 0.1) ? 8 :
                  configSetting.getUpload().getThreads(),
              generateOutput ? Util::printDot : () -> {});
          if (generateOutput) {
            System.out.printf("\nUpload: %,.2f Mbits/s\n", uploadResult.getRateInMbitS());
          }
        }

        String shareURL = null;
        if (generateShareURL && uploadResult != null && downloadResult != null) {
          shareURL = ShareUrlService.createShareURL(fastestServer.getKey().getId(), fastestServer.getValue().getLatency(),
              uploadResult.getRateInMbitS(), downloadResult.getRateInMbitS());
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
