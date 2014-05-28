package org.springframework.data.rest.webmvc;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.lightadmin.api.config.utils.ScopeMetadataUtils;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.search.SpecificationCreator;
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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.isPredicateScope;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.isSpecificationScope;
import static org.springframework.data.jpa.domain.Specifications.where;

@SuppressWarnings("unchecked")
@RepositoryRestController
public class RepositoryScopedSearchController extends AbstractRepositoryRestController {

    private static final String BASE_MAPPING = "/{repositoryName}/scope/{scopeName}";

    private GlobalAdministrationConfiguration configuration;
    private SpecificationCreator specificationCreator;

    @Autowired
    public RepositoryScopedSearchController(GlobalAdministrationConfiguration configuration, @Qualifier("defaultConversionService") ConversionService conversionService, PagedResourcesAssembler<Object> pagedResourcesAssembler) {
        super(pagedResourcesAssembler);

        this.configuration = configuration;
        this.specificationCreator = new SpecificationCreator(conversionService, configuration);
    }

    @RequestMapping(value = BASE_MAPPING + "/search/count", method = RequestMethod.GET)
    public ResponseEntity<?> countItems(RootResourceInformation repoRequest, WebRequest request, @PathVariable String repositoryName, @PathVariable String scopeName) {
        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName(repositoryName);

        DynamicRepositoryInvoker repositoryInvoker = (DynamicRepositoryInvoker) repoRequest.getInvoker();

        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();

        final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope(scopeName);

        final Specification filterSpecification = specificationFromRequest(request, persistentEntity);

        if (isPredicateScope(scope)) {
            final ScopeMetadataUtils.PredicateScopeMetadata predicateScope = (ScopeMetadataUtils.PredicateScopeMetadata) scope;

            return responseEntity(countItemsBySpecificationAndPredicate(repositoryInvoker, filterSpecification, predicateScope.predicate()));
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            return responseEntity(countItemsBySpecification(repositoryInvoker, and(scopeSpecification, filterSpecification)));
        }

        return responseEntity(countItemsBySpecification(repositoryInvoker, filterSpecification));
    }

    @RequestMapping(value = BASE_MAPPING + "/search", method = RequestMethod.GET)
    public ResponseEntity<?> filterEntities(RootResourceInformation repoRequest, PersistentEntityResourceAssembler assembler, WebRequest request, Pageable pageable, @PathVariable String repositoryName, @PathVariable String scopeName) throws Exception {

        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName(repositoryName);

        DynamicRepositoryInvoker repositoryInvoker = (DynamicRepositoryInvoker) repoRequest.getInvoker();

        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();

        final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope(scopeName);

        final Specification filterSpecification = specificationFromRequest(request, persistentEntity);

        Set<FieldMetadata> listViewFields = domainTypeAdministrationConfiguration.getListViewFragment().getFields();

        if (isPredicateScope(scope)) {
            final ScopeMetadataUtils.PredicateScopeMetadata predicateScope = (ScopeMetadataUtils.PredicateScopeMetadata) scope;

            final Page page = findBySpecificationAndPredicate(repositoryInvoker, filterSpecification, predicateScope.predicate(), pageable);

            Object resources = resultToResources(page, assembler);

            return new ResponseEntity<>(resources, HttpStatus.OK);
//            return negotiateResponse(request, page, pageMetadata(page), DomainConfigurationUnitType.LIST_VIEW, listViewFields);
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            Page page = findItemsBySpecification(repositoryInvoker, and(scopeSpecification, filterSpecification), pageable);

            Object resources = resultToResources(page, assembler);

            return new ResponseEntity<>(resources, HttpStatus.OK);

//            return negotiateResponse(request, page, pageMetadata(page), DomainConfigurationUnitType.LIST_VIEW, listViewFields);
        }

        Page page = findItemsBySpecification(repositoryInvoker, filterSpecification, pageable);

        Object resources = resultToResources(page, assembler);

        return new ResponseEntity<>(resources, HttpStatus.OK);

//        return negotiateResponse(request, page, pageMetadata(page), DomainConfigurationUnitType.LIST_VIEW, listViewFields);
    }

    private PagedResources.PageMetadata pageMetadata(final Page page) {
        return new PagedResources.PageMetadata(page.getSize(), page.getNumber() + 1, page.getTotalElements(), page.getTotalPages());
    }

    private Specification specificationFromRequest(WebRequest request, PersistentEntity<?, ?> persistentEntity) {
        return specificationCreator.toSpecification(persistentEntity, request.getParameterMap());
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

    private ResponseEntity<?> responseEntity(Object value) {
        return ControllerUtils.toResponseEntity(HttpStatus.OK, new HttpHeaders(), new Resource<>(value));
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

        return selectPage(newArrayList(Collections2.filter(items, predicate)), pageable);
    }
}