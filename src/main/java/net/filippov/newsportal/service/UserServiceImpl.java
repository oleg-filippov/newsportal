package net.filippov.newsportal.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.filippov.newsportal.dao.UserDao;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.domain.UserRole;
import net.filippov.newsportal.exception.PersistentException;
import net.filippov.newsportal.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao storage;
	
	@Autowired
	private UserRoleService roleService;
	
	public UserServiceImpl() {}
	
	@Override
	public Long add(User user) {
		try {
			UserRole defaultRole = roleService.getByAuthority("ROLE_USER");
			Set<UserRole> userRoles = new HashSet<UserRole>();
			userRoles.add(defaultRole);
			user.setRoles(userRoles);
			return storage.insert(user);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public User getById(Long id) {
		try {
			return storage.select(id);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public User getByLogin(String login) {
		try {
			return storage.getByLogin(login);
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<User> getAll() {
		try {
			return storage.getAll();
		} catch (PersistentException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
}
