package org.springframework.data.rest.webmvc.support;

import com.google.common.collect.Maps;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Map;

public class SimpleMapResource extends Resource<Map<String, Object>> {

    public SimpleMapResource(Link... links) {
        super(Maps.<String, Object>newLinkedHashMap(), links);
    }

    public SimpleMapResource(Iterable<Link> links) {
        super(Maps.<String, Object>newLinkedHashMap(), links);
    }

    public void put(String key, Object value) {
        getContent().put(key, value);
    }
}