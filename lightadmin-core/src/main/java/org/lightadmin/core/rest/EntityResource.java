package org.lightadmin.core.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Map;

public class EntityResource extends Resource<Map> {

    public EntityResource(Object content, Link... links) {
        super((Map) content, links);
    }

    public EntityResource(Object content, Iterable iterable) {
        super((Map) content, iterable);
    }
}