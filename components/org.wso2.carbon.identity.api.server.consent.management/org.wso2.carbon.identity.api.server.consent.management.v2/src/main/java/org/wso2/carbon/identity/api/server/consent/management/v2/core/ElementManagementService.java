/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.consent.management.v2.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.consent.mgt.core.ConsentManager;
import org.wso2.carbon.consent.mgt.core.exception.ConsentManagementException;
import org.wso2.carbon.consent.mgt.core.model.PIICategory;
import org.wso2.carbon.consent.mgt.core.util.ConsentUtils;
import org.wso2.carbon.consent.mgt.core.util.FilterQueriesUtil;
import org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PaginationLink;
import org.wso2.carbon.identity.api.server.consent.management.v2.util.ConsentMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.consent.management.v2.util.FilterAttributeExtractor;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_ELEMENT_UUID_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_FILTER_EXPRESSION;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_QUERY_PARAM;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.FilterConstants;
import static org.wso2.carbon.consent.mgt.core.util.ConsentUtils.handleClientException;


/**
 * Service class for element (PIICategory) operations in the V2 Consent Management API.
 */
public class ElementManagementService {

    private static final Log LOG = LogFactory.getLog(ElementManagementService.class);

    private final ConsentManager consentManager;

    public ElementManagementService(ConsentManager consentManager) {

        this.consentManager = consentManager;
    }

    /**
     * Creates a new element (PIICategory).
     *
     * @param request Element create request.
     * @return ElementDTO with created element details.
     * @throws ConsentManagementException if creation fails.
     */
    public ElementDTO createElement(ElementCreateRequest request) {

        try {
            String displayName = StringUtils.isNotBlank(request.getDisplayName()) ? request.getDisplayName()
                    : request.getName();
            PIICategory piiCategory = new PIICategory(request.getName(), request.getDescription(), false, displayName);
            piiCategory.setTenantId(ConsentUtils.getTenantIdFromCarbonContext());
            PIICategory created = consentManager.addPIICategoryWithUuid(piiCategory);
            return toElementDTO(created);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Retrieves a single element by ID.
     *
     * @param elementId Element ID.
     * @return Response with ElementDTO.
     * @throws ConsentManagementException if retrieval fails.
     */
    public ElementDTO getElement(String elementId) {

        try {
            PIICategory piiCategory = consentManager.getPIICategoryByUuid(elementId);
            if (piiCategory == null) {
                throw handleClientException(ERROR_CODE_ELEMENT_UUID_NOT_FOUND, elementId);
            }
            return toElementDTO(piiCategory);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Lists elements with optional filtering and pagination.
     *
     * @param filterExpression Filter expression string (null for no filtering).
     * @param limit            Maximum results.
     * @param after            Cursor for pagination (results after this cursor).
     * @param before           Cursor for pagination (results before this cursor).
     * @return Response with ElementListResponse.
     * @throws ConsentManagementException if listing fails.
     */
    public ElementListResponse listElements(String filterExpression, Integer limit, String after, String before) {

        try {
            // Set default values if the parameters are not set.
            limit = validatedLimit(limit);

            // Validate before and after parameters.
            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM,
                        "Both 'before' and 'after' parameters are provided.");
            }

            if (StringUtils.isNotBlank(filterExpression)) {
                try {
                    FilterTreeBuilder filterTreeBuilder = new FilterTreeBuilder(filterExpression);
                    Node filterTree = filterTreeBuilder.buildTree();
                    Set<String> supportedAttrs = new HashSet<>(List.of(FilterConstants.FILTER_ATTR_NAME));
                    FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                    extractor.validateFilterAttributes(filterTree, supportedAttrs);
                } catch (IdentityException | java.io.IOException e) {
                    throw handleClientException(ERROR_CODE_INVALID_FILTER_EXPRESSION, e.getMessage());
                }
            }

            List<ExpressionNode> expressionNodes =
                    FilterQueriesUtil.getExpressionNodes(filterExpression, after, before);
            List<PIICategory> categories = consentManager.listPIICategories(expressionNodes, limit + 1);

            ElementListResponse listResponse = new ElementListResponse();
            List<PaginationLink> links = new ArrayList<>();

            if (categories != null && !categories.isEmpty()) {
                boolean hasMoreItems = categories.size() > limit;
                boolean needsReverse = StringUtils.isNotBlank(before);
                boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                        (StringUtils.isNotBlank(before) && !hasMoreItems);
                boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

                String url = "?limit=" + limit;
                if (StringUtils.isNotBlank(filterExpression)) {
                    try {
                        url += "&filter=" + URLEncoder.encode(filterExpression, StandardCharsets.UTF_8.name());
                    } catch (UnsupportedEncodingException e) {
                        LOG.debug("Server encountered an error while building pagination URL for the response.", e);
                    }
                }

                if (hasMoreItems) {
                    categories = new ArrayList<>(categories.subList(0, limit));
                }
                if (needsReverse) {
                    Collections.reverse(categories);
                }
                if (!isFirstPage) {
                    String prevCursor = Base64.getEncoder().encodeToString(
                            String.valueOf(categories.get(0).getId()).getBytes(StandardCharsets.UTF_8));
                    links.add(buildPaginationLink(
                            url + "&" + FilterConstants.FILTER_ATTR_BEFORE + "=" + prevCursor,
                            ConsentManagementConstants.LINK_REL_PREVIOUS));
                }
                if (!isLastPage) {
                    String nextCursor = Base64.getEncoder().encodeToString(
                            String.valueOf(categories.get(categories.size() - 1).getId())
                                    .getBytes(StandardCharsets.UTF_8));
                    links.add(buildPaginationLink(
                            url + "&" + FilterConstants.FILTER_ATTR_AFTER + "=" + nextCursor,
                            ConsentManagementConstants.LINK_REL_NEXT));
                }
            }

            List<ElementDTO> items = new ArrayList<>();
            if (categories != null) {
                for (PIICategory cat : categories) {
                    items.add(toElementDTO(cat));
                }
            }

            listResponse.setTotalResults(items.size());
            listResponse.setLinks(links);
            listResponse.setElements(items);
            return listResponse;
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Deletes an element by ID.
     *
     * @param elementId Element ID.
     * @throws ConsentManagementException if deletion fails.
     */
    public void deleteElement(String elementId) {

        try {
            consentManager.deletePIICategory(elementId);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    private ElementDTO toElementDTO(PIICategory cat) {

        ElementDTO dto = new ElementDTO();
        if (StringUtils.isNotBlank(cat.getUuid())) {
            dto.setId(cat.getUuid());
        }
        dto.setName(cat.getName());
        dto.setDisplayName(cat.getDisplayName());
        dto.setDescription(cat.getDescription());
        dto.setTenantDomain(cat.getTenantDomain());
        return dto;
    }

    private Integer validatedLimit(Integer limit) throws ConsentManagementException {

        limit = limit == null ? DEFAULT_LIMIT : limit;
        if (limit <= 0) {
            throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM, limit.toString());
        }
        return limit;
    }

    private PaginationLink buildPaginationLink(String url, String rel) {

        return new PaginationLink()
                .href(ConsentMgtEndpointUtil.buildURIForHeader(ConsentManagementConstants.V2_API_PATH_COMPONENT +
                        ConsentManagementConstants.ELEMENTS_PATH + url).toString())
                .rel(rel);
    }
}
