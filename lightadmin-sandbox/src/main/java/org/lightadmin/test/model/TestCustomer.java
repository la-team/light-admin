package org.lightadmin.test.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.lightadmin.demo.model.AbstractEntity;
import org.lightadmin.demo.model.EmailAddress;

import javax.persistence.*;
import java.util.Set;

@Entity
public class TestCustomer extends AbstractEntity {

    @Column(length = 64)
    @NotEmpty
    private String firstname;

    @Column(length = 64)
    @NotEmpty
    private String lastname;

    @Column(unique = true)
    @Embedded
    private EmailAddress emailAddress;

    @ManyToMany
    @JoinTable(name = "testcustomer_discount",
            joinColumns =
            @JoinColumn(name = "customer_id", referencedColumnName = "ID"),
            inverseJoinColumns =
            @JoinColumn(name = "discount_program_id", referencedColumnName = "ID")
    )
    private Set<TestDiscountProgram> discountPrograms;

    public TestCustomer() {
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<TestDiscountProgram> getDiscountPrograms() {
        return discountPrograms;
    }

    public void setDiscountPrograms(Set<TestDiscountProgram> discountPrograms) {
        this.discountPrograms = discountPrograms;
    }
}
