package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public final class ConfigSetting {

    @XmlElement(name = "client")
    private Client client;
    @XmlElement(name = "download")
    private DownloadSetting download;
    @XmlElement(name = "upload")
    private UploadSetting upload;

}
