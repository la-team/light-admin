package org.lightadmin.core.legacy;

//import org.springframework.data.rest.repository.EntityMetadata;
//import org.springframework.data.rest.repository.RepositoryMetadata;
//import org.springframework.data.rest.repository.UriToDomainObjectUriResolver;

@Deprecated
public class UriToDomainTypeResolver {
//    extends
//} UriToDomainObjectUriResolver {
//
//    private static final String ID_NULL = "NULL";
//
//    @Override
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public Object resolve(URI baseUri, URI uri) {
//        URI relativeUri = baseUri.relativize(uri);
//        Stack<URI> uris = UriUtils.explode(baseUri, relativeUri);
//
//        if (uris.size() < 1) {
//            return null;
//        }
//
//        String repoName = UriUtils.path(uris.get(0));
//        String sId = UriUtils.path(uris.get(1));
//
//        RepositoryMetadata repoMeta = repositoryMetadataFor(repoName);
//
//        CrudRepository repo;
//        if (null == (repo = repoMeta.repository())) {
//            return null;
//        }
//
//        EntityMetadata entityMeta;
//        if (null == (entityMeta = repoMeta.entityMetadata())) {
//            return null;
//        }
//
//        if (ID_NULL.equals(sId) && repo instanceof DynamicJpaRepository) {
//            return ((DynamicJpaRepository) repo).getNullPlaceholder();
//        }
//
//        Class<? extends Serializable> idType = (Class<? extends Serializable>) entityMeta.idAttribute().type();
//        Serializable serId = null;
//        if (ClassUtils.isAssignable(idType, String.class)) {
//            serId = sId;
//        } else {
//            for (ConversionService cs : getConversionServices()) {
//                if (cs.canConvert(String.class, idType)) {
//                    serId = cs.convert(sId, idType);
//                    break;
//                }
//            }
//        }
//
//        return repo.findOne(serId);
//    }

}
