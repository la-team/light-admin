package org.lightadmin.core.rest;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.lightadmin.api.config.utils.ScopeMetadataUtils;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfigurationAware;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.lightadmin.core.search.SpecificationCreator;
import org.lightadmin.core.web.util.FileResourceLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.data.rest.repository.RepositoryConstraintViolationException;
import org.springframework.data.rest.repository.RepositoryMetadata;
import org.springframework.data.rest.webmvc.PagingAndSorting;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.isPredicateScope;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.isSpecificationScope;
import static org.lightadmin.core.rest.binary.OperationBuilder.operationBuilder;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@SuppressWarnings("unchecked")
@RequestMapping("/rest")
public class DynamicRepositoryRestController extends FlexibleRepositoryRestController implements GlobalAdministrationConfigurationAware {

    private SpecificationCreator specificationCreator;

    private GlobalAdministrationConfiguration configuration;

    private ApplicationContext applicationContext;
    private LightAdminConfiguration lightAdminConfiguration;
    private OperationBuilder operationBuilder;

    @Autowired
    private FileResourceLoader fileResourceLoader;

    @PostConstruct
    public void init() throws Exception {
        this.specificationCreator = new SpecificationCreator(conversionService, configuration);
        this.operationBuilder = operationBuilder(configuration, lightAdminConfiguration);
    }

    @RequestMapping(value = "/{repository}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> createOrUpdate(ServletServerHttpRequest request, URI baseUri, @PathVariable String repository) throws IOException, IllegalAccessException, InstantiationException {
        return super.createOrUpdate(request, baseUri, repository, "");
    }

    private static final Date NULL_PLACEHOLDER_MAGIC_DATE = new Date(-377743392000001L);

    @Override
    @SuppressWarnings("rawtypes")
    protected void attrMetaSet(AttributeMetadata attrMeta, Object incomingVal, Object entity) throws IOException {
        DomainTypeBasicConfiguration repo;
        if (attrMeta.isCollectionLike() || attrMeta.isSetLike()) {
            collectionAttrMetaSet(attrMeta, incomingVal, entity);
        } else if ((repo = configuration.forDomainType(attrMeta.type())) != null && (repo.getRepository().isNullPlaceholder(incomingVal))) {
            attrMeta.set(null, entity);
        } else if (NULL_PLACEHOLDER_MAGIC_DATE.equals(incomingVal)) {
            attrMeta.set(null, entity);
        } else if (DomainTypeAttributeType.isOfFileType(attrMeta)) {
            operationBuilder.saveOperation(entity).perform(attrMeta, incomingVal);
        } else {
            attrMeta.set(incomingVal, entity);
        }
    }


    private void collectionAttrMetaSet(AttributeMetadata attrMeta, Object incomingVal, Object entity) throws IOException {
        // Trying to avoid collection-was-no-longer-referenced issue
        // if the collection is modifiable
        try {
            Collection col = (Collection) attrMeta.get(entity);
            col.clear();
            col.addAll((Collection) incomingVal);
        } catch (UnsupportedOperationException e) {
            attrMeta.set(incomingVal, entity);
        }
    }

