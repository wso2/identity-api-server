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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.CertificatePatch;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ClaimConstraintModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpsResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.Error;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionList;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionListItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.RequestedCredentialModel;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.openid4vc.presentation.common.constant.VPConstants;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementClientException;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementErrorCode;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementException;
import org.wso2.carbon.identity.openid4vc.template.management.model.ConnectedIdpInfo;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinition;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinition.ClaimConstraint;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinition.RequestedCredential;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinitionSearchResult;
import org.wso2.carbon.identity.openid4vc.template.management.service.PresentationDefinitionService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for VP Presentation Definition Management API.
 * Handles business logic, model conversion, and error mapping.
 */
public class ServerVPDefinitionManagementService {

    private static final Log LOG = LogFactory.getLog(ServerVPDefinitionManagementService.class);

    /**
     * List presentation definitions with cursor-based pagination and optional filtering.
     *
     * @param before base64-encoded backward cursor for reverse pagination; {@code null} for forward direction.
     * @param after  base64-encoded forward cursor from the previous page's "next" link; {@code null} to start from
     *               the beginning.
     * @param filter a SCIM-style filter expression to narrow results; {@code null} to return all definitions.
     * @param limit  maximum number of records per page; capped at {@code MAX_LIMIT}.
     * @return the paginated list of presentation definitions with cursor-based navigation links.
     */
    public PresentationDefinitionList listPresentationDefinitions(String before, String after,
            String filter, Integer limit) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing presentation definitions.");
        }
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

            String urlBase = VPDefinitionManagementConstants.PARAM_LIMIT + resolvedLimit;
            if (StringUtils.isNotBlank(filter)) {
                try {
                    urlBase += VPDefinitionManagementConstants.PARAM_FILTER
                            + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
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
                result.addLinksItem(buildPaginationLink(
                        urlBase + VPDefinitionManagementConstants.PARAM_BEFORE + encoded,
                        VPDefinitionManagementConstants.LINK_REL_PREVIOUS));
            }
            if (!isLastPage && pageItems.get(pageItems.size() - 1).getCursorKey() != null) {
                String encoded = Base64.getEncoder().encodeToString(
                        pageItems.get(pageItems.size() - 1).getCursorKey()
                                .toString().getBytes(StandardCharsets.UTF_8));
                result.addLinksItem(buildPaginationLink(
                        urlBase + VPDefinitionManagementConstants.PARAM_AFTER + encoded,
                        VPDefinitionManagementConstants.LINK_REL_NEXT));
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
     * @param creationModel the request model containing the name, description, and credential constraints.
     * @return the newly created presentation definition with its server-assigned ID.
     */
    public PresentationDefinitionResponse createPresentationDefinition(
            PresentationDefinitionCreationModel creationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating presentation definition.");
        }
        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinition definition = new PresentationDefinition.Builder()
                    .identifier(creationModel.getIdentifier())
                    .displayName(creationModel.getDisplayName())
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
     * Get a presentation definition by UUID.
     *
     * @param definitionId the server-generated UUID of the definition to retrieve.
     * @return the matching presentation definition.
     */
    public PresentationDefinitionResponse getPresentationDefinition(String definitionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinition definition =
                    service.getPresentationDefinitionById(definitionId, tenantId);
            if (definition == null) {
                throw handleNotFound(definitionId);
            }
            return toResponse(definition);
        } catch (javax.ws.rs.WebApplicationException e) {
            throw e;
        } catch (PresentationManagementClientException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEFINITION, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEFINITION, e, definitionId);
        }
    }

    /**
     * Update a presentation definition.
     *
     * @param definitionId the server-generated UUID of the definition to update.
     * @param updateModel  the model containing the fields to replace.
     * @return the updated presentation definition.
     */
    public PresentationDefinitionResponse updatePresentationDefinition(
            String definitionId, PresentationDefinitionUpdateModel updateModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinition existing =
                    service.getPresentationDefinitionById(definitionId, tenantId);
            if (existing == null) {
                throw handleNotFound(definitionId);
            }

            List<RequestedCredential> credentials = updateModel.getCredentials() != null
                    ? toRequestedCredentials(updateModel.getCredentials())
                    : null;

            PresentationDefinition definition = new PresentationDefinition.Builder()
                    .definitionId(existing.getDefinitionId())
                    .displayName(updateModel.getDisplayName())
                    .description(updateModel.getDescription())
                    .requestedCredentials(credentials)
                    .tenantId(tenantId)
                    .build();

            PresentationDefinition updated = service.updatePresentationDefinition(
                    definition, tenantId);
            return toResponse(updated);
        } catch (javax.ws.rs.WebApplicationException e) {
            throw e;
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
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e, definitionId);
        }
    }

    /**
     * Delete a presentation definition.
     *
     * @param definitionId the server-generated UUID of the definition to delete.
     */
    public void deletePresentationDefinition(String definitionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            service.deletePresentationDefinition(definitionId, tenantId);
        } catch (javax.ws.rs.WebApplicationException e) {
            throw e;
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
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_DELETING_DEFINITION, e, definitionId);
        }
    }

    /**
     * Get all connections that reference this presentation definition.
     *
     * @param definitionId the server-generated UUID of the definition to query.
     * @return the list of identity provider connections configured to use this definition.
     */
    public ConnectedIdpsResponse getConnectedIdps(String definitionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving connected connections for presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            List<ConnectedIdpInfo> idps =
                    service.getConnectedIdps(definitionId, tenantId);

            String serverUrl = IdentityUtil.getServerURL(
                    VPDefinitionManagementConstants.IDENTITY_PROVIDER_PATH_COMPONENT, true, true);

            List<ConnectedIdpInfo> effectiveIdps =
                    idps != null ? idps : Collections.<ConnectedIdpInfo>emptyList();
            List<ConnectedIdpItem> items = new ArrayList<>();
            for (ConnectedIdpInfo info : effectiveIdps) {
                ConnectedIdpItem item = new ConnectedIdpItem();
                item.setIdpId(info.getIdpId());
                item.setName(info.getIdpName());
                item.setSelf(serverUrl + info.getIdpId());
                items.add(item);
            }

            ConnectedIdpsResponse response = new ConnectedIdpsResponse();
            response.setCount(items.size());
            response.setTotalResults(items.size());
            response.setStartIndex(1);
            response.setConnectedIdps(items);
            return response;
        } catch (javax.ws.rs.WebApplicationException e) {
            throw e;
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
     * @param definitionId the server-generated UUID of the definition containing the credential.
     * @param credentialId the ID of the specific credential query within the definition.
     * @param patchRequest the list of ADD, REMOVE, or REPLACE operations to apply to the CA certificate list.
     * @return the updated presentation definition reflecting the CA certificate changes.
     */
    public PresentationDefinitionResponse patchTrustedCas(String definitionId, String credentialId,
            List<CertificatePatch> patchRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Patching trusted CAs for credential: " + credentialId
                    + " in presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            PresentationDefinition definition =
                    service.getPresentationDefinitionById(definitionId, tenantId);
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
                                .entity(buildError(ErrorMessage.ERROR_CODE_INVALID_PATCH_REQUEST.getCode(),
                                        "Patch request must not be empty."))
                                .build());
            }

            List<String> existingCas = target.getTrustedCas();
            List<String> updatedTrustedCas = new ArrayList<>(
                    existingCas != null ? existingCas : Collections.emptyList());
            for (CertificatePatch patch : patchRequest) {
                switch (patch.getOperation()) {
                    case ADD:
                        applyAddCertificatePatch(patch, updatedTrustedCas);
                        break;
                    case REMOVE:
                        applyRemoveCertificatePatch(patch, updatedTrustedCas);
                        break;
                    case REPLACE:
                        applyReplaceCertificatePatch(patch, updatedTrustedCas);
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
            requestedCredential.setFormat(apiModel.getFormat() != null
                    ? apiModel.getFormat() : VPDefinitionManagementConstants.DEFAULT_CREDENTIAL_FORMAT);
            requestedCredential.setEnforceTrustedIssuer(Boolean.TRUE.equals(apiModel.getEnforceTrustedIssuer()));
            requestedCredential.setTrustedCas(decodeBase64PemList(apiModel.getTrustedCaPems()));
            requestedCredential.setKeyResolutionMethod(
                    apiModel.getKeyResolutionMethod() != null
                            ? apiModel.getKeyResolutionMethod()
                            : VPDefinitionManagementConstants.DEFAULT_KEY_RESOLUTION_METHOD);
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
        response.setIdentifier(definition.getIdentifier());
        response.setDisplayName(definition.getDisplayName());
        response.setDescription(definition.getDescription());
        response.setCredentials(toCredentialModels(definition.getRequestedCredentials()));
        return response;
    }

    private PresentationDefinitionListItem toListItem(PresentationDefinition definition) {

        PresentationDefinitionListItem item = new PresentationDefinitionListItem();
        item.setId(definition.getDefinitionId());
        item.setIdentifier(definition.getIdentifier());
        item.setDisplayName(definition.getDisplayName());
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

        if (e != null && LOG.isDebugEnabled()) {
            LOG.debug("Client error [" + errorMessage.getCode() + "]: " + errorMessage.getMessage(), e);
        }
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

        LOG.error("Server error [" + errorMessage.getCode() + "]: " + errorMessage.getMessage(), e);
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

        if (!Boolean.parseBoolean(IdentityUtil.getProperty(VPConstants.ConfigKeys.FEATURE_ENABLED))) {
            throw buildFeatureDisabledError();
        }
        PresentationDefinitionService service =
                VPDefinitionManagementServiceHolder.getPresentationDefinitionService();
        if (service == null) {
            throw buildFeatureDisabledError();
        }
        return service;
    }

    private void applyAddCertificatePatch(CertificatePatch patch, List<String> trustedCas) {

        if (StringUtils.isBlank(patch.getCertificate())) {
            throw buildInvalidPatchRequestException("Certificate is required for ADD operation.");
        }
        try {
            trustedCas.add(new String(Base64.getDecoder().decode(patch.getCertificate()), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            throw buildInvalidPatchRequestException("Certificate is not valid Base64.");
        }
    }

    private void applyRemoveCertificatePatch(CertificatePatch patch, List<String> trustedCas) {

        if (patch.getCertificateIndex() == null) {
            throw buildInvalidPatchRequestException("certificateIndex is required for REMOVE operation.");
        }
        int index = patch.getCertificateIndex();
        if (index < 0 || index >= trustedCas.size()) {
            throw buildInvalidPatchRequestException("Invalid certificate index: " + index);
        }
        trustedCas.remove(index);
    }

    private void applyReplaceCertificatePatch(CertificatePatch patch, List<String> trustedCas) {

        if (patch.getCertificateIndex() == null) {
            throw buildInvalidPatchRequestException("certificateIndex is required for REPLACE operation.");
        }
        if (StringUtils.isBlank(patch.getCertificate())) {
            throw buildInvalidPatchRequestException("Certificate is required for REPLACE operation.");
        }
        int index = patch.getCertificateIndex();
        if (index < 0 || index >= trustedCas.size()) {
            throw buildInvalidPatchRequestException("Invalid certificate index: " + index);
        }
        try {
            trustedCas.set(index,
                    new String(Base64.getDecoder().decode(patch.getCertificate()), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            throw buildInvalidPatchRequestException("Certificate is not valid Base64.");
        }
    }

    private javax.ws.rs.WebApplicationException buildInvalidPatchRequestException(String description) {

        return new javax.ws.rs.WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(buildError(ErrorMessage.ERROR_CODE_INVALID_PATCH_REQUEST.getCode(), description))
                        .build());
    }

    private Error buildError(String code, String message) {

        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;
    }

    private javax.ws.rs.WebApplicationException buildFeatureDisabledError() {

        Error error = new Error();
        error.setCode(ErrorMessage.ERROR_CODE_FEATURE_DISABLED.getCode());
        error.setMessage(ErrorMessage.ERROR_CODE_FEATURE_DISABLED.getMessage());
        error.setDescription(ErrorMessage.ERROR_CODE_FEATURE_DISABLED.getDescription());
        return new javax.ws.rs.WebApplicationException(
                Response.status(Response.Status.NOT_IMPLEMENTED).entity(error).build());
    }
}
