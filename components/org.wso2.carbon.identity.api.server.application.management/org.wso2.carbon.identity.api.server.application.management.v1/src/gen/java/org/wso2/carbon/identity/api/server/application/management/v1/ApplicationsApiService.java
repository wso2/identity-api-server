/*
 * Copyright (c) 2024-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.wso2.carbon.identity.api.server.application.management.v1.*;
import org.wso2.carbon.identity.api.server.application.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.AdaptiveAuthTemplates;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationOwner;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharingPatchRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplatesList;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPICreationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPIPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPIResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ConfiguredAuthenticatorsModal;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.application.management.v1.GroupBasicInfo;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowResultResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowStatusResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.ScriptUpdateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustMetaData;
import javax.ws.rs.core.Response;


public interface ApplicationsApiService {

      public Response addAuthorizedAPI(String applicationId, AuthorizedAPICreationModel authorizedAPICreationModel);

      public Response changeApplicationOwner(String applicationId, ApplicationOwner applicationOwner);

      public Response createApplication(ApplicationModel applicationModel, String template);

      public Response createApplicationTemplate(ApplicationTemplateModel applicationTemplateModel);

      public Response deleteApplication(String applicationId);

      public Response deleteApplicationTemplate(String templateId);

      public Response deleteAuthorizedAPI(String applicationId, String apiId);

      public Response deleteCustomInboundConfiguration(String applicationId, String inboundProtocolId);

      public Response deleteInboundOAuthConfiguration(String applicationId);

      public Response deleteInboundSAMLConfiguration(String applicationId);

      public Response deletePassiveStsConfiguration(String applicationId);

      public Response deleteWSTrustConfiguration(String applicationId);

      public Response exportApplication(String applicationId, Boolean exportSecrets);

      public Response exportApplicationAsFile(String applicationId, Boolean exportSecrets, String accept);

      public Response generateLoginFlow(LoginFlowGenerateRequest loginFlowGenerateRequest);

      public Response getAdaptiveAuthTemplates();

      public Response getAllApplicationTemplates(Integer limit, Integer offset, SearchContext searchContext);

      public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy, String attributes);

      public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy, String attributes, Boolean excludeSystemPortals);

      public Response getApplication(String applicationId);

      public Response getApplicationTemplate(String templateId);

      public Response getAuthorizedAPIs(String applicationId);

      public Response getConfiguredAuthenticators(String applicationId);

      public Response getCustomInboundConfiguration(String applicationId, String inboundProtocolId);

      public Response getCustomProtocolMetadata(String inboundProtocolId);

      public Response getGroups(String domain, String filter);

      public Response getInboundAuthenticationConfigurations(String applicationId);

      public Response getInboundOAuthConfiguration(String applicationId);

      public Response getInboundProtocols(Boolean customOnly);

      public Response getInboundSAMLConfiguration(String applicationId);

      public Response getLoginFlowGenerationResult(String operationId);

      public Response getLoginFlowGenerationStatus(String operationId);

      public Response getOIDCMetadata();

      public Response getPassiveStsConfiguration(String applicationId);

      public Response getResidentApplication();

      public Response getSAMLMetadata();

      public Response getWSTrustConfiguration(String applicationId);

      public Response getWSTrustMetadata();

      public Response importApplication(InputStream fileInputStream, Attachment fileDetail);

      public Response importApplicationForUpdate(InputStream fileInputStream, Attachment fileDetail);

      public Response patchApplication(String applicationId, ApplicationPatchModel applicationPatchModel);

      public Response patchApplicationSharing(ApplicationSharingPatchRequest applicationSharingPatchRequest);

      public Response patchAuthorizedAPI(String applicationId, String apiId, AuthorizedAPIPatchModel authorizedAPIPatchModel);

      public Response regenerateOAuthClientSecret(String applicationId);

      public Response revokeOAuthClient(String applicationId);

      public Response shareApplicationWithAll(ApplicationShareAllRequestBody applicationShareAllRequestBody);

      public Response shareApplicationWithSelected(ApplicationShareSelectedRequestBody applicationShareSelectedRequestBody);

      public Response shareOrgApplication(String applicationId, ApplicationSharePOSTRequest applicationSharePOSTRequest);

      public Response shareOrgApplicationDelete(String applicationId, String sharedOrganizationId);

      public Response shareOrgApplicationGet(String applicationId, String before, String after, String filter, Integer limit, Boolean recursive, String excludedAttributes, String attributes);

      public Response sharedApplicationsAllDelete(String applicationId);

      public Response sharedApplicationsGet(String applicationId);

      public Response unshareApplicationFromAll(ApplicationUnshareAllRequestBody applicationUnshareAllRequestBody);

      public Response unshareApplicationFromSelected(ApplicationUnshareSelectedRequestBody applicationUnshareSelectedRequestBody);

      public Response updateAuthenticationScript(String applicationId, ScriptUpdateModel scriptUpdateModel);

      public Response updateApplicationTemplate(String templateId, ApplicationTemplateModel applicationTemplateModel);

      public Response updateCustomInboundConfiguration(String applicationId, String inboundProtocolId, CustomInboundProtocolConfiguration customInboundProtocolConfiguration);

      public Response updateInboundOAuthConfiguration(String applicationId, OpenIDConnectConfiguration openIDConnectConfiguration);

      public Response updateInboundSAMLConfiguration(String applicationId, SAML2Configuration saML2Configuration);

      public Response updatePassiveStsConfiguration(String applicationId, PassiveStsConfiguration passiveStsConfiguration);

      public Response updateResidentApplication(ProvisioningConfiguration provisioningConfiguration);

      public Response updateWSTrustConfiguration(String applicationId, WSTrustConfiguration wsTrustConfiguration);
}
