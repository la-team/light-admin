package org.lightadmin.core.test.util;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.beans.parsing.DslConfigurationClass;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;

public class DummyConfigurationsHelper {

	public static DslConfigurationClass emptyDomainEntityConfiguration() {
		return new DslConfigurationClass( DomainEntity.class, DomainEntityEmptyConfiguration.class );
	}

	public static DslConfigurationClass domainEntityConfigurationWithException() {
		return new DslConfigurationClass( DomainEntity.class, ConfigurationWithException.class );
	}

	public static class DomainEntity {

	}

	@SuppressWarnings( "unused" )
	@Administration( DomainEntity.class )
	public static class ConfigurationWithException {

		public static EntityConfiguration configuration( EntityConfigurationBuilder configurationBuilder ) {
			throw new RuntimeException();
		}
	}

	@Administration( DomainEntity.class )
	public static class DomainEntityEmptyConfiguration {

	}

}