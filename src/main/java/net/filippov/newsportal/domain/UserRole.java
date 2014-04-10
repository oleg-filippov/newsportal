package net.filippov.newsportal.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role", uniqueConstraints = {
		@UniqueConstraint(columnNames = "authority") })
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
	@NamedQuery(
			name = "UserRole.GET_ALL",
			query = "from UserRole"),
	@NamedQuery(
			name = "UserRole.GET_BY_AUTHORITY",
			query = "from UserRole ur where ur.authority = :authority")
})
public class UserRole extends BaseEntity implements GrantedAuthority {

	private static final long serialVersionUID = -3981661393211469078L;
	
	@Column(name = "authority", length = 20)
	private String authority;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getAuthority() == null) ? 0 : getAuthority().hashCode());
		return result;
	}
	
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

	@Override
	public String toString() {
		return String.format("UserRole[id=%d, authority=%s]",
				getId(), getAuthority());
	}
}
