package org.lightadmin.demo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table( name = "DEMO_ENTRIES" )
public class Entry {

	@Id
	@GeneratedValue
	private Integer id;

	@Basic
	@Column( length = 64 )
	@NotEmpty
	@Size( min = 1, max = 50 )
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId( final Integer id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}
}