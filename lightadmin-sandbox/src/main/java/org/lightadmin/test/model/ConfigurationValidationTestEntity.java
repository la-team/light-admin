package org.lightadmin.test.model;

import org.joda.time.Hours;
import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class ConfigurationValidationTestEntity extends AbstractEntity {

	private String name;

	private Hours unsupportedType;
}