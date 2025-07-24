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

package org.wso2.carbon.identity.api.server.script.library.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.script.library.common.Constants;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibrary;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryListResponse;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryPOSTRequest;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryPUTRequest;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryResponse;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.functions.library.mgt.FunctionLibraryManagementService;
import org.wso2.carbon.identity.functions.library.mgt.exception.FunctionLibraryManagementClientException;
import org.wso2.carbon.identity.functions.library.mgt.exception.FunctionLibraryManagementException;
import org.wso2.carbon.identity.functions.library.mgt.exception.FunctionLibraryManagementServerException;
import org.wso2.carbon.identity.functions.library.mgt.model.FunctionLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.script.library.common.Constants.SCRIPT_LIBRARY_CONTENT_PATH;
import static org.wso2.carbon.identity.api.server.script.library.common.Constants.SCRIPT_LIBRARY_PATH_COMPONENT;

/**
 * Call internal osgi services to perform server script library management operations.
 */
public class ServerScriptLibrariesService {

    private final FunctionLibraryManagementService functionLibraryManagementService;
    private static final Log log = LogFactory.getLog(ServerScriptLibrariesService.class);

    public ServerScriptLibrariesService(FunctionLibraryManagementService functionLibraryManagementService) {

        if (log.isDebugEnabled()) {
            log.debug("Initializing ServerScriptLibrariesService with FunctionLibraryManagementService.");
        }
        this.functionLibraryManagementService = functionLibraryManagementService;
    }

