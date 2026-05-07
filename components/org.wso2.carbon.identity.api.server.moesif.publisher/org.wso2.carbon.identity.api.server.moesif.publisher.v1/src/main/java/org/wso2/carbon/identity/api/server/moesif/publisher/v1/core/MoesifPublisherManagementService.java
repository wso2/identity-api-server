/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.moesif.publisher.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisher;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisherAdd;
import org.wso2.carbon.identity.data.publisher.authentication.moesif.MoesifConfigurationManagementService;
import org.wso2.carbon.identity.data.publisher.authentication.moesif.dto.MoesifPublisherDTO;
import org.wso2.carbon.identity.data.publisher.authentication.moesif.exception.MoesifConfigurationManagementClientException;
import org.wso2.carbon.identity.data.publisher.authentication.moesif.exception.MoesifConfigurationManagementException;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * API-layer service that bridges REST API to the OSGi MoesifConfigurationManagementService.
 */
public class MoesifPublisherManagementService {

    private static final Log log = LogFactory.getLog(MoesifPublisherManagementService.class);
    private final MoesifConfigurationManagementService moesifConfigurationManagementService;

    public MoesifPublisherManagementService(
            MoesifConfigurationManagementService moesifConfigurationManagementService) {

        this.moesifConfigurationManagementService = moesifConfigurationManagementService;
    }

    public MoesifPublisher addMoesifPublisher(MoesifPublisherAdd moesifPublisherAdd) {

        try {
            MoesifPublisherDTO result = moesifConfigurationManagementService
                    .addMoesifPublisher(moesifPublisherAdd.getName(), moesifPublisherAdd.getApiKeyValue());
            return buildResponse(result);
        } catch (MoesifConfigurationManagementException e) {
            throw handleException(e);
        }
    }

    public MoesifPublisher getMoesifPublisher(String publisherName) {

        try {
            MoesifPublisherDTO result = moesifConfigurationManagementService.getMoesifPublisher(publisherName);
            return buildResponse(result);
        } catch (MoesifConfigurationManagementException e) {
            throw handleException(e);
        }
    }

    public List<MoesifPublisher> getMoesifPublishers() {

        try {
            List<MoesifPublisherDTO> results = moesifConfigurationManagementService.getMoesifPublishers();
            return results.stream().map(this::buildResponse).collect(Collectors.toList());
        } catch (MoesifConfigurationManagementException e) {
            throw handleException(e);
        }
    }

    public MoesifPublisher updateMoesifPublisherApiKey(String publisherName, String apiKeyValue) {

        try {
            MoesifPublisherDTO result = moesifConfigurationManagementService
                    .updateMoesifPublisherApiKey(publisherName, apiKeyValue);
            return buildResponse(result);
        } catch (MoesifConfigurationManagementException e) {
            throw handleException(e);
        }
    }

    public void deleteMoesifPublisher(String publisherName) {

        try {
            moesifConfigurationManagementService.deleteMoesifPublisher(publisherName);
        } catch (MoesifConfigurationManagementException e) {
            throw handleException(e);
        }
    }

    private MoesifPublisher buildResponse(MoesifPublisherDTO dto) {

        MoesifPublisher publisher = new MoesifPublisher();
        publisher.setName(dto.getName());
        return publisher;
    }

    private APIError handleException(MoesifConfigurationManagementException e) {

        if (e instanceof MoesifConfigurationManagementClientException) {
            return buildClientError(e);
        }
        return buildServerError(e);
    }

    private APIError buildClientError(MoesifConfigurationManagementException e) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(e.getMessage())
                .withDescription(e.getDescription())
                .build(log, e.getMessage());

        Response.Status status = Response.Status.BAD_REQUEST;
        if (e.getErrorCode() != null) {
            if (e.getErrorCode().equals("MOESIF_60002")) {
                status = Response.Status.CONFLICT;
            } else if (e.getErrorCode().equals("MOESIF_60004")) {
                status = Response.Status.NOT_FOUND;
            }
        }
        return new APIError(status, errorResponse);
    }

    private APIError buildServerError(MoesifConfigurationManagementException e) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(e.getMessage())
                .withDescription(e.getDescription())
                .build(log, e, e.getMessage());

        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }
}
