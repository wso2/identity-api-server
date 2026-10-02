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
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpsResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.CredentialModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.IssuerListResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.IssuerModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationClaimModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionList;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionListItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.openid4vc.template.management.PresentationDefinitionManager;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementClientException;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementErrorCode;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementException;
import org.wso2.carbon.identity.openid4vc.template.management.model.ConnectedIdpInfo;
import org.wso2.carbon.identity.openid4vc.template.management.model.Credential;
import org.wso2.carbon.identity.openid4vc.template.management.model.Issuer;
import org.wso2.carbon.identity.openid4vc.template.management.model.Issuer.KeyResolutionMethod;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationClaim;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinition;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinitionSearchResult;

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
 * Core service for VP Presentation Definition Management API, handling business logic, model conversion,
 * and error mapping.
 */
public class ServerVPDefinitionManagementService {

    private static final Log LOG = LogFactory.getLog(ServerVPDefinitionManagementService.class);
    private final PresentationDefinitionManager presentationDefinitionManager;

    public ServerVPDefinitionManagementService(PresentationDefinitionManager presentationDefinitionManager) {

        this.presentationDefinitionManager = presentationDefinitionManager;
    }

    /**
     * List presentation definitions with cursor-based pagination and optional filtering.
     *
     * @param before backward cursor; {@code null} for forward direction.
     * @param after  forward cursor; {@code null} to start from the beginning.
     * @param filter SCIM-style filter expression; {@code null} to return all definitions.
     * @param limit  maximum records per page, capped at {@code MAX_LIMIT}.
     * @return the paginated list of presentation definitions.
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

            PresentationDefinitionSearchResult searchResult =
                    presentationDefinitionManager.listWithPagination(
                            after, before, resolvedLimit + 1, filter, sortOrder, tenantId);

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
                    LOG.error("Server encountered an error while building pagination URL for the response.", e);
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
                        PaginationLink.RelEnum.PREVIOUS));
            }
            if (!isLastPage && pageItems.get(pageItems.size() - 1).getCursorKey() != null) {
                String encoded = Base64.getEncoder().encodeToString(
                        pageItems.get(pageItems.size() - 1).getCursorKey()
                                .toString().getBytes(StandardCharsets.UTF_8));
                result.addLinksItem(buildPaginationLink(
                        urlBase + VPDefinitionManagementConstants.PARAM_AFTER + encoded,
                        PaginationLink.RelEnum.NEXT));
            }

            result.setTotalResults(searchResult.getTotalCount());
            result.setPresentationDefinitions(pageItems.stream()
                    .filter(Objects::nonNull)
                    .map(this::toListItem)
                    .collect(Collectors.toList()));
            return result;
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_LISTING_DEFINITIONS, e);
        }
    }

    private PaginationLink buildPaginationLink(String href, PaginationLink.RelEnum rel) {

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

            PresentationDefinition definition = new PresentationDefinition.Builder()
                    .identifier(creationModel.getIdentifier())
                    .displayName(creationModel.getDisplayName())
                    .description(creationModel.getDescription())
                    .credentials(toCredentials(creationModel.getCredentials()))
                    .tenantId(tenantId)
                    .build();

            PresentationDefinition created = presentationDefinitionManager.createPresentationDefinition(
                    definition, tenantId);
            return toResponse(created);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_ALREADY_EXISTS.equals(e.getErrorCode())) {
                throw handleClientError(ErrorMessage.ERROR_CODE_DEFINITION_ALREADY_EXISTS, e,
                        Response.Status.CONFLICT);
            }
            if (PresentationManagementErrorCode.VALIDATION_ERROR.equals(e.getErrorCode())) {
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

            PresentationDefinition definition =
                    presentationDefinitionManager.getPresentationDefinitionById(definitionId, tenantId);
            if (definition == null) {
                throw handleNotFound(definitionId);
            }
            return toResponse(definition);
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

            PresentationDefinition existing =
                    presentationDefinitionManager.getPresentationDefinitionById(definitionId, tenantId);
            if (existing == null) {
                throw handleNotFound(definitionId);
            }

            List<Credential> credentials = updateModel.getCredentials() != null
                    ? toCredentials(updateModel.getCredentials())
                    : null;

            PresentationDefinition definition = new PresentationDefinition.Builder()
                    .id(existing.getId())
                    .displayName(updateModel.getDisplayName())
                    .description(updateModel.getDescription())
                    .credentials(credentials)
                    .tenantId(tenantId)
                    .build();

            PresentationDefinition updated = presentationDefinitionManager.updatePresentationDefinition(
                    definition, tenantId);
            return toResponse(updated);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND.equals(e.getErrorCode())) {
                throw handleNotFound(definitionId);
            }
            if (PresentationManagementErrorCode.VALIDATION_ERROR.equals(e.getErrorCode())) {
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

            presentationDefinitionManager.deletePresentationDefinition(definitionId, tenantId);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND.equals(e.getErrorCode())) {
                throw handleNotFound(definitionId);
            }
            if (PresentationManagementErrorCode.DEFINITION_IN_USE.equals(e.getErrorCode())) {
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

            List<ConnectedIdpInfo> idps = presentationDefinitionManager.getConnectedIdps(definitionId, tenantId);

            String serverUrl = IdentityUtil.getServerURL(
                    VPDefinitionManagementConstants.IDENTITY_PROVIDER_PATH_COMPONENT, true, true);

            List<ConnectedIdpInfo> effectiveIdps = idps != null ? idps : Collections.emptyList();
            List<ConnectedIdpItem> items = new ArrayList<>();
            for (ConnectedIdpInfo idp : effectiveIdps) {
                ConnectedIdpItem item = new ConnectedIdpItem();
                item.setIdpId(idp.getIdpId());
                item.setName(idp.getIdpName());
                item.setSelf(serverUrl + idp.getIdpId());
                items.add(item);
            }

            ConnectedIdpsResponse response = new ConnectedIdpsResponse();
            response.setCount(items.size());
            response.setTotalResults(items.size());
            response.setStartIndex(1);
            response.setConnectedIdps(items);
            return response;
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND.equals(e.getErrorCode())) {
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
     * Retrieve the issuer configurations for a specific credential within a presentation definition.
     *
     * @param definitionId the server-generated UUID of the definition.
     * @param credentialId the user-defined identifier of the target credential.
     * @return the issuer configurations stored for that credential.
     */
    public IssuerListResponse getIssuerConfigs(String definitionId, String credentialId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving issuer configs for credential: " + credentialId
                    + " in presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();

            PresentationDefinition definition =
                    presentationDefinitionManager.getPresentationDefinitionById(definitionId, tenantId);
            if (definition == null) {
                throw handleNotFound(definitionId);
            }

            Credential target = findCredential(definition, credentialId);
            if (target == null) {
                throw handleCredentialNotFound(definitionId, credentialId);
            }

            return toIssuerListResponse(target.getIssuers());
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEFINITION, e, definitionId);
        }
    }

    /**
     * Atomically replace the issuer configurations for a specific credential.
     *
     * @param definitionId             the server-generated UUID of the definition.
     * @param credentialId             the user-defined identifier of the target credential.
     * @param issuerListResponse the new issuer configs (replaces all existing ones).
     * @return the stored issuer configurations.
     */
    public IssuerListResponse replaceIssuerConfigs(String definitionId, String credentialId,
            IssuerListResponse issuerListResponse) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Replacing issuer configs for credential: " + credentialId
                    + " in presentation definition: " + definitionId);
        }
        try {
            int tenantId = getTenantId();

            PresentationDefinition definition =
                    presentationDefinitionManager.getPresentationDefinitionById(definitionId, tenantId);
            if (definition == null) {
                throw handleNotFound(definitionId);
            }

            if (findCredential(definition, credentialId) == null) {
                throw handleCredentialNotFound(definitionId, credentialId);
            }

            List<IssuerModel> requestModels = issuerListResponse != null
                    ? issuerListResponse.getIssuerConfigs()
                    : null;

            for (IssuerModel model : safeList(requestModels)) {
                if (model.getKeySourceType() == null) {
                    throw handleClientError(ErrorMessage.ERROR_CODE_INVALID_INPUT, null,
                            Response.Status.BAD_REQUEST,
                            "Each issuer configuration must specify a keySourceType.");
                }
            }

            List<Issuer> issuers = toDomainIssuers(requestModels);
            presentationDefinitionManager.replaceIssuerConfigs(definitionId, credentialId, issuers, tenantId);

            return toIssuerListResponse(issuers);
        } catch (PresentationManagementClientException e) {
            if (PresentationManagementErrorCode.DEFINITION_NOT_FOUND.equals(e.getErrorCode())) {
                throw handleNotFound(definitionId);
            }
            if (PresentationManagementErrorCode.VALIDATION_ERROR.equals(e.getErrorCode())) {
                throw handleClientError(ErrorMessage.ERROR_CODE_INVALID_INPUT, e,
                        Response.Status.BAD_REQUEST, e.getMessage());
            }
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e, definitionId);
        } catch (PresentationManagementException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEFINITION, e, definitionId);
        }
    }

    private List<Credential> toCredentials(List<CredentialModel> apiModels)
            throws PresentationManagementClientException {

        if (apiModels == null) {
            return null;
        }
        List<Credential> result = new ArrayList<>();
        for (CredentialModel apiModel : apiModels) {
            Credential credential = new Credential();
            credential.setIdentifier(apiModel.getId());
            credential.setType(apiModel.getType());
            credential.setFormat(apiModel.getFormat() != null
                    ? apiModel.getFormat().value() : VPDefinitionManagementConstants.DEFAULT_CREDENTIAL_FORMAT);
            credential.setClaims(toPresentationClaims(apiModel.getClaims()));
            result.add(credential);
        }
        return result;
    }

    private List<CredentialModel> toCredentialModels(List<Credential> domainCredentials) {

        if (domainCredentials == null) {
            return null;
        }
        List<CredentialModel> result = new ArrayList<>();
        for (Credential credential : domainCredentials) {
            CredentialModel model = new CredentialModel();
            model.setId(credential.getIdentifier());
            model.setType(credential.getType());
            model.setFormat(CredentialModel.FormatEnum.fromValue(credential.getFormat()));
            model.setClaims(toPresentationClaimModels(credential.getClaims()));
            result.add(model);
        }
        return result;
    }

    private List<Issuer> toDomainIssuers(List<IssuerModel> apiModels) {

        if (apiModels == null) {
            return null;
        }
        List<Issuer> result = new ArrayList<>();
        for (IssuerModel model : apiModels) {
            Issuer issuer = new Issuer();
            KeyResolutionMethod method = KeyResolutionMethod.valueOf(model.getKeySourceType().name());
            issuer.setKeyResolutionMethod(method);
            issuer.setIssuerUrl(model.getIssuerUrl());
            if (KeyResolutionMethod.JWKS_URI == method) {
                issuer.setJwksUri(model.getKeySource());
            } else {
                issuer.setCertificate(decodeBase64Pem(model.getKeySource()));
            }
            result.add(issuer);
        }
        return result;
    }

    private List<IssuerModel> toIssuerModels(List<Issuer> issuers) {

        if (issuers == null) {
            return null;
        }
        List<IssuerModel> result = new ArrayList<>();
        for (Issuer issuer : issuers) {
            IssuerModel model = new IssuerModel();
            model.setKeySourceType(IssuerModel.KeySourceTypeEnum.valueOf(issuer.getKeyResolutionMethod().name()));
            model.setIssuerUrl(issuer.getIssuerUrl());
            if (KeyResolutionMethod.JWKS_URI == issuer.getKeyResolutionMethod()) {
                model.setKeySource(issuer.getJwksUri());
            } else {
                model.setKeySource(encodeBase64Pem(issuer.getCertificate()));
            }
            result.add(model);
        }
        return result;
    }

    private IssuerListResponse toIssuerListResponse(List<Issuer> issuers) {

        IssuerListResponse response = new IssuerListResponse();
        response.setIssuerConfigs(toIssuerModels(issuers));
        return response;
    }

    private String decodeBase64Pem(String base64Pem) {

        if (StringUtils.isBlank(base64Pem)) {
            return null;
        }
        return new String(Base64.getDecoder().decode(base64Pem), StandardCharsets.UTF_8);
    }

    private String encodeBase64Pem(String rawPem) {

        if (StringUtils.isBlank(rawPem)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(rawPem.getBytes(StandardCharsets.UTF_8));
    }

    private List<PresentationClaim> toPresentationClaims(List<PresentationClaimModel> apiModels) {

        if (apiModels == null) {
            return null;
        }
        List<PresentationClaim> result = new ArrayList<>();
        for (PresentationClaimModel apiModel : apiModels) {
            PresentationClaim claim = new PresentationClaim();
            claim.setPath(apiModel.getPath());
            claim.setMandatory(Boolean.TRUE.equals(
                    apiModel.getMandatory() == null ? Boolean.TRUE : apiModel.getMandatory()));
            result.add(claim);
        }
        return result;
    }

    private List<PresentationClaimModel> toPresentationClaimModels(List<PresentationClaim> claims) {

        if (claims == null) {
            return null;
        }
        List<PresentationClaimModel> result = new ArrayList<>();
        for (PresentationClaim claim : claims) {
            PresentationClaimModel model = new PresentationClaimModel();
            model.setPath(claim.getPath());
            model.setMandatory(claim.isMandatory());
            result.add(model);
        }
        return result;
    }

    private PresentationDefinitionResponse toResponse(PresentationDefinition definition) {

        PresentationDefinitionResponse response = new PresentationDefinitionResponse();
        response.setId(definition.getId());
        response.setIdentifier(definition.getIdentifier());
        response.setDisplayName(definition.getDisplayName());
        response.setDescription(definition.getDescription());
        response.setCredentials(toCredentialModels(definition.getCredentials()));
        return response;
    }

    private PresentationDefinitionListItem toListItem(PresentationDefinition definition) {

        PresentationDefinitionListItem item = new PresentationDefinitionListItem();
        item.setId(definition.getId());
        item.setIdentifier(definition.getIdentifier());
        item.setDisplayName(definition.getDisplayName());
        item.setDescription(definition.getDescription());
        return item;
    }

    private Credential findCredential(PresentationDefinition definition, String credentialId) {

        List<Credential> credentials = definition.getCredentials();
        if (credentials == null) {
            return null;
        }
        for (Credential cred : credentials) {
            if (credentialId.equals(cred.getIdentifier())) {
                return cred;
            }
        }
        return null;
    }

    private <T> List<T> safeList(List<T> list) {

        return list != null ? list : Collections.emptyList();
    }

    // --- Error handling ---

    private APIError handleNotFound(String definitionId) {

        ErrorResponse errorResponse = getErrorBuilder(ErrorMessage.ERROR_CODE_DEFINITION_NOT_FOUND, definitionId)
                .build(LOG, ErrorMessage.ERROR_CODE_DEFINITION_NOT_FOUND.getDescription());
        return new APIError(Response.Status.NOT_FOUND, errorResponse);
    }

    private APIError handleCredentialNotFound(String definitionId, String credentialId) {

        ErrorResponse errorResponse = getErrorBuilder(
                ErrorMessage.ERROR_CODE_CREDENTIAL_NOT_FOUND, credentialId, definitionId)
                .build(LOG, ErrorMessage.ERROR_CODE_CREDENTIAL_NOT_FOUND.getDescription());
        return new APIError(Response.Status.NOT_FOUND, errorResponse);
    }

    private APIError handleClientError(
            ErrorMessage errorMessage, Exception e, Response.Status status, String... args) {

        ErrorResponse errorResponse = getErrorBuilder(errorMessage, args)
                .build(LOG, errorMessage.getDescription());
        return new APIError(status, errorResponse);
    }

    private APIError handleServerError(ErrorMessage errorMessage, Exception e, String... args) {

        ErrorResponse errorResponse = getErrorBuilder(errorMessage, args)
                .build(LOG, errorMessage.getDescription());
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(ErrorMessage errorMessage, String... args) {

        return new ErrorResponse.Builder()
                .withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage())
                .withDescription(includeData(errorMessage, args));
    }

    private String includeData(ErrorMessage errorMessage, String... args) {

        if (args == null || args.length == 0) {
            return errorMessage.getDescription();
        }
        return String.format(errorMessage.getDescription(), (Object[]) args);
    }

    // --- Utility ---

    private int getTenantId() {

        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
    }
}
