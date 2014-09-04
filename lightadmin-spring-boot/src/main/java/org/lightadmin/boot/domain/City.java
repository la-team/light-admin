package org.lightadmin.boot.domain;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String state;

    @Column
    @NotBlank
    private String country;

    @Column
    @NotBlank
    private String map;

    public City() {
    }

    public City(String name, String country) {
        super();
        this.name = name;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getState() {
        return this.state;
    }

    public String getCountry() {
        return this.country;
    }

    public String getMap() {
        return this.map;
    }

    @Override
    public String toString() {
        return getName() + "," + getState() + "," + getCountry();
    }
}