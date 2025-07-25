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

package org.wso2.carbon.identity.api.server.idp.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.idp.v1.TrustedTokenIssuersApiService;
import org.wso2.carbon.identity.api.server.idp.v1.core.ServerIdpManagementService;
import org.wso2.carbon.identity.api.server.idp.v1.factories.ServerIdpManagementServiceFactory;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.TrustedTokenIssuerPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.TrustedTokenIssuerResponse;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.TRUSTED_TOKEN_ISSUER_PATH_COMPONENT;

/**
 * Implementation of the Trusted Token Issuers REST API.
 */
public class TrustedTokenIssuersApiServiceImpl implements TrustedTokenIssuersApiService {

    private static final Log log = LogFactory.getLog(TrustedTokenIssuersApiServiceImpl.class);
    private final ServerIdpManagementService idpManagementService;

    public TrustedTokenIssuersApiServiceImpl() {

        try {
            this.idpManagementService = ServerIdpManagementServiceFactory.getServerIdpManagementService();
            if (log.isDebugEnabled()) {
                log.debug("TrustedTokenIssuersApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating ServerIdpManagementService.", e);
            throw new RuntimeException("Error occurred while initiating ServerIdpManagementService.", e);
        }
    }

    @Override
    public Response addTrustedTokenIssuer(TrustedTokenIssuerPOSTRequest trustedTokenIssuerPOSTRequest) {

        IdentityProviderPOSTRequest identityProviderPOSTRequest =
                getIdentityProviderPOSTRequest(trustedTokenIssuerPOSTRequest);
        IdentityProviderResponse idPResponse = idpManagementService.addIDP(identityProviderPOSTRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                TRUSTED_TOKEN_ISSUER_PATH_COMPONENT + "/" + idPResponse.getId());
        return Response.created(location).entity(getTrustedTokenIssuerResponse(idPResponse)).build();
    }

    @Override
    public Response deleteTrustedTokenIssuer(String trustedTokenIssuerId, Boolean force) {

        if (force) {
            idpManagementService.forceDeleteIDP(trustedTokenIssuerId);
        } else {
            idpManagementService.deleteIDP(trustedTokenIssuerId);
        }
        return Response.noContent().build();
    }

    @Override
    public Response getTrustedTokenIssuer(String trustedTokenIssuerId) {

        IdentityProviderResponse idPResponse = idpManagementService.getIDP(trustedTokenIssuerId);
        return Response.ok().entity(getTrustedTokenIssuerResponse(idPResponse)).build();
    }

    @Override
    public Response getTrustedTokenIssuers(Integer limit, Integer offset, String filter, String sortBy,
                                           String sortOrder, String requiredAttributes) {

        return Response.ok().entity(idpManagementService.getTrustedTokenIssuers(requiredAttributes, limit, offset,
                filter, sortBy, sortOrder)).build();
    }

    @Override
    public Response patchTrustedTokenIssuer(String trustedTokenIssuerId, List<Patch> patchRequest) {

        IdentityProviderResponse idPResponse = idpManagementService.patchIDP(trustedTokenIssuerId, patchRequest);
        return Response.ok().entity(getTrustedTokenIssuerResponse(idPResponse)).build();
    }

    private IdentityProviderPOSTRequest getIdentityProviderPOSTRequest(TrustedTokenIssuerPOSTRequest
                                                                               trustedTokenIssuerPOSTRequest) {

        IdentityProviderPOSTRequest identityProviderPOSTRequest = new IdentityProviderPOSTRequest();
        identityProviderPOSTRequest.setIdpIssuerName(trustedTokenIssuerPOSTRequest.getIssuer());
        identityProviderPOSTRequest.setAlias(trustedTokenIssuerPOSTRequest.getAlias());
        identityProviderPOSTRequest.setName(trustedTokenIssuerPOSTRequest.getName());
        identityProviderPOSTRequest.setCertificate(trustedTokenIssuerPOSTRequest.getCertificate());
        identityProviderPOSTRequest.setDescription(trustedTokenIssuerPOSTRequest.getDescription());
        identityProviderPOSTRequest.setImage(trustedTokenIssuerPOSTRequest.getImage());
        identityProviderPOSTRequest.setTemplateId(trustedTokenIssuerPOSTRequest.getTemplateId());
        identityProviderPOSTRequest.setClaims(trustedTokenIssuerPOSTRequest.getClaims());
        return identityProviderPOSTRequest;
    }

    private TrustedTokenIssuerResponse getTrustedTokenIssuerResponse(IdentityProviderResponse
                                                                             identityProviderResponse) {

        TrustedTokenIssuerResponse trustedTokenIssuerResponse = new TrustedTokenIssuerResponse();
        trustedTokenIssuerResponse.setId(identityProviderResponse.getId());
        trustedTokenIssuerResponse.setName(identityProviderResponse.getName());
        trustedTokenIssuerResponse.setDescription(identityProviderResponse.getDescription());
        trustedTokenIssuerResponse.setTemplateId(identityProviderResponse.getTemplateId());
        trustedTokenIssuerResponse.setIsEnabled(identityProviderResponse.getIsEnabled());
        trustedTokenIssuerResponse.setIsPrimary(identityProviderResponse.getIsPrimary());
        trustedTokenIssuerResponse.setImage(identityProviderResponse.getImage());
        trustedTokenIssuerResponse.setIsFederationHub(identityProviderResponse.getIsFederationHub());
        trustedTokenIssuerResponse.setHomeRealmIdentifier(identityProviderResponse.getHomeRealmIdentifier());
        trustedTokenIssuerResponse.setCertificate(identityProviderResponse.getCertificate());
        trustedTokenIssuerResponse.setAlias(identityProviderResponse.getAlias());
        trustedTokenIssuerResponse.setIssuer(identityProviderResponse.getIdpIssuerName());
        trustedTokenIssuerResponse.setClaims(identityProviderResponse.getClaims());
        trustedTokenIssuerResponse.setRoles(identityProviderResponse.getRoles());
        trustedTokenIssuerResponse.setGroups(identityProviderResponse.getGroups());
        trustedTokenIssuerResponse.setFederatedAuthenticators(identityProviderResponse.getFederatedAuthenticators());
        trustedTokenIssuerResponse.setProvisioning(identityProviderResponse.getProvisioning());
        return trustedTokenIssuerResponse;
    }
}
