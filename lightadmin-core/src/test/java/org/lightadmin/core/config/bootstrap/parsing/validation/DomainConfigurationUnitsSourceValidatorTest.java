package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.apache.commons.lang.StringUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.test.util.DomainTypeEntityMetadataUtils;

import java.util.List;

import static org.junit.Assert.fail;

public class DomainConfigurationUnitsSourceValidatorTest {

	private DomainConfigurationUnitsSourceValidator testee;

	@Test
	public void domainTypeWithoutConstructorFailure() throws Exception {
		testee = new DomainConfigurationUnitsSourceValidator( DomainTypeEntityMetadataUtils.entityMetadataResolver( DomainType.class ), fieldMetadataValidatorMock() );

		final Capture<DomainConfigurationProblem> problemCapture = configurationProblemCapture();

		testee.validateDomainType( domainConfigurationSourceMock( DomainType.class ), problemReporter( problemCapture ) );

		final List<DomainConfigurationProblem> domainConfigurationProblems = problemCapture.getValues();

		assertValidationMessagePresent( "Domain Configuration \"DomainTypeConfiguraiton\": Type DomainType must have default constructor.", domainConfigurationProblems );
	}

	@Test
	public void domainTypeIsNotPersistentFailure() throws Exception {
		final DomainTypeEntityMetadataResolver entityMetadataResolver = EasyMock.createMock( DomainTypeEntityMetadataResolver.class );
		EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( DomainType.class ) ).andReturn( null ).once();
		EasyMock.replay( entityMetadataResolver );

		testee = new DomainConfigurationUnitsSourceValidator( entityMetadataResolver, fieldMetadataValidatorMock() );

		final Capture<DomainConfigurationProblem> problemCapture = configurationProblemCapture();

		testee.validateDomainType( domainConfigurationSourceMock( DomainType.class ), problemReporter( problemCapture ) );

		final List<DomainConfigurationProblem> domainConfigurationProblems = problemCapture.getValues();

		assertValidationMessagePresent( "Domain Configuration \"DomainTypeConfiguraiton\": Non-persistent type DomainType is not supported.", domainConfigurationProblems );
	}

	private Capture<DomainConfigurationProblem> configurationProblemCapture() {
		return new Capture<DomainConfigurationProblem>( CaptureType.ALL );
	}

	private void assertValidationMessagePresent( final String validationMessage, final List<DomainConfigurationProblem> configurationProblems ) {
		for ( DomainConfigurationProblem configurationProblem : configurationProblems ) {
			if ( StringUtils.equals( validationMessage, configurationProblem.getMessage() ) ) {
				return;
			}
		}
		fail();
	}

	private ProblemReporter problemReporter( Capture<DomainConfigurationProblem> problemCapture ) {
		ProblemReporter problemReporter = EasyMock.createStrictMock( ProblemReporter.class );
		problemReporter.error( EasyMock.<DomainConfigurationProblem>capture( problemCapture ) );
		EasyMock.expectLastCall().anyTimes();

		EasyMock.replay( problemReporter );

		return problemReporter;
	}

	@SuppressWarnings( "unchecked" )
	private FieldMetadataValidator<FieldMetadata> fieldMetadataValidatorMock() {
		final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator = EasyMock.createMock( FieldMetadataValidator.class );
		EasyMock.expect( fieldMetadataValidator.isValidFieldMetadata( EasyMock.<FieldMetadata>anyObject(), EasyMock.<Class>anyObject() ) ).andReturn( true ).anyTimes();
		EasyMock.replay( fieldMetadataValidator );
		return fieldMetadataValidator;
	}

	@SuppressWarnings( "unchecked" )
	private DomainConfigurationSource domainConfigurationSourceMock( Class domainType ) {
		DomainConfigurationSource domainConfigurationSource = EasyMock.createMock( DomainConfigurationSource.class );
		EasyMock.expect( domainConfigurationSource.getDomainType() ).andReturn( domainType ).anyTimes();
		EasyMock.expect( domainConfigurationSource.getConfigurationName() ).andReturn( "DomainTypeConfiguraiton" ).anyTimes();
		EasyMock.replay( domainConfigurationSource );
		return domainConfigurationSource;
	}

	private static class DomainType {

	}
}