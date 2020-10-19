/*
 * Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.idp.v1.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.search.ConditionType;
import org.apache.cxf.jaxrs.ext.search.PrimitiveStatement;
import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.idp.common.Constants;
import org.wso2.carbon.identity.api.server.idp.common.IdentityProviderServiceHolder;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claim;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.ConnectedApp;
import org.wso2.carbon.identity.api.server.idp.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplate;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplateListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplateListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import org.wso2.carbon.identity.api.server.idp.v1.model.Link;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningClaim;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import org.wso2.carbon.identity.application.common.model.CertificateInfo;
import org.wso2.carbon.identity.application.common.model.ClaimConfig;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.IdentityProviderProperty;
import org.wso2.carbon.identity.application.common.model.JustInTimeProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.LocalRole;
import org.wso2.carbon.identity.application.common.model.PermissionsAndRoleConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ProvisioningConnectorConfig;
import org.wso2.carbon.identity.application.common.model.RoleMapping;
import org.wso2.carbon.identity.application.common.model.SubProperty;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.LocalClaim;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceSearchBean;
import org.wso2.carbon.identity.configuration.mgt.core.search.ComplexCondition;
import org.wso2.carbon.identity.configuration.mgt.core.search.Condition;
import org.wso2.carbon.identity.configuration.mgt.core.search.PrimitiveCondition;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.identity.template.mgt.TemplateMgtConstants;
import org.wso2.carbon.identity.template.mgt.exception.TemplateManagementClientException;
import org.wso2.carbon.identity.template.mgt.exception.TemplateManagementException;
import org.wso2.carbon.identity.template.mgt.exception.TemplateManagementServerException;
import org.wso2.carbon.identity.template.mgt.model.Template;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementServerException;
import org.wso2.carbon.idp.mgt.model.ConnectedAppsResult;
import org.wso2.carbon.idp.mgt.model.IdpSearchResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_TEMPLATE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.PROP_CATEGORY;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.PROP_DISPLAY_ORDER;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.PROP_SERVICES;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.SERV_AUTHENTICATION;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.SERV_PROVISIONING;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.TEMPLATE_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.PrimitiveOperator.EQUALS;

/**
 * Call internal osgi services to perform server identity provider related operations.
 */
public class ServerIdpManagementService {

    private static final Log log = LogFactory.getLog(ServerIdpManagementService.class);

