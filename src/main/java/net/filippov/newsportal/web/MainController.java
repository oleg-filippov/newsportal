package net.filippov.newsportal.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.NewsService;
import net.filippov.newsportal.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@SessionAttributes(value = {"categories", "tags"})
public class MainController {

	// URL's
	private static final String ABOUT_URL = "/about";
	private static final String CONTACTS_URL = "/contacts";
	private static final String ERROR_URL = "/error";
	private static final String HOME_URL = "/";
	private static final String HOME_CUSTOM_PAGE_URL = "/page/{pageNumber}";
	
	// View parameters
	private static final int NEWS_PER_PAGE = 3;
	private static final int TAGS_MAX_COUNT = 50;

	@Autowired
	private NewsService newsService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TagService tagService;
	
	public MainController() {}

	// Home-page, get news on first page
	@RequestMapping(method = RequestMethod.GET, value = HOME_URL)
	public ModelAndView home(HttpSession session) {
		
		return homeModelAndView(1, session);
	}
	
	// Home-page, get news on custom page
	@RequestMapping(method = RequestMethod.GET,value = HOME_CUSTOM_PAGE_URL)
	public ModelAndView home(@PathVariable("pageNumber") Integer pageNumber,
			HttpSession session) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + HOME_URL);
		}
		
		return homeModelAndView(pageNumber, session);
	}
	
	private ModelAndView homeModelAndView(Integer pageNumber, HttpSession session) {
		
		if (session.getAttribute("categoryNames") == null) {
			session.setAttribute("categoryNames", categoryService.getAllNames());
		}
		if (session.getAttribute("tagNames") == null) {
			session.setAttribute("tagNames", tagService.getAllNames(TAGS_MAX_COUNT));
		}
		
		Map<String, Object> newsData = newsService.getByPage(pageNumber, NEWS_PER_PAGE);
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
	
	/**
	 * About-page
	 */
	@RequestMapping(method = RequestMethod.GET, value = ABOUT_URL)
	public String aboutPage() {

		return "about";
	}

	/**
	 * Contacts-page
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTACTS_URL)
	public String contactsPage() {

		return "contacts";
	}
}
