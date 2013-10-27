package org.lightadmin.api.config.management.rmi;

public interface DataManipulationService {

    void truncateDatabase();

    void populateDatabase();

}