package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConfigSettingsServiceTest {

    @Test
    public void getSettingFromXml() throws ParsingException {
        final ConfigSetting result = ConfigSettingsService.getSettingFromXml(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<settings>\n"
                + "<client ip=\"37.19.195.144\" lat=\"48.1936\" lon=\"16.3726\" isp=\"Datacamp Limited\" isprating=\"3.7\" "
                + "rating=\"0\" ispdlavg=\"0\" ispulavg=\"0\" loggedin=\"0\" country=\"AT\" />\n"
                + "<server-config threadcount=\"4\" ignoreids=\"\" notonmap=\"\" forcepingid=\"\" preferredserverid=\"\"/>\n"
                + "<licensekey>f7a45ced624d3a70-1df5b7cd427370f7-b91ee21d6cb22d7b</licensekey>\n"
                + "<customer>speedtest</customer>\n"
                + "<odometer start=\"19601573884\" rate=\"12\"/>\n"
                + "<times dl1=\"5000000\" dl2=\"35000000\" dl3=\"800000000\" ul1=\"1000000\" ul2=\"8000000\" ul3=\"35000000\"/>\n"
                + "<download testlength=\"10\" initialtest=\"250K\" mintestsize=\"250K\" threadsperurl=\"4\"/>\n"
                + "<upload testlength=\"10\" ratio=\"5\" initialtest=\"0\" mintestsize=\"32K\" threads=\"2\" maxchunksize=\"512K\" "
                + "maxchunkcount=\"50\" threadsperurl=\"4\"/>\n"
                + "<latency testlength=\"10\" waittime=\"50\" timeout=\"20\"/>\n"
                + "<socket-download testlength=\"15\" initialthreads=\"4\" minthreads=\"4\" maxthreads=\"32\" threadratio=\"750K\" "
                + "maxsamplesize=\"5000000\" minsamplesize=\"32000\" startsamplesize=\"1000000\" startbuffersize=\"1\" "
                + "bufferlength=\"5000\" packetlength=\"1000\" readbuffer=\"65536\"/>\n"
                + "<socket-upload testlength=\"15\" initialthreads=\"dyn:tcpulthreads\" minthreads=\"dyn:tcpulthreads\" maxthreads=\"32\" "
                + "threadratio=\"750K\" maxsamplesize=\"1000000\" minsamplesize=\"32000\" startsamplesize=\"100000\" "
                + "startbuffersize=\"2\" bufferlength=\"1000\" packetlength=\"1000\" disabled=\"false\"/>\n"
                + "<socket-latency testlength=\"10\" waittime=\"50\" timeout=\"20\"/>\n"
                + "<translation lang=\"xml\">\n"
                + "</translation>\n"
                + "</settings>").getBytes());
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getClient());
        Assertions.assertEquals("37.19.195.144", result.getClient().getIpAddress());
        Assertions.assertNotNull(result.getDownload());
        Assertions.assertEquals(4, result.getDownload().getThreadsPerUrl());
        Assertions.assertNotNull(result.getUpload());
        Assertions.assertEquals(5, result.getUpload().getRatio());
    }

    @Test
    public void getSettingFromXmlInvalidParameter() {
        assertThrows(IllegalArgumentException.class, () -> ConfigSettingsService.getSettingFromXml(null));
    }

}
