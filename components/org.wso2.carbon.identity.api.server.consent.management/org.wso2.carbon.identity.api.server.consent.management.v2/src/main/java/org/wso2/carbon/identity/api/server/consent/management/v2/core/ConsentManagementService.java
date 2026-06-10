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
import org.wso2.carbon.consent.mgt.core.model.AddReceiptResponse;
import org.wso2.carbon.consent.mgt.core.model.ConsentAuthorization;
import org.wso2.carbon.consent.mgt.core.model.ConsentPurpose;
import org.wso2.carbon.consent.mgt.core.model.PIICategory;
import org.wso2.carbon.consent.mgt.core.model.PIICategoryValidity;
import org.wso2.carbon.consent.mgt.core.model.Purpose;
import org.wso2.carbon.consent.mgt.core.model.PurposePIICategoryBinding;
import org.wso2.carbon.consent.mgt.core.model.PurposeVersion;
import org.wso2.carbon.consent.mgt.core.model.Receipt;
import org.wso2.carbon.consent.mgt.core.model.ReceiptInput;
import org.wso2.carbon.consent.mgt.core.model.ReceiptService;
import org.wso2.carbon.consent.mgt.core.util.ConsentReceiptUtils;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationEntry;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentPurposeBinding;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentResponseDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentSummaryDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentValidateResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentedElementDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentedPurposeDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementTerminationInfo;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PaginationLink;
import org.wso2.carbon.identity.api.server.consent.management.v2.util.ConsentMgtEndpointUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ACTIVE_STATE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CONSENT_INVALID_STATE_FOR_AUTHORIZE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CONSENT_REJECTED_WITH_AUTHORIZATIONS;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CONSENT_USER_NOT_IN_AUTHORIZATION_LIST;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_ELEMENT_UUID_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_QUERY_PARAM;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.FilterConstants;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.PENDING_STATE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.REVOKE_STATE;
import static org.wso2.carbon.consent.mgt.core.util.ConsentUtils.handleClientException;

/**
 * Service class for consent (receipt) operations in the V2 Consent Management API.
 */
public class ConsentManagementService {

    private static final Log LOG = LogFactory.getLog(ConsentManagementService.class);


    private final ConsentManager consentManager;

    public ConsentManagementService(ConsentManager consentManager) {

        this.consentManager = consentManager;
    }

