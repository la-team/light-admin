package org.lightadmin.core.config.management.rmi;

public interface DataManipulationService {

	void truncateDatabase();

	void populateDatabase();

}