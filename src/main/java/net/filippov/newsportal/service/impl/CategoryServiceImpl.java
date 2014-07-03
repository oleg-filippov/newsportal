package net.filippov.newsportal.service.impl;

import static net.filippov.newsportal.service.util.QueryParameters.setParam;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.domain.Category;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CategoryService}
 * 
 * @author Oleg Filippov
 */
@Service("CategoryService")
public class CategoryServiceImpl extends AbstractServiceImpl<Category>
		implements CategoryService {

	@Autowired
	public CategoryServiceImpl(GenericRepository<Category, Long> repository) {
		super(repository);
	}

	/**
	 * @see net.filippov.newsportal.service.CategoryService#getByName(java.lang.String)
	 */
	@Override
	public Category getByName(String name) {
		try {
			return repository.getByNamedQuery("Category.GET_BY_NAME",
					setParam("name", name).buildMap());
		} catch (PersistenceException e) {
			String message = String.format("Unable to get category=%s", name);
			throw new ServiceException(message, e);
		}
	}
}
