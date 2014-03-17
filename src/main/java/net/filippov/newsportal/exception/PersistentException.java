package net.filippov.newsportal.exception;

public class PersistentException extends RuntimeException {

	private static final long serialVersionUID = -681635380193322428L;

	public PersistentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistentException(String message) {
		super(message);
	}
}
