package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "server")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Server {

    @XmlAttribute(name = "url")
    private String url;

    @XmlAttribute(name = "lat")
    private double lat;

    @XmlAttribute(name = "lon")
    private double lon;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "country")
    private String country;

    @XmlAttribute(name = "cc")
    private String countryCode;

    @XmlAttribute(name = "sponsor")
    private String sponsor;

    @XmlAttribute(name = "id")
    private int id;

    @XmlAttribute(name = "host")
    private String host;

    public Server() {
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public Server(String url, double lat, double lon, String name, String country, String countryCode, String sponsor,
                  int id, String host) {
        this.url = url;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.country = country;
        this.countryCode = countryCode;
        this.sponsor = sponsor;
        this.id = id;
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "Server [url=" + url + ", lat=" + lat + ", lon=" + lon + ", name=" + name + ", country=" + country
                + ", countryCode=" + countryCode + ", sponsor=" + sponsor + ", id=" + id + ", host=" + host + "]";
    }

}
