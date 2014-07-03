package net.filippov.newsportal.service.impl;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CommentService")
public class CommentServiceImpl extends AbstractServiceImpl<Comment>
		implements CommentService {

	@Autowired
	public CommentServiceImpl(GenericRepository<Comment, Long> repository) {
		super(repository);
	}

}
