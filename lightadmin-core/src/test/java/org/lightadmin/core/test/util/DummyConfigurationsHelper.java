package org.lightadmin.core.test.util;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;

public class DummyConfigurationsHelper {

	public static class DomainEntity {
	}

	@SuppressWarnings( "unused" )
	@Administration( DomainEntity.class )
	public static class ConfigurationWithException {
		public static EntityConfiguration configuration( EntityConfigurationBuilder configurationBuilder ) {
			throw new RuntimeException(  );
		}
	}

	public static class WrongConfiguration {
	}

	@Administration( DomainEntity.class )
	public static class DomainEntityEmptyConfiguration {
	}

}