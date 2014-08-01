package org.springframework.data.rest.webmvc.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

public abstract class DynamicResourcesMixin<T> extends Resources<T> {

    @Override
    @XmlElement(name = "embedded")
    @JsonProperty("_embedded")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS, using = Jackson2HalModule.HalResourcesSerializer.class)
    @JsonDeserialize(using = Jackson2HalModule.HalResourcesDeserializer.class)
    public abstract Collection<T> getContent();

}
