/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.endpoint.configuration.dispatcher;

import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.error.APIError;

import java.util.ResourceBundle;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Map API Error status codes.
 */
public class APIErrorExceptionMapper implements ExceptionMapper<WebApplicationException> {

    static final String BUNDLE = "ErrorMappings";
    static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE);

    private static Response.Status getHttpsStatusCode(String errorCode, Response.Status defaultStatus) {

        Response.Status mappedStatus = null;
        try {
            String statusCodeValue = resourceBundle.getString(errorCode);
            mappedStatus = Response.Status.fromStatusCode(Integer.parseInt(statusCodeValue));
        } catch (Throwable e) {
            //Ignore if error mapping has invalid input
        }
        return mappedStatus != null ? mappedStatus : defaultStatus;
    }

    @Override
    public Response toResponse(WebApplicationException e) {

        if (e instanceof APIError) {
            Object response = ((APIError) e).getResponseEntity();
            Response.Status status = getHttpsStatusCode(((APIError) e).getCode(), ((APIError) e).getStatus());
            return buildResponse(response, status);

        }
        return e.getResponse();
    }

    private Response buildResponse(Object response, Response.Status status) {

        if (response != null) {
            return Response.status(status).entity(response)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(status).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build();
        }
    }
}
