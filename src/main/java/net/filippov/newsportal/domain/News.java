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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "news")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "News.GET_ALL",
			query = "from News n order by n.created desc"),
	@NamedQuery(
			name = "News.GET_ALL_BY_USER_ID",
			query = "from News n where n.author.id = :id order by n.created desc"),
	@NamedQuery(
			name = "News.GET_ALL_BY_TAG_ID",
			query = "from News n join n.tags t where t.id = :id"),
	@NamedQuery(
			name = "News.GET_COUNT",
			query = "select count(*) from News"),
	@NamedQuery(
			name = "News.GET_COUNT_BY_USER_ID",
			query = "select count(*) from News n where n.author.id = :id"),
	@NamedQuery(
			name = "News.GET_COUNT_BY_TAG_ID",
			query = "select count(*) from News n join n.tags t where t.id = :id"),
	@NamedQuery(
			name = "News.INCREASE_VIEWS_COUNT_BY_ID",
			query = "update News set views_count = views_count + 1 where id = :id")
})
public class News extends AbstractEntity {

	private static final long serialVersionUID = 3513552163842451989L;

	@NotBlank(message = "{validation.news.title}")
	@Column(name = "title", nullable = false, columnDefinition = "VARCHAR(100)")
	private String title;

	@NotBlank(message = "{validation.news.preview}")
	@Column(name = "preview", nullable = false)
	private String preview;

	@NotBlank(message = "{validation.news.content}")
	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "created", insertable = false, updatable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date created;

	@Column(name = "last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;

	@Column(name = "views_count")
	private int viewsCount;

	@Formula("select count(*) from Comment c where c.news_id = id")
	private int commentsCount;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User author;

	@OneToMany(mappedBy = "news", cascade = CascadeType.REMOVE)
	private List<Comment> comments;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "news_tag",
			joinColumns = {@JoinColumn(name = "news_id") },
			inverseJoinColumns = {@JoinColumn(name = "tag_id") })
	@OrderBy("name")
	private Set<Tag> tags;

	public News() {
		created = new Date();
		viewsCount = 0;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public int getViewsCount() {
		return viewsCount;
	}
	
	public void setViewsCount(int viewsCount) {
		this.viewsCount = viewsCount;
	}
	
	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getTitle() == null) ? 0 : getTitle().hashCode());
		result = prime * result
				+ ((getCreated() == null) ? 0 : getCreated().hashCode());
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

		News other = (News) obj;
		
		if (getTitle() != null
				? !getTitle().equals(other.getTitle())
				: other.getTitle() != null) {
			return false;
		}
		if (getCreated() != null
				? getCreated().compareTo(other.getCreated()) != 0
				: other.getCreated() != null) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("News[id=%d, author=%s]",
				getId(), getAuthor().getLogin());
	}
}
