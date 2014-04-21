package net.filippov.newsportal.web;

import java.util.List;
import java.util.Map;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.web.constants.View;
import net.filippov.newsportal.web.constants.URL;
import net.filippov.newsportal.web.constants.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	ApplicationContext context;
	
	public SearchController() {}

	/**
	 * Search by category - first page
	 * @param categoryName
	 * @return search by category ModelAndView
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_CATEGORY)
	public ModelAndView viewArticlesByCategory(@PathVariable("name") String categoryName) {

		return categoryModelAndView(1, categoryName);
	}

	/**
	 * Search by category - custom page
	 * @param categoryName
	 * @param pageNumber
	 * @return search by category ModelAndView
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_CATEGORY_CUSTOM_PAGE)
	public ModelAndView viewArticlesByCategory(@PathVariable("name") String categoryName,
			@PathVariable("number") Integer pageNumber) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_CATEGORY);
		}
		return categoryModelAndView(pageNumber, categoryName);
	}
	
	/**
	 * 
	 */
	private ModelAndView categoryModelAndView(Integer pageNumber, String categoryName) {
		
		Map<String, Object> articlesData = articleService.getByPageByCategoryName(
				pageNumber, Web.ARTICLES_PER_PAGE, categoryName);
		String requestUrl = String.format("/category/%s/", categoryName);
		String message = String.format(context.getMessage("search.result.byCategory", null,
				LocaleContextHolder.getLocale()), categoryName);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * Search by tag - first page
	 * @param tagName
	 * @return search by tag ModelAndView
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_TAG)
	public ModelAndView viewArticlesByTag(@PathVariable("name") String tagName) {

		return tagModelAndView(1, tagName);
	}

	/**
	 * Search by tag - custom page
	 * @param tagName
	 * @return search by tag ModelAndView
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_TAG_CUSTOM_PAGE)
	public ModelAndView viewArticlesByTag(@PathVariable("name") String tagName,
			@PathVariable("number") Integer pageNumber) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_TAG);
		}
		return tagModelAndView(pageNumber, tagName);
	}

	/**
	 * 
	 */
	private ModelAndView tagModelAndView(Integer pageNumber, String tagName) {

		Map<String, Object> articlesData = articleService.getByPageByTagName(
				pageNumber, Web.ARTICLES_PER_PAGE, tagName);
		String requestUrl = String.format("/tags/%s/", tagName);
		String message = String.format(context.getMessage("search.result.byTag", null,
				LocaleContextHolder.getLocale()), tagName);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * @param userId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_USER)
	public ModelAndView viewArticlesByUser(@PathVariable("id") Long userId) {

		return userModelAndView(1, userId);
	}
	
	/**
	 * @param userId
	 * @param pageNumber
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_USER_CUSTOM_PAGE)
	public ModelAndView viewArticlesByUser(@PathVariable("id") Long userId,
			@PathVariable("number") Integer pageNumber) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_USER);
		}
		return userModelAndView(pageNumber, userId);
	}
	
	/**
	 * 
	 */
	private ModelAndView userModelAndView(Integer pageNumber, Long userId) {
		
		Map<String, Object> articlesData = articleService.getByPageByUserId(
				pageNumber, Web.ARTICLES_PER_PAGE, userId);
		String userLogin = (String) articlesData.get("userLogin");
		String requestUrl = String.format("/user/%d/articles/", userId);
		String message = String.format(context.getMessage("search.result.byUser", null,
				LocaleContextHolder.getLocale()), userLogin);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * @param userId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_FRAGMENT)
	public ModelAndView viewArticlesByFragment(@RequestParam("fragment") String fragment) {
		
		return fragmentModelAndView(1, fragment);
	}
	
	/**
	 * @param userId
	 * @param pageNumber
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SEARCH_BY_FRAGMENT_CUSTOM_PAGE)
	public ModelAndView viewArticlesByFragment(@RequestParam String fragment,
			@PathVariable("number") Integer pageNumber) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + URL.SEARCH_BY_USER);
		}
		return fragmentModelAndView(pageNumber, fragment);
	}
	
	/**
	 * 
	 */
	private ModelAndView fragmentModelAndView(Integer pageNumber, String fragment) {
		
		Map<String, Object> articlesData = articleService.getByPageByFragment(
				pageNumber, Web.ARTICLES_PER_PAGE, fragment);
		String requestUrl = String.format("/search/%s/", fragment);
		String message = String.format(context.getMessage("search.result.byFragment", null,
				LocaleContextHolder.getLocale()), fragment);
		
		return searchModelAndView(articlesData, pageNumber, message, requestUrl);
	}
	
	/**
	 * 
	 */
	private ModelAndView searchModelAndView(Map<String, Object> articlesData,
			Integer pageNumber, String message, String requestUrl) {
		
		// Set required model attributes
		ModelAndView mav = new ModelAndView(View.SEARCH);
		mav.addObject("message", message);
		mav.addObject("requestUrl", requestUrl);
		mav.addObject("currentPage", pageNumber);
		
		if (articlesData.isEmpty()) {	// articleCount == 0
			return mav;
		}
		
		Integer pageCount = (Integer) articlesData.get("pageCount");
		// pageNumber > pageCount
		if (pageCount == -1) {
			return new ModelAndView(View.ERROR);
		}

		@SuppressWarnings("unchecked")
		List<Article> articlesByPage = (List<Article>) articlesData.get("articlesByPage");

		// Set the rest model attributes
		mav.addObject("pageCount", pageCount);
		mav.addObject("articlesByPage", articlesByPage);
		
		return mav;
	}
}