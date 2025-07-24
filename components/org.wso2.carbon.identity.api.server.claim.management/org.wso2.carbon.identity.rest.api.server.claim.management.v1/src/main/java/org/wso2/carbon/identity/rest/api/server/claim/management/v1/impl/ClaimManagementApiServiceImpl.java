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
            claimManagementService = ServerClaimManagementServiceFactory.getServerClaimManagementService();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while initiating claim management services.", e);
        }
    }

    @Override
    public Response addClaimDialect(ClaimDialectReqDTO claimDialect) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding claim dialect: " + (claimDialect != null ? claimDialect.getDialectURI() : "null"));
        }
        String resourceId = claimManagementService.addClaimDialect(claimDialect);
        LOG.info("Successfully added claim dialect with URI: " + 
                (claimDialect != null ? claimDialect.getDialectURI() : "null"));
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response addExternalClaim(String dialectId, ExternalClaimReqDTO externalClaim) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding external claim for dialect: " + dialectId + ", claim URI: " + 
                    (externalClaim != null ? externalClaim.getClaimURI() : "null"));
        }
        String resourceId = claimManagementService.addExternalClaim(dialectId, externalClaim);
        LOG.info("Successfully added external claim for dialect: " + dialectId);
        return Response.created(getResourceLocation(dialectId, resourceId)).build();
    }

    @Override
    public Response addLocalClaim(LocalClaimReqDTO localClaim) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding local claim: " + (localClaim != null ? localClaim.getClaimURI() : "null"));
        }
        String resourceId = claimManagementService.addLocalClaim(localClaim);
        LOG.info("Successfully added local claim: " + (localClaim != null ? localClaim.getClaimURI() : "null"));
        return Response.created(getResourceLocation(LOCAL_DIALECT_PATH, resourceId)).build();
    }

    @Override
    public Response importClaimDialectFromFile(InputStream fileInputStream, Attachment fileDetail) {

        String fileName = fileDetail != null && fileDetail.getDataHandler() != null ? 
                fileDetail.getDataHandler().getName() : "unknown";
        if (LOG.isDebugEnabled()) {
            LOG.debug("Importing claim dialect from file: " + fileName);
        }
        String resourceId = claimManagementService.importClaimDialectFromFile(fileInputStream, fileDetail);
        LOG.info("Successfully imported claim dialect from file: " + fileName);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response updateClaimDialectFromFile(InputStream fileInputStream, Attachment fileDetail,
                                               Boolean preserveClaims) {

        String fileName = fileDetail != null && fileDetail.getDataHandler() != null ? 
                fileDetail.getDataHandler().getName() : "unknown";
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating claim dialect from file: " + fileName + ", preserveClaims: " + preserveClaims);
        }
        String resourceId = claimManagementService.updateClaimDialectFromFile(fileInputStream, fileDetail,
                                                                              preserveClaims);
        LOG.info("Successfully updated claim dialect from file: " + fileName);
        return Response.ok().location(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response deleteClaimDialect(String dialectId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting claim dialect: " + dialectId);
        }
        claimManagementService.deleteClaimDialect(dialectId);
        LOG.info("Successfully deleted claim dialect: " + dialectId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteExternalClaim(String dialectId, String claimId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting external claim: " + claimId + " from dialect: " + dialectId);
        }
        claimManagementService.deleteExternalClaim(dialectId, claimId);
        LOG.info("Successfully deleted external claim: " + claimId + " from dialect: " + dialectId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteLocalClaim(String claimId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting local claim: " + claimId);
        }
        claimManagementService.deleteLocalClaim(claimId);
        LOG.info("Successfully deleted local claim: " + claimId);
        return Response.noContent().build();
    }

    @Override
    public Response getClaimDialect(String dialectId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving claim dialect: " + dialectId);
        }
        return Response.ok().entity(claimManagementService.getClaimDialect(dialectId)).build();
    }

    @Override
    public Response getClaimDialects(Integer limit, Integer offset, String filter, String sort) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving claim dialects with limit: " + limit + ", offset: " + offset);
        }
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

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating claim dialect: " + dialectId + " to URI: " + 
                    (claimDialect != null ? claimDialect.getDialectURI() : "null"));
        }
        String resourceId = claimManagementService.updateClaimDialect(dialectId, claimDialect);
        LOG.info("Successfully updated claim dialect: " + dialectId);

        // Since the dialects identifier has changed we have to send the new identifier in the location header.
        return Response.ok().location(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response updateExternalClaim(String dialectId, String claimId, ExternalClaimReqDTO externalClaim) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating external claim: " + claimId + " in dialect: " + dialectId);
        }
        claimManagementService.updateExternalClaim(dialectId, claimId, externalClaim);
        LOG.info("Successfully updated external claim: " + claimId + " in dialect: " + dialectId);
        return Response.ok().build();
    }

    @Override
    public Response updateLocalClaim(String claimId, LocalClaimReqDTO localClaim) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating local claim: " + claimId);
        }
        claimManagementService.updateLocalClaim(claimId, localClaim);
        LOG.info("Successfully updated local claim: " + claimId);
        return Response.ok().build();
    }

    @Override
    public Response exportClaimDialectToFile(String dialectId, String accept) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exporting claim dialect: " + dialectId + " as file type: " + accept);
        }
        FileContent fileContent = claimManagementService.exportClaimDialectToFile(dialectId, accept);
        LOG.info("Successfully exported claim dialect: " + dialectId + " as file: " + 
                (fileContent != null ? fileContent.getFileName() : "unknown"));

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
