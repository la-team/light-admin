package org.lightadmin.core.config.beans.scanning;

import java.util.Set;

public interface ClassProvider {

	Set<Class> findClassCandidates( Set<Class> classes );

	Set<Class> findClassCandidates( String basePackage );

}