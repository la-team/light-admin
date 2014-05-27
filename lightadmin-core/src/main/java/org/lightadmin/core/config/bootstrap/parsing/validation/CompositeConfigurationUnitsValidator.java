package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.api.config.unit.SidebarsConfigurationUnit;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.config.domain.sidebar.SidebarMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.reporting.ProblemReporter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mapping.context.MappingContext;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.*;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.*;
import static org.springframework.util.ClassUtils.hasConstructor;

public class CompositeConfigurationUnitsValidator implements ConfigurationUnitsValidator<ConfigurationUnits> {

    private final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator;

    private final LightAdminConfiguration lightAdminConfiguration;
    private final MappingContext mappingContext;
    private final ResourceLoader resourceLoader;

    public CompositeConfigurationUnitsValidator(final MappingContext mappingContext, ResourceLoader resourceLoader, LightAdminConfiguration lightAdminConfiguration) {
        this(new DomainTypeFieldMetadataValidator(), lightAdminConfiguration, mappingContext, resourceLoader);
    }

    CompositeConfigurationUnitsValidator(final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator, LightAdminConfiguration lightAdminConfiguration, MappingContext mappingContext, ResourceLoader resourceLoader) {
        this.fieldMetadataValidator = fieldMetadataValidator;
        this.resourceLoader = resourceLoader;
        this.lightAdminConfiguration = lightAdminConfiguration;
        this.mappingContext = mappingContext;
    }

    @Override
    public void validate(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        validateDomainType(configurationUnits, problemReporter);

        validateFilters(configurationUnits, problemReporter);

        validateListView(configurationUnits, problemReporter);

        validateShowView(configurationUnits, problemReporter);

        validateQuickView(configurationUnits, problemReporter);

        validateFormView(configurationUnits, problemReporter);

        validateScopes(configurationUnits, problemReporter);

        validateSidebars(configurationUnits, problemReporter);
    }

    void validateDomainType(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final Class<?> domainType = configurationUnits.getDomainType();

        if (!hasConstructor(domainType)) {
            problemReporter.error(new DomainConfigurationProblem(configurationUnits, format("Type %s must have default constructor.", domainType.getSimpleName())));
        }
    }

    void validateSidebars(ConfigurationUnits configurationUnits, ProblemReporter problemReporter) {
        final SidebarsConfigurationUnit sidebarsConfigurationUnit = configurationUnits.getSidebars();
        final List<SidebarMetadata> sidebars = sidebarsConfigurationUnit.getSidebars();

        for (SidebarMetadata sidebar : sidebars) {
            final String jspFilePath = sidebar.getJspFilePath();
            if (!resourceLoader.getResource(jspFilePath).exists()) {
                problemReporter.error(new DomainConfigurationProblem(configurationUnits, SIDEBARS, "Wrong jsp file path defined for sidebar " + jspFilePath));
            }
        }
    }

    void validateScopes(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final ScopesConfigurationUnit scopes = configurationUnits.getScopes();

        for (ScopeMetadata scope : scopes) {
            if (isPredicateScope(scope)) {
                validatePredicateScope(scope, configurationUnits, problemReporter);
            }

            if (isSpecificationScope(scope)) {
                validateSpecificationScope(scope, configurationUnits, problemReporter);
            }
        }
    }

    void validateSpecificationScope(final ScopeMetadata scope, final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        SpecificationScopeMetadata specificationScopeMetadata = (SpecificationScopeMetadata) scope;
        if (specificationScopeMetadata.specification() == null) {
            problemReporter.error(new DomainConfigurationProblem(configurationUnits, SCOPES, "Filtering specification not defined for scope " + scope.getName()));
        }
    }

    void validatePredicateScope(final ScopeMetadata scope, final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        PredicateScopeMetadata predicateScopeMetadata = (PredicateScopeMetadata) scope;
        if (predicateScopeMetadata.predicate() == null) {
            problemReporter.error(new DomainConfigurationProblem(configurationUnits, SCOPES, "Filtering predicate not defined for scope " + scope.getName()));
        }
    }

    void validateFormView(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final FieldSetConfigurationUnit formViewFieldSet = configurationUnits.getFormViewConfigurationUnit();

        validateFields(formViewFieldSet.getFields(), configurationUnits, formViewFieldSet.getDomainConfigurationUnitType(), problemReporter);
    }

    void validateQuickView(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final FieldSetConfigurationUnit quickViewFieldSet = configurationUnits.getQuickViewConfigurationUnit();

        validateFields(quickViewFieldSet.getFields(), configurationUnits, quickViewFieldSet.getDomainConfigurationUnitType(), problemReporter);
    }

    void validateShowView(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final FieldSetConfigurationUnit showViewFieldSet = configurationUnits.getShowViewConfigurationUnit();

        validateFields(showViewFieldSet.getFields(), configurationUnits, showViewFieldSet.getDomainConfigurationUnitType(), problemReporter);
    }

    void validateListView(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final FieldSetConfigurationUnit listViewFieldSet = configurationUnits.getListViewConfigurationUnit();

        validateFields(listViewFieldSet.getFields(), configurationUnits, listViewFieldSet.getDomainConfigurationUnitType(), problemReporter);
    }

    void validateFilters(final ConfigurationUnits configurationUnits, final ProblemReporter problemReporter) {
        final Set<FieldMetadata> fields = FieldMetadataUtils.extractFields(configurationUnits.getFilters());

        validateFields(fields, configurationUnits, FILTERS, problemReporter);
    }

    private void validateFields(Set<FieldMetadata> fields, ConfigurationUnits configurationUnits, final DomainConfigurationUnitType configurationUnitType, ProblemReporter problemReporter) {
        for (FieldMetadata field : fields) {
            final Collection<? extends DomainConfigurationProblem> problems = fieldMetadataValidator.validateFieldMetadata(field, configurationUnits.getDomainType(), newDomainConfigurationValidationContext(configurationUnits, configurationUnitType));
            if (!problems.isEmpty()) {
                problemReporter.errors(problems);
            }
        }
    }

    private DomainConfigurationValidationContext newDomainConfigurationValidationContext(ConfigurationUnits configurationUnits, DomainConfigurationUnitType domainConfigurationUnitType) {
        return new DomainConfigurationValidationContext(lightAdminConfiguration, configurationUnits, domainConfigurationUnitType, mappingContext, resourceLoader);
    }
}