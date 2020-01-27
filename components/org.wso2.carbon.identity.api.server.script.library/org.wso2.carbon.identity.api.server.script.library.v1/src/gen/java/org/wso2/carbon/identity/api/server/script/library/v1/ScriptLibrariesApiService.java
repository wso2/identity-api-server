/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.script.library.v1;

import org.wso2.carbon.identity.api.server.script.library.v1.*;
import org.wso2.carbon.identity.api.server.script.library.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import org.wso2.carbon.identity.api.server.script.library.v1.model.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryListResponse;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryResponse;
import javax.ws.rs.core.Response;


public interface ScriptLibrariesApiService {

      public Response addScriptLibrary(String name, InputStream contentInputStream, Attachment contentDetail, String description);

      public Response deleteScriptLibrary(String scriptLibraryName);

      public Response getScriptLibraries(Integer limit, Integer offset);

      public Response getScriptLibraryByName(String scriptLibraryName);

      public Response getScriptLibraryContentByName(String scriptLibraryName);

      public Response updateScriptLibrary(String scriptLibraryName, InputStream contentInputStream, Attachment contentDetail, String description);
}
