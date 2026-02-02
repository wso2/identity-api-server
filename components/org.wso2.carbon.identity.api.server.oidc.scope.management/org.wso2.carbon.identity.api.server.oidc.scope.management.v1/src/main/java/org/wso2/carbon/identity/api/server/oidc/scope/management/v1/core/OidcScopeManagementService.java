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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.common.file.FileContent;
import org.wso2.carbon.identity.api.server.common.file.FileSerializationConfig;
import org.wso2.carbon.identity.api.server.common.file.FileSerializationException;
import org.wso2.carbon.identity.api.server.common.file.FileSerializationUtil;
import org.wso2.carbon.identity.api.server.common.file.YamlConfig;
import org.wso2.carbon.identity.api.server.oidc.scope.management.common.OidcScopeConstants;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.Scope;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ScopeConfiguration;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ScopeUpdateRequest;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.IdentityOAuthClientException;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.dto.ScopeDTO;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

        try {
            List<String> claimList = scopeObject.getClaims();
            String[] claimArray = claimList.toArray(new String[claimList.size()]);
            ScopeDTO scopeDTO = new ScopeDTO(scopeObject.getName(), scopeObject.getDisplayName(),
                    scopeObject.getDescription(), claimArray);
            oauthAdminService.addScope(scopeDTO);
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

        try {
            oauthAdminService.deleteScope(id);
        } catch (IdentityOAuthClientException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
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

        try {
            ScopeDTO scopeDTO = oauthAdminService.getScope(id);
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

        try {
            ScopeDTO[] scopes = oauthAdminService.getScopes();
            return buildScopeList(scopes);
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

        try {
            List<String> claimList = scopeUpdateObject.getClaims();
            String[] claimArray = claimList.toArray(new String[claimList.size()]);
            ScopeDTO scopeDTO = new ScopeDTO(id, scopeUpdateObject.getDisplayName(),
                    scopeUpdateObject.getDescription(), claimArray);
            oauthAdminService.updateScope(scopeDTO);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e, "Server encountered an error while updating OIDC scope: " + id);
        }
    }

    /**
     * Export an OIDC scope identified by the scopeName, in the given format.
     *
     * @param scopeName Scope name.
     * @param fileType  File type (Accept header value).
     * @return FileContent with scope data.
     */
    public FileContent exportScopeToFile(String scopeName, String fileType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exporting OIDC scope from the scope name: " + scopeName);
        }
        if (StringUtils.isBlank(fileType)) {
            throw new UnsupportedOperationException("No valid media type found");
        }

        Scope scope = getScope(scopeName);

        if (scope == null) {
            throw handleException(Response.Status.NOT_FOUND,
                    OidcScopeConstants.ErrorMessage.ERROR_CODE_ERROR_EXPORTING_SCOPE);
        }

        FileContent fileContent;
        try {
            fileContent = generateFileFromModel(fileType, scope);
        } catch (IdentityOAuthAdminException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    OidcScopeConstants.ErrorMessage.ERROR_CODE_ERROR_EXPORTING_SCOPE, e);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Successfully exported OIDC scope: %s as a file type of %s.",
                    scope.getName(), fileType));
        }
        return fileContent;
    }

    /**
     * Create a new OIDC scope by importing an YAML, JSON or XML configuration file.
     *
     * @param fileInputStream File to be imported as an input stream.
     * @param fileDetail      File details.
     * @return Scope name of the imported scope.
     */
    public String importScopeFromFile(InputStream fileInputStream, Attachment fileDetail) {

        Scope scope;
        try {
            scope = getScopeFromFile(fileInputStream, fileDetail);
        } catch (IdentityOAuthClientException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    OidcScopeConstants.ErrorMessage.ERROR_CODE_ERROR_IMPORTING_SCOPE, e);
        }

        return addScope(scope);
    }

    /**
     * Update an existing OIDC scope from an YAML, JSON or XML configuration file.
     *
     * @param scopeName       Scope name to update.
     * @param fileInputStream File to be imported as an input stream.
     * @param fileDetail      File details.
     * @return Scope name of the updated scope.
     */
    public String updateScopeFromFile(String scopeName, InputStream fileInputStream, Attachment fileDetail) {

        Scope scope;
        try {
            scope = getScopeFromFile(fileInputStream, fileDetail);
        } catch (IdentityOAuthClientException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    OidcScopeConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCOPE, e);
        }

        ScopeUpdateRequest updateRequest = new ScopeUpdateRequest();
        updateRequest.setDisplayName(scope.getDisplayName());
        updateRequest.setDescription(scope.getDescription());
        updateRequest.setClaims(scope.getClaims());

        updateScope(scopeName, updateRequest);
        return scopeName;
    }

    private FileContent generateFileFromModel(String fileType, Scope scope)
            throws IdentityOAuthAdminException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsing OIDC scope object to file content of type: " + fileType);
        }

        ScopeConfiguration scopeConfig = new ScopeConfiguration(scope);

        FileSerializationConfig config = new FileSerializationConfig();
        YamlConfig yamlConfig = new YamlConfig();
        yamlConfig.setDumperOptionsCustomizer(options -> {
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        });
        config.setYamlConfig(yamlConfig);

        try {
            return FileSerializationUtil.serialize(scopeConfig, scope.getName(), fileType, config);
        } catch (FileSerializationException e) {
            throw new IdentityOAuthAdminException("Error when parsing OIDC scope to file.", e);
        }
    }

    private Scope getScopeFromFile(InputStream fileInputStream, Attachment fileDetail)
            throws IdentityOAuthClientException {

        Scope scope;
        try {
            FileContent scopeFileContent = new FileContent(fileDetail.getDataHandler().getName(),
                    fileDetail.getDataHandler().getContentType(),
                    IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name()));
            scope = generateModelFromFile(scopeFileContent);
        } catch (IOException | IdentityOAuthClientException e) {
            throw new IdentityOAuthClientException("Provided input file is not in the correct format", e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }

        return scope;
    }

    private Scope generateModelFromFile(FileContent fileContent)
            throws IdentityOAuthClientException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Parsing OIDC scope from file: %s of type: %s.", fileContent.getFileName(),
                    fileContent.getFileType()));
        }
        if (StringUtils.isEmpty(fileContent.getContent())) {
            throw new IdentityOAuthClientException(String.format(
                            "Empty OIDC scope configuration file %s uploaded.", fileContent.getFileName()));
        }

        try {
            ScopeConfiguration scopeConfig = FileSerializationUtil.deserialize(fileContent, ScopeConfiguration.class);
            return scopeConfig.toScope();
        } catch (FileSerializationException e) {
            throw new IdentityOAuthClientException("Error when generating the OIDC scope model from file", e);
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

    private APIError handleException(Response.Status status, OidcScopeConstants.ErrorMessage errorMessage,
                                      IdentityOAuthAdminException e) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage())
                .withDescription(e.getMessage())
                .build(LOG, errorMessage.getMessage());
        return new APIError(status, errorResponse);
    }

    private APIError handleException(Response.Status status, OidcScopeConstants.ErrorMessage errorMessage) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage())
                .withDescription(errorMessage.getDescription())
                .build(LOG, errorMessage.getMessage());
        return new APIError(status, errorResponse);
   }
}
