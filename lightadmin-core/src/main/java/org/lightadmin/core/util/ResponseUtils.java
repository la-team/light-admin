package org.lightadmin.core.util;

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