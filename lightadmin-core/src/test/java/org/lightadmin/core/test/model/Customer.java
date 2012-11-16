package org.lightadmin.core.test.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Entity
public class Customer extends AbstractEntity {

	@Column( length = 64 )
	@NotEmpty
	private String firstname;

	@Column( length = 64 )
	@NotEmpty
	private String lastname;

	@Column( unique = true )
	private EmailAddress emailAddress;

	@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
	@JoinColumn( name = "customer_id" )
	private Set<Address> addresses = newHashSet();

	public Customer( String firstname, String lastname ) {
		Assert.hasText( firstname );
		Assert.hasText( lastname );

		this.firstname = firstname;
		this.lastname = lastname;
	}

	public Customer() {
	}

	public void add( Address address ) {
		Assert.notNull( address );
		this.addresses.add( address );
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

	public Set<Address> getAddresses() {
		return Collections.unmodifiableSet( addresses );
	}
}