package org.lightadmin.test.model;
import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ChildTestEntity extends AbstractEntity {

	private String name;

	@ManyToOne
	private ParentTestEntity parent;

	public ChildTestEntity( final String name, final ParentTestEntity parentEntity ) {
		this.name = name;
		this.parent = parentEntity;
	}

	public ChildTestEntity() {

	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public ParentTestEntity getParent() {
		return parent;
	}

	public void setParent( ParentTestEntity parent ) {
		this.parent = parent;
	}
}
