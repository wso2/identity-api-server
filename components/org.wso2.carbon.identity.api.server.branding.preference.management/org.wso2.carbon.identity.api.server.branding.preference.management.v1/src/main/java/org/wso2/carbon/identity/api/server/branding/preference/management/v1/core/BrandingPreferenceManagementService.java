/*
 * Copyright (c) 2021-2024, WSO2 LLC. (http://www.wso2.com).
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.core.utils.BrandingPreferenceUtils;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.CustomTextModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.ResolvedBrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.ResolvedBrandingPreferenceModelResolvedFrom;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.ResolvedCustomTextModal;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.branding.preference.management.core.BrandingPreferenceManager;
import org.wso2.carbon.identity.branding.preference.management.core.exception.BrandingPreferenceMgtClientException;
import org.wso2.carbon.identity.branding.preference.management.core.exception.BrandingPreferenceMgtException;
import org.wso2.carbon.identity.branding.preference.management.core.exception.BrandingPreferenceMgtServerException;
import org.wso2.carbon.identity.branding.preference.management.core.model.BrandingPreference;
import org.wso2.carbon.identity.branding.preference.management.core.model.CustomText;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.APPLICATION_TYPE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_NOT_ALLOWED_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.CUSTOM_TEXT_PREFERENCE_ALREADY_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.DEFAULT_LOCALE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_CUSTOM_TEXT_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_CUSTOM_TEXT_PREFERENCE_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_CUSTOM_TEXT_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_CUSTOM_TEXT_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_CUSTOM_TEXT_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CUSTOM_TEXT_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_INVALID_BRANDING_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_INVALID_CUSTOM_TEXT_PREFERENCE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ErrorMessage.ERROR_CODE_NOT_ALLOWED_BRANDING_PREFERENCE_CONFIGURATIONS;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ORGANIZATION_TYPE;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * Invoke internal OSGi service to perform branding preference management operations.
 */
public class BrandingPreferenceManagementService {

    private final BrandingPreferenceManager brandingPreferenceManager;
    private static final Log log = LogFactory.getLog(BrandingPreferenceManagementService.class);

    public BrandingPreferenceManagementService(BrandingPreferenceManager brandingPreferenceManager) {

        this.brandingPreferenceManager = brandingPreferenceManager;
    }

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

