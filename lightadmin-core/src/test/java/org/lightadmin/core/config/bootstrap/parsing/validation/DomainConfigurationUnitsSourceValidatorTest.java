package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.apache.commons.lang.StringUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.fail;

@Ignore
@SuppressWarnings("unchecked")
public class DomainConfigurationUnitsSourceValidatorTest {

    private DomainConfigurationUnitsSourceValidator testee;

    @Test
    public void domainTypeWithoutConstructorFailure() {
//        testee = new DomainConfigurationUnitsSourceValidator(alwaysValidFieldMetadataValidatorMock(), createNiceMock(ResourceLoader.class));

        final Capture<DomainConfigurationProblem> problemCapture = configurationProblemCapture();

        testee.validateDomainType(domainConfigurationSourceMock(DomainType.class), problemReporter(problemCapture));

        final List<DomainConfigurationProblem> domainConfigurationProblems = problemCapture.getValues();

        assertValidationMessagePresent("Domain Configuration \"DomainTypeConfiguraiton\": Type DomainType must have default constructor.", domainConfigurationProblems);
    }

    @Test
    public void invalidFieldDefinedForFiltersProblemReported() throws Exception {
//        testee = new DomainConfigurationUnitsSourceValidator(alwaysInvalidFieldMetadataValidator(), createNiceMock(ResourceLoader.class));

        final Capture<DomainConfigurationProblem> problemCapture = configurationProblemCapture();

        testee.validateFilters(domainConfigurationSourceMock(DomainType.class), problemReporter(problemCapture));

        final List<DomainConfigurationProblem> domainConfigurationProblems = problemCapture.getValues();

        assertValidationMessagePresent("Domain Configuration \"DomainTypeConfiguraiton\": Unit \"filters\": Invalid property/path 'Field' defined!", domainConfigurationProblems);
    }

    private FieldMetadataValidator<FieldMetadata> alwaysInvalidFieldMetadataValidator() {
        final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator = EasyMock.createMock(FieldMetadataValidator.class);
//        EasyMock.expect(fieldMetadataValidator.isValidFieldMetadata(EasyMock.<FieldMetadata>anyObject(), EasyMock.<Class>anyObject())).andReturn(false).anyTimes();
        EasyMock.replay(fieldMetadataValidator);
        return fieldMetadataValidator;
    }

    private Capture<DomainConfigurationProblem> configurationProblemCapture() {
        return new Capture<DomainConfigurationProblem>(CaptureType.ALL);
    }

    private void assertValidationMessagePresent(final String validationMessage, final List<DomainConfigurationProblem> configurationProblems) {
        for (DomainConfigurationProblem configurationProblem : configurationProblems) {
            if (StringUtils.equals(validationMessage, configurationProblem.getMessage())) {
                return;
            }
        }
        fail();
    }

    private ProblemReporter problemReporter(Capture<DomainConfigurationProblem> problemCapture) {
        ProblemReporter problemReporter = EasyMock.createStrictMock(ProblemReporter.class);
        problemReporter.error(EasyMock.<DomainConfigurationProblem>capture(problemCapture));
        EasyMock.expectLastCall().anyTimes();

        EasyMock.replay(problemReporter);

        return problemReporter;
    }

    @SuppressWarnings("unchecked")
    private FieldMetadataValidator<FieldMetadata> alwaysValidFieldMetadataValidatorMock() {
        final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator = EasyMock.createMock(FieldMetadataValidator.class);
//        EasyMock.expect(fieldMetadataValidator.validateFieldMetadata(EasyMock.<FieldMetadata>anyObject(), EasyMock.<Class>anyObject(), EasyMock.<DomainConfigurationValidationContext>anyObject())).andReturn(Collections.<DomainConfigurationProblem>emptyList()).anyTimes();
        EasyMock.replay(fieldMetadataValidator);
        return fieldMetadataValidator;
    }

    @SuppressWarnings("unchecked")
    private DomainConfigurationSource domainConfigurationSourceMock(Class domainType) {
        DomainConfigurationSource domainConfigurationSource = EasyMock.createMock(DomainConfigurationSource.class);
        EasyMock.expect(domainConfigurationSource.getDomainType()).andReturn(domainType).anyTimes();
        EasyMock.expect(domainConfigurationSource.getConfigurationName()).andReturn("DomainTypeConfiguraiton").anyTimes();

        EasyMock.expect(domainConfigurationSource.getFilters()).andReturn(filterConfigurationUnitMock()).anyTimes();
        EasyMock.replay(domainConfigurationSource);
        return domainConfigurationSource;
    }

    private FiltersConfigurationUnit filterConfigurationUnitMock() {
        final FieldMetadata fieldMetadata = EasyMock.createMock(FieldMetadata.class);
        EasyMock.expect(fieldMetadata.getName()).andReturn("Field").anyTimes();
        EasyMock.replay(fieldMetadata);

        FilterMetadata filterMetadata = EasyMock.createMock(FilterMetadata.class);
        EasyMock.expect(filterMetadata.getFieldMetadata()).andReturn(fieldMetadata).anyTimes();
        EasyMock.replay(filterMetadata);

        final Iterator<FilterMetadata> iterator = newArrayList(filterMetadata).iterator();
        final FiltersConfigurationUnit filtersConfigurationUnit = EasyMock.createMock(FiltersConfigurationUnit.class);
        EasyMock.expect(filtersConfigurationUnit.iterator()).andReturn(iterator).anyTimes();
        EasyMock.replay(filtersConfigurationUnit);
        return filtersConfigurationUnit;
    }

    private static class DomainType {

    }
}