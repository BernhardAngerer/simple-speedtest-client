package at.bernhardangerer.speedtestclient.exception;

public final class ParsingException extends Exception {
    public ParsingException(Exception exception) {
        super(exception);
    }
}
