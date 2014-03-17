package net.filippov.newsportal.dao;

import java.util.List;

import net.filippov.newsportal.domain.UserRole;

public interface UserRoleDao extends GenericDao<UserRole, Long> {

	UserRole getByAuthority(String authority);
	
	List<UserRole> getAll();
}
