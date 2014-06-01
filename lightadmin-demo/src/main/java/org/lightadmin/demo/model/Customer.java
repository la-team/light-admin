package org.lightadmin.demo.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Customer extends AbstractEntity {

	@Column(length = 64)
	@NotEmpty
	private String firstname;

	@Column(length = 64)
	@NotEmpty
	private String lastname;

    @Embedded
    @Column(unique = true)
	private EmailAddress emailAddress;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_id")
	private Set<Address> addresses;

	@ManyToMany
	@JoinTable(name = "customer_discount",
			   joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "ID"),
			   inverseJoinColumns = @JoinColumn(name = "discount_program_id", referencedColumnName = "ID")
	)
	private Set<DiscountProgram> discountPrograms;

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

	public void setFirstname( final String firstname ) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname( final String lastname ) {
		this.lastname = lastname;
	}

	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress( final EmailAddress emailAddress ) {
		this.emailAddress = emailAddress;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses( final Set<Address> addresses ) {
		this.addresses = addresses;
	}

	public Set<DiscountProgram> getDiscountPrograms() {
		return discountPrograms;
	}

	public void setDiscountPrograms( final Set<DiscountProgram> discountPrograms ) {
		this.discountPrograms = discountPrograms;
	}
}