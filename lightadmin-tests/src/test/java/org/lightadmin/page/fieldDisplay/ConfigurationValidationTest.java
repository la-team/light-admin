package org.lightadmin.page.fieldDisplay;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.*;

import static org.hamcrest.CoreMatchers.containsString;


public class ConfigurationValidationTest extends SeleniumIntegrationTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	//Covers LA-72 (https://github.com/max-dev/light-admin/issues/72)
	@Test
	public void missingFieldIsValidated() {

		ConfigurationProblemExceptionTrigger exceptionTrigger = new ConfigurationProblemExceptionTrigger()
				.setAdministration( MissingFieldAdmin.class )
				.setViewName( "filters" )
				.setExpectedProblem( "Missing property 'Missing Field' defined!" );

		expectedException.expectMessage( exceptionTrigger.getExpectedMessageMatcher() );

		exceptionTrigger.triggerException();
	}

	//Covers LA-2 comment: https://github.com/max-dev/light-admin/issues/2#issuecomment-12477966
    @Test
	public void transientFieldOnFormViewIsValidated() {

		ConfigurationProblemExceptionTrigger exceptionTrigger = new ConfigurationProblemExceptionTrigger()
				.setAdministration( TransientFieldOnFormViewConfiguration.class )
				.setViewName( "formView" )
				.setExpectedProblem( "Missing property 'Order Total' defined!" );

		expectedException.expectMessage( exceptionTrigger.getExpectedMessageMatcher() );

		exceptionTrigger.triggerException();
	}

	//Covers LA-89 (https://github.com/max-dev/light-admin/issues/89)
	@Test
	public void unsupportedFieldTypeIsValidated() {

		ConfigurationProblemExceptionTrigger exceptionTrigger = new ConfigurationProblemExceptionTrigger()
				.setAdministration( UnsupportedFieldTypeAdministration.class )
				.setViewName( "quickView" )
				.setExpectedProblem( "Persistent property 'Field Of Unsupported Type' has not supported type!" );

		expectedException.expectMessage( exceptionTrigger.getExpectedMessageMatcher() );

		exceptionTrigger.triggerException();
	}

    @Ignore("Test is outdated: Problem level was changed from 'ERROR' to 'WARNING' and is written to log instead of throwing exception")
	//Covers LA-89 (https://github.com/max-dev/light-admin/issues/89)
	@Test
	public void missingFileReferenceDirectoryIsValidated() {

		ConfigurationProblemExceptionTrigger exceptionTrigger = new ConfigurationProblemExceptionTrigger()
				.setAdministration( MissingBaseDirectoryAdministration.class )
				.setViewName( "listView" )
				.setExpectedProblem( "@FileReference property 'FileReference' has incorrect baseDirectory defined!" );

		expectedException.expectMessage( exceptionTrigger.getExpectedMessageMatcher() );

		exceptionTrigger.triggerException();
	}

	private class ConfigurationProblemExceptionTrigger {

		private Class<?> administration;
		private String viewName;
		private String expectedProblem;

		public ConfigurationProblemExceptionTrigger setAdministration( Class<?> administration ) {
			this.administration = administration;
			return this;
		}

		public ConfigurationProblemExceptionTrigger setViewName( String viewName ) {
			this.viewName = viewName;
			return this;
		}

		public ConfigurationProblemExceptionTrigger setExpectedProblem( String expectedProblem ) {
			this.expectedProblem = expectedProblem;
			return this;
		}

		public CombinableMatcher<String> getExpectedMessageMatcher() {
			return CoreMatchers.<String>
					both( containsString( "Domain Configuration" ) ).and( containsString( administration.getSimpleName() ) )
					.and( containsString( "Unit" ) ).and( containsString( viewName ) )
					.and( containsString( expectedProblem ) );
		}

		public void triggerException() {
			registerDomainTypeAdministrationConfiguration( administration );
		}
	}
}
