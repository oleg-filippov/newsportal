package net.filippov.newsportal.service.impl;

import net.filippov.newsportal.domain.Category;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CategoryService")
public class CategoryServiceImpl extends AbstractServiceImpl<Category>
		implements CategoryService {

	@Autowired
	public CategoryServiceImpl(GenericRepository<Category, Long> repository) {
		super(repository);
	}
}
