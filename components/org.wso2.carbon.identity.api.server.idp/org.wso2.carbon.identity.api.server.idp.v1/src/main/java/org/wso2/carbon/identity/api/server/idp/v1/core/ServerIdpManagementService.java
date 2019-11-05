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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.idp.common.Constant;
import org.wso2.carbon.identity.api.server.idp.common.Util;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claim;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderGetResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.PatchDocument;
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
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementServerException;
import org.wso2.carbon.idp.mgt.util.IdPManagementConstants;

import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURI;
import static org.wso2.carbon.identity.api.server.idp.common.Constant.IDP_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.v1.model.PatchDocument.OperationEnum;

/**
 * Call internal osgi services to perform server identity provider related operations.
 */
public class ServerIdpManagementService {

    private static final Log log = LogFactory.getLog(ServerIdpManagementService.class);

    /**
     * Get list of identity providers.
     *
     * @param attributes Required attributes in the IDP list response.
     * @param limit      Items per page.
     * @param offset     Offset.
     * @param filter     Filter string. E.g. filter="name" sw "google" and "isEnabled" eq "true"
     * @param sortBy     Attribute to sort the IDPs by. E.g. name
     * @param sortOrder  Order in which IDPs should be sorted. Can be either ASC or DESC.
     * @return IdentityProviderListResponse.
     */
    public IdentityProviderListResponse getIDPs(String attributes, Integer limit, Integer offset, String filter, String
            sortBy, String sortOrder) {

        // TODO: 11/4/19 check for null for Integer values
        handleNotImplementedCapabilities(attributes, limit, offset, filter, sortBy, sortOrder);
        try {
            return createIDPListResponse(Util.getIdentityProviderManager().getIdPs(ContextLoader
                    .getTenantDomainFromContext()));
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDPS);
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
            identityProvider = Util.getIdentityProviderManager().addIdPWithResourceId(
                    createIDP(identityProviderPOSTRequest), ContextLoader.getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP);
        }
        return createIDPResponse(identityProvider);
    }

    /**
     * Get an identity provider identified by resource ID.
     *
     * @param idpId IdP resource ID.
     * @return IdentityProviderGetResponse.
     */
    public IdentityProviderGetResponse getIDP(String idpId) {

        try {
            return createIDPGetResponse(Util.getIdentityProviderManager().getIdPByResourceId(idpId,
                    ContextLoader.getTenantDomainFromContext(), true));
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP);
        }
    }

    /**
     * Update an identity provider.
     *
     * @param identityProviderPUTRequest identityProviderPUTRequest.
     * @return IdentityProviderResponse.
     */
    public IdentityProviderResponse updateIDP(String identityProviderId, IdentityProviderPUTRequest
            identityProviderPUTRequest) {

        IdentityProvider identityProvider;
        try {
            identityProvider = Util.getIdentityProviderManager().updateIdPByResourceId(identityProviderId,
                    createIDP(identityProviderPUTRequest),
                    ContextLoader.getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP);
        }
        return createIDPResponse(identityProvider);
    }

    /**
     * Updates only root level attributes of IDP.
     *
     * @param identityProviderId Identity Provider resource ID.
     * @param patchDocument      Patch request in Json Patch notation See
     *                           <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf
     *                           .org/html/rfc6902</a>
     */
    public void patchIDP(String identityProviderId, List<PatchDocument> patchDocument) {

        try {
            IdentityProvider identityProvider = Util.getIdentityProviderManager().getIdPByResourceId(identityProviderId,
                    ContextLoader.getTenantDomainFromContext(), true);
            if (identityProvider != null) {
                IdentityProvider idpToUpdate = createIdPClone(identityProvider);
                for (PatchDocument patch : patchDocument) {
                    String path = patch.getPath();
                    OperationEnum operation = patch.getOperation();
                    // We support only 'REPLACE' patch operation.
                    if (operation == OperationEnum.REPLACE) {
                        String value = null;
                        if (patch.getValue() instanceof String) {
                            value = (String) patch.getValue();
                        }
                        switch (path) {
                            case Constant.NAME_PATH:
                                idpToUpdate.setIdentityProviderName(value);
                                break;
                            case Constant.DESCRIPTION_PATH:
                                idpToUpdate.setIdentityProviderDescription(value);
                                break;
                            case Constant.IMAGE_PATH:
                                idpToUpdate.setImageUrl(value);
                                break;
                            case Constant.IS_PRIMARY_PATH:
                                idpToUpdate.setPrimary(Boolean.parseBoolean(value));
                                break;
                            case Constant.IS_ENABLED_PATH:
                                idpToUpdate.setEnable(Boolean.parseBoolean(value));
                                break;
                            case Constant.IS_FEDERATION_HUB_PATH:
                                idpToUpdate.setFederationHub(Boolean.parseBoolean(value));
                                break;
                            case Constant.HOME_REALM_PATH:
                                idpToUpdate.setHomeRealmId(value);
                                break;
                            case Constant.PROPERTIES_PATH:
                                if (patch.getValue() instanceof ArrayList) {

                                    List list = (ArrayList) patch.getValue();
                                    List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties = new
                                            ArrayList<>();
                                    for (Object object : list) {
                                        if (object instanceof LinkedHashMap && ((LinkedHashMap) object).get("key") !=
                                                null && ((LinkedHashMap) object).get("value") != null) {

                                            org.wso2.carbon.identity.api.server.idp.v1.model.Property property =
                                                    new org.wso2.carbon
                                                            .identity.api.server.idp.v1.model.Property();
                                            LinkedHashMap map = (LinkedHashMap) object;
                                            property.setKey((String) map.get("key"));
                                            property.setValue((String) map.get("value"));
                                            properties.add(property);
                                        }
                                    }
                                    modifyIdPProperties(idpToUpdate, properties);
                                }
                                break;
                            default:
                                throw handleException(Response.Status.BAD_REQUEST, Constant.ErrorMessage
                                        .ERROR_CODE_INVALID_INPUT);
                        }
                    } else {
                        throw handleException(Response.Status.BAD_REQUEST, Constant.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT);
                    }
                }
                Util.getIdentityProviderManager().updateIdPByResourceId(identityProviderId, idpToUpdate,
                        ContextLoader.getTenantDomainFromContext());
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP);
        }
    }

    /**
     * Delete an Identity Provider.
     *
     * @param identityProviderId Identity Provider resource ID.
     * @param force              when force is set to true, IDP will be forcefully deleted even-though it is being
     *                           referred by service providers.
     */
    public void deleteIDP(String identityProviderId, boolean force) {

        try {
            if (force) {
                Util.getIdentityProviderManager().forceDeleteIdpByResourceId(identityProviderId, ContextLoader
                        .getTenantDomainFromContext());
            } else {
                Util.getIdentityProviderManager().deleteIdPByResourceId(identityProviderId, ContextLoader
                        .getTenantDomainFromContext());
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_IDP);
        }
    }

    /**
     * Get meta information about Identity Provider's federated authenticators.
     *
     * @return array of meta federated authenticators.
     */
    public MetaFederatedAuthenticatorListItem[] getMetaFederatedAuthenticators() {

        MetaFederatedAuthenticatorListItem[] metaAuthenticators = null;
        try {
            FederatedAuthenticatorConfig[] authenticatorConfigs = Util.getIdentityProviderManager()
                    .getAllFederatedAuthenticators();
            if (ArrayUtils.isNotEmpty(authenticatorConfigs)) {
                metaAuthenticators = new MetaFederatedAuthenticatorListItem[authenticatorConfigs.length];
                int i = 0;
                for (FederatedAuthenticatorConfig authenticatorConfig : authenticatorConfigs) {
                    MetaFederatedAuthenticatorListItem metaFederatedAuthenticator =
                            createMetaFederatedAuthenticatorListItem(authenticatorConfig);
                    metaAuthenticators[i++] = metaFederatedAuthenticator;
                }
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATORS);
        }
        return metaAuthenticators;
    }

    /**
     * Get meta information about a specific federated authenticator supported by the IDPs.
     *
     * @param id Federated authenticator ID.
     * @return MetaFederatedAuthenticator.
     */
    public MetaFederatedAuthenticator getMetaFederatedAuthenticator(String id) {

        String authenticatorName = base64UrlDecodeId(id);
        MetaFederatedAuthenticator authenticator = null;
        try {
            FederatedAuthenticatorConfig[] authenticatorConfigs = Util.getIdentityProviderManager()
                    .getAllFederatedAuthenticators();
            if (authenticatorConfigs != null) {
                for (FederatedAuthenticatorConfig authenticatorConfig : authenticatorConfigs) {
                    if (StringUtils.equals(authenticatorConfig.getName(), authenticatorName)) {
                        authenticator = createMetaFederatedAuthenticator(authenticatorConfig);
                        break;
                    }
                }
            }
            return authenticator;
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATOR);
        }
    }

    /**
     * Get meta information about Identity Provider's outbound provisioning connectors.
     *
     * @return array of meta outbound provisioning connectors.
     */
    public MetaOutboundConnectorListItem[] getMetaOutboundConnectors() {

        MetaOutboundConnectorListItem[] metaOutboundConnectors = null;
        try {
            ProvisioningConnectorConfig[] connectorConfigs = Util.getIdentityProviderManager()
                    .getAllProvisioningConnectors();
            if (ArrayUtils.isNotEmpty(connectorConfigs)) {
                metaOutboundConnectors = new MetaOutboundConnectorListItem[connectorConfigs.length];
                int i = 0;
                for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
                    MetaOutboundConnectorListItem metaOutboundConnector = createMetaOutboundConnectorListItem
                            (connectorConfig);
                    metaOutboundConnectors[i++] = metaOutboundConnector;
                }
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_CONNECTORS);

        }
        return metaOutboundConnectors;
    }

    /**
     * Get meta information about a specific outbound provisioning connector supported by the IDPs.
     *
     * @param id Outbound Provisioning Connector ID.
     * @return MetaOutboundConnector.
     */
    public MetaOutboundConnector getMetaOutboundConnector(String id) {

        String connectorName = base64UrlDecodeId(id);
        MetaOutboundConnector connector = null;
        try {
            ProvisioningConnectorConfig[] connectorConfigs = Util.getIdentityProviderManager()
                    .getAllProvisioningConnectors();
            if (connectorConfigs != null) {
                for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
                    if (StringUtils.equals(connectorConfig.getName(), connectorName)) {
                        connector = createMetaOutboundConnector(connectorConfig);
                        break;
                    }
                }
            }
            return connector;
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_META_CONNECTOR);
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
            IdentityProvider idP = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);

            if (idP != null) {
                listResponse = new FederatedAuthenticatorListResponse();
                FederatedAuthenticatorConfig[] fedAuthConfigs = idP.getFederatedAuthenticatorConfigs();
                if (fedAuthConfigs != null) {
                    List<FederatedAuthenticatorListItem> fedAuthList = new ArrayList<>();
                    String defaultAuthenticator = null;
                    for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
                        String fedAuthId = base64UrlEncodeId(config.getName());
                        FederatedAuthenticatorListItem listItem = new FederatedAuthenticatorListItem();
                        listItem.setAuthenticatorId(fedAuthId);
                        listItem.setName(config.getName());
                        listItem.setIsEnabled(config.isEnabled());
                        listItem.setAuthenticator(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                                "/%s/federated-authenticators/%s", idpId, fedAuthId)).toString());
                        fedAuthList.add(listItem);
                        if (idP.getDefaultAuthenticatorConfig() != null) {
                            defaultAuthenticator = base64UrlEncodeId(idP.getDefaultAuthenticatorConfig().getName());
                        }
                    }
                    listResponse.setDefaultAuthenticatorId(defaultAuthenticator);
                    listResponse.setAuthenticators(fedAuthList);
                }
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATORS);
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
            IdentityProvider idp = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idp != null) {
                FederatedAuthenticatorConfig[] fedAuthConfigs = idp.getFederatedAuthenticatorConfigs();
                if (fedAuthConfigs != null) {
                    for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
                        if (StringUtils.equals(config.getName(), base64UrlDecodeId(authenticatorId))) {
                            return createFederatedAuthenticator(authenticatorId, config);
                        }
                    }
                }
                throw handleException(Response.Status.NOT_FOUND,
                        Constant.ErrorMessage.ERROR_CODE_AUTHENTICATOR_NOT_FOUND_FOR_IDP);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATOR);
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
    public FederatedAuthenticatorResponse updateFederatedAuthenticator(String idpId, String federatedAuthenticatorId,
                                                                       FederatedAuthenticatorPUTRequest authenticator) {

        try {
            IdentityProvider idp = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idp != null) {
                // Need to create a clone, since modifying the fields of the original object, will modify the cached
                // IDP object.
                IdentityProvider idpToUpdate = createIdPClone(idp);

                // Create new FederatedAuthenticatorConfig to store the federated authenticator information.
                FederatedAuthenticatorConfig authConfig = createFederatedAuthenticator(federatedAuthenticatorId,
                        authenticator);
                FederatedAuthenticatorConfig[] fedAuthConfigs = idp.getFederatedAuthenticatorConfigs();
                int configPos = getExistingAuthConfigPosition(fedAuthConfigs, federatedAuthenticatorId);
                // If configPos != -1, modify the existing authenticatorConfig of IDP.
                if (configPos != -1) {
                    fedAuthConfigs[configPos] = authConfig;
                } else {
                    // If configPos is -1 add new authenticator to the list.
                    if (isAuthenticatorValid(federatedAuthenticatorId)) {
                        List<FederatedAuthenticatorConfig> authConfigList = new ArrayList<>(fedAuthConfigs != null ?
                                Arrays.asList(fedAuthConfigs) : new ArrayList<>());
                        authConfigList.add(authConfig);
                        fedAuthConfigs = authConfigList.toArray(new FederatedAuthenticatorConfig[0]);
                    } else {
                        throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage
                                .ERROR_CODE_AUTHENTICATOR_NOT_FOUND_FOR_IDP);
                    }
                }
                idpToUpdate.setFederatedAuthenticatorConfigs(fedAuthConfigs);

                // If no default authenticator has been set previously, mark this as default.
                if (idpToUpdate.getDefaultAuthenticatorConfig() == null) {
                    idpToUpdate.setDefaultAuthenticatorConfig(authConfig);
                }
                Util.getIdentityProviderManager().updateIdPByResourceId(idpId, idpToUpdate, ContextLoader
                        .getTenantDomainFromContext());
                return createFederatedAuthenticatorResponse(idpId, federatedAuthenticatorId, authConfig);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_AUTHENTICATOR);
        }
    }

    /**
     * Get outbound provisioning connectors of a specific Identity Provider
     *
     * @param idpId Identity Provider resource ID.
     * @return OutboundConnectorListResponse.
     */
    public OutboundConnectorListResponse getOutboundConnectors(String idpId) {

        try {
            IdentityProvider idp = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idp != null) {
                OutboundConnectorListResponse listResponse = null;
                String defaultConnectorId = null;

                ProvisioningConnectorConfig[] connectorConfigs = idp.getProvisioningConnectorConfigs();
                if (connectorConfigs != null) {
                    listResponse = new OutboundConnectorListResponse();
                    List<OutboundConnectorListItem> connectorList = new ArrayList<>();
                    for (ProvisioningConnectorConfig config : connectorConfigs) {
                        connectorList.add(createOutboundConnectorListItem(idpId, config));
                        if (idp.getDefaultProvisioningConnectorConfig() != null) {
                            defaultConnectorId = base64UrlEncodeId(idp.getDefaultProvisioningConnectorConfig()
                                    .getName());
                        }
                    }
                    listResponse.setDefaultConnectorId(defaultConnectorId);
                    listResponse.setConnectors(connectorList);
                }
                return listResponse;
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTORS);
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
            IdentityProvider idp = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idp != null) {
                ProvisioningConnectorConfig[] connectorConfigs = idp.getProvisioningConnectorConfigs();
                if (connectorConfigs != null) {
                    for (ProvisioningConnectorConfig config : connectorConfigs) {
                        if (StringUtils.equals(config.getName(), base64UrlDecodeId(connectorId))) {
                            return createOutboundConnector(config);
                        }
                    }
                }
                throw handleException(Response.Status.NOT_FOUND,
                        Constant.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND_FOR_IDP);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTOR);
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
    public OutboundConnectorResponse updateOutboundConnector(String idpId, String connectorId,
                                                             OutboundConnectorPUTRequest
                                                                     outboundConnector) {

        try {
            IdentityProvider idp = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idp != null) {
                IdentityProvider idpToUpdate = createIdPClone(idp);
                ProvisioningConnectorConfig connectorConfig = createProvisioningConnectorConfig(connectorId,
                        outboundConnector);

                ProvisioningConnectorConfig[] provConnectorConfigs = idp.getProvisioningConnectorConfigs();
                int configPos = getExistingProvConfigPosition(provConnectorConfigs, connectorId);
                if (configPos != -1) {
                    provConnectorConfigs[configPos] = connectorConfig;
                } else {
                    // if configPos is -1 add new authenticator to the list.
                    if (isConnectorValid(connectorId)) {
                        List<ProvisioningConnectorConfig> connectorConfigsList = new ArrayList<>(provConnectorConfigs !=
                                null ? Arrays.asList(provConnectorConfigs) : new ArrayList<>());
                        connectorConfigsList.add(connectorConfig);
                        provConnectorConfigs = connectorConfigsList.toArray(new ProvisioningConnectorConfig[0]);
                    } else {
                        throw handleException(Response.Status.NOT_FOUND,
                                Constant.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND_FOR_IDP);
                    }
                }
                idpToUpdate.setProvisioningConnectorConfigs(provConnectorConfigs);

                // If no default provisioning connector has been set previously, mark this as default.
                if (idpToUpdate.getDefaultProvisioningConnectorConfig() == null) {
                    idpToUpdate.setDefaultProvisioningConnectorConfig(connectorConfig);
                }

                Util.getIdentityProviderManager().updateIdPByResourceId(idpId, idpToUpdate, ContextLoader
                        .getTenantDomainFromContext());
                return createOutboundConnectorResponse(connectorConfig, connectorId, idpId);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_CONNECTOR);
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
                    Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider != null) {
                return createClaimResponse(identityProvider.getClaimConfig());
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_CLAIMS);
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
            IdentityProvider idP = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idP != null) {
                updateClaims(idP, claims);
                IdentityProvider updatedIdP = Util.getIdentityProviderManager().updateIdPByResourceId(idpId,
                        idP, ContextLoader.getTenantDomainFromContext());
                return createClaimResponse(updatedIdP.getClaimConfig());
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_CLAIMS);
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
                    Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider != null) {
                return createRoleResponse(identityProvider);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_ROLES);
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
            IdentityProvider idP = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idP != null) {
                updateRoles(idP, roles);

                IdentityProvider updatedIdP = Util.getIdentityProviderManager().updateIdPByResourceId(idpId,
                        idP, ContextLoader.getTenantDomainFromContext());
                return createRoleResponse(updatedIdP);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_ROLES);
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
                    Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider != null) {
                return createProvisioningResponse(identityProvider);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_PROVISIONING);
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
                    Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                            .getTenantDomainFromContext(), true);
            if (identityProvider != null) {
                return createJITResponse(identityProvider);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDP_JIT);
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
            IdentityProvider idP = Util.getIdentityProviderManager().getIdPByResourceId(idpId, ContextLoader
                    .getTenantDomainFromContext(), true);
            if (idP != null) {
                updateJIT(idP, justInTimeProvisioningConfig);

                IdentityProvider updatedIdP = Util.getIdentityProviderManager().updateIdPByResourceId(idpId,
                        idP, ContextLoader.getTenantDomainFromContext());
                return createJITResponse(updatedIdP);
            } else {
                throw handleException(Response.Status.NOT_FOUND, Constant.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_IDP_JIT);
        }
    }

    // Private Util Methods.

    private MetaFederatedAuthenticatorListItem createMetaFederatedAuthenticatorListItem(FederatedAuthenticatorConfig
                                                                                                authenticatorConfig) {

        MetaFederatedAuthenticatorListItem metaFederatedAuthenticator = new MetaFederatedAuthenticatorListItem();
        String authenticatorId = base64UrlEncodeId(authenticatorConfig.getName());
        metaFederatedAuthenticator.setName(authenticatorConfig.getName());
        metaFederatedAuthenticator.setAuthenticatorId(authenticatorId);
        metaFederatedAuthenticator.setMetaAuthenticator(buildURI(String.format(V1_API_PATH_COMPONENT +
                IDP_PATH_COMPONENT +
                "/meta/federated-authenticators/%s", authenticatorId)).toString());
        return metaFederatedAuthenticator;
    }

    private MetaFederatedAuthenticator createMetaFederatedAuthenticator(FederatedAuthenticatorConfig
                                                                                authenticatorConfig) {

        MetaFederatedAuthenticator metaFederatedAuthenticator = new MetaFederatedAuthenticator();
        metaFederatedAuthenticator.setName(authenticatorConfig.getName());
        metaFederatedAuthenticator.setAuthenticatorId(base64UrlEncodeId(authenticatorConfig.getName()));
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
        String connectorId = base64UrlEncodeId(connectorConfig.getName());
        metaOutboundProvisioningConnector.setConnectorId(connectorId);
        metaOutboundProvisioningConnector.setMetaConnector(buildURI(String.format(V1_API_PATH_COMPONENT +
                IDP_PATH_COMPONENT + "/meta/outbound-provisioning-connectors/%s", connectorId)).toString());
        return metaOutboundProvisioningConnector;
    }

    private MetaOutboundConnector createMetaOutboundConnector(ProvisioningConnectorConfig
                                                                      connectorConfig) {

        MetaOutboundConnector metaOutboundProvisioningConnector = new MetaOutboundConnector();
        metaOutboundProvisioningConnector.setName(connectorConfig.getName());
        metaOutboundProvisioningConnector.setConnectorId(base64UrlEncodeId(connectorConfig.getName()));
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
                authConfig.setName(base64UrlDecodeId(authenticator.getAuthenticatorId()));
                authConfig.setDisplayName(getDisplayNameOfAuthenticator(authConfig.getName()));
                authConfig.setEnabled(authenticator.getIsEnabled());
                List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> authProperties =
                        authenticator.getProperties();
                if (IdentityApplicationConstants.Authenticator.SAML2SSO.FED_AUTH_NAME.equals(authConfig.getName())) {
                    validateSamlMetadata(authProperties);
                }
                List<Property> properties = authProperties.stream()
                        .map(propertyToInternal)
                        .collect(Collectors.toList());
                authConfig.setProperties(properties.toArray(new Property[0]));
                fedAuthConfigs.add(authConfig);

                if (StringUtils.equals(defaultAuthenticator, authenticator.getAuthenticatorId())) {
                    defaultAuthConfig = authConfig;
                }
            }

            idp.setFederatedAuthenticatorConfigs(fedAuthConfigs.toArray(new FederatedAuthenticatorConfig[0]));
            idp.setDefaultAuthenticatorConfig(defaultAuthConfig);
        }
    }

    private String getDisplayNameOfAuthenticator(String authenticatorName) {

        try {
            FederatedAuthenticatorConfig[] authenticatorConfigs = Util.getIdentityProviderManager()
                    .getAllFederatedAuthenticators();
            for (FederatedAuthenticatorConfig config : authenticatorConfigs) {

                if (StringUtils.equals(config.getName(), authenticatorName)) {
                    return config.getDisplayName();
                }
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP);
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
                connectorConfig.setName(base64UrlDecodeId(connector.getConnectorId()));
                connectorConfig.setEnabled(connector.getIsEnabled());

                List<Property> properties = connector.getProperties().stream()
                        .map(propertyToInternal)
                        .collect(Collectors.toList());
                connectorConfig.setProvisioningProperties(properties.toArray(new Property[0]));
                connectorConfigs.add(connectorConfig);

                if (StringUtils.equals(defaultConnectorId, connector.getConnectorId())) {
                    defaultConnectorConfig = connectorConfig;
                }
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
                    String localClaimUri = base64UrlDecodeId(mapping.getLocalClaim().getId());

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

                    if (StringUtils.equals(mapping.getLocalClaim().getId(), claims.getUserIdClaim().getId())) {
                        claimConfig.setUserClaimURI(idpClaimUri);
                    }
                    if (StringUtils.equals(mapping.getLocalClaim().getId(), claims.getRoleClaim().getId())) {
                        claimConfig.setRoleClaimURI(idpClaimUri);
                    }
                }
            } else {
                claimConfig.setLocalClaimDialect(true);
                claimConfig.setUserClaimURI(base64UrlDecodeId(claims.getUserIdClaim().getId()));
                claimConfig.setRoleClaimURI(base64UrlDecodeId(claims.getRoleClaim().getId()));
            }
            List<ProvisioningClaim> provClaims = claims.getProvisioningClaims();
            for (ProvisioningClaim provClaim : provClaims) {
                String localClaimUri = base64UrlDecodeId(provClaim.getClaim().getId());
                if (CollectionUtils.isNotEmpty(claims.getMappings())) {
                    for (ClaimMapping internalMapping : claimMappings) {

                        if (StringUtils.equals(localClaimUri, internalMapping.getLocalClaim().getClaimUri())) {
                            internalMapping.setDefaultValue(provClaim.getDefaultValue());
                            internalMapping.setRequested(true);
                        }
                    }
                } else {
                    ClaimMapping internalMapping = new ClaimMapping();

                    org.wso2.carbon.identity.application.common.model.Claim localClaim = new org.wso2.carbon.identity
                            .application.common.model.Claim();
                    localClaim.setClaimUri(localClaimUri);
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

    private Function<IdentityProviderProperty, org.wso2.carbon.identity.api.server.idp.v1.model.Property>
            idPPropertyToExternal
            = property -> {

        org.wso2.carbon.identity.api.server.idp.v1.model.Property apiProperty = new org.wso2.carbon.identity.api
                .server.idp.v1.model.Property();
        apiProperty.setKey(property.getName());
        apiProperty.setValue(property.getValue());
        return apiProperty;
    };

    private Function<org.wso2.carbon.identity.api.server.idp.v1.model.Property, IdentityProviderProperty>
            propertyToInternalIdP
            = property -> {

        IdentityProviderProperty idPProperty = new IdentityProviderProperty();
        idPProperty.setValue(property.getValue());
        idPProperty.setName(property.getKey());
        return idPProperty;
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

        List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                identityProviderPOSTRequest.getProperties();
        List<IdentityProviderProperty> idpProperties = new ArrayList<>();
        if (properties != null) {
            for (org.wso2.carbon.identity.api.server.idp.v1.model.Property property : properties) {
                IdentityProviderProperty idpProperty = new IdentityProviderProperty();
                idpProperty.setName(property.getKey());
                idpProperty.setValue(property.getValue());
                idpProperties.add(idpProperty);
            }
        }
        if (StringUtils.isNotBlank(idpJWKSUri)) {
            IdentityProviderProperty jwksProperty = new IdentityProviderProperty();
            jwksProperty.setName(Constant.JWKS_URI);
            jwksProperty.setValue(idpJWKSUri);
            idpProperties.add(jwksProperty);
        }
        idp.setIdpProperties(idpProperties.toArray(new IdentityProviderProperty[0]));
        return idp;
    }

    private IdentityProviderListResponse createIDPListResponse(List<IdentityProvider> idps) {

        IdentityProviderListResponse listResponse = new IdentityProviderListResponse();
        List<IdentityProviderListItem> identityProviderListItem = new ArrayList<>();
        for (IdentityProvider idp : idps) {

            IdentityProviderListItem listItem = new IdentityProviderListItem();
            listItem.setId(idp.getResourceId());
            listItem.setName(idp.getIdentityProviderName());
            listItem.setDescription(idp.getIdentityProviderDescription());
            listItem.setIsEnabled(idp.isEnable());
            listItem.setImage(idp.getImageUrl());
            listItem.setIdentityProvider(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + "/%s",
                    idp.getResourceId())).toString());
            identityProviderListItem.add(listItem);
        }
        listResponse.setIdentityProviders(identityProviderListItem);
        listResponse.setLinks(null);
        listResponse.setTotalResults(50);
        listResponse.setCount(identityProviderListItem.size());
        listResponse.setStartIndex(1);

        return listResponse;
    }

    private IdentityProviderResponse createIDPResponse(IdentityProvider identityProvider) {

        IdentityProviderResponse idpResponse = new IdentityProviderResponse();
        if (identityProvider != null) {
            populateIDPResponse(idpResponse, identityProvider);
            idpResponse
                    .setSelf(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + "/%s", identityProvider
                            .getResourceId())).toString());
        }
        return idpResponse;
    }

    private IdentityProviderGetResponse createIDPGetResponse(IdentityProvider identityProvider) {

        IdentityProviderGetResponse idpResponse = new IdentityProviderGetResponse();

        if (identityProvider != null) {
            populateIDPResponse(idpResponse, identityProvider);
        }
        return idpResponse;
    }

    private void populateIDPResponse(IdentityProviderGetResponse idpResponse, IdentityProvider identityProvider) {

        idpResponse.setId(identityProvider.getResourceId());
        idpResponse.setIsEnabled(identityProvider.isEnable());
        idpResponse.setIsPrimary(identityProvider.isPrimary());
        idpResponse.setName(identityProvider.getIdentityProviderName());
        idpResponse.setDescription(identityProvider.getIdentityProviderDescription());
        idpResponse.setAlias(identityProvider.getAlias());
        idpResponse.setImage(identityProvider.getImageUrl());
        idpResponse.setIsFederationHub(identityProvider.isFederationHub());
        idpResponse.setHomeRealmIdentifier(identityProvider.getHomeRealmId());
        Certificate certificate = null;
        IdentityProviderProperty[] idpProperties = identityProvider.getIdpProperties();
        for (IdentityProviderProperty property : idpProperties) {
            if (Constant.JWKS_URI.equals(property.getName())) {
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
        idpResponse.setCertificate(certificate);

        idpResponse.setClaims(createClaimResponse(identityProvider.getClaimConfig()));
        idpResponse.setRoles(createRoleResponse(identityProvider));
        idpResponse.setFederatedAuthenticators(createFederatedAuthenticatorResponse(identityProvider));
        idpResponse.setProvisioning(createProvisioningResponse(identityProvider));
        List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties = Arrays.stream(identityProvider
                .getIdpProperties()).map(idPPropertyToExternal).collect(Collectors.toList());
        idpResponse.setProperties(properties);
    }

    private Claims createClaimResponse(ClaimConfig claimConfig) {

        Claims apiClaims = new Claims();
        List<org.wso2.carbon.identity.api.server.idp.v1.model.ClaimMapping> apiMappings = new ArrayList<>();
        List<ProvisioningClaim> provClaims = new ArrayList<>();

        if (claimConfig.getClaimMappings() != null) {
            for (ClaimMapping mapping : claimConfig.getClaimMappings()) {
                org.wso2.carbon.identity.api.server.idp.v1.model.ClaimMapping apiMapping = new org.wso2.carbon
                        .identity.api.server.idp.v1.model.ClaimMapping();

                Claim localClaim = new Claim();
                localClaim.setId(base64UrlEncodeId(mapping.getLocalClaim().getClaimUri()));
                localClaim.setUri(mapping.getLocalClaim().getClaimUri());
                LocalClaim localClaim1 = getLocalClaim(mapping.getLocalClaim().getClaimUri());
                Map<String, String> localClaim1Properties = localClaim1.getClaimProperties();
                localClaim.setDisplayName(localClaim1Properties.get(Constant.PROP_DISPLAY_NAME));
                apiMapping.setLocalClaim(localClaim);
                apiMapping.setIdpClaim(mapping.getRemoteClaim().getClaimUri());
                apiMappings.add(apiMapping);

                if (StringUtils.equals(mapping.getRemoteClaim().getClaimUri(), claimConfig.getRoleClaimURI())) {
                    Claim roleClaim = new Claim();
                    String roleClaimUri = mapping.getLocalClaim().getClaimUri();
                    roleClaim.setId(base64UrlEncodeId(roleClaimUri));
                    roleClaim.setUri(roleClaimUri);
                    LocalClaim localRoleClaim = getLocalClaim(roleClaimUri);
                    Map<String, String> roleClaimProperties = localRoleClaim.getClaimProperties();
                    roleClaim.setDisplayName(roleClaimProperties.get(Constant.PROP_DISPLAY_NAME));
                    apiClaims.setRoleClaim(roleClaim);

                } else if (StringUtils.equals(mapping.getRemoteClaim().getClaimUri(), claimConfig.getUserClaimURI())) {
                    Claim userIdClaim = new Claim();
                    String userIdClaimUri = mapping.getLocalClaim().getClaimUri();
                    userIdClaim.setId(base64UrlEncodeId(userIdClaimUri));
                    userIdClaim.setUri(userIdClaimUri);
                    LocalClaim localUserIdClaim = getLocalClaim(userIdClaimUri);
                    Map<String, String> userIdClaimProperties = localUserIdClaim.getClaimProperties();
                    userIdClaim.setDisplayName(userIdClaimProperties.get(Constant.PROP_DISPLAY_NAME));
                    apiClaims.setUserIdClaim(userIdClaim);
                }

                if (StringUtils.isNotBlank(mapping.getDefaultValue()) && mapping.isRequested()) {
                    ProvisioningClaim provClaim = new ProvisioningClaim();
                    provClaim.setClaim(localClaim);
                    provClaim.setDefaultValue(mapping.getDefaultValue());
                    provClaims.add(provClaim);
                }
            }
        } else {
            Claim roleClaim = new Claim();
            roleClaim.setId(base64UrlEncodeId(claimConfig.getRoleClaimURI()));
            roleClaim.setUri(claimConfig.getRoleClaimURI());
            LocalClaim localRoleClaim = getLocalClaim(claimConfig.getRoleClaimURI());
            Map<String, String> roleClaimProperties = localRoleClaim.getClaimProperties();
            roleClaim.setDisplayName(roleClaimProperties.get(Constant.PROP_DISPLAY_NAME));
            apiClaims.setRoleClaim(roleClaim);

            Claim userIdClaim = new Claim();
            userIdClaim.setId(base64UrlEncodeId(claimConfig.getUserClaimURI()));
            userIdClaim.setUri(claimConfig.getUserClaimURI());
            LocalClaim localUserIdClaim = getLocalClaim(claimConfig.getUserClaimURI());
            Map<String, String> userIdClaimProperties = localUserIdClaim.getClaimProperties();
            userIdClaim.setDisplayName(userIdClaimProperties.get(Constant.PROP_DISPLAY_NAME));
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
        String provRoles = identityProvider.getProvisioningRole();
        roleConfig.setMappings(apiRoleMappings);
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
            fedAuthListItem.setAuthenticatorId(base64UrlEncodeId(fedAuthConfig.getName()));
            fedAuthListItem.setName(fedAuthConfig.getName());
            fedAuthListItem.setIsEnabled(fedAuthConfig.isEnabled());
            fedAuthListItem.setAuthenticator(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                    "/%s/federated-authenticators/%s", idp.getResourceId(), base64UrlEncodeId(fedAuthConfig.getName())))
                    .toString());
            authenticators.add(fedAuthListItem);
        }
        fedAuthIDPResponse.setDefaultAuthenticatorId(idp.getDefaultAuthenticatorConfig() != null ? base64UrlEncodeId(idp
                .getDefaultAuthenticatorConfig().getName()) : null);
        fedAuthIDPResponse.setAuthenticators(authenticators);
        return fedAuthIDPResponse;
    }

    private ProvisioningResponse createProvisioningResponse(IdentityProvider idp) {

        ProvisioningResponse provisioningResponse = new ProvisioningResponse();
        provisioningResponse.setJit(createJITResponse(idp));
        ProvisioningConnectorConfig[] connectorConfigs = idp.getProvisioningConnectorConfigs();
        List<OutboundConnectorListItem> connectors = new ArrayList<>();
        for (ProvisioningConnectorConfig connectorConfig : connectorConfigs) {
            OutboundConnectorListItem connectorListItem = new OutboundConnectorListItem();
            connectorListItem.setConnectorId(base64UrlEncodeId(connectorConfig.getName()));
            connectorListItem.setName(connectorConfig.getName());
            connectorListItem.setIsEnabled(connectorConfig.isEnabled());
            connectorListItem.setConnector(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                            "/%s/provisioning/outbound-connectors/%s", idp.getResourceId(),
                    base64UrlEncodeId(connectorConfig.getName())))
                    .toString());
            connectors.add(connectorListItem);
        }
        OutboundConnectorListResponse outboundProvisioningGetResponse = new OutboundConnectorListResponse();
        outboundProvisioningGetResponse.setDefaultConnectorId(idp.getDefaultProvisioningConnectorConfig() != null ?
                base64UrlEncodeId(idp.getDefaultProvisioningConnectorConfig().getName()) : null);
        outboundProvisioningGetResponse.setConnectors(connectors);
        return provisioningResponse;
    }

    private JustInTimeProvisioning createJITResponse(IdentityProvider idp) {

        JustInTimeProvisioning jitConfig = new JustInTimeProvisioning();
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
            jitConfig.setUserstore(idp.getJustInTimeProvisioningConfig().getUserStoreClaimUri());
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
        metaSubProperty.setRegex(property.getRegex());
        metaSubProperty.setOptions(Arrays.asList(property.getOptions()));
        metaSubProperty.setDefaultValue(property.getDefaultValue());
        return metaSubProperty;
    };

    private Function<Property, MetaProperty> propertyToExternalMeta
            = property -> {

        MetaProperty metaProperty = new MetaProperty();
        metaProperty.setKey(property.getName());
        metaProperty.setType(getMetaPropertyType(property.getType()));
        metaProperty.setIsMandatory(property.isRequired());
        metaProperty.setIsConfidential(property.isConfidential());
        metaProperty.setDescription(property.getDescription());
        metaProperty.setDisplayName(property.getDisplayName());
        metaProperty.setDisplayOrder(property.getDisplayOrder());
        metaProperty.setRegex(property.getRegex());
        metaProperty.setOptions(Arrays.asList(property.getOptions()));
        metaProperty.setDefaultValue(property.getDefaultValue());
        List<MetaProperty> metaSubProperties = Arrays.stream(property.getSubProperties()).map(subPropertyToExternalMeta)
                .collect(Collectors.toList());
        metaProperty.setSubProperties(metaSubProperties);
        return metaProperty;
    };

    private MetaProperty.TypeEnum getMetaPropertyType(String property) {

        MetaProperty.TypeEnum typeEnum = null;

        if (StringUtils.isNotBlank(property)) {
            switch (property) {
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
                    typeEnum = null;
            }
        }
        return typeEnum;
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
            List<LocalClaim> localClaimList = Util.getClaimMetadataManagementService().getLocalClaims(
                    ContextLoader.getTenantDomainFromContext());

            localClaim = extractLocalClaimFromClaimList(claimUri, localClaimList);
        } catch (ClaimMetadataException e) {
            throw handleException(Response.Status.BAD_REQUEST, Constant.ErrorMessage.ERROR_CODE_INVALID_LOCAL_CLAIM_ID);
        }
        return localClaim;
    }

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

        IdentityProvider idPClone = new IdentityProvider();
        idPClone.setResourceId(idP.getResourceId());
        idPClone.setId(idP.getId());
        idPClone.setIdentityProviderName(idP.getIdentityProviderName());
        idPClone.setAlias(idP.getAlias());
        idPClone.setPrimary(idP.isPrimary());
        idPClone.setHomeRealmId(idP.getHomeRealmId());
        idPClone.setIdentityProviderDescription(idP.getIdentityProviderDescription());
        idPClone.setImageUrl(idP.getImageUrl());
        idPClone.setCertificate(idP.getCertificate());
        idPClone.setFederationHub(idP.isFederationHub());
        idPClone.setFederatedAuthenticatorConfigs(idP.getFederatedAuthenticatorConfigs());
        idPClone.setProvisioningConnectorConfigs(idP.getProvisioningConnectorConfigs());
        idPClone.setDefaultProvisioningConnectorConfig(idP.getDefaultProvisioningConnectorConfig());
        idPClone.setDefaultAuthenticatorConfig(idP.getDefaultAuthenticatorConfig());
        idPClone.setClaimConfig(idP.getClaimConfig());
        idPClone.setProvisioningRole(idP.getProvisioningRole());
        idPClone.setJustInTimeProvisioningConfig(idP.getJustInTimeProvisioningConfig());
        idPClone.setPermissionAndRoleConfig(idP.getPermissionAndRoleConfig());
        idPClone.setEnable(idP.isEnable());
        idPClone.setIdpProperties(idP.getIdpProperties());
        return idPClone;
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
                if (StringUtils.equals(provConnectorConfigs[i].getName(), base64UrlDecodeId(connectorId))) {
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
    private boolean isConnectorValid(String connectorId) throws IdentityProviderManagementException {

        ProvisioningConnectorConfig[] supportedConnectorConfigs = Util.getIdentityProviderManager()
                .getAllProvisioningConnectors();
        if (supportedConnectorConfigs != null) {
            String connectorName = base64UrlDecodeId(connectorId);
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

        String connectorId = base64UrlEncodeId(config.getName());
        OutboundConnectorListItem listItem = new OutboundConnectorListItem();
        listItem.setConnectorId(connectorId);
        listItem.setName(config.getName());
        listItem.setIsEnabled(config.isEnabled());
        listItem.setConnector(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
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
        String connectorName = base64UrlDecodeId(outboundConnectorId);
        connectorConfig.setName(connectorName);
        connectorConfig.setEnabled(connector.getIsEnabled());
        connectorConfig.setBlocking(connector.getBlockingEnabled());
        connectorConfig.setRulesEnabled(connector.getRulesEnabled());
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
    private FederatedAuthenticatorConfig createFederatedAuthenticator(String federatedAuthenticatorId,
                                                                      FederatedAuthenticatorPUTRequest authenticator) {

        FederatedAuthenticatorConfig authConfig = new FederatedAuthenticatorConfig();
        String authenticatorName = base64UrlDecodeId(federatedAuthenticatorId);
        authConfig.setName(authenticatorName);
        authConfig.setDisplayName(getDisplayNameOfAuthenticator(authenticatorName));
        authConfig.setEnabled(authenticator.getIsEnabled());
        List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> authProperties = authenticator.getProperties();
        if (IdentityApplicationConstants.Authenticator.SAML2SSO.FED_AUTH_NAME.equals(authenticatorName)) {
            validateSamlMetadata(authProperties);
        }
        List<Property> properties = authProperties.stream()
                .map(propertyToInternal)
                .collect(Collectors.toList());
        authConfig.setProperties(properties.toArray(new Property[0]));
        return authConfig;
    }

    private void validateSamlMetadata(List<org.wso2.carbon.identity.api.server.idp.v1.model.Property>
                                              samlAuthenticatorProperties) {

        if (samlAuthenticatorProperties != null) {
            for (org.wso2.carbon.identity.api.server.idp.v1.model.Property property : samlAuthenticatorProperties) {

                if (Constant.SELECT_MODE.equals(property.getKey()) &&
                        Constant.SELECT_MODE_METADATA.equals(property.getValue())) {
                    // SAML metadata file configuration has been selected. Hence we need to validate whether valid SAML
                    // metadata (property with key = 'meta_data_saml') is sent.

                    boolean validMetadataFound = false;
                    String encodedData = null;
                    int positionOfMetadataKey = -1;

                    for (int i = 0; i < samlAuthenticatorProperties.size(); i++) {
                        if (Constant.META_DATA_SAML.equals(samlAuthenticatorProperties.get(i).getKey()) &&
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
                        throw handleException(Response.Status.BAD_REQUEST, Constant.ErrorMessage
                                .ERROR_CODE_INVALID_SAML_METADATA);
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
    private boolean isAuthenticatorValid(String federatedAuthenticatorId) throws
            IdentityProviderManagementException {

        FederatedAuthenticatorConfig[] supportedAuthConfigs = Util.getIdentityProviderManager()
                .getAllFederatedAuthenticators();
        if (supportedAuthConfigs != null) {
            String authenticatorName = base64UrlDecodeId(federatedAuthenticatorId);
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
                        .equals(fedAuthConfigs[i].getName(), base64UrlDecodeId(federatedAuthenticatorId))) {
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
     * @param authenticatorId Federated Authenticator ID.
     * @param config          Federated Authenticator Config.
     * @return FederatedAuthenticator.
     */
    private FederatedAuthenticator createFederatedAuthenticator(String authenticatorId, FederatedAuthenticatorConfig
            config) {

        FederatedAuthenticator federatedAuthenticator = new FederatedAuthenticator();
        federatedAuthenticator.setAuthenticatorId(authenticatorId);
        federatedAuthenticator.setName(config.getName());
        federatedAuthenticator.setIsEnabled(config.isEnabled());
        List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                Arrays.stream(config.getProperties()).map(propertyToExternal).collect(Collectors.toList());
        federatedAuthenticator.setProperties(properties);
        return federatedAuthenticator;
    }

    /**
     * Create external Federated Authenticator Response model using internal FederatedAuthenticatorConfig.
     *
     * @param idPId           Identity Provider resource ID.
     * @param authenticatorId Federated Authenticator ID.
     * @param config          Federated Authenticator Config.
     * @return FederatedAuthenticatorResponse.
     */
    private FederatedAuthenticatorResponse createFederatedAuthenticatorResponse(String idPId, String authenticatorId,
                                                                                FederatedAuthenticatorConfig config) {

        FederatedAuthenticatorResponse authenticatorResponse = new FederatedAuthenticatorResponse();
        authenticatorResponse.setAuthenticatorId(authenticatorId);
        authenticatorResponse.setName(config.getName());
        authenticatorResponse.setIsEnabled(config.isEnabled());
        List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                Arrays.stream(config
                        .getProperties()).map(propertyToExternal)
                        .collect(Collectors.toList());
        authenticatorResponse.setProperties(properties);
        authenticatorResponse
                .setSelf(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                        "/%s/federated-authenticators/%s", idPId, authenticatorId)).toString());
        return authenticatorResponse;
    }


    /**
     * Create external OutboundConnector from Provisioning Config.
     *
     * @param config Internal provisioning connector config.
     * @return External outbound connector.
     */
    private OutboundConnector createOutboundConnector(ProvisioningConnectorConfig config) {

        OutboundConnector outboundConnector = null;
        if (config != null) {
            outboundConnector = new OutboundConnector();
            outboundConnector.setConnectorId(base64UrlEncodeId(config.getName()));
            outboundConnector.setName(config.getName());
            outboundConnector.setIsEnabled(config.isEnabled());
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
     * Create external outbound connector response from Provisioning config
     *
     * @param config      Provisioning connector config.
     * @param connectorId Outbound connector resource ID.
     * @param idPId       Identity provider resource ID.
     * @return External outbound connector response for PUT request.
     */
    private OutboundConnectorResponse createOutboundConnectorResponse(ProvisioningConnectorConfig config, String
            connectorId, String idPId) {

        OutboundConnectorResponse connectorResponse = null;
        if (config != null) {
            connectorResponse = new OutboundConnectorResponse();
            connectorResponse.setName(config.getName());
            connectorResponse.setIsEnabled(config.isEnabled());
            connectorResponse.setConnectorId(base64UrlEncodeId(config.getName()));
            connectorResponse.setBlockingEnabled(config.isBlocking());
            connectorResponse.setRulesEnabled(config.isRulesEnabled());
            List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                    Arrays.stream(config
                            .getProvisioningProperties()).map(propertyToExternal)
                            .collect(Collectors.toList());
            connectorResponse.setProperties(properties);
            connectorResponse.setSelf(buildURI(String.format(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT +
                    "/%s/federated-authenticators/%s", idPId, connectorId)).toString());
        }
        return connectorResponse;
    }

    /**
     * Replace IDP property list.
     *
     * @param identityProvider Identity Provider to be updated.
     * @param properties       External property list.
     */
    private void modifyIdPProperties(IdentityProvider identityProvider,
                                     List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties) {

        List<IdentityProviderProperty> idpProperties = properties.stream().map
                (propertyToInternalIdP).collect(Collectors.toList());
        identityProvider.setIdpProperties(idpProperties.toArray(new IdentityProviderProperty[0]));
    }

    /**
     * Base64-URL-encode identifiers.
     *
     * @param id entity identifier.
     * @return Base-64-URL-encoded value.
     */
    private String base64UrlEncodeId(String id) {

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(id.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64-URL-decode identifiers.
     *
     * @param encodedId Encoded identifier.
     * @return Decoded value.
     */
    private String base64UrlDecodeId(String encodedId) {

        return new String(Base64.getUrlDecoder().decode(encodedId.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Base64-encode content.
     *
     * @param content Message content.
     * @return Encoded value.
     */
    private String base64Encode(String content) {

        return new String(Base64.getEncoder().encode(content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Base64-decode content.
     *
     * @param encodedContent Encoded message content.
     * @return Decoded value.
     */
    private String base64Decode(String encodedContent) {

        return new String(Base64.getDecoder().decode(encodedContent.getBytes(StandardCharsets.UTF_8)));
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
                                        Constant.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());

        Response.Status status;

        if (e instanceof IdentityProviderManagementClientException && IdPManagementConstants.ErrorMessage
                .ERROR_CODE_IDP_DOES_NOT_EXIST.getCode().equals(e.getErrorCode())) {
            errorResponse.setCode(e.getErrorCode());
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.NOT_FOUND;
        } else if (e instanceof IdentityProviderManagementClientException && IdPManagementConstants.ErrorMessage
                .ERROR_CODE_IDP_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
            errorResponse.setCode(e.getErrorCode());
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.CONFLICT;
        } else if (e instanceof IdentityProviderManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(Constants.ERROR_CODE_DELIMITER) ?
                        errorCode : Constant.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof IdentityProviderManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(Constants.ERROR_CODE_DELIMITER) ?
                        errorCode : Constant.IDP_MANAGEMENT_PREFIX + errorCode;
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
    private APIError handleException(Response.Status status, Constant.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constant.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(errorMsg.getDescription());
    }

    /**
     * Return Not Implemented error response for IDP List attributes which are not yet supported by the server.
     *
     * @param attributes Attributes query param.
     * @param limit      Limit query param.
     * @param offset     Offset query param.
     * @param filter     Filter string.
     * @param sortBy     SortBy query param.
     * @param sortOrder  SortOrder query param.
     */
    private void handleNotImplementedCapabilities(String attributes, Integer limit, Integer offset, String filter,
                                                  String sortBy, String sortOrder) {

        Constant.ErrorMessage errorEnum = null;

        if (limit != null) {
            errorEnum = Constant.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (offset != null) {
            errorEnum = Constant.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (filter != null) {
            errorEnum = Constant.ErrorMessage.ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
        } else if (sortBy != null) {
            errorEnum = Constant.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        } else if (sortOrder != null) {
            errorEnum = Constant.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        } else if (attributes != null) {
            errorEnum = Constant.ErrorMessage.ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;
            throw new APIError(status, errorResponse);
        }
    }
}