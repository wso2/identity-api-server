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

package org.wso2.carbon.identity.api.server.fetch.remote.v1;

import org.wso2.carbon.identity.api.server.fetch.remote.v1.*;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.Error;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationGetResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationListResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPatchRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListResponse;
import javax.ws.rs.core.Response;


public interface RemoteFetchApiService {

      public Response addRemoteFetch(RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest);

      public Response deleteRemoteFetch(String id);

      public Response getRemoteFetch(String id);

      public Response getRemoteFetchConfigs();

      public Response getStatus(String id);

      public Response handleWebHook(PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest);

      public Response triggerRemoteFetch(String id);

      public Response updateRemoteFetch(String id, RemoteFetchConfigurationPatchRequest remoteFetchConfigurationPatchRequest);
}
