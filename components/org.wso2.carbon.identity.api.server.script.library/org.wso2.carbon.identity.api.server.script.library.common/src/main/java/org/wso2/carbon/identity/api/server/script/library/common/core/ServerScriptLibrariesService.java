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

package org.wso2.carbon.identity.api.server.script.library.common.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.script.library.common.Constants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.functions.library.mgt.FunctionLibraryManagementService;
import org.wso2.carbon.identity.functions.library.mgt.exception.FunctionLibraryManagementClientException;
import org.wso2.carbon.identity.functions.library.mgt.exception.FunctionLibraryManagementException;
import org.wso2.carbon.identity.functions.library.mgt.exception.FunctionLibraryManagementServerException;
import org.wso2.carbon.identity.functions.library.mgt.model.FunctionLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Call internal osgi services to perform server script library management operations. Shared by all versions of
 * the script library API. Returns function libraries, which each API version maps to its own response models.
 */
public class ServerScriptLibrariesService {

    private static final Log log = LogFactory.getLog(ServerScriptLibrariesService.class);
    private final FunctionLibraryManagementService functionLibraryManagementService;

    public ServerScriptLibrariesService(FunctionLibraryManagementService functionLibraryManagementService) {

        this.functionLibraryManagementService = functionLibraryManagementService;
    }

    /**
     * Get a page of script libraries.
     *
     * @param limit  Items per page.
     * @param offset Offset.
     * @return Script library page.
     */
    public ScriptLibraryPage getScriptLibraries(Integer limit, Integer offset) {

        List<FunctionLibrary> functionLibraries;
        try {
            functionLibraries = functionLibraryManagementService.listFunctionLibraries(ContextLoader
                    .getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_SCRIPT_LIBRARIES);
        }
        int validatedLimit = validateLimit(limit);
        int validatedOffset = validateOffset(offset);
        if (CollectionUtils.isEmpty(functionLibraries)) {
            return new ScriptLibraryPage(Collections.emptyList(), 0, validatedOffset);
        }
        List<FunctionLibrary> page = functionLibraries.subList(
                Math.min(functionLibraries.size(), validatedOffset),
                Math.min(functionLibraries.size(), validatedOffset + validatedLimit));
        return new ScriptLibraryPage(page, functionLibraries.size(), validatedOffset);
    }

    /**
     * Get a script library by name.
     *
     * @param scriptLibraryName Name of the script library.
     * @return Function library.
     */
    public FunctionLibrary getScriptLibrary(String scriptLibraryName) {

        if (!isScriptLibraryAvailable(scriptLibraryName)) {
            throw scriptLibraryNotFoundError(scriptLibraryName);
        }
        try {
            return functionLibraryManagementService.getFunctionLibrary(scriptLibraryName,
                    ContextLoader.getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
        }
    }

    /**
     * Get the content of a script library.
     *
     * @param scriptLibraryName Name of the script library.
     * @return Script library content.
     */
    public String getScriptLibraryContent(String scriptLibraryName) {

        return getScriptLibrary(scriptLibraryName).getFunctionLibraryScript();
    }

    /**
     * Add a script library.
     *
     * @param name               Name of the script library.
     * @param contentInputStream Content of the script library code.
     * @param description        Description of the script library.
     */
    public void addScriptLibrary(String name, InputStream contentInputStream, String description) {

        String content = readContent(contentInputStream, Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SCRIPT_LIBRARY);
        if (isScriptLibraryAvailable(name)) {
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_ALREADY_FOUND,
                    Response.Status.CONFLICT, name, ContextLoader.getTenantDomainFromContext());
        }
        if (!name.contains(Constants.SCRIPT_LIBRARY_EXTENSION)) {
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NAME_VALIDATION,
                    Response.Status.BAD_REQUEST);
        }
        try {
            functionLibraryManagementService.createFunctionLibrary(buildFunctionLibrary(name, description, content),
                    ContextLoader.getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SCRIPT_LIBRARY);
        }
    }

    /**
     * Update a script library.
     *
     * @param scriptLibraryName  Name of the script library.
     * @param contentInputStream Content of the script library code.
     * @param description        Description of the script library.
     */
    public void updateScriptLibrary(String scriptLibraryName, InputStream contentInputStream, String description) {

        String content = readContent(contentInputStream,
                Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY);
        if (!isScriptLibraryAvailable(scriptLibraryName)) {
            throw scriptLibraryNotFoundError(scriptLibraryName);
        }
        try {
            functionLibraryManagementService.updateFunctionLibrary(scriptLibraryName,
                    buildFunctionLibrary(scriptLibraryName, description, content),
                    ContextLoader.getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY);
        }
    }

    /**
     * Delete a script library. Deleting a script library that does not exist is a no-op.
     *
     * @param scriptLibraryName Name of the script library.
     */
    public void deleteScriptLibrary(String scriptLibraryName) {

        if (!isScriptLibraryAvailable(scriptLibraryName)) {
            return;
        }
        try {
            functionLibraryManagementService.deleteFunctionLibrary(scriptLibraryName,
                    ContextLoader.getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_SCRIPT_LIBRARY);
        }
    }

    /**
     * Check whether a script library exists.
     *
     * @param scriptLibraryName Name of the script library.
     * @return True if the script library exists.
     */
    private boolean isScriptLibraryAvailable(String scriptLibraryName) {

        try {
            return functionLibraryManagementService.isFunctionLibraryExists(scriptLibraryName,
                    ContextLoader.getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
            throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
        }
    }

    /**
     * Build an API error for the given client error.
     *
     * @param errorEnum Error message information.
     * @param status    HTTP status of the error response.
     * @param data      Error message data.
     * @return APIError.
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

    private int validateOffset(Integer offset) {

        if (offset != null && offset >= 0) {
            return offset;
        }
        throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_OFFSET_VALIDATION,
                Response.Status.BAD_REQUEST);
    }

    private int validateLimit(Integer limit) {

        final int maximumItemPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit == null) {
            return IdentityUtil.getDefaultItemsPerPage();
        }
        return Math.min(limit, maximumItemPerPage);
    }

    private String readContent(InputStream contentInputStream, Constants.ErrorMessage errorEnum) {

        try {
            return IOUtils.toString(contentInputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("Error occurred while reading contentInputStream: " + e);
            throw handleScriptLibraryClientError(errorEnum, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private APIError scriptLibraryNotFoundError(String scriptLibraryName) {

        return handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                Response.Status.NOT_FOUND, scriptLibraryName, ContextLoader.getTenantDomainFromContext());
    }

    private FunctionLibrary buildFunctionLibrary(String name, String description, String content) {

        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.setFunctionLibraryName(name);
        functionLibrary.setDescription(description);
        functionLibrary.setFunctionLibraryScript(content);
        return functionLibrary;
    }

    private APIError handleScriptLibraryError(FunctionLibraryManagementException e, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof FunctionLibraryManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e.getMessage());
            setErrorCodeAndDescription(e, errorResponse);
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof FunctionLibraryManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
            setErrorCodeAndDescription(e, errorResponse);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private void setErrorCodeAndDescription(FunctionLibraryManagementException e, ErrorResponse errorResponse) {

        if (e.getErrorCode() != null) {
            String errorCode = e.getErrorCode();
            errorCode =
                    errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                            errorCode : Constants.SCRIPT_LIBRARY_MANAGEMENT_PREFIX + errorCode;
            errorResponse.setCode(errorCode);
        }
        errorResponse.setDescription(e.getMessage());
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(errorMsg.getDescription());
    }
}
