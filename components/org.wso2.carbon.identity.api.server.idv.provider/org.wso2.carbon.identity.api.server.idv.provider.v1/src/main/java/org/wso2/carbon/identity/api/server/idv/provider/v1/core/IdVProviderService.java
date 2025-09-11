/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idv.provider.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.extension.identity.verification.provider.IdVProviderManager;
import org.wso2.carbon.extension.identity.verification.provider.exception.IdVProviderMgtClientException;
import org.wso2.carbon.extension.identity.verification.provider.exception.IdVProviderMgtException;
import org.wso2.carbon.extension.identity.verification.provider.model.IdVConfigProperty;
import org.wso2.carbon.extension.identity.verification.provider.model.IdVProvider;
import org.wso2.carbon.extension.identity.verification.provider.util.IdVProviderMgtConstants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.idv.provider.common.Constants;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.ConfigProperty;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderListResponse;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderRequest;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderResponse;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.VerificationClaim;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Service class for identity verification.
 */
public class IdVProviderService {

    private final IdVProviderManager idvProviderManager;
    private static final Log log = LogFactory.getLog(IdVProviderService.class);

    public IdVProviderService(IdVProviderManager idvProviderManager) {

        this.idvProviderManager = idvProviderManager;
    }

    /**
     * Add an identity verification provider.
     *
     * @param idVProviderRequest Identity verification provider request.
     * @return Identity verification providers.
     */
    public IdVProviderResponse addIdVProvider(IdVProviderRequest idVProviderRequest) {

        IdVProvider idVProvider;
        int tenantId = getTenantId();
        if (log.isDebugEnabled()) {
            log.debug("Adding IdV provider with name: " + 
                    (idVProviderRequest != null ? idVProviderRequest.getName() : "null") + 
                    " for tenant ID: " + tenantId);
        }

        if (idVProviderRequest == null) {
            log.warn("IdVProviderRequest is null. Cannot add IdV provider.");
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_ADDING_IDVP, "null");
        }

