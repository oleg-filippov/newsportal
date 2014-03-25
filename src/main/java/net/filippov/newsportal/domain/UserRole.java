package net.filippov.newsportal.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role", uniqueConstraints = {
		@UniqueConstraint(columnNames = "authority") })
@NamedQueries({
	@NamedQuery(
			name = "UserRole.GET_ALL",
			query = "from UserRole"),
	@NamedQuery(
			name = "UserRole.GET_BY_AUTHORITY",
			query = "from UserRole where authority = :authority")
})
public class UserRole extends AbstractEntity implements GrantedAuthority {

	private static final long serialVersionUID = -3981661393211469078L;
	
	@Column(name = "authority")
	private String authority;
	
	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
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
	public int entityHashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getAuthority() == null) ? 0 : getAuthority().hashCode());
		return result;
	}
	
	@Override
	public boolean entityEquals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		UserRole other = (UserRole) obj;

		return getAuthority().equals(other.getAuthority());
	}

	@Override
	public String toString() {
		return String.format("UserRole[id=%d, authority=%s]",
				getId(), getAuthority());
	}
}
