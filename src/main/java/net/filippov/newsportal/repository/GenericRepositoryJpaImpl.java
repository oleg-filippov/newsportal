package net.filippov.newsportal.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of {@link GenericRepository}
 * 
 * @param <T> The persistent type
 * @param <PK> The primary key type
 * 
 * @see net.filippov.newsportal.repository.GenericRepository
 * 
 * @author Oleg Filippov
 */
@Repository
@Scope("prototype")
public class GenericRepositoryJpaImpl<T extends Serializable, PK extends Serializable>
		implements GenericRepository<T, PK> {
	
	/**
	 * Persistent object class
	 */
	private Class<T> type;
	
	public void setType(Class<T> type) {
		this.type = type;
	}

	@PersistenceContext
	protected EntityManager em;

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#add(java.lang.Object)
	 */
	@Override
	public void add(T obj) {
		em.persist(obj);
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#get(java.io.Serializable)
	 */
	@Override
	public T get(PK id) {
		return em.find(type, id);
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#update(java.lang.Object)
	 */
	@Override
	public T update(T obj) {
		return em.merge(obj);
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#delete(java.lang.Object)
	 */
	@Override
	public void delete(T obj) {
		em.remove(obj);
	}
	
	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#deleteById(java.io.Serializable)
	 */
	@Override
	public void deleteById(PK id) {
		T obj = this.get(id);
		this.delete(obj);
	}
	
	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getByNamedQuery(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public T getByNamedQuery(String namedQuery, Map<String, Object> parameters) {
		try {
			TypedQuery<T> query = em.createNamedQuery(namedQuery, type);
			return setParameters(query, parameters).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getAll()
	 */
	@Override
	public List<T> getAll() {
		
		return em.createQuery("from " + type.getSimpleName(), type).getResultList();
	}
	
	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getAllByNamedQuery(
	 * java.lang.String)
	 */
	@Override
	public List<T> getAllByNamedQuery(String namedQuery) {
		
		return getAllByNamedQuery(namedQuery, 0, 0);
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getAllByNamedQuery(
	 * java.lang.String, int, int)
	 */
	@Override
	public List<T> getAllByNamedQuery(String namedQuery,
			int firstResult, int resultLimit) {
		
		return getAllByNamedQuery(namedQuery, null, firstResult, resultLimit);
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getAllByNamedQuery(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public List<T> getAllByNamedQuery(String namedQuery,
			Map<String, Object> parameters) {
		
		return getAllByNamedQuery(namedQuery, parameters, 0, 0);
	}

	/**
	 * All getAllByNamedQuery...-methods delegate to this one
	 * 
	 * @see net.filippov.newsportal.repository.GenericRepository#getAllByNamedQuery(
	 * java.lang.String, java.util.Map, int, int)
	 */
	@Override
	public List<T> getAllByNamedQuery(String namedQuery,
			Map<String, Object> parameters, int firstResult, int resultLimit) {

		TypedQuery<T> query = em.createNamedQuery(namedQuery, type)
				.setFirstResult(firstResult);
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		if (parameters != null) {
			query = setParameters(query, parameters);
		}
		return query.getResultList();
	}
	
	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getAllNamesByNamedQuery(
	 * java.lang.String, java.util.Map, int, int)
	 */
	@Override
	public List<String> getAllNamesByNamedQuery(String namedQuery, int resultLimit) {
		
		TypedQuery<String> query = em.createNamedQuery(namedQuery, String.class);
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		return query.getResultList();
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getCount()
	 */
	@Override
	public int getCount() {
		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> cQuery = cBuilder.createQuery(Long.class);
		cQuery.select(cBuilder.count(cQuery.from(type)));

		return em.createQuery(cQuery).getSingleResult().intValue();
	}

	/**
	 * @see net.filippov.newsportal.repository.GenericRepository#getCountByNamedQuery(java.lang.String, java.util.Map)
	 */
	@Override
	public int getCountByNamedQuery(String namedQuery,
			Map<String, Object> parameters) {
		
		TypedQuery<Long> query = em.createNamedQuery(namedQuery, Long.class);
		return setParameters(query, parameters).getSingleResult().intValue();
	}
	
	/**
	 * Bind parameters to the typed query
	 * @param query typed query
	 * @param parameters Map of parameters to set
	 * @return typed query with parameters
	 */
	private <P> TypedQuery<P> setParameters(TypedQuery<P> query,
			Map<String, Object> parameters) {
		
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}
}
