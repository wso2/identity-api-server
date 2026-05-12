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
import org.wso2.carbon.identity.api.server.consent.management.v2.ElementsApiService;
import org.wso2.carbon.identity.api.server.consent.management.v2.core.ElementManagementService;
import org.wso2.carbon.identity.api.server.consent.management.v2.factories.ElementManagementServiceFactory;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementDTO;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.ELEMENTS_PATH;

/**
 * Implementation of the ElementsApiService interface.
 */
public class ElementsApiServiceImpl implements ElementsApiService {

    private final ElementManagementService elementsService;

    public ElementsApiServiceImpl() {

        try {
            this.elementsService = ElementManagementServiceFactory.getElementManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating element management service.", e);
        }
    }

    @Override
    public Response elementsCreate(ElementCreateRequest elementCreateRequest) {

        ElementDTO dto = elementsService.createElement(elementCreateRequest);
        URI location = ContextLoader.buildURIForHeader(ELEMENTS_PATH + "/" + dto.getId());
        return Response.created(location).entity(dto).build();
    }

    @Override
    public Response elementsDelete(String elementId) {

        elementsService.deleteElement(elementId);
        return Response.noContent().build();
    }

    @Override
    public Response elementsGet(String elementId) {

        return Response.ok().entity(elementsService.getElement(elementId)).build();
    }

    @Override
    public Response elementsList(String filter, Integer limit, String after, String before) {

        return Response.ok().entity(elementsService.listElements(filter, limit, after, before)).build();
    }
}
