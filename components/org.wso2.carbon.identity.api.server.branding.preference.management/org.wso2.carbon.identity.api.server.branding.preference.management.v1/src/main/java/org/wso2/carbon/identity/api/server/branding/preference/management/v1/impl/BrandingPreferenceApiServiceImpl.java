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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.BrandingPreferenceApiService;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.core.BrandingPreferenceManagementService;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.factories.BrandingAIPreferenceManagementServiceFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.factories.BrandingPreferenceManagementServiceFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationRequestModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationResponseModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationResultModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationStatusModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.CustomTextModel;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.APPLICATION_TYPE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.BRANDING_PREFERENCE_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.CUSTOM_TYPE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.DEFAULT_LOCALE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.GET_CUSTOM_TEXT_COMPONENT_WITH_QUERY_PARAM;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.GET_PREFERENCE_COMPONENT_WITH_QUERY_PARAM;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.ORGANIZATION_TYPE;
import static org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceManagementConstants.QUERY_PARAM_INDICATOR;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * Implementation of branding preference management REST API.
 */
public class BrandingPreferenceApiServiceImpl implements BrandingPreferenceApiService {

    private static final Log log = LogFactory.getLog(BrandingPreferenceApiServiceImpl.class);
    private final BrandingPreferenceManagementService brandingPreferenceManagementService;

    //TODO: Improve API to manage application level & language level theming resources in addition to the tenant level.

