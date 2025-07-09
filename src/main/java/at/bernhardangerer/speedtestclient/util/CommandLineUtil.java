package at.bernhardangerer.speedtestclient.util;

import org.apache.commons.cli.*;

public final class CommandLineUtil {

  public static final String NO_DOWNLOAD = "noDownload";
  public static final String NO_UPLOAD = "noUpload";
  public static final String SHARE = "share";
  public static final String DEDICATED_SERVER_HOST = "dedicatedServerHost";
  public static final String LIST_SERVER_HOSTS = "listServerHosts";

  private CommandLineUtil() {
  }

  public static CommandLine getCommandLine(final Options options, final String[] args) {
    final CommandLineParser parser = new DefaultParser();
    CommandLine line = null;
    try {
      line = parser.parse(options, args);
    } catch (ParseException e) {
      help(options);
    }
    return line;
  }

  public static Options createOptions() {
    final Options options = new Options();
    options.addOption(createOption("nd", NO_DOWNLOAD, null, "Do not perform download test"));
    options.addOption(createOption("nu", NO_UPLOAD, null, "Do not perform upload test"));
    options.addOption(createOption("s", SHARE, null, "Generate and provide an URL to the speedtest.net share results image"));
    options.addOption(createOption("h", DEDICATED_SERVER_HOST, "HOST", "Dedicated server host to run the tests against"));
    options.addOption(createOption("l", LIST_SERVER_HOSTS, null, "Provide a list of server hosts to run the tests against"));
    return options;
  }

  private static Option createOption(String shortName, String longName, String argName, String description) {
    return Option.builder(shortName)
            .longOpt(longName)
            .argName(argName)
            .desc(description)
            .hasArg(argName != null)
            .build();
  }

  private static void help(final Options options) {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Optional parameters:", options);
  }

}
