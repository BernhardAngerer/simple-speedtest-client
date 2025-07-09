package at.bernhardangerer.speedtestclient.exception;

public final class ServerRequestException extends Exception {
    public ServerRequestException(String message) {
        super(message);
    }

    public ServerRequestException(Exception exception) {
        super(exception);
    }
}
