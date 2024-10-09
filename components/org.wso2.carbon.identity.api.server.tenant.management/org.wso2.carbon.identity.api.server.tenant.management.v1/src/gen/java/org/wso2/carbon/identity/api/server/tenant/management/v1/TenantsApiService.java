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

package org.wso2.carbon.identity.api.server.tenant.management.v1;

import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantPutModel;

import javax.ws.rs.core.Response;


public interface TenantsApiService {

      public Response addTenant(TenantModel tenantModel);

      public Response deleteTenantMetadata(String tenantId);

      public Response getOwner(String tenantId, String ownerId);

      public Response getOwners(String tenantId);

      public Response getTenant(String tenantId);

      public Response getTenantByDomain(String tenantDomain);

      public Response isDomainExist(String tenantDomain);

      public Response retrieveTenants(Integer limit, Integer offset, String sortOrder, String sortBy, String filter);

      public Response updateOwner(String tenantId, String ownerId, OwnerPutModel ownerPutModel);

      public Response updateTenantStatus(String tenantId, TenantPutModel tenantPutModel);
}
