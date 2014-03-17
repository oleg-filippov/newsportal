package net.filippov.newsportal.service;

import java.util.List;

import net.filippov.newsportal.domain.Comment;

public interface CommentService {

	Long add(Comment comment);
	
	Comment getById(Long id);
	
	List<Comment> getAllByNewsId(Long id);
}
