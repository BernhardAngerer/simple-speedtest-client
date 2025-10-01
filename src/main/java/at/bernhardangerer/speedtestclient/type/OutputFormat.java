package at.bernhardangerer.speedtestclient.type;

import lombok.Getter;

@Getter
public enum OutputFormat {
    JSON,
    XML,
    CSV,
    CONSOLE;

    public static OutputFormat fromString(final String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Invalid output format");
        }

        return switch (value.toLowerCase()) {
            case "json" -> JSON;
            case "xml" -> XML;
            case "csv" -> CSV;
            case "console" -> CONSOLE;
            default -> throw new IllegalArgumentException("Unknown output format: " + value);
        };
    }
}
