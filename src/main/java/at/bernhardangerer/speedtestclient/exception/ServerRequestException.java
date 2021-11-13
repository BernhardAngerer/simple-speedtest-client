package at.bernhardangerer.speedtestclient.exception;

public class ServerRequestException extends Exception {
  public ServerRequestException(String message) {
    super(message);
  }

  public ServerRequestException(Exception e) {
    super(e);
  }
}
