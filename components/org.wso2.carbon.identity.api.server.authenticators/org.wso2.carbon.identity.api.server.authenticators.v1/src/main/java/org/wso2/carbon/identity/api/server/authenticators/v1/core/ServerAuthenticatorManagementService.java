/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.authenticators.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.authenticators.common.Constants;
import org.wso2.carbon.identity.api.server.authenticators.v1.impl.LocalAuthenticatorConfigBuilderFactory;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.ConnectedApp;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Link;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.NameFilter;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.SystemLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorCreation;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.ApplicationAuthenticatorService;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementClientException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementServerException;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtClientException;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtException;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtServerException;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.RequestPathAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedLocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.util.AuthenticatorMgtExceptionBuilder.AuthenticatorMgtError;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.base.AuthenticatorPropertyConstants;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.core.model.OperationNode;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementServerException;
import org.wso2.carbon.idp.mgt.IdpManager;
import org.wso2.carbon.idp.mgt.model.ConnectedAppsResult;
import org.wso2.carbon.idp.mgt.model.IdpSearchResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.authenticators.common.Constants.AUTHENTICATOR_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;

/**
 * Call internal osgi services to perform server authenticators related operations.
 */
public class ServerAuthenticatorManagementService {

    private final ApplicationManagementService applicationManagementService;
    private final IdpManager idpManager;
    private final ApplicationAuthenticatorService applicationAuthenticatorService;
    private static final Log log = LogFactory.getLog(ServerAuthenticatorManagementService.class);

    public ServerAuthenticatorManagementService(ApplicationManagementService applicationManagementService,
                                                IdpManager idpManager,
                                                ApplicationAuthenticatorService applicationAuthenticatorService) {

        this.applicationManagementService = applicationManagementService;
        this.idpManager = idpManager;
        this.applicationAuthenticatorService = applicationAuthenticatorService;
    }

