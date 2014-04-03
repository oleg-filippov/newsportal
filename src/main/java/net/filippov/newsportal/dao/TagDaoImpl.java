package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.exception.PersistentException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("TagDao")
public class TagDaoImpl extends GenericDaoHibernateImpl<Tag, Long>
		implements TagDao {
	
	public TagDaoImpl() {}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tag> getAll() {
	    try {
	    	return getCurrentSession().getNamedQuery("Tag.GET_ALL")
	    			.setCacheable(true)
	    			.list();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting all tags", e);
		}
	}

	@Override
	public Tag getByName(String name) {
		try {
	    	Query query = getCurrentSession().getNamedQuery("Tag.GET_BY_NAME")
	    			.setParameter("name", name)
	    			.setCacheable(true);
	    	if (query.list().size() > 0) {
    			return (Tag) query.list().get(0);
    		} else {
    			return null;
    		}
		} catch (HibernateException e) {
			throw new PersistentException("Error getting tag by name=" + name, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getAutocompleteJson() {
		try {
	    	List<String> tagNames = getCurrentSession().getNamedQuery("Tag.GET_ALL_NAMES")
	    			.setCacheable(true)
	    			.list();
	    	return !tagNames.isEmpty()
	    			? tagNames.toString().replaceAll("\\s+", "")
	    			: "";
		} catch (HibernateException e) {
			throw new PersistentException("Error getting tags autocompleteJson", e);
		}
	}
}
