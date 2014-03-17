package net.filippov.newsportal.service;

import java.util.List;

import net.filippov.newsportal.dao.UserRoleDao;
import net.filippov.newsportal.domain.UserRole;
import net.filippov.newsportal.exception.PersistentException;
import net.filippov.newsportal.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("UserRoleService")
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleDao storage;
	
	@Override
	public UserRole getByAuthority(String authority) {
		try {
			return storage.getByAuthority(authority);
		} catch (PersistentException e) {
			throw new ServiceException("Error getting Role[authority=" + authority +"]", e);
		}
	}

	@Override
	@Transactional
	public List<UserRole> getAll() {
		try {
			return storage.getAll();
		} catch (PersistentException e) {
			throw new ServiceException("Error getting list of roles", e);
		}
	}
}
