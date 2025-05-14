/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.management.v1;

import org.wso2.carbon.identity.api.server.organization.management.v1.*;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.GetOrganizationResponse;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.MetaAttributesResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationCheckResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryAttributes;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryCheckPOSTResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryPostRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationHandleCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationMetadata;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPUTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPatchRequestItem;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationsDiscoveryResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedOrganizationsResponse;
import javax.ws.rs.core.Response;


public interface OrganizationsApiService {

      public Response organizationCheckDiscovery(OrganizationDiscoveryCheckPOSTRequest organizationDiscoveryCheckPOSTRequest);

      public Response organizationDiscoveryGet(String organizationId);

      public Response organizationDiscoveryPost(OrganizationDiscoveryPostRequest organizationDiscoveryPostRequest);

      public Response organizationMetadataGet();

      public Response organizationPost(OrganizationPOSTRequest organizationPOSTRequest);

      public Response organizationsCheckHandlePost(OrganizationHandleCheckPOSTRequest organizationHandleCheckPOSTRequest);

      public Response organizationsCheckNamePost(OrganizationNameCheckPOSTRequest organizationNameCheckPOSTRequest);

      public Response organizationsDiscoveryGet(String filter, Integer offset, Integer limit);

      public Response organizationsGet(String filter, Integer limit, String after, String before, Boolean recursive);

      public Response organizationsMetaAttributesGet(String filter, Integer limit, String after, String before, Boolean recursive);

      public Response organizationsOrganizationIdDelete(String organizationId);

      public Response organizationsOrganizationIdDiscoveryDelete(String organizationId);

      public Response organizationsOrganizationIdDiscoveryPut(String organizationId, OrganizationDiscoveryAttributes organizationDiscoveryAttributes);

      public Response organizationsOrganizationIdGet(String organizationId, Boolean includePermissions);

      public Response organizationsOrganizationIdPatch(String organizationId, List<OrganizationPatchRequestItem> organizationPatchRequestItem);

      public Response organizationsOrganizationIdPut(String organizationId, OrganizationPUTRequest organizationPUTRequest);

      public Response shareOrgApplication(String organizationId, String applicationId, ApplicationSharePOSTRequest applicationSharePOSTRequest);

      public Response shareOrgApplicationDelete(String organizationId, String applicationId, String sharedOrganizationId);

      public Response shareOrgApplicationDeleteAll(String organizationId, String applicationId);

      public Response shareOrgApplicationGet(String organizationId, String applicationId);

      public Response sharedApplicationsAllDelete(String organizationId, String applicationId);

      public Response sharedApplicationsGet(String organizationId, String applicationId);
}
