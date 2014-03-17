package net.filippov.newsportal.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -1603048637032593628L;

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

}
