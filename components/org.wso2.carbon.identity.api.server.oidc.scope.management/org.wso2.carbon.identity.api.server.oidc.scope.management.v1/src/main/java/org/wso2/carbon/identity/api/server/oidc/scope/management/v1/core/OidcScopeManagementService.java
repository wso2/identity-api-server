package org.wso2.carbon.identity.api.server.oidc.scope.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.oidc.scope.management.common.OidcScopeConstants;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.Scope;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ScopeUpdateRequest;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.ScopeDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.oidc.scope.management.common.OIDCScopeManagementServiceHolder.getOAuthAdminService;

/**
 * OIDC scope management APIs are processed in this class.
 */
public class OidcScopeManagementService {

    private static final Log LOG = LogFactory.getLog(OidcScopeManagementService.class);

    public String addScope(Scope scopeObject) {

        try {
            List<String> claimList = scopeObject.getClaims();
            String[] claimArray = claimList.toArray(new String[claimList.size()]);
            ScopeDTO scopeDTO = new ScopeDTO(scopeObject.getName(), scopeObject.getDisplayName(),
                    scopeObject.getDescription(), claimArray);
            getOAuthAdminService().addScope(scopeDTO);
            return scopeDTO.getName();
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while adding OIDC scope: " + scopeObject.getName());
        }
    }

    public void deleteScope(String id) {

        try {
            getOAuthAdminService().deleteScope(id);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while deleting OIDC scope: " + id);
        }
    }

    public Scope getScope(String id) {

        try {
            ScopeDTO scopeDTO = getOAuthAdminService().getScope(id);
            return convertScopeDTOObjectToScope(scopeDTO);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while retrieving OIDC scope: " + id);
        }
    }

    public List<Scope> getScopes() {

        try {
            ScopeDTO[] scopes = getOAuthAdminService().getScopes();
            return buildScopeList(scopes);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while listing OIDC scopes.");
        }

    }

    public void updateScope(String id, ScopeUpdateRequest scopeUpdateObject) {

        try {
            List<String> claimList = scopeUpdateObject.getClaims();
            String[] claimArray = claimList.toArray(new String[claimList.size()]);
            ScopeDTO scopeDTO = new ScopeDTO(id, scopeUpdateObject.getDisplayName(),
                    scopeUpdateObject.getDescription(), claimArray);
            getOAuthAdminService().updateScope(scopeDTO);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while updating OIDC scope: " + id);
        }
    }

    private List<Scope> buildScopeList(ScopeDTO[] scopeDTOS) {

        List<Scope> scopeList = new ArrayList<>();

        for (int i = 0; i < scopeDTOS.length; i++) {
            scopeList.add(convertScopeDTOObjectToScope(scopeDTOS[i]));
        }
        return scopeList;

    }

    private Scope convertScopeDTOObjectToScope(ScopeDTO scopeDTO) {

        Scope scope = new Scope();
        scope.setName(scopeDTO.getName());
        scope.setDisplayName(scopeDTO.getDisplayName());
        scope.setDescription(scopeDTO.getDescription());
        scope.setClaims(Arrays.asList(scopeDTO.getClaim()));
        return scope;

    }

    private APIError handleException(IdentityOAuthAdminException e, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(e.getErrorCode())
                .withMessage(message).withDescription(e.getMessage());
        ErrorResponse errorResponse = builder.build(LOG, e, message);
        Response.Status status;
        if (OidcScopeConstants.ErrorMessage.INVALID_REQUEST.getCode().equals(e.getErrorCode())) {
            status = Response.Status.BAD_REQUEST;
        } else if (OidcScopeConstants.ErrorMessage.ERROR_CONFLICT_REQUEST.getCode().equals(e.getErrorCode())) {
            status = Response.Status.CONFLICT;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }
}
