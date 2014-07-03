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

/**
 * Represents an article with String content
 * 
 * @author Oleg Filippov
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "Article.GET_ALL",
			query = "from Article a order by a.created desc"),
	@NamedQuery(
			name = "Article.GET_ALL_BY_USER_ID",
			query = "from Article a where a.author.id = :id order by a.created desc"),
	@NamedQuery(
			name = "Article.GET_ALL_BY_CATEGORY_NAME",
			query = "from Article a where a.category.name = :name order by a.created desc"),
	@NamedQuery(
			name = "Article.GET_ALL_BY_TAG_NAME",
			query = "select a from Tag t join t.articles a where t.name = :name order by a.created desc"),
	@NamedQuery(
			name = "Article.GET_ALL_BY_FRAGMENT",
			query = "from Article a where a.content like :fragment order by a.created desc"),
	@NamedQuery(
			name = "Article.GET_COUNT_BY_FRAGMENT",
			query = "select count(a.id) from Article a where a.content like :fragment")
})
public class Article extends BaseEntity {

	private static final long serialVersionUID = 38150497082508411L;

	/**
	 * Article title
	 */
	@NotBlank(message = "{validation.article.title}")
	@Column(name = "title", nullable = false, length = 100)
	private String title;

	/**
	 * Article preview
	 */
	@NotBlank(message = "{validation.article.preview}")
	@Column(name = "preview", nullable = false)
	private String preview;

	/**
	 * Article content
	 */
	@NotBlank(message = "{validation.article.content}")
	@Column(name = "content", nullable = false, length = 65535)
	private String content;

	/**
	 * Article creation date and time
	 */
	@Column(name = "created", insertable = false, updatable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date created;

	/**
	 * Article modification date and time
	 */
	@Column(name = "last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;

	/**
	 * Article view count
	 */
	@Column(name = "view_count", insertable = false, columnDefinition = "INT DEFAULT 0")
	private int viewCount;

	/**
	 * Article comment count
	 */
	@Formula("select count(id) from Comment c where c.article_id = id")
	private int commentCount;
	
	/**
	 * Author of this article
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User author;

	/**
	 * Category of this article
	 */
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	/**
	 * Comments to this article
	 */
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Comment> comments;
	
	/**
	 * Tags of this article
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "article_tag",
			joinColumns = {@JoinColumn(name = "article_id") },
			inverseJoinColumns = {@JoinColumn(name = "tag_id") })
	@OrderBy("name")
	private Set<Tag> tags;

	/**
	 * Default constructor initializing needed fields
	 */
	public Article() {
		created = new Date();
		viewCount = 0;
	}

	/**
	 * @return article title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title article title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return article preview
	 */
	public String getPreview() {
		return preview;
	}

	/**
	 * @param preview article preview to set
	 */
	public void setPreview(String preview) {
		this.preview = preview;
	}

	/**
	 * @return article content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content article content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return article date and time of creation
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @return article modification date and time
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified date and time when article was last modified
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return article view count
	 */
	public int getViewCount() {
		return viewCount;
	}
	
	/**
	 * @param viewCount article view count to set
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	/**
	 * @return article comment count
	 */
	public int getCommentCount() {
		return commentCount;
	}

	/**
	 * @return author of this article
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author author of this article to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}
	
	/**
	 * @return category of this article
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category category of this article to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return comments to this article
	 */
	public Set<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments comments to this article to set
	 */
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	/**
	 * @return tags of this article
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags tags of this article to set
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
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

		Article other = (Article) obj;
		
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

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Article[id=%d, author=%s]",
				getId(), getAuthor() == null ? "null" : getAuthor().getLogin());
	}
}