    /**
     * Get list of identity providers.
     *
     * @param requiredAttributes Required attributes in the IDP list response.
     * @param limit      Items per page.
     * @param offset     Offset.
     * @param filter     Filter string. E.g. filter="name" sw "google" and "isEnabled" eq "true"
     * @param sortBy     Attribute to sort the IDPs by. E.g. name
     * @param sortOrder  Order in which IDPs should be sorted. Can be either ASC or DESC.
     * @return IdentityProviderListResponse.
     */
    public IdentityProviderListResponse getIDPs(String requiredAttributes, Integer limit, Integer offset, String filter,
                                                String sortBy, String sortOrder) {

        try {
            List<String> requestedAttributeList = null;
            if (StringUtils.isNotBlank(requiredAttributes)) {
                requestedAttributeList = new ArrayList<>(Arrays.asList(requiredAttributes.split(",")));
            }
            return createIDPListResponse(
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPs(limit, offset, filter,
                            sortBy, sortOrder, ContextLoader.getTenantDomainFromContext(), requestedAttributeList),
                    requestedAttributeList);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDPS, null);
        }
    }

    /**
     * Add an identity provider.
     *
     * @param identityProviderPOSTRequest identityProviderPOSTRequest.
     * @return IdentityProviderResponse.
     */
    public IdentityProviderResponse addIDP(IdentityProviderPOSTRequest identityProviderPOSTRequest) {

        IdentityProvider identityProvider;
        try {
            identityProvider = IdentityProviderServiceHolder.getIdentityProviderManager().addIdPWithResourceId(
                    createIDP(identityProviderPOSTRequest), ContextLoader.getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP, null);
        }
        return createIDPResponse(identityProvider);
    }

    /**
     * Get an identity provider identified by resource ID.
     *
     * @param idpId IdP resource ID.
     * @return IdentityProviderGetResponse.
     */
    public IdentityProviderResponse getIDP(String idpId) {

        try {
            IdentityProvider identityProvider =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId,
                            ContextLoader.getTenantDomainFromContext(), true);
            if (identityProvider == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            return createIDPResponse(identityProvider);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP, idpId);
        }
    }

    /**
     * Updates only root level attributes of IDP.
     *
     * @param identityProviderId Identity Provider resource ID.
     * @param patchRequest       Patch request in Json Patch notation See
     *                           <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf
     *                           .org/html/rfc6902</a>.
     *                           We support only Patch 'replace' operation on root level attributes of an Identity
     *                           Provider.
     */
    public IdentityProviderResponse patchIDP(String identityProviderId, List<Patch> patchRequest) {

        try {
            IdentityProvider identityProvider =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(identityProviderId,
                            ContextLoader.getTenantDomainFromContext(), true);
            if (identityProvider == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        identityProviderId);
            }
            IdentityProvider idpToUpdate = createIdPClone(identityProvider);
            processPatchRequest(patchRequest, idpToUpdate);
            IdentityProvider updatedIdP = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .updateIdPByResourceId(identityProviderId, idpToUpdate,
                            ContextLoader.getTenantDomainFromContext());
            return createIDPResponse(updatedIdP);

        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP, identityProviderId);
        }
    }

    /**
     * Delete an Identity Provider.
     *
     * @param identityProviderId Identity Provider resource ID.
     */
    public void deleteIDP(String identityProviderId) {

        try {
            IdentityProviderServiceHolder.getIdentityProviderManager().deleteIdPByResourceId(identityProviderId,
                    ContextLoader.getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_IDP, identityProviderId);
        }
    }

    /**
     * Forcefully delete an Identity Provider. When force is set to true, IDP will be forcefully deleted even-though
     * it is being referred by service providers.
     *
     * @param identityProviderId Identity Provider resource ID.
     */
    public void forceDeleteIDP(String identityProviderId) {

        try {
            IdentityProviderServiceHolder.getIdentityProviderManager().forceDeleteIdpByResourceId(identityProviderId,
                    ContextLoader.getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_IDP, identityProviderId);
        }
    }

    /**
     * Get meta information about Identity Provider's federated authenticators.
     *
     * @return list of meta federated authenticators.
     */
    public List<MetaFederatedAuthenticatorListItem> getMetaFederatedAuthenticators() {

        List<MetaFederatedAuthenticatorListItem> metaAuthenticators = new ArrayList<>();
        try {
            FederatedAuthenticatorConfig[] authenticatorConfigs =
                    IdentityProviderServiceHolder.getIdentityProviderManager()
                            .getAllFederatedAuthenticators();
            if (ArrayUtils.isNotEmpty(authenticatorConfigs)) {
                for (FederatedAuthenticatorConfig authenticatorConfig : authenticatorConfigs) {
                    MetaFederatedAuthenticatorListItem metaFederatedAuthenticator =
                            createMetaFederatedAuthenticatorListItem(authenticatorConfig);
                    metaAuthenticators.add(metaFederatedAuthenticator);
                }
            }
            return metaAuthenticators;
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATORS, null);
        }
    }

    /**
     * Get meta information about a specific federated authenticator supported by the IDPs.
     *
     * @param id Federated authenticator ID.
     * @return MetaFederatedAuthenticator.
     */
    public MetaFederatedAuthenticator getMetaFederatedAuthenticator(String id) {

        MetaFederatedAuthenticator authenticator = null;
        try {
            String authenticatorName = decodeAuthenticatorID(id);
            FederatedAuthenticatorConfig[] authenticatorConfigs =
                    IdentityProviderServiceHolder.getIdentityProviderManager()
                            .getAllFederatedAuthenticators();
            if (ArrayUtils.isNotEmpty(authenticatorConfigs)) {
                for (FederatedAuthenticatorConfig authenticatorConfig : authenticatorConfigs) {
                    if (StringUtils.equals(authenticatorConfig.getName(), authenticatorName)) {
                        authenticator = createMetaFederatedAuthenticator(authenticatorConfig);
                        break;
                    }
                }
            }
            return authenticator;
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATOR, id);
        }
    }

    /**
     * Decode authenticator ID.
     *
     * @param id Authenticator ID.
     * @return Base64 URL decoded authenticator ID.
     * @throws IdentityProviderManagementClientException IF an error occurred while decoding the authenticator ID.
     */
    private String decodeAuthenticatorID(String id) throws IdentityProviderManagementClientException {

        try {
            return base64URLDecode(id);
        } catch (IllegalArgumentException e) {
            if (StringUtils.isBlank(e.getLocalizedMessage())) {
                throw new IdentityProviderManagementClientException("Invalid Authenticator ID: " + id, e);
            }
            throw new IdentityProviderManagementClientException(
                    String.format("%s : Authenticator ID: %s", e.getMessage(), id), e);
        }
    }

    /**
     * Get meta information about Identity Provider's outbound provisioning connectors.
     *
     * @return List of meta outbound provisioning connectors.
     */
    public List<MetaOutboundConnectorListItem> getMetaOutboundConnectors() {

        List<MetaOutboundConnectorListItem> metaOutboundConnectors = new ArrayList<>();
        try {
            ProvisioningConnectorConfig[] connectorConfigs = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .getAllProvisioningConnectors();
            if (ArrayUtils.isNotEmpty(connectorConfigs)) {
                for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
                    MetaOutboundConnectorListItem metaOutboundConnector = createMetaOutboundConnectorListItem
                            (connectorConfig);
                    metaOutboundConnectors.add(metaOutboundConnector);
                }
            }
            return metaOutboundConnectors;
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_CONNECTORS, null);
        }
    }

    /**
     * Get meta information about a specific outbound provisioning connector supported by the IDPs.
     *
     * @param id Outbound Provisioning Connector ID.
     * @return MetaOutboundConnector.
     */
    public MetaOutboundConnector getMetaOutboundConnector(String id) {

        String connectorName = base64URLDecode(id);
        MetaOutboundConnector connector = null;
        try {
            ProvisioningConnectorConfig[] connectorConfigs = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .getAllProvisioningConnectors();
            if (ArrayUtils.isNotEmpty(connectorConfigs)) {
                for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
                    if (StringUtils.equals(connectorConfig.getName(), connectorName)) {
                        connector = createMetaOutboundConnector(connectorConfig);
                        break;
                    }
                }
            }
            return connector;
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_CONNECTOR, id);
        }
    }

    /**
     * Returns configured federated authenticators of a specific identity provider.
     *
     * @param idpId Identity provider resource ID.
     * @return FederatedAuthenticatorListResponse Federated authenticator list.
     */
    public FederatedAuthenticatorListResponse getFederatedAuthenticators(String idpId) {

        FederatedAuthenticatorListResponse listResponse;

        try {
            IdentityProvider idP =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);

            if (idP == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            listResponse = new FederatedAuthenticatorListResponse();
            FederatedAuthenticatorConfig[] fedAuthConfigs = idP.getFederatedAuthenticatorConfigs();
            if (fedAuthConfigs != null) {
                List<FederatedAuthenticatorListItem> fedAuthList = new ArrayList<>();
                String defaultAuthenticator = null;
                for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
                    String fedAuthId = base64URLEncode(config.getName());
                    FederatedAuthenticatorListItem listItem = new FederatedAuthenticatorListItem();
                    listItem.setAuthenticatorId(fedAuthId);
                    listItem.setName(config.getName());
                    listItem.setIsEnabled(config.isEnabled());
                    listItem.setSelf(
                            ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                                    "/%s/federated-authenticators/%s", idpId, fedAuthId)).toString());
                    fedAuthList.add(listItem);
                    if (idP.getDefaultAuthenticatorConfig() != null) {
                        defaultAuthenticator = base64URLEncode(idP.getDefaultAuthenticatorConfig().getName());
                    }
                }
                listResponse.setDefaultAuthenticatorId(defaultAuthenticator);
                listResponse.setAuthenticators(fedAuthList);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATORS, idpId);
        }
        return listResponse;
    }

    /**
     * Get information of a specific federated authenticator of an IDP.
     *
     * @param idpId           Identity Provider resource ID.
     * @param authenticatorId Federated Authenticator ID.
     * @return FederatedAuthenticator.
     */
    public FederatedAuthenticator getFederatedAuthenticator(String idpId, String authenticatorId) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            FederatedAuthenticatorConfig[] fedAuthConfigs = idp.getFederatedAuthenticatorConfigs();
            if (fedAuthConfigs != null) {
                for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
                    if (StringUtils.equals(config.getName(), base64URLDecode(authenticatorId))) {
                        return createFederatedAuthenticator(authenticatorId, idp);
                    }
                }
            }
            throw handleException(Response.Status.NOT_FOUND,
                    Constants.ErrorMessage.ERROR_CODE_AUTHENTICATOR_NOT_FOUND_FOR_IDP, authenticatorId);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATOR,
                    authenticatorId);
        }
    }

    /**
     * Update federated authenticator of and IDP.
     *
     * @param idpId                Identity Provider resource ID.
     * @param authenticatorRequest Federated Authenticators Request.
     * @return FederatedAuthenticatorListResponse.
     */
    public FederatedAuthenticatorListResponse updateFederatedAuthenticators(String idpId, FederatedAuthenticatorRequest
            authenticatorRequest) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            // Need to create a clone, since modifying the fields of the original object, will modify the cached
            // IDP object.
            IdentityProvider idpToUpdate = createIdPClone(idp);
            updateFederatedAuthenticatorConfig(idpToUpdate, authenticatorRequest);

            IdentityProvider updatedIdp = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .updateIdPByResourceId(
                            idpId, idpToUpdate, ContextLoader.getTenantDomainFromContext());
            return createFederatedAuthenticatorResponse(updatedIdp);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP, null);
        }
    }

    /**
     * Update federated authenticator of and IDP.
     *
     * @param idpId                    Identity Provider resource ID.
     * @param federatedAuthenticatorId Federated Authenticator ID.
     * @param authenticator            Federated Authenticator information.
     * @return FederatedAuthenticator.
     */
    public FederatedAuthenticator updateFederatedAuthenticator(String idpId, String federatedAuthenticatorId,
                                                               FederatedAuthenticatorPUTRequest authenticator) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            // Need to create a clone, since modifying the fields of the original object, will modify the cached
            // IDP object.
            IdentityProvider idpToUpdate = createIdPClone(idp);

            // Create new FederatedAuthenticatorConfig to store the federated authenticator information.
            FederatedAuthenticatorConfig authConfig = createFederatedAuthenticatorConfig(federatedAuthenticatorId,
                    authenticator);
            FederatedAuthenticatorConfig[] fedAuthConfigs = createFederatedAuthenticatorArrayClone
                    (federatedAuthenticatorId, idp.getFederatedAuthenticatorConfigs());
            int configPos = getExistingAuthConfigPosition(fedAuthConfigs, federatedAuthenticatorId);
            // If configPos != -1, modify the existing authenticatorConfig of IDP.
            if (configPos != -1) {
                fedAuthConfigs[configPos] = authConfig;
            } else {
                // If configPos is -1 add new authenticator to the list.
                if (isValidAuthenticator(federatedAuthenticatorId)) {
                    List<FederatedAuthenticatorConfig> authConfigList = new ArrayList<>(Arrays.asList(fedAuthConfigs));
                    authConfigList.add(authConfig);
                    fedAuthConfigs = authConfigList.toArray(new FederatedAuthenticatorConfig[0]);
                } else {
                    throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage
                            .ERROR_CODE_AUTHENTICATOR_NOT_FOUND_FOR_IDP, federatedAuthenticatorId);
                }
            }
            idpToUpdate.setFederatedAuthenticatorConfigs(fedAuthConfigs);

            if (authenticator.getIsDefault()) {
                idpToUpdate.setDefaultAuthenticatorConfig(authConfig);
            } else if (idpToUpdate.getDefaultAuthenticatorConfig() != null && idpToUpdate
                    .getDefaultAuthenticatorConfig().getName().equals(authConfig.getName())) {
                idpToUpdate.setDefaultAuthenticatorConfig(null);
            }

            IdentityProvider updatedIdP = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .updateIdPByResourceId(idpId, idpToUpdate, ContextLoader
                            .getTenantDomainFromContext());
            return createFederatedAuthenticator(federatedAuthenticatorId, updatedIdP);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_AUTHENTICATOR,
                    federatedAuthenticatorId);
        }
    }

    /**
     * Get outbound provisioning connectors of a specific Identity Provider.
     *
     * @param idpId Identity Provider resource ID.
     * @return OutboundConnectorListResponse.
     */
    public OutboundConnectorListResponse getOutboundConnectors(String idpId) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            OutboundConnectorListResponse listResponse = null;
            String defaultConnectorId = null;

            ProvisioningConnectorConfig[] connectorConfigs = idp.getProvisioningConnectorConfigs();
            if (connectorConfigs != null) {
                listResponse = new OutboundConnectorListResponse();
                List<OutboundConnectorListItem> connectorList = new ArrayList<>();
                for (ProvisioningConnectorConfig config : connectorConfigs) {
                    connectorList.add(createOutboundConnectorListItem(idpId, config));
                    if (idp.getDefaultProvisioningConnectorConfig() != null) {
                        defaultConnectorId = base64URLEncode(idp.getDefaultProvisioningConnectorConfig()
                                .getName());
                    }
                }
                listResponse.setDefaultConnectorId(defaultConnectorId);
                listResponse.setConnectors(connectorList);
            }
            return listResponse;

        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTORS, idpId);
        }
    }

    /**
     * Get specific outbound provisioning connector of an Identity Provider.
     *
     * @param idpId       Identity Provider resource ID.
     * @param connectorId Outbound provisioning connector ID.
     * @return OutboundConnector.
     */
    public OutboundConnector getOutboundConnector(String idpId, String connectorId) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            ProvisioningConnectorConfig[] connectorConfigs = idp.getProvisioningConnectorConfigs();
            if (connectorConfigs != null) {
                for (ProvisioningConnectorConfig config : connectorConfigs) {
                    if (StringUtils.equals(config.getName(), base64URLDecode(connectorId))) {
                        return createOutboundConnector(connectorId, idp);
                    }
                }
            }
            throw handleException(Response.Status.NOT_FOUND,
                    Constants.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND_FOR_IDP, connectorId);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTOR, connectorId);
        }
    }

    /**
     * Update outbound provisioning connector config.
     *
     * @param idpId                    Identity Provider resource ID.
     * @param outboundConnectorRequest Outbound provisioning connector request.
     * @return OutboundConnectorListResponse.
     */
    public OutboundConnectorListResponse updateOutboundConnectors(String idpId, OutboundProvisioningRequest
            outboundConnectorRequest) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            // Need to create a clone, since modifying the fields of the original object, will modify the cached
            // IDP object.
            IdentityProvider idpToUpdate = createIdPClone(idp);
            updateOutboundConnectorConfig(idpToUpdate, outboundConnectorRequest);

            IdentityProvider updatedIdp = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .updateIdPByResourceId(
                            idpId, idpToUpdate, ContextLoader.getTenantDomainFromContext());
            return createOutboundProvisioningResponse(updatedIdp);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP, null);
        }
    }

    /**
     * Update outbound provisioning connector config.
     *
     * @param idpId             Identity Provider resource ID.
     * @param connectorId       Outbound provisioning connector ID.
     * @param outboundConnector New Outbound Connector information.
     * @return OutboundConnector OutboundConnector response.
     */
    public OutboundConnector updateOutboundConnector(String idpId, String connectorId,
                                                     OutboundConnectorPUTRequest
                                                             outboundConnector) {

        try {
            IdentityProvider idp =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idp == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            IdentityProvider idpToUpdate = createIdPClone(idp);
            ProvisioningConnectorConfig connectorConfig = createProvisioningConnectorConfig(connectorId,
                    outboundConnector);

            ProvisioningConnectorConfig[] provConnectorConfigs = createProvisioningConnectorArrayClone(connectorId, idp
                    .getProvisioningConnectorConfigs());
            int configPos = getExistingProvConfigPosition(provConnectorConfigs, connectorId);
            if (configPos != -1) {
                provConnectorConfigs[configPos] = connectorConfig;
            } else {
                // if configPos is -1 add new authenticator to the list.
                if (isValidConnector(connectorId)) {
                    List<ProvisioningConnectorConfig> connectorConfigsList = new ArrayList<>(
                            Arrays.asList(provConnectorConfigs));
                    connectorConfigsList.add(connectorConfig);
                    provConnectorConfigs = connectorConfigsList.toArray(new ProvisioningConnectorConfig[0]);
                } else {
                    throw handleException(Response.Status.NOT_FOUND,
                            Constants.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND_FOR_IDP, connectorId);
                }
            }
            idpToUpdate.setProvisioningConnectorConfigs(provConnectorConfigs);

            if (outboundConnector.getIsDefault()) {
                idpToUpdate.setDefaultProvisioningConnectorConfig(connectorConfig);
            } else if (idpToUpdate.getDefaultProvisioningConnectorConfig() != null && idpToUpdate
                    .getDefaultProvisioningConnectorConfig().getName().equals(connectorConfig.getName())) {
                idpToUpdate.setDefaultProvisioningConnectorConfig(null);
            }

            IdentityProvider updatedIdP = IdentityProviderServiceHolder.getIdentityProviderManager()
                    .updateIdPByResourceId(idpId, idpToUpdate, ContextLoader
                            .getTenantDomainFromContext());
            return createOutboundConnector(connectorId, updatedIdP);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_CONNECTOR, connectorId);
        }
    }

    /**
     * Get Claim Configuration.
     *
     * @param idpId Identity Provider resource ID.
     * @return Claims.
     */
    public Claims getClaimConfig(String idpId) {

        try {
            IdentityProvider identityProvider =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            return createClaimResponse(identityProvider.getClaimConfig());

        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CLAIMS, idpId);
        }
    }

    /**
     * Update claim configuration.
     *
     * @param idpId  Identity Provider resource ID.
     * @param claims Claims.
     * @return Claims   Update claim config.
     */
    public Claims updateClaimConfig(String idpId, Claims claims) {

        try {
            IdentityProvider idP =
                    createIdPClone(IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId,
                            ContextLoader.getTenantDomainFromContext(), true));
            if (idP == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            updateClaims(idP, claims);
            IdentityProvider updatedIdP =
                    IdentityProviderServiceHolder.getIdentityProviderManager().updateIdPByResourceId(idpId,
                            idP, ContextLoader.getTenantDomainFromContext());
            return createClaimResponse(updatedIdP.getClaimConfig());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_CLAIMS, idpId);
        }
    }

    /**
     * Get Role Configuration.
     *
     * @param idpId Identity Provider resource ID.
     * @return Roles.
     */
    public Roles getRoleConfig(String idpId) {

        try {
            IdentityProvider identityProvider =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            return createRoleResponse(identityProvider);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_ROLES, idpId);
        }
    }

    /**
     * Update role configuration.
     *
     * @param idpId Identity Provider resource ID.
     * @param roles Role information.
     * @return Roles    Update role config.
     */
    public Roles updateRoleConfig(String idpId, Roles roles) {

        try {
            IdentityProvider idP =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idP == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            updateRoles(idP, roles);

            IdentityProvider updatedIdP =
                    IdentityProviderServiceHolder.getIdentityProviderManager().updateIdPByResourceId(idpId,
                            idP, ContextLoader.getTenantDomainFromContext());
            return createRoleResponse(updatedIdP);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_ROLES, idpId);
        }
    }

    /**
     * Get Provisioning configuration. Includes JIT config and outbound provisioning connectors.
     *
     * @param idpId Identity Provider resource ID.
     * @return ProvisioningResponse.
     */
    public ProvisioningResponse getProvisioningConfig(String idpId) {

        try {
            IdentityProvider identityProvider =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            return createProvisioningResponse(identityProvider);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_PROVISIONING, idpId);
        }
    }

    /**
     * Get Just-In-Time Provisioning configuration.
     *
     * @param idpId Identity Provider resource ID.
     * @return JustInTimeProvisioning.
     */
    public JustInTimeProvisioning getJITConfig(String idpId) {

        try {
            IdentityProvider identityProvider =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            return createJITResponse(identityProvider);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_JIT, idpId);
        }
    }

    /**
     * Update Just-In-Time Provisioning configuration.
     *
     * @param idpId                        Identity Provider resource ID.
     * @param justInTimeProvisioningConfig JIT config.
     * @return updated JIT config.
     */
    public JustInTimeProvisioning updateJITConfig(String idpId, JustInTimeProvisioning justInTimeProvisioningConfig) {

        try {
            IdentityProvider idP =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (idP == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND,
                        idpId);
            }
            updateJIT(idP, justInTimeProvisioningConfig);

            IdentityProvider updatedIdP =
                    IdentityProviderServiceHolder.getIdentityProviderManager().updateIdPByResourceId(idpId,
                            idP, ContextLoader.getTenantDomainFromContext());
            return createJITResponse(updatedIdP);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_JIT, idpId);
        }
    }

    /**
     * Get applications that are connected to the Identity Provider identified by resource ID.
     *
     * @param resourceId    IDP resource ID.
     * @param limit         Limit parameter.
     * @param offset        Offset parameter.
     * @return  ConnectedApps.
     */
    public ConnectedApps getConnectedApps(String resourceId, Integer limit, Integer offset) {

        try {
            ConnectedAppsResult connectedAppsResult =
                    IdentityProviderServiceHolder.getIdentityProviderManager().getConnectedApplications(resourceId,
                            limit, offset, ContextLoader.getTenantDomainFromContext());
            return createConnectedAppsResponse(resourceId, connectedAppsResult);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTED_APPS,
                    resourceId);
        }
    }

    /**
     * Retrieve the list of IDP templates.
     *
     * @param limit         Items per page.
     * @param offset        Offset.
     * @param searchContext Search Criteria. E.g. filter="name" sw "google" and "category" eq "DEFAULT"
     * @return List of identity templates.
     */
    public IdentityProviderTemplateListResponse getIDPTemplates(Integer limit, Integer offset, SearchContext
            searchContext) {

        try {
            TemplateManager templateManager = IdentityProviderServiceHolder.getTemplateManager();
            List<Template> templateList = templateManager.listTemplates(
                    TemplateMgtConstants.TemplateType.IDP_TEMPLATE.toString(), limit, offset, getSearchCondition
                            (TemplateMgtConstants.TemplateType.IDP_TEMPLATE.toString(), ContextLoader
                                    .getTenantDomainFromContext(), searchContext));
            return createIDPTemplateListResponse(templateList, offset, limit, searchContext.getSearchExpression());
        } catch (TemplateManagementException e) {
            throw handleTemplateMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDP_TEMPLATES, null);
        }
    }

    /**
     * Retrieve search condition from @{SearchContext}.
     *
     * @param templateType  Template type.
     * @param tenantDomain  Tenant domain.
     * @param searchContext Search context.
     * @return  Condition.
     */
    private Condition getSearchCondition(String templateType, String tenantDomain, SearchContext searchContext) {

        if (searchContext != null) {
            SearchCondition<ResourceSearchBean> searchCondition = searchContext.getCondition(ResourceSearchBean.class);
            if (searchCondition != null) {
                Condition result = buildSearchCondition(searchCondition);
                Condition typeCondition = new PrimitiveCondition(Constants.TEMPLATE_TYPE_KEY, EQUALS, templateType);
                Condition tenantCondition = new PrimitiveCondition(Constants.TENANT_DOMAIN_KEY, EQUALS, tenantDomain);

                List<Condition> list = new ArrayList<>();
                list.add(result);
                list.add(typeCondition);
                list.add(tenantCondition);
                return new ComplexCondition(getComplexOperatorFromOdata(ConditionType.AND),
                        list);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Search condition parsed from the search expression is invalid.");
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Cannot find a valid search context.");
            }
        }
        return null;
    }

    private Condition buildSearchCondition(SearchCondition searchCondition) {

        if (!(searchCondition.getStatement() == null)) {
            PrimitiveStatement primitiveStatement = searchCondition.getStatement();

            if (Constants.SEARCH_KEY_NAME.equals(primitiveStatement.getProperty())) {
                return new PrimitiveCondition(
                        Constants.SEARCH_KEY_NAME_INTERNAL, getPrimitiveOperatorFromOdata(primitiveStatement
                        .getCondition()), primitiveStatement.getValue());
            } else if (Constants.SEARCH_KEY_SERVICES.equals(primitiveStatement.getProperty()) &&
                    getPrimitiveOperatorFromOdata(primitiveStatement.getCondition()) == EQUALS) {
                Condition servicesAttrKeyCondition = new PrimitiveCondition(Constants.ATTR_KEY, EQUALS, Constants
                        .SEARCH_KEY_SERVICES);
                Condition servicesAttrValueCondition = new PrimitiveCondition(Constants.ATTR_VALUE, EQUALS,
                        primitiveStatement.getValue());
                Condition servicesCondition = new ComplexCondition(getComplexOperatorFromOdata(ConditionType.AND),
                        Arrays.asList(servicesAttrKeyCondition, servicesAttrValueCondition));

                Condition servicesCombinedAttrKeyCondition = new PrimitiveCondition(Constants.ATTR_KEY, EQUALS,
                        Constants.SEARCH_KEY_SERVICES);
                Condition servicesCombinedAttrValueCondition = new PrimitiveCondition(Constants.ATTR_VALUE, EQUALS,
                        Constants.SEARCH_VALUE_AUTHENTICATION_PROVISIONING);
                Condition servicesCombinedCondition = new ComplexCondition(getComplexOperatorFromOdata(ConditionType
                        .AND), Arrays.asList(servicesCombinedAttrKeyCondition, servicesCombinedAttrValueCondition));

                return new ComplexCondition(getComplexOperatorFromOdata(ConditionType.OR), Arrays.asList
                        (servicesCondition, servicesCombinedCondition));
            } else if (Constants.SEARCH_KEYS.contains(primitiveStatement.getProperty())) {
                List<Condition> list = new ArrayList<>();
                Condition attrKeyCondition = new PrimitiveCondition(Constants.ATTR_KEY, EQUALS, primitiveStatement
                        .getProperty());
                Condition attrValueCondition = new PrimitiveCondition(Constants.ATTR_VALUE,
                        getPrimitiveOperatorFromOdata(primitiveStatement.getCondition()), primitiveStatement
                        .getValue());
                list.add(attrKeyCondition);
                list.add(attrValueCondition);
                return new ComplexCondition(getComplexOperatorFromOdata(ConditionType.AND),
                        list);
            } else {
                throw handleException(Response.Status.BAD_REQUEST,
                        Constants.ErrorMessage.ERROR_CODE_ERROR_INVALID_SEARCH_FILTER, null);
            }
        } else {
            List<Condition> conditions = new ArrayList<>();
            for (Object condition : searchCondition.getSearchConditions()) {
                Condition buildCondition = buildSearchCondition((SearchCondition) condition);
                conditions.add(buildCondition);
            }
            return new ComplexCondition(getComplexOperatorFromOdata(searchCondition.getConditionType()), conditions);
        }
    }

    private org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.PrimitiveOperator
    getPrimitiveOperatorFromOdata(org.apache.cxf.jaxrs.ext.search.ConditionType odataConditionType) {

        org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.PrimitiveOperator
                primitiveConditionType = null;
        switch (odataConditionType) {
            case EQUALS:
                primitiveConditionType = EQUALS;
                break;
            case GREATER_OR_EQUALS:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.GREATER_OR_EQUALS;
                break;
            case LESS_OR_EQUALS:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.LESS_OR_EQUALS;
                break;
            case GREATER_THAN:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.GREATER_THAN;
                break;
            case NOT_EQUALS:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.NOT_EQUALS;
                break;
            case LESS_THAN:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.LESS_THAN;
                break;
            default:
                throw handleException(Response.Status.BAD_REQUEST,
                        Constants.ErrorMessage.ERROR_CODE_ERROR_INVALID_SEARCH_FILTER, null);
        }
        return primitiveConditionType;
    }

    private org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.ComplexOperator
    getComplexOperatorFromOdata(org.apache.cxf.jaxrs.ext.search.ConditionType odataConditionType) {

        org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.ComplexOperator
                complexConditionType = null;
        switch (odataConditionType) {
            case OR:
                complexConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType
                        .ComplexOperator.OR;
                break;
            case AND:
                complexConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType
                        .ComplexOperator.AND;
                break;
            default:
                throw handleException(Response.Status.BAD_REQUEST,
                        Constants.ErrorMessage.ERROR_CODE_ERROR_INVALID_SEARCH_FILTER, null);
        }
        return complexConditionType;
    }

    /**
     * Get an identity provider template identified by resource ID.
     *
     * @param templateId IDP template Id
     * @return IdentityProviderTemplateResponse
     */
    public IdentityProviderTemplate getIDPTemplate(String templateId) {

        try {
            Template idpTemplate = IdentityProviderServiceHolder.getTemplateManager().getTemplateById(templateId);
            return createIDPTemplateResponse(idpTemplate);
        } catch (TemplateManagementException e) {
            throw handleTemplateMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_TEMPLATE,
                    templateId);
        } catch (IOException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_TEMPLATE, templateId);
        }
    }

    /**
     * Update IDP template.
     *
     * @param identityProviderTemplate Updated IDP template
     */
    public void updateIDPTemplate(String templateId, IdentityProviderTemplate identityProviderTemplate) {

        try {
            Template idpTemplate = generateIDPTemplate(identityProviderTemplate);
            IdentityProviderServiceHolder.getTemplateManager().updateTemplateById(templateId, idpTemplate);
        } catch (TemplateManagementException e) {
            throw handleTemplateMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_TEMPLATE,
                    identityProviderTemplate.getId());
        } catch (JsonProcessingException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_TEMPLATE, templateId);
        }
    }

    /**
     * Create a new IDP template.
     *
     * @param identityProviderTemplate identityProviderTemplatePOSTRequest
     * @return IdentityProviderTemplateResponse
     */
    public String createIDPTemplate(IdentityProviderTemplate identityProviderTemplate) {

        try {
            TemplateManager templateManager = IdentityProviderServiceHolder.getTemplateManager();
            Template idpTemplate = generateIDPTemplate(identityProviderTemplate);
            return templateManager.addTemplate(idpTemplate);
        } catch (TemplateManagementException e) {
            throw handleTemplateMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP_TEMPLATE, null);
        } catch (JsonProcessingException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP_TEMPLATE, null);
        }
    }

    /**
     * Delete a IDP template identified by resource Id.
     *
     * @param templateId Id of the IDP template
     */
    public void deleteIDPTemplate(String templateId) {

        try {
            TemplateManager templateManager = IdentityProviderServiceHolder.getTemplateManager();
            templateManager.deleteTemplateById(templateId);
        } catch (TemplateManagementException e) {
            throw handleTemplateMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_IDP_TEMPLATE,
                    templateId);
        }
    }

