package net.filippov.newsportal.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.springframework.security.core.GrantedAuthority;

/**
 * Implementation of {@link GrantedAuthority}
 * Represents user-roles (ROLE_USER, ROLE_AUTHOR, ROLE_ADMIN)
 * 
 * @author Oleg Filippov
 */
@Entity
@Table(name = "role")
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
	@NamedQuery(
			name = "UserRole.GET_BY_AUTHORITY",
			query = "from UserRole ur where ur.authority = :authority")
})
public class UserRole extends BaseEntity implements GrantedAuthority {
	
	private static final long serialVersionUID = 3571970651167544190L;

	/**
	 * Name of role
	 */
	@Column(name = "authority", nullable = false, unique = true, length = 20)
	private String authority;
	
	/**
	 * Set of users having this role
	 */
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;

	/**
	 * Default constructor
	 */
	public UserRole() {}

	/**
	 * @return users granted with this role
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users users granted with this role
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	/**
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getAuthority() == null) ? 0 : getAuthority().hashCode());
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		UserRole other = (UserRole) obj;
		
		if (getAuthority() != null
				? !getAuthority().equals(other.getAuthority())
				: other.getAuthority() != null) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("UserRole[id=%d, authority=%s]",
				getId(), getAuthority());
	}
}
