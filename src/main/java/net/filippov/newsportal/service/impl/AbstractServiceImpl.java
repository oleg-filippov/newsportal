package net.filippov.newsportal.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.AbstractService;

import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractServiceImpl<T extends Serializable>
		implements AbstractService<T> {
	
	/**
	 * Generic repository
	 */
	protected GenericRepository<T, Long> repository;
	
	/**
	 *  Object class simple name
	 */
	private String className;
	
	/**
	 * Constructor autowiring generic repository
	 */
	@SuppressWarnings("unchecked")
	public AbstractServiceImpl(GenericRepository<T, Long> repository) {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		Class<T> type = (Class<T>) pt.getActualTypeArguments()[0];
		className = type.getSimpleName();
		
		this.repository = repository;
		this.repository.setType(type);
	}

	@Override
	public void add(T obj) {
		try {
			repository.add(obj);
		} catch (PersistenceException e) {
			String message = String.format("Unable to add %s", obj);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void addTransactionally(T obj) {
		this.add(obj);
	}

	@Override
	public T get(Long id) {
		try {
			return repository.get(id);
		} catch (PersistenceException e) {
			String message = String.format("Unable to get %s id=%d", className, id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public T getTransactionally(Long id) {
		return this.get(id);
	}

	@Override
	public void update(T obj) {
		try {
			repository.update(obj);
		} catch (PersistenceException e) {
			String message = String.format("Unable to update %s", obj);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void updateTransactionally(T obj) {
		this.update(obj);
	}

	@Override
	@Transactional
	public void deleteTransactionally(T obj) {
		try {
			repository.delete(obj);
		} catch (PersistenceException e) {
			String message = String.format("Unable to delete %s", obj);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void deleteByIdTransactionally(Long id) {
		try {
			repository.deleteById(id);
		} catch (PersistenceException e) {
			String message = String.format("Unable to delete %s by id=", className, id);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public List<T> getAllTransactionally() {
		try {
			return repository.getAll();
		} catch (PersistenceException e) {
			String message = String.format("Unable to get all: %s", className);
			throw new ServiceException(message, e);
		}
	}
}
