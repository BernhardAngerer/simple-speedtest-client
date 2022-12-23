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
import java.util.*;
import java.util.stream.Collectors;

public class ServerSettingsService {
  private final static Logger logger = LogManager.getLogger(ServerSettingsService.class);
  private static final Set<String> SERVER_URLS = new HashSet<>(Arrays.asList(
      "https://www.speedtest.net/speedtest-servers-static.php", "http://c.speedtest.net/speedtest-servers-static.php",
      "https://www.speedtest.net/speedtest-servers.php", "http://c.speedtest.net/speedtest-servers.php"));

  static List<Server> getServersFromXML(byte[] bytes) throws ParsingException, MissingResultException {
    if (bytes != null) {
      try (InputStream is = new ByteArrayInputStream(bytes)) {
        JAXBContext jaxbContext = JAXBContext.newInstance(ServerSetting.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ServerSetting serverSetting = (ServerSetting) jaxbUnmarshaller.unmarshal(is);
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

  public static List<Server> requestServerList(int threadsPerURL) throws MissingResultException {
    if (threadsPerURL > 0) {
      List<Server> servers = SERVER_URLS.stream()
          .map(url -> {
            try {
              byte[] bytes = HttpGetClient.get(String.format("%s?threads=%d", url, threadsPerURL));
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
      Map<Double, Server> closestServers = serverList.stream()
          .collect(Collectors.toMap(
              server -> {
                try {
                  return Util.calculateDistance(lat, lon, server.getLat(), server.getLon(), distanceUnit);
                } catch (UnsupportedUnitException e) {
                  throw new RuntimeException(e);
                }
              },
              server -> server, (o1, o2) -> o1, TreeMap::new));
      Map<Double, Server> limitedClosestServers = closestServers.entrySet().stream()
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
