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
  private int threadsPerURL;

  public DownloadSetting() {
  }

  public DownloadSetting(int testLength, int threadsPerURL) {
    this.testLength = testLength;
    this.threadsPerURL = threadsPerURL;
  }

  public int getTestLength() {
    return testLength;
  }

  public void setTestLength(int testLength) {
    this.testLength = testLength;
  }

  public int getThreadsPerURL() {
    return threadsPerURL;
  }

  public void setThreadsPerURL(int threadsPerURL) {
    this.threadsPerURL = threadsPerURL;
  }

  @Override
  public String toString() {
    return "DownloadSetting{" +
        "testLength=" + testLength +
        ", threadsPerURL=" + threadsPerURL +
        '}';
  }

}
