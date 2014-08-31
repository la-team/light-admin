package org.springframework.data.rest.webmvc;

import com.google.common.base.Predicate;
import org.lightadmin.api.config.utils.ScopeMetadataUtils;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
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
import org.springframework.data.rest.core.invoke.DynamicRepositoryInvoker;
import org.springframework.data.rest.webmvc.support.SpecificationCreator;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.*;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SuppressWarnings("unchecked")
@RepositoryRestController
public class RepositoryScopedSearchController extends AbstractRepositoryRestController {

    private static final String BASE_MAPPING = "/{repository}/scope/{scopeName}";

    private final ConversionService conversionService;
    private final BeanFactory beanFactory;

    @Autowired
    public RepositoryScopedSearchController(@Qualifier("defaultConversionService") ConversionService conversionService, PagedResourcesAssembler<Object> pagedResourcesAssembler, BeanFactory beanFactory) {
        super(pagedResourcesAssembler);

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

            return new ResponseEntity<>(countItemsBySpecificationAndPredicate(repositoryInvoker, filterSpecification, predicateScope.predicate()), HttpStatus.OK);
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            return new ResponseEntity<>(countItemsBySpecification(repositoryInvoker, and(scopeSpecification, filterSpecification)), HttpStatus.OK);
        }

        return new ResponseEntity<>(countItemsBySpecification(repositoryInvoker, filterSpecification), HttpStatus.OK);
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

            return new ResponseEntity<>(resources, HttpStatus.OK);
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            Page page = findItemsBySpecification(repositoryInvoker, and(scopeSpecification, filterSpecification), pageable);

            Object resources = resultToResources(page, assembler);

            return new ResponseEntity<>(resources, HttpStatus.OK);
        }

        Page page = findItemsBySpecification(repositoryInvoker, filterSpecification, pageable);

        Object resources = resultToResources(page, assembler);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    private GlobalAdministrationConfiguration globalAdministrationConfiguration() {
        return beanFactory.getBean(GlobalAdministrationConfiguration.class);
    }

    private SpecificationCreator specificationCreator() {
        return new SpecificationCreator(this.conversionService, globalAdministrationConfiguration());
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

        return new PageImpl<>(itemsOnPage, pageable, items.size());
    }

    private Page findBySpecificationAndPredicate(DynamicRepositoryInvoker invoker, final Specification specification, Predicate predicate, final Pageable pageable) {
        final List<?> items = findItemsBySpecification(invoker, specification, pageable.getSort());

        return selectPage(newArrayList(filter(items, predicate)), pageable);
    }
}