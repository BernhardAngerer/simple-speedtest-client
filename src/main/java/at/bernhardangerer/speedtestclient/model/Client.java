package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public double getIspRating() {
        return ispRating;
    }

    public void setIspRating(double ispRating) {
        this.ispRating = ispRating;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Client [ipAddress=" + ipAddress + ", lat=" + lat + ", lon=" + lon + ", isp=" + isp + ", ispRating=" + ispRating
                + ", country=" + country + "]";
    }

}
