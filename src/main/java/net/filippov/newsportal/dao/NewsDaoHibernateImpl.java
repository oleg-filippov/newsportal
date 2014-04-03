package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.exception.PersistentException;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("NewsDao")
public class NewsDaoHibernateImpl extends GenericDaoHibernateImpl<News, Long>
		implements NewsDao {

	public NewsDaoHibernateImpl() {}

	@Override
	@SuppressWarnings("unchecked")
	public List<News> getAll() {
    	try {
    		return getCurrentSession().getNamedQuery("News.GET_ALL")
    				.setCacheable(true)
    				.list();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting all news", e);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<News> getByPage(int page, int newsPerPage) {
		try {
    		return getCurrentSession().getNamedQuery("News.GET_ALL")
    				.setCacheable(true)
    				.setMaxResults(newsPerPage)
    				.setFirstResult(newsPerPage * (page - 1))
    				.list();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting news by page=" + page, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<News> getByPageByUserId(int page, int newsPerPage, Long id) {
		try {
    		return getCurrentSession().getNamedQuery("News.GET_ALL_BY_USER_ID")
    				.setParameter("id", id)
    				.setCacheable(true)
    				.setMaxResults(newsPerPage)
    				.setFirstResult(newsPerPage * (page - 1))
    				.list();
		} catch (HibernateException e) {
			throw new PersistentException(
					"Error getting news by userId=" + id + " on page=" + page, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<News> getByPageByTagId(int page, int newsPerPage, Long id) {
		try {
    		return getCurrentSession().getNamedQuery("News.GET_ALL_BY_TAG_ID")
    				.setParameter("id", id)
    				.setCacheable(true)
    				.setMaxResults(newsPerPage)
    				.setFirstResult(newsPerPage * (page - 1))
    				.list();
		} catch (HibernateException e) {
			throw new PersistentException(
					"Error getting news by tagId=" + id + " on page=" + page, e);
		}
	}
	
	@Override
	public int getNewsCount() {
		try {
			Long newsCount = (Long) getCurrentSession().getNamedQuery("News.GET_COUNT")
					.setCacheable(true)
					.uniqueResult();
			return newsCount.intValue();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting newsCount", e);
		}
	}
	
	@Override
	public int getNewsCountByUserId(Long id) {
		try {
			Long newsCount = (Long) getCurrentSession().getNamedQuery("News.GET_COUNT_BY_USER_ID")
					.setParameter("id", id)
					.setCacheable(true)
					.uniqueResult();
			return newsCount.intValue();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting newsCount", e);
		}
	}

	@Override
	public int getNewsCountByTagId(Long id) {
		try {
			Long newsCount = (Long) getCurrentSession().getNamedQuery("News.GET_COUNT_BY_TAG_ID")
					.setParameter("id", id)
					.setCacheable(true)
					.uniqueResult();
			return newsCount.intValue();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting newsCount", e);
		}
	}

	@Override
	@Transactional
	public void increaseViewsCountById(Long id) {
    	try {
    		getCurrentSession().getNamedQuery("News.INCREASE_VIEWS_COUNT_BY_ID")
    				.setParameter("id", id)
    				.executeUpdate();
		} catch (HibernateException e) {
			throw new PersistentException("Error increasing viewsCount of News[id=" + id + "]", e);
		}
	}
}
