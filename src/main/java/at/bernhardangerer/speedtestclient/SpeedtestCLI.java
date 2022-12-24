package at.bernhardangerer.speedtestclient;

import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static at.bernhardangerer.speedtestclient.SpeedtestController.runSpeedTest;

public final class SpeedtestCLI {
  private static final Logger logger = LogManager.getLogger(SpeedtestCLI.class);

  public static void main(String[] args) {
    final CommandLine cmd = getCommandLine(createOptions(), args);
    if (cmd != null) {
      try {
        runSpeedTest(DistanceUnit.fromAbbreviation(Util.getConfigProperty("DistanceUnit.default")),
            !cmd.hasOption("noDownload"), !cmd.hasOption("noUpload"), cmd.hasOption("share"), true);
      } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
      }
    }
  }

  private static CommandLine getCommandLine(final Options options, final String[] args) {
    final CommandLineParser parser = new DefaultParser();
    CommandLine line = null;
    try {
      line = parser.parse(options, args);
    } catch (ParseException e) {
      help(options);
    }
    return line;
  }

  private static Options createOptions() {
    final Options options = new Options();
    options.addOption(new Option("noDownload", "Do not perform download test"));
    options.addOption(new Option("noUpload", "Do not perform upload test"));
    options.addOption(new Option("share", "Generate and provide a URL to the speedtest.net share results image"));
    return options;
  }

  private static void help(final Options options) {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Optional parameters:", options);
  }

}
