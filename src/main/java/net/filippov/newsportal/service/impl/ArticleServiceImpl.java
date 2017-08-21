package net.filippov.newsportal.service.impl;

import static net.filippov.newsportal.service.util.QueryParameters.setParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.domain.Category;
import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.NotFoundException;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.CommentService;
import net.filippov.newsportal.service.TagService;
import net.filippov.newsportal.service.UserService;
import net.filippov.newsportal.web.constants.Common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ArticleService}
 * 
 * @author Oleg Filippov
 */
@Service("ArticleService")
public class ArticleServiceImpl extends AbstractServiceImpl<Article> implements ArticleService {

	/**
	 * Default error-message template for "getByPage" methods
	 */
	private static final String ERROR_BY_PAGE = "Unable to get all articles by page=";
	
	private CategoryService categoryService;
	private CommentService commentService;
	private TagService tagService;
	private UserService userService;
	
	/**
	 * Constructor autowiring needed repositories and services
	 */
	@Autowired
	public ArticleServiceImpl(GenericRepository<Article, Long> repository,
			CategoryService categoryService,
			CommentService commentService,
			TagService tagService,
			UserService userService) {
		super(repository);
		this.categoryService = categoryService;
		this.commentService = commentService;
		this.tagService = tagService;
		this.userService = userService;
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#add(
	 * net.filippov.newsportal.domain.Article,java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void add(Article article, Long authorId, String categoryName, String tagString) {
		try {
			article = populateArticle(article, authorId, categoryName, tagString);
			this.add(article);
		} catch (PersistenceException e) {
			String message = String.format("Unable to add %s", article);
			throw new ServiceException(message, e);
		}
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#update(
	 * net.filippov.newsportal.domain.Article, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void update(Article article, String categoryName, String tagString) {
		try {
			article = populateArticle(article, null, categoryName, tagString);
			article.setLastModified(new Date());
			this.update(article);
		} catch (PersistenceException e) {
			String message = String.format("Unable to update %s", article);
			throw new ServiceException(message, e);
		}
	}
	
	/**
	 * Populates an article with author, category and tags from tag-string
	 * 
	 * @param article
	 * @param authorId
	 * @param categoryName
	 * @param tagString
	 * @return populated {@link Article}
	 * @throws PersistenceException
	 */
	private Article populateArticle(Article article, Long authorId, String categoryName, String tagString)
			throws PersistenceException {
		
		if (authorId != null) {	// Add article process
			User author = userService.get(authorId);
			article.setAuthor(author);
		}

		if (categoryName.isEmpty()) {
			categoryName = Common.DEFAULT_CATEGORY_NAME;
		}
		Category category = categoryService.getByName(categoryName);
		article.setCategory(category);
			
		if (!tagString.isEmpty()) {
			Set<Tag> tags = tagService.getTagsFromString(tagString);
			article.setTags(tags);
		}
		return article;
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#get(java.lang.Long, java.lang.Long, boolean)
	 */
	@Override
	@Transactional
	public Article get(Long articleId, Long userId, boolean needIncreaseViewCount) {
		
		Article article = repository.get(articleId);
		
		if (article == null) {
			throw new NotFoundException("Article not found");
		}
		
		if (needIncreaseViewCount && userId != article.getAuthor().getId()) {
			int viewCount = article.getViewCount();
			article.setViewCount(++viewCount);
			article = repository.update(article);
		}
		// lazy initialize ALL! comments. Need to change (not all -> resultLimit)
		article.getComments().size();
		return article;
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#getByPage(int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPage(int page, int articlesPerPage) {
		try {
			return getArticlesData(page, articlesPerPage, "Article.GET_ALL", repository.getCount());
		} catch (PersistenceException e) {
			String message = String.format("%s%d", ERROR_BY_PAGE, page);
 			throw new ServiceException(message, e);
		}
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#getByPageByCategoryName(
	 * int, int, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPageByCategoryName(int page,
			int articlesPerPage, String name) {
		try {
			Category category = categoryService.getByName(name);
			if (category == null) {
				throw new NotFoundException("Category not found");
			}
			return getArticlesData(page, articlesPerPage,
					"Article.GET_ALL_BY_CATEGORY_NAME",
					setParam("name", name).buildMap(),
					category.getArticleCount());
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, category=%s", ERROR_BY_PAGE, page, name);
			throw new ServiceException(message, e);
		}
	}

	/**
	 * @see net.filippov.newsportal.service.ArticleService#getByPageByTagName(
	 * int, int, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPageByTagName(int page, int articlesPerPage,
			String name) {
		try {
			Tag tag = tagService.getByName(name);
			if (tag == null) {
				throw new NotFoundException("Tag not found");
			}
			return getArticlesData(page, articlesPerPage,
					"Article.GET_ALL_BY_TAG_NAME",
					setParam("name", name).buildMap(),
					tag.getArticleCount());
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, tag=%s", ERROR_BY_PAGE, page, name);
			throw new ServiceException(message, e);
		}
	}

	/**
	 * @see net.filippov.newsportal.service.ArticleService#getByPageByUserId(
	 * int, int, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPageByUserId(int page, int articlesPerPage, Long id) {
		try {
			User user = userService.get(id);
			if (user == null) {
				throw new NotFoundException("User not found");
			}
			Map<String, Object> articlesData = getArticlesData(page, articlesPerPage,
					"Article.GET_ALL_BY_USER_ID",
					setParam("id", id).buildMap(),
					user.getArticleCount());
			articlesData.put("userLogin", user.getLogin());
			return articlesData;
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, user_id=%d", ERROR_BY_PAGE, page, id);
			throw new ServiceException(message, e);
		}
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#getByPageByFragment(
	 * int, int, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPageByFragment(int page, int articlesPerPage,
			String fragment) {
		try {
			Map<String, Object> parameters = setParam(
					"fragment", "%" + fragment + "%").buildMap();
			int articleCount = repository.getCountByNamedQuery(
					"Article.GET_COUNT_BY_FRAGMENT", parameters);
			return getArticlesData(page, articlesPerPage,
					"Article.GET_ALL_BY_FRAGMENT", parameters, articleCount);
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, fragment=%s", ERROR_BY_PAGE, page, fragment);
			throw new ServiceException(message, e);
		}
	}

	/**
	 * Gets map of articles data by named query without query parameters
	 */
	private Map<String, Object> getArticlesData(int page, int articlesPerPage,
			String namedQuery, int articleCount) {
		return getArticlesData(page, articlesPerPage, namedQuery, null, articleCount);
	}
	
	/**
	 * Gets map of articles data by named query
	 * 
	 * @param page
	 * @param articlesPerPage number of articles per page
	 * @param namedQuery to use
	 * @param params Map of parameters of the named query
	 * @param articleCount article count by specified parameter
	 * @return filled Map with articles and number of pages
	 * @throws PersistenceException
	 */
	private Map<String, Object> getArticlesData(int page, int articlesPerPage,
			String namedQuery, Map<String, Object> params, int articleCount)
					throws PersistenceException {
		
		Map<String, Object> articlesData = new HashMap<String, Object>();
		
		if (articleCount == 0) {
			return articlesData;
		}
		
		int pageCount = articleCount / articlesPerPage
				+ (articleCount % articlesPerPage == 0 ? 0 : 1);	
		if (page > pageCount) {
			throw new NotFoundException("page > pageCount");
		}
		
		articlesData.put("pageCount", pageCount);
		
		int firstResult = articlesPerPage * (page - 1);
		articlesData.put("articlesByPage", repository.getAllByNamedQuery(
				namedQuery, params, firstResult, articlesPerPage));
		return articlesData;
	}
	
	/**
	 * @see net.filippov.newsportal.service.ArticleService#addComment(
	 * net.filippov.newsportal.domain.Comment, java.lang.Long, net.filippov.newsportal.domain.Article)
	 */
	@Override
	@Transactional
	public void addComment(String content, Long authorId, Long articleId) {
		Comment comment = null;
		try {
			User author = userService.get(authorId);
			Article article = repository.get(articleId);
			comment = new Comment(author, article, content);
			commentService.add(comment);
		} catch (PersistenceException e) {
			String message = String.format("Unable to add %s", comment);
			throw new ServiceException(message, e);
		}
	}
}
