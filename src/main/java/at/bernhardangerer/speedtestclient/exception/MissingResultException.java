package at.bernhardangerer.speedtestclient.exception;

public class MissingResultException extends Exception {
  public MissingResultException(String message) {
	  super(message);
  }

  public MissingResultException() {
  }
}
