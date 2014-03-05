package org.springframework.data.repository.core.support;

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.util.Assert;

import java.io.Serializable;

public class DefaultRepositoryMetadata extends AbstractRepositoryMetadata {

    private static final String MUST_BE_A_REPOSITORY = String.format("Given type must be assignable to %s!",
            Repository.class);

    private final Class<? extends Serializable> idType;
    private final Class<?> domainType;

    /**
     * Creates a new {@link DefaultRepositoryMetadata} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public DefaultRepositoryMetadata(Class<?> repositoryInterface) {

        super(repositoryInterface);
        Assert.isTrue(Repository.class.isAssignableFrom(repositoryInterface), MUST_BE_A_REPOSITORY);
        Assert.isTrue(repositoryInterface.isAnnotationPresent(RepositoryDefinition.class));

        this.idType = resolveIdType(repositoryInterface);
        this.domainType = resolveDomainType(repositoryInterface);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.RepositoryMetadata#getDomainType()
     */
    @Override
    public Class<?> getDomainType() {
        return this.domainType;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.RepositoryMetadata#getIdType()
     */
    @Override
    public Class<? extends Serializable> getIdType() {
        return this.idType;
    }

    private Class<? extends Serializable> resolveIdType(Class<?> repositoryInterface) {
        RepositoryDefinition annotation = repositoryInterface.getAnnotation(RepositoryDefinition.class);

        if (annotation == null || annotation.idClass() == null) {
            throw new IllegalArgumentException(String.format("Could not resolve id type of %s!", repositoryInterface));
        }

        return annotation.idClass();
    }

    private Class<?> resolveDomainType(Class<?> repositoryInterface) {

        RepositoryDefinition annotation = repositoryInterface.getAnnotation(RepositoryDefinition.class);

        if (annotation == null || annotation.domainClass() == null) {
            throw new IllegalArgumentException(String.format("Could not resolve domain type of %s!", repositoryInterface));
        }

        return annotation.domainClass();
    }
}