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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log log = LogFactory.getLog(ScriptLibrariesApiServiceImpl.class);
    private final ServerScriptLibrariesService serverScriptLibrariesService;

    public ScriptLibrariesApiServiceImpl() {

        if (log.isDebugEnabled()) {
            log.debug("Initializing ScriptLibrariesApiServiceImpl.");
        }
        try {
            this.serverScriptLibrariesService = ServerScriptLibrariesServiceFactory.getServerScriptLibrariesService();
            if (log.isDebugEnabled()) {
                log.debug("ScriptLibrariesApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating ServerScriptLibrariesService: " + e.getMessage());
            throw new RuntimeException("Error occurred while initiating ServerScriptLibrariesService.", e);
        }
    }

    @Override
    public Response addScriptLibrary(String name, InputStream contentInputStream, Attachment contentDetail,
                                     String description) {

        if (log.isDebugEnabled()) {
            log.debug("Adding script library with name: " + (name != null ? name : "null"));
        }
        
        serverScriptLibrariesService.addScriptLibrary(name, contentInputStream, description);
        
        try {
            URI location =
                    ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + SCRIPT_LIBRARY_PATH_COMPONENT
                            + "/" + URLEncoder.encode(name, StandardCharsets.UTF_8.name())
                            .replace("+", "%20"));
            log.info("Script library added successfully with name: " + (name != null ? name : "null"));
            return Response.created(location).build();
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding URL for script library name: " + (name != null ? name : "null") + 
                     ", Error: " + e.getMessage());
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

        if (log.isDebugEnabled()) {
            log.debug("Deleting script library with name: " + (scriptLibraryName != null ? scriptLibraryName : "null"));
        }
        
        serverScriptLibrariesService.deleteScriptLibrary(scriptLibraryName);
        log.info("Script library deleted successfully with name: " + 
                (scriptLibraryName != null ? scriptLibraryName : "null"));
        return Response.noContent().build();
    }

    @Override
    public Response getScriptLibraries(Integer limit, Integer offset) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving script libraries with limit: " + limit + ", offset: " + offset);
        }
        
        ScriptLibraryListResponse scriptLibraryListResponse = serverScriptLibrariesService.getScriptLibraries(limit,
                offset);
        
        if (log.isDebugEnabled()) {
            int count = scriptLibraryListResponse != null ? scriptLibraryListResponse.getCount() : 0;
            log.debug("Retrieved " + count + " script libraries.");
        }
        
        return Response.ok().entity(scriptLibraryListResponse).build();
    }

    @Override
    public Response getScriptLibraryByName(String scriptLibraryName) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving script library by name: " + (scriptLibraryName != null ? scriptLibraryName : "null"));
        }
        
        ScriptLibraryResponse scriptLibraryResponse = serverScriptLibrariesService.getScriptLibrary(scriptLibraryName);
        
        if (log.isDebugEnabled()) {
            log.debug("Successfully retrieved script library: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null"));
        }
        
        return Response.ok().entity(scriptLibraryResponse).build();
    }

    @Override
    public Response getScriptLibraryContentByName(String scriptLibraryName) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving script library content by name: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null"));
        }
        
        String content = serverScriptLibrariesService.getScriptLibraryContentByName(scriptLibraryName);
        
        if (log.isDebugEnabled()) {
            log.debug("Successfully retrieved script library content for: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null"));
        }
        
        return Response.ok().entity(content).build();
    }

    @Override
    public Response updateScriptLibrary(String scriptLibraryName, InputStream contentInputStream,
                                        Attachment contentDetail, String description) {

        if (log.isDebugEnabled()) {
            log.debug("Updating script library with name: " + 
                     (scriptLibraryName != null ? scriptLibraryName : "null"));
        }
        
        serverScriptLibrariesService.updateScriptLibrary(scriptLibraryName, contentInputStream, description);
        log.info("Script library updated successfully with name: " + 
                (scriptLibraryName != null ? scriptLibraryName : "null"));
        return Response.ok().build();
    }
}
