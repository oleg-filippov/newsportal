package net.filippov.newsportal.service;

import java.util.List;

import net.filippov.newsportal.domain.User;

public interface UserService {

	Long add(User user);
	
	User getById(Long id);
	
	User getByLogin(String login);
	
	List<User> getAll();
	
}
