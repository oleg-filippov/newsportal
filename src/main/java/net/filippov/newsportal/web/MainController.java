package net.filippov.newsportal.web;

import java.text.MessageFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class MainController {

	// URL's
	private static final String ABOUT_URL = "/about";
	private static final String CONTACTS_URL = "/contacts";
	private static final String ERROR_URL = "/error";

	@Autowired
	private NewsService newsService;

	// Home-page, list news
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String home(Model model, SessionStatus status) {

		Collection<News> allNews = newsService.getAll();
		model.addAttribute("allNews", allNews);

		return "home";
	}

	// About-page
	@RequestMapping(method = RequestMethod.GET, value = ABOUT_URL)
	public String aboutPage() {

		return "about";
	}

	// Contacts-page
	@RequestMapping(method = RequestMethod.GET, value = CONTACTS_URL)
	public String contactsPage() {

		return "contacts";
	}

	// Error-page
	@RequestMapping(value = ERROR_URL)
	public String errorPage(Model model, HttpServletRequest request) {

		model.addAttribute("messageProperty", "error.signIn");
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		String message = MessageFormat.format("{0} returned for {1}", 
				statusCode, requestUri);

		model.addAttribute("errorMessage", message);
		  
		return "errors/error";
	}
}
