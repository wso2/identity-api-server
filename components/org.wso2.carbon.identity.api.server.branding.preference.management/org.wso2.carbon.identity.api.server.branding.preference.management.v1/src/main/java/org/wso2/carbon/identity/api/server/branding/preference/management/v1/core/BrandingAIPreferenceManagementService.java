/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceServiceHolder;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationRequestModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationResponseModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationResultModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationStatusModel;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.branding.preference.management.core.exception.BrandingAIClientException;
import org.wso2.carbon.identity.branding.preference.management.core.exception.BrandingAIServerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_BRANDING_RESULT;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_BRANDING_RESULT_STATUS;

/**
 * Service class for AI branding preference management.
 */
public class BrandingAIPreferenceManagementService {

    private static final Log log = LogFactory.getLog(BrandingAIPreferenceManagementService.class);

    /**
     * Initiates the branding preference generation process by invoking BrandingPreferenceManager.
     *
     * @param brandingGenerationRequestModel BrandingGenerationRequestModel.
     * @return BrandingGenerationResponseModel.
     */
    public BrandingGenerationResponseModel generateBrandingPreference(
            BrandingGenerationRequestModel brandingGenerationRequestModel) {

        try {
            String operationId = BrandingPreferenceServiceHolder.getBrandingPreferenceAiManager()
                    .generateBrandingPreference(brandingGenerationRequestModel.getWebsiteUrl());
            BrandingGenerationResponseModel response = new BrandingGenerationResponseModel();
            response.setOperationId(operationId);
            return response;
        } catch (BrandingAIServerException e) {
            throw handleServerException(e);
        } catch (BrandingAIClientException e) {
            throw handleClientException(e);
        }
    }

    /**
     * Retrieves the branding preference generation status by invoking BrandingPreferenceManager.
     *
     * @param operationId operation id of the branding preference generation process.
     * @return BrandingGenerationStatusModel.
     */
    public BrandingGenerationStatusModel getBrandingPreferenceGenerationStatus(String operationId) {

        try {
            Object generationStatus = BrandingPreferenceServiceHolder.getBrandingPreferenceAiManager()
                    .getBrandingPreferenceGenerationStatus(operationId);
            BrandingGenerationStatusModel response = new BrandingGenerationStatusModel();
            response.setStatus(convertObjectToMap(generationStatus));
            return response;
        } catch (BrandingAIServerException e) {
            throw handleServerException(e);
        } catch (BrandingAIClientException e) {
            throw handleClientException(e);
        }
    }

    /**
     * Retrieves the branding preference generation result by invoking BrandingPreferenceManager.
     *
     * @param operationId operation id of the branding preference generation process.
     * @return BrandingGenerationResultModel.
     */
    public BrandingGenerationResultModel getBrandingPreferenceGenerationResult(String operationId) {

        try {
            Object generationResult = BrandingPreferenceServiceHolder.getBrandingPreferenceAiManager()
                    .getBrandingPreferenceGenerationResult(operationId);
            BrandingGenerationResultModel response = new BrandingGenerationResultModel();

            Map<String, Object> resultMap = convertObjectToMap(generationResult);
            response.setStatus(getStatusFromResult(resultMap));

            if (!resultMap.containsKey("data")) {
                throw new BrandingAIServerException(ERROR_CODE_ERROR_GETTING_BRANDING_RESULT_STATUS.getMessage(),
                        ERROR_CODE_ERROR_GETTING_BRANDING_RESULT_STATUS.getCode());
            }

            Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
            response.setData(dataMap);
            return response;
        } catch (BrandingAIServerException e) {
            throw handleServerException(e);
        } catch (BrandingAIClientException e) {
            throw handleClientException(e);
        }
    }

    private BrandingGenerationResultModel.StatusEnum getStatusFromResult(Map<String, Object> resultMap)
            throws BrandingAIServerException {

        if (resultMap.containsKey("status")) {
            String status = (String) resultMap.get("status");
            if ("IN_PROGRESS".equals(status)) {
                return BrandingGenerationResultModel.StatusEnum.IN_PROGRESS;
            } else if ("COMPLETED".equals(status)) {
                return BrandingGenerationResultModel.StatusEnum.COMPLETED;
            } else if ("FAILED".equals(status)) {
                return BrandingGenerationResultModel.StatusEnum.FAILED;
            }
        }
        throw new BrandingAIServerException(ERROR_CODE_ERROR_GETTING_BRANDING_RESULT.getMessage(),
                ERROR_CODE_ERROR_GETTING_BRANDING_RESULT.getCode());
    }


    private APIError handleClientException(BrandingAIClientException error) {

        ErrorResponse.Builder errorResponseBuilder = new ErrorResponse.Builder()
                .withCode(error.getErrorCode())
                .withMessage(error.getMessage());
        if (error.getBrandingAIResponse() != null) {
            Response.Status status = Response.Status.fromStatusCode(error.getBrandingAIResponse().getStatusCode());
            errorResponseBuilder.withDescription(error.getBrandingAIResponse().getResponseBody());
            return new APIError(status, errorResponseBuilder.build());
        }
        return new APIError(Response.Status.BAD_REQUEST, errorResponseBuilder.build());
    }

    private APIError handleServerException(BrandingAIServerException error) {

        ErrorResponse.Builder errorResponseBuilder = new ErrorResponse.Builder()
                .withCode(error.getErrorCode())
                .withMessage(error.getMessage());
        if (error.getBrandingAIResponse() != null) {
            Response.Status status = Response.Status.fromStatusCode(error.getBrandingAIResponse().getStatusCode());
            errorResponseBuilder.withDescription(error.getBrandingAIResponse().getResponseBody());
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



