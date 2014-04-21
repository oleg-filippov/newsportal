package net.filippov.newsportal.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.service.TagService;
import net.filippov.newsportal.web.constants.URL;
import net.filippov.newsportal.web.constants.View;
import net.filippov.newsportal.web.constants.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TagService tagService;
	
	public MainController() {}

	/**
	 * Home-page, get news on first page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.HOME)
	public ModelAndView home(HttpSession session) {
		
		return homeModelAndView(1, session);
	}
	
	/**
	 * Home-page, get news on custom page
	 */
	@RequestMapping(method = RequestMethod.GET,value = URL.HOME_CUSTOM_PAGE)
	public ModelAndView home(@PathVariable("number") Integer pageNumber,
			HttpSession session) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.HOME);
		}
		
		return homeModelAndView(pageNumber, session);
	}
	
	/**
	 * Fill ModelAndView with news data
	 */
	private ModelAndView homeModelAndView(Integer pageNumber, HttpSession session) {
		
		Map<String, Object> articlesData = articleService.getByPage(pageNumber, Web.ARTICLES_PER_PAGE);
		Integer pageCount = (Integer) articlesData.get("pageCount");

		// pageNumber > pageCount
		if (pageCount == -1)
			return new ModelAndView(View.ERROR);
		
		@SuppressWarnings("unchecked")
		List<Article> articlesByPage = (List<Article>) articlesData.get("articlesByPage");
		
		if (session.getAttribute("categories") == null) {
			session.setAttribute("categories", categoryService.getAllTransactionally());
		}
		if (session.getAttribute("tags") == null) {
			session.setAttribute("tags", tagService.getAllTransactionally());
		}
		
		ModelAndView mav = new ModelAndView(View.HOME);
		mav.addObject("pageCount", pageCount);
		mav.addObject("articlesByPage", articlesByPage);
		mav.addObject("currentPage", pageNumber);
		mav.addObject("requestUrl", URL.HOME);
		
		return mav;
	}
	
	/**
	 * About-page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.ABOUT)
	public String aboutPage() {

		return View.ABOUT;
	}

	/**
	 * Contacts-page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.CONTACTS)
	public String contactsPage() {

		return View.CONTACTS;
	}
}
