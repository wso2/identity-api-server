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

package org.wso2.carbon.identity.api.server.jwt.issuers.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.idp.v1.core.ServerIdpManagementService;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.JwtIssuersApiService;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.JWTIssuerPOSTRequest;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Implementation of the JWT Issuers REST API.
 */
public class JwtIssuersApiServiceImpl implements JwtIssuersApiService {

    private static final String IDP_PATH_COMPONENT = "";
    @Autowired
    private ServerIdpManagementService serverIdpManagementService;

    @Override
    public Response addJWTIssuer(JWTIssuerPOSTRequest jwTIssuerPOSTRequest) {
        IdentityProviderPOSTRequest identityProviderPOSTRequest = new IdentityProviderPOSTRequest();
        //set name
        identityProviderPOSTRequest.setIdpIssuerName(jwTIssuerPOSTRequest.getIdpIssuerName());
        identityProviderPOSTRequest.setAlias(jwTIssuerPOSTRequest.getAlias());
        identityProviderPOSTRequest.setName(jwTIssuerPOSTRequest.getName());
        Certificate certificate = jwTIssuerPOSTRequest.getCertificate();
        if (certificate != null) {
            org.wso2.carbon.identity.api.server.idp.v1.model.Certificate certificate1 = new
                    org.wso2.carbon.identity.api.server.idp.v1.model.Certificate();
            certificate1.setJwksUri(certificate.getJwksUri());
            certificate1.setCertificates(certificate.getCertificates());
            identityProviderPOSTRequest.setCertificate(certificate1);
        }
        identityProviderPOSTRequest.setDescription(jwTIssuerPOSTRequest.getDescription());
        identityProviderPOSTRequest.setImage(jwTIssuerPOSTRequest.getImage());
        identityProviderPOSTRequest.setTemplateId(jwTIssuerPOSTRequest.getTemplateId());
        IdentityProviderResponse idPResponse = serverIdpManagementService.addIDP(identityProviderPOSTRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + "/" +
                idPResponse.getId());
        return Response.created(location).entity(idPResponse).build();
    }

    @Override
    public Response getJwtIssuers(Integer limit, Integer offset, String filter, String sortBy, String sortOrder,
                                  String requiredAttributes) {

        return Response.ok().entity(serverIdpManagementService.getJwtIssuers(requiredAttributes, limit, offset, filter,
                sortBy, sortOrder)).build();
    }
}
