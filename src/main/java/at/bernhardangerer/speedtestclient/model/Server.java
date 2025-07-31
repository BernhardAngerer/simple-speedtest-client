package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "server")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Server {

    @XmlAttribute(name = "url")
    private String url;
    @XmlAttribute(name = "lat")
    private Double lat;
    @XmlAttribute(name = "lon")
    private Double lon;
    @XmlAttribute(name = "name")
    private String city;
    @XmlAttribute(name = "country")
    private String country;
    @XmlAttribute(name = "cc")
    private String isoAlpha2CountryCode;
    @XmlAttribute(name = "sponsor")
    private String sponsor;
    @XmlAttribute(name = "id")
    private Integer id;
    @XmlAttribute(name = "host")
    private String host;

}
