package net.filippov.newsportal.service;

import java.util.Map;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;

public interface NewsService extends AbstractService<News> {
	
	void add(News news, Long authorId, String categoryName, String tagString);
	
	News get(Long newsId, Long userId);
	
	void update(News news, String categoryName, String tagString);
	
	Map<String, Object> getByPage(int page, int newsPerPage);

	Map<String, Object> getByPageByUserId(int page, int newsPerPage, Long id);
	
	Map<String, Object> getByPageByCategoryName(int page, int newsPerPage, String name);
	
	Map<String, Object> getByPageByTagName(int page, int newsPerPage, String name);
	
	void addComment(Comment comment, Long authorId, News news);
}
