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
import org.wso2.carbon.consent.mgt.core.model.Purpose;
import org.wso2.carbon.consent.mgt.core.model.PurposePIICategory;
import org.wso2.carbon.consent.mgt.core.model.PurposeVersion;
import org.wso2.carbon.consent.mgt.core.util.ConsentUtils;
import org.wso2.carbon.consent.mgt.core.util.FilterQueriesUtil;
import org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PaginationLink;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTOLatestVersion;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeElementBinding;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeElementDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeSummaryDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionSummaryDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.SetLatestVersionRequest;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.DEFAULT_PURPOSE_GROUP;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_ELEMENT_UUID_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_FILTER_EXPRESSION;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_QUERY_PARAM;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PII_CATEGORY_ID_REQUIRED;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_UUID_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_REQUIRED;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.FilterConstants;
import static org.wso2.carbon.consent.mgt.core.util.ConsentUtils.handleClientException;

/**
 * Service class for purpose operations in the V2 Consent Management API.
 */
public class PurposeManagementService {

    private static final Log LOG = LogFactory.getLog(PurposeManagementService.class);

    private final ConsentManager consentManager;

    public PurposeManagementService(ConsentManager consentManager) {

        this.consentManager = consentManager;
    }

    /**
     * Creates a new purpose.
     *
     * @param request Purpose create request.
     * @return PurposeDTO with created purpose details.
     * @throws ConsentManagementException if creation fails.
     */
    public PurposeDTO createPurpose(PurposeCreateRequest request) {

        try {
            List<PurposePIICategory> purposePIICategories = buildPurposePIICategories(request.getElements());
            Purpose purpose = new Purpose(request.getName(), request.getDescription(),
                    DEFAULT_PURPOSE_GROUP, request.getType(), purposePIICategories);
            purpose.setTenantId(ConsentUtils.getTenantIdFromCarbonContext());
            purpose.setVersion(request.getVersion());
            purpose.setProperties(request.getProperties());
            Purpose result = consentManager.addPurposeWithUuid(purpose);
            return toPurposeDTO(result);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Retrieves a purpose by ID.
     *
     * @param purposeId Purpose ID.
     * @return Response with PurposeDTO.
     * @throws ConsentManagementException if retrieval fails.
     */
    public PurposeDTO getPurpose(String purposeId) {

        try {
            Purpose purpose = consentManager.getPurposeByUuid(purposeId);
            if (purpose == null) {
                throw handleClientException(ERROR_CODE_PURPOSE_UUID_NOT_FOUND, purposeId);
            }
            return toPurposeDTO(purpose);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Lists purposes with optional filtering and pagination.
     *
     * @param filterExpression Filter expression string (null for no filtering).
     * @param limit            Maximum results.
     * @param after            Cursor for pagination (results after this cursor).
     * @param before           Cursor for pagination (results before this cursor).
     * @return Response with list of PurposeSummaryDTOs.
     * @throws ConsentManagementException if listing fails.
     */
    public PurposeListResponse listPurposes(String filterExpression, Integer limit, String after, String before) {

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
                    Set<String> supportedAttrs = new HashSet<>(Arrays.asList(
                            FilterConstants.FILTER_ATTR_NAME,
                            FilterConstants.FILTER_ATTR_TYPE));
                    FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                    extractor.validateFilterAttributes(filterTree, supportedAttrs);
                } catch (IdentityException | java.io.IOException e) {
                    throw handleClientException(ERROR_CODE_INVALID_FILTER_EXPRESSION, e.getMessage());
                }
            }

            List<ExpressionNode> expressionNodes =
                    FilterQueriesUtil.getExpressionNodes(filterExpression, after, before);
            List<Purpose> purposes = consentManager.listPurposes(expressionNodes, limit + 1);

            PurposeListResponse listResponse = new PurposeListResponse();
            List<PaginationLink> links = new ArrayList<>();

            if (purposes != null && !purposes.isEmpty()) {
                boolean hasMoreItems = purposes.size() > limit;
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
                    purposes = new ArrayList<>(purposes.subList(0, limit));
                }
                if (needsReverse) {
                    Collections.reverse(purposes);
                }
                if (!isFirstPage) {
                    String prevCursor = Base64.getEncoder().encodeToString(
                            String.valueOf(purposes.get(0).getId()).getBytes(StandardCharsets.UTF_8));
                    links.add(buildPaginationLink(ConsentManagementConstants.PURPOSES_PATH,
                            url + "&" + FilterConstants.FILTER_ATTR_BEFORE + "=" + prevCursor,
                            ConsentManagementConstants.LINK_REL_PREVIOUS));
                }
                if (!isLastPage) {
                    String nextCursor = Base64.getEncoder().encodeToString(
                            String.valueOf(purposes.get(purposes.size() - 1).getId()).getBytes(StandardCharsets.UTF_8));
                    links.add(buildPaginationLink(ConsentManagementConstants.PURPOSES_PATH,
                            url + "&" + FilterConstants.FILTER_ATTR_AFTER + "=" + nextCursor,
                            ConsentManagementConstants.LINK_REL_NEXT));
                }
            }

            List<PurposeSummaryDTO> items = new ArrayList<>();
            if (purposes != null) {
                for (Purpose p : purposes) {
                    items.add(toPurposeSummaryDTO(p));
                }
            }

            listResponse.setTotalResults(items.size());
            listResponse.setLinks(links);
            listResponse.setPurposes(items);
            return listResponse;
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Deletes a purpose by ID.
     *
     * @param purposeId Purpose ID.
     * @throws ConsentManagementException if deletion fails.
     */
    public void deletePurpose(String purposeId) {

        try {
            consentManager.deletePurpose(purposeId);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Creates a new version for a purpose.
     *
     * @param purposeId Purpose ID.
     * @param request   Version create request.
     * @return PurposeVersionDTO with created version details.
     * @throws ConsentManagementException if creation fails.
     */
    public PurposeVersionDTO createPurposeVersion(String purposeId, PurposeVersionCreateRequest request) {

        try {
            PurposeVersion version = new PurposeVersion();
            version.setVersion(request.getVersion());
            version.setDescription(request.getDescription());
            version.setPurposePIICategories(buildPurposePIICategories(request.getElements()));
            version.setProperties(request.getProperties());

            PurposeVersion created = consentManager.addPurposeVersion(purposeId, version,
                    Boolean.TRUE.equals(request.getSetAsLatest()));
            return toPurposeVersionDTO(created);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Retrieves a specific version of a purpose.
     *
     * @param purposeId Purpose UUID.
     * @param versionId Version UUID.
     * @return Response with PurposeVersionDTO.
     * @throws ConsentManagementException if retrieval fails.
     */
    public PurposeVersionDTO getPurposeVersion(String purposeId, String versionId) {

        try {
            PurposeVersion version = consentManager.getPurposeVersion(purposeId, versionId);
            if (version == null) {
                throw handleClientException(ERROR_CODE_PURPOSE_VERSION_NOT_FOUND, versionId);
            }
            return toPurposeVersionDTO(version);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Lists versions of a purpose with pagination applied in memory.
     *
     * @param purposeId Purpose UUID.
     * @param limit     Maximum results.
     * @param after     Cursor for pagination (results after this cursor).
     * @param before    Cursor for pagination (results before this cursor).
     * @return Response with list of PurposeVersionDTOs.
     * @throws ConsentManagementException if listing fails.
     */
    public PurposeVersionListResponse listPurposeVersions(String purposeId, Integer limit, String after,
                                                           String before) {

        try {
            // Set default values if the parameters are not set.
            limit = validatedLimit(limit);

            // Validate before and after parameters.
            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM,
                        "Both 'before' and 'after' parameters are provided.");
            }

            List<PurposeVersion> all = consentManager.listPurposeVersions(purposeId);
            if (all == null) {
                all = Collections.emptyList();
            }

            List<PurposeVersion> page;
            if (StringUtils.isNotBlank(after)) {
                String afterId = new String(Base64.getDecoder().decode(after), StandardCharsets.UTF_8);
                int afterIdx = -1;
                for (int i = 0; i < all.size(); i++) {
                    if (afterId.equals(all.get(i).getUuid())) {
                        afterIdx = i;
                        break;
                    }
                }
                page = new ArrayList<>(afterIdx >= 0 ? all.subList(afterIdx + 1, all.size()) : all);
            } else if (StringUtils.isNotBlank(before)) {
                String beforeId = new String(Base64.getDecoder().decode(before), StandardCharsets.UTF_8);
                int beforeIdx = all.size();
                for (int i = 0; i < all.size(); i++) {
                    if (beforeId.equals(all.get(i).getUuid())) {
                        beforeIdx = i;
                        break;
                    }
                }
                page = new ArrayList<>(all.subList(0, beforeIdx));
            } else {
                page = new ArrayList<>(all);
            }

            PurposeVersionListResponse listResponse = new PurposeVersionListResponse();
            List<PaginationLink> links = new ArrayList<>();
            String versionsPath = ConsentManagementConstants.PURPOSES_PATH + "/" + purposeId +
                    ConsentManagementConstants.VERSIONS_PATH;

            if (!page.isEmpty()) {
                boolean hasMoreItems = page.size() > limit;
                boolean needsReverse = StringUtils.isNotBlank(before);
                boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                        (StringUtils.isNotBlank(before) && !hasMoreItems);
                boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

                if (hasMoreItems) {
                    page.remove(page.size() - 1);
                }
                if (needsReverse) {
                    Collections.reverse(page);
                }
                if (!isFirstPage) {
                    String prevCursor = Base64.getEncoder().encodeToString(
                            page.get(0).getUuid().getBytes(StandardCharsets.UTF_8));
                    links.add(buildPaginationLink(versionsPath,
                            "?limit=" + limit + "&" + FilterConstants.FILTER_ATTR_BEFORE + "=" + prevCursor,
                            ConsentManagementConstants.LINK_REL_PREVIOUS));
                }
                if (!isLastPage) {
                    String nextCursor = Base64.getEncoder().encodeToString(
                            page.get(page.size() - 1).getUuid().getBytes(StandardCharsets.UTF_8));
                    links.add(buildPaginationLink(versionsPath,
                            "?limit=" + limit + "&" + FilterConstants.FILTER_ATTR_AFTER + "=" + nextCursor,
                            ConsentManagementConstants.LINK_REL_NEXT));
                }
            }

            List<PurposeVersionSummaryDTO> dtos = new ArrayList<>();
            for (PurposeVersion v : page) {
                dtos.add(toPurposeVersionSummaryDTO(v));
            }

            listResponse.setTotalResults(dtos.size());
            listResponse.setLinks(links);
            listResponse.setVersions(dtos);
            return listResponse;
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Deletes a specific version of a purpose.
     *
     * @param purposeId Purpose UUID.
     * @param versionId Version UUID.
     * @throws ConsentManagementException if deletion fails.
     */
    public void deletePurposeVersion(String purposeId, String versionId) {

        try {
            consentManager.deletePurposeVersion(purposeId, versionId);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Sets the latest version of a purpose using the version UUID from the request body.
     *
     * @param purposeId UUID of the purpose.
     * @param request   SetLatestVersionRequest containing the target versionId UUID.
     * @throws ConsentManagementException if operation fails.
     */
    public void setLatestVersion(String purposeId, SetLatestVersionRequest request) {

        try {
            if (request == null || request.getId() == null) {
                throw handleClientException(ERROR_CODE_PURPOSE_VERSION_REQUIRED, "id is required");
            }

            PurposeVersion version = consentManager.getPurposeVersion(purposeId, request.getId());
            if (version == null) {
                throw handleClientException(ERROR_CODE_PURPOSE_VERSION_NOT_FOUND, request.getId());
            }
            consentManager.setLatestPurposeVersion(purposeId, version.getVersion());
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    private List<PurposePIICategory> buildPurposePIICategories(List<PurposeElementBinding> elements)
            throws ConsentManagementException {

        List<PurposePIICategory> result = new ArrayList<>();
        if (elements == null) {
            return result;
        }
        for (PurposeElementBinding binding : elements) {
            if (binding == null || binding.getId() == null) {
                throw handleClientException(ERROR_CODE_PII_CATEGORY_ID_REQUIRED, "PII Category ID is required");
            }
            PIICategory element = consentManager.getPIICategoryByUuid(binding.getId());
            if (element == null) {
                throw handleClientException(ERROR_CODE_ELEMENT_UUID_NOT_FOUND, binding.getId());
            }
            Boolean mandatory = Boolean.TRUE.equals(binding.getMandatory());
            result.add(new PurposePIICategory(element, mandatory));
        }
        return result;
    }

    private PurposeSummaryDTO toPurposeSummaryDTO(Purpose purpose) {

        PurposeSummaryDTO dto = new PurposeSummaryDTO();
        dto.setId(purpose.getUuid());
        dto.setName(purpose.getName());
        dto.setDescription(purpose.getDescription());
        dto.setType(purpose.getGroupType());
        dto.setTenantDomain(purpose.getTenantDomain());
        PurposeVersion latestVersion = purpose.getLatestVersion();
        if (latestVersion != null) {
            PurposeDTOLatestVersion lv = new PurposeDTOLatestVersion();
            if (StringUtils.isNotBlank(latestVersion.getUuid())) {
                lv.setId(latestVersion.getUuid());
            }
            lv.setVersion(latestVersion.getVersion());
            dto.setLatestVersion(lv);
        }
        return dto;
    }

    private PurposeDTO toPurposeDTO(Purpose purpose) {

        PurposeDTO dto = new PurposeDTO();
        dto.setId(purpose.getUuid());
        dto.setName(purpose.getName());
        dto.setDescription(purpose.getDescription());
        dto.setType(purpose.getGroupType());
        dto.setTenantDomain(purpose.getTenantDomain());

        PurposeVersion latestVersion = purpose.getLatestVersion();
        if (latestVersion != null) {
            PurposeDTOLatestVersion lv = new PurposeDTOLatestVersion();
            if (StringUtils.isNotBlank(latestVersion.getUuid())) {
                lv.setId(latestVersion.getUuid());
            }
            lv.setVersion(latestVersion.getVersion());
            dto.setLatestVersion(lv);
        }

        List<PurposeElementDTO> elementDTOs = new ArrayList<>();
        List<PurposePIICategory> categories = purpose.getPurposePIICategories();
        if (categories != null) {
            for (PurposePIICategory cat : categories) {
                elementDTOs.add(toPurposeElementDTO(cat));
            }
        }
        dto.setElements(elementDTOs);

        if (latestVersion != null && latestVersion.getProperties() != null) {
            dto.setProperties(latestVersion.getProperties());
        }
        return dto;
    }

    private PurposeVersionDTO toPurposeVersionDTO(PurposeVersion version) {

        PurposeVersionDTO dto = new PurposeVersionDTO();
        if (StringUtils.isNotBlank(version.getUuid())) {
            dto.setId(version.getUuid());
        }
        dto.setVersion(version.getVersion());
        dto.setDescription(version.getDescription());
        dto.setProperties(version.getProperties());

        List<PurposeElementDTO> elementDTOs = new ArrayList<>();
        if (version.getPurposePIICategories() != null) {
            for (PurposePIICategory cat : version.getPurposePIICategories()) {
                elementDTOs.add(toPurposeElementDTO(cat));
            }
        }
        dto.setElements(elementDTOs);
        return dto;
    }

    private PurposeVersionSummaryDTO toPurposeVersionSummaryDTO(PurposeVersion version) {

        PurposeVersionSummaryDTO dto = new PurposeVersionSummaryDTO();
        if (StringUtils.isNotBlank(version.getUuid())) {
            dto.setId(version.getUuid());
        }
        dto.setVersion(version.getVersion());
        dto.setDescription(version.getDescription());
        return dto;
    }

    private PurposeElementDTO toPurposeElementDTO(PurposePIICategory cat) {

        PurposeElementDTO dto = new PurposeElementDTO();
        if (StringUtils.isNotBlank(cat.getUuid())) {
            dto.setId(cat.getUuid());
        }
        dto.setName(cat.getName());
        dto.setDisplayName(cat.getDisplayName());
        dto.setDescription(cat.getDescription());
        dto.setMandatory(cat.getMandatory());
        return dto;
    }

    private Integer validatedLimit(Integer limit) throws ConsentManagementException {

        limit = limit == null ? DEFAULT_LIMIT : limit;
        if (limit <= 0) {
            throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM, limit.toString());
        }
        return limit;
    }

    private PaginationLink buildPaginationLink(String resourcePath, String url, String rel) {

        return new PaginationLink()
                .href(ConsentMgtEndpointUtil.buildURIForHeader(ConsentManagementConstants.V2_API_PATH_COMPONENT +
                        resourcePath + url).toString())
                .rel(rel);
    }

}

