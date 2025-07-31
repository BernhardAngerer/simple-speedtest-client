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

        switch (value.toLowerCase()) {
            case "json": return JSON;
            case "xml": return XML;
            case "csv": return CSV;
            case "console": return CONSOLE;
            default: throw new IllegalArgumentException("Unknown output format: " + value);
        }
    }
}
