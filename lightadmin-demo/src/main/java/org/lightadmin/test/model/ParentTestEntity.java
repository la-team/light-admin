package org.lightadmin.test.model;
import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Entity
public class ParentTestEntity extends AbstractEntity {

	private String name;

	@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
	@JoinColumn( name = "parent_id" )
	private Set<ComplexDataTypeEntity> complexTypeEntities = newHashSet();

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

	public Set<ComplexDataTypeEntity> getComplexTypeEntities() {
		return Collections.unmodifiableSet( complexTypeEntities );
	}
}
