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

package org.wso2.carbon.identity.api.server.idv.provider.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.idv.provider.v1.IdvProvidersApiService;
import org.wso2.carbon.identity.api.server.idv.provider.v1.core.IdVProviderService;
import org.wso2.carbon.identity.api.server.idv.provider.v1.factories.IdVProviderServiceFactory;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderListResponse;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderRequest;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderResponse;

import static org.wso2.carbon.identity.api.server.idv.provider.common.Constants.IDV_API_PATH_COMPONENT;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * This class implements the IdvProvidersApiService interface.
 */
public class IdvProvidersApiServiceImpl implements IdvProvidersApiService {

    private static final Log log = LogFactory.getLog(IdvProvidersApiServiceImpl.class);
    private final IdVProviderService idVProviderService;

    public IdvProvidersApiServiceImpl() {
    
        try {
            this.idVProviderService = IdVProviderServiceFactory.getIdVProviderService();
            if (log.isDebugEnabled()) {
                log.debug("IdvProvidersApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating IdVProviderService: " + e.getMessage(), e);
            throw new RuntimeException("Error occurred while initiating IdVProviderService.", e);
        }
    }

    @Override
    public Response addIdVProvider(IdVProviderRequest idVProviderRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Adding IdV provider with name: " + 
                    (idVProviderRequest != null ? idVProviderRequest.getName() : "null"));
        }

        if (idVProviderRequest == null) {
            log.warn("IdVProviderRequest is null. Cannot add IdV provider.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        IdVProviderResponse idVProviderResponse =
                idVProviderService.addIdVProvider(idVProviderRequest);
        URI location = ContextLoader.buildURIForHeader(IDV_API_PATH_COMPONENT + idVProviderResponse.getId());
        log.info("IdV provider added successfully with ID: " + idVProviderResponse.getId());
        return Response.created(location).entity(idVProviderResponse).build();
    }

    @Override
    public Response deleteIdVProvider(String idvProviderId) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting IdV provider with ID: " + idvProviderId);
        }
        idVProviderService.deleteIdVProvider(idvProviderId);
        log.info("IdV provider deleted successfully with ID: " + idvProviderId);
        return Response.noContent().build();
    }

    @Override
    public Response getIdVProvider(String idvProviderId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdV provider with ID: " + idvProviderId);
        }
        IdVProviderResponse idVProviderResponse = idVProviderService.getIdVProvider(idvProviderId);
        if (log.isDebugEnabled()) {
            log.debug("IdV provider retrieved successfully with ID: " + idvProviderId);
        }
        return Response.ok().entity(idVProviderResponse).build();
    }

    @Override
    public Response getIdVProviders(Integer limit, Integer offset, String filter) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdV providers with limit: " + limit + ", offset: " + offset + 
                    ", filter: " + filter);
        }
        IdVProviderListResponse idVProviderListResponse = idVProviderService.getIdVProviders(limit, offset, filter);
        if (log.isDebugEnabled()) {
            log.debug("Retrieved " + idVProviderListResponse.getCount() + " IdV providers");
        }
        return Response.ok().entity(idVProviderListResponse).build();
    }

    @Override
    public Response updateIdVProviders(String idvProviderId, IdVProviderRequest idVProviderRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating IdV provider with ID: " + idvProviderId + ", name: " + 
                    (idVProviderRequest != null ? idVProviderRequest.getName() : "null"));
        }

        if (idVProviderRequest == null) {
            log.warn("IdVProviderRequest is null. Cannot update IdV provider.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        IdVProviderResponse idVProviderResponse =
                idVProviderService.updateIdVProvider(idvProviderId, idVProviderRequest);
        log.info("IdV provider updated successfully with ID: " + idvProviderId);
        return Response.ok().entity(idVProviderResponse).build();
    }
}
