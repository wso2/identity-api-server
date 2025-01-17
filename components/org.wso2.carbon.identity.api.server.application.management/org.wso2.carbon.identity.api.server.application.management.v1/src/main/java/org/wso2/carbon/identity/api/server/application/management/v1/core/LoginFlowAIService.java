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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.identity.ai.service.mgt.exceptions.AIClientException;
import org.wso2.carbon.identity.ai.service.mgt.exceptions.AIServerException;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowResultResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowStatusResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.StatusEnum;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.AI_RESPONSE_DATA_KEY;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.AI_RESPONSE_STATUS_KEY;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.AUTHENTICATOR_IDP_KEY;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.AUTHENTICATOR_NAME_KEY;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.CLAIM_URI_KEY;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.DESCRIPTION_KEY;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_LOGINFLOW_AI_RESULT;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_LOGINFLOW_AI_RESULT_STATUS;

/**
 * Service class for login flow AI related operations.
 */
public class LoginFlowAIService {

    private static final Log log = LogFactory.getLog(LoginFlowAIService.class);

    /**
     * Generate authentication sequence.
     *
     * @param loginFlowGenerateRequest LoginFlowGenerateRequest.
     * @return LoginFlowGenerateResponse.
     */
    public LoginFlowGenerateResponse generateAuthenticationSequence(LoginFlowGenerateRequest loginFlowGenerateRequest) {

        try {
            List<Map<String, Object>> userClaims = loginFlowGenerateRequest.getUserClaims();
            JSONArray userClaimsJsonArray = new JSONArray();
            for (Map<String, Object> userClaim : userClaims) {
                userClaimsJsonArray.put(new JSONObject()
                        .put(CLAIM_URI_KEY, userClaim.get(CLAIM_URI_KEY))
                        .put(DESCRIPTION_KEY, userClaim.get(DESCRIPTION_KEY)));
            }

            JSONObject availableAuthenticators = new JSONObject();
            Map<String, List<Map<String, Object>>> authenticators = loginFlowGenerateRequest
                    .getAvailableAuthenticators();
            for (Map.Entry<String, List<Map<String, Object>>> entry : authenticators.entrySet()) {
                String authenticatorType = entry.getKey();
                List<Map<String, Object>> authenticatorList = entry.getValue();

                JSONArray authenticatorsJsonArray = new JSONArray();
                for (Map<String, Object> authenticator : authenticatorList) {
                    authenticatorsJsonArray.put(new JSONObject()
                            .put(AUTHENTICATOR_NAME_KEY, authenticator.get(AUTHENTICATOR_NAME_KEY))
                            .put(AUTHENTICATOR_IDP_KEY, authenticator.get(AUTHENTICATOR_IDP_KEY))
                            .put(DESCRIPTION_KEY, authenticator.get(DESCRIPTION_KEY)));
                }
                availableAuthenticators.put(authenticatorType, authenticatorsJsonArray);
            }

            String operationId = ApplicationManagementServiceHolder.getLoginFlowAIManagementService()
                    .generateAuthenticationSequence(loginFlowGenerateRequest.getUserQuery(), userClaimsJsonArray,
                            availableAuthenticators);
            LoginFlowGenerateResponse response = new LoginFlowGenerateResponse();
            response.setOperationId(operationId);
            return response;
        } catch (AIServerException e) {
            throw handleServerException(e);
        } catch (AIClientException e) {
            throw handleClientException(e);
        }
    }

    /**
     * Get login flow AI generation result.
     *
     * @param operationId Operation ID of the login flow AI generation.
     * @return LoginFlowResultResponse.
     */
    public LoginFlowResultResponse getLoginFlowAIGenerationResult(String operationId) {

        try {
            Object generationResult = ApplicationManagementServiceHolder.getLoginFlowAIManagementService()
                    .getAuthenticationSequenceGenerationResult(operationId);
            LoginFlowResultResponse response = new LoginFlowResultResponse();
            Map<String, Object> generationResultMap = (Map<String, Object>) generationResult;
            response.setStatus(getStatusFromResult(generationResultMap));
            if (!((Map<?, ?>) generationResult).containsKey(AI_RESPONSE_DATA_KEY)) {
                throw new AIServerException(ERROR_CODE_ERROR_GETTING_LOGINFLOW_AI_RESULT_STATUS.getMessage(),
                        ERROR_CODE_ERROR_GETTING_LOGINFLOW_AI_RESULT_STATUS.getCode());
            }
            Map<String, Object> dataMap = (Map<String, Object>) generationResultMap.get(AI_RESPONSE_DATA_KEY);
            response.setData(dataMap);
            return response;
        } catch (AIServerException e) {
            throw handleServerException(e);
        } catch (AIClientException e) {
            throw handleClientException(e);
        }
    }

