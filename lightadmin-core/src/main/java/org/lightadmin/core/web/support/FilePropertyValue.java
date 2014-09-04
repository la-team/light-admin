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
package org.lightadmin.core.web.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class FilePropertyValue {
    private boolean fileExists;
    private Link fileLink;
    private String fileName;
    private byte[] value;

    public FilePropertyValue(boolean fileExists) {
        this.fileExists = fileExists;
    }

    public FilePropertyValue(Link fileLink) {
        this(true);
        this.fileLink = fileLink;
    }

    public FilePropertyValue(Link fileLink, byte[] value) {
        this(fileLink);
        this.value = value;
    }

    public FilePropertyValue(String fileName, byte[] value) {
        this.fileName = fileName;
        this.value = value;
    }

    @JsonUnwrapped
    @JsonInclude(NON_NULL)
    @JsonProperty("file_name")
    public String getFileName() {
        return fileName;
    }

    @JsonUnwrapped
    @JsonProperty("file_exists")
    public boolean isFileExists() {
        return fileExists;
    }

    @JsonUnwrapped
    @JsonInclude(NON_NULL)
    @JsonProperty("file_link")
    public Link getFileLink() {
        return fileLink;
    }

    @JsonUnwrapped
    @JsonInclude(NON_EMPTY)
    @JsonProperty("value")
    public byte[] getValue() {
        return value;
    }
}