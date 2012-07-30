package radmin;

import com.google.common.base.Predicate;

public interface FiltersRegistry {

	FiltersRegistry add( final Predicate filter );
}