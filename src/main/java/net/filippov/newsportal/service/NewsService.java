package net.filippov.newsportal.service;

import java.util.List;
import java.util.Map;

import net.filippov.newsportal.domain.News;

public interface NewsService {
	
	Long add(News news);
	
	News getById(Long id);
	
	List<News> getAll();
	
	Map<String, Object> getByPage(int page, int newsPerPage);
	
	void update(News news);
	
	void deleteById(Long id);
	
	void increaseViewsCountById(Long id);
}
