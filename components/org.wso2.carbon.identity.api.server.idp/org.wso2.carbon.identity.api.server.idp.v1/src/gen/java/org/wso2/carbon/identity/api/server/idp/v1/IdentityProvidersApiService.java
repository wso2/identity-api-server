package org.wso2.carbon.identity.api.server.idp.v1;

import org.wso2.carbon.identity.api.server.idp.v1.*;
import org.wso2.carbon.identity.api.server.idp.v1.model.*;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.Error;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderGetResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface IdentityProvidersApiService {
      public Response addIDP(IdentityProviderPOSTRequest identityProviderPOSTRequest );
      public Response deleteIDP(String identityProviderId,  Boolean force );
      public Response getClaimConfig(String identityProviderId );
      public Response getConnectedApps(String identityProviderId );
      public Response getFederatedAuthenticator(String identityProviderId,  String federatedAuthenticatorId );
      public Response getFederatedAuthenticators(String identityProviderId );
      public Response getIDP(String identityProviderId );
      public Response getIDPs(Integer limit,  Integer offset,  String filter,  String sortOrder,  String sortBy,  String attributes );
      public Response getJITConfig(String identityProviderId );
      public Response getMetaFederatedAuthenticator(String federatedAuthenticatorId );
      public Response getMetaFederatedAuthenticators();
      public Response getMetaOutboundConnector(String outboundProvisioningConnectorId );
      public Response getMetaOutboundConnectors();
      public Response getOutboundConnector(String identityProviderId,  String outboundProvisioningConnectorId );
      public Response getOutboundConnectors(String identityProviderId );
      public Response getProvisioningConfig(String identityProviderId );
      public Response getRoleConfig(String identityProviderId );
      public Response patchIDP(String identityProviderId,  List<PatchDocument> patchDocument );
      public Response updateClaimConfig(String identityProviderId,  Claims claims );
      public Response updateFederatedAuthenticator(String identityProviderId,  String federatedAuthenticatorId,  FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest );
      public Response updateIDP(String identityProviderId,  IdentityProviderPUTRequest identityProviderPUTRequest );
      public Response updateJITConfig(String identityProviderId,  JustInTimeProvisioning justInTimeProvisioning );
      public Response updateOutboundConnector(String identityProviderId,  String outboundProvisioningConnectorId,  OutboundConnectorPUTRequest outboundConnectorPUTRequest );
      public Response updateRoleConfig(String identityProviderId,  Roles roles );
}
