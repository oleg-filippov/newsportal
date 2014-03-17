package net.filippov.newsportal.service;

import java.util.List;

import net.filippov.newsportal.dao.CommentDao;
import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.exception.PersistentException;
import net.filippov.newsportal.exception.ServiceException;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("CommentService")
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao storage;
	
	public CommentServiceImpl() {}
	
	@Autowired
	private NewsService newsService;
	
	@Override
	public Long add(Comment comment) {
		try {
			News news = comment.getNews();
			newsService.increaseCommentsCountById(news.getId());
			return storage.insert(comment);
		} catch (PersistentException e) {
			throw new ServiceException("Error add " + comment, e);
		}
	}

	@Override
	public Comment getById(Long id) {
		try {
			return storage.select(id);
		} catch (PersistentException e) {
			throw new ServiceException("Error selecting Comment[id=" + id + "]" , e);
		}
	}

	@Override
	public List<Comment> getAllByNewsId(Long id) {
		try {
			return storage.getAllByNewsId(id);
		} catch (HibernateException e) {
			throw new PersistentException("Error getting comments of News[id=" + id +"]", e);
		}
	}
}
