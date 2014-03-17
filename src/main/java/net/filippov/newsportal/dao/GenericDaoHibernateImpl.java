package net.filippov.newsportal.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.filippov.newsportal.exception.PersistentException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericDaoHibernateImpl<T, PK extends Serializable>
		implements GenericDao<T, PK> {

	private Class<T> type;
	
	@Autowired
    protected SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public GenericDaoHibernateImpl() {
		Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@SuppressWarnings("unchecked")
    public PK insert(T obj) {
    	try {
    		return (PK) getCurrentSession().save(obj);
		} catch (HibernateException e) {
			throw new PersistentException("Error inserting " + obj, e);
		}
    }

	@Override
	@SuppressWarnings("unchecked")
    public T select(PK id) {
    	try {
    		return (T) getCurrentSession().get(type, id);
		} catch (HibernateException e) {
			throw new PersistentException("Error selecting " + type + "[id=" + id + "]", e);
		}
    }

    @Override
    public void update(T obj) {
    	try {
    		getCurrentSession().update(obj);
		} catch (HibernateException e) {
			throw new PersistentException("Error updating " + obj, e);
		}
    }

    @Override
    public void delete(T obj) {
    	try {
    		getCurrentSession().delete(obj);
		} catch (HibernateException e) {
			throw new PersistentException("Error deleting " + obj, e);
		}
    }
}
