package org.lightadmin.test.model;
import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Entity
public class ParentTestEntity extends AbstractEntity {

	private String name;

	@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
	@JoinColumn( name = "parent_id" )
	private List<ComplexDataTypeEntity> complexTypeEntities = newArrayList();

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

	public void addComplexTypeEntity( ComplexDataTypeEntity entity ) {
		this.complexTypeEntities.add( entity );
	}

	public List<ComplexDataTypeEntity> getComplexTypeEntities() {
		return Collections.unmodifiableList( complexTypeEntities );
	}
}
