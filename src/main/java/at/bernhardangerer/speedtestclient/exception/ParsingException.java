package at.bernhardangerer.speedtestclient.exception;

public final class ParsingException extends Exception {
    public ParsingException(final Exception exception) {
        super(exception);
    }
}
