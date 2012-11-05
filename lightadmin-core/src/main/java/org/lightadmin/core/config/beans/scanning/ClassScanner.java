package org.lightadmin.core.config.beans.scanning;

import java.util.Set;

public interface ClassScanner {

	Set<Class> scan( String basePackage );

}