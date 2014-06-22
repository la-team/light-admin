package org.lightadmin.core.rest.binary;

import org.springframework.data.mapping.PersistentProperty;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class FileStorageUtils {

    public static String relativePathToStoreBinaryAttrValue(String domainTypeName, Object idValue, PersistentProperty attrMeta) {
        return relativePathToDomainStorageAttributeDirectory(domainTypeName, idValue, attrMeta) + "/" + "file.bin";
    }

    public static String relativePathToDomainStorageAttributeDirectory(String domainTypeName, Object idValue, PersistentProperty attrMeta) {
        return relativePathToDomainStorageDirectory(domainTypeName, idValue) + "/" + attrMeta.getName();
    }

    public static String relativePathToDomainStorageDirectory(String domainTypeName, Object idValue) {
        return format("/domain/%s/%s",
                domainTypeName,
                valueOf(idValue)
        );
    }
}