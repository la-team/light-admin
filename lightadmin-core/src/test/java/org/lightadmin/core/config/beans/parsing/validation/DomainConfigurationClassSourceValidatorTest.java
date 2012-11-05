package org.lightadmin.core.config.beans.parsing.validation;

public class DomainConfigurationClassSourceValidatorTest {

	private DomainConfigurationClassSourceValidator subject;

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
	//		subject = new DomainConfigurationClassSourceValidator( propertyValidator );
	//
	//		final DomainConfigurationSource domainConfiguration = DummyConfigurationsHelper.emptyDomainEntityConfiguration();
	//
	//		subject.validateScreenContext( domainConfiguration, problemReporter );
	//
	//		EasyMock.verify( problemReporter );
	//
	//		assertEquals( domainConfiguration, problemCapture.getValue().getDomainConfiguration() );
	//		assertEquals( DomainConfigurationUnit.SCREEN_CONTEXT, problemCapture.getValue().getConfigurationUnit() );
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
	//		subject = new DomainConfigurationClassSourceValidator( propertyValidator );
	//
	//		final DomainConfigurationSource domainConfiguration = DummyConfigurationsHelper.emptyDomainEntityConfiguration();
	//
	//		subject.validateEntityConfiguration( domainConfiguration, problemReporter );
	//
	//		EasyMock.verify( problemReporter );
	//
	//		assertEquals( domainConfiguration, problemCapture.getValue().getDomainConfiguration() );
	//		assertEquals( DomainConfigurationUnit.CONFIGURATION, problemCapture.getValue().getConfigurationUnit() );
	//	}
}