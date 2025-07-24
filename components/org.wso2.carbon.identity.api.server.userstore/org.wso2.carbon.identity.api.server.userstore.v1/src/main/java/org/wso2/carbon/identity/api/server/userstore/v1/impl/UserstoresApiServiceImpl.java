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

package org.wso2.carbon.identity.api.server.userstore.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.FileContent;
import org.wso2.carbon.identity.api.server.userstore.v1.UserstoresApiService;
import org.wso2.carbon.identity.api.server.userstore.v1.core.ServerUserStoreService;
import org.wso2.carbon.identity.api.server.userstore.v1.factories.ServerUserStoreServiceFactory;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ClaimAttributeMapping;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.userstore.v1.model.RDBMSConnectionReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreResponse;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.userstore.common.UserStoreConstants.USER_STORE_PATH_COMPONENT;

/**
 * User Store API Service Implementation
 */
public class UserstoresApiServiceImpl implements UserstoresApiService {

    private static final Log LOG = LogFactory.getLog(UserstoresApiServiceImpl.class);
    private final ServerUserStoreService serverUserStoreService;

    public UserstoresApiServiceImpl() {

        try {
            this.serverUserStoreService = ServerUserStoreServiceFactory.getServerUserStoreService();
            LOG.debug("UserstoresApiServiceImpl initialized successfully");
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating ServerUserStoreService", e);
            throw new RuntimeException("Error occurred while initiating ServerUserStoreService.", e);
        }
    }

