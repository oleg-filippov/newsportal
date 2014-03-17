package net.filippov.newsportal.exception;

public class NotUniqueUserEmailException extends RuntimeException {

	private static final long serialVersionUID = -6488905601051854966L;

	public NotUniqueUserEmailException(Throwable cause) {
		super(cause);
	}
}
