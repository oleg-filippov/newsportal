package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.exception.PersistentException;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

@Repository("NewsDao")
public class NewsDaoHibernateImpl extends GenericDaoHibernateImpl<News, Long>
		implements NewsDao {

	public NewsDaoHibernateImpl() {}

	@Override
	@SuppressWarnings("unchecked")
	public List<News> getAll() {
    	try {
    		return getCurrentSession().getNamedQuery("News.GET_ALL")
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
    			.setMaxResults(newsPerPage)
    			.setFirstResult(newsPerPage * (page - 1))
    			.list();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting news by page=" + page, e);
		}
	}
	
	@Override
	public int getNewsCount() {
		try {
			Long newsCount = (Long) getCurrentSession().getNamedQuery("News.GET_COUNT")
				.uniqueResult();
			return (newsCount == null) ? 0 : newsCount.intValue();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting newsCount", e);
		}
	}

	@Override
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
