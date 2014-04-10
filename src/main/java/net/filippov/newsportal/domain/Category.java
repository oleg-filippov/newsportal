package net.filippov.newsportal.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "category", uniqueConstraints = {
		@UniqueConstraint(columnNames = "name") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "Category.GET_ALL",
			query = "from Category c order by c.name"),
	@NamedQuery(
			name = "Category.GET_ALL_NAMES",
			query = "select c.name from Category c order by c.name"),
	@NamedQuery(
			name = "Category.GET_BY_NAME",
			query = "from Category c where c.name = :name"),
	@NamedQuery(
			name = "Category.GET_ALL_BY_FRAGMENT",
			query = "from Category c where c.name like :fragment order by c.name")
})
public class Category extends BaseEntity {

	private static final long serialVersionUID = 7371123602615782324L;

	@Column(name = "name", nullable = false, length = 30)
	private String name;
	
//	@Formula("select count(n.id) from Category c join c.news n where c.id = n.category.id")
//	private int newsCount;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private Set<News> news;
	
	public Category() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public int getNewsCount() {
//		return newsCount;
//	}

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
