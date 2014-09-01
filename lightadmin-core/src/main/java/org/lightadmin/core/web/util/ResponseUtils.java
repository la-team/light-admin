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
package org.lightadmin.core.web.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class ResponseUtils {

    public static HttpHeaders octetStreamResponseHeader(MediaType mediaType, long length, String eTag) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(length);
        responseHeaders.setContentType(mediaType);
        responseHeaders.setCacheControl("max-age");
        if (isNotBlank(eTag)) {
            responseHeaders.setETag(eTag);
        }
        responseHeaders.set("Content-Disposition", "inline; filename=\"file.jpg\"");
        return responseHeaders;
    }

    public static HttpHeaders responseHeader(byte[] content, MediaType mediaType) {
        return octetStreamResponseHeader(mediaType, content.length, null);
    }

    public static String eTag(Class<?> clazz, String field, long size) {
        return "\"" + clazz + ":" + field + ":" + size + "\"";
    }

    public static void addImageResourceHeaders(HttpServletResponse response, HttpHeaders httpHeaders) {
        for (String httpHeaderKey : httpHeaders.keySet()) {
            response.setHeader(httpHeaderKey, httpHeaders.get(httpHeaderKey).get(0));
        }
    }
}