//  Private utility Methods.

    private IdentityProviderTemplateListResponse createIDPTemplateListResponse(
            List<Template> templateInfoList, Integer offset, Integer limit, String filter) {

        IdentityProviderTemplateListResponse idpTemplateListResponse = new IdentityProviderTemplateListResponse();
        if (!CollectionUtils.isEmpty(templateInfoList)) {
            List<IdentityProviderTemplateListItem> idpTemplates = new ArrayList<>();
            for (Template idpTemplate: templateInfoList) {
                IdentityProviderTemplateListItem idpTemplateListItem = new IdentityProviderTemplateListItem();
                idpTemplateListItem.setId(idpTemplate.getTemplateId());
                idpTemplateListItem.setDescription(idpTemplate.getDescription());
                idpTemplateListItem.setName(idpTemplate.getTemplateName());
                idpTemplateListItem.setImage(idpTemplate.getImageUrl());
                idpTemplateListItem.setSelf(
                        ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT
                                        + IDP_TEMPLATE_PATH_COMPONENT + "/%s",
                                idpTemplate.getTemplateId())).toString());
                if (idpTemplate.getPropertiesMap().containsKey(PROP_CATEGORY)) {
                    if (IdentityProviderTemplateListItem.CategoryEnum.CUSTOM.toString()
                            .equals(idpTemplate.getPropertiesMap().get(PROP_CATEGORY))) {
                        idpTemplateListItem.setCategory(IdentityProviderTemplateListItem.CategoryEnum.CUSTOM);
                    } else {
                        idpTemplateListItem.setCategory(IdentityProviderTemplateListItem.CategoryEnum.DEFAULT);
                    }
                }
                if (idpTemplate.getPropertiesMap().containsKey(PROP_DISPLAY_ORDER)) {
                    idpTemplateListItem.setDisplayOrder(
                            Integer.valueOf(idpTemplate.getPropertiesMap().get(PROP_DISPLAY_ORDER)));
                }
                if ((idpTemplate.getPropertiesMap().containsKey(PROP_SERVICES)) &&
                        (idpTemplate.getPropertiesMap().get(PROP_SERVICES) != null)) {
                    idpTemplateListItem.setServices(Arrays.asList(
                            idpTemplate.getPropertiesMap().get(PROP_SERVICES).split(",")));
                }
                idpTemplates.add(idpTemplateListItem);
            }
            idpTemplateListResponse.setTemplates(idpTemplates);
            idpTemplateListResponse.setCount(idpTemplates.size());
        } else {
            idpTemplateListResponse.setCount(0);
        }
        limit = (limit == null) ? Integer.valueOf(0) : limit;
        offset = (offset == null) ? Integer.valueOf(0) : offset;
        idpTemplateListResponse.setTotalResults(templateInfoList.size());
        idpTemplateListResponse.setStartIndex(offset + 1);
        idpTemplateListResponse.setLinks(createLinks(V1_API_PATH_COMPONENT + IDP_TEMPLATE_PATH_COMPONENT,
                limit, offset, templateInfoList.size(), filter));
        return idpTemplateListResponse;
    }

    private IdentityProviderTemplate createIDPTemplateResponse(Template idpTemplate) throws IOException {

        IdentityProviderTemplate idpTemplateResponse = new IdentityProviderTemplate();
        idpTemplateResponse.setId(idpTemplate.getTemplateId());
        idpTemplateResponse.setName(idpTemplate.getTemplateName());
        idpTemplateResponse.setDescription(idpTemplate.getDescription());
        idpTemplateResponse.setImage(idpTemplate.getImageUrl());
        if (idpTemplate.getPropertiesMap().containsKey(PROP_CATEGORY)) {
            if (IdentityProviderTemplateListItem.CategoryEnum.CUSTOM.toString()
                    .equals(idpTemplate.getPropertiesMap().get(PROP_CATEGORY))) {
                idpTemplateResponse.setCategory(IdentityProviderTemplate.CategoryEnum.CUSTOM);
            } else {
                idpTemplateResponse.setCategory(IdentityProviderTemplate.CategoryEnum.DEFAULT);
            }
        }
        if (idpTemplate.getPropertiesMap().containsKey(PROP_DISPLAY_ORDER)) {
            idpTemplateResponse.setDisplayOrder(
                    Integer.valueOf(idpTemplate.getPropertiesMap().get(PROP_DISPLAY_ORDER)));
        }
        if (idpTemplate.getTemplateScript() != null) {
            ObjectMapper mapper = new ObjectMapper();
            IdentityProviderPOSTRequest idp = mapper.readValue(idpTemplate.getTemplateScript(),
                    IdentityProviderPOSTRequest.class);
            idpTemplateResponse.setIdp(idp);
        }
        return idpTemplateResponse;
    }

    private Template generateIDPTemplate(IdentityProviderTemplate idpTemplate) throws JsonProcessingException {

        Template identityProviderTemplate = new Template();

        identityProviderTemplate.setTemplateName(idpTemplate.getName());
        identityProviderTemplate.setDescription(idpTemplate.getDescription());
        identityProviderTemplate.setImageUrl(idpTemplate.getImage());
        identityProviderTemplate.setTenantId(IdentityTenantUtil
                .getTenantId(ContextLoader.getTenantDomainFromContext()));
        Map<String, String> properties = createPropertiesMapForIdPTemplate(idpTemplate);
        identityProviderTemplate.setTemplateType(TemplateMgtConstants.TemplateType.IDP_TEMPLATE);
        identityProviderTemplate.setPropertiesMap(properties);
        identityProviderTemplate.setTemplateScript(createIDPTemplateScript(idpTemplate.getIdp()));
        return identityProviderTemplate;
    }

    private Map<String, String> createPropertiesMapForIdPTemplate(IdentityProviderTemplate idpTemplate) {

        Map<String, String> properties = new HashMap<>();
        if (idpTemplate.getCategory() != null) {
            properties.put(PROP_CATEGORY, idpTemplate.getCategory().toString());
        }
        if (idpTemplate.getDisplayOrder() != null) {
            properties.put(PROP_DISPLAY_ORDER, String.valueOf(idpTemplate.getDisplayOrder()));
        }
        ArrayList<String> services = createServicesListForIdP(idpTemplate.getIdp());
        if (!services.isEmpty()) {
            String servicesString = String.join(",", services);
            properties.put(PROP_SERVICES, servicesString);
        }
        return properties;
    }

    private ArrayList<String> createServicesListForIdP(IdentityProviderPOSTRequest idp) {

        ArrayList<String> services = new ArrayList<>();
        if (idp.getProvisioning() != null && idp.getProvisioning().getOutboundConnectors() != null && CollectionUtils
                .isNotEmpty(idp.getProvisioning().getOutboundConnectors().getConnectors())) {
            services.add(SERV_PROVISIONING);
        }
        if (idp.getFederatedAuthenticators() != null && CollectionUtils.isNotEmpty(idp.getFederatedAuthenticators()
                .getAuthenticators())) {
            services.add(SERV_AUTHENTICATION);
        }
        return services;
    }

    private APIError handleTemplateMgtException(TemplateManagementException e, Constants.ErrorMessage errorEnum,
                                                String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());

        Response.Status status;

        if (e instanceof TemplateManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(TEMPLATE_MGT_ERROR_CODE_DELIMITER) ?
                        errorCode : Constants.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof TemplateManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(TEMPLATE_MGT_ERROR_CODE_DELIMITER) ?
                        errorCode : Constants.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return new APIError(status, errorResponse);
    }

    private String createIDPTemplateScript(IdentityProviderPOSTRequest idpTemplate) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(idpTemplate);
    }

    private ConnectedApps createConnectedAppsResponse(String resourceId, ConnectedAppsResult connectedAppsResult) {

        ConnectedApps connectedAppsResponse = new ConnectedApps();
        if (connectedAppsResult == null) {
            return connectedAppsResponse;
        }
        List<ConnectedApp> connectedAppList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(connectedAppsResult.getApps())) {
            for (String app : connectedAppsResult.getApps()) {
                ConnectedApp listItem = new ConnectedApp();
                listItem.setAppId(app);
                listItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                                "/applications/%s", app)).toString());
                connectedAppList.add(listItem);
            }
            connectedAppsResponse.setConnectedApps(connectedAppList);
            connectedAppsResponse.setCount(connectedAppList.size());
        } else {
            connectedAppsResponse.setCount(0);
        }

        connectedAppsResponse.setTotalResults(connectedAppsResult.getTotalAppCount());
        connectedAppsResponse.setStartIndex(connectedAppsResult.getOffSet() + 1);
        connectedAppsResponse.setLinks(createLinks(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                        Constants.PATH_SEPERATOR + resourceId + "/connected-apps", connectedAppsResult.getLimit(),
                connectedAppsResult.getOffSet(), connectedAppsResult.getTotalAppCount(), null));
        return connectedAppsResponse;
    }

    private MetaFederatedAuthenticatorListItem createMetaFederatedAuthenticatorListItem(FederatedAuthenticatorConfig
                                                                                                authenticatorConfig) {

        MetaFederatedAuthenticatorListItem metaFederatedAuthenticator = new MetaFederatedAuthenticatorListItem();
        String authenticatorId = base64URLEncode(authenticatorConfig.getName());
        metaFederatedAuthenticator.setName(authenticatorConfig.getName());
        metaFederatedAuthenticator.setAuthenticatorId(authenticatorId);
        metaFederatedAuthenticator.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                IDP_PATH_COMPONENT + "/meta/federated-authenticators/%s", authenticatorId)).toString());
        return metaFederatedAuthenticator;
    }

    private MetaFederatedAuthenticator createMetaFederatedAuthenticator(FederatedAuthenticatorConfig
                                                                                authenticatorConfig) {

        MetaFederatedAuthenticator metaFederatedAuthenticator = new MetaFederatedAuthenticator();
        metaFederatedAuthenticator.setName(authenticatorConfig.getName());
        metaFederatedAuthenticator.setAuthenticatorId(base64URLEncode(authenticatorConfig.getName()));
        metaFederatedAuthenticator.setDisplayName(authenticatorConfig.getDisplayName());
        Property[] properties = authenticatorConfig.getProperties();
        List<MetaProperty> metaProperties = Arrays.stream(properties).map(propertyToExternalMeta).collect(Collectors
                .toList());
        metaFederatedAuthenticator.setProperties(metaProperties);
        return metaFederatedAuthenticator;
    }

    private MetaOutboundConnectorListItem createMetaOutboundConnectorListItem(ProvisioningConnectorConfig
                                                                                      connectorConfig) {

        MetaOutboundConnectorListItem metaOutboundProvisioningConnector = new MetaOutboundConnectorListItem();
        metaOutboundProvisioningConnector.setName(connectorConfig.getName());
        String connectorId = base64URLEncode(connectorConfig.getName());
        metaOutboundProvisioningConnector.setConnectorId(connectorId);
        metaOutboundProvisioningConnector
                .setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                        "/meta/outbound-provisioning-connectors/%s", connectorId)).toString());
        return metaOutboundProvisioningConnector;
    }

    private MetaOutboundConnector createMetaOutboundConnector(ProvisioningConnectorConfig
                                                                      connectorConfig) {

        MetaOutboundConnector metaOutboundProvisioningConnector = new MetaOutboundConnector();
        metaOutboundProvisioningConnector.setName(connectorConfig.getName());
        metaOutboundProvisioningConnector.setDisplayName(connectorConfig.getName());
        metaOutboundProvisioningConnector.setConnectorId(base64URLEncode(connectorConfig.getName()));
        Property[] properties = connectorConfig.getProvisioningProperties();
        List<MetaProperty> metaProperties = Arrays.stream(properties).map(propertyToExternalMeta).collect(Collectors
                .toList());
        metaOutboundProvisioningConnector.setProperties(metaProperties);
        return metaOutboundProvisioningConnector;
    }

    private void updateFederatedAuthenticatorConfig(IdentityProvider idp, FederatedAuthenticatorRequest
            federatedAuthenticatorRequest) {

        if (federatedAuthenticatorRequest != null) {
            List<FederatedAuthenticator> federatedAuthenticators = federatedAuthenticatorRequest.getAuthenticators();
            String defaultAuthenticator = federatedAuthenticatorRequest.getDefaultAuthenticatorId();
            FederatedAuthenticatorConfig defaultAuthConfig = null;
            List<FederatedAuthenticatorConfig> fedAuthConfigs = new ArrayList<>();
            for (FederatedAuthenticator authenticator : federatedAuthenticators) {
                FederatedAuthenticatorConfig authConfig = new FederatedAuthenticatorConfig();
                authConfig.setName(base64URLDecode(authenticator.getAuthenticatorId()));
                authConfig.setDisplayName(getDisplayNameOfAuthenticator(authConfig.getName()));
                authConfig.setEnabled(authenticator.getIsEnabled());
                List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> authProperties =
                        authenticator.getProperties();
                if (IdentityApplicationConstants.Authenticator.SAML2SSO.FED_AUTH_NAME.equals(authConfig.getName())) {
                    validateSamlMetadata(authProperties);
                }
                if (authProperties != null) {
                    List<Property> properties = authProperties.stream()
                            .map(propertyToInternal)
                            .collect(Collectors.toList());
                    authConfig.setProperties(properties.toArray(new Property[0]));
                }
                fedAuthConfigs.add(authConfig);

                if (StringUtils.equals(defaultAuthenticator, authenticator.getAuthenticatorId())) {
                    defaultAuthConfig = authConfig;
                }
            }

            if (StringUtils.isNotBlank(defaultAuthenticator) && defaultAuthConfig == null) {
                throw handleException(Response.Status.BAD_REQUEST,
                        Constants.ErrorMessage.ERROR_CODE_INVALID_DEFAULT_AUTHENTICATOR, null);
            }
            idp.setFederatedAuthenticatorConfigs(fedAuthConfigs.toArray(new FederatedAuthenticatorConfig[0]));
            idp.setDefaultAuthenticatorConfig(defaultAuthConfig);
        }
    }

    /**
     * Returns the 'DisplayName' property of the federated authenticator identified by authenticator name.
     *
     * @param authenticatorName Federated authenticator name.
     * @return Display name of authenticator.
     */
    private String getDisplayNameOfAuthenticator(String authenticatorName) {

        try {
            FederatedAuthenticatorConfig[] authenticatorConfigs =
                    IdentityProviderServiceHolder.getIdentityProviderManager()
                            .getAllFederatedAuthenticators();
            for (FederatedAuthenticatorConfig config : authenticatorConfigs) {

                if (StringUtils.equals(config.getName(), authenticatorName)) {
                    return config.getDisplayName();
                }
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP, null);
        }
        return null;
    }

    private void updateOutboundConnectorConfig(IdentityProvider idp,
                                               OutboundProvisioningRequest outboundProvisioningRequest) {

        if (outboundProvisioningRequest != null) {
            List<OutboundConnector> outboundConnectors = outboundProvisioningRequest.getConnectors();
            String defaultConnectorId = outboundProvisioningRequest.getDefaultConnectorId();
            ProvisioningConnectorConfig defaultConnectorConfig = null;
            List<ProvisioningConnectorConfig> connectorConfigs = new ArrayList<>();
            for (OutboundConnector connector : outboundConnectors) {
                ProvisioningConnectorConfig connectorConfig = new ProvisioningConnectorConfig();
                connectorConfig.setName(base64URLDecode(connector.getConnectorId()));
                connectorConfig.setEnabled(connector.getIsEnabled());

                if (connector.getProperties() != null) {
                    List<Property> properties = connector.getProperties().stream()
                            .map(propertyToInternal)
                            .collect(Collectors.toList());
                    connectorConfig.setProvisioningProperties(properties.toArray(new Property[0]));
                }
                connectorConfigs.add(connectorConfig);

                if (StringUtils.equals(defaultConnectorId, connector.getConnectorId())) {
                    defaultConnectorConfig = connectorConfig;
                }
            }

            if (StringUtils.isNotBlank(defaultConnectorId) && defaultConnectorConfig == null) {
                throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                        .ERROR_CODE_INVALID_DEFAULT_OUTBOUND_CONNECTOR, null);
            }

            idp.setProvisioningConnectorConfigs(connectorConfigs.toArray(new ProvisioningConnectorConfig[0]));
            idp.setDefaultProvisioningConnectorConfig(defaultConnectorConfig);
        }
    }

    private void updateJIT(IdentityProvider identityProvider, JustInTimeProvisioning jit) {

        if (jit != null) {
            JustInTimeProvisioning.SchemeEnum schemeEnum = jit.getScheme();

            JustInTimeProvisioningConfig jitConfig = new JustInTimeProvisioningConfig();
            jitConfig.setProvisioningEnabled(jit.getIsEnabled());
            jitConfig.setProvisioningUserStore(jit.getUserstore());
            switch (schemeEnum) {
                case PROMPT_USERNAME_PASSWORD_CONSENT:
                    jitConfig.setModifyUserNameAllowed(true);
                    jitConfig.setPasswordProvisioningEnabled(true);
                    jitConfig.setPromptConsent(true);
                    break;
                case PROMPT_PASSWORD_CONSENT:
                    jitConfig.setModifyUserNameAllowed(false);
                    jitConfig.setPasswordProvisioningEnabled(true);
                    jitConfig.setPromptConsent(true);
                    break;
                case PROMPT_CONSENT:
                    jitConfig.setModifyUserNameAllowed(false);
                    jitConfig.setPasswordProvisioningEnabled(false);
                    jitConfig.setPromptConsent(true);
                    break;
                case PROVISION_SILENTLY:
                    jitConfig.setModifyUserNameAllowed(false);
                    jitConfig.setPasswordProvisioningEnabled(false);
                    jitConfig.setPromptConsent(false);
                    break;
            }
            identityProvider.setJustInTimeProvisioningConfig(jitConfig);
        }
    }

    private void updateClaims(IdentityProvider idp, Claims claims) {

        if (claims != null) {
            ClaimConfig claimConfig = new ClaimConfig();
            List<ClaimMapping> claimMappings = new ArrayList<>();
            List<org.wso2.carbon.identity.application.common.model.Claim> idpClaims = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(claims.getMappings())) {
                claimConfig.setLocalClaimDialect(false);
                for (org.wso2.carbon.identity.api.server.idp.v1.model.ClaimMapping mapping : claims.getMappings()) {

                    String idpClaimUri = mapping.getIdpClaim();
                    String localClaimUri = mapping.getLocalClaim().getUri();

                    ClaimMapping internalMapping = new ClaimMapping();
                    org.wso2.carbon.identity.application.common.model.Claim remoteClaim = new org.wso2.carbon.identity
                            .application.common.model.Claim();
                    remoteClaim.setClaimUri(idpClaimUri);

                    org.wso2.carbon.identity.application.common.model.Claim localClaim = new org.wso2.carbon.identity
                            .application.common.model.Claim();
                    localClaim.setClaimUri(localClaimUri);

                    internalMapping.setRemoteClaim(remoteClaim);
                    internalMapping.setLocalClaim(localClaim);
                    claimMappings.add(internalMapping);
                    idpClaims.add(remoteClaim);
                }
            } else {
                claimConfig.setLocalClaimDialect(true);
            }

            if ((claims.getUserIdClaim() != null)) {
                claimConfig.setUserClaimURI(claims.getUserIdClaim().getUri());
            }
            if (claims.getRoleClaim() != null) {
                claimConfig.setRoleClaimURI(claims.getRoleClaim().getUri());
            }
            List<ProvisioningClaim> provClaims = claims.getProvisioningClaims();
            for (ProvisioningClaim provClaim : provClaims) {
                String provClaimUri = provClaim.getClaim().getUri();
                if (CollectionUtils.isNotEmpty(claims.getMappings())) {
                    for (ClaimMapping internalMapping : claimMappings) {

                        if (StringUtils.equals(provClaimUri, internalMapping.getRemoteClaim().getClaimUri())) {
                            internalMapping.setDefaultValue(provClaim.getDefaultValue());
                            internalMapping.setRequested(true);
                        }
                    }
                } else {
                    ClaimMapping internalMapping = new ClaimMapping();
                    org.wso2.carbon.identity.application.common.model.Claim localClaim = new org.wso2.carbon.identity
                            .application.common.model.Claim();
                    localClaim.setClaimUri(provClaimUri);
                    internalMapping.setLocalClaim(localClaim);
                    internalMapping.setDefaultValue(provClaim.getDefaultValue());
                    internalMapping.setRequested(true);
                    claimMappings.add(internalMapping);
                }
            }
            claimConfig.setClaimMappings(claimMappings.toArray(new ClaimMapping[0]));
            claimConfig.setIdpClaims(idpClaims.toArray(new org.wso2.carbon.identity.application.common.model.Claim[0]));
            idp.setClaimConfig(claimConfig);
        }
    }

    private void updateRoles(IdentityProvider idp, Roles roles) {

        if (roles != null) {
            PermissionsAndRoleConfig permissionsAndRoleConfig = new PermissionsAndRoleConfig();

            List<org.wso2.carbon.identity.api.server.idp.v1.model.RoleMapping> mappings = roles.getMappings();
            List<RoleMapping> internalMappings = new ArrayList<>();

            List<String> idpRoles = new ArrayList<>();

            if (mappings != null) {
                for (org.wso2.carbon.identity.api.server.idp.v1.model.RoleMapping mapping : mappings) {

                    RoleMapping internalMapping = new RoleMapping();


                    internalMapping.setLocalRole(new LocalRole(mapping.getLocalRole()));
                    internalMapping.setRemoteRole(mapping.getIdpRole());
                    idpRoles.add(mapping.getIdpRole());
                    internalMappings.add(internalMapping);
                }
            }
            permissionsAndRoleConfig.setIdpRoles(idpRoles.toArray(new String[0]));
            permissionsAndRoleConfig.setRoleMappings(internalMappings.toArray(new RoleMapping[0]));
            idp.setPermissionAndRoleConfig(permissionsAndRoleConfig);
            idp.setProvisioningRole(StringUtils.join(roles.getOutboundProvisioningRoles(), ","));
        }
    }

    private Function<org.wso2.carbon.identity.api.server.idp.v1.model.Property, Property> propertyToInternal
            = apiProperty -> {

        Property property = new Property();
        property.setName(apiProperty.getKey());
        property.setValue(apiProperty.getValue());
        return property;
    };

    private Function<Property, org.wso2.carbon.identity.api.server.idp.v1.model.Property> propertyToExternal
            = property -> {

        org.wso2.carbon.identity.api.server.idp.v1.model.Property apiProperty = new org.wso2.carbon.identity.api
                .server.idp.v1.model.Property();
        apiProperty.setKey(property.getName());
        apiProperty.setValue(property.getValue());
        return apiProperty;
    };

    private IdentityProvider createIDP(IdentityProviderPOSTRequest identityProviderPOSTRequest) {

        String idpJWKSUri = null;
        IdentityProvider idp = new IdentityProvider();
        idp.setIdentityProviderName(identityProviderPOSTRequest.getName());
        idp.setAlias(identityProviderPOSTRequest.getAlias());
        idp.setPrimary(false);
        idp.setIdentityProviderDescription(identityProviderPOSTRequest.getDescription());
        idp.setHomeRealmId(identityProviderPOSTRequest.getHomeRealmIdentifier());
        idp.setImageUrl(identityProviderPOSTRequest.getImage());
        if (identityProviderPOSTRequest.getCertificate() != null && StringUtils.isNotBlank(identityProviderPOSTRequest
                .getCertificate().getJwksUri())) {
            idpJWKSUri = identityProviderPOSTRequest.getCertificate().getJwksUri();
        } else if (identityProviderPOSTRequest.getCertificate() != null && identityProviderPOSTRequest.getCertificate()
                .getCertificates() != null) {
            idp.setCertificate(StringUtils.join(identityProviderPOSTRequest.getCertificate().getCertificates(), ""));
        }
        idp.setFederationHub(identityProviderPOSTRequest.getIsFederationHub());

        updateFederatedAuthenticatorConfig(idp, identityProviderPOSTRequest.getFederatedAuthenticators());
        if (identityProviderPOSTRequest.getProvisioning() != null) {
            updateOutboundConnectorConfig(idp, identityProviderPOSTRequest.getProvisioning().getOutboundConnectors());
            updateJIT(idp, identityProviderPOSTRequest.getProvisioning().getJit());
        }
        updateClaims(idp, identityProviderPOSTRequest.getClaims());
        updateRoles(idp, identityProviderPOSTRequest.getRoles());

        List<IdentityProviderProperty> idpProperties = new ArrayList<>();
        if (StringUtils.isNotBlank(idpJWKSUri)) {
            IdentityProviderProperty jwksProperty = new IdentityProviderProperty();
            jwksProperty.setName(Constants.JWKS_URI);
            jwksProperty.setValue(idpJWKSUri);
            idpProperties.add(jwksProperty);
        }
        idp.setIdpProperties(idpProperties.toArray(new IdentityProviderProperty[0]));
        return idp;
    }

    private IdentityProviderListResponse createIDPListResponse(IdpSearchResult idpSearchResult,
                                                               List<String> requestedAttributeList) {

        List<IdentityProvider> idps = idpSearchResult.getIdPs();
        IdentityProviderListResponse listResponse = new IdentityProviderListResponse();
        if (CollectionUtils.isNotEmpty(idps)) {
            List<IdentityProviderListItem> identityProviderList = new ArrayList<>();
            for (IdentityProvider idp : idps) {
                IdentityProviderListItem listItem = populateIDPListResponse(idp, requestedAttributeList);
                identityProviderList.add(listItem);
            }
            listResponse.setIdentityProviders(identityProviderList);
            listResponse.setCount(idps.size());
        } else {
            listResponse.setCount(0);
        }

        listResponse.setTotalResults(idpSearchResult.getTotalIDPCount());
        listResponse.setStartIndex(idpSearchResult.getOffSet() + 1);
        listResponse.setLinks(createLinks(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT, idpSearchResult
                        .getLimit(), idpSearchResult.getOffSet(), idpSearchResult.getTotalIDPCount(), idpSearchResult
                .getFilter()));
        return listResponse;
    }

    private IdentityProviderListItem populateIDPListResponse(IdentityProvider idp,
                                                             List<String> requestedAttributeList) {

        IdentityProviderListItem identityProviderListItem = new IdentityProviderListItem();
        // Create IDP basic information.
        identityProviderListItem.setId(idp.getResourceId());
        identityProviderListItem.setName(idp.getIdentityProviderName());
        identityProviderListItem.setDescription(idp.getIdentityProviderDescription());
        identityProviderListItem.setIsEnabled(idp.isEnable());
        identityProviderListItem.setImage(idp.getImageUrl());
        identityProviderListItem.setSelf(
                ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + "/%s",
                        idp.getResourceId())).toString());

        // Populate optional IDP information if exists.
        if (requestedAttributeList != null) {
            for (String requestedAttribute : requestedAttributeList) {
                switch (requestedAttribute) {
                    case Constants.IS_PRIMARY:
                        identityProviderListItem.setIsPrimary(idp.isPrimary());
                        break;
                    case Constants.IS_FEDERATION_HUB:
                        identityProviderListItem.setIsFederationHub(idp.isFederationHub());
                        break;
                    case Constants.HOME_REALM_IDENTIFIER:
                        identityProviderListItem.setHomeRealmIdentifier(idp.getHomeRealmId());
                        break;
                    case Constants.CERTIFICATE:
                        identityProviderListItem.setCertificate(createIDPCertificate(idp));
                        break;
                    case Constants.ALIAS:
                        identityProviderListItem.setAlias(idp.getAlias());
                        break;
                    case Constants.CLAIMS:
                        identityProviderListItem.setClaims(createClaimResponse(idp.getClaimConfig()));
                        break;
                    case Constants.ROLES:
                        identityProviderListItem.setRoles(createRoleResponse(idp));
                        break;
                    case Constants.FEDERATED_AUTHENTICATORS:
                        identityProviderListItem.setFederatedAuthenticators(createFederatedAuthenticatorResponse(idp));
                        break;
                    case Constants.PROVISIONING:
                        identityProviderListItem.setProvisioning(createProvisioningResponse(idp));
                        break;
                    default:
                        if (log.isDebugEnabled()) {
                            log.debug("Unknown requested attribute: " + requestedAttribute);
                        }
                        break;
                }
            }
        }
        return identityProviderListItem;
    }

    private List<Link> createLinks(String url, int limit, int offset, int total, String filter) {

        List<Link> links = new ArrayList<>();

        // Next Link
        if (limit > 0 && offset >= 0 && (offset + limit) < total) {
            links.add(buildPageLink(new StringBuilder(url), Constants.PAGE_LINK_REL_NEXT, (offset +
                    limit), limit, filter));
        }

        // Previous Link
        // Previous link matters only if offset and limit are greater than 0.
        if (offset > 0 && limit > 0) {
            if ((offset - limit) >= 0) { // A previous page of size 'limit' exists
                links.add(buildPageLink(new StringBuilder(url), Constants.PAGE_LINK_REL_PREVIOUS,
                        calculateOffsetForPreviousLink(offset, limit, total), limit, filter));
            } else { // A previous page exists but it's size is less than the specified limit
                links.add(buildPageLink(new StringBuilder(url), Constants.PAGE_LINK_REL_PREVIOUS, 0, offset, filter));
            }
        }

        return links;
    }

    private int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        int newOffset = (offset - limit);
        if (newOffset < total) {
            return newOffset;
        }

        return calculateOffsetForPreviousLink(newOffset, limit, total);
    }

    private Link buildPageLink(StringBuilder url, String rel, int offset, int limit, String filter) {

        if (StringUtils.isNotBlank(filter)) {
            try {
                url.append(String.format(Constants.PAGINATION_WITH_FILTER_LINK_FORMAT, offset, limit, URLEncoder
                        .encode(filter, StandardCharsets.UTF_8.name())));
            } catch (UnsupportedEncodingException e) {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                        .ERROR_CODE_BUILDING_LINKS, "Unable to url-encode filter: " + filter);
            }
        } else {
            url.append(String.format(Constants.PAGINATION_LINK_FORMAT, offset, limit));
        }
        return new Link().rel(rel).href(ContextLoader.buildURIForBody((url.toString())).toString());
    }

    private IdentityProviderResponse createIDPResponse(IdentityProvider identityProvider) {

        IdentityProviderResponse idpResponse = new IdentityProviderResponse();
        populateIDPBasicInfo(idpResponse, identityProvider);
        idpResponse.setCertificate(createIDPCertificate(identityProvider));
        idpResponse.setClaims(createClaimResponse(identityProvider.getClaimConfig()));
        idpResponse.setRoles(createRoleResponse(identityProvider));
        idpResponse.setFederatedAuthenticators(createFederatedAuthenticatorResponse(identityProvider));
        idpResponse.setProvisioning(createProvisioningResponse(identityProvider));
        return idpResponse;
    }

    private void populateIDPBasicInfo(IdentityProviderResponse idpResponse, IdentityProvider identityProvider) {

        idpResponse.setId(identityProvider.getResourceId());
        idpResponse.setIsEnabled(identityProvider.isEnable());
        idpResponse.setIsPrimary(identityProvider.isPrimary());
        idpResponse.setName(identityProvider.getIdentityProviderName());
        idpResponse.setDescription(identityProvider.getIdentityProviderDescription());
        idpResponse.setAlias(identityProvider.getAlias());
        idpResponse.setImage(identityProvider.getImageUrl());
        idpResponse.setIsFederationHub(identityProvider.isFederationHub());
        idpResponse.setHomeRealmIdentifier(identityProvider.getHomeRealmId());
    }

    private Certificate createIDPCertificate(IdentityProvider identityProvider) {

        Certificate certificate = null;
        IdentityProviderProperty[] idpProperties = identityProvider.getIdpProperties();
        for (IdentityProviderProperty property : idpProperties) {
            if (Constants.JWKS_URI.equals(property.getName())) {
                certificate = new Certificate().jwksUri(property.getValue());
                break;
            }
        }
        if (certificate == null && ArrayUtils.isNotEmpty(identityProvider.getCertificateInfoArray())) {
            List<String> certificates = new ArrayList<>();
            for (CertificateInfo certInfo : identityProvider.getCertificateInfoArray()) {
                certificates.add(certInfo.getCertValue());
            }
            certificate = new Certificate().certificates(certificates);
        }
        return certificate;
    }

    private Claims createClaimResponse(ClaimConfig claimConfig) {

        Claims apiClaims = new Claims();
        List<org.wso2.carbon.identity.api.server.idp.v1.model.ClaimMapping> apiMappings = new ArrayList<>();
        List<ProvisioningClaim> provClaims = new ArrayList<>();

        if (claimConfig != null) {
            if (claimConfig.getClaimMappings() != null) {
                for (ClaimMapping mapping : claimConfig.getClaimMappings()) {
                    org.wso2.carbon.identity.api.server.idp.v1.model.ClaimMapping apiMapping = new org.wso2.carbon
                            .identity.api.server.idp.v1.model.ClaimMapping();

                    Claim localClaim = new Claim();
                    localClaim.setId(base64URLEncode(mapping.getLocalClaim().getClaimUri()));
                    localClaim.setUri(mapping.getLocalClaim().getClaimUri());
                    localClaim.setDisplayName(getDisplayNameOfLocalClaim(mapping.getLocalClaim().getClaimUri()));
                    apiMapping.setLocalClaim(localClaim);
                    // As the provisioning claims are added as claim mappings without any remote claim internally, we
                    // need to validate this here.
                    if (StringUtils.isNotBlank(mapping.getRemoteClaim().getClaimUri())) {
                        apiMapping.setIdpClaim(mapping.getRemoteClaim().getClaimUri());
                        apiMappings.add(apiMapping);
                    }

                    if (StringUtils.isNotBlank(mapping.getDefaultValue()) && mapping.isRequested()) {
                        ProvisioningClaim provClaimResponse = new ProvisioningClaim();
                        Claim provClaim = new Claim();
                        if (StringUtils.isNotBlank(mapping.getRemoteClaim().getClaimUri())) {
                            provClaim.setUri(mapping.getRemoteClaim().getClaimUri());
                        } else {
                            provClaim.setId(base64URLEncode(mapping.getLocalClaim().getClaimUri()));
                            provClaim.setUri(mapping.getLocalClaim().getClaimUri());
                            provClaim.setDisplayName(getDisplayNameOfLocalClaim(mapping.getLocalClaim().getClaimUri()));
                        }
                        provClaimResponse.setClaim(provClaim);
                        provClaimResponse.setDefaultValue(mapping.getDefaultValue());
                        provClaims.add(provClaimResponse);
                    }
                }
            }

            Claim roleClaim = new Claim();
            if (getLocalClaim(claimConfig.getRoleClaimURI()) != null) {
                roleClaim.setId(base64URLEncode(claimConfig.getRoleClaimURI()));
                roleClaim.setDisplayName(getDisplayNameOfLocalClaim(claimConfig.getRoleClaimURI()));
            }
            roleClaim.setUri(claimConfig.getRoleClaimURI());
            apiClaims.setRoleClaim(roleClaim);

            Claim userIdClaim = new Claim();
            if (getLocalClaim(claimConfig.getUserClaimURI()) != null) {
                userIdClaim.setId(base64URLEncode(claimConfig.getUserClaimURI()));
                userIdClaim.setDisplayName(getDisplayNameOfLocalClaim(claimConfig.getUserClaimURI()));
            }
            userIdClaim.setUri(claimConfig.getUserClaimURI());
            apiClaims.setUserIdClaim(userIdClaim);
        }

        apiClaims.setMappings(apiMappings);
        apiClaims.setProvisioningClaims(provClaims);
        return apiClaims;
    }

    private Roles createRoleResponse(IdentityProvider identityProvider) {

        PermissionsAndRoleConfig permissionsAndRoleConfig = identityProvider.getPermissionAndRoleConfig();
        Roles roleConfig = new Roles();

        List<org.wso2.carbon.identity.api.server.idp.v1.model.RoleMapping> apiRoleMappings = new ArrayList<>();

        if (permissionsAndRoleConfig != null) {
            if (permissionsAndRoleConfig.getRoleMappings() != null) {
                for (RoleMapping roleMapping : permissionsAndRoleConfig.getRoleMappings()) {
                    org.wso2.carbon.identity.api.server.idp.v1.model.RoleMapping apiRoleMapping = new org.wso2.carbon
                            .identity.api.server.idp.v1.model.RoleMapping();
                    apiRoleMapping.setIdpRole(roleMapping.getRemoteRole());
                    apiRoleMapping.setLocalRole(IdentityUtil.addDomainToName(roleMapping
                            .getLocalRole().getLocalRoleName(), roleMapping.getLocalRole().getUserStoreId()));
                    apiRoleMappings.add(apiRoleMapping);
                }
            }
        }
        roleConfig.setMappings(apiRoleMappings);

        String provRoles = identityProvider.getProvisioningRole();
        if (StringUtils.isNotBlank(provRoles)) {
            roleConfig.setOutboundProvisioningRoles(Arrays.asList(provRoles.split(",")));
        }
        return roleConfig;
    }

    private FederatedAuthenticatorListResponse createFederatedAuthenticatorResponse(IdentityProvider idp) {

        FederatedAuthenticatorConfig[] fedAuthConfigs = idp.getFederatedAuthenticatorConfigs();
        FederatedAuthenticatorListResponse fedAuthIDPResponse = new FederatedAuthenticatorListResponse();
        List<FederatedAuthenticatorListItem> authenticators = new ArrayList<>();
        for (FederatedAuthenticatorConfig fedAuthConfig : fedAuthConfigs) {
            FederatedAuthenticatorListItem fedAuthListItem = new FederatedAuthenticatorListItem();
            fedAuthListItem.setAuthenticatorId(base64URLEncode(fedAuthConfig.getName()));
            fedAuthListItem.setName(fedAuthConfig.getName());
            fedAuthListItem.setIsEnabled(fedAuthConfig.isEnabled());
            fedAuthListItem.setSelf(
                    ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                                    "/%s/federated-authenticators/%s", idp.getResourceId(),
                            base64URLEncode(fedAuthConfig.getName())))
                            .toString());
            authenticators.add(fedAuthListItem);
        }
        fedAuthIDPResponse.setDefaultAuthenticatorId(idp.getDefaultAuthenticatorConfig() != null ? base64URLEncode(idp
                .getDefaultAuthenticatorConfig().getName()) : null);
        fedAuthIDPResponse.setAuthenticators(authenticators);
        return fedAuthIDPResponse;
    }

    private ProvisioningResponse createProvisioningResponse(IdentityProvider idp) {

        ProvisioningResponse provisioningResponse = new ProvisioningResponse();
        provisioningResponse.setJit(createJITResponse(idp));
        provisioningResponse.setOutboundConnectors(createOutboundProvisioningResponse(idp));
        return provisioningResponse;
    }

    private OutboundConnectorListResponse createOutboundProvisioningResponse(IdentityProvider idp) {

        ProvisioningConnectorConfig[] connectorConfigs = idp.getProvisioningConnectorConfigs();
        List<OutboundConnectorListItem> connectors = new ArrayList<>();
        if (connectorConfigs != null) {
            for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
                OutboundConnectorListItem connectorListItem = new OutboundConnectorListItem();
                connectorListItem.setConnectorId(base64URLEncode(connectorConfig.getName()));
                connectorListItem.setName(connectorConfig.getName());
                connectorListItem.setIsEnabled(connectorConfig.isEnabled());
                connectorListItem.setSelf(
                        ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                                        "/%s/provisioning/outbound-connectors/%s", idp.getResourceId(),
                                base64URLEncode(connectorConfig.getName())))
                                .toString());
                connectors.add(connectorListItem);
            }
        }
        OutboundConnectorListResponse outboundConnectorListResponse = new OutboundConnectorListResponse();
        outboundConnectorListResponse.setDefaultConnectorId(idp.getDefaultProvisioningConnectorConfig() != null ?
                base64URLEncode(idp.getDefaultProvisioningConnectorConfig().getName()) : null);
        outboundConnectorListResponse.setConnectors(connectors);
        return outboundConnectorListResponse;
    }

    private JustInTimeProvisioning createJITResponse(IdentityProvider idp) {

        JustInTimeProvisioning jitConfig = new JustInTimeProvisioning();
        if (idp.getJustInTimeProvisioningConfig() != null) {
            jitConfig.setIsEnabled(idp.getJustInTimeProvisioningConfig().isProvisioningEnabled());

            if (idp.getJustInTimeProvisioningConfig().isProvisioningEnabled()) {
                boolean modifyUsername = idp.getJustInTimeProvisioningConfig().isModifyUserNameAllowed();
                boolean passwordProvision = idp.getJustInTimeProvisioningConfig().isPasswordProvisioningEnabled();
                boolean promptConsent = idp.getJustInTimeProvisioningConfig().isPromptConsent();
                if (modifyUsername && passwordProvision && promptConsent) {
                    jitConfig.setScheme(JustInTimeProvisioning.SchemeEnum.PROMPT_USERNAME_PASSWORD_CONSENT);
                } else if (passwordProvision && promptConsent) {
                    jitConfig.setScheme(JustInTimeProvisioning.SchemeEnum.PROMPT_PASSWORD_CONSENT);
                } else if (promptConsent) {
                    jitConfig.setScheme(JustInTimeProvisioning.SchemeEnum.PROMPT_CONSENT);
                } else {
                    jitConfig.setScheme(JustInTimeProvisioning.SchemeEnum.PROVISION_SILENTLY);
                }
                jitConfig.setUserstore(idp.getJustInTimeProvisioningConfig().getProvisioningUserStore());
            }
        }
        return jitConfig;
    }

    private Function<SubProperty, MetaProperty> subPropertyToExternalMeta = property -> {

        MetaProperty metaSubProperty = new MetaProperty();
        metaSubProperty.setKey(property.getName());
        metaSubProperty.setType(getMetaPropertyType(property.getType()));
        metaSubProperty.setIsMandatory(property.isRequired());
        metaSubProperty.setIsConfidential(property.isConfidential());
        metaSubProperty.setDescription(property.getDescription());
        metaSubProperty.setDisplayName(property.getDisplayName());
        metaSubProperty.setDisplayOrder(property.getDisplayOrder());
        metaSubProperty.setRegex(property.getRegex() != null ? property.getRegex() : ".*");
        metaSubProperty.setOptions(Arrays.asList(property.getOptions()));
        metaSubProperty.setDefaultValue(property.getDefaultValue() != null ? property.getDefaultValue() : "");
        return metaSubProperty;
    };

    private Function<Property, MetaProperty> propertyToExternalMeta = property -> {

        MetaProperty metaProperty = new MetaProperty();
        metaProperty.setKey(property.getName());
        metaProperty.setType(getMetaPropertyType(property.getType()));
        metaProperty.setIsMandatory(property.isRequired());
        metaProperty.setIsConfidential(property.isConfidential());
        metaProperty.setDescription(property.getDescription());
        metaProperty.setDisplayName(property.getDisplayName());
        metaProperty.setDisplayOrder(property.getDisplayOrder());
        metaProperty.setRegex(property.getRegex() != null ? property.getRegex() : ".*");
        metaProperty.setOptions(Arrays.asList(property.getOptions()));
        metaProperty.setDefaultValue(property.getDefaultValue() != null ? property.getDefaultValue() : "");
        List<MetaProperty> metaSubProperties = Arrays.stream(property.getSubProperties()).map(subPropertyToExternalMeta)
                .collect(Collectors.toList());
        metaProperty.setSubProperties(metaSubProperties);
        return metaProperty;
    };

    /**
     * Get MetaProperty Type Enum for the internal property type.
     *
     * @param propertyType Internal property type.
     * @return MetaProperty.TypeEnum.
     */
    private MetaProperty.TypeEnum getMetaPropertyType(String propertyType) {

        MetaProperty.TypeEnum typeEnum = MetaProperty.TypeEnum.STRING;

        if (StringUtils.isNotBlank(propertyType)) {
            switch (propertyType) {
                case "string":
                    typeEnum = MetaProperty.TypeEnum.STRING;
                    break;
                case "boolean":
                    typeEnum = MetaProperty.TypeEnum.BOOLEAN;
                    break;
                case "integer":
                    typeEnum = MetaProperty.TypeEnum.INTEGER;
                    break;
                default:
                    typeEnum = MetaProperty.TypeEnum.STRING;
            }
        }
        return typeEnum;
    }

    /**
     * Retrieves display name property of Local claim identified by local claim uri.
     *
     * @param claimUri Local claim uri.
     * @return Display Name.
     */
    private String getDisplayNameOfLocalClaim(String claimUri) {

        LocalClaim localClaim = getLocalClaim(claimUri);
        if (localClaim != null) {
            Map<String, String> localClaimProperties = localClaim.getClaimProperties();
            return localClaimProperties.get(Constants.PROP_DISPLAY_NAME);
        } else {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_LOCAL_CLAIM_ID, claimUri);
        }
    }

    /**
     * Returns internal LocalClaim given local claim URI.
     *
     * @param claimUri URI of the required local claim.
     * @return Local Claim.
     */
    private LocalClaim getLocalClaim(String claimUri) {

        LocalClaim localClaim;
        try {
            List<LocalClaim> localClaimList =
                    IdentityProviderServiceHolder.getClaimMetadataManagementService().getLocalClaims(
                            ContextLoader.getTenantDomainFromContext());

            localClaim = extractLocalClaimFromClaimList(claimUri, localClaimList);
        } catch (ClaimMetadataException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_LOCAL_CLAIM_ID, claimUri);
        }
        return localClaim;
    }

    /**
     * Extracts the LocalClaim corresponding to the given claim uri.
     *
     * @param claimURI  Local claim uri.
     * @param claimList Local claim list.
     * @return LocalClaim.
     */
    private LocalClaim extractLocalClaimFromClaimList(String claimURI, List<LocalClaim> claimList) {

        for (LocalClaim claim : claimList) {
            if (StringUtils.equals(claim.getClaimURI(), claimURI)) {
                return claim;
            }
        }
        return null;
    }

    /**
     * Create a duplicate of the input Identity Provider.
     *
     * @param idP Identity Provider.
     * @return Clone of IDP.
     */
    private IdentityProvider createIdPClone(IdentityProvider idP) {

        try {
            return (IdentityProvider) BeanUtils.cloneBean(idP);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
                e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_IDP, idP.getResourceId());
        }
    }

    /**
     * Creates a clone of IDP's federated authenticator list to be modified during PUT request.
     *
     * @param authenticatorId Federated authenticator resource ID.
     * @param configs         IDP's authenticator configs.
     * @return Clone of authenticator config array.
     */
    private FederatedAuthenticatorConfig[] createFederatedAuthenticatorArrayClone(String authenticatorId,
                                                                                  FederatedAuthenticatorConfig[]
                                                                                          configs) {

        List<FederatedAuthenticatorConfig> cloneList = new ArrayList<>();
        try {
            for (FederatedAuthenticatorConfig config : configs) {
                cloneList.add((FederatedAuthenticatorConfig) BeanUtils.cloneBean(config));
            }
            return cloneList.toArray(new FederatedAuthenticatorConfig[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
                e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_IDP_AUTHENTICATOR, authenticatorId);
        }
    }

    /**
     * Creates a clone of IDP's provisioning connector config list to be modified during PUT request.
     *
     * @param connectorId Provisioning connector resource ID.
     * @param configs     IDP's provisioning connector configs.
     * @return Clone of connector config array.
     */
    private ProvisioningConnectorConfig[] createProvisioningConnectorArrayClone(String connectorId,
                                                                                ProvisioningConnectorConfig[] configs) {

        List<ProvisioningConnectorConfig> cloneList = new ArrayList<>();
        try {
            for (ProvisioningConnectorConfig config : configs) {
                cloneList.add((ProvisioningConnectorConfig) BeanUtils.cloneBean(config));
            }
            return cloneList.toArray(new ProvisioningConnectorConfig[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
                e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_IDP_CONNECTOR, connectorId);
        }
    }

    /**
     * Return the position indicator of an outbound connector from the configured list of provisioning connector
     * configs of an IDP.
     *
     * @param provConnectorConfigs Outbound provisioning connector configs of IDP.
     * @param connectorId          Outbound provisioning connector ID.
     * @return Position of the specified connector in the config array.
     */
    private int getExistingProvConfigPosition(ProvisioningConnectorConfig[] provConnectorConfigs, String connectorId) {

        int configPos = -1;
        if (provConnectorConfigs != null) {
            for (int i = 0; i < provConnectorConfigs.length; i++) {
                if (StringUtils.equals(provConnectorConfigs[i].getName(), base64URLDecode(connectorId))) {
                    configPos = i;
                    break;
                }
            }
        }
        return configPos;
    }

    /**
     * Verify whether the sent connectorId is a supported outbound connector type by the server.
     *
     * @param connectorId Outbound Provisioning Connector ID.
     * @return whether Connector is a supported one by the server.
     * @throws IdentityProviderManagementException IdentityProviderManagementException.
     */
    private boolean isValidConnector(String connectorId) throws IdentityProviderManagementException {

        ProvisioningConnectorConfig[] supportedConnectorConfigs =
                IdentityProviderServiceHolder.getIdentityProviderManager()
                        .getAllProvisioningConnectors();
        if (supportedConnectorConfigs != null) {
            String connectorName = base64URLDecode(connectorId);
            for (ProvisioningConnectorConfig supportedConfig : supportedConnectorConfigs) {
                if (StringUtils.equals(supportedConfig.getName(), connectorName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private OutboundConnectorListItem createOutboundConnectorListItem(String idPId, ProvisioningConnectorConfig
            config) {

        String connectorId = base64URLEncode(config.getName());
        OutboundConnectorListItem listItem = new OutboundConnectorListItem();
        listItem.setConnectorId(connectorId);
        listItem.setName(config.getName());
        listItem.setIsEnabled(config.isEnabled());
        listItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                "/%s/provisioning/outbound-connectors/%s", idPId, connectorId)).toString());
        return listItem;
    }

    /**
     * Create internal provisioning connector config from external outbound connector PUT request.
     *
     * @param outboundConnectorId Outbound provisioning connector resource ID.
     * @param connector           Outbound provisioning connector PUT request.
     * @return Internal Provisioning connector config.
     */
    private ProvisioningConnectorConfig createProvisioningConnectorConfig(String outboundConnectorId,
                                                                          OutboundConnectorPUTRequest connector) {

        ProvisioningConnectorConfig connectorConfig = new ProvisioningConnectorConfig();
        String connectorName = base64URLDecode(outboundConnectorId);
        connectorConfig.setName(connectorName);
        connectorConfig.setEnabled(connector.getIsEnabled());
        connectorConfig.setBlocking(connector.getBlockingEnabled());
        connectorConfig.setRulesEnabled(connector.getRulesEnabled());

        if (connector.getProperties() == null) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_OUTBOUND_PROVISIONING_CONFIG_NOT_FOUND, connectorName);
        }
        List<Property> properties = connector.getProperties().stream()
                .map(propertyToInternal)
                .collect(Collectors.toList());
        connectorConfig.setProvisioningProperties(properties.toArray(new Property[0]));
        return connectorConfig;
    }

    /**
     * Create internal federated authenticator config from external federated authenticator PUT request.
     *
     * @param federatedAuthenticatorId Federated authenticator ID.
     * @param authenticator            Internal federated authenticator config.
     * @return Federated authenticator config of the specified ID.
     */
    private FederatedAuthenticatorConfig createFederatedAuthenticatorConfig(String federatedAuthenticatorId,
                                                                            FederatedAuthenticatorPUTRequest
                                                                                    authenticator) {

        FederatedAuthenticatorConfig authConfig = new FederatedAuthenticatorConfig();
        String authenticatorName = base64URLDecode(federatedAuthenticatorId);
        authConfig.setName(authenticatorName);
        authConfig.setDisplayName(getDisplayNameOfAuthenticator(authenticatorName));
        authConfig.setEnabled(authenticator.getIsEnabled());
        List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> authProperties = authenticator.getProperties();
        if (IdentityApplicationConstants.Authenticator.SAML2SSO.FED_AUTH_NAME.equals(authenticatorName)) {
            validateSamlMetadata(authProperties);
        }
        List<Property> properties = authProperties.stream().map(propertyToInternal).collect(Collectors.toList());
        authConfig.setProperties(properties.toArray(new Property[0]));
        return authConfig;
    }

    /**
     * If selectMode property is set as saml metadata file configuration mode, this function validates whether a
     * valid base-64 encoded SAML metadata file content is provided with the property key 'meta_data_saml'. If found,
     * it will decode the file content and update the value of 'meta_data_saml' property with decoded content.
     *
     * @param samlAuthenticatorProperties Authenticator properties of SAML authenticator.
     */
    private void validateSamlMetadata(List<org.wso2.carbon.identity.api.server.idp.v1.model.Property>
                                              samlAuthenticatorProperties) {

        if (samlAuthenticatorProperties != null) {
            for (org.wso2.carbon.identity.api.server.idp.v1.model.Property property : samlAuthenticatorProperties) {

                if (Constants.SELECT_MODE.equals(property.getKey()) &&
                        Constants.SELECT_MODE_METADATA.equals(property.getValue())) {
                    // SAML metadata file configuration has been selected. Hence we need to validate whether valid SAML
                    // metadata (property with key = 'meta_data_saml') is sent.

                    boolean validMetadataFound = false;
                    String encodedData = null;
                    int positionOfMetadataKey = -1;

                    for (int i = 0; i < samlAuthenticatorProperties.size(); i++) {
                        if (Constants.META_DATA_SAML.equals(samlAuthenticatorProperties.get(i).getKey()) &&
                                StringUtils.isNotBlank
                                        (samlAuthenticatorProperties.get(i).getValue())) {
                            validMetadataFound = true;
                            encodedData = samlAuthenticatorProperties.get(i).getValue();
                            positionOfMetadataKey = i;
                        }
                    }
                    if (validMetadataFound) {
                        String metadata = base64Decode(encodedData);
                        // Add decoded data to property list.
                        org.wso2.carbon.identity.api.server.idp.v1.model.Property metadataProperty =
                                samlAuthenticatorProperties.get(positionOfMetadataKey);
                        metadataProperty.setValue(metadata);
                        samlAuthenticatorProperties.set(positionOfMetadataKey, metadataProperty);
                    } else {
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_SAML_METADATA, null);
                    }
                }
            }
        }
    }

    /**
     * Verify whether the sent authenticatorId is a supported authenticator type by the server.
     *
     * @param federatedAuthenticatorId Federated Authenticator ID.
     * @return whether Authenticator is a supported one by the server.
     * @throws IdentityProviderManagementException IdentityProviderManagementException.
     */
    private boolean isValidAuthenticator(String federatedAuthenticatorId) throws
            IdentityProviderManagementException {

        FederatedAuthenticatorConfig[] supportedAuthConfigs = IdentityProviderServiceHolder.getIdentityProviderManager()
                .getAllFederatedAuthenticators();
        if (supportedAuthConfigs != null) {
            String authenticatorName = base64URLDecode(federatedAuthenticatorId);
            for (FederatedAuthenticatorConfig supportedConfig : supportedAuthConfigs) {
                if (StringUtils.equals(supportedConfig.getName(), authenticatorName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return the position indicator of a federated authenticator from the configured list of federated authenticator
     * configs of an IDP.
     *
     * @param fedAuthConfigs           Federated authenticator config array.
     * @param federatedAuthenticatorId Authenticator ID.
     * @return Position of the authenticator identified by federatedAuthenticatorId in the array.
     */
    private int getExistingAuthConfigPosition(FederatedAuthenticatorConfig[] fedAuthConfigs, String
            federatedAuthenticatorId) {

        // configPos used to identify if the federated authenticator is already configured for the IDP.
        int configPos = -1;
        if (fedAuthConfigs != null) {
            for (int i = 0; i < fedAuthConfigs.length; i++) {
                if (StringUtils
                        .equals(fedAuthConfigs[i].getName(), base64URLDecode(federatedAuthenticatorId))) {
                    configPos = i;
                    break;
                }
            }
        }
        return configPos;
    }

    /**
     * Create API Federated Authenticator model using internal FederatedAuthenticatorConfig.
     *
     * @param authenticatorId  Federated Authenticator ID.
     * @param identityProvider Identity Provider information.
     * @return FederatedAuthenticator.
     */
    private FederatedAuthenticator createFederatedAuthenticator(String authenticatorId,
                                                                IdentityProvider identityProvider) {

        FederatedAuthenticatorConfig[] authConfigs = identityProvider.getFederatedAuthenticatorConfigs();
        if (ArrayUtils.isEmpty(authConfigs)) {
            return null;
        }
        FederatedAuthenticatorConfig config = null;
        boolean isDefaultAuthenticator = false;
        String authenticatorName = base64URLDecode(authenticatorId);
        for (FederatedAuthenticatorConfig authConfig : authConfigs) {
            if (StringUtils.equals(authConfig.getName(), authenticatorName)) {
                config = authConfig;
            }
        }
        if (identityProvider.getDefaultAuthenticatorConfig() != null && StringUtils.equals(identityProvider
                .getDefaultAuthenticatorConfig().getName(), authenticatorName)) {
            isDefaultAuthenticator = true;
        }
        FederatedAuthenticator federatedAuthenticator = new FederatedAuthenticator();
        if (config != null) {
            federatedAuthenticator.setAuthenticatorId(authenticatorId);
            federatedAuthenticator.setName(config.getName());
            federatedAuthenticator.setIsEnabled(config.isEnabled());
            federatedAuthenticator.setIsDefault(isDefaultAuthenticator);
            List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                    Arrays.stream(config.getProperties()).map(propertyToExternal).collect(Collectors.toList());
            federatedAuthenticator.setProperties(properties);
        }
        return federatedAuthenticator;
    }

    /**
     * Create external OutboundConnector from Provisioning Config.
     *
     * @param connectorId      Outbound provisioning connector resource ID.
     * @param identityProvider Identity Provider information.
     * @return External outbound connector.
     */
    private OutboundConnector createOutboundConnector(String connectorId, IdentityProvider identityProvider) {

        ProvisioningConnectorConfig[] connectorConfigs = identityProvider.getProvisioningConnectorConfigs();
        if (ArrayUtils.isEmpty(connectorConfigs)) {
            return null;
        }
        ProvisioningConnectorConfig config = null;
        boolean isDefaultConnector = false;
        String connectorName = base64URLDecode(connectorId);
        for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
            if (StringUtils.equals(connectorConfig.getName(), connectorName)) {
                config = connectorConfig;
            }
        }
        if (identityProvider.getDefaultProvisioningConnectorConfig() != null && StringUtils.equals(identityProvider
                .getDefaultProvisioningConnectorConfig().getName(), connectorName)) {
            isDefaultConnector = true;
        }

        OutboundConnector outboundConnector = null;
        if (config != null) {
            outboundConnector = new OutboundConnector();
            outboundConnector.setConnectorId(connectorId);
            outboundConnector.setName(config.getName());
            outboundConnector.setIsEnabled(config.isEnabled());
            outboundConnector.setIsDefault(isDefaultConnector);
            outboundConnector.setBlockingEnabled(config.isBlocking());
            outboundConnector.setRulesEnabled(config.isRulesEnabled());
            List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                    Arrays.stream(config
                            .getProvisioningProperties()).map(propertyToExternal)
                            .collect(Collectors.toList());
            outboundConnector.setProperties(properties);
        }
        return outboundConnector;
    }

    /**
     * Evaluate the list of patch operations and update the root level attributes of the identity provider accordingly.
     *
     * @param patchRequest List of patch operations.
     * @param idpToUpdate  Identity Provider to be updated.
     */
    private void processPatchRequest(List<Patch> patchRequest, IdentityProvider idpToUpdate) {

        if (CollectionUtils.isEmpty(patchRequest)) {
            return;
        }
        for (Patch patch : patchRequest) {
            String path = patch.getPath();
            Patch.OperationEnum operation = patch.getOperation();
            String value = patch.getValue();
            // We support only 'ADD', 'REPLACE' and 'REMOVE' patch operations.
            if (operation == Patch.OperationEnum.REPLACE) {
                if (path.matches(Constants.CERTIFICATE_PATH_REGEX) && path.split(Constants.PATH_SEPERATOR).length ==
                        4) {
                    List<String> certificates = new ArrayList<>();
                    int index = Integer.parseInt(path.split(Constants.PATH_SEPERATOR)[3]);
                    if (ArrayUtils.isNotEmpty(idpToUpdate.getCertificateInfoArray()) && (index >= 0)
                            && (index < idpToUpdate.getCertificateInfoArray().length)) {
                        for (CertificateInfo certInfo : idpToUpdate.getCertificateInfoArray()) {
                            certificates.add(certInfo.getCertValue());
                        }
                        certificates.set(index, value);
                        idpToUpdate.setCertificate(StringUtils.join(certificates, ""));
                    } else {
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, null);
                    }
                } else {
                    switch (path) {
                        case Constants.NAME_PATH:
                            idpToUpdate.setIdentityProviderName(value);
                            break;
                        case Constants.DESCRIPTION_PATH:
                            idpToUpdate.setIdentityProviderDescription(value);
                            break;
                        case Constants.IMAGE_PATH:
                            idpToUpdate.setImageUrl(value);
                            break;
                        case Constants.IS_PRIMARY_PATH:
                            idpToUpdate.setPrimary(Boolean.parseBoolean(value));
                            break;
                        case Constants.IS_ENABLED_PATH:
                            idpToUpdate.setEnable(Boolean.parseBoolean(value));
                            break;
                        case Constants.IS_FEDERATION_HUB_PATH:
                            idpToUpdate.setFederationHub(Boolean.parseBoolean(value));
                            break;
                        case Constants.HOME_REALM_PATH:
                            idpToUpdate.setHomeRealmId(value);
                            break;
                        case Constants.ALIAS_PATH:
                            idpToUpdate.setAlias(value);
                            break;
                        case Constants.CERTIFICATE_JWKSURI_PATH:
                            List<IdentityProviderProperty> idpProperties = new ArrayList<>();
                            IdentityProviderProperty jwksProperty = new IdentityProviderProperty();
                            jwksProperty.setName(Constants.JWKS_URI);
                            jwksProperty.setValue(value);
                            idpProperties.add(jwksProperty);
                            idpToUpdate.setIdpProperties(idpProperties.toArray(new IdentityProviderProperty[0]));
                            break;
                        default:
                            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                    .ERROR_CODE_INVALID_INPUT, null);
                    }
                }
            } else if (operation == Patch.OperationEnum.ADD && path.matches(Constants.CERTIFICATE_PATH_REGEX) && path
                    .split(Constants.PATH_SEPERATOR).length == 4) {
                List<String> certificates = new ArrayList<>();
                int index = Integer.parseInt(path.split(Constants.PATH_SEPERATOR)[3]);
                if (index < 0) {
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Invalid index in 'path' attribute");
                }
                if (ArrayUtils.isNotEmpty(idpToUpdate.getCertificateInfoArray())) {
                    if (index > idpToUpdate.getCertificateInfoArray().length) {
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Invalid index in 'path' attribute");
                    }
                    for (CertificateInfo certInfo : idpToUpdate.getCertificateInfoArray()) {
                        certificates.add(certInfo.getCertValue());
                    }
                }
                certificates.add(index, value);
                idpToUpdate.setCertificate(StringUtils.join(certificates, ""));
            } else if (operation == Patch.OperationEnum.REMOVE && path.matches(Constants.CERTIFICATE_PATH_REGEX) &&
                    path.split(Constants.PATH_SEPERATOR).length == 4) {
                List<String> certificates = new ArrayList<>();
                int index = Integer.parseInt(path.split(Constants.PATH_SEPERATOR)[3]);
                if (ArrayUtils.isNotEmpty(idpToUpdate.getCertificateInfoArray()) && (index >= 0) && index <
                        idpToUpdate.getCertificateInfoArray().length) {
                    for (CertificateInfo certInfo : idpToUpdate.getCertificateInfoArray()) {
                        certificates.add(certInfo.getCertValue());
                    }
                    certificates.remove(index);
                } else {
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Invalid index in 'path' attribute");
                }
                idpToUpdate.setCertificate(StringUtils.join(certificates, ""));
            } else {
                // Throw an error if any other patch operations are sent in the request.
                throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                        .ERROR_CODE_INVALID_INPUT, null);
            }
        }
    }

    /**
     * Base64-decode content.
     *
     * @param encodedContent Encoded message content.
     * @return Decoded value.
     */
    private String base64Decode(String encodedContent) {

        return new String(Base64.getDecoder().decode(encodedContent), (StandardCharsets.UTF_8));
    }

    /**
     * Handle IdentityProviderManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityProviderManagementException
     * @param errorEnum Error Message information.
     * @return APIError.
     */
    private APIError handleIdPException(IdentityProviderManagementException e,
                                        Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());

        Response.Status status;

        if (e instanceof IdentityProviderManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof IdentityProviderManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Constant.ErrorMessage.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }
}
