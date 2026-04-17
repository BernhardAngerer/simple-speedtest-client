package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ConfigSettingsService {
    private static final String CONFIG_URL = "https://www.speedtest.net/speedtest-config.php";

    private ConfigSettingsService() {
    }

    static ConfigSetting getSettingFromXml(final byte[] xml) throws ParsingException {
        if (xml != null) {
            try (InputStream is = new ByteArrayInputStream(xml)) {
                final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                saxParserFactory.setNamespaceAware(true);
                saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                saxParserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                saxParserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

                final XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
                final SAXSource saxSource = new SAXSource(xmlReader, new InputSource(is));

                final JAXBContext jaxbContext = JAXBContext.newInstance(ConfigSetting.class);
                final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                return (ConfigSetting) jaxbUnmarshaller.unmarshal(saxSource);
            } catch (IOException | JAXBException | ParserConfigurationException | SAXException e) {
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
