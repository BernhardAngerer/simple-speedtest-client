package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.type.OutputFormat;
import at.bernhardangerer.speedtestclient.util.Constant;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.List;

import static at.bernhardangerer.speedtestclient.util.CommandLineUtil.DEDICATED_SERVER_HOST;

public final class SpeedtestCliService {

    private SpeedtestCliService() {
    }

    public static boolean isValidOptionAndOptionValue(final CommandLine cmd, final String optionName) throws ParseException {
        return cmd.hasOption(optionName) && cmd.getParsedOptionValue(optionName) != null
                && !cmd.getParsedOptionValue(optionName).toString().trim().isEmpty();
    }

    public static void processListServerHostsOption(final OutputFormat outputFormat)
            throws MissingResultException, ServerRequestException, ParsingException {
        final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
        final List<Server> serverHostList =
                ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerUrl());
        switch (outputFormat) {
            case JSON: {
                ServerHostListPrinter.printJson(serverHostList);
                break;
            }
            case XML: {
                ServerHostListPrinter.printXml(serverHostList);
                break;
            }
            case CSV: {
                ServerHostListPrinter.printCsv(serverHostList, Constant.COMMA);
                break;
            }
            case CONSOLE: {
                ServerHostListPrinter.printHumanReadable(serverHostList);
                break;
            }
        }
    }

    public static void processSpeedtestResult(final SpeedtestResult speedtestResult, final OutputFormat outputFormat) {
        if (outputFormat == OutputFormat.CONSOLE) {
            throw new IllegalArgumentException("Console output should not be passed to processSpeedtestResult()");
        }

        switch (outputFormat) {
            case JSON: {
                SpeedtestResultPrinter.printJson(speedtestResult);
                break;
            }
            case XML: {
                SpeedtestResultPrinter.printXml(speedtestResult);
                break;
            }
            case CSV: {
                SpeedtestResultPrinter.printCsv(speedtestResult, Constant.COMMA);
                break;
            }
            default: throw new IllegalArgumentException("Unsupported output format: " + outputFormat.name());
        }
    }

    public static Server getDedicatedServer(final CommandLine cmd)
            throws MissingResultException, ServerRequestException, ParsingException {
        final ConfigSetting configSetting = ConfigSettingsService.requestSetting();
        final List<Server> serverHostList =
                ServerSettingsService.requestServerList(configSetting.getDownload().getThreadsPerUrl());
        return serverHostList.stream().filter(server -> {
            try {
                return cmd.getParsedOptionValue(DEDICATED_SERVER_HOST).toString().trim().equals(server.getHost());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);
    }

}
