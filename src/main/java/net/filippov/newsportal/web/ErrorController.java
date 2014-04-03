package net.filippov.newsportal.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorController.class);

	private static final String ERROR_URL = "/error";

	public ErrorController() {}

	// Error-page
	@RequestMapping(value = ERROR_URL)
	public String errorPage(Model model, HttpServletRequest request) {

		Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");

		switch (statusCode) {
		case 400:
			model.addAttribute("messageProperty", "error.400");
			break;
		case 404:
			model.addAttribute("messageProperty", "error.404");
			break;
		case 500:
			model.addAttribute("messageProperty", "error.500");
			break;
		default:
			model.addAttribute("messageProperty", "error.default");
		}

		String requestUrl = (String) request
				.getAttribute("javax.servlet.error.request_uri");
		if (requestUrl == null)
			requestUrl = "Unknown";

		Throwable ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		if (ex != null) {
			LOG.error(ex.getCause().getMessage(), ex.getCause());
			// hidden attribute
			model.addAttribute("exception", ex.getCause());
		}

		model.addAttribute("statusCode", statusCode);
		model.addAttribute("requestUrl", requestUrl);

		return "error";
	}
}
