package org.lightadmin.api.config.utils;

import org.lightadmin.core.util.Transformer;

import java.io.Serializable;

public interface EntityNameExtractor<F> extends Transformer<F, String>, Serializable {

    @Override
    String apply(F input);
}