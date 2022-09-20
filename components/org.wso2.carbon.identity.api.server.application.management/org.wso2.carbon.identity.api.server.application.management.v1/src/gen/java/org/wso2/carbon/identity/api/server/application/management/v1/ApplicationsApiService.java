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

package org.wso2.carbon.identity.api.server.application.management.v1;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.wso2.carbon.identity.api.server.application.management.v1.*;
import org.wso2.carbon.identity.api.server.application.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import org.wso2.carbon.identity.api.server.application.management.v1.AdaptiveAuthTemplates;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplatesList;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustMetaData;
import javax.ws.rs.core.Response;


public interface ApplicationsApiService {

      public Response changeApplicationOwner(String applicationId, ApplicationOwner applicationOwner);

      public Response createApplication(ApplicationModel applicationModel, String template);

      public Response createApplicationTemplate(ApplicationTemplateModel applicationTemplateModel);

      public Response deleteApplication(String applicationId);

      public Response deleteApplicationTemplate(String templateId);

      public Response deleteCustomInboundConfiguration(String applicationId, String inboundProtocolId);

      public Response deleteInboundOAuthConfiguration(String applicationId);

      public Response deleteInboundSAMLConfiguration(String applicationId);

      public Response deletePassiveStsConfiguration(String applicationId);

      public Response deleteWSTrustConfiguration(String applicationId);

      public Response exportApplication(String applicationId, Boolean exportSecrets);

      public Response getAdaptiveAuthTemplates();

      public Response getAllApplicationTemplates(Integer limit, Integer offset, SearchContext searchContext);

      public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy, String attributes);

      public Response getApplication(String applicationId);

      public Response getConfiguredAuthenticators(String applicationId);

      public Response getApplicationTemplate(String templateId);

      public Response getCustomInboundConfiguration(String applicationId, String inboundProtocolId);

      public Response getCustomProtocolMetadata(String inboundProtocolId);

      public Response getInboundAuthenticationConfigurations(String applicationId);

      public Response getInboundOAuthConfiguration(String applicationId);

      public Response getInboundProtocols(Boolean customOnly);

      public Response getInboundSAMLConfiguration(String applicationId);

      public Response getOIDCMetadata();

      public Response getPassiveStsConfiguration(String applicationId);

      public Response getResidentApplication();

      public Response getSAMLMetadata();

      public Response getWSTrustConfiguration(String applicationId);

      public Response getWSTrustMetadata();

      public Response importApplication(InputStream fileInputStream, Attachment fileDetail);

      public Response importApplicationForUpdate(InputStream fileInputStream, Attachment fileDetail);

      public Response patchApplication(String applicationId, ApplicationPatchModel applicationPatchModel);

      public Response regenerateOAuthClientSecret(String applicationId);

      public Response revokeOAuthClient(String applicationId);

      public Response updateApplicationTemplate(String templateId, ApplicationTemplateModel applicationTemplateModel);

      public Response updateCustomInboundConfiguration(String applicationId, String inboundProtocolId, CustomInboundProtocolConfiguration customInboundProtocolConfiguration);

      public Response updateInboundOAuthConfiguration(String applicationId, OpenIDConnectConfiguration openIDConnectConfiguration);

      public Response updateInboundSAMLConfiguration(String applicationId, SAML2Configuration saML2Configuration);

      public Response updatePassiveStsConfiguration(String applicationId, PassiveStsConfiguration passiveStsConfiguration);

      public Response updateResidentApplication(ProvisioningConfiguration provisioningConfiguration);

      public Response updateWSTrustConfiguration(String applicationId, WSTrustConfiguration wsTrustConfiguration);
}
