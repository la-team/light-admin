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
package org.lightadmin.core.web.support;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.springframework.hateoas.core.LinkBuilderSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DomainLinkBuilder extends LinkBuilderSupport<DomainLinkBuilder> {

    public static final String DOMAIN_BASE_URI = "/domain";

    public DomainLinkBuilder(LightAdminConfiguration lightAdminConfiguration) {
        this(prepareBuilder(lightAdminConfiguration.getApplicationUrl(DOMAIN_BASE_URI)));
    }

    private DomainLinkBuilder(UriComponentsBuilder builder) {
        super(builder);
    }

    private static UriComponentsBuilder prepareBuilder(String baseUrl) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().pathSegment(baseUrl);
    }

    @Override
    protected DomainLinkBuilder createNewInstance(UriComponentsBuilder builder) {
        return new DomainLinkBuilder(builder);
    }

    @Override
    protected DomainLinkBuilder getThis() {
        return this;
    }
}