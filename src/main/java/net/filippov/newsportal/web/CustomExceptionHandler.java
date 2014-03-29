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

@ControllerAdvice
public class CustomExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@Autowired
	ApplicationContext context;

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public String handleMaxUploadSizeException(MaxUploadSizeExceededException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.maxSize", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	@ExceptionHandler(UnacceptableFileFormatException.class)
	@ResponseBody
	public String handleFileFormatException(UnacceptableFileFormatException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.type", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	@ExceptionHandler(value = {IOException.class, MultipartException.class})
	@ResponseBody
	public String handleCustomException(Exception e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.custom", null,
				LocaleContextHolder.getLocale());
		return message;
	}

}
