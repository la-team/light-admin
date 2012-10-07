package org.lightadmin.core.view.support.context;

import org.lightadmin.core.view.support.Builder;

public interface ScreenContextBuilder extends Builder<ScreenContext> {

	ScreenContextBuilder screenName( String screenName );

	ScreenContextBuilder menuName( String menuName );
}