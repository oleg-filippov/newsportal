package net.filippov.newsportal.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.exception.UnacceptableFileFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	ApplicationContext context;

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public String handleCustomException(MaxUploadSizeExceededException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.maxSize", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	@ExceptionHandler(UnacceptableFileFormatException.class)
	@ResponseBody
	public String handleCustomException(UnacceptableFileFormatException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.type", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	@ExceptionHandler(value = { IOException.class, MultipartException.class })
	@ResponseBody
	public String handleCustomException(MultipartException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("validation.file.custom", null,
				LocaleContextHolder.getLocale());
		return message;
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleCustomException(Model model, ServiceException e) {

		LOG.error(e.getMessage(), e);
		String message = context.getMessage("error.serverError", null,
				LocaleContextHolder.getLocale());
		model.addAttribute("errorMessage", message);
		
		return "errors/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleCustomException(Model model, Exception e) throws IOException {

		LOG.error(e.getMessage(), e);

		return "errors/error";
	}

}
