/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.rest.binary;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.BeanWrapper;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.getFile;

public class FileExistsRestOperation extends AbstractFileRestOperation {

    protected FileExistsRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public boolean perform(PersistentProperty persistentProperty) throws IOException {
        if (persistentProperty.getType().equals(byte[].class)) {
            return ArrayUtils.isNotEmpty((byte[]) BeanWrapper.create(entity, null).getProperty(persistentProperty));
        }

        if (persistentProperty.isAnnotationPresent(FileReference.class)) {
            FileReference fileReference = (FileReference) persistentProperty.findAnnotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                File file = referencedFile(persistentProperty);
                return file.exists() && FileUtils.sizeOf(file) > 0;
            } else {
                File file = fileStorageFile(persistentProperty);
                return file.exists() && FileUtils.sizeOf(file) > 0;
            }
        }
        return false;
    }
}