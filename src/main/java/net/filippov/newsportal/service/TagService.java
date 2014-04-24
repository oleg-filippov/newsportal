package net.filippov.newsportal.service;

import java.util.List;
import java.util.Set;

import net.filippov.newsportal.domain.Tag;

/**
 * Provides tag-related operations
 * 
 * @author Oleg Filippov
 */
public interface TagService extends AbstractService<Tag> {
	
	/**
	 * Get tag from repository by it's name
	 * 
	 * @param name tag name
	 * @return tag
	 */
	Tag getByName(String name);
	
	/**
	 * Get all tag names
	 * 
	 * @return list of tags
	 */
	List<String> getAllNames();
	
	/**
	 * Get json of all tag names
	 * 
	 * @return json
	 */
	String getAutocompleteJson();
	
	/**
	 * Get set of tags from comma-separated string of tags
	 * 
	 * @param tagString string of tags
	 * @return set of tags
	 */
	Set<Tag> getTagsFromString(String tagString);
	
	/**
	 * Get comma-separated string of tags from set of tags
	 * 
	 * @param tags set of tags
	 * @return string of tags
	 */
	String getTagString(Set<Tag> tags);
}
