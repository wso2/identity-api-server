/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.script.library.v1.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.script.library.common.Constants;
import org.wso2.carbon.identity.api.server.script.library.v1.ScriptLibrariesApiService;
import org.wso2.carbon.identity.api.server.script.library.v1.core.ServerScriptLibrariesService;
import org.wso2.carbon.identity.api.server.script.library.v1.factories.ServerScriptLibrariesServiceFactory;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryListResponse;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryResponse;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.script.library.common.Constants.SCRIPT_LIBRARY_PATH_COMPONENT;

/**
 * Implementation of the script library Rest API.
 */
public class ScriptLibrariesApiServiceImpl implements ScriptLibrariesApiService {

    private final ServerScriptLibrariesService serverScriptLibrariesService;

    public ScriptLibrariesApiServiceImpl() {

        try {
            this.serverScriptLibrariesService = ServerScriptLibrariesServiceFactory.getServerScriptLibrariesService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ServerScriptLibrariesService.", e);
        }
    }

    @Override
    public Response addScriptLibrary(String name, InputStream contentInputStream, Attachment contentDetail,
                                     String description) {

        serverScriptLibrariesService.addScriptLibrary(name, contentInputStream, description);
        try {
            URI location =
                    ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + SCRIPT_LIBRARY_PATH_COMPONENT
                            + "/" + URLEncoder.encode(name, StandardCharsets.UTF_8.name())
                            .replace("+", "%20"));
            return Response.created(location).build();
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withCode(Constants.ErrorMessage.ERROR_CODE_ERROR_ENCODING_URL
                            .getCode()).withMessage(Constants.ErrorMessage.ERROR_CODE_ERROR_ENCODING_URL.getMessage())
                            .withDescription(Constants.ErrorMessage.ERROR_CODE_ERROR_ENCODING_URL.getDescription())
                            .build();
            throw new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        }
    }

    @Override
    public Response deleteScriptLibrary(String scriptLibraryName) {

        serverScriptLibrariesService.deleteScriptLibrary(scriptLibraryName);
        return Response.noContent().build();
    }

    @Override
    public Response getScriptLibraries(Integer limit, Integer offset) {

        ScriptLibraryListResponse scriptLibraryListResponse = serverScriptLibrariesService.getScriptLibraries(limit,
                offset);
        return Response.ok().entity(scriptLibraryListResponse).build();
    }

    @Override
    public Response getScriptLibraryByName(String scriptLibraryName) {

        ScriptLibraryResponse scriptLibraryResponse = serverScriptLibrariesService.getScriptLibrary(scriptLibraryName);
        return Response.ok().entity(scriptLibraryResponse).build();
    }

    @Override
    public Response getScriptLibraryContentByName(String scriptLibraryName) {

        return Response.ok().entity(serverScriptLibrariesService.getScriptLibraryContentByName(scriptLibraryName))
                .build();
    }

    @Override
    public Response updateScriptLibrary(String scriptLibraryName, InputStream contentInputStream,
                                        Attachment contentDetail, String description) {

        serverScriptLibrariesService.updateScriptLibrary(scriptLibraryName, contentInputStream, description);
        return Response.ok().build();
    }
}
