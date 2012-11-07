package org.lightadmin.core.config.bootstrap.parsing.validation;

public class DomainConfigurationUnitsSourceValidatorTest {

	private DomainConfigurationUnitsSourceValidator subject;

	//	@Test
	//	public void screenContextNotDefinedError() throws Exception {
	//		final Capture<DomainConfigurationProblem> problemCapture = new Capture<DomainConfigurationProblem>();
	//
	//		ProblemReporter problemReporter = EasyMock.createStrictMock( ProblemReporter.class );
	//		problemReporter.error( EasyMock.<DomainConfigurationProblem>capture( problemCapture ) );
	//		EasyMock.expectLastCall();
	//
	//		EasyMock.replay( problemReporter );
	//
	//		final DomainTypePropertyValidator propertyValidator = EasyMock.createMock( DomainTypePropertyValidator.class );
	//
	//		subject = new DomainConfigurationUnitsSourceValidator( propertyValidator );
	//
	//		final DomainConfigurationSource domainConfiguration = DummyConfigurationsHelper.emptyDomainEntityConfiguration();
	//
	//		subject.validateScreenContext( domainConfiguration, problemReporter );
	//
	//		EasyMock.verify( problemReporter );
	//
	//		assertEquals( domainConfiguration, problemCapture.getValue().getDomainConfiguration() );
	//		assertEquals( DomainConfigurationUnitType.SCREEN_CONTEXT, problemCapture.getValue().getConfigurationUnitType() );
	//	}
	//
	//	@Test
	//	public void entityConfigurationNotDefinedWarning() throws Exception {
	//		final Capture<DomainConfigurationProblem> problemCapture = new Capture<DomainConfigurationProblem>();
	//
	//		ProblemReporter problemReporter = EasyMock.createStrictMock( ProblemReporter.class );
	//		problemReporter.warning( EasyMock.<DomainConfigurationProblem>capture( problemCapture ) );
	//		EasyMock.expectLastCall();
	//
	//		EasyMock.replay( problemReporter );
	//
	//		final DomainTypePropertyValidator propertyValidator = EasyMock.createMock( DomainTypePropertyValidator.class );
	//
	//		subject = new DomainConfigurationUnitsSourceValidator( propertyValidator );
	//
	//		final DomainConfigurationSource domainConfiguration = DummyConfigurationsHelper.emptyDomainEntityConfiguration();
	//
	//		subject.validateEntityConfiguration( domainConfiguration, problemReporter );
	//
	//		EasyMock.verify( problemReporter );
	//
	//		assertEquals( domainConfiguration, problemCapture.getValue().getDomainConfiguration() );
	//		assertEquals( DomainConfigurationUnitType.CONFIGURATION, problemCapture.getValue().getConfigurationUnitType() );
	//	}
}