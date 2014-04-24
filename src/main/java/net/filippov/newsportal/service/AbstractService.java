package net.filippov.newsportal.service;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract service providing basic transactional and nontransactional operations
 * 
 * @author Oleg Filippov
 */
public interface AbstractService<T extends Serializable> {
	
	/**
	 * Save object into repository
	 * 
	 * @param obj to save
	 */
	void add(T obj);
	
	/**
	 * Save object transactionally
	 * 
	 * @param obj to save
	 */
	void addTransactionally(T obj);
	
	/**
	 * Get object from repository
	 * 
	 * @param id of object
	 * @return persistent object or null if not found
	 */
	T get(Long id);
	
	/**
	 * Get object from repository transactionally
	 * 
	 * @param id of object
	 * @return persistent object or null if not found
	 */
	T getTransactionally(Long id);
	
	/**
	 * Update object
	 * 
	 * @param obj to update
	 */
	void update(T obj);
	
	/**
	 * Update object transactionally
	 * 
	 * @param obj to update
	 */
	void updateTransactionally(T obj);
	
	/**
	 * Delete object transactionally
	 * 
	 * @param obj to delete
	 */
	void deleteTransactionally(T obj);
	
	/**
	 * Delete object by it's id transactionally
	 * 
	 * @param id of the object to delete
	 */
	void deleteByIdTransactionally(Long id);
	
	/**
	 * Get all objects from repository transactionally
	 * 
	 * @return list of objects
	 */
	List<T> getAllTransactionally();
}
