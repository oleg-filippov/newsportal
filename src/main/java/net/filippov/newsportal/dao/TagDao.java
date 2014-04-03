package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.Tag;

public interface TagDao extends GenericDao<Tag, Long> {
	
	List<Tag> getAll();

	Tag getByName(String name);
	
	String getAutocompleteJson();
}
