package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.News;

public interface NewsDao extends GenericDao<News, Long> {
	
	List<News> getAll();
	
	void increaseViewsCountById(Long id);
}
