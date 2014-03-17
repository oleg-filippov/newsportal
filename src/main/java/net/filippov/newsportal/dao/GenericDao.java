package net.filippov.newsportal.dao;

import java.io.Serializable;

public interface GenericDao <T, PK extends Serializable> {
	
	/**
	 * Insert object into database
	 * @param object to be inserted into database
	 * @return id of the object has been inserted into database
	 */
	PK insert(T newInstance);
	
	/**
	 * Select object from database
	 * @param id of the object to be selected from database
	 * @return selected object
	 */
	T select(PK id);
	
	/**
	 * Update object in database
	 * @param transientObject to be updated
	 */
	void update(T transientObject);
	
	/**
	 * Delete object from database
	 * @param persistentObject to be deleted
	 */
	void delete(T persistentObject);
}
