package net.filippov.newsportal.exception;

public class NotUniqueUserLoginException extends RuntimeException {

	private static final long serialVersionUID = 684455195352627556L;

	public NotUniqueUserLoginException(Throwable cause) {
		super(cause);
	}
}
