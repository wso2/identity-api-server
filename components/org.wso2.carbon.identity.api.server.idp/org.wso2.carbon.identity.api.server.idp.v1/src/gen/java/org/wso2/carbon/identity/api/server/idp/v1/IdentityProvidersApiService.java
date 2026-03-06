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

package org.wso2.carbon.identity.api.server.idp.v1;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.wso2.carbon.identity.api.server.idp.v1.*;
import org.wso2.carbon.identity.api.server.idp.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.AssociationRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.AssociationResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.idp.v1.model.Error;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdPGroup;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplate;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplateListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.ws.rs.core.Response;


public interface IdentityProvidersApiService {

      public Response addIDP(IdentityProviderPOSTRequest identityProviderPOSTRequest);

      public Response addIDPTemplate(IdentityProviderTemplate identityProviderTemplate);

      public Response deleteIDP(String identityProviderId, Boolean force);

      public Response deleteIDPTemplate(String templateId);

      public Response exportIDPToFile(String identityProviderId, Boolean excludeSecrets, String accept);

      public Response getClaimConfig(String identityProviderId);

      public Response getConnectedApps(String identityProviderId, Integer limit, Integer offset, String filter);

      public Response getFederatedAssociationConfig(String identityProviderId);

      public Response getFederatedAuthenticator(String identityProviderId, String federatedAuthenticatorId);

      public Response getFederatedAuthenticators(String identityProviderId);

      public Response getGroupConfig(String identityProviderId);

      public Response getIDP(String identityProviderId);

      public Response getIDPTemplate(String templateId);

      public Response getIDPTemplates(Integer limit, Integer offset, SearchContext searchContext);

      public Response getIDPs(Integer limit, Integer offset, String filter, String sortOrder, String sortBy, String requiredAttributes);

      public Response getJITConfig(String identityProviderId);

      public Response getMetaFederatedAuthenticator(String federatedAuthenticatorId);

      public Response getMetaFederatedAuthenticators();

      public Response getMetaOutboundConnector(String outboundProvisioningConnectorId);

      public Response getMetaOutboundConnectors();

      public Response getOutboundConnector(String identityProviderId, String outboundProvisioningConnectorId);

      public Response getOutboundConnectors(String identityProviderId);

      public Response getProvisioningConfig(String identityProviderId);

      public Response getRoleConfig(String identityProviderId);

      public Response importIDPFromFile(InputStream fileInputStream, Attachment fileDetail);

      public Response patchIDP(String identityProviderId, List<Patch> patch);

      public Response updateClaimConfig(String identityProviderId, Claims claims);

      public Response updateFederatedAssociationConfig(String identityProviderId, AssociationRequest associationRequest);

      public Response updateFederatedAuthenticator(String identityProviderId, String federatedAuthenticatorId, FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest);

      public Response updateFederatedAuthenticators(String identityProviderId, FederatedAuthenticatorRequest federatedAuthenticatorRequest);

      public Response updateGroupConfig(String identityProviderId, List<IdPGroup> idPGroup);

      public Response updateIDPFromFile(String identityProviderId, InputStream fileInputStream, Attachment fileDetail);

      public Response updateIDPTemplate(String templateId, IdentityProviderTemplate identityProviderTemplate);

      public Response updateJITConfig(String identityProviderId, JustInTimeProvisioning justInTimeProvisioning);

      public Response updateOutboundConnector(String identityProviderId, String outboundProvisioningConnectorId, OutboundConnectorPUTRequest outboundConnectorPUTRequest);

      public Response updateOutboundConnectors(String identityProviderId, OutboundProvisioningRequest outboundProvisioningRequest);

      public Response updateRoleConfig(String identityProviderId, Roles roles);
}
