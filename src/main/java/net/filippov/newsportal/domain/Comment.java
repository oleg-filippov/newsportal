package net.filippov.newsportal.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity {

	private static final long serialVersionUID = 8668190795666429761L;

	@NotBlank(message = "{validation.comment.content}")
	@Column(name = "content")
	private String content;
	
	@Column(name = "created", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "news_id")
	private News news;
	
	public Comment() {}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
	
	@Override
	public int entityHashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getContent() == null) ? 0 : getContent().hashCode());
		result = prime * result
				+ ((getCreated() == null) ? 0 : getCreated().hashCode());
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

		Comment other = (Comment) obj;

		return getContent().equals(other.getContent())
				&& getCreated().equals(other.getCreated());
	}

	@Override
	public String toString() {
		return String.format("Comment[id=%d, author=%s, news=%s]",
				getId(), getAuthor(), getNews());
	}
}
