/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.role.management.v1;

import org.wso2.carbon.identity.api.server.organization.role.management.v1.*;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleGetResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleObj;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePatchRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePatchResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolesListResponse;
import javax.ws.rs.core.Response;


public interface OrganizationsApiService {

      public Response createRole(String organizationId, RolePostRequest rolePostRequest);

      public Response organizationsOrganizationIdRolesGet(String organizationId, String filter, Integer count, String cursor);

      public Response organizationsOrganizationIdRolesRoleIdDelete(String roleId, String organizationId);

      public Response organizationsOrganizationIdRolesRoleIdGet(String roleId, String organizationId);

      public Response organizationsOrganizationIdRolesRoleIdPatch(String roleId, String organizationId, RolePatchRequest rolePatchRequest);

      public Response organizationsOrganizationIdRolesRoleIdPut(String roleId, String organizationId, RolePutRequest rolePutRequest);

      public Response organizationsOrganizationIdUsersUserIdRolesGet(String userId, String organizationId);
}
