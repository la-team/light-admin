package org.lightadmin.core.config.beans.parsing;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.domain.filter.Filter;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.TableFragment;
import org.lightadmin.core.util.Pair;

import java.util.Iterator;

import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.*;

public class DomainConfigurationDecoratorTest {

	private DomainConfigurationDecorator subject;

	@Test
	public void entityMetadataAppliedToFilters() throws Exception {
		final DomainConfigurationInterface domainConfiguration = EasyMock.createMock( DomainConfigurationInterface.class );
		EasyMock.expect( domainConfiguration.getFilters() ).andReturn( filters( "field1", "field2" ) ).once();
		EasyMock.expect( domainConfiguration.getDomainTypeEntityMetadata() ).andReturn( domainTypeEntityMetadataMock( DomainEntity.class, "field1", "field2" ) ).once();
		EasyMock.replay( domainConfiguration );

		subject = new DomainConfigurationDecorator( domainConfiguration, alwaysTrueFilter() );

		final Filters filters = subject.getFilters();

		EasyMock.verify( domainConfiguration );

		for ( Filter filter : filters ) {
			assertNotNull( filter.getAttributeMetadata() );
			assertEquals( filter.getFieldName(), filter.getAttributeMetadata().getName() );
		}
	}

	@Test
	public void propertyFilterAppliedToFilters() throws Exception {
		final DomainConfigurationInterface domainConfiguration = EasyMock.createMock( DomainConfigurationInterface.class );
		EasyMock.expect( domainConfiguration.getFilters() ).andReturn( filters( "field1", "field2" ) ).once();
		EasyMock.expect( domainConfiguration.getDomainTypeEntityMetadata() ).andReturn( domainTypeEntityMetadataMock( DomainEntity.class ) ).once();
		EasyMock.replay( domainConfiguration );

		final ConfigurationUnitPropertyFilter propertyFilter = EasyMock.createMock( ConfigurationUnitPropertyFilter.class );
		EasyMock.expect( propertyFilter.apply( eq( "field1" ) ) ).andReturn( false ).once();
		EasyMock.expect( propertyFilter.apply( eq( "field2" ) ) ).andReturn( true ).once();
		EasyMock.replay( propertyFilter );

		subject = new DomainConfigurationDecorator( domainConfiguration, propertyFilter );

		final Filters filters = subject.getFilters();

		EasyMock.verify( domainConfiguration, propertyFilter );

		assertEquals( 1, filters.size() );
		assertEquals( "field2", filters.iterator().next().getFieldName() );
	}

	@Test
	public void propertyFilterAppliedToListView() {
		final DomainConfigurationInterface domainConfiguration = EasyMock.createMock( DomainConfigurationInterface.class );
		EasyMock.expect( domainConfiguration.getListViewFragment() ).andReturn( listView( "field1", "field2", "field3" ) ).once();
		EasyMock.replay( domainConfiguration );

		final ConfigurationUnitPropertyFilter propertyFilter = EasyMock.createMock( ConfigurationUnitPropertyFilter.class );
		EasyMock.expect( propertyFilter.apply( eq( "field1" ) ) ).andReturn( false ).once();
		EasyMock.expect( propertyFilter.apply( eq( "field2" ) ) ).andReturn( true ).once();
		EasyMock.expect( propertyFilter.apply( eq( "field3" ) ) ).andReturn( true ).once();
		EasyMock.replay( propertyFilter );

		subject = new DomainConfigurationDecorator( domainConfiguration, propertyFilter );

		final TableFragment listViewFragment = ( TableFragment ) subject.getListViewFragment();

		EasyMock.verify( domainConfiguration, propertyFilter );

		final Iterator<Pair<String, String>> columnsIterator = listViewFragment.getColumns().iterator();

		assertEquals( 2, listViewFragment.getColumns().size() );
		assertEquals( "field2", columnsIterator.next().getFirst() );
		assertEquals( "field3", columnsIterator.next().getFirst() );
	}
}