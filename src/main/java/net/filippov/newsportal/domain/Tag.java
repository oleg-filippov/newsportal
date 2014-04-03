package net.filippov.newsportal.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "tag", uniqueConstraints = {
		@UniqueConstraint(columnNames = "name") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "Tag.GET_ALL",
			query = "from Tag t order by t.name"),
	@NamedQuery(
			name = "Tag.GET_ALL_NAMES",
			query = "select t.name from Tag t order by t.name"),
	@NamedQuery(
			name = "Tag.GET_BY_NAME",
			query = "from Tag t where t.name = :name"),
	@NamedQuery(
			name = "Tag.GET_ALL_BY_FRAGMENT",
			query = "from Tag t where t.name like :fragment order by t.name")
})
public class Tag extends AbstractEntity {

	private static final long serialVersionUID = 1282054549729552169L;

	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "news_tag",
		joinColumns = {@JoinColumn(name = "tag_id") },
		inverseJoinColumns = {@JoinColumn(name = "news_id") })
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
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
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

		Tag other = (Tag) obj;
		
		if (getName() != null
				? !getName().equals(other.getName())
				: other.getName() != null) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Tag[id=%d, name=%s]", getId(), getName());
	}

}
