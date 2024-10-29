/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1;

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.*;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareWithAllRequestBody;
import javax.ws.rs.core.Response;


public interface UsersApiService {

      public Response processUserSharing(UserShareRequestBody userShareRequestBody);

      public Response processUserSharingAll(UserShareWithAllRequestBody userShareWithAllRequestBody);

      public Response processUserUnsharing(UserUnshareRequestBody userUnshareRequestBody);

      public Response removeUserSharing(UserUnshareWithAllRequestBody userUnshareWithAllRequestBody);

      public Response usersUserIdSharedOrganizationsGet(String userId, String after, String before, Integer limit, String filter, Boolean recursive);

      public Response usersUserIdSharedRolesGet(String userId, String orgId, String after, String before, Integer limit, String filter, Boolean recursive);
}
