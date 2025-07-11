package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement(name = "servers")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public final class Servers {

    @XmlElement(name = "server")
    private List<Server> serverList;

}
