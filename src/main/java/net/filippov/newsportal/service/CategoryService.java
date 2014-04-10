package net.filippov.newsportal.service;

import java.util.List;

import net.filippov.newsportal.domain.Category;

public interface CategoryService extends AbstractService<Category> {
	
	Category getByName(String name);
	
	List<String> getAllNames();
}
