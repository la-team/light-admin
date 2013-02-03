package org.lightadmin.core.config.management.rmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute;

public class DataManipulationServiceImpl implements DataManipulationService {

	@Autowired
	private DataSource dataSource;

	@Override
	public void truncateDatabase() {
		execute( databasePopulator( "classpath:db/truncate.sql" ), this.dataSource );
	}

	@Override
	public void populateDatabase() {
		execute( databasePopulator( "classpath:db/data.sql" ), this.dataSource );
	}

	private ResourceDatabasePopulator databasePopulator( final String location ) {
		final ResourceLoader resourceLoader = new DefaultResourceLoader();
		final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript( resourceLoader.getResource( location ) );
		return databasePopulator;
	}
}