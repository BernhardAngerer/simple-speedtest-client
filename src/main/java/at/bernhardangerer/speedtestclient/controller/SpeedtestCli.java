package at.bernhardangerer.speedtestclient.controller;

import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.service.SpeedtestCliService;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.type.OutputFormat;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static at.bernhardangerer.speedtestclient.controller.SpeedtestController.runSpeedTest;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.DEDICATED_SERVER_HOST;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.LIST_SERVER_HOSTS;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_DOWNLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.NO_UPLOAD;
import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.OUTPUT_FORMAT;
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
                OutputFormat outputFormat = OutputFormat.CONSOLE;
                if (SpeedtestCliService.isValidOptionAndOptionValue(cmd, OUTPUT_FORMAT)) {
                    outputFormat = OutputFormat.fromString(cmd.getParsedOptionValue(OUTPUT_FORMAT).toString().trim());
                }

                if (cmd.hasOption(LIST_SERVER_HOSTS)) {
                    SpeedtestCliService.processListServerHostsOption(outputFormat);
                    return;
                }

                Server dedicatedServer = null;
                if (SpeedtestCliService.isValidOptionAndOptionValue(cmd, DEDICATED_SERVER_HOST)) {
                    dedicatedServer = SpeedtestCliService.getDedicatedServer(cmd);

                    if (dedicatedServer == null) {
                        System.err.println("The provided host is not in the list of valid server hosts!");
                        return;
                    }
                }

                final SpeedtestResult result = runSpeedTest(DistanceUnit.fromAbbreviation(Util.getConfigProperty("DistanceUnit.default")),
                        !cmd.hasOption(NO_DOWNLOAD), !cmd.hasOption(NO_UPLOAD), cmd.hasOption(SHARE),
                        outputFormat == OutputFormat.CONSOLE, dedicatedServer);

                if (outputFormat != OutputFormat.CONSOLE) {
                    SpeedtestCliService.processSpeedtestResult(result, outputFormat);
                }
            } catch (Exception e) {
                System.err.println("Sorry, an unexpected exception occurred!");
                logger.error(e.getMessage(), e);
            }
        }
    }

}
