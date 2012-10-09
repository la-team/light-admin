package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.domain.Builder;

public interface ScreenContextBuilder extends Builder<ScreenContext> {

	ScreenContextBuilder screenName( String screenName );

	ScreenContextBuilder menuName( String menuName );
}