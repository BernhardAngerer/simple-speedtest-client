package at.bernhardangerer.speedtestclient.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "upload")
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadSetting {

  @XmlAttribute(name = "ratio")
  private int ratio;

  @XmlAttribute(name = "maxchunkcount")
  private int maxChunkCount;

  @XmlAttribute(name = "threads")
  private int threads;

  @XmlAttribute(name = "testlength")
  private int testLength;

  public UploadSetting() {
  }

  public UploadSetting(int ratio, int maxChunkCount, int threads, int testLength) {
    this.ratio = ratio;
    this.maxChunkCount = maxChunkCount;
    this.threads = threads;
    this.testLength = testLength;
  }

  public int getRatio() {
    return ratio;
  }

  public void setRatio(int ratio) {
    this.ratio = ratio;
  }

  public int getMaxChunkCount() {
    return maxChunkCount;
  }

  public void setMaxChunkCount(int maxChunkCount) {
    this.maxChunkCount = maxChunkCount;
  }

  public int getThreads() {
    return threads;
  }

  public void setThreads(int threads) {
    this.threads = threads;
  }

  public int getTestLength() {
    return testLength;
  }

  public void setTestLength(int testLength) {
    this.testLength = testLength;
  }

  @Override
  public String toString() {
    return "UploadSetting{" +
        "ratio=" + ratio +
        ", maxChunkCount=" + maxChunkCount +
        ", threads=" + threads +
        ", testLength=" + testLength +
        '}';
  }

}
