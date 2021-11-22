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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceServiceHolder;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.core.utils.BrandingPreferenceUtils;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementClientException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementServerException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_RESOURCE_TYPE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.CONFIG_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.DEFAULT_LOCALE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_BUILDING_RESPONSE_EXCEPTION;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_CHECKING_BRANDING_PREFERENCE_EXISTS;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_INVALID_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_JSON_PROCESSING_EXCEPTION;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_UNSUPPORTED_ENCODING_EXCEPTION;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ORGANIZATION_TYPE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.RESOURCE_ALREADY_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.RESOURCE_NAME_SEPARATOR;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.RESOURCE_NOT_EXISTS_ERROR_CODE;
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
     */
    public void addBrandingPreference(BrandingPreferenceModel brandingPreferenceModel) {

        String tenantDomain = getTenantDomainFromContext();
        /**
         * Currently this API provides the support to only configure tenant wise branding preference for 'en-US' locale.
         * So always use resource name as default resource name.
         */
        String resourceName = getDefaultResourceName();
        // Check whether a branding resource already exists with the same name in the particular tenant to be added.
        if (isResourceExists(BRANDING_RESOURCE_TYPE, resourceName)) {
            throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_BRANDING_PREFERENCE, tenantDomain);
        }
        String preferencesJSON = generatePreferencesJSONFromRequest(brandingPreferenceModel.getPreference());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_BRANDING_PREFERENCE, null);
        }

        try {
            InputStream inputStream = BrandingPreferenceUtils.generatePreferenceInputStream(preferencesJSON);
            Resource brandingPreferenceResource =
                    buildResourceFromBrandingPreference(brandingPreferenceModel, inputStream);
            BrandingPreferenceServiceHolder.getBrandingPreferenceConfigManager()
                    .addResource(BRANDING_RESOURCE_TYPE, brandingPreferenceResource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_ADDING_BRANDING_PREFERENCE, tenantDomain);
        } catch (JsonProcessingException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_JSON_PROCESSING_EXCEPTION,
                    e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_UNSUPPORTED_ENCODING_EXCEPTION,
                    e.getMessage());
        }
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
        /**
         * Currently this API provides the support to only configure tenant wise branding preference for 'en-US' locale.
         * So always use resource name as default resource name.
         */
        String resourceName = getDefaultResourceName();
        // Check whether the branding resource exists in the particular tenant.
        if (!isResourceExists(BRANDING_RESOURCE_TYPE, resourceName)) {
            throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS, tenantDomain);
        }

        try {
            BrandingPreferenceServiceHolder.getBrandingPreferenceConfigManager()
                    .deleteResource(BRANDING_RESOURCE_TYPE, resourceName);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_DELETING_BRANDING_PREFERENCE, tenantDomain);
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

        String tenantDomain = getTenantDomainFromContext();
        /**
         * Currently this API provides the support to only configure tenant wise branding preference for 'en-US' locale.
         * So always use resource name as default resource name.
         */
        String resourceName = getDefaultResourceName();
        try {
            // Return default branding preference.
            List<ResourceFile> resourceFiles = BrandingPreferenceServiceHolder.getBrandingPreferenceConfigManager()
                    .getFiles(BRANDING_RESOURCE_TYPE, resourceName);
            if (resourceFiles.isEmpty()) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            if (StringUtils.isBlank(resourceFiles.get(0).getId())) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }

            InputStream inputStream = BrandingPreferenceServiceHolder.getBrandingPreferenceConfigManager()
                    .getFileById(BRANDING_RESOURCE_TYPE, resourceName, resourceFiles.get(0).getId());
            if (inputStream == null) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            return buildBrandingPreferenceFromResource(inputStream);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_BRANDING_PREFERENCE, tenantDomain);
        } catch (IOException e) {
            throw handleException
                    (Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_ERROR_BUILDING_RESPONSE_EXCEPTION, null);
        }
    }

    /**
     * Update branding preferences.
     *
     * @param brandingPreferenceModel Branding Preference Model with new preferences.
     */
    public void updateBrandingPreference(BrandingPreferenceModel brandingPreferenceModel) {

        String tenantDomain = getTenantDomainFromContext();
        /**
         * Currently this API provides the support to only configure tenant wise branding preference for 'en-US' locale.
         * So always use resource name as default resource name.
         */
        String resourceName = getDefaultResourceName();
        // Check whether the branding resource exists in the particular tenant.
        if (!isResourceExists(BRANDING_RESOURCE_TYPE, resourceName)) {
            throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS, tenantDomain);
        }

        String preferencesJSON = generatePreferencesJSONFromRequest(brandingPreferenceModel.getPreference());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_BRANDING_PREFERENCE, null);
        }

        try {
            InputStream inputStream = BrandingPreferenceUtils.generatePreferenceInputStream(preferencesJSON);
            Resource brandingPreferenceResource =
                    buildResourceFromBrandingPreference(brandingPreferenceModel, inputStream);
            BrandingPreferenceServiceHolder.getBrandingPreferenceConfigManager()
                    .replaceResource(BRANDING_RESOURCE_TYPE, brandingPreferenceResource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE, tenantDomain);
        } catch (JsonProcessingException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_JSON_PROCESSING_EXCEPTION,
                    e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_UNSUPPORTED_ENCODING_EXCEPTION,
                    e.getMessage());
        }
    }

    /**
     * Check whether a branding preference resource already exists with the same name in the particular tenant.
     *
     * @param resourceType Resource type.
     * @param resourceName Resource name.
     * @return Return true if the resource already exists. If not return false.
     */
    private boolean isResourceExists(String resourceType, String resourceName) {

        Resource resource;
        try {
            resource = BrandingPreferenceServiceHolder.getBrandingPreferenceConfigManager()
                    .getResource(resourceType, resourceName);
        } catch (ConfigurationManagementException e) {
            if (RESOURCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                return false;
            }
            throw handleConfigurationMgtException
                    (e, ERROR_CODE_ERROR_CHECKING_BRANDING_PREFERENCE_EXISTS, getTenantDomainFromContext());
        }
        if (resource == null) {
            return false;
        }
        return true;
    }

    /**
     * Generate and return resource name of the default branding of the particular tenant.
     *
     * @return resource name of the default branding resource.
     */
    private String getDefaultResourceName() {

        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        String resourceName = tenantId + RESOURCE_NAME_SEPARATOR + DEFAULT_LOCALE;
        return resourceName;
    }

    /**
     * Build a resource object from Branding Preference Model.
     *
     * @param model       Branding Preference Model.
     * @param inputStream Branding Preference file stream.
     * @return Resource object.
     */
    private Resource buildResourceFromBrandingPreference(BrandingPreferenceModel model, InputStream inputStream) {

        /**
         * Currently this API provides the support to only configure tenant wise branding preference for 'en-US' locale.
         * So always use resource name as default resource name.
         */
        String resourceName = getDefaultResourceName();
        Resource resource = new Resource();
        resource.setResourceName(resourceName);
        // Set file.
        ResourceFile file = new ResourceFile();
        file.setName(resourceName);
        file.setInputStream(inputStream);
        List<ResourceFile> resourceFiles = new ArrayList<>();
        resourceFiles.add(file);
        resource.setFiles(resourceFiles);
        return resource;
    }

    /**
     * Build a Branding Preference Model from branding preference file stream.
     *
     * @param inputStream Branding Preference file stream.
     * @return Branding Preference Model.
     */
    private BrandingPreferenceModel buildBrandingPreferenceFromResource(InputStream inputStream)
            throws IOException {

        String preferencesJSON = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException
                    (Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_ERROR_BUILDING_RESPONSE_EXCEPTION, null);
        }

        ObjectMapper mapper = new ObjectMapper();
        Object preference = mapper.readValue(preferencesJSON, Object.class);
        BrandingPreferenceModel brandingPreferenceModel = new BrandingPreferenceModel();
        brandingPreferenceModel.setPreference(preference);
        brandingPreferenceModel.setType(BrandingPreferenceModel.TypeEnum.valueOf(ORGANIZATION_TYPE));
        brandingPreferenceModel.setName(getTenantDomainFromContext());
        brandingPreferenceModel.setLocale(DEFAULT_LOCALE);
        return brandingPreferenceModel;
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
     * Handle configuration management exceptions and return an API error.
     *
     * @param exception Configuration management exception
     * @param errorEnum Branding preference management error enum.
     * @param data      Relevant data.
     * @return Processed API Error.
     */
    private APIError handleConfigurationMgtException(ConfigurationManagementException exception,
                                                     BrandingPreferenceManagementConstants.ErrorMessage errorEnum,
                                                     String data) {

        ErrorResponse errorResponse =
                getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.getDescription());
        Response.Status status;
        if (exception instanceof ConfigurationManagementClientException) {
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        BRANDING_PREFERENCE_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            if (RESOURCE_ALREADY_EXISTS_ERROR_CODE.equals(exception.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else if (RESOURCE_NOT_EXISTS_ERROR_CODE.equals(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof ConfigurationManagementServerException) {
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
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
