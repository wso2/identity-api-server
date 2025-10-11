/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Common util class.
 */
public class Util {

    private static final Log LOG = LogFactory.getLog(Util.class);
    private static final String PAGE_LINK_REL_NEXT = "next";
    private static final String PAGE_LINK_REL_PREVIOUS = "previous";
    private static final String PAGINATION_LINK_FORMAT = Constants.V1_API_PATH_COMPONENT
            + "%s?offset=%d&limit=%d";

    /**
     * Get correlation id of current thread.
     *
     * @return correlation-id.
     */
    public static String getCorrelation() {
        String ref;
        if (isCorrelationIDPresent()) {
            ref = MDC.get(Constants.CORRELATION_ID_MDC);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieved existing correlation ID from MDC.");
            }
        } else {
            ref = UUID.randomUUID().toString();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Generated new correlation ID as none was present in MDC.");
            }
        }
        return ref;
    }

    /**
     * Check whether correlation id present in the log MDC.
     *
     * @return whether the correlation id is present.
     */
    public static boolean isCorrelationIDPresent() {
        return MDC.get(Constants.CORRELATION_ID_MDC) != null;
    }

    /**
     * Base64 URL encodes a given string.
     *
     * @param value String to be encoded.
     * @return Encoded string.
     */
    public static String base64URLEncode(String value) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Performing Base64 URL encoding.");
        }
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 URL decode a given encoded string.
     *
     * @param value Encoded string to be decoded.
     * @return Decoded string.
     */
    public static String base64URLDecode(String value) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Performing Base64 URL decoding.");
        }
        return new String(
                Base64.getUrlDecoder().decode(value),
                StandardCharsets.UTF_8);
    }

    /**
     * Build 'next' and 'previous' pagination links.
     * @param limit Value of the 'limit' parameter.
     * @param currentOffset Value of the 'currentOffset' parameter.
     * @param totalResultsFromSearch Value of the 'totalResultsFromSearch' parameter.
     * @param servicePathComponent API service path. E.g: applications/
     * @return A map containing pagination link key-value pairs.
     * @deprecated because this can not build pagination links when filter and attributes params are used in the api.
     * Use {@link #buildPaginationLinks(int, int, int, String, String, String)} instead.
     */
    @Deprecated
    public static Map<String, String> buildPaginationLinks(int limit, int currentOffset, int totalResultsFromSearch,
                                                           String servicePathComponent) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Building pagination links with limit: %d, offset: %d, total: %d", 
                    limit, currentOffset, totalResultsFromSearch));
        }
        Map<String, String> links = new HashMap<>();

        // Next link.
        if ((currentOffset + limit) < totalResultsFromSearch) {
            links.put(PAGE_LINK_REL_NEXT, ContextLoader.buildURIForBody
                    (String.format(PAGINATION_LINK_FORMAT, servicePathComponent, (currentOffset + limit), limit))
                    .toString());
        }

        /*
        Previous link.
        Previous link matters only if offset is greater than 0.
        */
        if (currentOffset > 0) {
            if ((currentOffset - limit) >= 0) { // A previous page of size 'limit' exists.
                links.put(PAGE_LINK_REL_PREVIOUS, ContextLoader.buildURIForBody
                        (String.format(PAGINATION_LINK_FORMAT, servicePathComponent,
                                calculateOffsetForPreviousLink(currentOffset, limit, totalResultsFromSearch), limit))
                        .toString());
            } else { // A previous page exists but it's size is less than the specified limit.
                links.put(PAGE_LINK_REL_PREVIOUS, ContextLoader.buildURIForBody
                        (String.format(PAGINATION_LINK_FORMAT, servicePathComponent, 0, currentOffset)).toString());
            }
        }

        return links;
    }

    /**
     * Build 'next' and 'previous' pagination links.
     *
     * @param limit                  Value of the 'limit' parameter.
     * @param currentOffset          Value of the 'currentOffset' parameter.
     * @param totalResultsFromSearch Value of the 'totalResultsFromSearch' parameter.
     * @param servicePathComponent   API service path. E.g: applications/
     * @param requiredAttributes     Value of the 'attributes' parameter.
     * @param filter                 Value of the 'filter' parameter.
     * @return A map containing pagination link key-value pairs.
     */
    public static Map<String, String> buildPaginationLinks(int limit, int currentOffset, int totalResultsFromSearch,
                                                           String servicePathComponent, String requiredAttributes,
                                                           String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Building pagination links with limit: %d, offset: %d, total: %d, " +
                    "attributes: %s, filter: %s", limit, currentOffset, totalResultsFromSearch, 
                    requiredAttributes != null ? "[provided]" : "null", filter != null ? "[provided]" : "null"));
        }
        Map<String, String> links = new HashMap<>();

        StringBuilder otherParams = new StringBuilder();
        if (!StringUtils.isEmpty(requiredAttributes)) {
            otherParams.append("&attributes=").append(requiredAttributes);
        }
        if (!StringUtils.isEmpty(filter)) {
            otherParams.append("&filter=").append(filter.replace(" ", "+"));
        }

        // Next link.
        if ((currentOffset + limit) < totalResultsFromSearch) {
            links.put(PAGE_LINK_REL_NEXT, ContextLoader.buildURIForBody(String.format(PAGINATION_LINK_FORMAT,
                    servicePathComponent, (currentOffset + limit), limit) + otherParams).toString());
        }

        /*
        Previous link.
        Previous link matters only if offset is greater than 0.
        */
        if (currentOffset > 0) {
            if ((currentOffset - limit) >= 0) { // A previous page of size 'limit' exists.
                links.put(PAGE_LINK_REL_PREVIOUS, ContextLoader.buildURIForBody
                        (String.format(PAGINATION_LINK_FORMAT, servicePathComponent,
                                calculateOffsetForPreviousLink(currentOffset, limit, totalResultsFromSearch), limit) +
                                otherParams).toString());
            } else { // A previous page exists but it's size is less than the specified limit.
                links.put(PAGE_LINK_REL_PREVIOUS, ContextLoader.buildURIForBody
                        (String.format(PAGINATION_LINK_FORMAT, servicePathComponent, 0, currentOffset) +
                                otherParams).toString());
            }
        }

        return links;
    }

    private static int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        if (limit <= 0) {
            // If limit is 0 or negative, consider it as 0 and build the previous page.
            return offset;
        }

        int newOffset = (offset - limit);
        if (newOffset < total) {
            return newOffset;
        }

        // If offset is greater than total, go back by the chunks of limit until a proper page is found.
        return calculateOffsetForPreviousLink(newOffset, limit, total);
    }

    /**
     * Resolves the valid media type for a given file type.
     *
     * @param fileType File type.
     * @return Media type.
     */
    public static String getMediaType(String fileType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolving media type for file type: " + (fileType != null ? fileType : "null"));
        }
        
        if (containsValidMediaType(fileType, Constants.VALID_MEDIA_TYPES_XML)) {
            return Constants.MEDIA_TYPE_XML;
        } else if (containsValidMediaType(fileType, Constants.VALID_MEDIA_TYPES_JSON)) {
            return Constants.MEDIA_TYPE_JSON;
        } else if (containsValidMediaType(fileType, Constants.VALID_MEDIA_TYPES_YAML)) {
            return Constants.MEDIA_TYPE_YAML;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Unsupported media type for file type: " + (fileType != null ? fileType : "null"));
        }
        return Constants.MEDIA_TYPE_UNSUPPORTED;
    }

    private static boolean containsValidMediaType(String fileType, String[] supportedMediaTypes) {

        for (String supportedMediaType : supportedMediaTypes) {
            if (fileType.contains(supportedMediaType)) {
                return true;
            }
        }
        return false;
    }
}
