package net.filippov.newsportal.service;

import java.util.List;

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
	
	@Override
	public void increaseCommentsCountById(Long id) {
		try {
			storage.increaseCommentsCountById(id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
}
