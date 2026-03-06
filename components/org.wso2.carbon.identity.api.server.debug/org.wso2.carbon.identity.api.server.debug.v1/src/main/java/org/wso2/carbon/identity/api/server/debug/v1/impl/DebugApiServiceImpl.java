/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.v1.DebugApiService;
import org.wso2.carbon.identity.api.server.debug.v1.constants.DebugConstants;
import org.wso2.carbon.identity.api.server.debug.v1.core.DebugService;
import org.wso2.carbon.identity.api.server.debug.v1.factories.DebugServiceFactory;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponseMetadata;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;
import org.wso2.carbon.identity.api.server.debug.v1.model.Error;
import org.wso2.carbon.identity.debug.framework.DebugFrameworkConstants;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Util.getCorrelation;

/**
 * Implementation of the DebugAService interface.
 */
public class DebugApiServiceImpl implements DebugApiService {
    private static final Log LOG = LogFactory.getLog(DebugApiServiceImpl.class);
    private static final String ERROR_MESSAGE_MISSING_DEBUG_ID =
            "Debug framework response does not contain debugId or state.";

    private final DebugService debugService;

    public DebugApiServiceImpl() {

        this.debugService = DebugServiceFactory.getDebugService();
    }

    @Override
    public Response startDebugSession(String resourceType, DebugConnectionRequest debugConnectionRequest) {

        try {
            Map<String, Object> debugResult = debugService.processStartSession(resourceType, debugConnectionRequest);
            return Response.ok().entity(buildDebugConnectionResponse(debugResult)).build();
        } catch (DebugFrameworkClientException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Invalid debug request.", e);
            }
            return buildErrorResponse(Response.Status.BAD_REQUEST,
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST.getCode(),
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST.getMessage(),
                    resolveClientErrorDescription(e,
                            DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST.getDescription()));
        } catch (DebugFrameworkServerException e) {
            LOG.error("Error occurred while processing debug request.", e);
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getCode(),
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getMessage(),
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getDescription());
        }
    }

    @Override
    public Response getDebugResult(String debugId) {

        try {
            Map<String, Object> frameworkResponse = debugService.processGetResult(debugId);
            if (frameworkResponse == null) {
                return buildResultNotFoundResponse();
            }
            DebugResult apiResponse = buildDebugResponse(frameworkResponse, debugId);
            return Response.ok().entity(apiResponse).build();
        } catch (DebugFrameworkClientException e) {
            if (DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getCode().equals(e.getErrorCode())) {
                return buildResultNotFoundResponse();
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Invalid debug result request.", e);
            }
            return buildErrorResponse(Response.Status.BAD_REQUEST,
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST.getCode(),
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST.getMessage(),
                    resolveClientErrorDescription(e,
                            DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST.getDescription()));
        } catch (DebugFrameworkServerException e) {
            LOG.error("Error occurred while retrieving debug result.", e);
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getCode(),
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getMessage(),
                    DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getDescription());
        }
    }

    private DebugConnectionResponse buildDebugConnectionResponse(Map<String, Object> debugResult)
            throws DebugFrameworkServerException {

        DebugConnectionResponse response = new DebugConnectionResponse();

        Object debugId = debugResult.get(DebugConstants.ResponseKeys.SESSION_ID);
        if (debugId == null) {
            debugId = debugResult.get(DebugConstants.ResponseKeys.STATE);
        }
        if (debugId == null) {
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    ERROR_MESSAGE_MISSING_DEBUG_ID);
        }
        response.setDebugId(debugId.toString());
        DebugConnectionResponse.StatusEnum status
            = resolveConnectionStatus(debugResult.get(DebugConstants.ResponseKeys.STATUS));
        response.setStatus(status);
        response.setMessage(resolveConnectionMessage(status, debugResult.get(DebugConstants.ResponseKeys.MESSAGE)));
        Object timestamp = debugResult.get(DebugConstants.ResponseKeys.TIMESTAMP);
        if (timestamp instanceof Number) {
            response.setTimestamp(((Number) timestamp).longValue());
        } else {
            response.setTimestamp(System.currentTimeMillis());
        }
        Object authorizationUrl = debugResult.get(DebugConstants.ResponseKeys.AUTHORIZATION_URL);
        if (authorizationUrl != null) {
            DebugConnectionResponseMetadata metadata = new DebugConnectionResponseMetadata();
            metadata.setAuthorizationUrl(authorizationUrl.toString());
            response.setMetadata(metadata);
        }

        return response;
    }

    private DebugConnectionResponse.StatusEnum resolveConnectionStatus(Object status) {

        if (status == null) {
            return DebugConnectionResponse.StatusEnum.SUCCESS;
        }
        String statusValue = status.toString().toUpperCase(Locale.ROOT);
        for (DebugConnectionResponse.StatusEnum candidate : DebugConnectionResponse.StatusEnum.values()) {
            if (candidate.name().equals(statusValue)) {
                return candidate;
            }
        }
        LOG.warn("Unrecognized debug status from framework: " + status + ". Falling back to SUCCESS.");
        return DebugConnectionResponse.StatusEnum.SUCCESS;
    }

    private String resolveConnectionMessage(DebugConnectionResponse.StatusEnum status, Object frameworkMessage) {

        if (frameworkMessage != null && !frameworkMessage.toString().trim().isEmpty()) {
            return frameworkMessage.toString();
        }
        switch (status) {
            case IN_PROGRESS:
                return "Debug session is in progress";
            case FAILURE:
                return "Debug session execution failed";
            case DIRECT_RESULT:
                return "Debug session completed with direct result";
            case SUCCESS:
            default:
                return "Debug session executed successfully";
        }
    }

    private Response buildErrorResponse(Response.Status status, String code, String message, String description) {

        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        error.setDescription(description);
        error.setTraceId(getCorrelation());
        return Response.status(status).entity(error).build();
    }

    private String resolveClientErrorDescription(DebugFrameworkClientException exception, String fallback) {

        if (exception == null) {
            return fallback;
        }
        if (exception.getDescription() != null && !exception.getDescription().trim().isEmpty()) {
            return exception.getDescription();
        }
        if (exception.getMessage() != null && !exception.getMessage().trim().isEmpty()) {
            return exception.getMessage();
        }
        return fallback;
    }

    private DebugResult buildDebugResponse(Map<String, Object> frameworkResponse,
                                           String requestedDebugId) {

        DebugResult response = new DebugResult();

        Object debugId = frameworkResponse.get(DebugConstants.ResponseKeys.SESSION_ID);
        if (debugId == null) {
            debugId = frameworkResponse.get(DebugConstants.ResponseKeys.STATE);
        }
        if (debugId == null) {
            debugId = requestedDebugId;
        }
        if (debugId != null) {
            response.setDebugId(debugId.toString());
        }

        DebugResult.StatusEnum status = resolveResultStatus(
                frameworkResponse.get(DebugConstants.ResponseKeys.STATUS),
                frameworkResponse.get(DebugConstants.ResponseKeys.SUCCESS));
        response.setStatus(status);

        Map<String, Object> metadata = new LinkedHashMap<>();
        if (debugId != null) {
            metadata.put(DebugConstants.ResponseKeys.SESSION_ID, debugId.toString());
        }
        for (Map.Entry<String, Object> entry : frameworkResponse.entrySet()) {
            String key = entry.getKey();
            if (!DebugConstants.ResponseKeys.SESSION_ID.equals(key)
                    && !DebugConstants.ResponseKeys.STATUS.equals(key)
                    && !DebugConstants.ResponseKeys.SUCCESS.equals(key)
                    && !DebugConstants.ResponseKeys.STATE.equals(key)) {
                metadata.put(key, entry.getValue());
            }
        }
        response.setMetadata(metadata);

        return response;
    }

    private DebugResult.StatusEnum resolveResultStatus(Object status, Object success) {

        if (status != null) {
            String statusValue = status.toString().toUpperCase(Locale.ROOT);
            for (DebugResult.StatusEnum candidate : DebugResult.StatusEnum.values()) {
                if (candidate.name().equals(statusValue)) {
                    return candidate;
                }
            }
            LOG.warn("Unrecognized debug result status from framework: " + status + ". Falling back.");
        }

        if (success instanceof Boolean) {
            return (Boolean) success ? DebugResult.StatusEnum.SUCCESS : DebugResult.StatusEnum.FAILURE;
        }

        return DebugResult.StatusEnum.FAILURE;
    }

    private Response buildResultNotFoundResponse() {

        return buildErrorResponse(Response.Status.NOT_FOUND,
                DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND.getCode(),
                DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND.getMessage(),
                DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND.getDescription());
    }
}
