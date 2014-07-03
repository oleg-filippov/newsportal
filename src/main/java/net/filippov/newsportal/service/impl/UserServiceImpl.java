package net.filippov.newsportal.service.impl;

import static net.filippov.newsportal.service.util.QueryParameters.setParam;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.PersistenceException;

import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.domain.UserRole;
import net.filippov.newsportal.exception.NotUniqueUserFieldException;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.repository.GenericRepository;
import net.filippov.newsportal.service.UserService;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserService}
 * 
 * @author Oleg Filippov
 */
@Service("UserService")
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	private GenericRepository<UserRole, Long> roleRepository;
	private PasswordEncoder bCryptPasswordEncoder;
	
	/**
	 * Constructor autowiring needed repositories and
	 * {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}
	 */
	@Autowired
	public UserServiceImpl(GenericRepository<User, Long> repository,
			GenericRepository<UserRole, Long> roleRepository,
			PasswordEncoder bCryptPasswordEncoder) {
		super(repository);
		this.roleRepository = roleRepository;
		this.roleRepository.setType(UserRole.class);
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	/**
	 * Add user into repository with default role ("ROLE_USER") and encoded password
	 * 
	 * @see net.filippov.newsportal.service.impl.AbstractServiceImpl#add(java.io.Serializable)
	 */
	@Override
	@Transactional
	public void add(User user) {
		try {
			UserRole defaultRole = roleRepository.getByNamedQuery(
					"UserRole.GET_BY_AUTHORITY",
					setParam("authority", "ROLE_USER").buildMap());
			Set<UserRole> userRoles = new HashSet<UserRole>();
			userRoles.add(defaultRole);
			user.setRoles(userRoles);
			
			String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			
			repository.add(user);
			LOG.info("ADDED: " + user);
		} catch (PersistenceException e) {
			if (e.getCause().getClass() == ConstraintViolationException.class) {
				ConstraintViolationException ce = (ConstraintViolationException) e.getCause();
				if (ce.getConstraintName().indexOf("LOGIN") != -1) {
					throw new NotUniqueUserFieldException("login");
				} else if (ce.getConstraintName().indexOf("EMAIL") != -1) {
					throw new NotUniqueUserFieldException("email");
				}
			}
			String message = String.format("Unable to add %s", user);
			throw new ServiceException(message, e);
		}
	}

	/**
	 * @see net.filippov.newsportal.service.UserService#getByLogin(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public User getByLogin(String login) {
		try {
			User user = repository.getByNamedQuery(
					"User.GET_BY_LOGIN",
					setParam("login", login).buildMap());
			if (user != null) {
				user.getRoles().size();
			}
			return user;
		} catch (PersistenceException e) {
			String message = String.format("Unable to get user by login=%s", login);
			throw new ServiceException(message, e);
		}
	}

	/**
	 * @see net.filippov.newsportal.service.UserService#getByEmail(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		try {
			User user = repository.getByNamedQuery(
					"User.GET_BY_EMAIL",
					setParam("email", email).buildMap());
			return user;
		} catch (PersistenceException e) {
			String message = String.format("Unable to get user by email=%s", email);
			throw new ServiceException(message, e);
		}
	}
}
