package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.exception.UnsupportedUnitException;
import at.bernhardangerer.speedtestclient.model.Server;
import at.bernhardangerer.speedtestclient.model.ServerSetting;
import at.bernhardangerer.speedtestclient.type.DistanceUnit;
import at.bernhardangerer.speedtestclient.util.Util;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class ServerSettingsService {
    private static final Logger logger = LogManager.getLogger(ServerSettingsService.class);
    private static final Set<String> SERVER_URLS = new HashSet<>(Arrays.asList(
            "https://www.speedtest.net/speedtest-servers-static.php", "http://c.speedtest.net/speedtest-servers-static.php",
            "https://www.speedtest.net/speedtest-servers.php", "http://c.speedtest.net/speedtest-servers.php"));

    private ServerSettingsService() {
    }

    static List<Server> getServersFromXML(byte[] bytes) throws ParsingException, MissingResultException {
        if (bytes != null) {
            try (InputStream is = new ByteArrayInputStream(bytes)) {
                final JAXBContext jaxbContext = JAXBContext.newInstance(ServerSetting.class);
                final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                final ServerSetting serverSetting = (ServerSetting) jaxbUnmarshaller.unmarshal(is);
                if (serverSetting != null && serverSetting.getServers() != null && serverSetting.getServers().getServerList() != null) {
                    return serverSetting.getServers().getServerList();
                } else {
                    throw new MissingResultException("Missing server list result");
                }
            } catch (IOException | JAXBException e) {
                throw new ParsingException(e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static List<Server> requestServerList(int threadsPerUrl) throws MissingResultException {
        if (threadsPerUrl > 0) {
            final List<Server> servers = SERVER_URLS.stream()
                    .map(url -> {
                        try {
                            final byte[] bytes = HttpGetClient.get(String.format("%s?threads=%d", url, threadsPerUrl));
                            return getServersFromXML(bytes);
                        } catch (ParsingException | MissingResultException | ServerRequestException e) {
                            logger.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            if (!servers.isEmpty()) {
                return servers;
            } else {
                throw new MissingResultException("Empty server list");
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Map<Double, Server> findClosestServers(double lat, double lon, int limit, DistanceUnit distanceUnit,
                                                         List<Server> serverList) throws MissingResultException {
        if (limit > 0 && distanceUnit != null && serverList != null && !serverList.isEmpty()) {
            final Map<Double, Server> closestServers = serverList.stream()
                    .collect(Collectors.toMap(
                            server -> {
                                try {
                                    return Util.calculateDistance(lat, lon, server.getLat(), server.getLon(), distanceUnit);
                                } catch (UnsupportedUnitException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            server -> server, (server1, server2) -> server1, TreeMap::new));
            final Map<Double, Server> limitedClosestServers = closestServers.entrySet().stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!limitedClosestServers.isEmpty()) {
                return limitedClosestServers;
            } else {
                throw new MissingResultException("Empty list for limited closest servers");
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
