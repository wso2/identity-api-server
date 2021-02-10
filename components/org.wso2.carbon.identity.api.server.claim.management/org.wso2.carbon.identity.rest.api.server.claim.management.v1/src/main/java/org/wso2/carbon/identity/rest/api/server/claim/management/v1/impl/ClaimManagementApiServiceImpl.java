/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.ClaimManagementApiService;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.core.ServerClaimManagementService;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.CMT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.LOCAL_DIALECT_PATH;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;

/**
 * Claim management api service implementation.
 */
public class ClaimManagementApiServiceImpl extends ClaimManagementApiService {

    @Autowired
    private ServerClaimManagementService claimManagementService;

    @Override
    public Response addClaimDialect(ClaimDialectReqDTO claimDialect) {

        String resourceId = claimManagementService.addClaimDialect(claimDialect);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response addExternalClaim(String dialectId, ExternalClaimReqDTO externalClaim) {

        String resourceId = claimManagementService.addExternalClaim(dialectId, externalClaim);
        return Response.created(getResourceLocation(dialectId, resourceId)).build();
    }

    @Override
    public Response addLocalClaim(LocalClaimReqDTO localClaim) {

        String resourceId = claimManagementService.addLocalClaim(localClaim);
        return Response.created(getResourceLocation(LOCAL_DIALECT_PATH, resourceId)).build();
    }

    @Override
    public Response deleteClaimDialect(String dialectId) {

        claimManagementService.deleteClaimDialect(dialectId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteExternalClaim(String dialectId, String claimId) {

        claimManagementService.deleteExternalClaim(dialectId, claimId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteLocalClaim(String claimId) {

        claimManagementService.deleteLocalClaim(claimId);
        return Response.noContent().build();
    }

    @Override
    public Response getClaimDialect(String dialectId) {

        return Response.ok().entity(claimManagementService.getClaimDialect(dialectId)).build();
    }

    @Override
    public Response getClaimDialects(Integer limit, Integer offset, String filter, String sort) {

        return Response.ok().entity(claimManagementService.getClaimDialects(limit, offset, filter, sort)).build();
    }

    @Override
    public Response getExternalClaim(String dialectId, String claimId) {

        return Response.ok().entity(claimManagementService.getExternalClaim(dialectId, claimId)).build();
    }

    @Override
    public Response getExternalClaims(String dialectId, Integer limit, Integer offset, String filter, String sort) {

        return Response.ok()
                .entity(claimManagementService.getExternalClaims(dialectId, limit, offset, filter, sort))
                .build();
    }

    @Override
    public Response getLocalClaim(String claimId) {

        return Response.ok().entity(claimManagementService.getLocalClaim(claimId)).build();
    }

    @Override
    public Response getLocalClaims(String attributes, Integer limit, Integer offset, String filter,
                                   String sort, Boolean excludeIdentityClaims) {

        return Response.ok().entity(claimManagementService.getLocalClaims(
                excludeIdentityClaims, attributes, limit, offset, filter, sort)).build();
    }

    @Override
    public Response updateClaimDialect(String dialectId, ClaimDialectReqDTO claimDialect) {

        String resourceId = claimManagementService.updateClaimDialect(dialectId, claimDialect);

        // Since the dialects identifier has changed we have to send the new identifier in the location header.
        return Response.ok().location(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response updateExternalClaim(String dialectId, String claimId, ExternalClaimReqDTO externalClaim) {

        claimManagementService.updateExternalClaim(dialectId, claimId, externalClaim);
        return Response.ok().build();
    }

    @Override
    public Response updateLocalClaim(String claimId, LocalClaimReqDTO localClaim) {

        claimManagementService.updateLocalClaim(claimId, localClaim);
        return Response.ok().build();
    }

    private URI getResourceLocation(String dialectId) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + CMT_PATH_COMPONENT + "/%s", dialectId));
    }

    private URI getResourceLocation(String dialectId, String claimId) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + CMT_PATH_COMPONENT + "/%s/claims/%s", dialectId,
                claimId));
    }
}
