package org.lightadmin.boot.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private City city;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column
    private String address;

    @NotBlank
    @Column
    private String zip;

    public Hotel() {
    }

    public Hotel(City city, String name) {
        this.city = city;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public City getCity() {
        return this.city;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getZip() {
        return this.zip;
    }
}