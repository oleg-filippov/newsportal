package net.filippov.newsportal.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "user", uniqueConstraints = {
		@UniqueConstraint(columnNames = "login"),
		@UniqueConstraint(columnNames = "email") })
@NamedQueries({
	@NamedQuery(
			name = "User.GET_BY_LOGIN",
			query = "from User where login = :login"),
	@NamedQuery(
			name = "User.GET_ALL",
			query = "from User")
})
public class User extends AbstractEntity {

	private static final long serialVersionUID = 286409866205173021L;
	
	@Size(min = 4, max = 20, message = "{validation.user.loginSize}")
	@Column(name = "login", unique = true, nullable = false)
	private String login;

	@Size(min = 7, max = 60, message = "{validation.user.passwordSize}")
	@Column(name = "password", nullable = false)
	private String password;

	@Size(min = 1, max = 50, message = "{validation.user.nameNotBlank}")
	@Column(name = "name", nullable = false)
	private String name;

	@NotBlank(message = "{validation.user.emailNotBlank}")
	@Email(message = "{validation.user.emailValid}")
	@Column(name = "email", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
	private String email;

	@Column(name = "registered", insertable = false, updatable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registered;

	@Column(name = "locked", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean locked;
	
	@Column(name = "enabled", columnDefinition = "BOOLEAN DEFAULT TRUE")
	private boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role",
			joinColumns = {@JoinColumn(name = "user_id") },
			inverseJoinColumns = {@JoinColumn(name = "role_id") })
	private Set<UserRole> roles;
	
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private List<News> news;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private List<Comment> comments;
	
	public User() {}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	@Override
	public int entityHashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getEmail() == null) ? 0 : getEmail().hashCode());
		result = prime * result
				+ ((getLogin() == null) ? 0 : getLogin().hashCode());
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
		
		User other = (User) obj;
		
		return getEmail().equals(other.getEmail())
				&& getLogin().equals(other.getLogin());
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, login=%s]", getId(), getLogin());
	}
}
