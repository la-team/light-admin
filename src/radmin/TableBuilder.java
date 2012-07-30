package radmin;

import com.google.common.base.Function;

public class TableBuilder {

	private Table table;

	public TableBuilder field( String fieldName ) {
		table.fieldRow( fieldName );
		return this;
	}

	public TableBuilder field( String fieldName, Function<Entity, String> function ) {
		table.fieldRow( fieldName, function );
		return this;
	}

	public Fragment fragment() {
		return null;
	}

	public TableBuilder renderer( final Renderer renderer ) {
		return null;
	}
}