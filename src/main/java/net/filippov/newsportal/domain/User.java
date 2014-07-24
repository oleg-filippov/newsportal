package net.filippov.newsportal.domain;

import java.util.Date;
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
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Stores information about user
 * 
 * @author Oleg Filippov
 */
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "User.GET_BY_LOGIN",
			query = "FROM User u WHERE u.login = :login"),
	@NamedQuery(
			name = "User.GET_BY_EMAIL",
			query = "FROM User u WHERE u.email = :email")
})
public class User extends BaseEntity {
	
	private static final long serialVersionUID = 8091488929047153516L;

	/**
	 * User login
	 */
	@Size(min = 4, max = 30, message = "{validation.user.loginSize}")
	@Column(name = "login", nullable = false, unique = true)
	private String login;

	/**
	 * User password
	 */
	@Size(min = 7, max = 60, message = "{validation.user.passwordSize}")
	@Column(name = "password", nullable = false)
	private String password;

	/**
	 * User fullname
	 */
	@Size(min = 1, max = 50, message = "{validation.user.nameNotBlank}")
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * User e-mail
	 */
	@NotBlank(message = "{validation.user.emailNotBlank}")
	@Email(message = "{validation.user.emailValid}")
	@Column(name = "email", nullable = false, unique = true, length = 50)
	private String email;

	/**
	 * Registration date and time
	 */
	@Column(name = "registered", insertable = false, updatable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date registered;

	/**
	 * State of user (locked or not)
	 */
	@Column(name = "locked", insertable = false,
			columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean locked;
	
	/**
	 * State of user (enabled or not)
	 */
	@Column(name = "enabled", insertable = false,
			columnDefinition = "BOOLEAN DEFAULT TRUE")
	private boolean enabled;
	
	/**
	 * Article count of this user
	 */
	@Formula("SELECT COUNT(a.id) FROM Article a WHERE a.user_id = id")
	private int articleCount;
	
	/**
	 * Comment count of this user
	 */
	@Formula("SELECT COUNT(c.id) FROM Comment c WHERE c.user_id = id")
	private int commentCount;

	/**
	 * User roles
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",
			joinColumns = {@JoinColumn(name = "user_id") },
			inverseJoinColumns = {@JoinColumn(name = "role_id") })
	private Set<UserRole> roles;
	
	/**
	 * User articles
	 */
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private Set<Article> articles;

	/**
	 * User comments
	 */
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private Set<Comment> comments;
	
	/**
	 * Default constructor initializing needed fields
	 */
	public User() {
		registered = new Date();
		locked = false;
		enabled = true;
	}

	/**
	 * @return user login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login user login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return user password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password user password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return user full name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name user full name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return user e-mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email user e-mail to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return registration date and time
	 */
	public Date getRegistered() {
		return registered;
	}

	/**
	 * @return true if user is locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return true if user is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return this user article count
	 */
	public int getArticleCount() {
		return articleCount;
	}

	/**
	 * @return this user comment count
	 */
	public int getCommentCount() {
		return commentCount;
	}

	/**
	 * @return user roles
	 */
	public Set<UserRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles user roles to set
	 */
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	/**
	 * @return this user comments
	 */
	public Set<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments this user comments to set
	 */
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @return this user articles
	 */
	public Set<Article> getArticles() {
		return articles;
	}

	/**
	 * @param articles this user articles to set
	 */
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getLogin() == null) ? 0 : getLogin().hashCode());
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
		
		User other = (User) obj;
		
		if (getLogin() != null
				? !getLogin().equals(other.getLogin())
				: other.getLogin() != null) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("User[id=%d, login=%s]", getId(), getLogin());
	}
}
