/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.moesif.publisher.v1.impl;

import org.wso2.carbon.identity.api.server.moesif.publisher.v1.MoesifPublishersApiService;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.core.MoesifPublisherManagementService;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.factories.MoesifPublisherManagementServiceFactory;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisher;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisherReq;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * Implementation of Moesif publishers REST API.
 */
public class MoesifPublishersApiServiceImpl implements MoesifPublishersApiService {

    private final MoesifPublisherManagementService moesifPublisherManagementService;

    public MoesifPublishersApiServiceImpl() {

        try {
            this.moesifPublisherManagementService = MoesifPublisherManagementServiceFactory
                    .getMoesifPublisherManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating Moesif publisher service.", e);
        }
    }

    @Override
    public Response createMoesifPublisher(MoesifPublisherReq moesifPublisherReq) {

        MoesifPublisher created = moesifPublisherManagementService.addMoesifPublisher(moesifPublisherReq);
        URI location = URI.create("moesif-publishers/" + created.getName());
        return Response.created(location).entity(created).build();
    }

    @Override
    public Response deleteMoesifPublisher() {

        moesifPublisherManagementService.deleteMoesifPublisher();
        return Response.noContent().build();
    }

    @Override
    public Response getMoesifPublisher() {

        MoesifPublisher publisher = moesifPublisherManagementService.getMoesifPublisher();
        return Response.ok().entity(publisher).build();
    }

    @Override
    public Response updateMoesifPublisher(MoesifPublisherReq moesifPublisherReq) {

        MoesifPublisher updated = moesifPublisherManagementService.updateMoesifPublisher(
                moesifPublisherReq.getApiKeyValue(),
                moesifPublisherReq.getEventPublisherEnablement());
        return Response.ok().entity(updated).build();
    }

}
