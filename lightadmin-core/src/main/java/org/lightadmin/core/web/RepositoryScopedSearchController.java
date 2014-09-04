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
package org.lightadmin.core.web;

import com.google.common.base.Predicate;
import org.lightadmin.api.config.utils.ScopeMetadataUtils;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.persistence.repository.invoker.DynamicRepositoryInvoker;
import org.lightadmin.core.persistence.support.SpecificationCreator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.*;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.data.rest.webmvc.ControllerUtils.EMPTY_RESOURCE_LIST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SuppressWarnings("unchecked")
@RepositoryRestController
public class RepositoryScopedSearchController {

    private static final String BASE_MAPPING = "/{repository}/scope/{scopeName}";

    private final ConversionService conversionService;
    private final BeanFactory beanFactory;

    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    public RepositoryScopedSearchController(@Qualifier("defaultConversionService") ConversionService conversionService, PagedResourcesAssembler<Object> pagedResourcesAssembler, BeanFactory beanFactory) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.conversionService = conversionService;
        this.beanFactory = beanFactory;
    }

    @RequestMapping(value = BASE_MAPPING + "/search/count", method = GET)
    public ResponseEntity<?> countItems(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, RootResourceInformation repoRequest, WebRequest request, @PathVariable String scopeName) {
        DynamicRepositoryInvoker repositoryInvoker = (DynamicRepositoryInvoker) repoRequest.getInvoker();

        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();

        final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope(scopeName);

        final Specification filterSpecification = specificationFromRequest(request, persistentEntity);

        if (isPredicateScope(scope)) {
            final PredicateScopeMetadata predicateScope = (PredicateScopeMetadata) scope;

            return new ResponseEntity(countItemsBySpecificationAndPredicate(repositoryInvoker, filterSpecification, predicateScope.predicate()), HttpStatus.OK);
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            return new ResponseEntity(countItemsBySpecification(repositoryInvoker, and(scopeSpecification, filterSpecification)), HttpStatus.OK);
        }

        return new ResponseEntity(countItemsBySpecification(repositoryInvoker, filterSpecification), HttpStatus.OK);
    }

    @RequestMapping(value = BASE_MAPPING + "/search", method = GET)
    public ResponseEntity<?> filterEntities(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, RootResourceInformation repoRequest, PersistentEntityResourceAssembler assembler, WebRequest request, Pageable pageable, @PathVariable String scopeName) throws Exception {
        DynamicRepositoryInvoker repositoryInvoker = (DynamicRepositoryInvoker) repoRequest.getInvoker();
        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();

        final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope(scopeName);

        final Specification filterSpecification = specificationFromRequest(request, persistentEntity);

        if (isPredicateScope(scope)) {
            final PredicateScopeMetadata predicateScope = (PredicateScopeMetadata) scope;

            final Page page = findBySpecificationAndPredicate(repositoryInvoker, filterSpecification, predicateScope.predicate(), pageable);

            Object resources = resultToResources(page, assembler);

            return new ResponseEntity(resources, HttpStatus.OK);
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            Page page = findItemsBySpecification(repositoryInvoker, and(scopeSpecification, filterSpecification), pageable);

            Object resources = resultToResources(page, assembler);

            return new ResponseEntity(resources, HttpStatus.OK);
        }

        Page page = findItemsBySpecification(repositoryInvoker, filterSpecification, pageable);

        Object resources = resultToResources(page, assembler);

        return new ResponseEntity(resources, HttpStatus.OK);
    }

    protected Resources resultToResources(Object result, PersistentEntityResourceAssembler assembler) {
        if (result instanceof Page) {
            Page<Object> page = (Page<Object>) result;
            return entitiesToResources(page, assembler);
        }

        if (result instanceof Iterable) {
            return entitiesToResources((Iterable<Object>) result, assembler);
        }

        if (null == result) {
            return new Resources(EMPTY_RESOURCE_LIST);
        }

        Resource<Object> resource = assembler.toResource(result);
        return new Resources(singletonList(resource));
    }

    protected Resources<? extends Resource<Object>> entitiesToResources(Page<Object> page, PersistentEntityResourceAssembler assembler) {
        return pagedResourcesAssembler.toResource(page, assembler);
    }

    protected Resources<Resource<Object>> entitiesToResources(Iterable<Object> entities, PersistentEntityResourceAssembler assembler) {
        List<Resource<Object>> resources = newArrayList();
        for (Object obj : entities) {
            resources.add(obj == null ? null : assembler.toResource(obj));
        }
        return new Resources(resources);
    }

    private Specification specificationFromRequest(WebRequest request, PersistentEntity<?, ?> persistentEntity) {
        return specificationCreator().toSpecification(persistentEntity, request.getParameterMap());
    }

    private long countItemsBySpecificationAndPredicate(DynamicRepositoryInvoker repository, final Specification specification, Predicate predicate) {
        final List<?> items = findItemsBySpecification(repository, specification);

        return filter(items, predicate).size();
    }

    private long countItemsBySpecification(final DynamicRepositoryInvoker invoker, final Specification specification) {
        return invoker.count(specification);
    }

    private List<?> findItemsBySpecification(final DynamicRepositoryInvoker invoker, final Specification specification) {
        return invoker.findAll(specification);
    }

    private Specification and(Specification specification, Specification otherSpecification) {
        return where(specification).and(otherSpecification);
    }

    private Page<?> findItemsBySpecification(final DynamicRepositoryInvoker invoker, final Specification specification, final Pageable pageSort) {
        return invoker.findAll(specification, pageSort);
    }

    private List<?> findItemsBySpecification(final DynamicRepositoryInvoker invoker, final Specification specification, final Sort sort) {
        return invoker.findAll(specification, sort);
    }

    private Page<?> selectPage(List<Object> items, Pageable pageable) {
        final List<Object> itemsOnPage = items.subList(pageable.getOffset(), Math.min(items.size(), pageable.getOffset() + pageable.getPageSize()));

        return new PageImpl(itemsOnPage, pageable, items.size());
    }

    private Page findBySpecificationAndPredicate(DynamicRepositoryInvoker invoker, final Specification specification, Predicate predicate, final Pageable pageable) {
        final List<?> items = findItemsBySpecification(invoker, specification, pageable.getSort());

        return selectPage(newArrayList(filter(items, predicate)), pageable);
    }

    private GlobalAdministrationConfiguration globalAdministrationConfiguration() {
        return beanFactory.getBean(GlobalAdministrationConfiguration.class);
    }

    private SpecificationCreator specificationCreator() {
        return new SpecificationCreator(this.conversionService, globalAdministrationConfiguration());
    }
}