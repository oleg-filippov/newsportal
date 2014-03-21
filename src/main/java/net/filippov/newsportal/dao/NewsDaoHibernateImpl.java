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
	public void increaseViewsCountById(Long id) {
    	try {
    		getCurrentSession().getNamedQuery("News.INCREASE_VIEWS_COUNT_BY_ID")
    			.setParameter("id", id)
    			.executeUpdate();
		} catch (HibernateException e) {
			throw new PersistentException("Error increasing viewsCount of News[id=" + id + "]", e);
		}
	}

	@Override
	public void increaseCommentsCountById(Long id) {
    	try {
    		getCurrentSession().getNamedQuery("News.INCREASE_COMMENTS_COUNT_BY_ID")
    			.setParameter("id", id)
    			.executeUpdate();
		} catch (HibernateException e) {
			throw new PersistentException("Error increasing commentsCount of News[id=" + id + "]", e);
		}
	}
}
