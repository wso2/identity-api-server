/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.Error;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionList;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionListItem;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.RequestedCredentialModel;
import org.wso2.carbon.identity.openid4vc.presentation.common.exception.PresentationDefinitionNotFoundException;
import org.wso2.carbon.identity.openid4vc.presentation.common.exception.VPException;
import org.wso2.carbon.identity.openid4vc.presentation.common.model.PresentationDefinition;
import org.wso2.carbon.identity.openid4vc.presentation.common.model.PresentationDefinition.RequestedCredential;
import org.wso2.carbon.identity.openid4vc.presentation.definition.service.PresentationDefinitionService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for VP Presentation Definition Management API.
 * Handles business logic, model conversion, and error mapping.
 */
public class ServerVPDefinitionManagementService {

    /**
     * List all presentation definitions for the current tenant.
     *
     * @return PresentationDefinitionList
     */
    public PresentationDefinitionList listPresentationDefinitions() {

        try {
            int tenantId = getTenantId();
            PresentationDefinitionService service = getService();

            List<PresentationDefinition> definitions = service.getAllPresentationDefinitions(tenantId);

            PresentationDefinitionList listResponse = new PresentationDefinitionList();
            listResponse.setTotalResults(definitions.size());
            listResponse.setPresentationDefinitions(
                    definitions.stream()
                            .map(this::toListItem)
                            .collect(Collectors.toList()));
            return listResponse;
        } catch (VPException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_LISTING_DEFINITIONS, e);
        }
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
                    .requestedCredentials(toRequestedCredentials(creationModel.getRequestedCredentials()))
                    .tenantId(tenantId)
                    .build();

            PresentationDefinition created = service.createPresentationDefinition(definition, tenantId);
            return toResponse(created);
        } catch (VPException e) {
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                throw handleClientError(ErrorMessage.ERROR_CODE_DEFINITION_ALREADY_EXISTS, e,
                        Response.Status.CONFLICT);
            }
            if (e.getMessage() != null && (e.getMessage().contains("Invalid") ||
                    e.getMessage().contains("required"))) {
                throw handleClientError(ErrorMessage.ERROR_CODE_INVALID_INPUT, e,
                        Response.Status.BAD_REQUEST, e.getMessage());
            }
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
        } catch (PresentationDefinitionNotFoundException e) {
            throw handleNotFound(definitionId);
        } catch (VPException e) {
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

            List<RequestedCredential> credentials = updateModel.getRequestedCredentials() != null
                    ? toRequestedCredentials(updateModel.getRequestedCredentials())
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
        } catch (PresentationDefinitionNotFoundException e) {
            throw handleNotFound(definitionId);
        } catch (VPException e) {
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
        } catch (PresentationDefinitionNotFoundException e) {
            throw handleNotFound(definitionId);
        } catch (VPException e) {
            throw handleServerError(ErrorMessage.ERROR_CODE_ERROR_DELETING_DEFINITION, e,
                    definitionId);
        }
    }

    // --- Conversion helpers ---

    /**
     * Convert API RequestedCredentialModel list to domain RequestedCredential list.
     */
    private List<RequestedCredential> toRequestedCredentials(
            List<RequestedCredentialModel> apiModels) {

        if (apiModels == null) {
            return null;
        }
        List<RequestedCredential> result = new ArrayList<>();
        for (RequestedCredentialModel apiModel : apiModels) {
            RequestedCredential cred = new RequestedCredential();
            cred.setType(apiModel.getType());
            cred.setPurpose(apiModel.getPurpose());
            cred.setIssuer(apiModel.getIssuer());
            cred.setClaims(apiModel.getRequestedClaims());
            result.add(cred);
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
        for (RequestedCredential cred : domainCredentials) {
            RequestedCredentialModel model = new RequestedCredentialModel();
            model.setType(cred.getType());
            model.setPurpose(cred.getPurpose());
            model.setIssuer(cred.getIssuer());
            model.setRequestedClaims(cred.getClaims());
            result.add(model);
        }
        return result;
    }

    private PresentationDefinitionResponse toResponse(PresentationDefinition definition) {

        PresentationDefinitionResponse response = new PresentationDefinitionResponse();
        response.setId(definition.getDefinitionId());
        response.setName(definition.getName());
        response.setDescription(definition.getDescription());
        response.setDefinition(toCredentialModels(definition.getRequestedCredentials()));
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

        PresentationDefinitionService service =
                VPDefinitionManagementServiceHolder.getPresentationDefinitionService();
        if (service == null) {
            throw new javax.ws.rs.WebApplicationException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("PresentationDefinitionService is not available").build());
        }
        return service;
    }
}
