/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.oidc.scope.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.oidc.scope.management.common.OidcScopeConstants;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.Scope;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ScopeUpdateRequest;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.IdentityOAuthClientException;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.dto.ScopeDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * OIDC scope management APIs are processed in this class.
 */
public class OidcScopeManagementService {

    private final OAuthAdminServiceImpl oauthAdminService;
    private static final Log LOG = LogFactory.getLog(OidcScopeManagementService.class);

    public OidcScopeManagementService(OAuthAdminServiceImpl oauthAdminService) {

        this.oauthAdminService = oauthAdminService;
    }

    /**
     * Add an OIDC scope.
     *
     * @param scopeObject Scope.
     * @return Return location URI of created scope.
     */
    public String addScope(Scope scopeObject) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding OIDC scope: " + (scopeObject != null ? scopeObject.getName() : "null"));
        }
        try {
            List<String> claimList = scopeObject.getClaims();
            String[] claimArray = claimList.toArray(new String[claimList.size()]);
            ScopeDTO scopeDTO = new ScopeDTO(scopeObject.getName(), scopeObject.getDisplayName(),
                    scopeObject.getDescription(), claimArray);
            oauthAdminService.addScope(scopeDTO);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully added OIDC scope: " + scopeDTO.getName());
            }
            return scopeDTO.getName();
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while adding OIDC scope: " + scopeObject.getName());
        }
    }

    /**
     * Delete an OIDC scope.
     *
     * @param id Scope name.
     */
    public void deleteScope(String id) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting OIDC scope: " + id);
        }
        try {
            oauthAdminService.deleteScope(id);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted OIDC scope: " + id);
            }
        } catch (IdentityOAuthClientException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Client error while deleting OIDC scope: " + id + ". " + e.getMessage());
            }
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while deleting OIDC scope: " + id);
        }
    }

    /**
     * Get an OIDC scope.
     *
     * @param id Scope name.
     * @return Return scope details.
     */
    public Scope getScope(String id) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving OIDC scope: " + id);
        }
        try {
            ScopeDTO scopeDTO = oauthAdminService.getScope(id);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved OIDC scope: " + id);
            }
            return convertScopeDTOObjectToScope(scopeDTO);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while retrieving OIDC scope: " + id);
        }
    }

    /**
     * List all available OIDC scopes.
     *
     * @return List of scopes.
     */
    public List<Scope> getScopes() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving all OIDC scopes");
        }
        try {
            ScopeDTO[] scopes = oauthAdminService.getScopes();
            List<Scope> scopeList = buildScopeList(scopes);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + (scopes != null ? scopes.length : 0) + " OIDC scopes");
            }
            return scopeList;
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while listing OIDC scopes.");
        }

    }

    /**
     * Update an existing scope.
     *
     * @param id                Scope name.
     * @param scopeUpdateObject Updated scope object.
     */
    public void updateScope(String id, ScopeUpdateRequest scopeUpdateObject) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating OIDC scope: " + id);
        }
        try {
            List<String> claimList = scopeUpdateObject.getClaims();
            String[] claimArray = claimList.toArray(new String[claimList.size()]);
            ScopeDTO scopeDTO = new ScopeDTO(id, scopeUpdateObject.getDisplayName(),
                    scopeUpdateObject.getDescription(), claimArray);
            oauthAdminService.updateScope(scopeDTO);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully updated OIDC scope: " + id);
            }
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while updating OIDC scope: " + id);
        }
    }

    /**
     * Build scope list.
     *
     * @param scopeDTOS ScopeDTOs.
     * @return Return list of scope.
     */
    private List<Scope> buildScopeList(ScopeDTO[] scopeDTOS) {

        List<Scope> scopeList = new ArrayList<>();

        for (int i = 0; i < scopeDTOS.length; i++) {
            scopeList.add(convertScopeDTOObjectToScope(scopeDTOS[i]));
        }
        return scopeList;
    }

    /**
     * Convert ScopeDTO to Scope object.
     *
     * @param scopeDTO ScopeDTO.
     * @return Converted Scope.
     */
    private Scope convertScopeDTOObjectToScope(ScopeDTO scopeDTO) {

        Scope scope = new Scope();
        scope.setName(scopeDTO.getName());
        scope.setDisplayName(scopeDTO.getDisplayName());
        scope.setDescription(scopeDTO.getDescription());
        scope.setClaims(Arrays.asList(scopeDTO.getClaim()));
        return scope;
    }

    /**
     * Handle error cases.
     *
     * @param e       Exception.
     * @param message Error message.
     * @return API error.
     */
    private APIError handleException(IdentityOAuthAdminException e, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(e.getErrorCode())
                .withMessage(message).withDescription(e.getMessage());
        ErrorResponse errorResponse;
        Response.Status status;
        if (OidcScopeConstants.ErrorMessage.INVALID_REQUEST.getCode().equals(e.getErrorCode())) {
            errorResponse = builder.build(LOG, message);
            status = Response.Status.BAD_REQUEST;
        } else if (OidcScopeConstants.ErrorMessage.ERROR_CONFLICT_REQUEST.getCode().equals(e.getErrorCode())) {
            errorResponse = builder.build(LOG, message);
            status = Response.Status.CONFLICT;
        } else if (OidcScopeConstants.ErrorMessage.SCOPE_NOT_FOUND.getCode().equals(e.getErrorCode())) {
            errorResponse = builder.build(LOG, message);
            status = Response.Status.NOT_FOUND;
        } else {
            errorResponse = builder.build(LOG, e, message);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }
}