    @RequestMapping(value = "/{repository}/{id}/{property}/file", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteFileOfPropertyOfEntity(ServletServerHttpRequest request, URI baseUri, @PathVariable String repository, @PathVariable String id, @PathVariable String property) throws IOException {
        final RepositoryMetadata repoMeta = repositoryMetadataFor(repository);
        final Serializable serId = stringToSerializable(id, (Class<? extends Serializable>) repoMeta.entityMetadata().idAttribute().type());
        final CrudRepository repo = repoMeta.repository();

        final Object entity;
        final AttributeMetadata attrMeta;
        if (null == (entity = repo.findOne(serId)) || null == (attrMeta = repoMeta.entityMetadata().attribute(property))) {
            return notFoundResponse(request);
        }

        if (!DomainTypeAttributeType.isOfFileType(attrMeta)) {
            return new ResponseEntity(METHOD_NOT_ALLOWED);
        }

        operationBuilder.deleteOperation(entity).perform(attrMeta);

        return new ResponseEntity(new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{repository}/{id}/{property}/file", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> filePropertyOfEntity(ServletServerHttpRequest request, ServletResponse response, URI baseUri, @PathVariable String repository, @PathVariable String id, @PathVariable String property, @RequestParam(value = "width", defaultValue = "-1") int width, @RequestParam(value = "height", defaultValue = "-1") int height) throws IOException {
        final RepositoryMetadata repoMeta = repositoryMetadataFor(repository);
        final Serializable serId = stringToSerializable(id, (Class<? extends Serializable>) repoMeta.entityMetadata().idAttribute().type());

        final Object entity;
        final AttributeMetadata attrMeta;
        if (null == (entity = repoMeta.repository().findOne(serId)) || null == (attrMeta = repoMeta.entityMetadata().attribute(property))) {
            return notFoundResponse(request);
        }

        if (DomainTypeAttributeType.isOfFileType(attrMeta)) {
            return fileResourceLoader.loadFile(entity, attrMeta, (HttpServletResponse) response, width, height);
        }
        return new ResponseEntity(BAD_REQUEST);
    }

    @RequestMapping(value = "/upload", method = {RequestMethod.PUT, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<?> saveFilePropertyOfEntity(final ServletServerHttpRequest request) throws IOException {
        final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request.getServletRequest();

        final Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();

        if (!fileMap.isEmpty()) {
            final Map.Entry<String, MultipartFile> fileEntry = fileMap.entrySet().iterator().next();

            final Map<String, Object> result = newLinkedHashMap();
            result.put("fileName", fileEntry.getValue().getOriginalFilename());
            result.put("fileContent", fileEntry.getValue().getBytes());

            return negotiateResponse(request, HttpStatus.OK, new HttpHeaders(), result);
        }

        return new ResponseEntity(METHOD_NOT_ALLOWED);
    }

    @ResponseBody
    @RequestMapping(value = "/{repositoryName}/{id}/unit/{configurationUnit}", method = RequestMethod.GET)
    public ResponseEntity<?> entity(ServletServerHttpRequest request, URI baseUri, @PathVariable String repositoryName, @PathVariable String id, @PathVariable String configurationUnit) throws IOException {

        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName(repositoryName);

        final DomainTypeEntityMetadata domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
        final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

        Serializable entityId = stringToSerializable(id, (Class<? extends Serializable>) domainTypeEntityMetadata.getIdAttribute().getType());

        final Object entity = repository.findOne(entityId);

        final DomainConfigurationUnitType configurationUnitType = DomainConfigurationUnitType.forName(configurationUnit);

        return negotiateResponse(request, HttpStatus.OK, new HttpHeaders(), new DomainTypeResource(entity, configurationUnitType, fields(domainTypeAdministrationConfiguration, configurationUnitType)));
    }

    @ResponseBody
    @RequestMapping(value = "/{repositoryName}/scope/{scopeName}/search/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> countItems(ServletServerHttpRequest request, @SuppressWarnings("unused") URI baseUri, @PathVariable String repositoryName, @PathVariable String scopeName) {
        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName(repositoryName);

        final DomainTypeEntityMetadata domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
        final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

        final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope(scopeName);

        final Specification filterSpecification = specificationFromRequest(request, domainTypeEntityMetadata);

        if (isPredicateScope(scope)) {
            final ScopeMetadataUtils.PredicateScopeMetadata predicateScope = (ScopeMetadataUtils.PredicateScopeMetadata) scope;

            return responseEntity(countItemsBySpecificationAndPredicate(repository, filterSpecification, predicateScope.predicate()));
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            return responseEntity(countItemsBySpecification(repository, and(scopeSpecification, filterSpecification)));
        }

        return responseEntity(countItemsBySpecification(repository, filterSpecification));
    }

    @ResponseBody
    @RequestMapping(value = "/{repositoryName}/scope/{scopeName}/search", method = RequestMethod.GET)
    public ResponseEntity<?> filterEntities(ServletServerHttpRequest request, @SuppressWarnings("unused") URI baseUri, PagingAndSorting pageSort, @PathVariable String repositoryName, @PathVariable String scopeName) throws IOException {

        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName(repositoryName);

        final DomainTypeEntityMetadata domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
        final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

        final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope(scopeName);

        final Specification filterSpecification = specificationFromRequest(request, domainTypeEntityMetadata);

        Set<FieldMetadata> listViewFields = domainTypeAdministrationConfiguration.getListViewFragment().getFields();

        if (isPredicateScope(scope)) {
            final ScopeMetadataUtils.PredicateScopeMetadata predicateScope = (ScopeMetadataUtils.PredicateScopeMetadata) scope;

            final Page page = findBySpecificationAndPredicate(repository, filterSpecification, predicateScope.predicate(), pageSort);

            return negotiateResponse(request, page, pageMetadata(page), DomainConfigurationUnitType.LIST_VIEW, listViewFields);
        }

        if (isSpecificationScope(scope)) {
            final Specification scopeSpecification = ((ScopeMetadataUtils.SpecificationScopeMetadata) scope).specification();

            Page page = findItemsBySpecification(repository, and(scopeSpecification, filterSpecification), pageSort);

            return negotiateResponse(request, page, pageMetadata(page), DomainConfigurationUnitType.LIST_VIEW, listViewFields);
        }

        Page page = findItemsBySpecification(repository, filterSpecification, pageSort);

        return negotiateResponse(request, page, pageMetadata(page), DomainConfigurationUnitType.LIST_VIEW, listViewFields);
    }

    @Override
    @ExceptionHandler(RepositoryConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity handleValidationFailure(RepositoryConstraintViolationException ex, ServletServerHttpRequest request) throws IOException {
        final Map packet = newHashMap();
        final List<Map<String, String>> errors = newArrayList();

        for (FieldError fe : ex.getErrors().getFieldErrors()) {
            List<Object> args = newArrayList(fe.getObjectName(), fe.getField(), fe.getRejectedValue());

            if (fe.getArguments() != null) {
                Collections.addAll(args, fe.getArguments());
            }

            String msg = applicationContext.getMessage(fe.getCode(), args.toArray(), fe.getDefaultMessage(), null);
            Map<String, String> error = newHashMap();
            error.put("field", fe.getField());
            error.put("message", msg);
            errors.add(error);
        }
        packet.put("errors", errors);

        return negotiateResponse(request, BAD_REQUEST, new HttpHeaders(), packet);
    }

    @Override
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity handleMiscFailures(Throwable t, ServletServerHttpRequest request) throws IOException {
        LOG.debug("Handled exception", t);
        Map<String, String> error = singletonMap("message", t.getLocalizedMessage());
        Map packet = newHashMap();
        packet.put("errors", asList(error));
        return negotiateResponse(request, BAD_REQUEST, new HttpHeaders(), packet);
    }

    @Override
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public ResponseEntity handleMessageConversionFailure(Exception ex, HttpServletRequest request) throws IOException {
        LOG.error("Handled exception", ex);
        return handleMiscFailures(ex.getCause(), new ServletServerHttpRequest(request));
    }

    private Set<FieldMetadata> fields(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, final DomainConfigurationUnitType configurationUnitType) {
        switch (configurationUnitType) {
            case SHOW_VIEW:
                return domainTypeAdministrationConfiguration.getShowViewFragment().getFields();
            case FORM_VIEW:
                return domainTypeAdministrationConfiguration.getFormViewFragment().getFields();
            case QUICK_VIEW:
                return domainTypeAdministrationConfiguration.getQuickViewFragment().getFields();
            default:
                return domainTypeAdministrationConfiguration.getShowViewFragment().getFields();
        }
    }

    private long countItemsBySpecificationAndPredicate(DynamicJpaRepository repository, final Specification specification, Predicate predicate) {
        final List<?> items = findItemsBySpecification(repository, specification);

        return Collections2.filter(items, predicate).size();
    }

    private long countItemsBySpecification(final DynamicJpaRepository repository, final Specification specification) {
        return repository.count(specification);
    }

    private Page findBySpecificationAndPredicate(DynamicJpaRepository repository, final Specification specification, Predicate predicate, final PagingAndSorting pageSort) {
        final List<?> items = findItemsBySpecification(repository, specification, pageSort.getSort());

        return selectPage(newArrayList(Collections2.filter(items, predicate)), pageSort);
    }

    private Page<?> findItemsBySpecification(final DynamicJpaRepository repository, final Specification specification, final PagingAndSorting pageSort) {
        return repository.findAll(specification, pageSort);
    }

    private List<?> findItemsBySpecification(final DynamicJpaRepository repository, final Specification specification, final Sort sort) {
        return repository.findAll(specification, sort);
    }

    private List<?> findItemsBySpecification(final DynamicJpaRepository repository, final Specification specification) {
        return repository.findAll(specification);
    }

    private Page<?> selectPage(List<Object> items, PagingAndSorting pageSort) {
        final List<Object> itemsOnPage = items.subList(pageSort.getOffset(), Math.min(items.size(), pageSort.getOffset() + pageSort.getPageSize()));

        return new PageImpl<Object>(itemsOnPage, pageSort, items.size());
    }

    private Specification and(Specification specification, Specification otherSpecification) {
        return Specifications.where(specification).and(otherSpecification);
    }

    private Specification specificationFromRequest(ServletServerHttpRequest request, final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> entityMetadata) {
        final Map<String, String[]> parameters = request.getServletRequest().getParameterMap();

        return specificationCreator.toSpecification(entityMetadata, parameters);
    }

    private ResponseEntity<?> negotiateResponse(ServletServerHttpRequest request, Page page, PagedResources.PageMetadata pageMetadata, final DomainConfigurationUnitType configurationUnitType, Set<FieldMetadata> fieldMetadatas) throws IOException {
        return negotiateResponse(request, HttpStatus.OK, new HttpHeaders(), new PagedResources(toResources(page, configurationUnitType, fieldMetadatas), pageMetadata, Lists.<Link>newArrayList()));
    }

    private PagedResources.PageMetadata pageMetadata(final Page page) {
        return new PagedResources.PageMetadata(page.getSize(), page.getNumber() + 1, page.getTotalElements(), page.getTotalPages());
    }

    private List<Object> toResources(Page page, final DomainConfigurationUnitType configurationUnitType, Set<FieldMetadata> fieldMetadatas) {
        if (!page.hasContent()) {
            return newLinkedList();
        }

        List<Object> allResources = newArrayList();
        for (final Object item : page) {
            allResources.add(new DomainTypeResource(item, configurationUnitType, fieldMetadatas));
        }
        return allResources;
    }

    private ResponseEntity<String> responseEntity(Object value) {
        return new ResponseEntity<String>(valueOf(value), new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    @Autowired
    public void setGlobalAdministrationConfiguration(final GlobalAdministrationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setLightAdminConfiguration(LightAdminConfiguration lightAdminConfiguration) {
        this.lightAdminConfiguration = lightAdminConfiguration;
    }
}