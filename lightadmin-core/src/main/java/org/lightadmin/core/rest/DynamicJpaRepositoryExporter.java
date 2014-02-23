package org.lightadmin.core.rest;

//import org.springframework.data.rest.repository.jpa.JpaRepositoryExporter;

public class DynamicJpaRepositoryExporter {
//        extends JpaRepositoryExporter {
//
//    @Autowired
//    private GlobalAdministrationConfiguration globalAdministrationConfiguration;
//
//    @Autowired
//    private DomainTypeEntityMetadataResolver domainTypeEntityMetadataResolver;
//
//    @Override
//    public void refresh() {
//        //		if ( repositoriesAlreadyInitialized() ) {
//        //			return;
//        //		}
//
//        repositories = new DynamicRepositoriesDecorator(globalAdministrationConfiguration, domainTypeEntityMetadataResolver);
//
//        repositoryMetadata = newHashMap();
//
//        for (Class<?> domainType : repositories) {
//            if (exportOnlyTheseClasses.isEmpty() || exportOnlyTheseClasses.contains(domainType.getName())) {
//                final String repositoryServiceExporterName = repositoryServiceExporterName(domainType);
//
//                repositoryMetadata.put(repositoryServiceExporterName, createRepositoryMetadata(repositoryServiceExporterName, domainType, repositoryInterface(domainType), repositories));
//            }
//        }
//    }
//
//    private String repositoryServiceExporterName(final Class<?> domainType) {
//        return StringUtils.uncapitalize(domainType.getSimpleName());
//    }
//
//    private Class<?> repositoryInterface(final Class<?> domainType) {
//        return repositories.getRepositoryInformationFor(domainType).getRepositoryInterface();
//    }
//
//    private boolean repositoriesAlreadyInitialized() {
//        return null != repositories;
//    }
}