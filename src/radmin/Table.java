package radmin;

import com.google.common.base.Function;

public interface Table {

	Table fieldRow( String fieldName );

	Table fieldRow( String fieldName, Function<Entity, String> function );

	Fragment fragment();
}