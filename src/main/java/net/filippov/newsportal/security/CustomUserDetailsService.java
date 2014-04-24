package net.filippov.newsportal.security;

import java.util.ArrayList;
import java.util.Collection;

import net.filippov.newsportal.domain.UserRole;
import net.filippov.newsportal.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link UserDetailsService}
 * 
 * @author Oleg Filippov
 */
@Component("UserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService service;

	@Override
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
	
		if ("".equals(login)) {
			throw new UsernameNotFoundException("User not found");
		}
		
		net.filippov.newsportal.domain.User user = service.getByLogin(login);

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		
		return new User(
				user.getLogin(),
				user.getPassword(),
				user.isEnabled(),
				accountNonExpired,
				credentialsNonExpired,
				!user.isLocked(),
				getAuthorities(user.getRoles()));
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(
			Collection<UserRole> roles) {

		Collection<GrantedAuthority> resultRoles = new ArrayList<GrantedAuthority>();
		for (UserRole role : roles) {
			resultRoles.add(role);
		}
		return resultRoles;
	}
}
