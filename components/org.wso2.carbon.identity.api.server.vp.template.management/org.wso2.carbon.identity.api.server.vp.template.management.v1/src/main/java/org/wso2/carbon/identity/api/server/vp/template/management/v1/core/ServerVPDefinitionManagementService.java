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

package org.wso2.carbon.identity.api.server.vp.template.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.openid4vc.presentation.common.constant.OpenID4VPConstants;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.CertificatePatch;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.Error;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionList;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionListItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ClaimConstraintModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedConnectionItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedConnectionsResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.RequestedCredentialModel;
import org.wso2.carbon.identity.openid4vc.presentation.management.exception.PresentationManagementClientException;
import org.wso2.carbon.identity.openid4vc.presentation.management.exception.PresentationManagementErrorCode;
import org.wso2.carbon.identity.openid4vc.presentation.management.exception.PresentationManagementException;
import org.wso2.carbon.identity.openid4vc.presentation.management.model.ConnectedConnectionInfo;
import org.wso2.carbon.identity.openid4vc.presentation.management.model.PresentationDefinition;
import org.wso2.carbon.identity.openid4vc.presentation.management.model.PresentationDefinition.ClaimConstraint;
import org.wso2.carbon.identity.openid4vc.presentation.management.model.PresentationDefinition.RequestedCredential;
import org.wso2.carbon.identity.openid4vc.presentation.management.model.PresentationDefinitionSearchResult;
import org.wso2.carbon.identity.openid4vc.presentation.management.service.PresentationDefinitionService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for VP Presentation Definition Management API.
 * Handles business logic, model conversion, and error mapping.
 */
public class ServerVPDefinitionManagementService {

