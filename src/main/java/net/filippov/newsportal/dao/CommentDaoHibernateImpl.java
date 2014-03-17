package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.exception.PersistentException;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

@Repository("CommentDao")
public class CommentDaoHibernateImpl extends GenericDaoHibernateImpl<Comment, Long>
		implements CommentDao {

	public CommentDaoHibernateImpl() {}

	@Override
	@SuppressWarnings("unchecked")
	public List<Comment> getAllByNewsId(Long id) {
    	try {
    		return getCurrentSession().createQuery(
    			"from Comment where news_id = :id order by created desc")
    			.setParameter("id", id)
    			.list();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting comments of News[id=" + id + "]", e);
		}
	}
}
