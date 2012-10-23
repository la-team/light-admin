package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.beans.parsing.ConfigurationUnitPropertyFilter;

public interface Fragment {

	Fragment filter( ConfigurationUnitPropertyFilter propertyFilter );
}