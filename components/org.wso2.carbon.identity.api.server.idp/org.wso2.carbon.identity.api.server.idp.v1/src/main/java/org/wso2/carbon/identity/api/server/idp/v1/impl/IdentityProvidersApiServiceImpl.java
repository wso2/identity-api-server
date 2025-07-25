/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.FileContent;
import org.wso2.carbon.identity.api.server.idp.v1.IdentityProvidersApiService;
import org.wso2.carbon.identity.api.server.idp.v1.core.ServerIdpManagementService;
import org.wso2.carbon.identity.api.server.idp.v1.factories.ServerIdpManagementServiceFactory;
import org.wso2.carbon.identity.api.server.idp.v1.model.AssociationRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdPGroup;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplate;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_TEMPLATE_PATH_COMPONENT;

/**
 * Implementation of the Identity Provider Rest API.
 */
public class IdentityProvidersApiServiceImpl implements IdentityProvidersApiService {

    private static final Log log = LogFactory.getLog(IdentityProvidersApiServiceImpl.class);
    private final ServerIdpManagementService idpManagementService;

    public IdentityProvidersApiServiceImpl() {

        try {
            this.idpManagementService = ServerIdpManagementServiceFactory.getServerIdpManagementService();
            if (log.isDebugEnabled()) {
                log.debug("IdentityProvidersApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating ServerIdpManagementService.", e);
            throw new RuntimeException("Error occurred while initiating ServerIdpManagementService.", e);
        }
    }

    @Override
    public Response addIDP(IdentityProviderPOSTRequest identityProviderPOSTRequest) {

        IdentityProviderResponse idPResponse = idpManagementService.addIDP(identityProviderPOSTRequest);
        URI location =
                ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + "/" + idPResponse.getId());
        return Response.created(location).entity(idPResponse).build();
    }

    @Override
    public Response addIDPTemplate(IdentityProviderTemplate identityProviderTemplatePOSTRequest) {

        String idpTemplateId =
                idpManagementService.createIDPTemplate(identityProviderTemplatePOSTRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                IDP_PATH_COMPONENT + IDP_TEMPLATE_PATH_COMPONENT + "/" + idpTemplateId);
        return Response.created(location).build();
    }

    @Override
    public Response deleteIDP(String identityProviderId, Boolean force) {

        if (force) {
            idpManagementService.forceDeleteIDP(identityProviderId);
        } else {
            idpManagementService.deleteIDP(identityProviderId);
        }

        return Response.noContent().build();
    }

    @Override
    public Response deleteIDPTemplate(String templateId) {

        idpManagementService.deleteIDPTemplate(templateId);
        return Response.noContent().build();
    }

    @Override
    public Response exportIDPToFile(String identityProviderId, Boolean excludeSecrets, String accept) {

        FileContent fileContent = idpManagementService.exportIDP(identityProviderId,
                excludeSecrets, accept);

        return Response.ok()
                .type(fileContent.getFileType())
                .header("Content-Disposition", "attachment; filename=\""
                        + fileContent.getFileName() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .entity(fileContent.getContent().getBytes(StandardCharsets.UTF_8))
                .build();
    }

    @Override
    public Response getClaimConfig(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getClaimConfig(identityProviderId)).build();
    }

    @Override
    public Response getConnectedApps(String identityProviderId, Integer limit, Integer offset) {

        return Response.ok().entity(idpManagementService.getConnectedApps(identityProviderId, limit, offset)).build();
    }

    @Override
    public Response getFederatedAuthenticator(String identityProviderId, String federatedAuthenticatorId) {

        return Response.ok().entity(idpManagementService.getFederatedAuthenticator(identityProviderId,
                federatedAuthenticatorId)).build();
    }

