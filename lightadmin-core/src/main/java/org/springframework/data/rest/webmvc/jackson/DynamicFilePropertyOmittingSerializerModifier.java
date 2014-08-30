package org.springframework.data.rest.webmvc.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.FILE;

public class DynamicFilePropertyOmittingSerializerModifier extends BeanSerializerModifier {

    private final GlobalAdministrationConfiguration configuration;

    public DynamicFilePropertyOmittingSerializerModifier(GlobalAdministrationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public BeanSerializerBuilder updateBuilder(SerializationConfig config, BeanDescription beanDesc, BeanSerializerBuilder builder) {
        if (!configuration.isManagedDomainType(beanDesc.getBeanClass())) {
            return builder;
        }

        PersistentEntity<?, ?> entity = configuration.forManagedDomainType(beanDesc.getBeanClass()).getPersistentEntity();

        List<BeanPropertyWriter> result = newArrayList();
        for (BeanPropertyWriter writer : builder.getProperties()) {
            PersistentProperty<?> persistentProperty = findProperty(writer.getName(), entity, beanDesc);

            if (persistentProperty == null) {
                result.add(writer);
                continue;
            }

            if (PersistentPropertyType.forPersistentProperty(persistentProperty) == FILE) {
                continue;
            }

            result.add(writer);
        }

        builder.setProperties(result);

        return builder;
    }

    private PersistentProperty<?> findProperty(String finalName, PersistentEntity<?, ?> entity, BeanDescription description) {
        for (BeanPropertyDefinition definition : description.findProperties()) {
            if (definition.getName().equals(finalName)) {
                return entity.getPersistentProperty(definition.getInternalName());
            }
        }
        return null;
    }
}