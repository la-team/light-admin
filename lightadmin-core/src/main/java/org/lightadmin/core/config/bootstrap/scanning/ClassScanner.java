package org.lightadmin.core.config.bootstrap.scanning;

import java.util.Set;

public interface ClassScanner {

	Set<Class> scan( String basePackage );

}