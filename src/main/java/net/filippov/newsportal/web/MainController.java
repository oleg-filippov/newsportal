package net.filippov.newsportal.web;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	// URL's
	private static final String ABOUT_URL = "/about";
	private static final String CONTACTS_URL = "/contacts";
	private static final String ERROR_URL = "/error";
	private static final String HOME_URL = "/";
	private static final String HOME_CUSTOM_PAGE_URL = "/page/{pageNumber}";
	
	private static final int NEWS_PER_PAGE = 9;

	@Autowired
	private NewsService service;
	
	public MainController() {}

	// Home-page, get news on first page
	@RequestMapping(method = RequestMethod.GET, value = HOME_URL)
	public ModelAndView home() {
		
		return viewNewsPerPage(1);
	}
	
	// Home-page, get news on custom page
	@RequestMapping(method = RequestMethod.GET,value = HOME_CUSTOM_PAGE_URL)
	public ModelAndView home(@PathVariable("pageNumber") Integer pageNumber) {

		if (pageNumber == 1)
			return new ModelAndView("redirect:" + HOME_URL);
		
		return viewNewsPerPage(pageNumber);
	}
	
	private ModelAndView viewNewsPerPage(Integer pageNumber) {
		
		Map<String, Object> newsData = service.getByPage(pageNumber, NEWS_PER_PAGE);
		Integer pagesCount = (Integer) newsData.get("pagesCount");

		// pageNumber > pagesCount
		if (pagesCount == -1)
			return new ModelAndView("redirect:" + ERROR_URL);
		
		@SuppressWarnings("unchecked")
		List<News> newsByPage = (List<News>) newsData.get("newsByPage");
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("pagesCount", pagesCount);
		mav.addObject("newsByPage", newsByPage);
		mav.addObject("currentPage", pageNumber);
		mav.setViewName("home");
		
		return mav;
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
