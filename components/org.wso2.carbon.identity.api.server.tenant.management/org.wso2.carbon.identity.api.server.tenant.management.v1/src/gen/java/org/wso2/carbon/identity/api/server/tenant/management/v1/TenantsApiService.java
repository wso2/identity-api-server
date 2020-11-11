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

package org.wso2.carbon.identity.api.server.tenant.management.v1;

import org.wso2.carbon.identity.api.server.tenant.management.v1.*;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerResponse;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantResponseModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantsListResponse;
import javax.ws.rs.core.Response;


public interface TenantsApiService {

      public Response addTenant(TenantModel tenantModel);

      public Response deleteTenantMetadata(String tenantId);

      public Response getOwners(String tenantId);

      public Response getTenant(String tenantId);

      public Response getTenantByDomain(String tenantDomain);

      public Response isDomainExist(String tenantDomain);

      public Response retrieveTenants(Integer limit, Integer offset, String sortOrder, String sortBy, String filter);

      public Response updateTenantStatus(String tenantId, TenantPutModel tenantPutModel);
}
