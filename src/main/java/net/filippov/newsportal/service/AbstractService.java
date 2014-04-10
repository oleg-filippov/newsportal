package net.filippov.newsportal.service;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 
 * @author Oleg Filippov
 */
public interface AbstractService<T extends Serializable> {
	
	/**
	 * @param obj
	 */
	void add(T obj);
	
	/**
	 * @param obj
	 */
	void addTransactionally(T obj);
	
	/**
	 * @param id
	 * @return
	 */
	T get(Long id);
	
	/**
	 * @param id
	 * @return
	 */
	T getTransactionally(Long id);
	
	/**
	 * @param obj
	 */
	void update(T obj);
	
	/**
	 * @param obj
	 */
	void updateTransactionally(T obj);
	
	/**
	 * @param obj
	 */
	void deleteTransactionally(T obj);
	
	/**
	 * @param id
	 */
	void deleteByIdTransactionally(Long id);
	
	/**
	 * @return
	 */
	List<T> getAllTransactionally();
}
