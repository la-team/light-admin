package org.lightadmin.core.extension;

import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.UriToEntityConverter;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class RefreshableUriToEntityConverter extends UriToEntityConverter {

    private final Repositories repositories;

    public RefreshableUriToEntityConverter(Repositories repositories, DomainClassConverter<?> domainClassConverter) {
        super(repositories, domainClassConverter);

        this.repositories = repositories;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        final Set<ConvertiblePair> convertiblePairs = new HashSet<ConvertiblePair>();
        for (Class<?> domainType : repositories) {
            convertiblePairs.add(new ConvertiblePair(URI.class, domainType));
        }
        return convertiblePairs;
    }
}