        BrandingPreference responseDTO;
        try {
            BrandingPreference requestDTO = buildRequestDTOFromBrandingRequest(brandingPreferenceModel);
            responseDTO = brandingPreferenceManager.addBrandingPreference(requestDTO);
            log.info("Branding preference added successfully for tenant: " + tenantDomain + 
                    " and type: " + brandingPreferenceModel.getType());
        } catch (BrandingPreferenceMgtException e) {
            if (BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Branding preferences are already exists for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_BRANDING_PREFERENCE,
                        tenantDomain);
            }
            if (BRANDING_PREFERENCE_NOT_ALLOWED_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Requested branding preference customizations are not allowed to add for tenant: "
                            + tenantDomain, e);
                }
                throw handleExceptionWithMessage(Response.Status.FORBIDDEN,
                        ERROR_CODE_NOT_ALLOWED_BRANDING_PREFERENCE_CONFIGURATIONS, e.getMessage());
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
            brandingPreferenceManager.deleteBrandingPreference(type, name, locale);
            log.info("Branding preference deleted successfully for tenant: " + tenantDomain + 
                    " and type: " + type);
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

        /*
         Currently this API provides the support to only configure tenant wise & application wise branding preference
         for 'en-US' locale.
         So always use locale as default locale(en-US).
        */
        String tenantDomain = getTenantDomainFromContext();
        try {
            BrandingPreference responseDTO;
            if (APPLICATION_TYPE.equals(type)) {
                // Get application specific branding preference.
                responseDTO = brandingPreferenceManager.getBrandingPreference(APPLICATION_TYPE, name, DEFAULT_LOCALE);
            } else {
                // Get tenant specific branding preference.(default branding preference)
                responseDTO = brandingPreferenceManager.getBrandingPreference(ORGANIZATION_TYPE, tenantDomain,
                        DEFAULT_LOCALE);
            }

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
     * Retrieve the resolved branding preferences.
     *
     * @param type   Resource Type.
     * @param name   Name.
     * @param locale Language preference.
     * @return The resolved branding preference resource. If not exists return the default preferences.
     */
    public ResolvedBrandingPreferenceModel resolveBrandingPreference(String type, String name, String locale) {

        return resolveBrandingPreference(type, name, locale, false);
    }

    /**
     * Retrieve the resolved branding preferences.
     *
     * @param type                Resource Type.
     * @param name                Name.
     * @param locale              Language preference.
     * @param restrictToPublished Whether to resolve using only published branding preferences.
     * @return The resolved branding preference resource. If not exists return the default preferences.
     */
    public ResolvedBrandingPreferenceModel resolveBrandingPreference(String type, String name, String locale,
                                                             boolean restrictToPublished) {

        /*
         Currently this API provides the support to only configure organization wise & application wise
         branding preference for 'en-US' locale.
         So always locale as default locale(en-US).
        */
        String tenantDomain = getTenantDomainFromContext();
        try {
            BrandingPreference responseDTO;
            if (APPLICATION_TYPE.equals(type)) {
                // Get application specific branding preference.
                responseDTO = brandingPreferenceManager.resolveBrandingPreference(APPLICATION_TYPE, name,
                        DEFAULT_LOCALE, restrictToPublished);
            } else {
                // Get default branding preference.
                responseDTO = brandingPreferenceManager.resolveBrandingPreference(ORGANIZATION_TYPE, tenantDomain,
                        DEFAULT_LOCALE, restrictToPublished);
            }

            return buildResolvedBrandingResponseFromResponseDTO(responseDTO);
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

        BrandingPreference responseDTO;
        try {
            BrandingPreference requestDTO = buildRequestDTOFromBrandingRequest(brandingPreferenceModel);
            responseDTO = brandingPreferenceManager.replaceBrandingPreference(requestDTO);
            log.info("Branding preference updated successfully for tenant: " + tenantDomain + 
                    " and type: " + brandingPreferenceModel.getType());
        } catch (BrandingPreferenceMgtException e) {
            if (BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a branding preferences to update for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            if (BRANDING_PREFERENCE_NOT_ALLOWED_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Requested branding preference customizations are not allowed to update for tenant: "
                            + tenantDomain, e);
                }
                throw handleExceptionWithMessage(Response.Status.FORBIDDEN,
                        ERROR_CODE_NOT_ALLOWED_BRANDING_PREFERENCE_CONFIGURATIONS, e.getMessage());
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE, tenantDomain);
        }
        return buildBrandingResponseFromResponseDTO(responseDTO);
    }

    /**
     * Create a custom text preference resource with a resource file.
     *
     * @param customTextModal Custom Text preference post request.
     * @return created custom text preference model.
     */
    public CustomTextModel addCustomTextPreference(CustomTextModel customTextModal) {

        String tenantDomain = getTenantDomainFromContext();
        String preferencesJSON = generatePreferencesJSONFromRequest(customTextModal.getPreference());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_CUSTOM_TEXT_PREFERENCE, null);
        }

        CustomText responseDTO;
        try {
            CustomText requestDTO = buildRequestDTOFromCustomTextRequest(customTextModal);
            responseDTO = brandingPreferenceManager.addCustomText(requestDTO);
            log.info("Custom text preference added successfully for tenant: " + tenantDomain + 
                    " and screen: " + (customTextModal.getScreen() != null ? customTextModal.getScreen() : "null"));
        } catch (BrandingPreferenceMgtException e) {
            if (CUSTOM_TEXT_PREFERENCE_ALREADY_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Custom Text preferences are already exists for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_CUSTOM_TEXT_PREFERENCE,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_ADDING_CUSTOM_TEXT_PREFERENCE, tenantDomain);
        }
        return buildCustomTextResponseFromResponseDTO(responseDTO);
    }

    /**
     * Delete custom text preference resource.
     *
     * @param type   Resource type.
     * @param name   Name.
     * @param screen Screen Name.
     * @param locale Language preference.
     */
    public void deleteCustomTextPreference(String type, String name, String screen, String locale) {

        String tenantDomain = getTenantDomainFromContext();
        if (ORGANIZATION_TYPE.equals(type)) {
            name = tenantDomain;
        }

        try {
            brandingPreferenceManager.deleteCustomText(type, name, screen, locale);
            log.info("Custom text preference deleted successfully for tenant: " + tenantDomain + 
                    " and screen: " + (screen != null ? screen : "null"));
        } catch (BrandingPreferenceMgtException e) {
            if (CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a custom text preferences to delete for tenant: " + tenantDomain, e);
                }
                return;
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_DELETING_CUSTOM_TEXT_PREFERENCE,
                    tenantDomain);
        }
    }

    /**
     * Delete all the custom text preference resources of the current tenant.
     */
    public void deleteAllCustomTextPreferences() {

        String tenantDomain = getTenantDomainFromContext();
        try {
            brandingPreferenceManager.deleteAllCustomText();
        } catch (BrandingPreferenceMgtException e) {
            if (CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a custom text preferences to delete for tenant: " + tenantDomain, e);
                }
                return;
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_DELETING_CUSTOM_TEXT_PREFERENCE,
                    tenantDomain);
        }
    }

    /**
     * Retrieve the requested branding preferences.
     *
     * @param type   Resource Type.
     * @param name   Name.
     * @param screen Screen Name.
     * @param locale Language preference.
     * @return The requested custom text preference resource.
     */
    public CustomTextModel getCustomTextPreference(String type, String name, String screen, String locale) {

        String tenantDomain = getTenantDomainFromContext();
        if (ORGANIZATION_TYPE.equals(type) || StringUtils.isBlank(name)) {
            name = tenantDomain;
        }
        if (StringUtils.isBlank(locale)) {
            locale = DEFAULT_LOCALE;
        }
        try {
            CustomText responseDTO = brandingPreferenceManager.getCustomText(type, name, screen, locale);
            return buildCustomTextResponseFromResponseDTO(responseDTO);
        } catch (BrandingPreferenceMgtException e) {
            if (CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a custom text preference configurations for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_CUSTOM_TEXT_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_GETTING_CUSTOM_TEXT_PREFERENCE,
                    tenantDomain);
        }
    }

    /**
     * Retrieve the resolved branding preferences.
     *
     * @param type   Resource Type.
     * @param name   Name.
     * @param locale Language preference.
     * @return The resolved custom text preference resource. If not exists return the default preferences.
     */
    public ResolvedCustomTextModal resolveCustomTextPreference(String type, String name, String screen, String locale) {

        String tenantDomain = getTenantDomainFromContext();
        if (ORGANIZATION_TYPE.equals(type) || StringUtils.isBlank(name)) {
            name = tenantDomain;
        }
        if (StringUtils.isBlank(locale)) {
            locale = DEFAULT_LOCALE;
        }
        try {
            CustomText responseDTO = brandingPreferenceManager.resolveCustomText(type, name, screen, locale);
            return buildResolvedCustomTextResponseFromResponseDTO(responseDTO);
        } catch (BrandingPreferenceMgtException e) {
            if (CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a custom text preference configurations for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_CUSTOM_TEXT_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_GETTING_CUSTOM_TEXT_PREFERENCE,
                    tenantDomain);
        }
    }

    /**
     * Update custom text preferences.
     *
     * @param customTextModel Custom Text Preference Model with new preferences.
     * @return Updated custom text preference model.
     */
    public CustomTextModel updateCustomTextPreference(CustomTextModel customTextModel) {

        String tenantDomain = getTenantDomainFromContext();

        String preferencesJSON = generatePreferencesJSONFromRequest(customTextModel.getPreference());
        if (!BrandingPreferenceUtils.isValidJSONString(preferencesJSON)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_CUSTOM_TEXT_PREFERENCE, null);
        }

        CustomText responseDTO;
        try {
            CustomText requestDTO = buildRequestDTOFromCustomTextRequest(customTextModel);
            responseDTO = brandingPreferenceManager.replaceCustomText(requestDTO);
            log.info("Custom text preference updated successfully for tenant: " + tenantDomain + 
                    " and screen: " + (customTextModel.getScreen() != null ? customTextModel.getScreen() : "null"));
        } catch (BrandingPreferenceMgtException e) {
            if (CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not find a custom text preferences to update for tenant: " + tenantDomain, e);
                }
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_CUSTOM_TEXT_PREFERENCE_NOT_EXISTS,
                        tenantDomain);
            }
            throw handleBrandingPreferenceMgtException(e, ERROR_CODE_ERROR_UPDATING_CUSTOM_TEXT_PREFERENCE,
                    tenantDomain);
        }
        return buildCustomTextResponseFromResponseDTO(responseDTO);
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
     * Build custom text preference requestDTO from request body.
     *
     * @param customTextModel Custom Text preference request body.
     * @return Custom Text preference requestDTO object.
     */
    private CustomText buildRequestDTOFromCustomTextRequest(CustomTextModel customTextModel) {

        CustomText customTextRequestDTO = new CustomText();
        customTextRequestDTO.setType(customTextModel.getType().toString());
        if (ORGANIZATION_TYPE.equals(customTextModel.getType().toString())) {
            customTextRequestDTO.setName(getTenantDomainFromContext());
        } else {
            customTextRequestDTO.setName(customTextModel.getName());
        }
        if (StringUtils.isBlank(customTextModel.getLocale())) {
            customTextRequestDTO.setLocale(DEFAULT_LOCALE);
        } else {
            customTextRequestDTO.setLocale(customTextModel.getLocale());
        }
        customTextRequestDTO.setScreen(customTextModel.getScreen());
        customTextRequestDTO.setPreference(customTextModel.getPreference());
        return customTextRequestDTO;
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
     * Build branding preference response object from the responseDTO.
     *
     * @param responseDTO Branding preference responseDTO object.
     * @return Branding preference response object{@link BrandingPreferenceModel}.
     */
    private ResolvedBrandingPreferenceModel buildResolvedBrandingResponseFromResponseDTO(
            BrandingPreference responseDTO) {

        ResolvedBrandingPreferenceModelResolvedFrom resolvedFrom =
                new ResolvedBrandingPreferenceModelResolvedFrom();
        resolvedFrom.setType(ResolvedBrandingPreferenceModelResolvedFrom.TypeEnum.valueOf(
                responseDTO.getResolvedFrom().getType()));
        resolvedFrom.setName(responseDTO.getResolvedFrom().getName());
        ResolvedBrandingPreferenceModel brandingPreferenceResponse = new ResolvedBrandingPreferenceModel();
        brandingPreferenceResponse.setType(ResolvedBrandingPreferenceModel.TypeEnum.valueOf(responseDTO.getType()));
        brandingPreferenceResponse.setName(responseDTO.getName());
        brandingPreferenceResponse.setLocale(responseDTO.getLocale());
        brandingPreferenceResponse.setResolvedFrom(resolvedFrom);
        brandingPreferenceResponse.setPreference(responseDTO.getPreference());
        return brandingPreferenceResponse;
    }

    /**
     * Build custom text preference response object from the responseDTO.
     *
     * @param responseDTO Custom Text preference responseDTO object.
     * @return Custom Text preference response object{@link CustomTextModel}.
     */
    private CustomTextModel buildCustomTextResponseFromResponseDTO(CustomText responseDTO) {

        CustomTextModel customTextModel = new CustomTextModel();
        customTextModel.setType(CustomTextModel.TypeEnum.valueOf(responseDTO.getType()));
        customTextModel.setName(responseDTO.getName());
        customTextModel.setLocale(responseDTO.getLocale());
        customTextModel.setScreen(responseDTO.getScreen());
        customTextModel.setPreference(responseDTO.getPreference());
        return customTextModel;
    }

    /**
     * Build custom text preference response object from the responseDTO.
     *
     * @param responseDTO Custom Text preference responseDTO object.
     * @return Custom Text preference response object{@link CustomTextModel}.
     */
    private ResolvedCustomTextModal buildResolvedCustomTextResponseFromResponseDTO(CustomText responseDTO) {

        ResolvedBrandingPreferenceModelResolvedFrom resolvedFrom =
                new ResolvedBrandingPreferenceModelResolvedFrom();
        resolvedFrom.setType(ResolvedBrandingPreferenceModelResolvedFrom.TypeEnum.valueOf(
                responseDTO.getResolvedFrom().getType()));
        resolvedFrom.setName(responseDTO.getResolvedFrom().getName());
        ResolvedCustomTextModal customTextModel = new ResolvedCustomTextModal();
        customTextModel.setType(ResolvedCustomTextModal.TypeEnum.valueOf(responseDTO.getType()));
        customTextModel.setName(responseDTO.getName());
        customTextModel.setLocale(responseDTO.getLocale());
        customTextModel.setScreen(responseDTO.getScreen());
        customTextModel.setResolvedFrom(resolvedFrom);
        customTextModel.setPreference(responseDTO.getPreference());
        return customTextModel;
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

        ErrorResponse errorResponse;
        Response.Status status;
        if (exception instanceof BrandingPreferenceMgtClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception.getMessage());
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        BRANDING_PREFERENCE_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            if (BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE.equals(exception.getErrorCode()) ||
                    CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(exception.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else if (BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE.equals(exception.getErrorCode()) ||
                    CUSTOM_TEXT_PREFERENCE_ALREADY_EXISTS_ERROR_CODE.equals(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof BrandingPreferenceMgtServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.getDescription());
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        BRANDING_PREFERENCE_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.getDescription());
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

    /**
     * Handle exceptions generated in API with a custom description.
     *
     * @param status      HTTP Status.
     * @param error       Error Message information.
     * @param description Error Message description.
     * @return APIError.
     */
    private APIError handleExceptionWithMessage(Response.Status status,
                                                BrandingPreferenceManagementConstants.ErrorMessage error,
                                                String description) {

        ErrorResponse errorResponse = new ErrorResponse.Builder().withCode(error.getCode())
                .withMessage(error.getMessage()).withDescription(description).build();
        return new APIError(status, errorResponse);
    }
}
