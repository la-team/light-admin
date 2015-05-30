/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.util;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;

public class NamingUtils {

    @SuppressWarnings("unchecked")
    public static String entityName(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, Object entity) {
        final EntityNameExtractor<Object> nameExtractor = domainTypeAdministrationConfiguration.getEntityConfiguration().getNameExtractor();

        return exceptionAwareNameExtractor(nameExtractor, domainTypeAdministrationConfiguration).apply(entity);
    }

    public static String entityId(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, Object entity) {
        if (entity == null) {
            return null;
        }

        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();
        PersistentProperty idProperty = persistentEntity.getIdProperty();

        BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);

        return String.valueOf(beanWrapper.getPropertyValue(idProperty.getName()));
    }

    public static String cutLongText(String text) {
        text = trim(text);
        //		if ( text.length() > 50 ) {
        //			return substring( text, 0, 47 ) + "...";
        //		}
        return text;
    }
}