    /**
     * List presentation definitions with cursor-based pagination and optional filtering.
     *
     * @param before Base64-encoded backward cursor, or null.
     * @param after  Base64-encoded forward cursor, or null.
     * @param filter SCIM-style filter expression, or null.
     * @param limit  Maximum number of records to return.
     * @return PresentationDefinitionList with pagination links.
     */
    public PresentationDefinitionList listPresentationDefinitions(String before, String after,
            String filter, Integer limit) {

        PresentationDefinitionList result = new PresentationDefinitionList();
        try {
            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw handleClientError(ErrorMessage.ERROR_CODE_INVALID_INPUT, null,
                        Response.Status.BAD_REQUEST, "Both 'before' and 'after' parameters cannot be provided.");
            }

            int resolvedLimit = (limit != null && limit > 0)
                    ? Math.min(limit, VPDefinitionManagementConstants.MAX_LIMIT)
                    : VPDefinitionManagementConstants.DEFAULT_LIMIT;
            String sortOrder = StringUtils.isNotBlank(before)
                    ? VPDefinitionManagementConstants.DESC_SORT_ORDER
                    : VPDefinitionManagementConstants.ASC_SORT_ORDER;

            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinitionSearchResult searchResult =
                    service.listWithPagination(after, before, resolvedLimit + 1, filter, sortOrder, tenantId);

            List<PresentationDefinition> definitions = searchResult.getDefinitions();

            if (definitions == null || definitions.isEmpty()) {
                result.setTotalResults(0);
                result.setPresentationDefinitions(new ArrayList<>());
                return result;
            }

            boolean hasMoreItems = definitions.size() > resolvedLimit;
            boolean needsReverse = StringUtils.isNotBlank(before);
            boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after))
                    || (StringUtils.isNotBlank(before) && !hasMoreItems);
            boolean isLastPage = !hasMoreItems
                    && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

            String urlBase = "?limit=" + resolvedLimit;
            if (StringUtils.isNotBlank(filter)) {
                try {
                    urlBase += "&filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    // UTF-8 is always supported; ignore.
                }
            }

            List<PresentationDefinition> pageItems = new ArrayList<>(definitions);
            if (hasMoreItems) {
                pageItems.remove(pageItems.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(pageItems);
            }
            if (!isFirstPage && pageItems.get(0).getCursorKey() != null) {
                String encoded = Base64.getEncoder().encodeToString(
                        pageItems.get(0).getCursorKey().toString().getBytes(StandardCharsets.UTF_8));
                result.addLinksItem(buildPaginationLink(urlBase + "&before=" + encoded, "previous"));
            }
            if (!isLastPage && pageItems.get(pageItems.size() - 1).getCursorKey() != null) {
                String encoded = Base64.getEncoder().encodeToString(
                        pageItems.get(pageItems.size() - 1).getCursorKey()
                                .toString().getBytes(StandardCharsets.UTF_8));
                result.addLinksItem(buildPaginationLink(urlBase + "&after=" + encoded, "next"));
            }

            result.setTotalResults(searchResult.getTotalCount());
            result.setPresentationDefinitions(pageItems.stream()
                    .filter(Objects::nonNull)
                    .map(this::toListItem)
                    .collect(Collectors.toList()));
            return result;
        } catch (javax.ws.rs.WebApplicationException e) {
            throw e;
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_LISTING_DEFINITIONS, e);
        }
    }

    private PaginationLink buildPaginationLink(String href, String rel) {

        PaginationLink link = new PaginationLink();
        link.setRel(rel);
        link.setHref(VPDefinitionManagementConstants.VP_DEFINITION_MANAGEMENT_PATH_COMPONENT + href);
        return link;
    }

    /**
     * Create a new presentation definition.
     *
     * @param creationModel The creation model
     * @return Created PresentationDefinitionResponse
     */
    public PresentationDefinitionResponse createPresentationDefinition(
            PresentationDefinitionCreationModel creationModel) {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            String definitionId = UUID.randomUUID().toString();

            PresentationDefinition definition = new PresentationDefinition.Builder()
                    .definitionId(definitionId)
                    .name(creationModel.getName())
                    .description(creationModel.getDescription())
                    .requestedCredentials(toRequestedCredentials(creationModel.getCredentials()))
                    .tenantId(tenantId)
                    .build();

            PresentationDefinition created = service.createPresentationDefinition(definition, tenantId);
            return toResponse(created);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_ALREADY_EXISTS == e.getErrorCode()) {
                throw handleClientError(ErrorMessage.ERROR_CODE_DEFINITION_ALREADY_EXISTS, e,
                        Response.Status.CONFLICT);
            }
            if (PresentationManagementErrorCode.VALIDATION_ERROR == e.getErrorCode()) {
                throw handleClientError(ErrorMessage.ERROR_CODE_INVALID_INPUT, e,
                        Response.Status.BAD_REQUEST, e.getMessage());
            }
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_CREATING_DEFINITION, e);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_CREATING_DEFINITION, e);
        }
    }

    /**
     * Get a presentation definition by ID.
     *
     * @param definitionId The definition ID
     * @return PresentationDefinitionResponse
     */
    public PresentationDefinitionResponse getPresentationDefinition(String definitionId) {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinition definition = service.getPresentationDefinitionById(
                    definitionId, tenantId);
            return toResponse(definition);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND == e.getErrorCode()) {
                throw handleNotFound(definitionId);
            }
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEFINITION, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEFINITION, e,
                    definitionId);
        }
    }

    /**
     * Update a presentation definition.
     *
     * @param definitionId The definition ID
     * @param updateModel  The update model
     * @return Updated PresentationDefinitionResponse
     */
    public PresentationDefinitionResponse updatePresentationDefinition(
            String definitionId, PresentationDefinitionUpdateModel updateModel) {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            List<RequestedCredential> credentials = updateModel.getCredentials() != null
                    ? toRequestedCredentials(updateModel.getCredentials())
                    : null;

            PresentationDefinition definition = new PresentationDefinition.Builder()
                    .definitionId(definitionId)
                    .name(updateModel.getName())
                    .description(updateModel.getDescription())
                    .requestedCredentials(credentials)
                    .tenantId(tenantId)
                    .build();

            PresentationDefinition updated = service.updatePresentationDefinition(
                    definition, tenantId);
            return toResponse(updated);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND == e.getErrorCode()) {
                throw handleNotFound(definitionId);
            }
            if (PresentationManagementErrorCode.VALIDATION_ERROR == e.getErrorCode()) {
                throw handleClientError(ErrorMessage.ERROR_CODE_INVALID_INPUT, e,
                        Response.Status.BAD_REQUEST, e.getMessage());
            }
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e,
                    definitionId);
        }
    }

    /**
     * Delete a presentation definition.
     *
     * @param definitionId The definition ID
     */
    public void deletePresentationDefinition(String definitionId) {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            service.deletePresentationDefinition(definitionId, tenantId);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND == e.getErrorCode()) {
                throw handleNotFound(definitionId);
            }
            if (PresentationManagementErrorCode.DEFINITION_IN_USE == e.getErrorCode()) {
                throw handleClientError(ErrorMessage.ERROR_CODE_DEFINITION_IN_USE, e,
                        Response.Status.CONFLICT, definitionId);
            }
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_DELETING_DEFINITION, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_DELETING_DEFINITION, e,
                    definitionId);
        }
    }

    /**
     * Get all connections that reference this presentation definition.
     *
     * @param definitionId The definition ID
     * @return ConnectedConnectionsResponse with list of connections
     */
    public ConnectedConnectionsResponse getConnectedConnections(String definitionId) {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            List<ConnectedConnectionInfo> connections =
                    service.getConnectedConnections(definitionId, tenantId);

            String serverUrl = IdentityUtil.getServerURL(
                    "/api/server/v1/identity-providers/", true, true);

            List<ConnectedConnectionItem> items = new ArrayList<>();
            for (ConnectedConnectionInfo info : (connections != null ? connections : Collections.emptyList())) {
                ConnectedConnectionItem item = new ConnectedConnectionItem();
                item.setConnectionId(info.getConnectionId());
                item.setName(info.getConnectionName());
                item.setSelf(serverUrl + info.getConnectionId());
                items.add(item);
            }

            ConnectedConnectionsResponse response = new ConnectedConnectionsResponse();
            response.setCount(items.size());
            response.setTotalResults(items.size());
            response.setStartIndex(1);
            response.setConnectedConnections(items);
            return response;
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND == e.getErrorCode()) {
                throw handleNotFound(definitionId);
            }
            throw handleServerError(
                    ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONNECTED_CONNECTIONS, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(
                    ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONNECTED_CONNECTIONS, e, definitionId);
        }
    }

    /**
     * Patch trusted CA certificates for a specific credential within a presentation definition.
     *
     * @param definitionId The definition ID
     * @param credentialId The credential query ID
     * @param patchRequest List of patch operations
     * @return Updated PresentationDefinitionResponse
     */
    public PresentationDefinitionResponse patchTrustedCas(String definitionId, String credentialId,
            List<CertificatePatch> patchRequest) {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinition definition = service.getPresentationDefinitionById(definitionId, tenantId);
            if (definition == null) {
                throw handleNotFound(definitionId);
            }

            List<RequestedCredential> credentials = definition.getRequestedCredentials();
            if (credentials == null) {
                throw handleNotFound(definitionId);
            }

            RequestedCredential target = null;
            for (RequestedCredential credential : credentials) {
                if (credentialId.equals(credential.getCredentialId())) {
                    target = credential;
                    break;
                }
            }
            if (target == null) {
                throw handleNotFound(credentialId);
            }

            if (patchRequest == null || patchRequest.isEmpty()) {
                throw new javax.ws.rs.WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(buildError("VPD-60005", "Patch request must not be empty."))
                                .build());
            }

            List<String> existingCas = target.getTrustedCas();
            List<String> updatedTrustedCas = new ArrayList<>(existingCas != null ? existingCas : Collections.emptyList());
            for (CertificatePatch patch : patchRequest) {
                switch (patch.getOperation()) {
                    case ADD:
                        if (StringUtils.isBlank(patch.getCertificate())) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "Certificate is required for ADD operation."))
                                            .build());
                        }
                        try {
                            String rawPemToAdd = new String(
                                    Base64.getDecoder().decode(patch.getCertificate()),
                                    StandardCharsets.UTF_8);
                            updatedTrustedCas.add(rawPemToAdd);
                        } catch (IllegalArgumentException e) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "Certificate is not valid Base64."))
                                            .build());
                        }
                        break;
                    case REMOVE:
                        if (patch.getCertificateIndex() == null) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "certificateIndex is required for REMOVE operation."))
                                            .build());
                        }
                        int removeIdx = patch.getCertificateIndex();
                        if (removeIdx < 0 || removeIdx >= updatedTrustedCas.size()) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "Invalid certificate index: " + removeIdx))
                                            .build());
                        }
                        updatedTrustedCas.remove(removeIdx);
                        break;
                    case REPLACE:
                        if (patch.getCertificateIndex() == null) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "certificateIndex is required for REPLACE operation."))
                                            .build());
                        }
                        if (StringUtils.isBlank(patch.getCertificate())) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "Certificate is required for REPLACE operation."))
                                            .build());
                        }
                        int replaceIdx = patch.getCertificateIndex();
                        if (replaceIdx < 0 || replaceIdx >= updatedTrustedCas.size()) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "Invalid certificate index: " + replaceIdx))
                                            .build());
                        }
                        try {
                            String rawPemToReplace = new String(
                                    Base64.getDecoder().decode(patch.getCertificate()),
                                    StandardCharsets.UTF_8);
                            updatedTrustedCas.set(replaceIdx, rawPemToReplace);
                        } catch (IllegalArgumentException e) {
                            throw new javax.ws.rs.WebApplicationException(
                                    Response.status(Response.Status.BAD_REQUEST)
                                            .entity(buildError("VPD-60005",
                                                    "Certificate is not valid Base64."))
                                            .build());
                        }
                        break;
                    default:
                        break;
                }
            }
            target.setTrustedCas(updatedTrustedCas);

            PresentationDefinition updated = service.updatePresentationDefinition(definition, tenantId);
            return toResponse(updated);
        } catch (javax.ws.rs.WebApplicationException e) {
            throw e;
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND == e.getErrorCode()) {
                throw handleNotFound(definitionId);
            }
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e, definitionId);
        }
    }

    // --- Conversion helpers ---

    /**
     * Convert API RequestedCredentialModel list to domain RequestedCredential list.
     */
    private List<RequestedCredential> toRequestedCredentials(
            List<RequestedCredentialModel> apiModels) throws PresentationManagementClientException {

        if (apiModels == null) {
            return null;
        }
        List<RequestedCredential> result = new ArrayList<>();
        for (RequestedCredentialModel apiModel : apiModels) {
            RequestedCredential requestedCredential = new RequestedCredential();
            requestedCredential.setCredentialId(apiModel.getId());
            requestedCredential.setType(apiModel.getType());
            requestedCredential.setFormat(apiModel.getFormat() != null ? apiModel.getFormat() : "dc+sd-jwt");
            requestedCredential.setEnforceTrustedIssuer(Boolean.TRUE.equals(apiModel.getEnforceTrustedIssuer()));
            requestedCredential.setTrustedCas(decodeBase64PemList(apiModel.getTrustedCaPems()));
            requestedCredential.setKeyResolutionMethod(apiModel.getKeyResolutionMethod() != null ? apiModel.getKeyResolutionMethod() : "x5c");
            requestedCredential.setJwksUri(apiModel.getJwksUri());
            requestedCredential.setIssuerPem(apiModel.getIssuerPem());
            requestedCredential.setClaims(toClaimConstraints(apiModel.getClaims()));
            result.add(requestedCredential);
        }
        return result;
    }

    /**
     * Convert domain RequestedCredential list to API RequestedCredentialModel list.
     */
    private List<RequestedCredentialModel> toCredentialModels(
            List<RequestedCredential> domainCredentials) {

        if (domainCredentials == null) {
            return null;
        }
        List<RequestedCredentialModel> result = new ArrayList<>();
        for (RequestedCredential requestedCredential : domainCredentials) {
            RequestedCredentialModel model = new RequestedCredentialModel();
            model.setId(requestedCredential.getCredentialId());
            model.setType(requestedCredential.getType());
            model.setFormat(requestedCredential.getFormat());
            model.setEnforceTrustedIssuer(requestedCredential.isEnforceTrustedIssuer());
            model.setTrustedCaPems(encodeBase64PemList(requestedCredential.getTrustedCas()));
            model.setKeyResolutionMethod(requestedCredential.getKeyResolutionMethod());
            model.setJwksUri(requestedCredential.getJwksUri());
            model.setIssuerPem(requestedCredential.getIssuerPem());
            model.setClaims(toClaimConstraintModels(requestedCredential.getClaims()));
            result.add(model);
        }
        return result;
    }

    private List<ClaimConstraint> toClaimConstraints(List<ClaimConstraintModel> apiModels) {

        if (apiModels == null) {
            return null;
        }
        List<ClaimConstraint> result = new ArrayList<>();
        for (ClaimConstraintModel claimConstraintModel : apiModels) {
            ClaimConstraint claimConstraint = new ClaimConstraint();
            claimConstraint.setId(claimConstraintModel.getId());
            claimConstraint.setPath(claimConstraintModel.getPath());
            claimConstraint.setMandatory(Boolean.TRUE.equals(
                    claimConstraintModel.getMandatory() == null ? Boolean.TRUE : claimConstraintModel.getMandatory()));
            claimConstraint.setAllowedValues(claimConstraintModel.getAllowedValues());
            result.add(claimConstraint);
        }
        return result;
    }

    private List<String> decodeBase64PemList(List<String> base64Pems) {

        if (base64Pems == null) {
            return null;
        }
        List<String> rawPems = new ArrayList<>();
        for (String encodedPem : base64Pems) {
            if (StringUtils.isNotBlank(encodedPem)) {
                rawPems.add(new String(Base64.getDecoder().decode(encodedPem), StandardCharsets.UTF_8));
            }
        }
        return rawPems;
    }

    private List<String> encodeBase64PemList(List<String> rawPems) {

        if (rawPems == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (String pem : rawPems) {
            if (StringUtils.isNotBlank(pem)) {
                result.add(Base64.getEncoder().encodeToString(pem.getBytes(StandardCharsets.UTF_8)));
            }
        }
        return result;
    }

    private List<ClaimConstraintModel> toClaimConstraintModels(List<ClaimConstraint> domainConstraints) {

        if (domainConstraints == null) {
            return null;
        }
        List<ClaimConstraintModel> result = new ArrayList<>();
        for (ClaimConstraint claimConstraint : domainConstraints) {
            ClaimConstraintModel claimConstraintModel = new ClaimConstraintModel();
            claimConstraintModel.setId(claimConstraint.getId());
            claimConstraintModel.setPath(claimConstraint.getPath());
            claimConstraintModel.setMandatory(claimConstraint.isMandatory());
            claimConstraintModel.setAllowedValues(claimConstraint.getAllowedValues());
            result.add(claimConstraintModel);
        }
        return result;
    }

    private PresentationDefinitionResponse toResponse(PresentationDefinition definition) {

        PresentationDefinitionResponse response = new PresentationDefinitionResponse();
        response.setId(definition.getDefinitionId());
        response.setName(definition.getName());
        response.setDescription(definition.getDescription());
        response.setCredentials(toCredentialModels(definition.getRequestedCredentials()));
        return response;
    }

    private PresentationDefinitionListItem toListItem(PresentationDefinition definition) {

        PresentationDefinitionListItem item = new PresentationDefinitionListItem();
        item.setId(definition.getDefinitionId());
        item.setName(definition.getName());
        item.setDescription(definition.getDescription());
        return item;
    }

    // --- Error handling ---

    private javax.ws.rs.WebApplicationException handleNotFound(String definitionId) {

        Error error = new Error();
        error.setCode(ErrorMessage.ERROR_CODE_DEFINITION_NOT_FOUND.getCode());
        error.setMessage(ErrorMessage.ERROR_CODE_DEFINITION_NOT_FOUND.getMessage());
        error.setDescription(
                String.format(ErrorMessage.ERROR_CODE_DEFINITION_NOT_FOUND.getDescription(),
                        definitionId));
        return new javax.ws.rs.WebApplicationException(
                Response.status(Response.Status.NOT_FOUND).entity(error).build());
    }

    private javax.ws.rs.WebApplicationException handleClientError(
            ErrorMessage errorMessage, Exception e, Response.Status status, String... args) {

        Error error = new Error();
        error.setCode(errorMessage.getCode());
        error.setMessage(errorMessage.getMessage());
        error.setDescription(args.length > 0
                ? String.format(errorMessage.getDescription(), (Object[]) args)
                : errorMessage.getDescription());
        return new javax.ws.rs.WebApplicationException(
                Response.status(status).entity(error).build());
    }

    private javax.ws.rs.WebApplicationException handleServerError(
            ErrorMessage errorMessage, Exception e, String... args) {

        Error error = new Error();
        error.setCode(errorMessage.getCode());
        error.setMessage(errorMessage.getMessage());
        error.setDescription(args.length > 0
                ? String.format(errorMessage.getDescription(), (Object[]) args)
                : errorMessage.getDescription());
        return new javax.ws.rs.WebApplicationException(
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build());
    }

    // --- Utility ---

    private int getTenantId() {

        return ContextLoader.getTenantDomainFromContext() != null
                ? org.wso2.carbon.context.PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getTenantId()
                : -1234; // Super-tenant default
    }

    private PresentationDefinitionService getService() {

        if (!Boolean.parseBoolean(IdentityUtil.getProperty(OpenID4VPConstants.ConfigKeys.FEATURE_ENABLED))) {
            throw buildFeatureDisabledError();
        }
        PresentationDefinitionService service =
                VPDefinitionManagementServiceHolder.getPresentationDefinitionService();
        if (service == null) {
            throw buildFeatureDisabledError();
        }
        return service;
    }

    private Error buildError(String code, String message) {

        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;
    }

    private javax.ws.rs.WebApplicationException buildFeatureDisabledError() {

        Error error = new Error();
        error.setCode("VPD-60004");
        error.setMessage("OpenID4VP feature is not enabled.");
        error.setDescription(
                "The OpenID4VP feature is disabled. Enable it via [openid4vp] enabled=true in deployment.toml.");
        return new javax.ws.rs.WebApplicationException(
                Response.status(Response.Status.NOT_IMPLEMENTED).entity(error).build());
    }
}
