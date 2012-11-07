package org.lightadmin.core.config.bootstrap.parsing.configuration;

public class DomainConfigurationClassSourceTest {

//	private DomainConfigurationClassSource subject;
//
//	@Test( expected = IllegalArgumentException.class )
//	public void nullDomainTypeEntityMetadataNotAllowed() {
//		subject = new DomainConfigurationClassSource( null, DomainEntityEmptyConfiguration.class );
//	}
//
//	@Test( expected = IllegalArgumentException.class )
//	public void nullConfigurationClassNotAllowed() {
//		subject = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), null );
//	}
//
//	@Test
//	public void correctDomainTypeReturned() {
//		subject = emptyDomainEntityConfiguration();
//
//		assertEquals( DomainEntity.class, subject.getDomainType() );
//	}
//
//	@Test
//	public void correctEntityMetadataReturned() throws Exception {
//		final DomainTypeEntityMetadata expectedMetadata = domainTypeEntityMetadataMock( DomainEntity.class );
//
//		subject = new DomainConfigurationClassSource( expectedMetadata, DomainEntityEmptyConfiguration.class );
//
//		assertEquals( expectedMetadata, subject.getDomainTypeEntityMetadata() );
//	}
//
//	@Test
//	public void equalDomainConfigurations() throws Exception {
//		final DomainConfigurationClassSource domainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
//		final DomainConfigurationClassSource otherDomainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
//
//		assertEquals( domainConfigurationClassSource, otherDomainConfigurationClassSource );
//	}
//
//	@Test
//	public void notEqualDomainConfigurations() throws Exception {
//		final DomainConfigurationClassSource domainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( Object.class ), DomainEntityEmptyConfiguration.class );
//		final DomainConfigurationClassSource otherDomainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
//
//		assertFalse( domainConfigurationClassSource.equals( otherDomainConfigurationClassSource ) );
//	}
//
//	public static DomainConfigurationClassSource emptyDomainEntityConfiguration() {
//		return new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
//	}
//
//	public static DomainConfigurationClassSource domainEntityConfigurationWithException() {
//		return new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), ConfigurationWithException.class );
//	}
}