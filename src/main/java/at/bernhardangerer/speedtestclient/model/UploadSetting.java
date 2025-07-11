package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "upload")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UploadSetting {

    @XmlAttribute(name = "ratio")
    private int ratio;

    @XmlAttribute(name = "maxchunkcount")
    private int maxChunkCount;

    @XmlAttribute(name = "threads")
    private int threads;

    @XmlAttribute(name = "testlength")
    private int testLength;

}
