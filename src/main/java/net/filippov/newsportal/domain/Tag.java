package net.filippov.newsportal.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tag", uniqueConstraints = {
		@UniqueConstraint(columnNames = "name") })
public class Tag extends AbstractEntity {

	private static final long serialVersionUID = 1282054549729552169L;

	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "tags")
	private Set<News> news;
	
	public Tag() {}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<News> getNews() {
		return news;
	}

	public void setNews(Set<News> news) {
		this.news = news;
	}

	@Override
	public int entityHashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}
	
	@Override
	public boolean entityEquals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tag))
			return false;

		Tag other = (Tag) obj;

		return getName().equals(other.getName());
	}

	@Override
	public String toString() {
		return String.format("Tag[id=%d, name=%s]", getId(), getName());
	}
}
