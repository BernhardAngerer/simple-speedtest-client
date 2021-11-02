package at.bernhardangerer.speedtestclient.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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