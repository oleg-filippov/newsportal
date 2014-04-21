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
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "tag")
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
			query = "from Tag t where t.name = :name")
})
public class Tag extends BaseEntity {

	private static final long serialVersionUID = 1282054549729552169L;

	@Column(name = "name", nullable = false, unique = true, length = 20)
	private String name;
	
	// Mock
	@Formula("select count(a.id) from article_tag at "
			+ "join Article a on at.article_id = a.id "
			+ "join Tag t on at.tag_id = t.id where t.id = id")
	private int articleCount;
	
	@ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
	private Set<Article> articles;
	
	public Tag() {}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getArticleCount() {
		return articleCount;
	}
	
	public int getScale() {
		return articleCount > 9 ? 9 : articleCount;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
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
