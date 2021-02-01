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

package org.wso2.carbon.identity.api.server.userstore.v1;

import org.wso2.carbon.identity.api.server.userstore.v1.*;
import org.wso2.carbon.identity.api.server.userstore.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.AvailableUserStoreClassesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ConnectionEstablishedResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.MetaUserStoreType;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.userstore.v1.model.RDBMSConnectionReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreConfigurationsRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreListResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreResponse;
import javax.ws.rs.core.Response;


public interface UserstoresApiService {

      public Response addUserStore(UserStoreReq userStoreReq);

      public Response deleteUserStore(String userstoreDomainId);

      public Response getAvailableUserStoreTypes();

      public Response getPrimaryUserStore();

      public Response getSecondaryUserStores(Integer limit, Integer offset, String filter, String sort, String requiredAttributes);

      public Response getUserStoreByDomainId(String userstoreDomainId);

      public Response getUserStoreManagerProperties(String typeId);

      public Response patchUserStore(String userstoreDomainId, List<PatchDocument> patchDocument);

      public Response testRDBMSConnection(RDBMSConnectionReq rdBMSConnectionReq);

      public Response updateUserStore(String userstoreDomainId, UserStoreReq userStoreReq);
}
