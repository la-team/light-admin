package org.lightadmin.core.view.preparer;

import com.google.common.collect.Collections2;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.api.config.utils.ScopeMetadataUtils;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.util.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.*;

public class ListViewPreparer extends ConfigurationAwareViewPreparer {

    @Override
    protected void execute(final TilesRequestContext tilesContext, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration) {
        super.execute(tilesContext, attributeContext, configuration);

        addAttribute(attributeContext, "fields", configuration.getListViewFragment().getFields());

        addAttribute(attributeContext, "scopes", scopes(configuration));

        addAttribute(attributeContext, "filters", configuration.getFilters());
    }

    private List<Pair<? extends ScopeMetadata, Long>> scopes(DomainTypeAdministrationConfiguration configuration) {
        final List<Pair<? extends ScopeMetadata, Long>> result = newLinkedList();
        final Iterable<ScopeMetadata> scopes = configuration.getScopes();

        final DynamicJpaRepository<?, ?> repository = configuration.getRepository();

        for (ScopeMetadata scope : scopes) {
            if (isPredicateScope(scope)) {
                result.add(scopeWithRecordsCount((PredicateScopeMetadata) scope, repository));
            } else if (isSpecificationScope(scope)) {
                result.add(scopeWithRecordsCount((ScopeMetadataUtils.SpecificationScopeMetadata) scope, repository));
            } else {
                result.add(Pair.create(scope, repository.count()));
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Pair<? extends ScopeMetadata, Long> scopeWithRecordsCount(final ScopeMetadataUtils.SpecificationScopeMetadata scope, final DynamicJpaRepository<?, ?> repository) {
        return Pair.create(scope, repository.count(scope.specification()));
    }

    @SuppressWarnings("unchecked")
    private Pair<? extends ScopeMetadata, Long> scopeWithRecordsCount(final PredicateScopeMetadata scope, final JpaRepository<?, ?> repository) {
        long recordsCount = Collections2.filter(repository.findAll(), scope.predicate()).size();

        return Pair.create(scope, recordsCount);
    }
}