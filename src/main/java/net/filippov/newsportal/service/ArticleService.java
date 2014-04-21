package net.filippov.newsportal.service;

import java.util.Map;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.Article;

public interface ArticleService extends AbstractService<Article> {
	
	void add(Article article, Long authorId, String categoryName, String tagString);
	
	Article get(Long articleId, Long userId, boolean needIncreaseViewCount);
	
	void update(Article article, String categoryName, String tagString);
	
	Map<String, Object> getByPage(int page, int articlesPerPage);

	Map<String, Object> getByPageByCategoryName(int page, int articlesPerPage, String name);
	
	Map<String, Object> getByPageByFragment(int page, int articlesPerPage, String fragment);
	
	Map<String, Object> getByPageByTagName(int page, int articlesPerPage, String name);
	
	Map<String, Object> getByPageByUserId(int page, int articlesPerPage, Long id);
	
	void addComment(Comment comment, Long authorId, Article article);
}
