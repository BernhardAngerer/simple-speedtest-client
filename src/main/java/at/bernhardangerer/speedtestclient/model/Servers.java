package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "servers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Servers {

  @XmlElement(name = "server")
  private List<Server> serverList = null;

  public List<Server> getServerList() {
    return serverList;
  }

  public void setServerList(List<Server> serverList) {
    this.serverList = serverList;
  }
}