package at.bernhardangerer.speedtestclient.exception;

public final class MissingResultException extends Exception {
    public MissingResultException(final String message) {
        super(message);
    }

    public MissingResultException() {
    }
}
