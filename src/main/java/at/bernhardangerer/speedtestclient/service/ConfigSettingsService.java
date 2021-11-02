package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigSettingsService {
  private static final String CONFIG_URL = "https://www.speedtest.net/speedtest-config.php";

  static ConfigSetting getSettingFromXML(byte[] xml) throws ParsingException {
    if (xml != null) {
      try (InputStream is = new ByteArrayInputStream(xml)) {
        JAXBContext jaxbContext = JAXBContext.newInstance(ConfigSetting.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (ConfigSetting) jaxbUnmarshaller.unmarshal(is);
      } catch (IOException | JAXBException e) {
        throw new ParsingException(e);
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static ConfigSetting requestSetting() throws MissingResultException, ServerRequestException, ParsingException {
    final byte[] bytes = HttpGetClient.get(CONFIG_URL);
    if (bytes != null) {
      return getSettingFromXML(bytes);
    } else {
      throw new MissingResultException("Missing result for config settings request");
    }
  }

}
