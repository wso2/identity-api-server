/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceServiceHolder;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.core.utils.BrandingPreferenceUtils;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.branding.preference.mgt.exception.BrandingPreferenceMgtClientException;
import org.wso2.carbon.identity.branding.preference.mgt.exception.BrandingPreferenceMgtException;
import org.wso2.carbon.identity.branding.preference.mgt.exception.BrandingPreferenceMgtServerException;
import org.wso2.carbon.identity.branding.preference.mgt.model.BrandingPreference;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.DEFAULT_LOCALE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_INVALID_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ORGANIZATION_TYPE;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * Invoke internal OSGi service to perform branding preference management operations.
 */
public class BrandingPreferenceManagementService {

    private static final Log log = LogFactory.getLog(BrandingPreferenceManagementService.class);
    //TODO: Improve API to manage application level & language level theming resources in addition to the tenant level.

    /**
     * Create a branding preference resource with a resource file.
     *
     * @param brandingPreferenceModel Branding preference post request.
     * @return created branding preference model.
     */
    public BrandingPreferenceModel addBrandingPreference(BrandingPreferenceModel brandingPreferenceModel) {

        String tenantDomain = getTenantDomainFromContext();
        String preferencesJSON = generatePreferencesJSONFromRequest(brandingPreferenceModel.getPreference());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_BRANDING_PREFERENCE, null);
        }

        BrandingPreference requestDTO, responseDTO;
        try {
            requestDTO = buildRequestDTOFromBrandingRequest(brandingPreferenceModel);
            responseDTO = BrandingPreferenceServiceHolder.getBrandingPreferenceManager().
                    addBrandingPreference(requestDTO);
        } catch (BrandingPreferenceMgtException e) {
            if (BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Branding preferences are already exists for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_BRANDING_PREFERENCE,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_ADDING_BRANDING_PREFERENCE, tenantDomain);
        }
        return buildBrandingResponseFromResponseDTO(responseDTO);
    }

    /**
     * Delete branding preference resource.
     *
     * @param type   Resource type.
     * @param name   Name.
     * @param locale Language preference.
     */
    public void deleteBrandingPreference(String type, String name, String locale) {

        String tenantDomain = getTenantDomainFromContext();
        if (ORGANIZATION_TYPE.equals(type)) {
            name = tenantDomain;
        }

        try {
            BrandingPreferenceServiceHolder.getBrandingPreferenceManager().deleteBrandingPreference(type, name, locale);
        } catch (BrandingPreferenceMgtException e) {
            if (BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a branding preferences to delete for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_DELETING_BRANDING_PREFERENCE, tenantDomain);
        }
    }

    /**
     * Retrieve the requested branding preferences.
     *
     * @param type   Resource Type.
     * @param name   Name.
     * @param locale Language preference.
     * @return The requested branding preference resource. If not exists return the default preferences.
     */
    public BrandingPreferenceModel getBrandingPreference(String type, String name, String locale) {

        /**
         * Currently this API provides the support to only configure tenant wise branding preference for 'en-US' locale.
         * So always retrieve customized default branding preference.
         */
        String tenantDomain = getTenantDomainFromContext();
        try {
            // Get default branding preference.
            BrandingPreference responseDTO = BrandingPreferenceServiceHolder.getBrandingPreferenceManager().
                    getBrandingPreference(ORGANIZATION_TYPE, tenantDomain, DEFAULT_LOCALE);

            return buildBrandingResponseFromResponseDTO(responseDTO);
        } catch (BrandingPreferenceMgtException e) {
            if (BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a branding preference configurations for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_GETTING_BRANDING_PREFERENCE, tenantDomain);
        }
    }

    /**
     * Update branding preferences.
     *
     * @param brandingPreferenceModel Branding Preference Model with new preferences.
     * @return Updated branding preference model.
     */
    public BrandingPreferenceModel updateBrandingPreference(BrandingPreferenceModel brandingPreferenceModel) {

        String tenantDomain = getTenantDomainFromContext();
        String preferencesJSON = generatePreferencesJSONFromRequest(brandingPreferenceModel.getPreference());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_BRANDING_PREFERENCE, null);
        }

        BrandingPreference requestDTO, responseDTO;
        try {
            requestDTO = buildRequestDTOFromBrandingRequest(brandingPreferenceModel);
            responseDTO = BrandingPreferenceServiceHolder.getBrandingPreferenceManager().
                    replaceBrandingPreference(requestDTO);
        } catch (BrandingPreferenceMgtException e) {
            if (BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a branding preferences to update for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE, tenantDomain);
        }
        return buildBrandingResponseFromResponseDTO(responseDTO);
    }

    /**
     * Build branding preference requestDTO from request body.
     *
     * @param brandingModel Branding preference request body.
     * @return Branding preference requestDTO object.
     */
    private BrandingPreference buildRequestDTOFromBrandingRequest(BrandingPreferenceModel brandingModel) {

        BrandingPreference brandingRequestDTO = new BrandingPreference();
        brandingRequestDTO.setType(brandingModel.getType().toString());
        if (ORGANIZATION_TYPE.equals(brandingModel.getType().toString())) {
            brandingRequestDTO.setName(getTenantDomainFromContext());
        } else {
            brandingRequestDTO.setName(brandingModel.getName());
        }
        if (StringUtils.isBlank(brandingModel.getLocale())) {
            brandingRequestDTO.setLocale(DEFAULT_LOCALE);
        } else {
            brandingRequestDTO.setLocale(brandingModel.getLocale());
        }
        brandingRequestDTO.setPreference(brandingModel.getPreference());
        return brandingRequestDTO;
    }

    /**
     * Build branding preference response object from the responseDTO.
     *
     * @param responseDTO Branding preference responseDTO object.
     * @return Branding preference response object{@link BrandingPreferenceModel}.
     */
    private BrandingPreferenceModel buildBrandingResponseFromResponseDTO(BrandingPreference responseDTO) {

        BrandingPreferenceModel brandingPreferenceResponse = new BrandingPreferenceModel();
        brandingPreferenceResponse.setType(BrandingPreferenceModel.TypeEnum.valueOf(responseDTO.getType()));
        brandingPreferenceResponse.setName(responseDTO.getName());
        brandingPreferenceResponse.setLocale(responseDTO.getLocale());
        brandingPreferenceResponse.setPreference(responseDTO.getPreference());
        return brandingPreferenceResponse;
    }

    /**
     * Build a JSON string which contains preferences from a preference object.
     *
     * @param object Preference object of Branding Preference Model.
     * @return JSON string which contains preferences.
     */
    private String generatePreferencesJSONFromRequest(Object object) {

        ObjectMapper mapper = new ObjectMapper();
        String preferencesJSON = null;
        try {
            preferencesJSON = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error while generating JSON string from the branding preference request.", e);
            }
        }
        return preferencesJSON;
    }

    /**
     * Handle branding preference management exceptions and return an API error.
     *
     * @param exception Branding preference management exception
     * @param errorEnum Branding preference management error enum.
     * @param data      Relevant data.
     * @return Processed API Error.
     */
    private APIError handleBrandingPreferenceMgtException(BrandingPreferenceMgtException exception,
                                                          BrandingPreferenceManagementConstants.ErrorMessage errorEnum,
                                                          String data) {

        ErrorResponse errorResponse =
                getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.getDescription());
        Response.Status status;
        if (exception instanceof BrandingPreferenceMgtClientException) {
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        BRANDING_PREFERENCE_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            if (BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE.equals(exception.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else if (BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof BrandingPreferenceMgtServerException) {
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        BRANDING_PREFERENCE_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, BrandingPreferenceManagementConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(BrandingPreferenceManagementConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private String includeData(BrandingPreferenceManagementConstants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }
}
