package at.bernhardangerer.speedtestclient;

import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.service.ConfigSettingsService;
import at.bernhardangerer.speedtestclient.service.ServerSettingsService;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static at.bernhardangerer.speedtestclient.SpeedtestController.runSpeedTest;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.DEDICATED_SERVER_HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.LIST_SERVER_HOSTS;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_DOWNLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_UPLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.SHARE;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.createOptions;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.getCommandLine;

public final class SpeedtestCli {
    private static final Logger logger = LogManager.getLogger(SpeedtestCli.class);

    private SpeedtestCli() {
    }

    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(String[] args) {
        final CommandLine cmd = getCommandLine(createOptions(), args);
        if (cmd != null) {
            try {
                if (cmd.hasOption(LIST_SERVER_HOSTS)) {
                    final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
                    final List<Server> serverHostList =
                            ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerUrl());
                    System.out.println("List of valid server hosts:");
                    serverHostList.forEach(server -> System.out.println(server.getHost()));
                    return;
                }

                Server dedicatedServer = null;
                if (cmd.hasOption(DEDICATED_SERVER_HOST) && cmd.getParsedOptionValue(DEDICATED_SERVER_HOST) != null
                        && !cmd.getParsedOptionValue(DEDICATED_SERVER_HOST).toString().trim().isEmpty()) {
                    final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
                    final List<Server> serverHostList =
                            ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerUrl());
                    dedicatedServer = serverHostList.stream().filter(server -> {
                        try {
                            return cmd.getParsedOptionValue(DEDICATED_SERVER_HOST).toString().trim().equals(server.getHost());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }).findFirst().orElse(null);

                    if (dedicatedServer == null) {
                        System.out.println("The provided host is not in the list of valid server hosts!");
                        return;
                    }
                }

                runSpeedTest(DistanceUnit.fromAbbreviation(Util.getConfigProperty("DistanceUnit.default")),
                        !cmd.hasOption(NO_DOWNLOAD), !cmd.hasOption(NO_UPLOAD), cmd.hasOption(SHARE), true, dedicatedServer);
            } catch (Exception e) {
                System.out.println("Sorry, an unexpected exception occurred!");
                logger.error(e.getMessage(), e);
            }
        }
    }

}
