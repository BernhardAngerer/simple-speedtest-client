package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "download")
@XmlAccessorType(XmlAccessType.FIELD)
public final class DownloadSetting {

    @XmlAttribute(name = "testlength")
    private int testLength;

    @XmlAttribute(name = "threadsperurl")
    private int threadsPerUrl;

    public DownloadSetting() {
    }

    public DownloadSetting(int testLength, int threadsPerUrl) {
        this.testLength = testLength;
        this.threadsPerUrl = threadsPerUrl;
    }

    public int getTestLength() {
        return testLength;
    }

    public void setTestLength(int testLength) {
        this.testLength = testLength;
    }

    public int getThreadsPerUrl() {
        return threadsPerUrl;
    }

    public void setThreadsPerUrl(int threadsPerUrl) {
        this.threadsPerUrl = threadsPerUrl;
    }

    @Override
    public String toString() {
        return "DownloadSetting {"
                + "testLength=" + testLength
                + ", threadsPerUrl=" + threadsPerUrl
                + '}';
    }

}
