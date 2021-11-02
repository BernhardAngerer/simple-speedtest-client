package at.bernhardangerer.speedtestclient.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerSetting {

  @XmlElement(name = "servers")
  private Servers servers = null;

  public Servers getServers() {
    return servers;
  }

  public void setServers(Servers servers) {
    this.servers = servers;
  }

}
