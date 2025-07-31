package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Client {

    @XmlAttribute(name = "ip")
    private String ipAddress;
    @XmlAttribute(name = "lat")
    private Double lat;
    @XmlAttribute(name = "lon")
    private Double lon;
    @XmlAttribute(name = "isp")
    private String isp;
    @XmlAttribute(name = "isprating")
    private Double ispRating;
    @XmlAttribute(name = "country")
    private String isoAlpha2CountryCode;

}
