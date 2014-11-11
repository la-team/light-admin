package org.lightadmin.test.model;

import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class EntityWithInvalidFileReference extends AbstractEntity {

	@FileReference( baseDirectory = "wrongDirectory" )
	private String fileReference;

}
