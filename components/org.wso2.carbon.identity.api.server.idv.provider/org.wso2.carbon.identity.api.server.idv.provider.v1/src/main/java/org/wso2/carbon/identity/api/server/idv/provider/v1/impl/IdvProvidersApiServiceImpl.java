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

    private final IdVProviderService idVProviderService;

    public IdvProvidersApiServiceImpl() {
    
        try {
            this.idVProviderService = IdVProviderServiceFactory.getIdVProviderService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating IdVProviderService.", e);
        }
    }

    @Override
    public Response addIdVProvider(IdVProviderRequest idVProviderRequest) {

        IdVProviderResponse idVProviderResponse =
                idVProviderService.addIdVProvider(idVProviderRequest);
        URI location = ContextLoader.buildURIForHeader(IDV_API_PATH_COMPONENT + idVProviderResponse.getId());
        return Response.created(location).entity(idVProviderResponse).build();
    }

    @Override
    public Response deleteIdVProvider(String idvProviderId) {

        idVProviderService.deleteIdVProvider(idvProviderId);
        return Response.noContent().build();
    }

    @Override
    public Response getIdVProvider(String idvProviderId) {

        IdVProviderResponse idVProviderResponse = idVProviderService.getIdVProvider(idvProviderId);
        return Response.ok().entity(idVProviderResponse).build();
    }

    @Override
    public Response getIdVProviders(Integer limit, Integer offset, String filter) {

        IdVProviderListResponse idVProviderListResponse = idVProviderService.getIdVProviders(limit, offset, filter);
        return Response.ok().entity(idVProviderListResponse).build();
    }

    @Override
    public Response updateIdVProviders(String idvProviderId, IdVProviderRequest idVProviderRequest) {

        IdVProviderResponse idVProviderResponse =
                idVProviderService.updateIdVProvider(idvProviderId, idVProviderRequest);
        return Response.ok().entity(idVProviderResponse).build();
    }
}
