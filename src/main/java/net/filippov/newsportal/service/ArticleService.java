package net.filippov.newsportal.service;

import java.util.Map;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.exception.NotFoundException;

/**
 * Provides article-related operations
 * 
 * @author Oleg Filippov
 */
public interface ArticleService extends AbstractService<Article> {
	
	/**
	 * Save article into repository
	 * 
	 * @param article to save
	 * @param authorId article's author id
	 * @param categoryName name of article's category
	 * @param tagString string of comma separated tags
	 */
	void add(Article article, Long authorId, String categoryName, String tagString);
	
	/**
	 * Get article from repository by it's id and increase view count if:
	 * 1. user is anonymous
	 * 2. logged user is not the author of this article
	 * 3. requested URL is not equal to current article URL
	 * 
	 * @param articleId id of article
	 * @param userId logged user id
	 * @param needIncreaseViewCount requested URL is not equal to current article URL
	 * @return article
	 * @throws NotFoundException
	 */
	Article get(Long articleId, Long userId, boolean needIncreaseViewCount)
			throws NotFoundException;
	
	/**
	 * Update article
	 * 
	 * @param article to update
	 * @param categoryName name of article's category
	 * @param tagString string of comma separated tags
	 */
	void update(Article article, String categoryName, String tagString);
	
	/**
	 * Get articles by specified page
	 * 
	 * @param page
	 * @param articlesPerPage number of articles per page
	 * @return Map of articles and number of pages
	 */
	Map<String, Object> getByPage(int page, int articlesPerPage);

	/**
	 * Get articles by category on specified page
	 * 
	 * @param page
	 * @param articlesPerPage number of articles per page
	 * @param name of category
	 * @return Map of articles and number of pages
	 * @throws NotFoundException if category not found
	 */
	Map<String, Object> getByPageByCategoryName(int page, int articlesPerPage, String name)
			throws NotFoundException;
	
	/**
	 * Search article content by fragment on specified page
	 * 
	 * @param page
	 * @param articlesPerPage number of articles per page
	 * @param fragment to search
	 * @return Map of articles and number of pages
	 */
	Map<String, Object> getByPageByFragment(int page, int articlesPerPage, String fragment);
	
	/**
	 * Get articles by tag on specified page
	 * 
	 * @param page
	 * @param articlesPerPage number of articles per page
	 * @param name of tag
	 * @return Map of articles and number of pages
	 * @throws NotFoundException if tag not found
	 */
	Map<String, Object> getByPageByTagName(int page, int articlesPerPage, String name)
			throws NotFoundException;
	
	/**
	 * Get articles by user id on specified page
	 * 
	 * @param page
	 * @param articlesPerPage number of articles per page
	 * @param id of user
	 * @return Map of articles and number of pages
	 * @throws NotFoundException if user not found
	 */
	Map<String, Object> getByPageByUserId(int page, int articlesPerPage, Long id)
			throws NotFoundException;
	
	/**
	 * Save a comment by it's content
	 * 
	 * @param content comment content
	 * @param authorId author primary key
	 * @param articleId article primary key
	 */
	void addComment(String content, Long authorId, Long articleId);
}
