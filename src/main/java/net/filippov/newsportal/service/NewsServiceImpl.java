package net.filippov.newsportal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.filippov.newsportal.dao.NewsDao;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.PersistentException;
import net.filippov.newsportal.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("NewsService")
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
			return fillNewsData(page, newsPerPage);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public Map<String, Object> getByPageByUserId(int page, int newsPerPage, Long id) {
		try {
			return fillNewsData(page, newsPerPage, User.class, id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public Map<String, Object> getByPageByTagId(int page, int newsPerPage, Long id) {
		try {
			return fillNewsData(page, newsPerPage, Tag.class, id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	private Map<String, Object> fillNewsData(int page, int newsPerPage)
			throws PersistentException {
		
		return fillNewsData(page, newsPerPage, News.class, null);
	}
	
	private Map<String, Object> fillNewsData(int page, int newsPerPage,
			@SuppressWarnings("rawtypes") Class type, Long id)
			throws PersistentException {
		
		Map<String, Object> newsData = new HashMap<String, Object>();

		int newsCount;
		if (type == User.class) {
			newsCount = storage.getNewsCountByUserId(id);
		} else if (type == Tag.class) {
			newsCount = storage.getNewsCountByTagId(id);
		} else {
			newsCount = storage.getNewsCount();
		}
		
		int pagesCount = newsCount / newsPerPage
				+ (newsCount % newsPerPage == 0 ? 0 : 1);
		
		if (newsCount == 0) {
			newsData.put("pagesCount", 0);
		} else if (page > pagesCount) {
			newsData.put("pagesCount", -1);
		} else {
			newsData.put("pagesCount", pagesCount);
			
			if (type == User.class) {
				newsData.put("newsByPage",
						storage.getByPageByUserId(page, newsPerPage, id));
			} else if (type == Tag.class) {
				newsData.put("newsByPage",
						storage.getByPageByTagId(page, newsPerPage, id));
			} else {
				newsData.put("newsByPage",
						storage.getByPage(page, newsPerPage));
			}
		}
		return newsData;
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
