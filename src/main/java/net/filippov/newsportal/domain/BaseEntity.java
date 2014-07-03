package net.filippov.newsportal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class for all entity-classes. Defines the primary id and version.
 * 
 * @author Oleg Filippov
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1520556867799623763L;

	/**
	 * Primary key of the persistent object
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	/**
	 * Version of the persistent object
	 */
	@Version
	@Column(nullable = false, insertable = false, columnDefinition = "INT DEFAULT 0")
	Integer version;
	
	/**
	 * Get the primary key of the persistent object
     *
     * @return the id
	 */
	public Long getId() {
		return id;
	}
}
