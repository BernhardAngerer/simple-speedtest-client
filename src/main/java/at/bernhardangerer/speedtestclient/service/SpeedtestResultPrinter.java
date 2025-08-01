package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.model.Client;
import at.bernhardangerer.speedtestclient.model.LatencyTestResult;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.SpeedtestResult;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Constant;
import at.bernhardangerer.speedtestclient.util.CsvUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.util.Arrays;
import java.util.List;

public final class SpeedtestResultPrinter {

    private SpeedtestResultPrinter() {
    }

    public static void printJson(final SpeedtestResult speedtestResult) {
        if (speedtestResult == null) {
            return;
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(speedtestResult));
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize speedtestResult to JSON: " + e.getMessage());
        }
    }

    public static void printXml(final SpeedtestResult speedtestResult) {
        if (speedtestResult == null) {
            return;
        }
        try {
            final JAXBContext context = JAXBContext.newInstance(SpeedtestResult.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(speedtestResult, System.out);
        } catch (JAXBException e) {
            System.err.println("Failed to serialize speedtestResult to XML: " + e.getMessage());
        }
    }

    public static void printCsv(final SpeedtestResult speedtestResult, final String delimiter) {
        if (speedtestResult == null) {
            return;
        }
        final String finalDelimiter = delimiter != null && !delimiter.isEmpty() ? delimiter : Constant.COMMA;

        final List<String> keys = Arrays.asList("startTime", "endTime", "clientIp", "clientLat", "clientLon",
                "clientIsp", "clientIspRating", "clientCountry", "serverId", "serverCity", "serverHost", "serverCountry",
                "serverLat", "serverLon", "serverSponsor", "serverUrl", "latencyMs", "distanceKm", "downloadMbps",
                "downloadBytes", "downloadDurationMs", "uploadMbps", "uploadBytes", "uploadDurationMs", "shareUrl");
        System.out.println(CsvUtil.joinStrings(keys, finalDelimiter));

        final List<Object> unformattedValues = createCsvValueList(speedtestResult);
        final List<String> formattedValues = CsvUtil.formatCsvValues(unformattedValues, finalDelimiter);
        System.out.println(CsvUtil.joinStrings(formattedValues, finalDelimiter));
    }

    private static List<Object> createCsvValueList(final SpeedtestResult speedtestResult) {
        final Client c = speedtestResult.getClient();
        final Server s = speedtestResult.getServer();
        final LatencyTestResult l = speedtestResult.getLatency();
        final TransferTestResult d = speedtestResult.getDownload();
        final TransferTestResult u = speedtestResult.getUpload();

        return Arrays.asList(
                speedtestResult.getStartTime(),
                speedtestResult.getEndTime(),
                c != null ? c.getIpAddress() : null,
                c != null ? c.getLat() : null,
                c != null ? c.getLon() : null,
                c != null ? c.getIsp() : null,
                c != null ? c.getIspRating() : null,
                c != null ? c.getIsoAlpha2CountryCode() : null,
                s != null ? s.getId() : null,
                s != null ? s.getCity() : null,
                s != null ? s.getHost() : null,
                s != null ? s.getCountry() : null,
                s != null ? s.getLat() : null,
                s != null ? s.getLon() : null,
                s != null ? s.getSponsor() : null,
                s != null ? s.getUrl() : null,
                l != null ? l.getLatency() : null,
                l != null ? l.getDistance() : null,
                d != null ? d.getRateInMbps() : null,
                d != null ? d.getBytes() : null,
                d != null ? d.getDurationInMs() : null,
                u != null ? u.getRateInMbps() : null,
                u != null ? u.getBytes() : null,
                u != null ? u.getDurationInMs() : null,
                speedtestResult.getShareUrl()
        );
    }

}