    /**
     * Creates a new consent receipt.
     *
     * @param request Consent create request.
     * @return ConsentResponseDTO with receipt details.
     * @throws ConsentManagementException if creation fails.
     */
    public ConsentResponseDTO createConsent(ConsentCreateRequest request) {

        try {
            return createConsentInternal(request);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    private ConsentResponseDTO createConsentInternal(ConsentCreateRequest request) throws ConsentManagementException {

        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        String currentUser = carbonContext.getUsername();
        String subjectId = StringUtils.isNotBlank(request.getSubjectId()) ? request.getSubjectId() : currentUser;
        boolean hasAuthorizations = request.getAuthorizations() != null && !request.getAuthorizations().isEmpty();
        boolean rejected = ConsentCreateRequest.StateEnum.REJECTED.equals(request.getState());
        if (rejected && hasAuthorizations) {
            throw handleClientException(ERROR_CODE_CONSENT_REJECTED_WITH_AUTHORIZATIONS, null);
        }

        ReceiptInput receiptInput = buildReceiptInput(request, subjectId);
        AddReceiptResponse addReceiptResponse = consentManager.addConsent(receiptInput);

        ConsentResponseDTO responseDTO = new ConsentResponseDTO();
        responseDTO.setId(addReceiptResponse.getConsentReceiptId());
        responseDTO.setLanguage(addReceiptResponse.getLanguage());
        responseDTO.setSubjectId(addReceiptResponse.getPiiPrincipalId());
        responseDTO.setTenantDomain(addReceiptResponse.getTenantDomain());
        boolean hasPendingAuth = request.getAuthorizations() != null && !request.getAuthorizations().isEmpty();
        if (hasPendingAuth) {
            responseDTO.setState(ConsentResponseDTO.StateEnum.PENDING);
        } else if (ConsentCreateRequest.StateEnum.REJECTED.equals(request.getState())) {
            responseDTO.setState(ConsentResponseDTO.StateEnum.REJECTED);
        } else {
            responseDTO.setState(ConsentResponseDTO.StateEnum.ACTIVE);
        }
        return responseDTO;
    }

    /**
     * Retrieves a consent receipt by ID.
     *
     * @param receiptId Receipt ID.
     * @return ConsentDTO with receipt details.
     * @throws ConsentManagementException if retrieval fails.
     */
    public ConsentDTO getConsent(String receiptId) {

        try {
            Receipt receipt = consentManager.getReceiptWithExtendedSchema(receiptId);
            // Lazy expiry on GET — validate status and re-fetch if state changed
            String latestState = consentManager.validateConsentStatus(receiptId);
            if (!latestState.equals(receipt.getState())) {
                receipt = consentManager.getReceiptWithExtendedSchema(receiptId);
            }
            return toConsentDTO(receipt);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Lists consent receipts with explicit filter params and pagination.
     *
     * @param subjectId        Filter by subject user ID.
     * @param serviceId        Filter by service ID.
     * @param state            Filter by consent state.
     * @param purposeId        Filter by purpose ID.
     * @param purposeVersionId Filter by specific purpose version ID.
     * @param limit            Maximum results.
     * @param after            Cursor for pagination (results after this cursor).
     * @param before           Cursor for pagination (results before this cursor).
     * @return Response with list of ConsentSummaryDTOs.
     * @throws ConsentManagementException if listing fails.
     */
    public ConsentListResponse listConsents(String subjectId, String serviceId, String state, String purposeId,
                                            String purposeVersionId, Integer limit, String after, String before) {

        try {
            return listConsentsInternal(subjectId, serviceId, state, purposeId, purposeVersionId, limit, after, before);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    private ConsentListResponse listConsentsInternal(String subjectId, String serviceId, String state,
                                                     String purposeId, String purposeVersionId, Integer limit,
                                                     String after, String before)
            throws ConsentManagementException {

        limit = validatedLimit(limit);

        if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
            throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM,
                    "Both 'before' and 'after' parameters are provided.");
        }

        List<Receipt> receipts = consentManager.listReceipts(
                subjectId,
                serviceId,
                state,
                purposeId != null ? purposeId.toString() : null,
                purposeVersionId != null ? purposeVersionId.toString() : null,
                after, before, limit + 1);

        ConsentListResponse result = new ConsentListResponse();
        List<PaginationLink> links = new ArrayList<>();

        if (receipts != null && !receipts.isEmpty()) {
            boolean hasMoreItems = receipts.size() > limit;
            boolean needsReverse = StringUtils.isNotBlank(before);
            boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                    (StringUtils.isNotBlank(before) && !hasMoreItems);
            boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

            String url = "?limit=" + limit;
            try {
                if (StringUtils.isNotBlank(subjectId)) {
                    url += "&subjectId=" + URLEncoder.encode(subjectId, StandardCharsets.UTF_8.name());
                }
                if (StringUtils.isNotBlank(serviceId)) {
                    url += "&serviceId=" + URLEncoder.encode(serviceId, StandardCharsets.UTF_8.name());
                }
                if (StringUtils.isNotBlank(state)) {
                    url += "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8.name());
                }
            } catch (UnsupportedEncodingException e) {
                LOG.debug("Server encountered an error while building pagination URL for the response.", e);
            }
            if (purposeId != null) {
                url += "&purposeId=" + purposeId;
            }
            if (purposeVersionId != null) {
                url += "&purposeVersionId=" + purposeVersionId;
            }

            if (hasMoreItems) {
                receipts = new ArrayList<>(receipts.subList(0, limit));
            }
            if (needsReverse) {
                Collections.reverse(receipts);
            }
            if (!isFirstPage) {
                String encodedString = Base64.getEncoder().encodeToString(
                        receipts.get(0).getCursor().getBytes(StandardCharsets.UTF_8));
                links.add(buildPaginationLink(url + "&" + FilterConstants.FILTER_ATTR_BEFORE + "=" + encodedString,
                        ConsentManagementConstants.LINK_REL_PREVIOUS));
            }
            if (!isLastPage) {
                String encodedString = Base64.getEncoder().encodeToString(
                        receipts.get(receipts.size() - 1).getCursor().getBytes(StandardCharsets.UTF_8));
                links.add(buildPaginationLink(url + "&" + FilterConstants.FILTER_ATTR_AFTER + "=" + encodedString,
                        ConsentManagementConstants.LINK_REL_NEXT));
            }
        }

        List<ConsentSummaryDTO> summaries = new ArrayList<>();
        if (receipts != null) {
            for (Receipt receipt : receipts) {
                summaries.add(toConsentSummaryDTO(receipt));
            }
        }

        result.setTotalResults(summaries.size());
        result.setLinks(links);
        result.setConsents(summaries);
        return result;
    }

    /**
     * Revokes a consent receipt.
     *
     * @param receiptId Receipt ID.
     * @throws ConsentManagementException if revocation fails.
     */
    public void revokeConsent(String receiptId) {

        try {
            Receipt receipt = consentManager.getReceiptWithExtendedSchema(receiptId);
            String currentState = StringUtils.isNotBlank(receipt.getState()) ? receipt.getState() : ACTIVE_STATE;
            if (REVOKE_STATE.equals(currentState)) {
                return;
            }
            String callingUser = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
            consentManager.authorizeConsent(receiptId, callingUser, REVOKE_STATE);
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    private ReceiptInput buildReceiptInput(ConsentCreateRequest request, String subjectId)
            throws ConsentManagementException {

        List<PurposePIICategoryBinding> purposeBindings = new ArrayList<>();
        if (request.getPurposes() != null) {
            for (ConsentPurposeBinding purposeBinding : request.getPurposes()) {
                List<PIICategory> piiCategories = new ArrayList<>();
                if (purposeBinding.getElements() != null) {
                    for (ElementTerminationInfo elementInfo : purposeBinding.getElements()) {
                        String elementId = elementInfo.getId().toString();
                        PIICategory piiCategory = consentManager.getPIICategoryByUuid(elementId);
                        if (piiCategory == null) {
                            throw handleClientException(ERROR_CODE_ELEMENT_UUID_NOT_FOUND, elementId);
                        }
                        piiCategories.add(piiCategory);
                    }
                }
                purposeBindings.add(new PurposePIICategoryBinding(
                        purposeBinding.getId().toString(), piiCategories));
            }
        }

        boolean rejected = ConsentCreateRequest.StateEnum.REJECTED.equals(request.getState());
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        Long expiryMillis = request.getExpiryTime();
        Timestamp expiryTime = expiryMillis != null ? new Timestamp(expiryMillis) : null;
        List<String> authorizationUserIds = null;
        if (request.getAuthorizations() != null) {
            authorizationUserIds = new ArrayList<>();
            for (AuthorizationEntry entry : request.getAuthorizations()) {
                authorizationUserIds.add(entry.getId());
            }
        }
        return ConsentReceiptUtils.buildReceiptInput(request.getLanguage(), subjectId, tenantDomain,
                expiryTime, rejected, authorizationUserIds, request.getProperties(),
                request.getServiceId(), purposeBindings, consentManager);
    }

    private ConsentDTO toConsentDTO(Receipt receipt) throws ConsentManagementException {

        ConsentDTO dto = new ConsentDTO();
        dto.setId(receipt.getConsentReceiptId());
        dto.setTimestamp(receipt.getConsentTimestamp());
        dto.setLanguage(receipt.getLanguage());
        dto.setSubjectId(receipt.getPiiPrincipalId());
        String state = StringUtils.isNotBlank(receipt.getState()) ? receipt.getState() : ACTIVE_STATE;
        dto.setState(ConsentDTO.StateEnum.fromValue(state));
        dto.setExpiryTime(receipt.getExpiryTime() != null ? receipt.getExpiryTime().getTime() : null);

        // Extract service name and purposes from the first service entry (V2 is single-service).
        List<ConsentedPurposeDTO> purposeDTOs = new ArrayList<>();
        List<ReceiptService> services = receipt.getServices();
        if (services != null && !services.isEmpty()) {
            ReceiptService receiptService = services.get(0);
            dto.setServiceId(receiptService.getService());

            if (receiptService.getPurposes() != null) {
                for (ConsentPurpose consentPurpose : receiptService.getPurposes()) {
                    purposeDTOs.add(toConsentedPurposeDTO(consentPurpose));
                }
            }
        }
        dto.setPurposes(purposeDTOs);

        dto.setProperties(receipt.getProperties());

        // Populate authorizations from backend
        List<ConsentAuthorization> auths = consentManager.getConsentAuthorizations(receipt.getConsentReceiptId());
        List<AuthorizationDTO> authDTOs = new ArrayList<>();
        if (auths != null) {
            for (ConsentAuthorization auth : auths) {
                // Skip PENDING authorizations — they are not exposed in the API response DTO
                if (PENDING_STATE.equals(auth.getStatus().name())) {
                    continue;
                }
                try {
                    AuthorizationDTO.StateEnum stateEnum =
                            AuthorizationDTO.StateEnum.fromValue(auth.getStatus().name());
                    AuthorizationDTO authDTO = new AuthorizationDTO();
                    authDTO.setUserId(auth.getUserId());
                    authDTO.setState(stateEnum);
                    authDTO.setUpdatedTime(auth.getUpdatedTime());
                    authDTOs.add(authDTO);
                } catch (IllegalArgumentException e) {
                    // Skip unrecognized authorization states
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Skipping unrecognized authorization state: " + auth.getStatus(), e);
                    }
                }
            }
        }
        dto.setAuthorizations(authDTOs);
        return dto;
    }

    private ConsentedPurposeDTO toConsentedPurposeDTO(ConsentPurpose consentPurpose) {

        ConsentedPurposeDTO dto = new ConsentedPurposeDTO();
        dto.setName(consentPurpose.getPurpose());

        // Resolve purpose int ID to UUID, and populate version label using the fetched purpose.
        String versionUuid = consentPurpose.getPurposeVersionId();
        try {
            Purpose purpose = consentManager.getPurposeByUuid(consentPurpose.getUuid());
            if (purpose == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Could not resolve purpose UUID for purposeId: " + consentPurpose.getUuid());
                }
            } else {
                if (StringUtils.isNotBlank(purpose.getUuid())) {
                    dto.setId(purpose.getUuid());
                }
                if (StringUtils.isNotBlank(purpose.getGroupType())) {
                    dto.setType(purpose.getGroupType());
                }
                if (StringUtils.isNotBlank(versionUuid)) {
                    dto.setVersionId(versionUuid);
                    PurposeVersion latestVersion = purpose.getLatestVersion();
                    if (latestVersion != null && versionUuid.equals(latestVersion.getUuid())) {
                        dto.setVersion(latestVersion.getVersion());
                        dto.setProperties(latestVersion.getProperties());
                    } else if (dto.getId() != null) {
                        PurposeVersion pv = consentManager.getPurposeVersion(dto.getId(), versionUuid);
                        if (pv != null) {
                            dto.setVersion(pv.getVersion());
                            dto.setProperties(pv.getProperties());
                        }
                    }
                }
            }
        } catch (ConsentManagementException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not resolve purpose UUID for purposeId: " + consentPurpose.getPurposeId(), e);
            }
        }

        List<ConsentedElementDTO> elementDTOs = new ArrayList<>();
        if (consentPurpose.getPiiCategory() != null) {
            for (PIICategoryValidity piiCategoryValidity : consentPurpose.getPiiCategory()) {
                ConsentedElementDTO elementDTO = new ConsentedElementDTO();
                elementDTO.setName(piiCategoryValidity.getName());
                elementDTO.setDisplayName(piiCategoryValidity.getDisplayName());
                // Resolve element int ID to UUID.
                try {
                    PIICategory element = consentManager.getPIICategoryByUuid(piiCategoryValidity.getUuid());
                    if (element == null) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Could not resolve element UUID for elementId: " + piiCategoryValidity.getUuid());
                        }
                    } else {
                        if (StringUtils.isNotBlank(element.getUuid())) {
                            elementDTO.setId(element.getUuid());
                        }
                    }
                } catch (ConsentManagementException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not resolve element UUID for elementId: " + piiCategoryValidity.getId(), e);
                    }
                }
                elementDTOs.add(elementDTO);
            }
        }
        dto.setElements(elementDTOs);
        return dto;
    }

