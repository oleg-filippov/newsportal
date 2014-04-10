package net.filippov.newsportal.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.TagService;
import static net.filippov.newsportal.service.util.QueryParameters.setParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TagService")
public class TagServiceImpl extends AbstractServiceImpl<Tag> implements TagService {
	
	@Autowired
	public TagServiceImpl(GenericRepository<Tag, Long> repository) {
		super(repository);
	}
	
	@Override
	public Tag getByName(String name) {
		try {
			return repository.getByNamedQuery("Tag.GET_BY_NAME",
					setParam("name", name).buildMap());
		} catch (PersistenceException e) {
			String message = String.format("Unable to get tag=%s", name);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public List<String> getAllNames(int resultLimit) {
		try {
			return repository.getAllNamesByNamedQuery(
					"Tag.GET_ALL_NAMES", resultLimit);
		} catch (PersistenceException e) {
			throw new ServiceException("Unable to get all tag names", e);
		}
	}
	
	@Override
	public String getAutocompleteJson() {
		List<String> tagNames = getAllNames(0);
		StringBuilder r = new StringBuilder("[");
		for (String name : tagNames) {
			r.append("{value:\'")
				.append(name)
				.append("\'},");
		}
		r.delete(r.length()-1, r.length());
		r.append("]");
		return r.toString();
	}
	
	@Override
	public Set<Tag> getTagsFromString(String tagString) {
		Set<Tag> result = new HashSet<Tag>();
		for (String tagName : tagString.split(",")) {
			tagName = tagName.replaceAll("\\s+", "");
			Tag tagObj = this.getByName(tagName);	// get persistent object
			if (tagObj == null) {
				tagObj = new Tag();
				tagObj.setName(tagName);
			}
			result.add(tagObj);
		}
		return result;
	}

	@Override
	public String getTagString(Set<Tag> tags) {
		StringBuilder result = new StringBuilder();
		for (Tag tag : tags) {
			result.append(tag.getName()).append(",");
		}
		result.delete(result.length()-1, result.length());
		return result.toString();
	}
}
