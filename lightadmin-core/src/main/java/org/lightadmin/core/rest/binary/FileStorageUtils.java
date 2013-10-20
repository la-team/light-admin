package org.lightadmin.core.rest.binary;

import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.Serializable;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class FileStorageUtils {

    public static String relativePathToStoreBinaryAttrValue(String domainTypeName, Serializable idValue, AttributeMetadata attrMeta) {
        return relativePathToDomainStorageDirectory(domainTypeName, idValue) + "/" + attrMeta.name() + "/" + "file.bin";
    }

    public static String relativePathToDomainStorageDirectory(String domainTypeName, Serializable idValue) {
        return format("/domain/%s/%s",
                domainTypeName,
                valueOf(idValue)
        );
    }
}