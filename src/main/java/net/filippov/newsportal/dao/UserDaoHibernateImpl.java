package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.NotUniqueUserEmailException;
import net.filippov.newsportal.exception.NotUniqueUserLoginException;
import net.filippov.newsportal.exception.PersistentException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository("UserDao")
public class UserDaoHibernateImpl extends GenericDaoHibernateImpl<User, Long>
		implements UserDao {
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public UserDaoHibernateImpl() {}

	@Override
	public void delete(User obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public User getByLogin(String login) {
    	try {
    		Query query = getCurrentSession().getNamedQuery("User.GET_BY_LOGIN")
    			.setParameter("login", login);
    		
    		if (query.list().size() > 0) {
    			return (User) query.list().get(0);
    		} else {
    			return null;
    		}
		} catch (HibernateException e) {
			throw new PersistentException("Error getting user with login=" + login, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		try {
    		return getCurrentSession().getNamedQuery("User.GET_ALL")
    			.list();
		} catch (HibernateException e) {
			throw new PersistentException("Error getting all users", e);
		}
	}
	
	@Override
	public Long insert(User user) {
		try {
			String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
    		return (Long) getCurrentSession().save(user);
		} catch(ConstraintViolationException e) {
			if (e.getConstraintName().startsWith("\"LOGIN_UNIQUE")) {
				throw new NotUniqueUserLoginException(e);
			} else if (e.getConstraintName().startsWith("\"EMAIL_UNIQUE")) {
				throw new NotUniqueUserEmailException(e);
			} else {
				throw new PersistentException(
					"Error inserting " + user, e);
			}
		} catch (HibernateException e) {
			throw new PersistentException("Error inserting " + user, e);
		}
	}
}
