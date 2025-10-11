/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.api.server.common.FileContent;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.ClaimManagementApiService;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.core.ServerClaimManagementService;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.factories.ServerClaimManagementServiceFactory;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.CMT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.LOCAL_DIALECT_PATH;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;

/**
 * Claim management api service implementation.
 */
public class ClaimManagementApiServiceImpl extends ClaimManagementApiService {

    private static final Log LOG = LogFactory.getLog(ClaimManagementApiServiceImpl.class);
    private static final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    private final ServerClaimManagementService claimManagementService;

    public ClaimManagementApiServiceImpl() {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing ClaimManagementApiServiceImpl");
            }
            claimManagementService = ServerClaimManagementServiceFactory.getServerClaimManagementService();
        } catch (Exception e) {
            LOG.error("Error occurred while initiating claim management services", e);
            throw new RuntimeException("Error occurred while initiating claim management services.", e);
        }
    }

    @Override
    public Response addClaimDialect(ClaimDialectReqDTO claimDialect) {

        if (claimDialect == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Null claim dialect provided for addition");
            }
            throw new IllegalArgumentException("Claim dialect cannot be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding claim dialect: " + claimDialect.getDialectURI());
        }
        String resourceId = claimManagementService.addClaimDialect(claimDialect);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response addExternalClaim(String dialectId, ExternalClaimReqDTO externalClaim) {

        if (externalClaim == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Null external claim provided for addition to dialect: " + dialectId);
            }
            throw new IllegalArgumentException("External claim cannot be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding external claim to dialect: " + dialectId + ", claim URI: " + 
                     externalClaim.getClaimURI());
        }
        String resourceId = claimManagementService.addExternalClaim(dialectId, externalClaim);
        return Response.created(getResourceLocation(dialectId, resourceId)).build();
    }

    @Override
    public Response addLocalClaim(LocalClaimReqDTO localClaim) {

        if (localClaim == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Null local claim provided for addition");
            }
            throw new IllegalArgumentException("Local claim cannot be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding local claim: " + localClaim.getClaimURI());
        }
        String resourceId = claimManagementService.addLocalClaim(localClaim);
        return Response.created(getResourceLocation(LOCAL_DIALECT_PATH, resourceId)).build();
    }

    @Override
    public Response importClaimDialectFromFile(InputStream fileInputStream, Attachment fileDetail) {

        if (fileInputStream == null || fileDetail == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Null file input stream or file detail provided for claim dialect import");
            }
            throw new IllegalArgumentException("File input stream and file detail cannot be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Importing claim dialect from file: " + fileDetail.getContentDisposition().getFilename());
        }
        String resourceId = claimManagementService.importClaimDialectFromFile(fileInputStream, fileDetail);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response updateClaimDialectFromFile(InputStream fileInputStream, Attachment fileDetail,
                                               Boolean preserveClaims) {

        String resourceId = claimManagementService.updateClaimDialectFromFile(fileInputStream, fileDetail,
                                                                              preserveClaims);
        return Response.ok().location(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response deleteClaimDialect(String dialectId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting claim dialect: " + dialectId);
        }
        claimManagementService.deleteClaimDialect(dialectId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteExternalClaim(String dialectId, String claimId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting external claim: " + claimId + " from dialect: " + dialectId);
        }
        claimManagementService.deleteExternalClaim(dialectId, claimId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteLocalClaim(String claimId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting local claim: " + claimId);
        }
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
                                   String sort, Boolean excludeIdentityClaims, Boolean excludeHiddenClaims,
                                   String profile) {

        return Response.ok().entity(claimManagementService.getLocalClaims(
                excludeIdentityClaims, attributes, limit, offset, filter, sort, excludeHiddenClaims, profile)).build();
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

    @Override
    public Response exportClaimDialectToFile(String dialectId, String accept) {

        FileContent fileContent = claimManagementService.exportClaimDialectToFile(dialectId, accept);

        return Response.ok()
                .type(fileContent.getFileType())
                .header(HTTP_HEADER_CONTENT_DISPOSITION, "attachment; filename=\""
                        + fileContent.getFileName() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .entity(fileContent.getContent().getBytes(StandardCharsets.UTF_8))
                .build();
    }

    private URI getResourceLocation(String dialectId) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + CMT_PATH_COMPONENT + "/%s", dialectId));
    }

    private URI getResourceLocation(String dialectId, String claimId) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + CMT_PATH_COMPONENT + "/%s/claims/%s", dialectId,
                claimId));
    }
}
