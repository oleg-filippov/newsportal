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
 * Represents category of article
 * 
 * @author Oleg Filippov
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "Category.GET_ALL",
			query = "FROM Category c ORDER BY c.name"),
	@NamedQuery(
			name = "Category.GET_BY_NAME",
			query = "FROM Category c WHERE c.name = :name")
})
public class Category extends BaseEntity {

	private static final long serialVersionUID = 7369591777044660460L;

	/**
	 * Category name
	 */
	@Column(name = "name", nullable = false, unique = true, length = 30)
	private String name;
	
	/**
	 * Article count having this category
	 */
	@Formula("SELECT COUNT(id) FROM Article a WHERE a.category_id = id")
	private int articleCount;
	
	/**
	 * Articles having this category
	 */
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private Set<Article> articles;
	
	/**
	 * Default constructor
	 */
	public Category() {}

	/**
	 * @return name of this category
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name category name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return article count having this category
	 */
	public int getArticleCount() {
		return articleCount;
	}

	/**
	 * @return articles having this category
	 */
	public Set<Article> getArticles() {
		return articles;
	}

	/**
	 * @param articles articles having this category
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		
		Category other = (Category) obj;
		
		if (getName() != null
				? !getName().equals(other.getName())
				: other.getName() != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Category[id=%d, name=%s]", getId(), getName());
	}
}