    /**
     * Authorizes (approves/rejects) a consent receipt.
     *
     * @param consentId Consent receipt ID.
     * @param request   Authorization create request.
     * @return Response with AuthorizationDTO.
     * @throws ConsentManagementException if authorization fails.
     */
    public AuthorizationDTO authorizeConsent(String consentId, AuthorizationCreateRequest request) {

        try {
            Receipt receipt = consentManager.getReceiptWithExtendedSchema(consentId);
            String currentState = StringUtils.isNotBlank(receipt.getState()) ? receipt.getState() : ACTIVE_STATE;
            if (!PENDING_STATE.equals(currentState)) {
                throw handleClientException(ERROR_CODE_CONSENT_INVALID_STATE_FOR_AUTHORIZE, consentId);
            }

            PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            String callingUser = carbonContext.getUsername();
            String authStatus = request.getState() != null ? request.getState().toString() : "APPROVED";

            List<ConsentAuthorization> existing = consentManager.getConsentAuthorizations(consentId);
            boolean callingUserInList = existing.stream().anyMatch(a -> callingUser.equals(a.getUserId()));
            if (!callingUserInList) {
                throw handleClientException(ERROR_CODE_CONSENT_USER_NOT_IN_AUTHORIZATION_LIST, callingUser);
            }

            consentManager.authorizeConsent(consentId, callingUser, authStatus);

            List<ConsentAuthorization> all = consentManager.getConsentAuthorizations(consentId);
            ConsentAuthorization updated = all.stream()
                    .filter(a -> callingUser.equals(a.getUserId()))
                    .findFirst()
                    .orElse(null);

            AuthorizationDTO dto = new AuthorizationDTO();
            dto.setUserId(callingUser);
            if (updated != null) {
                dto.setState(AuthorizationDTO.StateEnum.fromValue(updated.getStatus().name()));
                dto.setUpdatedTime(updated.getUpdatedTime());
            }
            return dto;
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    /**
     * Validates the current status of a consent receipt.
     *
     * @param consentId Consent receipt ID.
     * @return Response with ConsentValidateResponse.
     * @throws ConsentManagementException if validation fails.
     */
    public ConsentValidateResponse validateConsent(String consentId) {

        try {
            String status = consentManager.validateConsentStatus(consentId);

            ConsentValidateResponse validateResponse = new ConsentValidateResponse();
            validateResponse.setState(ConsentValidateResponse.StateEnum.fromValue(status));

            Receipt receipt = consentManager.getReceiptWithExtendedSchema(consentId);
            if (receipt != null) {
                validateResponse.setExpiryTime(
                        receipt.getExpiryTime() != null ? receipt.getExpiryTime().getTime() : null);
            }
            return validateResponse;
        } catch (ConsentManagementException e) {
            throw ConsentMgtEndpointUtil.handleConsentManagementException(e);
        }
    }

    private ConsentSummaryDTO toConsentSummaryDTO(Receipt receipt) {

        ConsentSummaryDTO dto = new ConsentSummaryDTO();
        dto.setId(receipt.getConsentReceiptId());
        dto.setSubjectId(receipt.getPiiPrincipalId());
        String state = StringUtils.isNotBlank(receipt.getState()) ? receipt.getState() : ACTIVE_STATE;
        dto.setState(ConsentSummaryDTO.StateEnum.fromValue(state));
        dto.setTimestamp(receipt.getConsentTimestamp());
        dto.setExpiryTime(receipt.getExpiryTime() != null ? receipt.getExpiryTime().getTime() : null);
        if (receipt.getServices() != null && !receipt.getServices().isEmpty()) {
            dto.setServiceId(receipt.getServices().get(0).getService());
        }
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
                        ConsentManagementConstants.CONSENTS_PATH + url).toString())
                .rel(rel);
    }
}
