package org.lightadmin.test.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.lightadmin.demo.model.AbstractEntity;
import org.lightadmin.demo.model.EmailAddress;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TestCustomer extends AbstractEntity {

	@Column( length = 64 )
	@NotEmpty
	private String firstname;

	@Column( length = 64 )
	@NotEmpty
	private String lastname;

	@Column( unique = true )
	private EmailAddress emailAddress;

	public TestCustomer() {
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname( String lastname ) {
		this.lastname = lastname;
	}

	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress( EmailAddress emailAddress ) {
		this.emailAddress = emailAddress;
	}

}
