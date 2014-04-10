package net.filippov.newsportal.service;

import net.filippov.newsportal.domain.User;

public interface UserService extends AbstractService<User> {

	User getByLogin(String login);
}
