package net.filippov.newsportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Generic repository providing basic and custom operations using named queries
 *
 * @param <T> The persistent type
 * @param <PK> The primary key type
 * 
 * @author Oleg Filippov
 */
public interface GenericRepository <T extends Serializable, PK extends Serializable> {
	
	/**
	 * Set generic type
	 * @param type
	 */
	void setType(Class<T> type);
	
	/**
	 * Add object into database
	 * @param object to be inserted into database
	 * @return id of the object has been inserted into database
	 */
	void add(T newInstance);
	
	/**
	 * Get object from database
	 * @param id of the object to be selected from database
	 * @return persistent instance of object or null if the object does not exist
	 */
	T get(PK id);
	
	/**
	 * 
	 * Update object in database
	 * @param transientObject to be updated
	 */
	T update(T transientObject);
	
	/**
	 * Delete object from database
	 * @param persistentObject to be deleted
	 */
	void delete(T persistentObject);
	
	/**
	 * Delete object from database by id
	 * @param id of the object to be deleted from database
	 */
	void deleteById(PK id);
	
	/**
	 * Get object from database using a named query
	 * @param namedQuery to use
	 * @param map of parameters to set
	 * @return persistent instance of object or null if the object does not exist
	 */
	T getByNamedQuery(String namedQuery, Map<String, Object> parameters);
	
	/**
	 * Get list of objects from database
	 * @return list of persistent objects
	 */
	List<T> getAll();
	
	/**
	 * Get list of objects from database using a named query
	 * @param namedQuery to use
	 * @return list of persistent objects
	 */
	List<T> getAllByNamedQuery(String namedQuery);
	
	/**
	 * Get list of objects from database using a named query
	 * @param namedQuery to use
	 * @param firstResult position of the first result, numbered from 0
	 * @param resultLimit results limit
	 * @return list of persistent objects
	 */
	List<T> getAllByNamedQuery(String namedQuery, int firstResult, int resultLimit);
	
	/**
	 * Get list of objects from database using a named query
	 * @param namedQuery to use
	 * @param parameters Map of parameters to set
	 * @return list of persistent objects
	 */
	List<T> getAllByNamedQuery(String namedQuery, Map<String, Object> parameters);
	
	/**
	 * Get list of objects from database using a named query
	 * @param namedQuery to use
	 * @param parameters Map of parameters to set
	 * @param firstResult position of the first result, numbered from 0
	 * @param resultLimit results limit
	 * @return list of persistent objects
	 */
	List<T> getAllByNamedQuery(String namedQuery, Map<String, Object> parameters,
			int firstResult, int resultLimit);
	
	/**
	 * Get names from table
	 * @param namedQuery to use
	 * @param resultLimit results limit
	 * @return
	 */
	List<String> getAllNamesByNamedQuery(String namedQuery, int resultLimit);
	
	/**
	 * Count all objects in database
	 * @return the number of objects
	 */
	int getCount();
	
	/**
	 * Count all objects in database using a named query
	 * @param namedQuery to use
	 * @param parameters Map of parameters to set
	 * @return the number of objects
	 */
	int getCountByNamedQuery(String namedQuery, Map<String, Object> parameters);
}
