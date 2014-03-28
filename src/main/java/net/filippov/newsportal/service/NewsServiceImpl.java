package net.filippov.newsportal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.filippov.newsportal.dao.NewsDao;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.exception.PersistentException;
import net.filippov.newsportal.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ServiceDao")
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsDao storage;
	
	public NewsServiceImpl() {}
	
	@Override
	@Transactional
	public List<News> getAll() {
		try {
			return storage.getAll();
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public Map<String, Object> getByPage(int page, int newsPerPage) {
		try {
			Map<String, Object> newsData = new HashMap<String, Object>();
			int newsCount = storage.getNewsCount();
			int pagesCount = newsCount / newsPerPage
					+ (newsCount % newsPerPage == 0 ? 0 : 1);
			
			if (newsCount == 0) {
				newsData.put("pagesCount", 0);
			} else if (page > pagesCount) {
				newsData.put("pagesCount", -1);
			} else {
				newsData.put("pagesCount", pagesCount);
				newsData.put("newsByPage", storage.getByPage(page, newsPerPage));
			}
			
			return newsData;
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Long add(News news) {
		try {
			return storage.insert(news);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public News getById(Long id) {
		try {
			return storage.select(id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void update(News news) {
		try {
			storage.update(news);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		try {
			News news = storage.select(id);
			if (news != null) {
				storage.delete(news);
			}
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public void increaseViewsCountById(Long id) {
		try {
			storage.increaseViewsCountById(id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
}