    /**
     * Get list of Script Libraries.
     *
     * @param limit  Items per page.
     * @param offset Offset.
     * @return ScriptLibraryListResponse.
     */
    public ScriptLibraryListResponse getScriptLibraries(Integer limit, Integer offset) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Retrieving script libraries for tenant: " + (tenantDomain != null ? tenantDomain : "null") + 
                     ", limit: " + limit + ", offset: " + offset);
        }
        
        List<FunctionLibrary> functionLibraries;
        try {
            functionLibraries = functionLibraryManagementService.listFunctionLibraries(tenantDomain);
            if (log.isDebugEnabled()) {
                int count = functionLibraries != null ? functionLibraries.size() : 0;
                log.debug("Retrieved " + count + " script libraries from service for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null"));
            }
        } catch (FunctionLibraryManagementException e) {
            log.error("Error listing script libraries for tenant: " + (tenantDomain != null ? tenantDomain : "null") + 
                     ", Error: " + e.getMessage());
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_SCRIPT_LIBRARIES);
        }
        return createScriptLibrariesList(functionLibraries, validateLimit(limit), validateOffset(offset));
    }

    /**
     * Validate the offset.
     *
     * @param offset Offset value
     * @return Validated offset.
     */
    private int validateOffset(Integer offset) {

        if (offset != null && offset >= 0) {
            return offset;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Invalid offset value provided: " + offset);
            }
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_OFFSET_VALIDATION,
                    Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Validate the limit.
     *
     * @param limit Limit value.
     * @return Validated limit.
     */
    private int validateLimit(Integer limit) {

        final int maximumItemPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit == null) {
            if (log.isDebugEnabled()) {
                log.debug("No limit provided, using default items per page: " + IdentityUtil.getDefaultItemsPerPage());
            }
            return IdentityUtil.getDefaultItemsPerPage();
        } else if (limit <= maximumItemPerPage) {
            return limit;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Provided limit " + limit + " exceeds maximum " + maximumItemPerPage + 
                         ", using maximum limit.");
            }
            return maximumItemPerPage;
        }
    }

    /**
     * Get a script library identified by resource ID.
     *
     * @param scriptLibraryId resource ID.
     * @return ScriptLibrary.
     */
    public ScriptLibraryResponse getScriptLibrary(String scriptLibraryId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Retrieving script library with ID: " + (scriptLibraryId != null ? scriptLibraryId : "null") + 
                     " for tenant: " + (tenantDomain != null ? tenantDomain : "null"));
        }
        
        if (isScriptLibraryAvailable(scriptLibraryId)) {
            FunctionLibrary functionLibrary;
            try {
                functionLibrary = functionLibraryManagementService.getFunctionLibrary(scriptLibraryId, tenantDomain);
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved script library: " + 
                             (scriptLibraryId != null ? scriptLibraryId : "null"));
                }
            } catch (FunctionLibraryManagementException e) {
                log.error("Error retrieving script library with ID: " + 
                         (scriptLibraryId != null ? scriptLibraryId : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null") + ", Error: " + e.getMessage());
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
            }
            return createScriptLibraryResponse(functionLibrary);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Script library not found with ID: " + (scriptLibraryId != null ? scriptLibraryId : "null") + 
                         " for tenant: " + (tenantDomain != null ? tenantDomain : "null"));
            }
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                    Response.Status.NOT_FOUND, scriptLibraryId, tenantDomain);
        }

    }

    /**
     * Add a script library.
     *
     * @param name               Name of the script library.
     * @param contentInputStream Content of the script library code.
     * @param description        Description of the script library
     */
    public void addScriptLibrary(String name, InputStream contentInputStream, String description) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Adding script library with name: " + (name != null ? name : "null") + 
                     " for tenant: " + (tenantDomain != null ? tenantDomain : "null"));
        }
        
        ScriptLibraryPOSTRequest scriptLibraryPOSTRequest = new ScriptLibraryPOSTRequest();
        scriptLibraryPOSTRequest.setName(name);
        scriptLibraryPOSTRequest.setDescription(description);
        String scriptLibraryPOSTRequestContent;
        try {
            scriptLibraryPOSTRequestContent = IOUtils.toString(contentInputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("Error occurred while reading contentInputStream for script library: " + 
                     (name != null ? name : "null") + ", Error: " + e.getMessage());
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SCRIPT_LIBRARY,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (isScriptLibraryAvailable(scriptLibraryPOSTRequest.getName())) {
            if (log.isDebugEnabled()) {
                log.debug("Script library already exists with name: " + 
                         (scriptLibraryPOSTRequest.getName() != null ? scriptLibraryPOSTRequest.getName() : "null"));
            }
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_ALREADY_FOUND,
                    Response.Status.CONFLICT, scriptLibraryPOSTRequest.getName(), tenantDomain);
        } else {
            FunctionLibrary functionLibrary = createScriptLibrary(scriptLibraryPOSTRequestContent,
                    scriptLibraryPOSTRequest);
            try {
                if (scriptLibraryPOSTRequest.getName() != null && 
                    scriptLibraryPOSTRequest.getName().contains(Constants.SCRIPT_LIBRARY_EXTENSION)) {
                    functionLibraryManagementService.createFunctionLibrary(functionLibrary, tenantDomain);
                    log.info("Script library created successfully with name: " + 
                            (name != null ? name : "null") + " for tenant: " + 
                            (tenantDomain != null ? tenantDomain : "null"));
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Invalid script library name format: " + 
                                 (scriptLibraryPOSTRequest.getName() != null ? 
                                  scriptLibraryPOSTRequest.getName() : "null"));
                    }
                    throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NAME_VALIDATION,
                            Response.Status.BAD_REQUEST);
                }
            } catch (FunctionLibraryManagementException e) {
                log.error("Error creating script library with name: " + (name != null ? name : "null") + 
                         " for tenant: " + (tenantDomain != null ? tenantDomain : "null") + 
                         ", Error: " + e.getMessage());
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SCRIPT_LIBRARY);
            }
        }

    }

    /**
     * Update a script library identified by resource ID.
     *
     * @param scriptLibraryName  Name of the script library.
     * @param contentInputStream Content of the script library code.
     * @param description        Description of the script library
     */
    public void updateScriptLibrary(String scriptLibraryName, InputStream contentInputStream,
                                    String description) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Updating script library with name: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                     (tenantDomain != null ? tenantDomain : "null"));
        }
        
        ScriptLibraryPUTRequest scriptLibraryPUTRequest = new ScriptLibraryPUTRequest();
        scriptLibraryPUTRequest.setDescription(description);
        String scriptLibraryPUTRequestContent;
        try {
            scriptLibraryPUTRequestContent = IOUtils.toString(contentInputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("Error occurred while reading contentInputStream for script library: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null") + ", Error: " + e.getMessage());
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (isScriptLibraryAvailable(scriptLibraryName)) {
            FunctionLibrary functionLibrary = createScriptLibraryPut(scriptLibraryName, scriptLibraryPUTRequestContent,
                    scriptLibraryPUTRequest);
            try {
                functionLibraryManagementService.updateFunctionLibrary(scriptLibraryName, functionLibrary,
                        tenantDomain);
                log.info("Script library updated successfully with name: " + 
                        (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                        (tenantDomain != null ? tenantDomain : "null"));
            } catch (FunctionLibraryManagementException e) {
                log.error("Error updating script library with name: " + 
                         (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null") + ", Error: " + e.getMessage());
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Script library not found for update with name: " + 
                         (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null"));
            }
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                    Response.Status.NOT_FOUND, scriptLibraryName, tenantDomain);
        }

    }

    /**
     * Get the script library content.
     *
     * @param scriptLibraryName Name of the script library
     * @return Script library content
     */
    public String getScriptLibraryContentByName(String scriptLibraryName) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Retrieving script library content by name: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                     (tenantDomain != null ? tenantDomain : "null"));
        }
        
        if (isScriptLibraryAvailable(scriptLibraryName)) {
            try {
                FunctionLibrary functionLibrary = functionLibraryManagementService
                        .getFunctionLibrary(scriptLibraryName, tenantDomain);
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved script library content for: " + 
                             (scriptLibraryName != null ? scriptLibraryName : "null"));
                }
                return functionLibrary.getFunctionLibraryScript();
            } catch (FunctionLibraryManagementException e) {
                log.error("Error retrieving script library content for name: " + 
                         (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null") + ", Error: " + e.getMessage());
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Script library not found for content retrieval with name: " + 
                         (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null"));
            }
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                    Response.Status.NOT_FOUND, scriptLibraryName, tenantDomain);
        }
    }

    /**
     * Delete a script library.
     *
     * @param scriptLibraryId script library resource ID.
     */
    public void deleteScriptLibrary(String scriptLibraryId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Deleting script library with ID: " + (scriptLibraryId != null ? scriptLibraryId : "null") + 
                     " for tenant: " + (tenantDomain != null ? tenantDomain : "null"));
        }
        
        if (isScriptLibraryAvailable(scriptLibraryId)) {
            try {
                functionLibraryManagementService.deleteFunctionLibrary(scriptLibraryId, tenantDomain);
                log.info("Script library deleted successfully with ID: " + 
                        (scriptLibraryId != null ? scriptLibraryId : "null") + " for tenant: " + 
                        (tenantDomain != null ? tenantDomain : "null"));
            } catch (FunctionLibraryManagementException e) {
                log.error("Error deleting script library with ID: " + 
                         (scriptLibraryId != null ? scriptLibraryId : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null") + ", Error: " + e.getMessage());
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_SCRIPT_LIBRARY);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Script library not found for deletion with ID: " + 
                         (scriptLibraryId != null ? scriptLibraryId : "null") + " for tenant: " + 
                         (tenantDomain != null ? tenantDomain : "null"));
            }
        }
    }

    /**
     * Check the whether the script name already exist.
     *
     * @param scriptLibraryName Name of the script library.
     * @return isAvailable boolean.
     */
    public boolean isScriptLibraryAvailable(String scriptLibraryName) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Checking script library availability for name: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                     (tenantDomain != null ? tenantDomain : "null"));
        }
        
        boolean isAvailable;
        try {
            isAvailable = functionLibraryManagementService.isFunctionLibraryExists(scriptLibraryName, tenantDomain);
            if (log.isDebugEnabled()) {
                log.debug("Script library availability check result: " + isAvailable + " for name: " + 
                         (scriptLibraryName != null ? scriptLibraryName : "null"));
            }
        } catch (FunctionLibraryManagementException e) {
            log.error("Error checking script library availability for name: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null") + " for tenant: " + 
                     (tenantDomain != null ? tenantDomain : "null") + ", Error: " + e.getMessage());
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
        }
        return isAvailable;
    }

    /**
     * Create the script library response.
     *
     * @param scriptLibrary script library object.
     * @return scriptLibraryResponse
     */
    private ScriptLibraryResponse createScriptLibraryResponse(FunctionLibrary scriptLibrary) {

        ScriptLibraryResponse scriptLibraryResponse = new ScriptLibraryResponse();
        scriptLibraryResponse.setName(scriptLibrary.getFunctionLibraryName());
        scriptLibraryResponse.setDescription(scriptLibrary.getDescription());
        try {
            String displayName =
                    URLEncoder.encode(scriptLibrary.getFunctionLibraryName(), StandardCharsets.UTF_8.name());
            scriptLibraryResponse.setContentRef(ContextLoader.buildURIForBody(String.format(
                    V1_API_PATH_COMPONENT + SCRIPT_LIBRARY_PATH_COMPONENT + "/%s" + SCRIPT_LIBRARY_CONTENT_PATH,
                    displayName)).toString().replace("+", "%20"));
            return scriptLibraryResponse;
        } catch (UnsupportedEncodingException e) {
            FunctionLibraryManagementException error = new FunctionLibraryManagementException(
                    Constants.ErrorMessage.ERROR_CODE_ERROR_ENCODING_URL.getMessage(), e);
            throw handleScriptLibraryError(error, Constants.ErrorMessage.ERROR_CODE_ERROR_ENCODING_URL);
        }
    }

    /**
     * Create script library object.
     *
     * @param scriptLibraryPOSTRequest ScriptLibraryPOSTRequest
     * @return functionLibrary
     */
    private FunctionLibrary createScriptLibrary(String scriptLibraryPOSTRequestContent,
                                                ScriptLibraryPOSTRequest scriptLibraryPOSTRequest) {

        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.setFunctionLibraryName(scriptLibraryPOSTRequest.getName());
        functionLibrary.setDescription(scriptLibraryPOSTRequest.getDescription());
        functionLibrary.setFunctionLibraryScript(scriptLibraryPOSTRequestContent);
        return functionLibrary;
    }

    /**
     * Create a function library object from ScriptLibraryPUTRequest.
     *
     * @param scriptLibraryName       Script library name.
     * @param scriptLibraryPUTRequest ScriptLibraryPUTRequest
     * @return functionLibrary
     */
    private FunctionLibrary createScriptLibraryPut(String scriptLibraryName, String scriptLibraryPUTRequestContent,
                                                   ScriptLibraryPUTRequest scriptLibraryPUTRequest) {

        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.setFunctionLibraryName(scriptLibraryName);
        functionLibrary.setDescription(scriptLibraryPUTRequest.getDescription());
        functionLibrary.setFunctionLibraryScript(scriptLibraryPUTRequestContent);
        return functionLibrary;
    }

    /**
     * Create a script libraries list response.
     *
     * @param scriptLibraries list of script libraries.
     * @param limit           Item per page.
     * @param offset          offset
     * @return scriptLibraryListResponse
     */
    private ScriptLibraryListResponse createScriptLibrariesList(List<FunctionLibrary> scriptLibraries,
                                                                Integer limit, Integer offset) {

        ScriptLibraryListResponse scriptLibraryListResponse = new ScriptLibraryListResponse();
        if (CollectionUtils.isNotEmpty(scriptLibraries)) {
            List<ScriptLibrary> scriptLibraryItem = new ArrayList<>();
            for (FunctionLibrary functionLibrary : scriptLibraries) {
                ScriptLibrary scriptLibrary = new ScriptLibrary();
                scriptLibrary.setName(functionLibrary.getFunctionLibraryName());
                scriptLibrary.setDescription(functionLibrary.getDescription());
                scriptLibrary.setSelf(
                        ContextLoader.buildURIForBody(
                                String.format(V1_API_PATH_COMPONENT + SCRIPT_LIBRARY_PATH_COMPONENT + "/%s",
                                        functionLibrary.getFunctionLibraryName())).toString());
                scriptLibraryItem.add(scriptLibrary);
            }
            scriptLibraryListResponse.setScriptLibraries(scriptLibraryItem.subList(
                    Math.min(scriptLibraryItem.size(), offset),
                    Math.min(scriptLibraryItem.size(), offset + limit)));
            scriptLibraryListResponse.setCount(scriptLibraryListResponse.getScriptLibraries().size());
            scriptLibraryListResponse.setTotalResults(scriptLibraries.size());
            scriptLibraryListResponse.setStartIndex(offset + 1);
        } else {
            scriptLibraryListResponse.setCount(0);
        }
        return scriptLibraryListResponse;
    }

    /**
     * Handle Client side errors.
     *
     * @param errorEnum Error Message information.
     * @param data      Error message data.
     * @return APIError
     */
    private APIError handleScriptLibraryClientError(Constants.ErrorMessage errorEnum, Response.Status status,
                                                    String... data) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDescription(String.format(errorEnum.getDescription(), data));
        errorResponse.setCode(errorEnum.getCode());
        errorResponse.setMessage(errorEnum.getMessage());
        errorResponse.setRef(Util.getCorrelation());
        return new APIError(status, errorResponse);
    }

    /**
     * Handle FunctionLibraryManagementExceptions, error description and status code to be sent
     * in the response.
     *
     * @param e         FunctionLibraryManagementException
     * @param errorEnum Error Message information.
     * @return APIError.
     */
    private APIError handleScriptLibraryError(FunctionLibraryManagementException e, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof FunctionLibraryManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e.getMessage());
            createErrorResponse(e, errorResponse);
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof FunctionLibraryManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
            createErrorResponse(e, errorResponse);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return new APIError(status, errorResponse);

    }

    /**
     * Create the error response setting the error code and description.
     *
     * @param e             FunctionLibraryManagementException
     * @param errorResponse ErrorResponse
     */
    private void createErrorResponse(FunctionLibraryManagementException e, ErrorResponse errorResponse) {

        if (e.getErrorCode() != null) {
            String errorCode = e.getErrorCode();
            errorCode =
                    errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                            errorCode : Constants.SCRIPT_LIBRARY_MANAGEMENT_PREFIX + errorCode;
            errorResponse.setCode(errorCode);
        }
        errorResponse.setDescription(e.getMessage());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(errorMsg.getDescription());
    }

}
