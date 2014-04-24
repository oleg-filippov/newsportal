package net.filippov.newsportal.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

/**
 * Category of article
 * 
 * @author Oleg Filippov
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "Category.GET_ALL",
			query = "from Category c order by c.name"),
	@NamedQuery(
			name = "Category.GET_BY_NAME",
			query = "from Category c where c.name = :name")
})
public class Category extends BaseEntity {

	private static final long serialVersionUID = 7371123602615782324L;

	/**
	 * Category name
	 */
	@Column(name = "name", nullable = false, unique = true, length = 30)
	private String name;
	
	/**
	 * Article count having this category
	 */
	@Formula("select count(id) from Article a where a.category_id = id")
	private int articleCount;
	
	/**
	 * Articles having this category
	 */
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private Set<Article> articles;
	
	public Category() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getArticleCount() {
		return articleCount;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		
		Category other = (Category) obj;
		
		if (getName() != null
				? !getName().equals(other.getName())
				: other.getName() != null) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Category[id=%d, name=%s]", getId(), getName());
	}
}
