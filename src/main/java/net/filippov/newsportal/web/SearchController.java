package net.filippov.newsportal.web;

import java.util.List;
import java.util.Map;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.NewsService;
import net.filippov.newsportal.service.TagService;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

	// URL's
	private static final String SEARCH_BY_TAG_URL = "/tags/{name}";
	private static final String SEARCH_BY_CATEGORY_URL = "/category/{name}";
	private static final String SEARCH_BY_CONTENT_URL = "/news/search";
	private static final String SEARCH_BY_TAG_CUSTOM_PAGE_URL = "/tags/{name}/page/{pageNumber}";
	private static final String SEARCH_BY_CATEGORY_CUSTOM_PAGE_URL = "/category/{name}/page/{pageNumber}";
	private static final String SEARCH_BY_CONTENT_CUSTOM_PAGE_URL = "/news/search/page/{pageNumber}";
	private static final String ERROR_URL = "/error";

	private static final int NEWS_PER_PAGE = 3;

	@Autowired
	private NewsService newsService;

	@Autowired
	private TagService tagService;

	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(method = RequestMethod.GET, value = SEARCH_BY_CATEGORY_URL)
	public ModelAndView viewNewsByCategory(@PathVariable("name") String categoryName) {

		return tagModelAndView(1, categoryName);
	}

	@RequestMapping(method = RequestMethod.GET, value = SEARCH_BY_CATEGORY_CUSTOM_PAGE_URL)
	public ModelAndView viewNewsByCategory(@PathVariable("name") String categoryName,
			@PathVariable("pageNumber") Integer pageNumber) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + SEARCH_BY_CATEGORY_URL);
		}

		return categoryModelAndView(pageNumber, categoryName);
	}

	private ModelAndView categoryModelAndView(Integer pageNumber, String categoryName) {

		Map<String, Object> newsData = newsService.getByPageByCategoryName(
				pageNumber, NEWS_PER_PAGE, categoryName);
		Integer pagesCount = (Integer) newsData.get("pagesCount");

		// pageNumber > pagesCount
		if (pagesCount == -1) {
			return new ModelAndView("redirect:" + ERROR_URL);
		}

		@SuppressWarnings("unchecked")
		List<News> newsByPage = (List<News>) newsData.get("newsByPage");

		ModelAndView mav = new ModelAndView();
		mav.addObject("pagesCount", pagesCount);
		mav.addObject("newsByPage", newsByPage);
		mav.addObject("currentPage", pageNumber);
		mav.setViewName("search");

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = SEARCH_BY_TAG_URL)
	public ModelAndView viewNewsByTag(@PathVariable("name") String tagName) {

		return tagModelAndView(1, tagName);
	}

	@RequestMapping(method = RequestMethod.GET, value = SEARCH_BY_TAG_CUSTOM_PAGE_URL)
	public ModelAndView viewNewsByTag(@PathVariable("name") String tagName,
			@PathVariable("pageNumber") Integer pageNumber) {

		if (pageNumber == 1) {
			return new ModelAndView("redirect:" + SEARCH_BY_TAG_URL);
		}

		return tagModelAndView(pageNumber, tagName);
	}

	private ModelAndView tagModelAndView(Integer pageNumber, String tagName) {

		Map<String, Object> newsData = newsService.getByPageByTagName(
				pageNumber, NEWS_PER_PAGE, tagName);
		Integer pagesCount = (Integer) newsData.get("pagesCount");

		// pageNumber > pagesCount
		if (pagesCount == -1) {
			return new ModelAndView("redirect:" + ERROR_URL);
		}

		@SuppressWarnings("unchecked")
		List<News> newsByPage = (List<News>) newsData.get("newsByPage");

		ModelAndView mav = new ModelAndView();
		mav.addObject("pagesCount", pagesCount);
		mav.addObject("newsByPage", newsByPage);
		mav.addObject("currentPage", pageNumber);
		mav.setViewName("search");

		return mav;
	}
}