package org.lightadmin.core.web.util;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.BeanWrapper;

import javax.servlet.http.HttpServletRequest;

import static java.lang.String.valueOf;
import static org.lightadmin.core.web.util.WebContextUtils.getCurrentRequest;
import static org.lightadmin.core.web.util.WebContextUtils.globalAdministrationConfiguration;

public final class ApplicationUrlResolver {

    private ApplicationUrlResolver() {
    }

    public static String domainBaseUrl(DomainTypeAdministrationConfiguration configuration) {
        return "/domain/" + configuration.getDomainTypeName();
    }

    public static DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration(Class<?> type) {
        return globalAdministrationConfiguration(getCurrentRequest().getServletContext()).forManagedDomainType(type);
    }

    public static String domainRestBaseUrl(DomainTypeAdministrationConfiguration configuration) {
        return "/rest/" + configuration.getDomainTypeName();
    }

    public static String domainRestEntityBaseUrl(DomainTypeAdministrationConfiguration configuration, Object id) {
        return domainRestBaseUrl(configuration) + "/" + id;
    }

    public static String domainRestScopeBaseUrl(DomainTypeAdministrationConfiguration configuration) {
        return "/rest/" + configuration.getDomainTypeName() + "/scope";
    }

    public static String filePropertyRestUrl(Object entity, String property) {
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeAdministrationConfiguration(getCurrentRequest(), entity);
        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        String idValue = valueOf(idAttributeValue(entity, persistentEntity));

        return domainRestEntityBaseUrl(domainTypeAdministrationConfiguration, idValue) + "/" + property + "/file";
    }

    private static DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration(HttpServletRequest currentRequest, Object entity) {
        return globalAdministrationConfiguration(currentRequest.getServletContext()).forManagedDomainType(entity.getClass());
    }

    private static Object idAttributeValue(Object entity, PersistentEntity persistentProperty) {
        return BeanWrapper.create(entity, null).getProperty(persistentProperty.getIdProperty());
    }
}