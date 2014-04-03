package net.filippov.newsportal.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.filippov.newsportal.dao.TagDao;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.exception.PersistentException;
import net.filippov.newsportal.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TagService")
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao storage;
	
	public TagServiceImpl() {}

	@Override
	@Transactional
	public List<Tag> getAll() {
		try {
			return storage.getAll();
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public Tag getByName(String name) {
		try {
			return storage.getByName(name);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public Long add(Tag tag) {
		try {
			return storage.insert(tag);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public void addAll(Set<Tag> tags) {
		try {
			for (Tag tag : tags) {
				storage.insert(tag);
			}
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public Tag getById(Long id) {
		try {
			return storage.select(id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void update(Tag tag) {
		try {
			storage.update(tag);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		try {
			Tag tag = storage.select(id);
			if (tag != null) {
				storage.delete(tag);
			}
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public String getAutocompleteJson() {
		try {
			return storage.getAutocompleteJson();
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public Set<Tag> getTagsFromString(String tagsString) {
		Set<Tag> result = new HashSet<Tag>();
		for (String tagName : tagsString.split(",")) {
			tagName = tagName.replaceAll("\\s+", "");
			try {
				Tag tagObj = storage.getByName(tagName);
				if (tagObj != null) {
					result.add(tagObj);
				} else {
					tagObj = new Tag();
					tagObj.setName(tagName);
					result.add(tagObj);
					storage.insert(tagObj);
				}
			} catch (PersistentException e) {
				throw new ServiceException(e.getMessage(), e);
			}
		}
		return result;
	}

	@Override
	public String getTagsString(Set<Tag> tags) {
		StringBuilder result = new StringBuilder();
		for (Tag tag : tags) {
			result.append(tag.getName()).append(",");
		}
		result.delete(result.length()-1, result.length());
		return result.toString();
	}
}
