/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.wso2.carbon.identity.api.server.application.management.v1.*;
import org.wso2.carbon.identity.api.server.application.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import javax.ws.rs.core.Response;


public interface ApplicationsApiService {

      public Response createApplication(ApplicationModel applicationModel, String template);

      public Response deleteAdvancedConfigurations(String applicationId);

      public Response deleteApplication(String applicationId);

      public Response deleteAuthenticationSequence(String applicationId);

      public Response deleteClaimConfiguration(String applicationId);

      public Response deleteCustomInboundConfiguration(String applicationId, String inboundProtocolId);

      public Response deleteInboundAuthenticationConfigurations(String applicationId);

      public Response deleteInboundOAuthConfiguration(String applicationId);

      public Response deleteInboundSAMLConfiguration(String applicationId);

      public Response deletePassiveStsConfiguration(String applicationId);

      public Response deleteProvisioningConfiguration(String applicationId);

      public Response deleteWSTrustConfiguration(String applicationId);

      public Response getAdvancedConfigurations(String applicationId);

      public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy, String attributes);

      public Response getApplication(String applicationId);

      public Response getAuthenticationSequence(String applicationId);

      public Response getClaimConfiguration(String applicationId);

      public Response getCustomInboundConfiguration(String applicationId, String inboundProtocolId);

      public Response getInboundAuthenticationConfigurations(String applicationId);

      public Response getInboundOAuthConfiguration(String applicationId);

      public Response getInboundSAMLConfiguration(String applicationId);

      public Response getPassiveStsConfiguration(String applicationId);

      public Response getProvisioningConfiguration(String applicationId);

      public Response getResidentApplication();

      public Response getWSTrustConfiguration(String applicationId);

      public Response regenerateOAuthApplicationSecret(String applicationId);

      public Response revokeOAuthApplication(String applicationId);

      public Response updateAdvancedConfigurations(String applicationId, AdvancedApplicationConfiguration advancedApplicationConfiguration);

      public Response updateApplication(String applicationId, ApplicationModel applicationModel);

      public Response updateAuthenticationSequence(String applicationId, AuthenticationSequence authenticationSequence);

      public Response updateClaimConfiguration(String applicationId, ClaimConfiguration claimConfiguration);

      public Response updateCustomInboundConfiguration(String applicationId, String inboundProtocolId, CustomInboundProtocolConfiguration customInboundProtocolConfiguration);

      public Response updateInboundAuthenticationConfigurations(String applicationId, InboundProtocols inboundProtocols);

      public Response updateInboundOAuthConfiguration(String applicationId, OpenIDConnectConfiguration openIDConnectConfiguration);

      public Response updateInboundSAMLConfiguration(String applicationId, SAML2Configuration saML2Configuration);

      public Response updatePassiveStsConfiguration(String applicationId, PassiveStsConfiguration passiveStsConfiguration);

      public Response updateProvisioningConfiguration(String applicationId, ProvisioningConfiguration provisioningConfiguration);

      public Response updateResidentApplication(ProvisioningConfiguration provisioningConfiguration);

      public Response updateWSTrustConfiguration(String applicationId, WSTrustConfiguration wsTrustConfiguration);
}