    /**
     * Get login flow AI generation status.
     *
     * @param operationId Operation ID of the login flow AI generation.
     * @return LoginFlowStatusResponse.
     */
    public LoginFlowStatusResponse getLoginFlowAIStatus(String operationId) {

        try {
            Object generationStatus = ApplicationManagementServiceHolder.getLoginFlowAIManagementService()
                    .getAuthenticationSequenceGenerationStatus(operationId);
            LoginFlowStatusResponse response = new LoginFlowStatusResponse();
            response.setOperationId(operationId);

            response.status(convertObjectToMap(generationStatus));
            return response;
        } catch (AIServerException e) {
            throw handleServerException(e);
        } catch (AIClientException e) {
            throw handleClientException(e);
        }
    }

    private StatusEnum getStatusFromResult(Map<String, Object> resultMap)
            throws AIServerException {

        if (resultMap.containsKey(AI_RESPONSE_STATUS_KEY)) {
            String status = (String) resultMap.get(AI_RESPONSE_STATUS_KEY);
            if ("IN_PROGRESS".equals(status)) {
                return StatusEnum.IN_PROGRESS;
            } else if ("COMPLETED".equals(status)) {
                return StatusEnum.COMPLETED;
            } else if ("FAILED".equals(status)) {
                return StatusEnum.FAILED;
            }
        }
        throw new AIServerException(ERROR_CODE_ERROR_GETTING_LOGINFLOW_AI_RESULT.getMessage(),
                ERROR_CODE_ERROR_GETTING_LOGINFLOW_AI_RESULT.getCode());
    }

    private APIError handleClientException(AIClientException error) {

        ErrorResponse.Builder errorResponseBuilder = new ErrorResponse.Builder()
                .withCode(error.getErrorCode())
                .withMessage(error.getMessage());
        if (error.getServerMessage() != null) {
            Response.Status status = Response.Status.fromStatusCode(error.getServerStatusCode());
            errorResponseBuilder.withDescription(error.getServerMessage());
            return new APIError(status, errorResponseBuilder.build());
        }
        return new APIError(Response.Status.BAD_REQUEST, errorResponseBuilder.build());
    }

    private APIError handleServerException(AIServerException error) {

        ErrorResponse.Builder errorResponseBuilder = new ErrorResponse.Builder()
                .withCode(error.getErrorCode())
                .withMessage(error.getMessage());
        if (error.getServerMessage() != null) {
            Response.Status status = Response.Status.fromStatusCode(error.getServerStatusCode());
            errorResponseBuilder.withDescription(error.getServerMessage());
            return new APIError(status, errorResponseBuilder.build());
        }
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponseBuilder.build());
    }

    private static Map<String, Object> convertObjectToMap(Object object) {

        if (object instanceof Map) {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    map.put(key, "");
                } else if (value instanceof Map) {
                    map.put(key, convertObjectToMap(value));
                } else if (value instanceof List) {
                    map.put(key, convertListToArray((List<?>) value));
                } else {
                    map.put(key, value);
                }
            }
            return map;
        }
        log.warn("Object is not an instance of Map. Returning an empty map.");
        return new HashMap<>();
    }

    private static Object[] convertListToArray(List<?> list) {

        Object[] array = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object value = list.get(i);
            if (value == null) {
                array[i] = "";
            } else if (value instanceof Map) {
                array[i] = convertObjectToMap(value);
            } else if (value instanceof List) {
                array[i] = convertListToArray((List<?>) value);
            } else {
                array[i] = value;
            }
        }
        return array;
    }
}
