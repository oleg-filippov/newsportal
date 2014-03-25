package net.filippov.newsportal.web;

import java.util.Collection;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("news")
public class MainController {
	
	private static final String ABOUT_URL = "/about";
	private static final String CONTACTS_URL = "/contacts";

	@Autowired
	private NewsService newsService;

	// Home-page, list news
	@RequestMapping(method = RequestMethod.GET, value = { "/", "", "/home", "/index" })
	public String home(Model model, SessionStatus status) {

		status.setComplete();
		Collection<News> allNews = newsService.getAll();
		model.addAttribute("allNews", allNews);

		return "home";
	}
	
	// About-page
	@RequestMapping(method=RequestMethod.GET, value = ABOUT_URL)
	public String aboutPage() {
		
		return "about";
	}
	
	// Contacts-page
	@RequestMapping(method=RequestMethod.GET, value = CONTACTS_URL)
	public String contactsPage() {
		
		return "contacts";
	}
}