    /**
     * Retrieves the list of available authenticators.
     *
     * @param filter The filter string.
     * @param limit  The items per page. **Not supported at the moment.**
     * @param offset The offset to be used with the limit parameter. **Not supported at the moment.**
     * @return The list of authenticators
     */
    public List<Authenticator> getAuthenticators(String filter, Integer limit, Integer offset) {

        handleNotImplementedCapabilities(limit, offset);

        try {
            String filterAuthenticatorName = null;
            String filterOperationForName = null;
            ArrayList<String> filterTagsList = null;
            int maximumItemPerPage = IdentityUtil.getMaximumItemPerPage();
            if (StringUtils.isNotBlank(filter)) {
                List<ExpressionNode> expressionNodes = getExpressionNodesForAuthenticator(filter);
                if (CollectionUtils.isNotEmpty(expressionNodes)) {
                    NameFilter nameFilter = getFilterAuthenticatorNameAndOperation(expressionNodes);
                    if (nameFilter != null) {
                        filterAuthenticatorName = nameFilter.getName();
                        filterOperationForName = nameFilter.getOperation();
                    }
                    filterTagsList = getFilterTagsList(expressionNodes);
                }
            }

            LocalAuthenticatorConfig[] localAuthenticatorConfigs = applicationManagementService
                    .getAllLocalAuthenticators(ContextLoader.getTenantDomainFromContext());
            int localAuthenticatorsCount = localAuthenticatorConfigs.length;
            RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs = new RequestPathAuthenticatorConfig[0];

            /* If there is no filter string available in the request, the request path authenticators are required to
            be fetched only if the  no. of local authenticators retrieved are less than the maximum items per page
            count as the no. of items returned in the response will be capped at the maximum items per page count. */
            if (StringUtils.isNotBlank(filter) || (StringUtils.isBlank(filter) && localAuthenticatorsCount <
                    maximumItemPerPage)) {
                requestPathAuthenticatorConfigs = applicationManagementService
                        .getAllRequestPathAuthenticators(ContextLoader.getTenantDomainFromContext());
            }

            List<String> requestedAttributeList = new ArrayList<>();
            requestedAttributeList.add(Constants.FEDERATED_AUTHENTICATORS);

            int idPCountToBeRetrieved = maximumItemPerPage - (localAuthenticatorsCount +
                    requestPathAuthenticatorConfigs.length);
            List<IdentityProvider> identityProviders = null;

            /* If there is no filter string available in the request, the identity providers are required to
            be fetched only if the total of local authenticators and request path authenticators retrieved above is
            less than the maximum items per page count as the no. of items returned in the response will be capped
            at the maximum items per page count. */
            if (idPCountToBeRetrieved > 0 && StringUtils.isBlank(filter)) {
                IdpSearchResult idpSearchResult = idpManager.getIdPs(idPCountToBeRetrieved, null, null,
                        null, null, ContextLoader.getTenantDomainFromContext(), requestedAttributeList);
                identityProviders = idpSearchResult.getIdPs();
            }

            return buildAuthenticatorsListResponse(filter, requestedAttributeList, filterAuthenticatorName,
                    filterOperationForName, filterTagsList, localAuthenticatorConfigs, requestPathAuthenticatorConfigs,
                    identityProviders);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_AUTHENTICATORS,
                    null);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDPS, null);
        }
    }

    /**
     * Retrieves the list of tags defined for the authenticators.
     *
     * @return The list of tags.
     */
    public List<String> getTags() {

        try {
            LocalAuthenticatorConfig[] localAuthenticatorConfigs = applicationManagementService
                    .getAllLocalAuthenticators(ContextLoader.getTenantDomainFromContext());

            RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs = applicationManagementService
                    .getAllRequestPathAuthenticators(ContextLoader.getTenantDomainFromContext());

            FederatedAuthenticatorConfig[] federatedAuthenticatorConfigs = idpManager
                    .getAllFederatedAuthenticators(ContextLoader.getTenantDomainFromContext());

            List<UserDefinedLocalAuthenticatorConfig> userDefinedLocalAuthConfigs = applicationAuthenticatorService
                    .getAllUserDefinedLocalAuthenticators(ContextLoader.getTenantDomainFromContext());

            return buildTagsListResponse(localAuthenticatorConfigs, requestPathAuthenticatorConfigs,
                    federatedAuthenticatorConfigs, userDefinedLocalAuthConfigs);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_AUTHENTICATORS,
                    null);
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDPS, null);
        } catch (AuthenticatorMgtException e) {
            throw handleAuthenticatorException(e);
        }
    }

    public ConnectedApps getConnectedAppsOfLocalAuthenticator(String authenticatorId, Integer limit, Integer offset) {

        try {
            ConnectedAppsResult connectedAppsResult = applicationManagementService
                    .getConnectedAppsForLocalAuthenticator(authenticatorId, ContextLoader.getTenantDomainFromContext(),
                            limit, offset);
            return createConnectedAppsResponse(authenticatorId, connectedAppsResult);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTED_APPS, authenticatorId);
        }
    }

    /**
     * Add the user defined local authenticator.
     *
     * @param config          The user defined local authenticator update request.
     * @return The created authenticator.
     */
    public Authenticator addUserDefinedLocalAuthenticator(UserDefinedLocalAuthenticatorCreation config) {

        try {
            UserDefinedLocalAuthenticatorConfig createdConfig = applicationAuthenticatorService
                    .addUserDefinedLocalAuthenticator(
                            LocalAuthenticatorConfigBuilderFactory.build(config),
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return LocalAuthenticatorConfigBuilderFactory.build(createdConfig);
        } catch (AuthenticatorMgtException e) {
            throw handleAuthenticatorException(e);
        }
    }

    /**
     * Deletes the user defined local authenticator.
     *
     * @param authenticatorId The authenticator ID.
     */
    public void deleteUserDefinedLocalAuthenticator(String authenticatorId) {

        try {
            applicationAuthenticatorService.deleteUserDefinedLocalAuthenticator(base64URLDecode(authenticatorId),
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (AuthenticatorMgtException e) {
            throw handleAuthenticatorException(e);
        }
    }

    /**
     * Updates the user defined local authenticator.
     *
     * @param authenticatorId The authenticator ID.
     * @param config          The user defined local authenticator update request.
     * @return The updated authenticator.
     */
    public Authenticator updateUserDefinedLocalAuthenticator(
            String authenticatorId, UserDefinedLocalAuthenticatorUpdate config) {

        try {
            String authenticatorName = base64URLDecode(authenticatorId);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            LocalAuthenticatorConfig existingAuthenticator = applicationAuthenticatorService
                    .getLocalAuthenticatorByName(authenticatorName, tenantDomain);
            if (existingAuthenticator == null) {
                AuthenticatorMgtError error = AuthenticatorMgtError.ERROR_CODE_ERROR_AUTHENTICATOR_NOT_FOUND;
                throw handleAuthenticatorException(new AuthenticatorMgtClientException(error.getCode(),
                                error.getMessage(), String.format(error.getMessage(), authenticatorName)),
                        Response.Status.NOT_FOUND);
            }
            UserDefinedLocalAuthenticatorConfig updatedConfig = applicationAuthenticatorService
                    .updateUserDefinedLocalAuthenticator(
                            LocalAuthenticatorConfigBuilderFactory.build(config, existingAuthenticator),
                            tenantDomain);
            return LocalAuthenticatorConfigBuilderFactory.build(updatedConfig);
        } catch (AuthenticatorMgtException e) {
            throw handleAuthenticatorException(e);
        }
    }

    public SystemLocalAuthenticatorUpdate updateSystemLocalAuthenticator(String authenticatorId,
                                                                         SystemLocalAuthenticatorUpdate systemConfig) {
        try {
            String authenticatorName = base64URLDecode(authenticatorId);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            LocalAuthenticatorConfig existingAuthenticator = applicationAuthenticatorService
                    .getLocalAuthenticatorByName(authenticatorName, tenantDomain);
            if (existingAuthenticator == null) {
                AuthenticatorMgtError error = AuthenticatorMgtError.ERROR_CODE_ERROR_AUTHENTICATOR_NOT_FOUND;
                throw handleAuthenticatorException(new AuthenticatorMgtClientException(error.getCode(),
                                error.getMessage(), String.format(error.getMessage(), authenticatorName)),
                        Response.Status.NOT_FOUND);
            }
//            existingAuthenticator.setAmrValue(systemConfig.getAmrValue());
            LocalAuthenticatorConfig localAuthenticatorConfig = new LocalAuthenticatorConfig();
            localAuthenticatorConfig.setName(systemConfig.getAmrValue());
            LocalAuthenticatorConfig updatedConfig = applicationAuthenticatorService
                    .updateAuthenticatorAmrValue(localAuthenticatorConfig, tenantDomain);

            return LocalAuthenticatorConfigBuilderFactory.buildSystemLocalAuthenticator(updatedConfig);
        } catch (AuthenticatorMgtException e) {
            throw handleAuthenticatorException(e);
        }
    }

    private ConnectedApps createConnectedAppsResponse(String resourceId, ConnectedAppsResult connectedAppsResult) {

        ConnectedApps connectedAppsResponse = new ConnectedApps();
        if (connectedAppsResult == null) {
            return connectedAppsResponse;
        }
        List<ConnectedApp> connectedAppList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(connectedAppsResult.getApps())) {
            for (String appId : connectedAppsResult.getApps()) {
                ConnectedApp listItem = new ConnectedApp();
                listItem.setAppId(appId);
                listItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                        "/applications/%s", appId)).toString());
                connectedAppList.add(listItem);
            }
            connectedAppsResponse.setConnectedApps(connectedAppList);
            connectedAppsResponse.setCount(connectedAppList.size());
        } else {
            connectedAppsResponse.setCount(0);
        }

        connectedAppsResponse.setTotalResults(connectedAppsResult.getTotalAppCount());
        connectedAppsResponse.setStartIndex(connectedAppsResult.getOffSet() + 1);
        connectedAppsResponse.setLinks(createLinks(V1_API_PATH_COMPONENT + AUTHENTICATOR_PATH_COMPONENT +
                        Constants.PATH_SEPERATOR + resourceId + "/connected-apps", connectedAppsResult.getLimit(),
                connectedAppsResult.getOffSet(), connectedAppsResult.getTotalAppCount(), null));
        return connectedAppsResponse;
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

    private int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        int newOffset = (offset - limit);
        if (newOffset < total) {
            return newOffset;
        }

        return calculateOffsetForPreviousLink(newOffset, limit, total);
    }

    private List<Authenticator> buildAuthenticatorsListResponse(String filter, List<String> requestedAttributeList,
                                                                String localAuthNames,
                                                                String authenticatorNameFilterOperator,
                                                                ArrayList<String> filterTagsList,
                                                                LocalAuthenticatorConfig[] localAuthenticatorConfigs,
                                                                RequestPathAuthenticatorConfig[]
                                                                        requestPathAuthenticatorConfigs,
                                                                List<IdentityProvider> identityProviders) {

        int maximumItemsPerPage = IdentityUtil.getMaximumItemPerPage();

        List<Authenticator> authenticators = new ArrayList<>();
        if (localAuthenticatorConfigs != null) {
            for (LocalAuthenticatorConfig config : localAuthenticatorConfigs) {
                addLocalAuthenticator(config, authenticators, localAuthNames, authenticatorNameFilterOperator,
                        filterTagsList, maximumItemsPerPage);
            }
        }

        if ((authenticators.size() < maximumItemsPerPage) && requestPathAuthenticatorConfigs != null) {
            for (RequestPathAuthenticatorConfig config : requestPathAuthenticatorConfigs) {
                addLocalAuthenticator(config, authenticators, localAuthNames, authenticatorNameFilterOperator,
                        filterTagsList, maximumItemsPerPage);
            }
        }

        if (StringUtils.isBlank(filter)) {
            if ((authenticators.size() < maximumItemsPerPage) && identityProviders != null) {
                for (IdentityProvider identityProvider : identityProviders) {
                    if (authenticators.size() < maximumItemsPerPage) {
                        List<String> configTagsListDistinct = getDistinctTags(identityProvider);
                        addIdp(identityProvider, authenticators, configTagsListDistinct);
                    }
                }
            }
        } else {
            List<ExpressionNode> expressionNodesForIdp = getExpressionNodesForIdp(filter);
            int idPCountToBeRetrieved = maximumItemsPerPage - authenticators.size();
            IdpSearchResult idpSearchResult;
            try {
                idpSearchResult = idpManager.getIdPs(idPCountToBeRetrieved, null, null, null,
                                ContextLoader.getTenantDomainFromContext(), requestedAttributeList,
                                expressionNodesForIdp);
                identityProviders = idpSearchResult.getIdPs();
                if (identityProviders != null) {
                    addIdPsToAuthenticatorList(maximumItemsPerPage, identityProviders, authenticators,
                            filterTagsList);
                    int limit = idpSearchResult.getLimit();
                    int offSet = idpSearchResult.getOffSet();
                    int totalIdpCount = idpSearchResult.getTotalIDPCount();
                    while (authenticators.size() < maximumItemsPerPage && limit > 0 && offSet >= 0 &&
                            totalIdpCount > (limit + offSet)) {
                        identityProviders = new ArrayList<>();
                        getFilteredIdPs(limit, offSet, requestedAttributeList, identityProviders,
                                expressionNodesForIdp);
                        addIdPsToAuthenticatorList(maximumItemsPerPage, identityProviders, authenticators,
                                filterTagsList);
                        offSet = offSet + limit;
                    }
                }
            } catch (IdentityProviderManagementException e) {
                throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDPS, null);
            }
        }
        return authenticators;
    }

    private void addIdPsToAuthenticatorList(int maximumItemsPerPage, List<IdentityProvider> identityProviders,
                                            List<Authenticator> authenticators, ArrayList<String> filterTagsList) {

        for (IdentityProvider identityProvider : identityProviders) {
            if (authenticators.size() < maximumItemsPerPage) {
                List<String> configTagsListDistinct = getDistinctTags(identityProvider);
                if (CollectionUtils.isNotEmpty(filterTagsList) &&
                        CollectionUtils.isNotEmpty(configTagsListDistinct)) {
                    boolean tagFound = false;
                    for (String filterTag : filterTagsList) {
                        if (tagFound) {
                            break;
                        }
                        for (String configTag : configTagsListDistinct) {
                            if (StringUtils.equalsIgnoreCase(configTag, filterTag)) {
                                addIdp(identityProvider, authenticators, configTagsListDistinct);
                                tagFound = true;
                                break;
                            }
                        }
                    }
                } else if (CollectionUtils.isEmpty(filterTagsList)) {
                    addIdp(identityProvider, authenticators, configTagsListDistinct);
                }
            }
        }
    }

    /**
     * Retrieves a distinct list of tags defined for the federated authenticators of an identity provider.
     *
     * @param identityProvider The identity provider.
     * @return A distinct list of tags defined for the federated authenticators of an identity provider.
     */
    private List<String> getDistinctTags(IdentityProvider identityProvider) {

        ArrayList<String> tagsList = new ArrayList<>();

        FederatedAuthenticatorConfig[] fedAuthConfigs = identityProvider
                .getFederatedAuthenticatorConfigs();
        if (fedAuthConfigs != null) {
            for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
                if (config.isEnabled()) {
                    FederatedAuthenticatorConfig federatedAuthenticatorConfig = applicationAuthenticatorService
                            .getFederatedAuthenticatorByName(config.getName());
                    if (federatedAuthenticatorConfig != null) {
                        String[] tags = federatedAuthenticatorConfig.getTags();
                        if (ArrayUtils.isNotEmpty(tags)) {
                            tagsList.addAll(Arrays.asList(tags));
                        }
                    }
                }
            }
            return tagsList.stream().distinct().collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Adds the identity provider to the list of authenticators.
     *
     * @param identityProvider       The identity provider.
     * @param authenticators         The list of authenticators.
     * @param configTagsListDistinct The distinct list of tags available for the identity provider.
     */
    private void addIdp(IdentityProvider identityProvider, List<Authenticator> authenticators,
                        List<String> configTagsListDistinct) {

        Authenticator authenticator = new Authenticator();
        authenticator.setId(identityProvider.getResourceId());
        authenticator.setName(identityProvider.getIdentityProviderName());
        String displayName = identityProvider.getDisplayName();
        if (StringUtils.isNotBlank(displayName)) {
            authenticator.setDisplayName(identityProvider.getDisplayName());
        } else {
            authenticator.setDisplayName(identityProvider.getIdentityProviderName());
        }
        authenticator.setIsEnabled(identityProvider.isEnable());
        authenticator.setType(Authenticator.TypeEnum.FEDERATED);
        authenticator.setImage(identityProvider.getImageUrl());
        authenticator.setDescription(identityProvider.getIdentityProviderDescription());

        /* For the /authenticators APIs, per IDP have an item in the response payload, not per federated authenticator
         within the IDP. If an IDP has more than one federated authenticator, it is considered as an existing older
         authenticator and should always be classified as a SYSTEM type. Otherwise, it can be classified as either
         SYSTEM or USER, depending on the 'definedBy' type of the federated authenticator. */
        if (identityProvider.getFederatedAuthenticatorConfigs().length == 1) {
            FederatedAuthenticatorConfig federatedAuthConfig = resolveFederatedAuthenticatorConfig(identityProvider);
            authenticator.definedBy(Authenticator.DefinedByEnum.valueOf(
                    String.valueOf(federatedAuthConfig.getDefinedByType())));
            if (federatedAuthConfig.getTags() != null) {
                authenticator.setTags(Arrays.asList(federatedAuthConfig.getTags()));
            }
        } else {
            authenticator.definedBy(Authenticator.DefinedByEnum.SYSTEM);
        }

        if (CollectionUtils.isNotEmpty(configTagsListDistinct)) {
            authenticator.setTags(configTagsListDistinct);
        }
        authenticators.add(authenticator);
        authenticator.setSelf(ContextLoader.buildURIForBody(
                String.format("/v1/identity-providers/%s", identityProvider.getResourceId())).toString());
    }

    private FederatedAuthenticatorConfig resolveFederatedAuthenticatorConfig(IdentityProvider identityProvider) {

        try {
            return idpManager.getFederatedAuthenticatorByName(
                    identityProvider.getFederatedAuthenticatorConfigs()[0].getName(),
                    ContextLoader.getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_LISTING_AUTHENTICATORS, String.format("An error occurred whiling " +
                    "retrieving federated authenticator configuration for identity provider: %s",
                    identityProvider.getIdentityProviderName()));
        }
    }

    /**
     * Adds the local authenticator to the list of authenticators.
     *
     * @param config                          The local authenticator configuration.
     * @param authenticators                  The list of authenticators.
     * @param filterAuthenticatorName         The authenticator name passed in the filter string.
     * @param authenticatorNameFilterOperator The filter operator passed for the authenticator name in the filter
     *                                        string.
     * @param tagsList                        The list of tags available for the local authenticator.
     * @param maximumItemsPerPage             The defined maximum items per page limit.
     */
    private void addLocalAuthenticator(LocalAuthenticatorConfig config, List<Authenticator> authenticators,
                                       String filterAuthenticatorName, String authenticatorNameFilterOperator,
                                       ArrayList<String> tagsList, int maximumItemsPerPage) {

        if (authenticators.size() < maximumItemsPerPage) {

            // For local authenticators and request path authenticators, the 'displayName' is considered as the
            // 'name' attribute during filtering.
            if (StringUtils.isNotBlank(filterAuthenticatorName) && CollectionUtils.isNotEmpty(tagsList) &&
                    ((StringUtils.equalsIgnoreCase(authenticatorNameFilterOperator, Constants.FilterOperations.SW) &&
                            StringUtils.startsWithIgnoreCase(config.getDisplayName(), filterAuthenticatorName)) ||
                            (StringUtils.equalsIgnoreCase(authenticatorNameFilterOperator,
                                    Constants.FilterOperations.EQ) &&
                                    StringUtils.equalsIgnoreCase(config.getDisplayName(), filterAuthenticatorName)))) {
                filterTags(config, authenticators, tagsList);
            } else if (StringUtils.isNotBlank(filterAuthenticatorName)) {
                if (StringUtils.equalsIgnoreCase(authenticatorNameFilterOperator, Constants.FilterOperations.SW) &&
                        StringUtils.startsWithIgnoreCase(config.getDisplayName(), filterAuthenticatorName)) {
                    Authenticator authenticator = addLocalAuthenticator(config);
                    authenticators.add(authenticator);
                } else if (StringUtils.equalsIgnoreCase(authenticatorNameFilterOperator, Constants.FilterOperations.EQ)
                        && StringUtils.equalsIgnoreCase(config.getDisplayName(), filterAuthenticatorName)) {
                    Authenticator authenticator = addLocalAuthenticator(config);
                    authenticators.add(authenticator);
                }
            } else if (CollectionUtils.isNotEmpty(tagsList)) {
                filterTags(config, authenticators, tagsList);
            } else {
                Authenticator authenticator = addLocalAuthenticator(config);
                authenticators.add(authenticator);
            }
        }
    }

    /**
     * Adds local authenticator to the list of authenticators if it satisfies the given filter.
     *
     * @param config         The local authenticator configuration.
     * @param authenticators The list of authenticators.
     * @param filterTagsList The list of tags passed in the filter string.
     */
    private void filterTags(LocalAuthenticatorConfig config, List<Authenticator> authenticators,
                            ArrayList<String> filterTagsList) {

        boolean tagFound = false;
        for (String filterTag : filterTagsList) {
            if (tagFound) {
                break;
            }
            String[] authenticatorConfigTags = config.getTags();
            if (ArrayUtils.isNotEmpty(authenticatorConfigTags)) {
                for (String tag : authenticatorConfigTags) {
                    if (StringUtils.equalsIgnoreCase(tag, filterTag)) {
                        Authenticator authenticator = addLocalAuthenticator(config);
                        authenticators.add(authenticator);
                        tagFound = true;
                        break;
                    }
                }
            }
        }
    }

    private Authenticator addLocalAuthenticator(LocalAuthenticatorConfig config) {

        Authenticator authenticator = new Authenticator();
        String authenticatorId = base64URLEncode(config.getName());
        authenticator.setId(authenticatorId);
        authenticator.setName(config.getName());
        authenticator.setDisplayName(config.getDisplayName());
        authenticator.setIsEnabled(config.isEnabled());
        authenticator.setType(Authenticator.TypeEnum.LOCAL);
        authenticator.definedBy(Authenticator.DefinedByEnum.valueOf(config.getDefinedByType().toString()));
        if (AuthenticatorPropertyConstants.DefinedByType.USER.equals(config.getDefinedByType()) && config instanceof
                UserDefinedLocalAuthenticatorConfig) {
            UserDefinedLocalAuthenticatorConfig userDefinedConfig = (UserDefinedLocalAuthenticatorConfig) config;
            authenticator.setImage(userDefinedConfig.getImageUrl());
            authenticator.setDescription(userDefinedConfig.getDescription());
        }
        authenticator.amrValue(config.getAmrValue());
        String[] tags = config.getTags();
        if (ArrayUtils.isNotEmpty(tags)) {
            authenticator.setTags(Arrays.asList(tags));
        }
        authenticator.setSelf(ContextLoader.buildURIForBody
                (String.format("/v1/configs/authenticators/%s", authenticatorId)).toString());
        return authenticator;
    }

    private List<String> buildTagsListResponse(LocalAuthenticatorConfig[] localAuthenticatorConfigs,
                                               RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs,
                                               FederatedAuthenticatorConfig[] federatedAuthenticatorConfigs,
                                               List<UserDefinedLocalAuthenticatorConfig> userDefinedLocalAuthConfigs) {

        ArrayList<String> tagsList = new ArrayList<>();
        if (localAuthenticatorConfigs != null) {
            for (LocalAuthenticatorConfig config : localAuthenticatorConfigs) {
                String[] tags = config.getTags();
                if (ArrayUtils.isNotEmpty(tags)) {
                    tagsList.addAll(Arrays.asList(tags));
                }
            }
        }
        if (requestPathAuthenticatorConfigs != null) {
            for (RequestPathAuthenticatorConfig config : requestPathAuthenticatorConfigs) {
                String[] tags = config.getTags();
                if (ArrayUtils.isNotEmpty(tags)) {
                    tagsList.addAll(Arrays.asList(tags));
                }
            }
        }
        if (ArrayUtils.isNotEmpty(federatedAuthenticatorConfigs)) {
            for (FederatedAuthenticatorConfig config : federatedAuthenticatorConfigs) {
                String[] tags = config.getTags();
                if (ArrayUtils.isNotEmpty(tags)) {
                    tagsList.addAll(Arrays.asList(tags));
                }
            }
        }
        if (!userDefinedLocalAuthConfigs.isEmpty()) {
            for (UserDefinedLocalAuthenticatorConfig config : userDefinedLocalAuthConfigs) {
                String[] tags = config.getTags();
                if (ArrayUtils.isNotEmpty(tags)) {
                    tagsList.addAll(Arrays.asList(tags));
                }
            }
        }
        return tagsList.stream().distinct().collect(Collectors.toList());
    }

    private List<ExpressionNode> getExpressionNodesForAuthenticator(String filter) {

        // Filter example : name sw go and (tag eq 2fa or tag eq Social-Login)
        List<ExpressionNode> expressionNodes = new ArrayList<>();
        FilterTreeBuilder filterTreeBuilder;
        try {
            if (StringUtils.isNotBlank(filter)) {
                filterTreeBuilder = new FilterTreeBuilder(filter);
                Node rootNode = filterTreeBuilder.buildTree();
                validateFilter(rootNode);
                setExpressionNodeListForAuthenticator(rootNode, expressionNodes);
            }
        } catch (IOException | IdentityException e) {
            throw buildClientError(Constants.ErrorMessage.ERROR_CODE_INVALID_FILTER_FORMAT, null);
        }
        return expressionNodes;
    }

    private void validateFilter(Node rootNode) {

        if (!(rootNode instanceof OperationNode)) {

            String attributeValue = ((ExpressionNode) rootNode).getAttributeValue();
            String operation = ((ExpressionNode) rootNode).getOperation();

            if (StringUtils.equalsIgnoreCase(attributeValue, Constants.FilterAttributes.NAME)) {
                if (StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.SW) ||
                        StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)) {
                    return;
                } else {
                    throw handleException(Response.Status.NOT_IMPLEMENTED,
                            Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_NAME, operation);
                }
            } else if (StringUtils.equalsIgnoreCase(attributeValue, Constants.FilterAttributes.TAG)) {
                if (StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)) {
                    return;
                } else {
                    throw handleException(Response.Status.NOT_IMPLEMENTED,
                            Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_TAG, operation);
                }
            } else {
                throw buildClientError(Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, attributeValue);
            }
        }

        Node rootNodeLeftNode = rootNode.getLeftNode();
        Node rootNodeRightNode = rootNode.getRightNode();
        boolean nameFiltering = false;
        boolean tagAvailableInLeftNode = false;
        boolean tagAvailableInRightNode = false;

        if (rootNodeLeftNode instanceof ExpressionNode) {
            String attributeValue = ((ExpressionNode) rootNodeLeftNode).getAttributeValue();
            nameFiltering = attributeValue.equals(Constants.FilterAttributes.NAME);
            tagAvailableInLeftNode = attributeValue.equals(Constants.FilterAttributes.TAG);

            if (!(nameFiltering || tagAvailableInLeftNode)) {
                throw buildClientError(Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, attributeValue);
            }
            String operation = ((ExpressionNode) rootNodeLeftNode).getOperation();

            if (nameFiltering && (!(StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.SW) ||
                    StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)))) {
                throw handleException(Response.Status.NOT_IMPLEMENTED,
                        Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_NAME, operation);
            }

            if (tagAvailableInLeftNode && !StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)) {
                throw handleException(Response.Status.NOT_IMPLEMENTED,
                        Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_TAG, operation);
            }
        } else {
            validateChildNode(rootNodeLeftNode);
        }

        if (rootNodeRightNode instanceof ExpressionNode) {
            String attributeValue = ((ExpressionNode) rootNodeRightNode).getAttributeValue();
            tagAvailableInRightNode = attributeValue.equals(Constants.FilterAttributes.TAG);
            boolean nameAvailableInRightNode = attributeValue.equals(Constants.FilterAttributes.NAME);

            if (!(tagAvailableInRightNode || nameAvailableInRightNode)) {
                throw buildClientError(Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, attributeValue);
            }

            if (nameFiltering && nameAvailableInRightNode) {
                throw handleException(Response.Status.NOT_IMPLEMENTED,
                        Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_FOR_MULTIPLE_NAMES, null);
            }

            String operation = ((ExpressionNode) rootNodeRightNode).getOperation();

            if (!nameFiltering) {
                nameFiltering = nameAvailableInRightNode;
                if (nameFiltering && (!(StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.SW) ||
                        StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)))) {
                    throw handleException(Response.Status.NOT_IMPLEMENTED,
                            Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_NAME, operation);
                }
            }

            if (tagAvailableInRightNode && !StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)) {
                throw handleException(Response.Status.NOT_IMPLEMENTED,
                        Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_TAG, operation);
            }

        } else {
            validateChildNode(rootNodeRightNode);
        }

        String operation = ((OperationNode) rootNode).getOperation();

        if (nameFiltering && !StringUtils.equalsIgnoreCase(operation, Constants.ComplexQueryOperations.AND)) {
            throw handleException(Response.Status.NOT_IMPLEMENTED,
                    Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_COMPLEX_QUERY_OPERATION_FOR_NAME, operation);
        }

        if (tagAvailableInLeftNode && tagAvailableInRightNode &&
                !StringUtils.equalsIgnoreCase(operation, Constants.ComplexQueryOperations.OR)) {
            throw handleException(Response.Status.NOT_IMPLEMENTED,
                    Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_COMPLEX_QUERY_OPERATION_FOR_TAG, operation);
        }
    }

    private void validateChildNode(Node childNode) {

        if (childNode instanceof OperationNode) {
            String operation = ((OperationNode) childNode).getOperation();
            if (!StringUtils.equalsIgnoreCase(operation, Constants.ComplexQueryOperations.OR)) {
                throw handleException(Response.Status.NOT_IMPLEMENTED,
                        Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_COMPLEX_QUERY_IN_FILTER, operation);
            }
        }
        validateChildNodeFilter(childNode.getLeftNode());
        validateChildNodeFilter(childNode.getRightNode());
    }

    private void validateChildNodeFilter(Node childNode) {

        if (childNode instanceof OperationNode) {
            validateChildNode(childNode);
        } else {
            String attributeValue = ((ExpressionNode) childNode).getAttributeValue();
            String operation = ((ExpressionNode) childNode).getOperation();
            if (!attributeValue.equals(Constants.FilterAttributes.TAG)) {
                throw buildClientError(Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, attributeValue);
            } else if (!StringUtils.equalsIgnoreCase(operation, Constants.FilterOperations.EQ)) {
                throw handleException(Response.Status.NOT_IMPLEMENTED,
                        Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_TAG, operation);
            }
        }
    }

    private void setExpressionNodeListForAuthenticator(Node node, List<ExpressionNode> expression) {

        if (node instanceof ExpressionNode) {
            expression.add((ExpressionNode) node);
        } else if (node instanceof OperationNode) {
            setExpressionNodeListForAuthenticator(node.getLeftNode(), expression);
            setExpressionNodeListForAuthenticator(node.getRightNode(), expression);
        }
    }

    private List<ExpressionNode> getExpressionNodesForIdp(String filter) {

        // Filter example : name sw go and (tag eq 2fa or tag eq Social-Login)
        List<ExpressionNode> expressionNodes = new ArrayList<>();
        FilterTreeBuilder filterTreeBuilder;
        try {
            if (StringUtils.isNotBlank(filter)) {
                filterTreeBuilder = new FilterTreeBuilder(filter);
                Node rootNode = filterTreeBuilder.buildTree();
                setExpressionNodeListForIdp(rootNode, expressionNodes);
            }
        } catch (IOException | IdentityException e) {
            throw buildClientError(Constants.ErrorMessage.ERROR_CODE_INVALID_FILTER_FORMAT, null);

        }
        return expressionNodes;
    }

    /**
     * Sets the expression nodes required for the retrieval of identity providers from the database.
     *
     * @param node       The node
     * @param expression The list of expression nodes.
     */
    private void setExpressionNodeListForIdp(Node node, List<ExpressionNode> expression) {

        if (node instanceof ExpressionNode) {
            if (StringUtils.isNotBlank(((ExpressionNode) node).getAttributeValue())) {
                if (StringUtils.equalsIgnoreCase(((ExpressionNode) node).getAttributeValue(),
                        Constants.FilterAttributes.NAME)) {
                    expression.add((ExpressionNode) node);
                }
            }
        } else if (node instanceof OperationNode) {
            setExpressionNodeListForIdp(node.getLeftNode(), expression);
            setExpressionNodeListForIdp(node.getRightNode(), expression);
        }
    }

    private void getFilteredIdPs(int limit, int offSet, List<String> requestedAttributeList,
                                 List<IdentityProvider> identityProviders, List<ExpressionNode> expressionNodes) {

        try {
            IdpSearchResult idpSearchResult = idpManager.getIdPs(limit, (offSet + limit), null, null,
                            ContextLoader.getTenantDomainFromContext(), requestedAttributeList, expressionNodes);
            identityProviders.addAll(idpSearchResult.getIdPs());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_IDPS, null);
        }
    }

    /**
     * The authenticator name and the filter operation for authenticator name passed in the filter string.
     *
     * @param expressionNodes The list of expression nodes.
     * @return NameFilter object containing the authenticator name and the filter operation for authenticator name
     * passed in the filter string.
     */
    private NameFilter getFilterAuthenticatorNameAndOperation(List<ExpressionNode> expressionNodes) {

        for (ExpressionNode expressionNode : expressionNodes) {
            if (expressionNode.getAttributeValue().equals(Constants.FilterAttributes.NAME)) {
                return new NameFilter(expressionNode.getValue(), expressionNode.getOperation());
            }
        }
        return null;
    }

    /**
     * Retrieves the list of tags passed in the filter string.
     *
     * @param expressionNodes The list of expression nodes.
     * @return The list of tags passed in the filter string.
     */
    private ArrayList<String> getFilterTagsList(List<ExpressionNode> expressionNodes) {

        ArrayList<String> tags = new ArrayList<>();
        if (expressionNodes != null) {
            for (ExpressionNode expressionNode : expressionNodes) {
                if (StringUtils.equalsIgnoreCase(expressionNode.getAttributeValue(), Constants.FilterAttributes.TAG)) {
                    String tag = expressionNode.getValue();
                    if (StringUtils.isNotBlank(tag)) {
                        tags.add(tag);
                    }
                }
            }
        }
        return tags;
    }

    /**
     * Handle IdentityApplicationManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityApplicationManagementException
     * @param errorEnum Error information.
     * @return APIError.
     */
    private APIError handleApplicationMgtException(IdentityApplicationManagementException e,
                                                   Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;
        Response.Status status;

        if (e instanceof IdentityApplicationManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.AUTHENTICATOR_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof IdentityApplicationManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.AUTHENTICATOR_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle IdentityProviderManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityProviderManagementException.
     * @param errorEnum Error information.
     * @return APIError.
     */
    private APIError handleIdPException(IdentityProviderManagementException e,
                                        Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;
        Response.Status status;

        if (e instanceof IdentityProviderManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.AUTHENTICATOR_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;

        } else if (e instanceof IdentityProviderManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.AUTHENTICATOR_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle IdentityProviderManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityProviderManagementException.
     * @return APIError.
     */
    private APIError handleAuthenticatorException(AuthenticatorMgtException e, Response.Status... responseStatus) {

        ErrorResponse.Builder errorResponseBuilder = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(e.getMessage())
                .withDescription(e.getDescription());
        Response.Status status = null;
        if (ArrayUtils.isNotEmpty(responseStatus)) {
            status = responseStatus[0];
        }
        ErrorResponse errorResponse;

        if (e instanceof AuthenticatorMgtClientException) {
            errorResponse = errorResponseBuilder.build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.AUTHENTICATOR_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getDescription());
            if (status == null) {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (e instanceof AuthenticatorMgtServerException) {
            errorResponse = errorResponseBuilder.build(log, e, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.AUTHENTICATOR_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = errorResponseBuilder.build(log, e, e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in the API.
     *
     * @param status HTTP status.
     * @param error  Error message information.
     * @param data   Context data.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error message information.
     * @param data     Context data.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message information.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = String.format(error.getDescription(), "");
        }
        return message;
    }

    /**
     * Handle client exceptions generated in API.
     *
     * @param errorEnum Error message information.
     * @param data      Context data.
     * @return APIError.
     */
    private APIError buildClientError(Constants.ErrorMessage errorEnum, String data) {

        String description = includeData(errorEnum, data);
        return buildClientError(errorEnum.getCode(), errorEnum.getMessage(), description);
    }

    private static APIError buildClientError(String errorCode, String message, String description) {

        ErrorResponse errorResponse = new ErrorResponse.Builder().withCode(errorCode).withMessage(message)
                .withDescription(description).build(log, description);

        Response.Status status = Response.Status.BAD_REQUEST;
        return new APIError(status, errorResponse);
    }

    private void handleNotImplementedCapabilities(Integer limit, Integer offset) {

        Constants.ErrorMessage errorEnum = null;
        if (limit != null || offset != null) {
            errorEnum = Constants.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        }
        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum, null).build(log, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;
            throw new APIError(status, errorResponse);
        }
    }
}
