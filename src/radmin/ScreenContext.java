package radmin;

import java.util.concurrent.Callable;
public interface ScreenContext {

	ScreenContext screenName( String screenName );

	ScreenContext menuName( String menuName );

	ScreenContext displayInMenu( Callable<Boolean> callable );
}