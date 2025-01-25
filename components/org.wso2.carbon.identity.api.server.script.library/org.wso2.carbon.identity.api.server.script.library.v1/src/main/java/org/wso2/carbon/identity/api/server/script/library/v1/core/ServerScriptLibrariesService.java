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

        List<FunctionLibrary> functionLibraries;
        try {
            functionLibraries = functionLibraryManagementService.listFunctionLibraries(ContextLoader
                    .getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
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
            return IdentityUtil.getDefaultItemsPerPage();
        } else if (limit <= maximumItemPerPage) {
            return limit;
        } else {
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

        if (isScriptLibraryAvailable(scriptLibraryId)) {
            FunctionLibrary functionLibrary;
            try {
                functionLibrary = functionLibraryManagementService.getFunctionLibrary(scriptLibraryId,
                        ContextLoader.getTenantDomainFromContext());
            } catch (FunctionLibraryManagementException e) {
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
            }
            return createScriptLibraryResponse(functionLibrary);
        } else {
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                    Response.Status.NOT_FOUND, scriptLibraryId, ContextLoader.getTenantDomainFromContext());
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

        ScriptLibraryPOSTRequest scriptLibraryPOSTRequest = new ScriptLibraryPOSTRequest();
        scriptLibraryPOSTRequest.setName(name);
        scriptLibraryPOSTRequest.setDescription(description);
        String scriptLibraryPOSTRequestContent;
        try {
            scriptLibraryPOSTRequestContent = IOUtils.toString(contentInputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("Error occurred while reading contentInputStream: " + e);
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SCRIPT_LIBRARY,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (isScriptLibraryAvailable(scriptLibraryPOSTRequest.getName())) {
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_ALREADY_FOUND,
                    Response.Status.CONFLICT, scriptLibraryPOSTRequest.getName(),
                    ContextLoader.getTenantDomainFromContext());
        } else {
            FunctionLibrary functionLibrary = createScriptLibrary(scriptLibraryPOSTRequestContent,
                    scriptLibraryPOSTRequest);
            try {
                if (scriptLibraryPOSTRequest.getName().contains(Constants.SCRIPT_LIBRARY_EXTENSION)) {
                    functionLibraryManagementService.createFunctionLibrary(functionLibrary, ContextLoader
                            .getTenantDomainFromContext());
                } else {
                    throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NAME_VALIDATION,
                            Response.Status.BAD_REQUEST);
                }
            } catch (FunctionLibraryManagementException e) {
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

        ScriptLibraryPUTRequest scriptLibraryPUTRequest = new ScriptLibraryPUTRequest();
        scriptLibraryPUTRequest.setDescription(description);
        String scriptLibraryPUTRequestContent;
        try {
            scriptLibraryPUTRequestContent = IOUtils.toString(contentInputStream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("Error occurred while reading contentInputStream: " + e);
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (isScriptLibraryAvailable(scriptLibraryName)) {
            FunctionLibrary functionLibrary = createScriptLibraryPut(scriptLibraryName, scriptLibraryPUTRequestContent,
                    scriptLibraryPUTRequest);
            try {
                functionLibraryManagementService.updateFunctionLibrary(scriptLibraryName, functionLibrary,
                        ContextLoader.getTenantDomainFromContext());
            } catch (FunctionLibraryManagementException e) {
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY);
            }
        } else {
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                    Response.Status.NOT_FOUND, scriptLibraryName, ContextLoader.getTenantDomainFromContext());
        }

    }

    /**
     * Get the script library content.
     *
     * @param scriptLibraryName Name of the script library
     * @return Script library content
     */
    public String getScriptLibraryContentByName(String scriptLibraryName) {

        if (isScriptLibraryAvailable(scriptLibraryName)) {
            try {
                FunctionLibrary functionLibrary = functionLibraryManagementService
                        .getFunctionLibrary(scriptLibraryName, ContextLoader.getTenantDomainFromContext());
                return functionLibrary.getFunctionLibraryScript();
            } catch (FunctionLibraryManagementException e) {
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY);
            }
        } else {
            throw handleScriptLibraryClientError(Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_NOT_FOUND,
                    Response.Status.NOT_FOUND, scriptLibraryName, ContextLoader.getTenantDomainFromContext());
        }
    }

    /**
     * Delete a script library.
     *
     * @param scriptLibraryId script library resource ID.
     */
    public void deleteScriptLibrary(String scriptLibraryId) {

        if (isScriptLibraryAvailable(scriptLibraryId)) {
            try {
                functionLibraryManagementService.deleteFunctionLibrary(scriptLibraryId,
                        ContextLoader.getTenantDomainFromContext());
            } catch (FunctionLibraryManagementException e) {
                throw handleScriptLibraryError(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_SCRIPT_LIBRARY);
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

        boolean isAvailable;
        try {
            isAvailable = functionLibraryManagementService.isFunctionLibraryExists(scriptLibraryName,
                    ContextLoader.getTenantDomainFromContext());
        } catch (FunctionLibraryManagementException e) {
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
