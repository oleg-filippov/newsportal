package net.filippov.newsportal.web;

import java.io.IOException;

import net.filippov.newsportal.exception.UnacceptableFileFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * Exception-handler
 * Catches an exception, logs it and sends validation message as @ResponseBody
 * 
 * @author Oleg Filippov
 */
@ControllerAdvice
public class CustomExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@Autowired
	ApplicationContext context;

	/**
	 * Handle {@link MaxUploadSizeExceededException}
	 * Image size is more than allowed
	 * 
	 * @param e exception to catch
	 * @return validation message
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public String handleMaxUploadSizeException(MaxUploadSizeExceededException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.maxSize", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	/**
	 * Handle {@link UnacceptableFileFormatException}
	 * An attempt to upload the image with not allowed format
	 * 
	 * @param e exception to catch
	 * @return validation message
	 */
	@ExceptionHandler(UnacceptableFileFormatException.class)
	@ResponseBody
	public String handleFileFormatException(UnacceptableFileFormatException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.type", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	/**
	 * Handle {@link IOException}, {@link MultipartException}
	 * File-system exceptions during image-upload process
	 * 
	 * @param e exception to catch
	 * @return validation message
	 */
	@ExceptionHandler(value = {IOException.class, MultipartException.class})
	@ResponseBody
	public String handleCustomException(Exception e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.custom", null,
				LocaleContextHolder.getLocale());
		return message;
	}
}
