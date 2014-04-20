package org.lightadmin.core.view.renderer;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.web.util.ApplicationUrlResolver;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.Writer;

import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;

public class DomainTypeEntityAttributeRenderer extends AbstractAttributeRenderer {

    private final PersistentEntity domainTypeEntityMetadata;

    public DomainTypeEntityAttributeRenderer(final PersistentEntity domainTypeEntityMetadata) {
        this.domainTypeEntityMetadata = domainTypeEntityMetadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void write(final Object value, final Writer writer) throws IOException {
        final DomainTypeAdministrationConfiguration domainTypeConfiguration = domainTypeConfiguration(domainTypeEntityMetadata.getType());

        final String entityViewUrl = entityShowUrl(value, domainTypeConfiguration);

        final EntityNameExtractor<Object> nameExtractor = domainTypeConfiguration.getEntityConfiguration().getNameExtractor();

        final String stringValue = exceptionAwareNameExtractor(nameExtractor, domainTypeConfiguration).apply(value);

        writer.write(String.format("<a href=\"%s\">%s</a>", entityViewUrl, stringValue));
    }

    private String entityShowUrl(final Object value, final DomainTypeAdministrationConfiguration domainTypeConfiguration) {
        final String domainBaseUrl = ApplicationUrlResolver.domainBaseUrl(domainTypeConfiguration);

        final Object entityId = BeanWrapper.create(value, null).getProperty(domainTypeEntityMetadata.getIdProperty());

        // TODO: max: Consider rewriting using PageContext
        final ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
        servletUriComponentsBuilder.path(domainBaseUrl + "/" + entityId);

        return servletUriComponentsBuilder.build().getPath();
    }
}