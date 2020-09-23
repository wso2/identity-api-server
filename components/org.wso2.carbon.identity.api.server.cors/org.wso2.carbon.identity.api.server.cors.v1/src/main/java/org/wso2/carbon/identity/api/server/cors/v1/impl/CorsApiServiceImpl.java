/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.cors.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.cors.v1.CorsApiService;
import org.wso2.carbon.identity.api.server.cors.v1.core.CORSService;

import javax.ws.rs.core.Response;

/**
 * Implementation of the CORS Rest API.
 */
public class CorsApiServiceImpl implements CorsApiService {

    @Autowired
    private CORSService corsService;

    @Override
    public Response getAssociatedAppsByCORSOrigin(String corsOriginId) {

        return Response.ok().entity(corsService.getAssociatedAppsByCORSOrigin(corsOriginId)).build();
    }

    @Override
    public Response getCORSOrigins() {

        return Response.ok().entity(corsService.getCORSOrigins()).build();
    }
}
