package org.lightadmin.core.util;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.BeanWrapper;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;

public class NamingUtils {

    @SuppressWarnings("unchecked")
    public static String entityName(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, Object entity) {
        final EntityNameExtractor nameExtractor = domainTypeAdministrationConfiguration.getEntityConfiguration().getNameExtractor();

        return exceptionAwareNameExtractor(nameExtractor, domainTypeAdministrationConfiguration).apply(entity);
    }

    public static String entityId(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, Object entity) {
        if (entity == null) {
            return null;
        }

        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();
        PersistentProperty idProperty = persistentEntity.getIdProperty();

        return String.valueOf(BeanWrapper.create(entity, null).getProperty(idProperty));
    }

    public static String cutLongText(String text) {
        text = trim(text);
        //		if ( text.length() > 50 ) {
        //			return substring( text, 0, 47 ) + "...";
        //		}
        return text;
    }
}