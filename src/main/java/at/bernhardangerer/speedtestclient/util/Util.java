package at.bernhardangerer.speedtestclient.util;

import at.bernhardangerer.speedtestclient.exception.UnsupportedUnitException;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class Util {
    private static final Logger logger = LogManager.getLogger(Util.class);

    private Util() {
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public static double calculateDistance(final double lat1, final double lon1, final double lat2, final double lon2,
                                           final DistanceUnit distanceUnit)
            throws UnsupportedUnitException {
        if (distanceUnit != null) {
            if (lat1 == lat2 && lon1 == lon2) {
                return 0d;
            } else {
                final double theta = lon1 - lon2;
                double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
                dist = Math.acos(dist);
                dist = Math.toDegrees(dist);
                dist = dist * 60 * 1.1515; // miles
                switch (distanceUnit) {
                    case MILE:
                        return dist;
                    case KILOMETER:
                        return dist * 1.609344;
                    case NAUTICAL_MILE:
                        return dist * 0.8684;
                    default:
                        throw new UnsupportedUnitException("DistanceUnit " + distanceUnit.name() + " not supported!");
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void printDot() {
        System.out.print(".");
    }

    public static Map<String, String> getQueryParams(final String paramString) {
        if (paramString != null) {
            final Map<String, String> params = new HashMap<>();
            for (String param : paramString.split("&")) {
                final String[] pair = param.split("=");
                final String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                if (!params.containsKey(key)) {
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                    }
                    params.put(key, value);
                }
            }
            return params;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public static double calculateMbps(final int bytes, final long timeInMs) {
        return (bytes * 8.0) / (timeInMs * 1000.0);
    }

    public static String getConfigProperty(final String key) {
        if (key != null) {
            final String resource = "config.properties";
            try (InputStream is = Util.class.getClassLoader().getResourceAsStream(resource)) {
                final Properties prop = new Properties();
                prop.load(is);
                return prop.getProperty(key);
            } catch (Exception e) {
                logger.error("Loading key \"" + key + "\" from property file \"" + resource + "\" was not possible!", e);
                return null;
            }
        } else {
            throw new IllegalArgumentException();
        }
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
