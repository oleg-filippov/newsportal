package net.filippov.newsportal.service;

import java.util.List;

import net.filippov.newsportal.domain.UserRole;

public interface UserRoleService {

	UserRole getByAuthority(String authority);
	
	List<UserRole> getAll();
}
