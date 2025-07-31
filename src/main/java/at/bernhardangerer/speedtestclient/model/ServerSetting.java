package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public final class ServerSetting {

    private Servers servers;

}
