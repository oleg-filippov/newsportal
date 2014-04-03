package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.News;

public interface NewsDao extends GenericDao<News, Long> {
	
	List<News> getAll();
	
	List<News> getByPage(int page, int newsPerPage);
	
	List<News> getByPageByUserId(int page, int newsPerPage, Long id);
	
	List<News> getByPageByTagId(int page, int newsPerPage, Long id);
	
	int getNewsCount();
	
	int getNewsCountByUserId(Long id);
	
	int getNewsCountByTagId(Long id);
	
	void increaseViewsCountById(Long id);
}
