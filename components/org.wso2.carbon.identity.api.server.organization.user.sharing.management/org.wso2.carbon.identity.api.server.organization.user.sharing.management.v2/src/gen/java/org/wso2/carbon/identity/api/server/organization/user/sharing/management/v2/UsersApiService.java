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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2;

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.*;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareSelectedRequestBody;
import javax.ws.rs.core.Response;


public interface UsersApiService {

      public Response getUserSharedOrganizations(String userId, String before, String after, String filter, Integer limit, Boolean recursive, String attributes);

      public Response patchUserSharing(UserSharingPatchRequest userSharingPatchRequest);

      public Response shareUsersWithAll(UserShareAllRequestBody userShareAllRequestBody);

      public Response shareUsersWithSelected(UserShareSelectedRequestBody userShareSelectedRequestBody);

      public Response unshareUsersFromAll(UserUnshareAllRequestBody userUnshareAllRequestBody);

      public Response unshareUsersFromSelected(UserUnshareSelectedRequestBody userUnshareSelectedRequestBody);
}
