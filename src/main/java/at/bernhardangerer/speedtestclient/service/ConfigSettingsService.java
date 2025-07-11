package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ConfigSettingsService {
    private static final String CONFIG_URL = "https://www.speedtest.net/speedtest-config.php";

    private ConfigSettingsService() {
    }

    static ConfigSetting getSettingFromXml(byte[] xml) throws ParsingException {
        if (xml != null) {
            try (InputStream is = new ByteArrayInputStream(xml)) {
                final JAXBContext jaxbContext = JAXBContext.newInstance(ConfigSetting.class);
                final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
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
            return getSettingFromXml(bytes);
        } else {
            throw new MissingResultException("Missing result for config settings request");
        }
    }

}
