package org.lightadmin.demo.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ComplexDataTypeEntity extends AbstractEntity  {

	private String name;

	@ManyToOne
	@JoinColumn( name = "child_id" )
	private ChildTestEntity childEntity;

	public ComplexDataTypeEntity( final String name, ChildTestEntity childEntity ) {
		this.name = name;
		this.childEntity = childEntity;
	}

	public ComplexDataTypeEntity() { }

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getChildEntityName() {
		return this.childEntity.getName();
	}
}
