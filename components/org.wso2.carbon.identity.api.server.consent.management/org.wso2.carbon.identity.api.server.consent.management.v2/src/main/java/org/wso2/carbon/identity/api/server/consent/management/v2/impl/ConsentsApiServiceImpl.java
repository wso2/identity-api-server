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

package org.wso2.carbon.identity.api.server.consent.management.v2.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.consent.management.v2.ConsentsApiService;
import org.wso2.carbon.identity.api.server.consent.management.v2.core.ConsentManagementService;
import org.wso2.carbon.identity.api.server.consent.management.v2.factories.ConsentManagementServiceFactory;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentResponseDTO;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.CONSENTS_PATH;

/**
 * REST API implementation for consent operations.
 */
public class ConsentsApiServiceImpl implements ConsentsApiService {

    private final ConsentManagementService consentService;

    public ConsentsApiServiceImpl() {

        try {
            this.consentService = ConsentManagementServiceFactory.getConsentManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating consent management service.", e);
        }
    }

    @Override
    public Response consentsAuthorize(String consentId, AuthorizationCreateRequest authorizationCreateRequest) {

        return Response.ok().entity(consentService.authorizeConsent(consentId, authorizationCreateRequest)).build();
    }

    @Override
    public Response consentsCreate(ConsentCreateRequest consentCreateRequest) {

        ConsentResponseDTO responseDTO = consentService.createConsent(consentCreateRequest);
        URI location = ContextLoader.buildURIForHeader(CONSENTS_PATH + "/" + responseDTO.getId());
        return Response.created(location).entity(responseDTO).build();
    }

    @Override
    public Response consentsGet(String consentId) {

        return Response.ok().entity(consentService.getConsent(consentId)).build();
    }

    @Override
    public Response consentsList(String subjectId, String serviceId, String state, UUID purposeId,
                                 UUID purposeVersionId, Integer limit, String after, String before) {

        return Response.ok().entity(
                consentService.listConsents(subjectId, serviceId, state, purposeId, purposeVersionId, limit, after,
                        before)).build();
    }

    @Override
    public Response consentsRevoke(String consentId) {

        consentService.revokeConsent(consentId);
        return Response.noContent().build();
    }

    @Override
    public Response consentsValidate(String consentId) {

        return Response.ok().entity(consentService.validateConsent(consentId)).build();
    }
}
