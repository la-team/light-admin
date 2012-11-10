package org.lightadmin.demo.model;

import javax.persistence.Entity;

@Entity
public class ParentTestEntity extends AbstractEntity {

	private String name;

	public ParentTestEntity( final String name ) {
		this.name = name;
	}

	public ParentTestEntity() {

	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}
}