    public BrandingPreferenceApiServiceImpl() {
        try {
            brandingPreferenceManagementService = BrandingPreferenceManagementServiceFactory
                    .getBrandingPreferenceManagementService();
            if (log.isDebugEnabled()) {
                log.debug("BrandingPreferenceApiServiceImpl initialized successfully");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating the branding preference service", e);
            throw new RuntimeException("Error occurred while initiating the branding preference service.", e);
        }
    }

    @Override
    public Response addBrandingPreference(BrandingPreferenceModel brandingPreferenceModel) {

        if (log.isDebugEnabled()) {
            log.debug("Received request to add branding preference for type: " + 
                    (brandingPreferenceModel != null ? brandingPreferenceModel.getType() : "null"));
        }
        if (StringUtils.isBlank(brandingPreferenceModel.getType().toString())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (!(ORGANIZATION_TYPE.equals(brandingPreferenceModel.getType().toString()) ||
                APPLICATION_TYPE.equals(brandingPreferenceModel.getType().toString()))) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Not Implemented.").build();
        }
        if ((!DEFAULT_LOCALE.equals(brandingPreferenceModel.getLocale())) &&
                StringUtils.isNotBlank(brandingPreferenceModel.getLocale())) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Not Implemented.").build();
        }

        BrandingPreferenceModel createdBrandingPreferenceModel =
                brandingPreferenceManagementService.addBrandingPreference(brandingPreferenceModel);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + BRANDING_PREFERENCE_CONTEXT_PATH
                    + QUERY_PARAM_INDICATOR + URLEncoder.encode(String.format(GET_PREFERENCE_COMPONENT_WITH_QUERY_PARAM,
                    ORGANIZATION_TYPE, getTenantDomainFromContext(), DEFAULT_LOCALE), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(createdBrandingPreferenceModel).build();
    }

    @Override
    public Response addCustomText(CustomTextModel customTextModel) {

        if (StringUtils.isBlank(customTextModel.getType().toString())
                || StringUtils.isBlank(customTextModel.getScreen())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (!ORGANIZATION_TYPE.equals(customTextModel.getType().toString())) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Not Implemented.").build();
        }

        String locale = customTextModel.getLocale();
        if (StringUtils.isBlank(customTextModel.getLocale())) {
            locale = DEFAULT_LOCALE;
        }

        CustomTextModel createdCustomTextModel =
                brandingPreferenceManagementService.addCustomTextPreference(customTextModel);
        URI location;
        try {
            location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + BRANDING_PREFERENCE_CONTEXT_PATH
                    + QUERY_PARAM_INDICATOR +
                    URLEncoder.encode(String.format(GET_CUSTOM_TEXT_COMPONENT_WITH_QUERY_PARAM, ORGANIZATION_TYPE,
                                    getTenantDomainFromContext(), customTextModel.getScreen(), locale),
                            StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(createdCustomTextModel).build();
    }

    @Override
    public Response deleteBrandingPreference(String type, String name, String locale) {

        if (type != null) {
            if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type) || CUSTOM_TYPE.equals(type))) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type))) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            type = ORGANIZATION_TYPE;
        }
        if (locale != null) {
            if (!DEFAULT_LOCALE.equals(locale)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            locale = DEFAULT_LOCALE;
        }

        brandingPreferenceManagementService.deleteBrandingPreference(type, name, locale);
        return Response.noContent().build();
    }

    @Override
    public Response deleteCustomText(String type, String name, String locale, String screen) {

        if (StringUtils.isBlank(type) && StringUtils.isBlank(screen) && StringUtils.isBlank(name) &&
                StringUtils.isBlank(locale)) {
            // Delete all custom text preferences if no query parameter was specified.
            brandingPreferenceManagementService.deleteAllCustomTextPreferences();
            return Response.noContent().build();
        }

        if (StringUtils.isBlank(type)) {
            type = ORGANIZATION_TYPE;
        } else if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type) || CUSTOM_TYPE.equals(type))) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (!ORGANIZATION_TYPE.equals(type)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (StringUtils.isBlank(screen)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (StringUtils.isBlank(locale)) {
            locale = DEFAULT_LOCALE;
        }

        brandingPreferenceManagementService.deleteCustomTextPreference(type, name, screen, locale);
        return Response.noContent().build();
    }

    @Override
    public Response getBrandingPreference(String type, String name, String locale) {

        if (type != null) {
            if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type) || CUSTOM_TYPE.equals(type))) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        return Response.ok()
                .entity(brandingPreferenceManagementService.getBrandingPreference(type, name, locale)).build();
    }

    @Override
    public Response generateBrandingPreference(BrandingGenerationRequestModel brandingGenerationRequestModel) {

        if (log.isDebugEnabled()) {
            log.debug("Received request to generate branding preference via AI for website URL: " + 
                    (brandingGenerationRequestModel != null ? brandingGenerationRequestModel.getWebsiteUrl() : "null"));
        }
        BrandingGenerationResponseModel response = BrandingAIPreferenceManagementServiceFactory
                .getBrandingAIPreferenceManagementService().generateBrandingPreference(
                brandingGenerationRequestModel);
        return Response.accepted().entity(response).build();

    }

    @Override
    public Response getBrandingGenerationResult(String operationId) {

        if (log.isDebugEnabled()) {
            log.debug("Received request to get branding generation result for operation ID: " + operationId);
        }
        BrandingGenerationResultModel response = BrandingAIPreferenceManagementServiceFactory
                .getBrandingAIPreferenceManagementService()
                .getBrandingPreferenceGenerationResult(operationId);
        return Response.ok().entity(response).build();
    }

    @Override
    public Response getBrandingGenerationStatus(String operationId) {

        BrandingGenerationStatusModel response = BrandingAIPreferenceManagementServiceFactory
                .getBrandingAIPreferenceManagementService()
                .getBrandingPreferenceGenerationStatus(operationId);
        return Response.ok().entity(response).build();
    }

    @Override
    public Response getCustomText(String type, String name, String locale, String screen) {

        if (StringUtils.isBlank(type)) {
            type = ORGANIZATION_TYPE;
        } else if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type) || CUSTOM_TYPE.equals(type))) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (!ORGANIZATION_TYPE.equals(type)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (StringUtils.isBlank(screen)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().entity(brandingPreferenceManagementService.
                getCustomTextPreference(type, name, screen, locale)).build();
    }

    @Override
    public Response resolveBrandingPreference(String type, String name, String locale, Boolean restrictToPublished) {

        if (type != null) {
            if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type) || CUSTOM_TYPE.equals(type))) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        return Response.ok().entity(brandingPreferenceManagementService
                .resolveBrandingPreference(type, name, locale, restrictToPublished)).build();
    }

    @Override
    public Response resolveCustomText(String type, String name, String locale, String screen) {

        if (StringUtils.isBlank(type)) {
            type = ORGANIZATION_TYPE;
        } else if (!(ORGANIZATION_TYPE.equals(type) || APPLICATION_TYPE.equals(type) || CUSTOM_TYPE.equals(type))) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (StringUtils.isBlank(screen)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().entity(brandingPreferenceManagementService.
                resolveCustomTextPreference(type, name, screen, locale)).build();
    }

    @Override
    public Response updateBrandingPreference(BrandingPreferenceModel brandingPreferenceModel) {

        if (StringUtils.isBlank(brandingPreferenceModel.getType().toString())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (!(ORGANIZATION_TYPE.equals(brandingPreferenceModel.getType().toString()) ||
                APPLICATION_TYPE.equals(brandingPreferenceModel.getType().toString()))) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if ((!DEFAULT_LOCALE.equals(brandingPreferenceModel.getLocale())) &&
                StringUtils.isNotBlank(brandingPreferenceModel.getLocale())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        BrandingPreferenceModel updatedBrandingPreferenceModel =
                brandingPreferenceManagementService.updateBrandingPreference(brandingPreferenceModel);
        return Response.ok().entity(updatedBrandingPreferenceModel).build();
    }

    @Override
    public Response updateCustomText(CustomTextModel customTextModel) {

        if (StringUtils.isBlank(customTextModel.getType().toString())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (!ORGANIZATION_TYPE.equals(customTextModel.getType().toString())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (StringUtils.isBlank(customTextModel.getScreen())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        CustomTextModel updatedCustomTextModel =
                brandingPreferenceManagementService.updateCustomTextPreference(customTextModel);
        return Response.ok().entity(updatedCustomTextModel).build();
    }
}
