package at.bernhardangerer.speedtestclient.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigSetting {

  @XmlElement(name = "client")
  private Client client = null;

  @XmlElement(name = "download")
  private DownloadSetting download = null;

  @XmlElement(name = "upload")
  private UploadSetting upload = null;

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public DownloadSetting getDownload() {
    return download;
  }

  public void setDownload(DownloadSetting download) {
    this.download = download;
  }

  public UploadSetting getUpload() {
    return upload;
  }

  public void setUpload(UploadSetting upload) {
    this.upload = upload;
  }

}
