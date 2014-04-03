package net.filippov.newsportal.service;

import java.util.List;
import java.util.Set;

import net.filippov.newsportal.domain.Tag;

public interface TagService {
	
	Long add(Tag tag);
	
	void addAll(Set<Tag> tags);
	
	Tag getById(Long id);
	
	Tag getByName(String name);
	
	List<Tag> getAll();
	
	void update(Tag tag);
	
	void deleteById(Long id);
	
	String getAutocompleteJson();
	
	Set<Tag> getTagsFromString(String tagsString);
	
	String getTagsString(Set<Tag> tags);
}
