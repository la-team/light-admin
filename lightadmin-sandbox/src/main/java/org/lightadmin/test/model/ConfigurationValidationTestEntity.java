package org.lightadmin.test.model;

import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ConfigurationValidationTestEntity extends AbstractEntity {

	private String name;

	@Enumerated( EnumType.STRING )
	private DummyEnum theEnum;

	private enum DummyEnum {
		FIRST, SECOND, THIRD
	}
}