        try {
            idVProvider = idvProviderManager.addIdVProvider(createIdVProvider(idVProviderRequest), tenantId);
        } catch (IdVProviderMgtException e) {
            if (IdVProviderMgtConstants.ErrorMessage.ERROR_IDVP_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
                throw handleException(Response.Status.CONFLICT,
                        Constants.ErrorMessage.ERROR_CODE_IDVP_EXISTS, idVProviderRequest.getName());
            }
            throw handleIdVException(e, Constants.ErrorMessage.ERROR_ADDING_IDVP, 
                    idVProviderRequest.getName());
        }
        if (log.isDebugEnabled()) {
            log.debug("IdV provider created successfully with ID: " + idVProvider.getIdVProviderUuid() + 
                    " for tenant ID: " + tenantId);
        }
        return getIdVProviderResponse(idVProvider);
    }

    /**
     * Update identity verification provider.
     *
     * @param idVProviderId      Identity verification provider id.
     * @param idVProviderRequest Identity verification provider request.
     * @return Identity verification provider response.
     */
    public IdVProviderResponse updateIdVProvider(String idVProviderId, IdVProviderRequest idVProviderRequest) {

        IdVProvider oldIdVProvider;
        IdVProvider newIdVProvider;
        int tenantId = getTenantId();
        if (log.isDebugEnabled()) {
            log.debug("Updating IdV provider with ID: " + idVProviderId + ", name: " + 
                    (idVProviderRequest != null ? idVProviderRequest.getName() : "null") + 
                    " for tenant ID: " + tenantId);
        }

        if (idVProviderRequest == null) {
            log.warn("IdVProviderRequest is null. Cannot update IdV provider.");
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_UPDATING_IDVP, idVProviderId);
        }

        try {
            oldIdVProvider = idvProviderManager.getIdVProvider(idVProviderId, tenantId);

            if (oldIdVProvider == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_IDVP_NOT_FOUND, idVProviderId);
            }
            IdVProvider updatedIdVProvider =
                    createUpdatedIdVProvider(oldIdVProvider, idVProviderRequest);
            newIdVProvider = idvProviderManager.updateIdVProvider(oldIdVProvider, updatedIdVProvider, tenantId);
        } catch (IdVProviderMgtException e) {
            if (IdVProviderMgtConstants.ErrorMessage.ERROR_EMPTY_IDVP_ID.getCode().equals(e.getErrorCode())) {
                throw handleIdVException(e, Constants.ErrorMessage.ERROR_CODE_IDV_PROVIDER_NOT_FOUND, idVProviderId);
            } else if (IdVProviderMgtConstants.ErrorMessage.ERROR_IDVP_ALREADY_EXISTS.getCode()
                    .equals(e.getErrorCode())) {
                throw handleException(Response.Status.CONFLICT,
                        Constants.ErrorMessage.ERROR_CODE_IDVP_EXISTS, idVProviderRequest.getName());
            } else {
                throw handleIdVException(e, Constants.ErrorMessage.ERROR_UPDATING_IDVP, idVProviderId);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("IdV provider updated successfully with ID: " + idVProviderId + 
                    " for tenant ID: " + tenantId);
        }
        return getIdVProviderResponse(newIdVProvider);
    }

    /**
     * Get identity verification provider by id.
     *
     * @param idVProviderId Identity verification provider id.
     * @return Identity verification provider response.
     */
    public IdVProviderResponse getIdVProvider(String idVProviderId) {

        try {
            int tenantId = getTenantId();
            if (log.isDebugEnabled()) {
                log.debug("Retrieving IdV provider with ID: " + idVProviderId + 
                        " for tenant ID: " + tenantId);
            }
            IdVProvider idVProvider = idvProviderManager.getIdVProvider(idVProviderId, tenantId);
            if (idVProvider == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_IDVP_NOT_FOUND, idVProviderId);
            }
            if (log.isDebugEnabled()) {
                log.debug("IdV provider retrieved successfully with ID: " + idVProviderId + 
                        " for tenant ID: " + tenantId);
            }
            return getIdVProviderResponse(idVProvider);
        } catch (IdVProviderMgtException e) {
            if (IdVProviderMgtConstants.ErrorMessage.ERROR_EMPTY_IDVP_ID.getCode().equals(e.getErrorCode())) {
                throw handleIdVException(e, Constants.ErrorMessage.ERROR_CODE_IDV_PROVIDER_NOT_FOUND, idVProviderId);
            } else {
                throw handleIdVException(e, Constants.ErrorMessage.ERROR_RETRIEVING_IDVP, idVProviderId);
            }
        }
    }

    /**
     * Get all identity verification providers.
     *
     * @param limit  Limit per page.
     * @param offset Offset value.
     * @return Identity verification providers.
     */
    public IdVProviderListResponse getIdVProviders(Integer limit, Integer offset) {

        return getIdVProviders(limit, offset, null);
    }

    /**
     * Get all identity verification providers with filtering.
     *
     * @param limit  Limit per page.
     * @param offset Offset value.
     * @return Identity verification providers.
     */
    public IdVProviderListResponse getIdVProviders(Integer limit, Integer offset, String filter) {

        int tenantId = getTenantId();
        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdV providers with limit: " + limit + ", offset: " + offset + 
                    ", filter: " + filter + " for tenant ID: " + tenantId);
        }
        try {
            int totalResults = idvProviderManager.getCountOfIdVProviders(tenantId, filter);

            IdVProviderListResponse idVProviderListResponse = new IdVProviderListResponse();

            if (totalResults > 0) {
                List<IdVProvider> idVProviders = idvProviderManager.getIdVProviders(limit, offset, filter, tenantId);

                if (CollectionUtils.isNotEmpty(idVProviders)) {
                    List<IdVProviderResponse> idVProvidersList = new ArrayList<>();
                    for (IdVProvider idVP : idVProviders) {
                        IdVProviderResponse idVPlistItem = getIdVProviderResponse(idVP);
                        idVProvidersList.add(idVPlistItem);
                    }
                    idVProviderListResponse.setIdentityVerificationProviders(idVProvidersList);
                    idVProviderListResponse.setCount(idVProviders.size());
                } else {
                    idVProviderListResponse.setCount(0);
                }
            } else {
                idVProviderListResponse.setCount(0);
            }
            offset = (offset == null) ? Integer.valueOf(0) : offset;
            idVProviderListResponse.setStartIndex(offset + 1);
            idVProviderListResponse.setTotalResults(totalResults);
            if (log.isDebugEnabled()) {
                log.debug("Retrieved " + idVProviderListResponse.getCount() + " out of " + totalResults + 
                        " IdV providers for tenant ID: " + tenantId);
            }
            return idVProviderListResponse;
        } catch (IdVProviderMgtException e) {
            throw handleIdVException(e, Constants.ErrorMessage.ERROR_RETRIEVING_IDVPS,
                    IdentityTenantUtil.getTenantDomain(tenantId));
        }
    }

    /**
     * Delete identity verification provider by id.
     *
     * @param idVProviderId Identity verification provider id.
     */
    public void deleteIdVProvider(String idVProviderId) {

        int tenantId = getTenantId();
        if (log.isDebugEnabled()) {
            log.debug("Deleting IdV provider with ID: " + idVProviderId + " for tenant ID: " + tenantId);
        }
        try {
            idvProviderManager.deleteIdVProvider(idVProviderId, tenantId);
            if (log.isDebugEnabled()) {
                log.debug("IdV provider deleted successfully with ID: " + idVProviderId + 
                        " for tenant ID: " + tenantId);
            }
        } catch (IdVProviderMgtException e) {
            throw handleIdVException(e, Constants.ErrorMessage.ERROR_DELETING_IDVP, idVProviderId);
        }
    }

    private List<VerificationClaim> getIdVClaimMappings(IdVProvider idVProvider) {

        Map<String, String> claimMappings = idVProvider.getClaimMappings();
        return claimMappings.entrySet().stream().map(entry -> {
            VerificationClaim verificationclaim = new VerificationClaim();
            verificationclaim.setLocalClaim(entry.getKey());
            verificationclaim.setIdvpClaim(entry.getValue());
            return verificationclaim;
        }).collect(Collectors.toList());
    }

    private IdVProviderResponse getIdVProviderResponse(IdVProvider idVProvider) {

        IdVProviderResponse idvProviderResponse = new IdVProviderResponse();
        idvProviderResponse.setId(idVProvider.getIdVProviderUuid());
        idvProviderResponse.setType(idVProvider.getType());
        idvProviderResponse.setName(idVProvider.getIdVProviderName());
        idvProviderResponse.setIsEnabled(idVProvider.isEnabled());
        idvProviderResponse.setDescription(idVProvider.getIdVProviderDescription());
        idvProviderResponse.setImage(idVProvider.getImageUrl());

        if (idVProvider.getIdVConfigProperties() != null) {
            List<ConfigProperty> configProperties =
                    Arrays.stream(idVProvider.getIdVConfigProperties()).
                            map(propertyToExternal).collect(Collectors.toList());

            idvProviderResponse.setConfigProperties(configProperties);
        }
        if (idVProvider.getClaimMappings() != null) {
            idvProviderResponse.setClaims(getIdVClaimMappings(idVProvider));
        }
        return idvProviderResponse;
    }

    private IdVProvider createIdVProvider(IdVProviderRequest idVProviderRequest) {

        IdVProvider idVProvider = new IdVProvider();
        idVProvider.setType(idVProviderRequest.getType());
        idVProvider.setIdVProviderName(idVProviderRequest.getName());
        idVProvider.setIdVProviderDescription(idVProviderRequest.getDescription());
        idVProvider.setEnabled(idVProviderRequest.getIsEnabled());
        if (idVProviderRequest.getClaims() != null) {
            idVProvider.setClaimMappings(getClaimMap(idVProviderRequest.getClaims()));
        }
        if (idVProviderRequest.getConfigProperties() != null) {
            List<ConfigProperty> properties = idVProviderRequest.getConfigProperties();
            idVProvider.setIdVConfigProperties(
                    properties.stream().map(propertyToInternal).toArray(IdVConfigProperty[]::new));
        }
        return idVProvider;
    }

    private IdVProvider createUpdatedIdVProvider(IdVProvider oldIdVProvider,
                                                 IdVProviderRequest idVProviderRequest) {

        IdVProvider idVProvider = new IdVProvider();
        idVProvider.setIdVProviderUUID(oldIdVProvider.getIdVProviderUuid());
        idVProvider.setType(idVProviderRequest.getType());
        idVProvider.setIdVProviderName(idVProviderRequest.getName());
        idVProvider.setIdVProviderDescription(idVProviderRequest.getDescription());
        idVProvider.setEnabled(idVProviderRequest.getIsEnabled());
        if (idVProviderRequest.getClaims() != null) {
            idVProvider.setClaimMappings(getClaimMap(idVProviderRequest.getClaims()));
        }
        if (idVProviderRequest.getConfigProperties() != null) {
            List<ConfigProperty> properties = idVProviderRequest.getConfigProperties();
            idVProvider.setIdVConfigProperties(
                    properties.stream().map(propertyToInternal).toArray(IdVConfigProperty[]::new));
        }
        return idVProvider;
    }

    private final Function<ConfigProperty, IdVConfigProperty> propertyToInternal = apiProperty -> {

        IdVConfigProperty idVConfigProperty = new IdVConfigProperty();
        idVConfigProperty.setName(apiProperty.getKey());
        idVConfigProperty.setValue(apiProperty.getValue());
        Boolean isSecret = apiProperty.getIsSecret();
        idVConfigProperty.setConfidential(isSecret != null && isSecret);
        return idVConfigProperty;
    };

    private final Function<IdVConfigProperty, ConfigProperty> propertyToExternal = apiProperty -> {

        ConfigProperty configProperty = new ConfigProperty();
        configProperty.setKey(apiProperty.getName());
        configProperty.setValue(apiProperty.getValue());
        configProperty.setIsSecret(apiProperty.isConfidential());
        return configProperty;
    };

    private Map<String, String> getClaimMap(List<VerificationClaim> verificationclaimList) {

        Map<String, String> claimMap = new HashMap<>();
        for (VerificationClaim verificationclaim : verificationclaimList) {
            claimMap.put(verificationclaim.getLocalClaim(), verificationclaim.getIdvpClaim());
        }
        return claimMap;
    }

    private APIError handleIdVException(IdentityException e, Constants.ErrorMessage errorEnum, String... data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof IdVProviderMgtClientException) {
            status = Response.Status.BAD_REQUEST;
            errorResponse = getErrorBuilder(e, errorEnum, data)
                    .build(log, buildErrorDescription(errorEnum.getDescription(), data));
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
            errorResponse = getErrorBuilder(e, errorEnum, data)
                    .build(log, e, buildErrorDescription(errorEnum.getDescription(), data));
        }
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(IdentityException exception,
                                                         Constants.ErrorMessage errorEnum, String... data) {

        String errorCode = (StringUtils.isBlank(exception.getErrorCode())) ?
                errorEnum.getCode() : exception.getErrorCode();
        String description = (StringUtils.isBlank(exception.getMessage())) ?
                errorEnum.getDescription() : exception.getMessage();
        return new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(errorEnum.getMessage())
                .withDescription(buildErrorDescription(description, data));
    }

    private String buildErrorDescription(String description, String... data) {

        if (ArrayUtils.isNotEmpty(data)) {
            return String.format(description, (Object[]) data);
        }
        return description;
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    public APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder()
                .withCode(errorMsg.getCode())
                .withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private String includeData(Constants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }

    /**
     * Get the tenant id from the tenant domain.
     *
     * @return Tenant id.
     */
    private int getTenantId() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (StringUtils.isBlank(tenantDomain)) {
            log.warn("Tenant domain is blank or null while retrieving tenant ID");
            throw handleException(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_RETRIEVING_TENANT, tenantDomain);
        }

        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);
        if (log.isDebugEnabled()) {
            log.debug("Resolved tenant ID: " + tenantId + " for domain: " + tenantDomain);
        }
        return tenantId;
    }
}
