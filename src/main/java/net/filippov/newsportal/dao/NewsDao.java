package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.News;

public interface NewsDao extends GenericDao<News, Long> {
	
	List<News> getAll();
	
	List<News> getByPage(int page, int newsPerPage);
	
	int getNewsCount();
	
	void increaseViewsCountById(Long id);
}