    @Override
    public Response addUserStore(UserStoreReq userStoreReq) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding user store: " + (userStoreReq != null ? userStoreReq.getName() : "null"));
        }
        UserStoreResponse response = serverUserStoreService.addUserStore(userStoreReq);
        LOG.info("User store added successfully with ID: " + response.getId());
        return Response.created(getResourceLocation(response.getId())).entity(response).build();
    }

    @Override
    public Response deleteUserStore(String userstoreDomainId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting user store with domain ID: " + userstoreDomainId);
        }
        serverUserStoreService.deleteUserStore(userstoreDomainId);
        LOG.info("User store deleted successfully with domain ID: " + userstoreDomainId);
        return Response.noContent().build();
    }

    @Override
    public Response exportUserStoreToFile(String userstoreDomainId, String accept) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exporting user store with domain ID: " + userstoreDomainId + ", format: " + accept);
        }
        FileContent fileContent = serverUserStoreService.exportUserStore(userstoreDomainId, accept);
        LOG.info("User store exported successfully for domain ID: " + userstoreDomainId);

        return Response.ok()
                .type(fileContent.getFileType())
                .header("Content-Disposition", "attachment; filename=\""
                        + fileContent.getFileName() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .entity(fileContent.getContent().getBytes(StandardCharsets.UTF_8))
                .build();
    }

    @Override
    public Response getAvailableUserStoreTypes() {

        LOG.debug("Retrieving available user store types");
        return Response.ok().entity(serverUserStoreService.getAvailableUserStoreTypes()).build();
    }

    @Override
    public Response getPrimaryUserStore() {

        LOG.debug("Retrieving primary user store");
        return Response.ok().entity(serverUserStoreService.getPrimaryUserStore()).build();
    }

    @Override
    public Response getSecondaryUserStores(Integer limit, Integer offset, String filter, String sort,
                                           String requiredAttributes) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving secondary user stores with parameters - limit: " + limit + ", offset: " + offset 
                    + ", filter: " + filter + ", sort: " + sort);
        }
        return Response.ok()
                .entity(serverUserStoreService.getUserStoreList(limit, offset, filter, sort, requiredAttributes))
                .build();
    }

    @Override
    public Response getUserStoreAttributeMappings(String typeId, Boolean includeIdentityClaimMappings) {

        boolean includeIdentityClaims = false;
        if (includeIdentityClaimMappings != null) {
            includeIdentityClaims = includeIdentityClaimMappings;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving user store attribute mappings for typeId: " + typeId + 
                    ", includeIdentityClaims: " + includeIdentityClaims);
        }
        return Response.ok().entity(serverUserStoreService.getUserStoreAttributeMappings(typeId,
                includeIdentityClaims)).build();
    }

    @Override
    public Response getUserStoreByDomainId(String userstoreDomainId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving user store by domain ID: " + userstoreDomainId);
        }
        return Response.ok().entity(serverUserStoreService.getUserStoreByDomainId(userstoreDomainId)).build();
    }

    @Override
    public Response getUserStoreManagerProperties(String typeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving user store manager properties for typeId: " + typeId);
        }
        return Response.ok().entity(serverUserStoreService.getUserStoreManagerProperties(typeId)).build();
    }

    @Override
    public Response updateAttributeMappings(String userstoreDomainId,
                                            List<ClaimAttributeMapping> claimAttributeMappings) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating attribute mappings for user store domain ID: " + userstoreDomainId + 
                    ", mappings count: " + (claimAttributeMappings != null ? claimAttributeMappings.size() : 0));
        }
        serverUserStoreService.updateClaimAttributeMappings(userstoreDomainId,
                claimAttributeMappings);
        LOG.info("Attribute mappings updated successfully for domain ID: " + userstoreDomainId);
        return Response.ok().build();
    }

    @Override
    public Response importUserStoreFromFile(InputStream fileInputStream, Attachment fileDetail) {

        String fileName = fileDetail != null && fileDetail.getDataHandler() != null ? 
                fileDetail.getDataHandler().getName() : "unknown";
        if (LOG.isDebugEnabled()) {
            LOG.debug("Importing user store from file: " + fileName);
        }
        String resourceId = serverUserStoreService.importUserStore(fileInputStream, fileDetail);
        LOG.info("User store imported successfully from file: " + fileName + ", resource ID: " + resourceId);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + USER_STORE_PATH_COMPONENT +
                "/" + resourceId);
        return Response.created(location).build();
    }

    @Override
    public Response patchUserStore(String userstoreDomainId, List<PatchDocument> patchDocument) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Patching user store with domain ID: " + userstoreDomainId + 
                    ", patch operations count: " + (patchDocument != null ? patchDocument.size() : 0));
        }
        return Response.ok().entity(serverUserStoreService.patchUserStore(userstoreDomainId, patchDocument)).build();
    }

    @Override
    public Response testRDBMSConnection(RDBMSConnectionReq rdBMSConnectionReq) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Testing RDBMS connection for domain: " + 
                    (rdBMSConnectionReq != null ? rdBMSConnectionReq.getDomain() : "null"));
        }
        return Response.ok().entity(serverUserStoreService.testRDBMSConnection(rdBMSConnectionReq)).build();
    }

    @Override
    public Response updateUserStore(String userstoreDomainId, UserStoreReq userStoreReq) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating user store with domain ID: " + userstoreDomainId + 
                    ", user store name: " + (userStoreReq != null ? userStoreReq.getName() : "null"));
        }
        return Response.ok().entity(serverUserStoreService.editUserStore(userstoreDomainId, userStoreReq)).build();
    }

    @Override
    public Response updateUserStoreFromFile(String userstoreDomainId, InputStream fileInputStream,
                                            Attachment fileDetail) {

        String fileName = fileDetail != null && fileDetail.getDataHandler() != null ? 
                fileDetail.getDataHandler().getName() : "unknown";
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating user store from file - domain ID: " + userstoreDomainId + ", file: " + fileName);
        }
        String resourceId =
                serverUserStoreService.updateUserStoreFromFile(userstoreDomainId, fileInputStream, fileDetail);
        LOG.info("User store updated successfully from file - domain ID: " + userstoreDomainId + 
                ", file: " + fileName + ", resource ID: " + resourceId);
        return Response.ok().location(getResourceLocation(resourceId)).build();
    }

    private URI getResourceLocation(String id) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + USER_STORE_PATH_COMPONENT + "/%s", id));
    }
}
