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

package org.wso2.carbon.identity.api.server.script.library.v2.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.script.library.common.core.ScriptLibraryPage;
import org.wso2.carbon.identity.api.server.script.library.common.core.ServerScriptLibrariesService;
import org.wso2.carbon.identity.api.server.script.library.common.factories.ServerScriptLibrariesServiceFactory;
import org.wso2.carbon.identity.api.server.script.library.v2.ScriptLibrariesApiService;
import org.wso2.carbon.identity.api.server.script.library.v2.model.ScriptLibrary;
import org.wso2.carbon.identity.api.server.script.library.v2.model.ScriptLibraryListResponse;
import org.wso2.carbon.identity.api.server.script.library.v2.model.ScriptLibraryResponse;
import org.wso2.carbon.identity.api.server.script.library.v2.util.ScriptLibraryIdUtil;
import org.wso2.carbon.identity.functions.library.mgt.model.FunctionLibrary;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.script.library.common.Constants.SCRIPT_LIBRARY_CONTENT_PATH;
import static org.wso2.carbon.identity.api.server.script.library.common.Constants.SCRIPT_LIBRARY_PATH_COMPONENT;

/**
 * Implementation of the script library Rest API v2. Script libraries are identified by their base64url encoded
 * name, which is resolved to the name here before calling the service layer.
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
        URI location = ContextLoader.buildURIForHeader(V2_API_PATH_COMPONENT + SCRIPT_LIBRARY_PATH_COMPONENT
                + "/" + ScriptLibraryIdUtil.encode(name));
        return Response.created(location).build();
    }

    @Override
    public Response deleteScriptLibrary(String scriptLibraryId) {

        serverScriptLibrariesService.deleteScriptLibrary(ScriptLibraryIdUtil.decode(scriptLibraryId));
        return Response.noContent().build();
    }

    @Override
    public Response getScriptLibraries(Integer limit, Integer offset) {

        ScriptLibraryPage page = serverScriptLibrariesService.getScriptLibraries(limit, offset);
        ScriptLibraryListResponse scriptLibraryListResponse = new ScriptLibraryListResponse();
        if (page.getTotalResults() == 0) {
            scriptLibraryListResponse.setCount(0);
            return Response.ok().entity(scriptLibraryListResponse).build();
        }
        List<ScriptLibrary> scriptLibraries = new ArrayList<>();
        for (FunctionLibrary functionLibrary : page.getScriptLibraries()) {
            String scriptLibraryId = ScriptLibraryIdUtil.encode(functionLibrary.getFunctionLibraryName());
            ScriptLibrary scriptLibrary = new ScriptLibrary();
            scriptLibrary.setId(scriptLibraryId);
            scriptLibrary.setName(functionLibrary.getFunctionLibraryName());
            scriptLibrary.setDescription(functionLibrary.getDescription());
            scriptLibrary.setSelf(ContextLoader.buildURIForBody(V2_API_PATH_COMPONENT
                    + SCRIPT_LIBRARY_PATH_COMPONENT + "/" + scriptLibraryId).toString());
            scriptLibraries.add(scriptLibrary);
        }
        scriptLibraryListResponse.setScriptLibraries(scriptLibraries);
        scriptLibraryListResponse.setCount(scriptLibraries.size());
        scriptLibraryListResponse.setTotalResults(page.getTotalResults());
        scriptLibraryListResponse.setStartIndex(page.getOffset() + 1);
        return Response.ok().entity(scriptLibraryListResponse).build();
    }

    @Override
    public Response getScriptLibraryById(String scriptLibraryId) {

        FunctionLibrary scriptLibrary =
                serverScriptLibrariesService.getScriptLibrary(ScriptLibraryIdUtil.decode(scriptLibraryId));
        String canonicalId = ScriptLibraryIdUtil.encode(scriptLibrary.getFunctionLibraryName());
        ScriptLibraryResponse scriptLibraryResponse = new ScriptLibraryResponse();
        scriptLibraryResponse.setId(canonicalId);
        scriptLibraryResponse.setName(scriptLibrary.getFunctionLibraryName());
        scriptLibraryResponse.setDescription(scriptLibrary.getDescription());
        scriptLibraryResponse.setContentRef(ContextLoader.buildURIForBody(V2_API_PATH_COMPONENT
                + SCRIPT_LIBRARY_PATH_COMPONENT + "/" + canonicalId + SCRIPT_LIBRARY_CONTENT_PATH).toString());
        return Response.ok().entity(scriptLibraryResponse).build();
    }

    @Override
    public Response getScriptLibraryContentById(String scriptLibraryId) {

        return Response.ok().entity(serverScriptLibrariesService.getScriptLibraryContent(
                ScriptLibraryIdUtil.decode(scriptLibraryId))).build();
    }

    @Override
    public Response updateScriptLibrary(String scriptLibraryId, InputStream contentInputStream,
                                        Attachment contentDetail, String description) {

        serverScriptLibrariesService.updateScriptLibrary(ScriptLibraryIdUtil.decode(scriptLibraryId),
                contentInputStream, description);
        return Response.ok().build();
    }
}
