package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.util.Util;
import jakarta.xml.bind.DatatypeConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public final class ShareUrlService {
  private static final Logger logger = LogManager.getLogger(ShareUrlService.class);

  public static String createShareURL(int serverId, double latency, double uploadMbps, double downloadMbps) throws IOException, MissingResultException, ServerRequestException {
    if (serverId > 0 && uploadMbps > 0 && downloadMbps > 0) {
      final int ping = (int) Math.round(latency);
      final int uploadKbps = (int) Math.round(uploadMbps * 1000.0d);
      final int downloadKbps = (int) Math.round(downloadMbps * 1000.0d);
      final String md5Hash = generateMD5Hash(String.format("%s-%s-%s-%s", ping, uploadKbps, downloadKbps, "297aae72"));
      final String encodedBody = String.format("serverid=%s&hash=%s&ping=%s&download=%s&upload=%s&accuracy=1",
          serverId, md5Hash, ping, downloadKbps, uploadKbps);
      final String result = HttpPostClient.postBodyWithSharedData("https://www.speedtest.net/api/api.php", encodedBody);
      final Map<String, String> queryParams = Util.getQueryParams(result);
      if (queryParams.containsKey("resultid") && queryParams.get("resultid") != null) {
        return String.format("http://www.speedtest.net/result/%s.png", queryParams.get("resultid"));
      } else {
        throw new MissingResultException("Missing result for shareURL request");
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

  static String generateMD5Hash(String data) {
    if (data != null) {
      try {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.getBytes());
        return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
      } catch (NoSuchAlgorithmException e) {
        logger.error(e.getMessage(), e);
        return null;
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

}
