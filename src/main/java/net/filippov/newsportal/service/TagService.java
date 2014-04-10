package net.filippov.newsportal.service;

import java.util.List;
import java.util.Set;

import net.filippov.newsportal.domain.Tag;

public interface TagService extends AbstractService<Tag> {
	
	Tag getByName(String name);
	
	List<String> getAllNames(int resultLimit);
	
	String getAutocompleteJson();
	
	Set<Tag> getTagsFromString(String tagString);
	
	String getTagString(Set<Tag> tags);
}
