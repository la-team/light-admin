package org.lightadmin.core.config.beans.parsing.validation;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.beans.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationUnit;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.test.util.DummyConfigurationsHelper;

import static org.junit.Assert.assertEquals;

public class DomainConfigurationValidatorTest {

	private DomainConfigurationValidator subject;

	@Test
	public void screenContextNotDefinedError() throws Exception {
		final Capture<DomainConfigurationProblem> problemCapture = new Capture<DomainConfigurationProblem>();

		ProblemReporter problemReporter = EasyMock.createStrictMock( ProblemReporter.class );
		problemReporter.error( EasyMock.<DomainConfigurationProblem>capture( problemCapture ) );
		EasyMock.expectLastCall();

		EasyMock.replay( problemReporter );

		final DomainTypePropertyValidatorInterface propertyValidator = EasyMock.createMock( DomainTypePropertyValidatorInterface.class );

		subject = new DomainConfigurationValidator( propertyValidator );

		final DomainConfigurationInterface domainConfiguration = DummyConfigurationsHelper.emptyDomainEntityConfiguration();

		subject.validateScreenContext( domainConfiguration, problemReporter );

		EasyMock.verify( problemReporter );

		assertEquals( domainConfiguration, problemCapture.getValue().getDomainConfiguration() );
		assertEquals( DomainConfigurationUnit.SCREEN_CONTEXT, problemCapture.getValue().getConfigurationUnit() );
	}

	@Test
	public void entityConfigurationNotDefinedWarning() throws Exception {
		final Capture<DomainConfigurationProblem> problemCapture = new Capture<DomainConfigurationProblem>();

		ProblemReporter problemReporter = EasyMock.createStrictMock( ProblemReporter.class );
		problemReporter.warning( EasyMock.<DomainConfigurationProblem>capture( problemCapture ) );
		EasyMock.expectLastCall();

		EasyMock.replay( problemReporter );

		final DomainTypePropertyValidatorInterface propertyValidator = EasyMock.createMock( DomainTypePropertyValidatorInterface.class );

		subject = new DomainConfigurationValidator( propertyValidator );

		final DomainConfigurationInterface domainConfiguration = DummyConfigurationsHelper.emptyDomainEntityConfiguration();

		subject.validateEntityConfiguration( domainConfiguration, problemReporter );

		EasyMock.verify( problemReporter );

		assertEquals( domainConfiguration, problemCapture.getValue().getDomainConfiguration() );
		assertEquals( DomainConfigurationUnit.CONFIGURATION, problemCapture.getValue().getConfigurationUnit() );
	}
}