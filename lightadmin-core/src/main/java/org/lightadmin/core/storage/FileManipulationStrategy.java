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
package org.lightadmin.core.storage;

import org.springframework.data.mapping.PersistentProperty;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public interface FileManipulationStrategy {

    void deleteFile(Object entity, PersistentProperty persistentProperty);

    boolean fileExists(Object entity, PersistentProperty persistentProperty) throws IOException;

    byte[] loadFile(Object entity, PersistentProperty persistentProperty) throws IOException;

    long copyFile(Object entity, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException;

    void saveFile(Object entity, PersistentProperty persistentProperty, Object value) throws IOException;

    void cleanup(Object entity, PersistentProperty persistentProperty) throws IOException;
}