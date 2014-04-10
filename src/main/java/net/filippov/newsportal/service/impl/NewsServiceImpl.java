package net.filippov.newsportal.service.impl;

import static net.filippov.newsportal.service.util.QueryParameters.setParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.domain.Category;
import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.NewsService;
import net.filippov.newsportal.service.TagService;
import net.filippov.newsportal.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("NewsService")
public class NewsServiceImpl extends AbstractServiceImpl<News> implements NewsService {

	private static final String ERROR_BY_PAGE = "Unable to get all news by page=";

	GenericRepository<Comment, Long> commentRepository;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	public NewsServiceImpl(GenericRepository<News, Long> rep,
			GenericRepository<Comment, Long> commentRep) {
		
		super(rep);
		commentRepository = commentRep;
		commentRepository.setType(Comment.class);
	}
	
	@Override
	@Transactional
	public void add(News news, Long authorId, String categoryName, String tagString) {
		try {
			news = populateNews(news, authorId, categoryName, tagString);
			this.add(news);
		} catch (PersistenceException e) {
			String message = String.format("Unable to add %s", news);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void update(News news, String categoryName, String tagString) {
		try {
			news = populateNews(news, null, categoryName, tagString);
			news.setLastModified(new Date());
			this.update(news);
		} catch (PersistenceException e) {
			String message = String.format("Unable to update %s", news);
			throw new ServiceException(message, e);
		}
	}
	
	private News populateNews(News news, Long authorId, String categoryName, String tagString)
			throws PersistenceException {
		
		if (authorId != null) {	// Add news process
			User author = userService.get(authorId);
			news.setAuthor(author);
		}

		if (categoryName.isEmpty()) {
			categoryName = "Games";	// TODO: Constants -> default category
		}
		Category category = categoryService.getByName(categoryName);
		news.setCategory(category);
			
		if (!tagString.isEmpty()) {
			Set<Tag> tags = tagService.getTagsFromString(tagString);
			news.setTags(tags);
		}
		return news;
	}
	
	@Override
	@Transactional
	public News get(Long newsId, Long userId) {
		
		News news = repository.get(newsId);
		if (userId != news.getAuthor().getId()) {
			int viewsCount = news.getViewsCount();
			news.setViewsCount(++viewsCount);
			news = repository.update(news);
		}
		news.getComments().size();	// lazy initialize ALL! comments. Need to change (not all)
		return news;
	}
	
	@Override
	@Transactional
	public Map<String, Object> getByPage(int page, int newsPerPage) {
		try {
			return getNewsData(page, newsPerPage, "News.GET_ALL", null);
		} catch (PersistenceException e) {
			String message = String.format("%s%d", ERROR_BY_PAGE, page);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public Map<String, Object> getByPageByUserId(int page, int newsPerPage,
			Long id) {
		try {
			return getNewsData(page, newsPerPage,
					"News.GET_ALL_BY_USER_ID",
					setParam("id", id).buildMap());
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, user_id=%d", ERROR_BY_PAGE, page, id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public Map<String, Object> getByPageByCategoryName(int page,
			int newsPerPage, String name) {
		try {
			return getNewsData(page, newsPerPage,
					"News.GET_ALL_BY_CATEGORY_NAME",
					setParam("name", name).buildMap(),
					"News.GET_COUNT_BY_CATEGORY_NAME");
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, category=%s", ERROR_BY_PAGE, page, name);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public Map<String, Object> getByPageByTagName(int page, int newsPerPage,
			String name) {
		try {
			return getNewsData(page, newsPerPage,
					"News.GET_ALL_BY_TAG_NAME",
					setParam("name", name).buildMap(),
					"News.GET_COUNT_BY_TAG_NAME");
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, tag=%s", ERROR_BY_PAGE, page, name);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void addComment(Comment comment, Long authorId, News news) {
		try {
			User author = userService.get(authorId);
			comment.setAuthor(author);
			comment.setNews(news);
			commentRepository.add(comment);
		} catch (PersistenceException e) {
			String message = String.format("Unable to add %s", comment);
			throw new ServiceException(message, e);
		}
	}
	
	private Map<String, Object> getNewsData(int page, int newsPerPage,
			String namedQuery, Map<String, Object> params)
					throws PersistenceException {
		return getNewsData(page, newsPerPage, namedQuery, params, null);
	}
	
	private Map<String, Object> getNewsData(int page, int newsPerPage,
			String namedQuery, Map<String, Object> params, String countNamedQuery)
					throws PersistenceException {
		
		Map<String, Object> newsData = new HashMap<String, Object>();

		int newsCount = (countNamedQuery == null)
				? repository.getCount()
				: repository.getCountByNamedQuery(countNamedQuery, params);
		int pagesCount = newsCount / newsPerPage
				+ (newsCount % newsPerPage == 0 ? 0 : 1);
		
		if (newsCount == 0) {
			newsData.put("pagesCount", 0);
		} else if (page > pagesCount) {
			newsData.put("pagesCount", -1);
		} else {
			newsData.put("pagesCount", pagesCount);
			int firstResult = newsPerPage * (page - 1);
			newsData.put("newsByPage", repository.getAllByNamedQuery(
					namedQuery, params, firstResult, newsPerPage));
		}
		return newsData;
	}
}
