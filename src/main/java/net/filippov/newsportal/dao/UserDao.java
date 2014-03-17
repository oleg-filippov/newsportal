package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.User;

public interface UserDao extends GenericDao<User, Long> {

	User getByLogin(String login);
	
	List<User> getAll();
}
