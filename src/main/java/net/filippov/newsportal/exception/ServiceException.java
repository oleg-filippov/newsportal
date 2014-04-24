package net.filippov.newsportal.exception;

/**
 * Common service-layer exception
 * 
 * @author Oleg Filippov
 */
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
