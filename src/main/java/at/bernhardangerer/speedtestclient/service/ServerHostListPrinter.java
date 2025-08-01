package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.Servers;
import at.bernhardangerer.speedtestclient.util.Constant;
import at.bernhardangerer.speedtestclient.util.CsvUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.util.Arrays;
import java.util.List;

public final class ServerHostListPrinter {

    private ServerHostListPrinter() {
    }

    public static void printJson(final List<Server> servers) {
        if (isEmptyOrNull(servers)) {
            return;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(servers));
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize servers to JSON: " + e.getMessage());
        }
    }

    public static void printXml(final List<Server> servers) {
        if (isEmptyOrNull(servers)) {
            return;
        }
        try {
            final JAXBContext context = JAXBContext.newInstance(Servers.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new Servers(servers), System.out);
        } catch (JAXBException e) {
            System.err.println("Failed to serialize servers to XML: " + e.getMessage());
        }
    }

    public static void printCsv(final List<Server> servers, final String delimiter) {
        if (isEmptyOrNull(servers)) {
            return;
        }
        final String finalDelimiter = delimiter != null && !delimiter.isEmpty() ? delimiter : Constant.COMMA;

        final List<String> keys = Arrays.asList("id", "sponsor", "city", "country", "countryCode", "host", "url", "lat", "lon");
        System.out.println(CsvUtil.joinStrings(keys, finalDelimiter));

        for (final Server s : servers) {
            final List<Object> unformattedValues = Arrays.asList(s.getId(), s.getSponsor(), s.getCity(), s.getCountry(),
                    s.getIsoAlpha2CountryCode(), s.getHost(), s.getUrl(), s.getLat(), s.getLon());
            final List<String> formattedValues = CsvUtil.formatCsvValues(unformattedValues, finalDelimiter);
            System.out.println(CsvUtil.joinStrings(formattedValues, finalDelimiter));
        }
    }

    public static void printHumanReadable(final List<Server> servers) {
        if (isEmptyOrNull(servers)) {
            return;
        }
        System.out.println("List of valid server hosts:");
        servers.forEach(server -> System.out.println(server.getHost()));
    }

    private static boolean isEmptyOrNull(final List<Server> list) {
        if (list == null || list.isEmpty()) {
            System.err.println("No servers available.");
            return true;
        }
        return false;
    }
}
