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

package org.wso2.carbon.identity.api.server.vc.config.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOffer;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferListItem;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferUpdateModel;
import org.wso2.carbon.identity.vc.config.management.VCOfferManager;
import org.wso2.carbon.identity.vc.config.management.exception.VCConfigMgtClientException;
import org.wso2.carbon.identity.vc.config.management.exception.VCConfigMgtException;
import org.wso2.carbon.identity.vc.config.management.exception.VCConfigMgtServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Server Verifiable Credential Configuration management service.
 */
public class ServerVCOfferManagementService {

    private static final Log LOG = LogFactory.getLog(ServerVCOfferManagementService.class);
    private final VCOfferManager vcOfferManager;

    public ServerVCOfferManagementService(VCOfferManager vcOfferManager) {

        this.vcOfferManager = vcOfferManager;
    }

    /**
     * Add a new VC offer.
     *
     * @param creationModel API request payload.
     * @return Created offer.
     */
    public VCOffer addVCOffer(VCOfferCreationModel creationModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            org.wso2.carbon.identity.vc.config.management.model.VCOffer offer =
                    toInternalModel(creationModel);
            org.wso2.carbon.identity.vc.config.management.model.VCOffer created =
                    vcOfferManager.add(offer, tenantDomain);
            return toApiModel(created);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while adding VC offer", null);
        }
    }

    /**
     * Delete a VC offer by identifier.
     *
     * @param offerId Offer identifier.
     */
    public void deleteVCOffer(String offerId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            vcOfferManager.delete(offerId, tenantDomain);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while deleting VC offer", offerId);
        }
    }

    /**
     * Retrieve a VC offer.
     *
     * @param offerId Offer identifier.
     * @return Offer.
     */
    public VCOffer getVCOffer(String offerId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            org.wso2.carbon.identity.vc.config.management.model.VCOffer offer =
                    vcOfferManager.get(offerId, tenantDomain);
            if (offer == null) {
                throw notFound(offerId);
            }
            return toApiModel(offer);
        } catch (VCConfigMgtException e) {
             throw handleVCConfigException(e, "Error while retrieving VC offer", offerId);
         }
     }

    /**
     * List VC offers for the logged-in tenant.
     *
     * @return List of offers.
     */
    public VCOfferList listVCOffers() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            List<org.wso2.carbon.identity.vc.config.management.model.VCOffer> offers =
                    vcOfferManager.list(tenantDomain);

            VCOfferList result = new VCOfferList();
            if (offers == null || offers.isEmpty()) {
                result.setTotalResults(0);
                result.setVcOffers(new ArrayList<>());
                return result;
            }

            List<VCOfferListItem> items = offers.stream()
                    .filter(Objects::nonNull)
                    .map(this::toApiListItem)
                    .collect(Collectors.toList());

            result.setTotalResults(items.size());
            result.setVcOffers(items);
            return result;
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while listing VC offers", null);
        }
    }

    /**
     * Update an existing VC offer.
     *
     * @param offerId      Offer identifier.
     * @param updateModel  Update payload.
     * @return Updated offer.
     */
    public VCOffer updateVCOffer(String offerId,
                                 VCOfferUpdateModel updateModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            org.wso2.carbon.identity.vc.config.management.model.VCOffer toUpdate =
                    toInternalModel(updateModel);
            org.wso2.carbon.identity.vc.config.management.model.VCOffer updated =
                    vcOfferManager.update(offerId, toUpdate, tenantDomain);
            return toApiModel(updated);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while updating VC offer", offerId);
        }
    }

    private VCOffer toApiModel(
            org.wso2.carbon.identity.vc.config.management.model.VCOffer model) {

        if (model == null) {
            return null;
        }

        VCOffer apiModel = new VCOffer();
        if (StringUtils.isNotBlank(model.getOfferId())) {
            apiModel.setOfferId(model.getOfferId());
        }
        apiModel.setDisplayName(model.getDisplayName());

        List<String> credentialConfigIds = model.getCredentialConfigurationIds();
        if (credentialConfigIds != null) {
            apiModel.setCredentialConfigurationIds(credentialConfigIds);
        } else {
            apiModel.setCredentialConfigurationIds(new ArrayList<>());
        }

        return apiModel;
    }

    private org.wso2.carbon.identity.vc.config.management.model.VCOffer toInternalModel(
            VCOfferCreationModel model) {

        org.wso2.carbon.identity.vc.config.management.model.VCOffer internalModel =
                new org.wso2.carbon.identity.vc.config.management.model.VCOffer();
        internalModel.setDisplayName(model.getDisplayName());
        if (model.getCredentialConfigurationIds() != null) {
            internalModel.setCredentialConfigurationIds(model.getCredentialConfigurationIds());
        }
        return internalModel;
    }

    private org.wso2.carbon.identity.vc.config.management.model.VCOffer toInternalModel(
            VCOfferUpdateModel model) {

        org.wso2.carbon.identity.vc.config.management.model.VCOffer internalModel =
                new org.wso2.carbon.identity.vc.config.management.model.VCOffer();
        if (model.getDisplayName() != null) {
            internalModel.setDisplayName(model.getDisplayName());
        }
        if (model.getCredentialConfigurationIds() != null) {
            internalModel.setCredentialConfigurationIds(model.getCredentialConfigurationIds());
        }
        return internalModel;
    }

    private APIError notFound(String data) {

        String message = "VC offer not found";
        ErrorResponse error = new ErrorResponse.Builder()
                .withCode("VC-60001")
                .withMessage("Resource not found")
                .withDescription(includeData(message, data))
                .build(LOG, message);
        return new APIError(Response.Status.NOT_FOUND, error);
    }

    private APIError handleVCConfigException(VCConfigMgtException exception, String defaultDescription, String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        String description = StringUtils.isNotBlank(exception.getMessage()) ?
                exception.getMessage() : defaultDescription;

        if (exception instanceof VCConfigMgtClientException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(prefixCode(exception.getCode()))
                    .withMessage("Invalid request or data")
                    .withDescription(includeData(description, data))
                    .build(LOG, exception.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (exception instanceof VCConfigMgtServerException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(prefixCode(exception.getCode()))
                    .withMessage("Server error")
                    .withDescription(includeData(description, data))
                    .build(LOG, exception, description);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode("VC-65000")
                    .withMessage("Unexpected error")
                    .withDescription(includeData(defaultDescription, data))
                    .build(LOG, exception, defaultDescription);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private String prefixCode(String code) {

        if (StringUtils.isBlank(code)) {
            return "VC-00000";
        }
        if (code.contains(Constants.ERROR_CODE_DELIMITER)) {
            return code;
        }
        return "VC-" + code;
    }

    private String includeData(String message, String data) {

        if (StringUtils.isNotBlank(data)) {
            return message + ": " + data;
        }
        return message;
    }

    private VCOfferListItem toApiListItem(
            org.wso2.carbon.identity.vc.config.management.model.VCOffer model) {

        if (model == null) {
            return null;
        }
        VCOfferListItem item = new VCOfferListItem();
        item.setOfferId(model.getOfferId());
        item.setDisplayName(model.getDisplayName());
        return item;
    }
}
