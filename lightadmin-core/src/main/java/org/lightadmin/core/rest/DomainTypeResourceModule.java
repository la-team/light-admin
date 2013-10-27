package org.lightadmin.core.rest;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.module.SimpleSerializers;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.springframework.hateoas.Resource;

import java.io.IOException;

public class DomainTypeResourceModule extends SimpleModule {

    private final DomainTypeToResourceConverter domainTypeToResourceConverter;

    public DomainTypeResourceModule(final DomainTypeToResourceConverter domainTypeToResourceConverter) {
        super("DomainTypeResourceModule", Version.unknownVersion());

        this.domainTypeToResourceConverter = domainTypeToResourceConverter;
    }

    @Override
    public void setupModule(final SetupContext context) {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(DomainTypeResource.class, new DomainTypeResourceSerializer());

        context.addSerializers(serializers);
    }

    private class DomainTypeResourceSerializer extends SerializerBase<DomainTypeResource> {

        protected DomainTypeResourceSerializer() {
            super(DomainTypeResource.class);
        }

        @Override
        public void serialize(DomainTypeResource value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (null == value) {
                provider.defaultSerializeNull(jgen);
                return;
            }

            final Resource resource = domainTypeToResourceConverter.convert(value.getResource(), value.getConfigurationUnitType(), value.getFieldMetadatas());

            jgen.writeObject(resource);
        }
    }
}