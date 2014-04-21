package net.filippov.newsportal.service.impl;

import static net.filippov.newsportal.service.util.QueryParameters.setParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.domain.Category;
import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.NotFoundException;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.service.TagService;
import net.filippov.newsportal.service.UserService;
import net.filippov.newsportal.web.constants.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ArticleService")
public class ArticleServiceImpl extends AbstractServiceImpl<Article> implements ArticleService {

	private static final String ERROR_BY_PAGE = "Unable to get all articles by page=";

	private GenericRepository<Comment, Long> commentRepository;
	
	private GenericRepository<Category, Long> categoryRepository;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	public ArticleServiceImpl(GenericRepository<Article, Long> rep,
			GenericRepository<Comment, Long> commentRepository,
			GenericRepository<Category, Long> categoryRepository) {
		
		super(rep);
		
		this.commentRepository = commentRepository;
		this.commentRepository.setType(Comment.class);
		
		this.categoryRepository = categoryRepository;
		this.categoryRepository.setType(Category.class);
	}
	
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
	
	private Article populateArticle(Article article, Long authorId, String categoryName, String tagString)
			throws PersistenceException {
		
		if (authorId != null) {	// Add article process
			User author = userService.get(authorId);
			article.setAuthor(author);
		}

		if (categoryName.isEmpty()) {
			categoryName = Web.DEFAULT_CATEGORY_NAME;
		}
		Category category = categoryRepository.getByNamedQuery(
				"Category.GET_BY_NAME",
				setParam("name", categoryName).buildMap());
		article.setCategory(category);
			
		if (!tagString.isEmpty()) {
			Set<Tag> tags = tagService.getTagsFromString(tagString);
			article.setTags(tags);
		}
		return article;
	}
	
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
	
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPageByCategoryName(int page,
			int articlesPerPage, String name) {
		try {
			Category category = categoryRepository.getByNamedQuery(
					"Category.GET_BY_NAME",
					setParam("name", name).buildMap());
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
	
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getByPageByFragment(int page, int articlesPerPage,
			String fragment) {
		try {
			return getArticlesData(page, articlesPerPage,
					"Article.GET_ALL_BY_FRAGMENT",
					setParam("fragment", "%" + fragment + "%").buildMap(), -1);
		} catch (PersistenceException e) {
			String message = String.format(
					"%s%d, fragment=%s", ERROR_BY_PAGE, page, fragment);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public void addComment(Comment comment, Long authorId, Article article) {
		try {
			User author = userService.get(authorId);
			comment.setAuthor(author);
			comment.setArticle(article);
			commentRepository.add(comment);
		} catch (PersistenceException e) {
			String message = String.format("Unable to add %s", comment);
			throw new ServiceException(message, e);
		}
	}
	
	private Map<String, Object> getArticlesData(int page, int articlesPerPage,
			String namedQuery, int articleCount) {
		return getArticlesData(page, articlesPerPage, namedQuery, null, articleCount);
	}
	
	private Map<String, Object> getArticlesData(int page, int articlesPerPage,
			String namedQuery, Map<String, Object> params, int articleCount)
					throws PersistenceException {
		
		Map<String, Object> articlesData = new HashMap<String, Object>();
		
		if (articleCount == 0) {
			return articlesData;
		// articleCount is unknown in advance (search by fragment)
		} else if (articleCount < 0 ) {
			int firstResult = articlesPerPage * (page - 1);
			List<Article> articlesByPage = repository.getAllByNamedQuery(
					namedQuery, params, firstResult, articlesPerPage);
			articleCount = articlesByPage.size();
			if (articleCount == 0) {
				return articlesData;
			}
			articlesData.put("articlesByPage", articlesByPage);
		}
		
		int pageCount = articleCount / articlesPerPage
				+ (articleCount % articlesPerPage == 0 ? 0 : 1);	
		if (page > pageCount) {
			articlesData.put("pageCount", -1);
			return articlesData;
		}
		
		articlesData.put("pageCount", pageCount);
		
		if (!articlesData.containsKey("articlesByPage")) {
			int firstResult = articlesPerPage * (page - 1);
			articlesData.put("articlesByPage", repository.getAllByNamedQuery(
					namedQuery, params, firstResult, articlesPerPage));
		}
		return articlesData;
	}
}
