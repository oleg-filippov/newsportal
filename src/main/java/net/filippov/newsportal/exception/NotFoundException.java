package net.filippov.newsportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throws if searching item (Article, Tag, Category...) not found
 * 
 * @author Oleg Filippov
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7582550023114509895L;

	public NotFoundException(String message) {
		super(message);
	}
}
