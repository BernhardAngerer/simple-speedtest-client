package at.bernhardangerer.speedtestclient.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class CsvUtil {

    private CsvUtil() {
    }

    public static List<String> formatCsvValues(final List<Object> objects, final String delimiter) {
        if (objects == null || objects.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("Invalid Object list or delimiter");
        }

        return objects.stream()
                .map(object -> formatCsvValue(object, delimiter))
                .collect(Collectors.toList());
    }

    public static String joinStrings(final List<String> strings, final String delimiter) {
        if (strings == null || strings.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("Invalid String list or delimiter");
        }

        return String.join(delimiter, strings);
    }

    public static String formatCsvValue(final Object value) {
        return formatCsvValue(value, Constant.COMMA);
    }

    public static String formatCsvValue(final Object value, final String delimiter) {
        if (value == null) {
            return "";
        }

        final String str;
        if (value instanceof Double || value instanceof Float || value instanceof BigDecimal) {
            str = String.format(Locale.US, "%.6f", ((Number) value).doubleValue());
        } else {
            str = value.toString();
        }

        final boolean needsEscaping = str.contains(Constant.DOUBLE_QUOTE)
                || str.contains(delimiter)
                || str.contains("\n")
                || str.contains("\r")
                || str.startsWith(Constant.SPACE)
                || str.endsWith(Constant.SPACE);
        if (needsEscaping) {
            return Constant.DOUBLE_QUOTE + str.replace(Constant.DOUBLE_QUOTE, "\"\"") + Constant.DOUBLE_QUOTE;
        }
        return str;
    }

}
