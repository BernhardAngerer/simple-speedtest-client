package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Client {

  @XmlAttribute(name = "ip")
  private String ip;

  @XmlAttribute(name = "lat")
  private double lat;

  @XmlAttribute(name = "lon")
  private double lon;

  @XmlAttribute(name = "isp")
  private String isp;

  @XmlAttribute(name = "isprating")
  private double isprating;

  @XmlAttribute(name = "country")
  private String country;

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
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

  public double getIsprating() {
    return isprating;
  }

  public void setIsprating(double isprating) {
    this.isprating = isprating;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return "Client [ip=" + ip + ", lat=" + lat + ", lon=" + lon + ", isp=" + isp + ", isprating=" + isprating
        + ", country=" + country + "]";
  }

}
