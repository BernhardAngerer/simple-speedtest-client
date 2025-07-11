package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public final class Client {

    @XmlAttribute(name = "ip")
    private String ipAddress;

    @XmlAttribute(name = "lat")
    private double lat;

    @XmlAttribute(name = "lon")
    private double lon;

    @XmlAttribute(name = "isp")
    private String isp;

    @XmlAttribute(name = "isprating")
    private double ispRating;

    @XmlAttribute(name = "country")
    private String country;

}
