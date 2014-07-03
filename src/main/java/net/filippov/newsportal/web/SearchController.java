package net.filippov.newsportal.web;

import java.util.List;
import java.util.Map;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.exception.NotFoundException;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.web.constants.URL;
import net.filippov.newsportal.web.constants.View;
import net.filippov.newsportal.web.constants.Common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for search-actions
 * 
 * @author Oleg Filippov
 */
@Controller
public class SearchController {

	private ArticleService articleService;
	private ApplicationContext context;
	
	/**
	 * Constructor autowiring needed services
	 * 
	 * @param articleService {@link ArticleService}
	 * @param context {@link ApplicationContext}
	 */
	@Autowired
	public SearchController(ArticleService articleService,
			ApplicationContext context) {
		this.articleService = articleService;
		this.context = context;
	}

	/**
	 * Search by category - first page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_CATEGORY)
	public ModelAndView viewArticlesByCategory(@PathVariable("name") String categoryName) {

		return categoryModelAndView(1, categoryName);
	}

	/**
	 * Search by category - custom page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_CATEGORY_CUSTOM_PAGE)
	public ModelAndView viewArticlesByCategory(@PathVariable("name") String categoryName,
			@PathVariable("number") Integer pageNumber) {

		validatePageNumber(pageNumber);
		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_CATEGORY);
		}
		return categoryModelAndView(pageNumber, categoryName);
	}
	
	/**
	 * Prepare searchByCategory-objects for search-ModelAndView
	 */
	private ModelAndView categoryModelAndView(Integer pageNumber, String categoryName) {
		
		Map<String, Object> articlesData = articleService.getByPageByCategoryName(
				pageNumber, Common.ARTICLES_PER_PAGE, categoryName);
		String requestUrl = String.format("/category/%s/", categoryName);
		String message = String.format(context.getMessage("search.result.byCategory", null,
				LocaleContextHolder.getLocale()), categoryName);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * Search by tag - first page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_TAG)
	public ModelAndView viewArticlesByTag(@PathVariable("name") String tagName) {

		return tagModelAndView(1, tagName);
	}

	/**
	 * Search by tag - custom page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_TAG_CUSTOM_PAGE)
	public ModelAndView viewArticlesByTag(@PathVariable("name") String tagName,
			@PathVariable("number") Integer pageNumber) {

		validatePageNumber(pageNumber);
		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_TAG);
		}
		return tagModelAndView(pageNumber, tagName);
	}

	/**
	 * Prepare searchByTag-objects for search-ModelAndView
	 */
	private ModelAndView tagModelAndView(Integer pageNumber, String tagName) {

		Map<String, Object> articlesData = articleService.getByPageByTagName(
				pageNumber, Common.ARTICLES_PER_PAGE, tagName);
		String requestUrl = String.format("/tags/%s/", tagName);
		String message = String.format(context.getMessage("search.result.byTag", null,
				LocaleContextHolder.getLocale()), tagName);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * Search by user - first page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_USER)
	public ModelAndView viewArticlesByUser(@PathVariable("id") Long userId) {

		return userModelAndView(1, userId);
	}
	
	/**
	 * Search by user - first page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_USER_CUSTOM_PAGE)
	public ModelAndView viewArticlesByUser(@PathVariable("id") Long userId,
			@PathVariable("number") Integer pageNumber) {

		validatePageNumber(pageNumber);
		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_USER);
		}
		return userModelAndView(pageNumber, userId);
	}
	
	/**
	 * Prepare searchByUser-objects for search-ModelAndView
	 */
	private ModelAndView userModelAndView(Integer pageNumber, Long userId) {
		
		Map<String, Object> articlesData = articleService.getByPageByUserId(
				pageNumber, Common.ARTICLES_PER_PAGE, userId);
		String userLogin = (String) articlesData.get("userLogin");
		String requestUrl = String.format("/user/%d/articles/", userId);
		String message = String.format(context.getMessage("search.result.byUser", null,
				LocaleContextHolder.getLocale()), userLogin);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * Search by fragment submit-form
	 */
	@RequestMapping(method = RequestMethod.POST, value = URL.SEARCH_BY_FRAGMENT_SUBMIT)
	public String searchSubmit(@RequestParam("fragment") String fragment) {
		
		return "redirect:/search/" + fragment;
	}
	
	/**
	 * Search by fragment - first page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_FRAGMENT)
	public ModelAndView viewArticlesByFragment(@PathVariable("fragment") String fragment) {
		
		return fragmentModelAndView(1, fragment);
	}
	
	/**
	 * Search by fragment - custom page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_FRAGMENT_CUSTOM_PAGE)
	public ModelAndView viewArticlesByFragment(@PathVariable("fragment") String fragment,
			@PathVariable("number") Integer pageNumber) {

		validatePageNumber(pageNumber);
		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_FRAGMENT);
		}
		return fragmentModelAndView(pageNumber, fragment);
	}
	
	/**
	 * Prepare searchByFragment-objects for search-ModelAndView
	 */
	private ModelAndView fragmentModelAndView(Integer pageNumber, String fragment) {

		Map<String, Object> articlesData = articleService.getByPageByFragment(
				pageNumber, Common.ARTICLES_PER_PAGE, fragment);
		String requestUrl = String.format("/search/%s/", fragment);
		String message = String.format(context.getMessage("search.result.byFragment", null,
				LocaleContextHolder.getLocale()), fragment);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * Fill search-ModelAndView with needed objects
	 * 
	 * @param articlesData	Map with articles
	 * @param pageNumber	number of page
	 * @param message		localized search-message
	 * @param requestUrl	root URL
	 * @return filled {@link ModelAndView}
	 */
	private ModelAndView searchModelAndView(Map<String, Object> articlesData,
			Integer pageNumber, String message, String requestUrl) {
		
		// Set required model attributes
		ModelAndView mav = new ModelAndView(View.SEARCH)
				.addObject("message", message)
				.addObject("requestUrl", requestUrl)
				.addObject("currentPage", pageNumber);
		
		if (articlesData.isEmpty()) {	// articleCount == 0
			return mav;
		}
		
		Integer pageCount = (Integer) articlesData.get("pageCount");

		@SuppressWarnings("unchecked")
		List<Article> articlesByPage = (List<Article>) articlesData.get("articlesByPage");

		return mav.addObject("pageCount", pageCount)
				.addObject("articlesByPage", articlesByPage);
	}
	
	private void validatePageNumber(int pageNumber) {
		if (pageNumber < 1) {
			throw new NotFoundException("Page < 1");
		}
	}
}