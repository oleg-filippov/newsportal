package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.Comment;

public interface CommentDao extends GenericDao<Comment, Long> {

	List<Comment> getAllByNewsId(Long id);
}
