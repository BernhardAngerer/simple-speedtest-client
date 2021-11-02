package at.bernhardangerer.speedtestclient.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigSetting {

  @XmlElement(name = "client")
  private ClientSetting client = null;

  @XmlElement(name = "download")
  private DownloadSetting download = null;

  @XmlElement(name = "upload")
  private UploadSetting upload = null;

  public ClientSetting getClient() {
    return client;
  }

  public void setClient(ClientSetting client) {
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