    @Override
    public Response getFederatedAuthenticators(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getFederatedAuthenticators(identityProviderId)).build();
    }

    @Override
    public Response getGroupConfig(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getGroupConfig(identityProviderId)).build();
    }

    @Override
    public Response getFederatedAssociationConfig(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getFederatedAssociationConfig(identityProviderId)).build();
    }

    @Override
    public Response getIDP(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getIDP(identityProviderId)).build();
    }

    @Override
    public Response getIDPTemplate(String templateId) {

        return Response.ok().entity(idpManagementService.getIDPTemplate(templateId)).build();
    }

    @Override
    public Response getIDPTemplates(Integer limit, Integer offset, SearchContext searchContext) {

        return Response.ok().entity(idpManagementService.getIDPTemplates(limit, offset, searchContext)).build();
    }

    @Override
    public Response getIDPs(Integer limit, Integer offset, String filter, String sortOrder, String sortBy,
                            String requiredAttributes) {

        return Response.ok().entity(idpManagementService.getIDPs(requiredAttributes, limit, offset, filter, sortBy,
                sortOrder)).build();
    }

    @Override
    public Response getJITConfig(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getJITConfig(identityProviderId)).build();
    }

    @Override
    public Response getMetaFederatedAuthenticator(String federatedAuthenticatorId) {

        return Response.ok().entity(idpManagementService.getMetaFederatedAuthenticator(federatedAuthenticatorId))
                .build();
    }

    @Override
    public Response getMetaFederatedAuthenticators() {

        return Response.ok().entity(idpManagementService.getMetaFederatedAuthenticators()).build();
    }

    @Override
    public Response getMetaOutboundConnector(String outboundProvisioningConnectorId) {

        return Response.ok().entity(idpManagementService.getMetaOutboundConnector(outboundProvisioningConnectorId))
                .build();
    }

    @Override
    public Response getMetaOutboundConnectors() {

        return Response.ok().entity(idpManagementService.getMetaOutboundConnectors()).build();
    }

    @Override
    public Response getOutboundConnector(String identityProviderId, String outboundProvisioningConnectorId) {

        return Response.ok().entity(idpManagementService.getOutboundConnector(identityProviderId,
                outboundProvisioningConnectorId)).build();
    }

    @Override
    public Response getOutboundConnectors(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getOutboundConnectors(identityProviderId)).build();
    }

    @Override
    public Response getProvisioningConfig(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getProvisioningConfig(identityProviderId)).build();
    }

    @Override
    public Response getRoleConfig(String identityProviderId) {

        return Response.ok().entity(idpManagementService.getRoleConfig(identityProviderId)).build();
    }

    @Override
    public Response importIDPFromFile(InputStream fileInputStream, Attachment fileDetail) {

        String resourceId = idpManagementService.importIDP(fileInputStream, fileDetail);
        URI location =
                ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + "/" + resourceId);
        return Response.created(location).build();
    }

    @Override
    public Response patchIDP(String identityProviderId, List<Patch> patchRequest) {

        return Response.ok().entity(idpManagementService.patchIDP(identityProviderId, patchRequest)).build();
    }

    @Override
    public Response updateClaimConfig(String identityProviderId, Claims claims) {

        return Response.ok().entity(idpManagementService.updateClaimConfig(identityProviderId, claims))
                .build();
    }

    @Override
    public Response updateFederatedAuthenticator(String identityProviderId, String federatedAuthenticatorId,
                                                 FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest) {

        return Response.ok().entity(idpManagementService.updateFederatedAuthenticator(identityProviderId,
                federatedAuthenticatorId, federatedAuthenticatorPUTRequest))
                .build();
    }

    @Override
    public Response updateFederatedAuthenticators(String identityProviderId,
                                                  FederatedAuthenticatorRequest federatedAuthenticatorRequest) {

        return Response.ok().entity(idpManagementService.updateFederatedAuthenticators(identityProviderId,
                federatedAuthenticatorRequest)).build();
    }

    @Override
    public Response updateIDPFromFile(String identityProviderId, InputStream fileInputStream, Attachment fileDetail) {

        idpManagementService.updateIDPFromFile(identityProviderId, fileInputStream, fileDetail);
        return Response.ok().build();
    }

    @Override
    public Response updateGroupConfig(String identityProviderId, List<IdPGroup> idPGroup) {

        return Response.ok().entity(idpManagementService.updateGroupConfig(identityProviderId, idPGroup)).build();
    }

    @Override
    public Response updateFederatedAssociationConfig(String identityProviderId, AssociationRequest associationRequest) {

        return Response.ok().entity(idpManagementService.updateFederatedAssociationConfig(identityProviderId,
                associationRequest)).build();
    }

    @Override
    public Response updateIDPTemplate(String templateId, IdentityProviderTemplate
            identityProviderTemplatePOSTRequest) {

        idpManagementService.updateIDPTemplate(templateId, identityProviderTemplatePOSTRequest);
        return Response.ok().build();
    }

    @Override
    public Response updateJITConfig(String identityProviderId, JustInTimeProvisioning justInTimeProvisioning) {

        return Response.ok().entity(idpManagementService.updateJITConfig(identityProviderId,
                justInTimeProvisioning)).build();
    }

    @Override
    public Response updateOutboundConnector(String identityProviderId, String outboundProvisioningConnectorId,
                                            OutboundConnectorPUTRequest outboundConnectorPUTRequest) {

        return Response.ok().entity(idpManagementService.updateOutboundConnector(identityProviderId,
                outboundProvisioningConnectorId, outboundConnectorPUTRequest))
                .build();
    }

    @Override
    public Response updateOutboundConnectors(String identityProviderId,
                                             OutboundProvisioningRequest outboundProvisioningRequest) {

        return Response.ok().entity(idpManagementService.updateOutboundConnectors(identityProviderId,
                outboundProvisioningRequest)).build();
    }

    @Override
    public Response updateRoleConfig(String identityProviderId, Roles roles) {

        return Response.ok().entity(idpManagementService.updateRoleConfig(identityProviderId, roles))
                .build();
    }
}
