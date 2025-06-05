/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.userstore.v1.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.FileContent;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.userstore.common.UserStoreConstants;
import org.wso2.carbon.identity.api.server.userstore.v1.core.functions.userstore.AttributeMappingsToApiModel;
import org.wso2.carbon.identity.api.server.userstore.v1.model.AddUserStorePropertiesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.Attribute;
import org.wso2.carbon.identity.api.server.userstore.v1.model.AvailableUserStoreClassesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ClaimAttributeMapping;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ConnectionEstablishedResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.MetaUserStoreType;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PropertiesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.RDBMSConnectionReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreAttributeMappingResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreConfigurations;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreConfigurationsRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreListResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStorePropertiesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreResponse;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataClientException;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.AttributeMapping;
import org.wso2.carbon.identity.claim.metadata.mgt.model.LocalClaim;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.user.store.configuration.UserStoreConfigService;
import org.wso2.carbon.identity.user.store.configuration.dto.PropertyDTO;
import org.wso2.carbon.identity.user.store.configuration.dto.UserStoreDTO;
import org.wso2.carbon.identity.user.store.configuration.model.UserStoreAttribute;
import org.wso2.carbon.identity.user.store.configuration.model.UserStoreAttributeMappings;
import org.wso2.carbon.identity.user.store.configuration.utils.IdentityUserStoreClientException;
import org.wso2.carbon.identity.user.store.configuration.utils.IdentityUserStoreMgtException;
import org.wso2.carbon.identity.user.store.configuration.utils.IdentityUserStoreServerException;
import org.wso2.carbon.user.api.Properties;
import org.wso2.carbon.user.api.Property;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserStoreConfigConstants;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tracker.UserStoreManagerRegistry;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.inspector.TagInspector;
import org.yaml.snakeyaml.inspector.TrustedPrefixesTagInspector;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_RESOURCE_LIMIT_REACHED;
import static org.wso2.carbon.identity.api.server.common.Constants.JSON_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_JSON;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_XML;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_YAML;
import static org.wso2.carbon.identity.api.server.common.Constants.REGEX_COMMA;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.XML_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.Constants.YAML_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.userstore.common.UserStoreConstants.ErrorMessage.ERROR_CODE_USER_STORE_LIMIT_REACHED;
import static org.wso2.carbon.identity.core.util.IdentityUtil.isValidFileName;

/**
 * Call internal osgi services to perform user store related operations.
 */
public class ServerUserStoreService {

    private final UserStoreConfigService userStoreConfigService;

    private final RealmService realmService;

    private final ClaimMetadataManagementService claimMetadataManagementService;

    private static final Log LOG = LogFactory.getLog(ServerUserStoreService.class);

    private static final String DUMMY_MESSAGE_ID = "DUMMY-MESSAGE-ID";

    private static final String EXPRESSION_LANGUAGE_REGEX = "^.*(\\$\\{|#\\{).*}.*$";

    private static final String PASSWORD = "password";

    private static boolean isAvailableUserStoreTypes(List<AvailableUserStoreClassesRes> userStoreList, String typeID) {

        for (AvailableUserStoreClassesRes userStore : userStoreList) {
            if (userStore.getTypeId().equals(typeID)) {
                return true;
            }
        }
        return false;
    }

    public ServerUserStoreService(UserStoreConfigService userStoreConfigService, RealmService realmService,
                                  ClaimMetadataManagementService claimMetadataManagementService) {

        this.userStoreConfigService = userStoreConfigService;
        this.realmService = realmService;
        this.claimMetadataManagementService = claimMetadataManagementService;
    }

    /**
     * Add a userStore {@link UserStoreReq}.
     *
     * @param userStoreReq {@link UserStoreReq} to insert.
     * @return UserStoreResponse
     */
    public UserStoreResponse addUserStore(UserStoreReq userStoreReq) {

        try {
            validateMandatoryProperties(userStoreReq);
            if (!isAvailableUserStoreTypes(getAvailableUserStoreTypes(), userStoreReq.getTypeId())) {
                throw handleException(Response.Status.BAD_REQUEST,
                        UserStoreConstants.ErrorMessage.ERROR_CODE_INVALID_USERSTORE_TYPE);
            }
            String userstoreDomain = userStoreReq.getName();
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<LocalClaim> localClaimList = new ArrayList<>();

            List<ClaimAttributeMapping> claimAttributeMappingList = userStoreReq.getClaimAttributeMappings();
            if (claimAttributeMappingList != null) {
                localClaimList =  createLocalClaimList(userstoreDomain, claimAttributeMappingList);
                validateClaimMappings(tenantDomain, localClaimList);
            }

            UserStoreDTO userStoreDTO = createUserStoreDTO(userStoreReq);
            userStoreConfigService.addUserStore(userStoreDTO);

            if (claimAttributeMappingList != null) {
                updateClaimMappings(userstoreDomain, tenantDomain, localClaimList);
            }

            return buildUserStoreResponseDTO(userStoreReq);
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * Delete a userStore by its domainId.
     *
     * @param userstoreDomainId base64 encoded url value of userStore domain Id.
     */
    public void deleteUserStore(String userstoreDomainId) {

        try {
            userStoreConfigService.deleteUserStore(base64URLDecodeId(userstoreDomainId));
        } catch (IdentityUserStoreClientException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * Update the user store by its domain Id.
     *
     * @param domainId     the domain name to be replaced
     * @param userStoreReq {@link UserStoreReq} to edit.
     * @return UserStoreResponse.
     */
    public UserStoreResponse editUserStore(String domainId, UserStoreReq userStoreReq) {

        /*
        domainName and typeName are not allowed to edit. iF domain name wanted to update then use
        userStoreConfigService.updateUserStoreByDomainName(base64URLDecodeId(domainId),
        createUserStoreDTO(userStoreReq, domainId));
         */
        try {
            validateUserstoreUpdateRequest(domainId, userStoreReq);
            String userstoreDomain = userStoreReq.getName();
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<LocalClaim> localClaimList = new ArrayList<>();
            List<ClaimAttributeMapping> claimAttributeMappingList = userStoreReq.getClaimAttributeMappings();
            if (claimAttributeMappingList != null) {
                localClaimList =  createLocalClaimList(userstoreDomain, claimAttributeMappingList);
                validateClaimMappings(tenantDomain, localClaimList);
            }
            userStoreConfigService.updateUserStore(createUserStoreDTO(userStoreReq), false);
            if (claimAttributeMappingList != null) {
                updateClaimMappings(userstoreDomain, tenantDomain, localClaimList);
            }
            return buildUserStoreResponseDTO(userStoreReq);
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * Export a secondary user store identified by the domain ID, in the given format.
     *
     * @param domainId Domain ID of the user store to be exported.
     * @param fileType The format of the exported string.
     * @return FileContent object of the user store in the requested format.
     */
    public FileContent exportUserStore(String domainId, String fileType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exporting user store from the domain ID: " + domainId);
        }
        if (StringUtils.isBlank(fileType)) {
            throw new UnsupportedOperationException("No valid media type found");
        }

        UserStoreConfigurationsRes userStoreConfigurationsRes = getUserStoreByDomainId(domainId);

        if (userStoreConfigurationsRes == null) {
            throw handleException(Response.Status.NOT_FOUND,
                        UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_EXPORTING_USER_STORE);
        }

        UserStoreConfigurations userStoreConfigsToExport = new UserStoreConfigurations();
        userStoreConfigsToExport.setId(domainId);
        userStoreConfigsToExport.setName(userStoreConfigurationsRes.getName());
        userStoreConfigsToExport.setDescription(userStoreConfigurationsRes.getDescription());
        userStoreConfigsToExport.setTypeId(userStoreConfigurationsRes.getTypeId());
        userStoreConfigsToExport.setTypeName(userStoreConfigurationsRes.getTypeName());
        userStoreConfigsToExport.setIsLocal(userStoreConfigurationsRes.getIsLocal());
        userStoreConfigsToExport.setClaimAttributeMappings(userStoreConfigurationsRes.getClaimAttributeMappings());

        for (AddUserStorePropertiesRes propertyRes : userStoreConfigurationsRes.getProperties()) {
            org.wso2.carbon.identity.api.server.userstore.v1.model.Property property = new org.wso2.carbon.identity
                    .api.server.userstore.v1.model.Property();
            property.setName(propertyRes.getName());
            property.setValue(propertyRes.getValue());
            userStoreConfigsToExport.addPropertiesItem(property);
        }

        FileContent fileContent;
        try {
            fileContent = generateFileFromModel(fileType, userStoreConfigsToExport);
        } catch (UserStoreException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_EXPORTING_USER_STORE);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Successfully exported userstore: %s as a file type of %s.",
                    userStoreConfigsToExport.getName(), fileType));
        }
        return fileContent;
    }

    /**
     * Create a new user store by importing an YAML, JSON or XML configuration file.
     *
     * @param fileInputStream File to be imported as an input stream.
     * @param fileDetail      File details.
     * @return Unique identifier of the created user store.
     */
    public String importUserStore(InputStream fileInputStream, Attachment fileDetail) {

        UserStoreReq userStoreConfigs;
        try {
            userStoreConfigs = getUserStoreFromFile(fileInputStream, fileDetail);
        } catch (UserStoreException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_IMPORTING_USER_STORE);
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_IMPORTING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }

        UserStoreResponse userStoreResponse = addUserStore(userStoreConfigs);
        return userStoreResponse.getId();
    }

    /**
     * Update an existing user store from an YAML, JSON or XML configuration file.
     *
     * @param userstoreDomainID Resource ID of the user store to be updated.
     * @param fileInputStream   File to be imported as an input stream.
     * @param fileDetail        File details.
     * @return Unique identifier of the updated user store.
     */
    public String updateUserStoreFromFile(String userstoreDomainID, InputStream fileInputStream,
                                          Attachment fileDetail) {

        UserStoreReq userStoreConfigs;
        try {
            userStoreConfigs = getUserStoreFromFile(fileInputStream, fileDetail);
        } catch (UserStoreException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_USER_STORE);
        }  catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }

        UserStoreResponse userStoreResponse = editUserStore(userstoreDomainID, userStoreConfigs);
        return userStoreResponse.getId();
    }

    /**
     * To retrieve the available user store classes.
     *
     * @return List<AvailableUserStoreClassesRes>.
     */
    public List<AvailableUserStoreClassesRes> getAvailableUserStoreTypes() {

        Set<String> classNames;
        try {
            classNames = userStoreConfigService.getAvailableUserStoreClasses();
            List<AvailableUserStoreClassesRes> propertiesToAdd = new ArrayList<>();
            Map<String, Boolean> userStoreManagersType = UserStoreManagerRegistry.getUserStoreManagersType();
            for (String className : classNames) {
                AvailableUserStoreClassesRes availableUserStoreClassesResDTO = new AvailableUserStoreClassesRes();
                String typeId = base64URLEncodeId(Objects.
                        requireNonNull(getUserStoreTypeName(className)));
                availableUserStoreClassesResDTO.setClassName(className);
                availableUserStoreClassesResDTO.setTypeName(getUserStoreTypeName(className));
                availableUserStoreClassesResDTO.setTypeId(typeId);
                availableUserStoreClassesResDTO.setSelf(
                        ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                                UserStoreConstants.USER_STORE_PATH_COMPONENT + "/meta/types/%s", typeId)).toString());

                if (userStoreManagersType.containsKey(className)) {
                    availableUserStoreClassesResDTO
                            .setIsLocal(userStoreManagersType.get(className));
                }

                propertiesToAdd.add(availableUserStoreClassesResDTO);
            }
            return propertiesToAdd;
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_RETRIEVING_USER_STORE_TYPE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * To retrieve the configured user store lists.
     *
     * @param limit  items per page.
     * @param offset 0 based index to get the results starting from this index + 1.
     * @param filter to specify the filtering capabilities.
     * @param sort   to specify the sorting order.
     * @return List<UserStoreListResponse>.
     */
    public List<UserStoreListResponse> getUserStoreList(Integer limit, Integer offset, String filter, String sort,
                                                        String requiredAttributes) {

        handleNotImplementedBehaviour(limit, offset, filter, sort);

        try {
            UserStoreDTO[] userStoreDTOS = userStoreConfigService.getUserStores();
            return buildUserStoreListResponse(userStoreDTOS, requiredAttributes);

        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * Retrieve primary user store.
     *
     * @return UserStoreConfigurationsRes.
     */
    public UserStoreConfigurationsRes getPrimaryUserStore() {

        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        RealmConfiguration realmConfiguration;
        try {
            realmConfiguration = realmService.getTenantUserRealm(tenantId).getRealmConfiguration();
        } catch (UserStoreException exception) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error occurred while getting the RealmConfiguration for tenant: " + tenantId, exception);
            }
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, UserStoreConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_REALM_CONFIG, Integer.toString(tenantId));
        }
        if (realmConfiguration == null) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, UserStoreConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_PRIMARY_USERSTORE);
        }
        List<AddUserStorePropertiesRes> propertiesTobeAdd = new ArrayList<>();
        UserStoreConfigurationsRes primaryUserstoreConfigs = new UserStoreConfigurationsRes();
        primaryUserstoreConfigs.setClassName(realmConfiguration.getUserStoreClass());
        primaryUserstoreConfigs.setDescription(realmConfiguration.getDescription());
        primaryUserstoreConfigs.setName(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME);
        primaryUserstoreConfigs.setTypeId(base64URLEncodeId(Objects.requireNonNull
                (getUserStoreTypeName(realmConfiguration.getUserStoreClass()))));
        primaryUserstoreConfigs.setTypeName(getUserStoreTypeName(realmConfiguration.getUserStoreClass()));
        Map<String, String> userstoreProps = realmConfiguration.getUserStoreProperties();
        if (MapUtils.isNotEmpty(userstoreProps)) {
            for (Map.Entry<String, String> entry : userstoreProps.entrySet()) {
                AddUserStorePropertiesRes userStorePropertiesRes = new AddUserStorePropertiesRes();
                userStorePropertiesRes.setName(entry.getKey());
                if (UserStoreConfigConstants.connectionPassword.equals(entry.getKey())) {
                    userStorePropertiesRes.setValue(UserStoreConstants.USER_STORE_PROPERTY_MASK);
                } else {
                    userStorePropertiesRes.setValue(entry.getValue());
                }
                propertiesTobeAdd.add(userStorePropertiesRes);
            }
        }
        primaryUserstoreConfigs.setProperties(propertiesTobeAdd);
        try {
            primaryUserstoreConfigs.setIsLocal(UserStoreManagerRegistry.
                    isLocalUserStore(realmConfiguration.getUserStoreClass()));
            List<ClaimAttributeMapping> claimAttributeMappings = getClaimAttributeMappings(
                    ContextLoader.getTenantDomainFromContext(), UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME);
            primaryUserstoreConfigs.setClaimAttributeMappings(claimAttributeMappings);

        } catch (UserStoreException e) {
            LOG.error(String.format("Cannot found user store manager type for user store manager: %s",
                    getUserStoreType(realmConfiguration.getUserStoreClass())), e);
        }
        return primaryUserstoreConfigs;
    }

    /**
     * Retrieve user store by its domain id.
     *
     * @param domainId the user store domain id.
     * @return UserStoreConfigurationsRes.
     */
    public UserStoreConfigurationsRes getUserStoreByDomainId(String domainId) {

        List<AddUserStorePropertiesRes> propertiesTobeAdd = new ArrayList<>();
        try {
            UserStoreDTO userStoreDTO = userStoreConfigService.getUserStore(base64URLDecodeId(domainId));
            if (userStoreDTO == null) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.
                        ERROR_CODE_NOT_FOUND);
            }
            List<ClaimAttributeMapping> claimAttributeMappings = getClaimAttributeMappings(
                    ContextLoader.getTenantDomainFromContext(), base64URLDecodeId(domainId));

            UserStoreConfigurationsRes userStoreConfigurations = new UserStoreConfigurationsRes();
            userStoreConfigurations.setClassName(userStoreDTO.getClassName());
            userStoreConfigurations.setDescription(userStoreDTO.getDescription());
            userStoreConfigurations.setName(userStoreDTO.getDomainId());
            userStoreConfigurations.setTypeId(base64URLEncodeId(Objects.requireNonNull
                    (getUserStoreTypeName(userStoreDTO.getClassName()))));
            userStoreConfigurations.setTypeName(getUserStoreTypeName(userStoreDTO.getClassName()));
            PropertyDTO[] dtoProperties = userStoreDTO.getProperties();
            for (PropertyDTO propertyDTO : dtoProperties) {
                AddUserStorePropertiesRes userStorePropertiesRes = new AddUserStorePropertiesRes();
                userStorePropertiesRes.setName(propertyDTO.getName());
                userStorePropertiesRes.setValue(propertyDTO.getValue());
                propertiesTobeAdd.add(userStorePropertiesRes);
            }
            userStoreConfigurations.setProperties(propertiesTobeAdd);
            try {
                userStoreConfigurations
                        .setIsLocal(UserStoreManagerRegistry.isLocalUserStore(userStoreDTO.getClassName()));
                userStoreConfigurations.setClaimAttributeMappings(claimAttributeMappings);
            } catch (UserStoreException e) {
                LOG.error(String.format("Cannot found user store manager type for user store manager: %s",
                        getUserStoreType(userStoreDTO.getClassName())), e);
            }
            return userStoreConfigurations;

        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE_BY_DOMAIN_ID;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * Retrieve the meta of user store type.
     *
     * @param typeId the user store type id.
     * @return MetaUserStoreType.
     */

    public MetaUserStoreType getUserStoreManagerProperties(String typeId) {

        Set<String> classNames;
        try {
            classNames = userStoreConfigService.getAvailableUserStoreClasses();
            if (CollectionUtils.isNotEmpty(classNames) &&
                    classNames.contains(getUserStoreType(base64URLDecodeId(typeId)))) {
                return buildUserStoreMetaResponse(typeId);
            } else {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.
                        ERROR_CODE_NOT_FOUND);
            }
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * Check the connection heath for JDBC userstores.
     *
     * @param rdBMSConnectionReq {@link RDBMSConnectionReq}.
     * @return ConnectionEstablishedResponse.
     */
    public ConnectionEstablishedResponse testRDBMSConnection(RDBMSConnectionReq rdBMSConnectionReq) {

        ConnectionEstablishedResponse connectionEstablishedResponse = new ConnectionEstablishedResponse();
        boolean isConnectionEstablished;
        connectionEstablishedResponse.setConnection(false);
        try {
            isConnectionEstablished = userStoreConfigService.testRDBMSConnection(rdBMSConnectionReq.getDomain(),
                    rdBMSConnectionReq.getDriverName(), rdBMSConnectionReq.getConnectionURL(),
                    rdBMSConnectionReq.getUsername(), rdBMSConnectionReq.getConnectionPassword(), DUMMY_MESSAGE_ID);
            if (isConnectionEstablished) {
                connectionEstablishedResponse.setConnection(true);
            }
        } catch (IdentityUserStoreMgtException e) {
            connectionEstablishedResponse.setConnection(false);
        }
        return connectionEstablishedResponse;
    }

    /**
     * To make a partial update or update the specific property of the user store config.
     *
     * @param domainId      user store domain id
     * @param patchDocument patch request
     * @return UserStoreResponse
     */
    public UserStoreResponse patchUserStore(String domainId, List<PatchDocument> patchDocument) {

        Pattern pattern = Pattern.compile(EXPRESSION_LANGUAGE_REGEX);
        List<PatchDocument> supportedPatchOperations = new ArrayList<>();
        for (PatchDocument patch : patchDocument) {
            if (pattern.matcher(patch.getValue()).matches()) {
                throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                        .ERROR_CODE_INVALID_INPUT);
            }
            //Only the Replace operation supported with PATCH request
            PatchDocument.OperationEnum operation = patch.getOperation();
            if (operation == PatchDocument.OperationEnum.REPLACE) {
                supportedPatchOperations.add(patch);
            }
        }

        if (CollectionUtils.isNotEmpty(supportedPatchOperations)) {
            UserStoreDTO userStoreDTO = buildUserStoreForPatch(domainId, supportedPatchOperations);
            return performPatchReplace(userStoreDTO);
        }
        return null;
    }

    /**
     * To make a partial update single or multiple claim attribute mappings of a specific user store.
     *
     * @param userstoreDomainId     user store domain id.
     * @param claimAttributeMapping list of claim attribute mappings in patch request.
     */
    public void updateClaimAttributeMappings(String userstoreDomainId,
                                             List<ClaimAttributeMapping> claimAttributeMapping) {

        if (claimAttributeMapping == null) {
            throw handleException(Response.Status.BAD_REQUEST,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_ATTRIBUTE_MAPPINGS);
        }

        try {
            String userstoreDomain = base64URLDecodeId(userstoreDomainId);
            String tenantDomain = ContextLoader.getTenantDomainFromContext();

            validateUserstore(userstoreDomainId);
            List<LocalClaim> localClaimList =  createLocalClaimList(userstoreDomain, claimAttributeMapping);
            validateClaimMappings(tenantDomain, localClaimList);
            updateClaimMappings(userstoreDomain, tenantDomain, localClaimList);
        }  catch (UserStoreException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_ID);
        } catch (IdentityUserStoreClientException e) {
            throw handleIdentityUserStoreMgtException(e, UserStoreConstants.ErrorMessage.ERROR_CODE_NOT_FOUND);
        }
    }

    /**
     * To check whether user store is exist.
     *
     * @param userStoreDomain     user store domain name.
     * @return boolean
     */
    private boolean isUserStoreExists(String userStoreDomain) throws UserStoreException {

        UserStoreManager userStoreManager;
        if (IdentityUtil.getPrimaryDomainName().equals(userStoreDomain)) {
            return true;
        } else {
            userStoreManager = ((UserStoreManager) CarbonContext.getThreadLocalCarbonContext().getUserRealm()
                    .getUserStoreManager()).getSecondaryUserStoreManager(userStoreDomain);
        }

        return userStoreManager != null;
    }

    /**
     * To handle the patch REPLACE request.
     *
     * @param userStoreDTO user store dto.
     * @return UserStoreResponse
     */
    private UserStoreResponse performPatchReplace(UserStoreDTO userStoreDTO) {

        try {
            userStoreConfigService.updateUserStore(userStoreDTO, false);
            return buildResponseForPatchReplace(userStoreDTO, userStoreDTO.getProperties());
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
    }

    /**
     * To list the claim attribute mappings for specific user store domain.
     *
     * @param tenantDomain        the tenant domain.
     * @param userstoreDomainName the user store domain name.
     * @return List<ClaimAttributeMapping>.
     */
    private List<ClaimAttributeMapping> getClaimAttributeMappings(String tenantDomain, String userstoreDomainName) {

        List<ClaimAttributeMapping> claimAttributeMappingList = new ArrayList<>();
        try {
            List<LocalClaim> localClaimList = claimMetadataManagementService.getLocalClaims(tenantDomain);
            for (LocalClaim localClaim : localClaimList) {
                if (localClaim.getMappedAttribute(userstoreDomainName) != null) {
                    ClaimAttributeMapping mapping = new ClaimAttributeMapping();
                    mapping.setClaimURI(localClaim.getClaimURI());
                    mapping.setMappedAttribute(localClaim.getMappedAttribute(userstoreDomainName));
                    claimAttributeMappingList.add(mapping);
                }
            }
            return claimAttributeMappingList;
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CLAIM_MAPPING);
        }
    }

    private UserStoreDTO buildUserStoreForPatch(String domainId, List<PatchDocument> patchDocuments) {

        UserStoreDTO userStoreDTO;
        try {
            userStoreDTO = userStoreConfigService.getUserStore(base64URLDecodeId(domainId));
            if (userStoreDTO == null) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.ERROR_CODE_NOT_FOUND);
            }

            PropertyDTO[] propertyDTOS = userStoreDTO.getProperties();
            for (PatchDocument patchDocument : patchDocuments) {
                String path = patchDocument.getPath();
                String value = patchDocument.getValue();
                if (StringUtils.isBlank(path)) {
                    throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT);
                }
                if (path.startsWith(UserStoreConstants.USER_STORE_PROPERTIES)) {
                    String propName = extractPropertyName(path);
                    if (StringUtils.isNotEmpty(propName)) {
                        boolean isPropertyExists = false;
                        for (PropertyDTO propertyDTO : propertyDTOS) {
                            if (propName.equals(propertyDTO.getName())) {
                                propertyDTO.setValue(value);
                                isPropertyExists = true;
                                break;
                            }
                        }
                        if (!isPropertyExists) {
                            Properties defaultProperties =
                                    UserStoreManagerRegistry.getUserStoreProperties(userStoreDTO.getClassName());

                            if (defaultProperties != null) {
                                // Check if the property exists in the optional or advanced properties of the
                                // user store.
                                isPropertyExists = Arrays.stream(defaultProperties.getOptionalProperties())
                                        .anyMatch(optionalProperty -> propName.equals(optionalProperty.getName())) ||
                                        Arrays.stream(defaultProperties.getAdvancedProperties())
                                                .anyMatch(advancedProperty -> propName.equals(
                                                        advancedProperty.getName()));
                            }

                            if (!isPropertyExists) {
                                throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                                        .ERROR_CODE_INVALID_INPUT);
                            }

                            // Updating the propertyDTOList with new property.
                            List<PropertyDTO> propertyDTOList = new ArrayList<>(Arrays.asList(propertyDTOS));
                            PropertyDTO newPropertyDTO = new PropertyDTO();
                            newPropertyDTO.setName(propName);
                            newPropertyDTO.setValue(value);
                            propertyDTOList.add(newPropertyDTO);
                            propertyDTOS = propertyDTOList.toArray(new PropertyDTO[0]);
                        }
                    }
                } else if (path.equals(UserStoreConstants.USER_STORE_DESCRIPTION)) {
                    userStoreDTO.setDescription(value);
                } else {
                    throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT);
                }
            }
            userStoreDTO.setProperties(propertyDTOS);
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }

        return userStoreDTO;
    }

    /**
     * Construct the response for patch replace.
     *
     * @param userStoreDTO {@link UserStoreDTO}.
     * @param propertyDTOS array of {@link PropertyDTO}.
     * @return UserStoreResponse.
     */
    private UserStoreResponse buildResponseForPatchReplace(UserStoreDTO userStoreDTO, PropertyDTO[] propertyDTOS) {

        UserStoreResponse userStoreResponseDTO = new UserStoreResponse();
        userStoreResponseDTO.setId((base64URLEncodeId(userStoreDTO.getDomainId())));
        userStoreResponseDTO.setName(userStoreDTO.getDomainId());
        userStoreResponseDTO.setTypeId(base64URLEncodeId(Objects.requireNonNull(getUserStoreTypeName
                (userStoreDTO.getClassName()))));
        userStoreResponseDTO.setTypeName(getUserStoreTypeName(userStoreDTO.getClassName()));
        userStoreResponseDTO.setDescription(userStoreDTO.getDescription());
        userStoreResponseDTO.setProperties(patchUserStoreProperties(propertyDTOS));
        return userStoreResponseDTO;
    }

    /**
     * Build user store properties response of created or updated user store.
     *
     * @param userStoreReq {@link UserStoreReq} to insert.
     * @return List<AddUserStorePropertiesRes>.
     */
    private List<AddUserStorePropertiesRes> buildUserStorePropertiesRes(UserStoreReq userStoreReq) {

        List<org.wso2.carbon.identity.api.server.userstore.v1.model.Property> values = userStoreReq.getProperties();
        List<AddUserStorePropertiesRes> propertiesToAdd = new ArrayList<>();

        for (org.wso2.carbon.identity.api.server.userstore.v1.model.Property value : values) {
            AddUserStorePropertiesRes addUserStorePropertiesRes = new AddUserStorePropertiesRes();
            addUserStorePropertiesRes.setName(value.getName());
            addUserStorePropertiesRes.setValue(value.getValue());
            propertiesToAdd.add(addUserStorePropertiesRes);
        }
        return propertiesToAdd;
    }

    /**
     * To update claim attribute mappings in bulk for specific user store.
     *
     * @param userstoreDomain user store domain name.
     * @param tenantDomain tenant domain name.
     * @param localClaimList list of claim attribute mappings.
     */
    private void updateClaimMappings(String userstoreDomain, String tenantDomain, List<LocalClaim> localClaimList) {

        try {
            claimMetadataManagementService.validateClaimAttributeMapping(localClaimList, tenantDomain);
            claimMetadataManagementService.updateLocalClaimMappings(localClaimList, tenantDomain, userstoreDomain);
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CLAIM_MAPPING);
        }
    }

    /**
     * To validate claim existence of claim attribute mappings.
     *
     * @param tenantDomain tenant domain name.
     * @param localClaimList list of claim attribute mappings.
     */
    private void validateClaimMappings(String tenantDomain, List<LocalClaim> localClaimList) {

        try {
            claimMetadataManagementService.validateClaimAttributeMapping(localClaimList, tenantDomain);
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CLAIM_MAPPING);
        }
    }

    /**
     * To construct properties list for patch request.
     *
     * @param propertyDTOS array of {@link PropertyDTO}
     * @return List<AddUserStorePropertiesRes>
     */
    private List<AddUserStorePropertiesRes> patchUserStoreProperties(PropertyDTO[] propertyDTOS) {

        List<AddUserStorePropertiesRes> propertiesToAdd = new ArrayList<>();
        for (PropertyDTO propertyDTO : propertyDTOS) {
            AddUserStorePropertiesRes patchUserStoreProperties = new AddUserStorePropertiesRes();
            patchUserStoreProperties.setName(propertyDTO.getName());
            patchUserStoreProperties.setValue(propertyDTO.getValue());
            propertiesToAdd.add(patchUserStoreProperties);
        }
        return propertiesToAdd;
    }

    /**
     * To create UserStoreDTO object for this request.
     *
     * @param userStoreReq {@link UserStoreReq}.
     * @return UserStoreDTO.
     */
    private UserStoreDTO createUserStoreDTO(UserStoreReq userStoreReq) {

        UserStoreDTO userStoreDTO = new UserStoreDTO();
        userStoreDTO.setDomainId(userStoreReq.getName());
        userStoreDTO.setClassName(getUserStoreType(base64URLDecodeId(userStoreReq.getTypeId())));
        userStoreDTO.setDescription(userStoreReq.getDescription());
        userStoreDTO.setProperties(createPropertyListDTO(userStoreReq));
        return userStoreDTO;
    }

    /**
     * To create Local claim list from a claim attribute mapping list.
     *
     * @param userStoreId user store domain Id.
     * @param claimAttributeMappingList list of claim attribute mappings.
     * @return List<LocalClaim>.
     */
    private List<LocalClaim> createLocalClaimList(String userStoreId,
                                                  List<ClaimAttributeMapping> claimAttributeMappingList) {

        List<LocalClaim> localClaimList = new ArrayList<>();

        for (ClaimAttributeMapping claimAttributeMapping : claimAttributeMappingList) {
            AttributeMapping attributeMapping = new AttributeMapping(userStoreId,
                    claimAttributeMapping.getMappedAttribute());
            LocalClaim localClaim = new LocalClaim(claimAttributeMapping.getClaimURI());
            localClaim.setMappedAttribute(attributeMapping);
            localClaimList.add(localClaim);
        }
        return localClaimList;
    }

    /**
     * Construct response list with configured user stores details.
     *
     * @param userStoreDTOS array of UserStoreDTO object.
     * @return List<UserStoreListResponse>.
     */
    private List<UserStoreListResponse> buildUserStoreListResponse(UserStoreDTO[] userStoreDTOS,
                                                                   String requiredAttributes) {

        List<UserStoreListResponse> userStoreListResponseToAdd = new ArrayList<>();
        Map<String, Boolean> userStoreManagersType = UserStoreManagerRegistry.getUserStoreManagersType();
        if (ArrayUtils.isNotEmpty(userStoreDTOS)) {
            for (UserStoreDTO jsonObject : userStoreDTOS) {
                UserStoreListResponse userStoreList = new UserStoreListResponse();
                userStoreList.setDescription(jsonObject.getDescription());
                userStoreList.setName(jsonObject.getDomainId());
                userStoreList.setId(base64URLEncodeId(jsonObject.getDomainId()));
                userStoreList.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                                UserStoreConstants.USER_STORE_PATH_COMPONENT + "/%s",
                        base64URLEncodeId(jsonObject.getDomainId()))).toString());
                userStoreList.setEnabled(jsonObject.getDisabled() != null && !jsonObject.getDisabled());
                userStoreList.setTypeName(getUserStoreTypeName(jsonObject.getClassName()));

                if (StringUtils.isNotBlank(requiredAttributes)) {
                    String[] requiredAttributesArray = requiredAttributes.split(REGEX_COMMA);
                    addUserstoreProperties(jsonObject, userStoreList, Arrays.asList(requiredAttributesArray));
                }

                if (userStoreManagersType.containsKey(jsonObject.getClassName())) {
                    userStoreList.setIsLocal(userStoreManagersType.get(jsonObject.getClassName()));
                }

                userStoreListResponseToAdd.add(userStoreList);
            }
        }
        return userStoreListResponseToAdd;
    }

    /**
     * Add requested user store properties to the response.
     *
     * @param userStoreDTO            userStoreDTO object.
     * @param userStoreListResponse   userStoreListResponse object.
     * @param requestedAttributesList Requested user store properties name list.
     */
    private void addUserstoreProperties(UserStoreDTO userStoreDTO, UserStoreListResponse userStoreListResponse,
                                        List<String> requestedAttributesList) {

        for (PropertyDTO propertyDTO : userStoreDTO.getProperties()) {
            if (requestedAttributesList.contains(propertyDTO.getName()) &&
                    StringUtils.isNotBlank(propertyDTO.getValue())) {
                AddUserStorePropertiesRes addUserStorePropertiesRes = new AddUserStorePropertiesRes();
                addUserStorePropertiesRes.setName(propertyDTO.getName());
                addUserStorePropertiesRes.setValue(propertyDTO.getValue());
                userStoreListResponse.addPropertiesItem(addUserStorePropertiesRes);
            }
        }
    }

    /**
     * Construct the user store type's meta.
     *
     * @param typeId        the type id of the user store.
     * @return MetaUserStoreType.
     */
    private MetaUserStoreType buildUserStoreMetaResponse(String typeId) {

        String typeName = base64URLDecodeId(typeId);

        Properties properties = UserStoreManagerRegistry.getUserStoreProperties(
                getUserStoreType(typeName));
        MetaUserStoreType metaUserStore = new MetaUserStoreType();
        UserStorePropertiesRes userStorePropertiesRes = new UserStorePropertiesRes();
        if ((properties != null)) {
            userStorePropertiesRes.mandatory(buildPropertiesRes(properties.getMandatoryProperties()));
            userStorePropertiesRes.optional(buildPropertiesRes(properties.getOptionalProperties()));
            userStorePropertiesRes.advanced(buildPropertiesRes(properties.getAdvancedProperties()));
        }
        metaUserStore.setProperties(userStorePropertiesRes);
        metaUserStore.setTypeId(typeId);
        metaUserStore.setTypeName(typeName);
        metaUserStore.setClassName(getUserStoreType(typeName));
        try {
            metaUserStore.setIsLocal(UserStoreManagerRegistry.isLocalUserStore(getUserStoreType(typeName)));
        } catch (UserStoreException e) {
            LOG.error(String.format("Cannot found user store manager type for user store manager: %s",
                    getUserStoreType(typeName)), e);
        }
        return metaUserStore;
    }

    /**
     * Construct properties list in the response.
     *
     * @param properties array of user store properties.
     * @return List<PropertiesRes>.
     */
    private List<PropertiesRes> buildPropertiesRes(Property[] properties) {

        List<PropertiesRes> propertiesToAdd = new ArrayList<>();

        for (Property property : properties) {
            PropertiesRes propertiesRes = new PropertiesRes();
            propertiesRes.setName(property.getName());
            propertiesRes.setDefaultValue(property.getValue());
            propertiesRes.setDescription(property.getDescription());
            propertiesRes.setAttributes(buildAttributes(property.getChildProperties()));
            propertiesToAdd.add(propertiesRes);
        }
        return propertiesToAdd;
    }

    /**
     * Constructs attributes for individual properties.
     *
     * @param properties Array of user store properties.
     * @return List<Attribute>
     */
    private List<Attribute> buildAttributes(Property[] properties) {

        if (ArrayUtils.isEmpty(properties)) {
            return null;
        }

        List<Attribute> attributes = new ArrayList<>();
        for (Property property : properties) {
            Attribute attribute = new Attribute();
            attribute.setName(property.getName());
            attribute.setValue(property.getValue());
            attributes.add(attribute);
        }
        return attributes;
    }

    /**
     * Retrieve the className for a given type.
     *
     * @param typeName user store type name.
     * @return user store class name.
     */
    private String getUserStoreType(String typeName) {

        HashMap<String, String> userStoreMap = getHashMap();
        for (Map.Entry<String, String> stringEntry : userStoreMap.entrySet()) {
            if (typeName.equals(((Map.Entry) stringEntry).getValue())) {
                return (String) ((Map.Entry) stringEntry).getKey();
            }
        }
        return null;
    }

    /**
     * Retrieve the user store type name for a given class name.
     *
     * @param className user store class name.
     * @return user store type name.
     */
    private String getUserStoreTypeName(String className) {

        HashMap<String, String> userStoreMap = getHashMap();
        for (Map.Entry<String, String> stringEntry : userStoreMap.entrySet()) {
            if (className.equals(((Map.Entry) stringEntry).getKey())) {
                return (String) ((Map.Entry) stringEntry).getValue();
            }
        }
        return null;
    }

    /**
     * Construct a map with class name and with the corresponding type name.
     *
     * @return HashMap.
     */
    private HashMap<String, String> getHashMap() {

        Set<String> classNames = UserStoreManagerRegistry.getUserStoreManagerClasses();
        HashMap<String, String> userStoreMap = new HashMap<>();

        for (String className : classNames) {
            userStoreMap.put(className, className.substring(className.lastIndexOf('.') + 1));
        }
        return userStoreMap;
    }

    /**
     * To construct the base 64 url encoded id.
     *
     * @param id domain name.
     * @return encoded string value.
     */
    private String base64URLEncodeId(String id) {

        return Base64.getUrlEncoder().withoutPadding().encodeToString(id.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * To construct the base 64 url decoded value.
     *
     * @param id domain name.
     * @return decoded string value.
     */
    private String base64URLDecodeId(String id) {

        return new String(Base64.getUrlDecoder().decode(id), StandardCharsets.UTF_8);
    }

    /**
     * Construct PropertyDTO array for POST request.
     *
     * @param userStoreReq {@link UserStoreReq}.
     * @return PropertyDTO[].
     */
    private PropertyDTO[] createPropertyListDTO(UserStoreReq userStoreReq) {

        List<org.wso2.carbon.identity.api.server.userstore.v1.model.Property> values = userStoreReq.getProperties();
        ArrayList<PropertyDTO> propertiesToAdd = new ArrayList<>();

        for (org.wso2.carbon.identity.api.server.userstore.v1.model.Property value : values) {
            PropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.setName(value.getName());
            propertyDTO.setValue(value.getValue());
            propertiesToAdd.add(propertyDTO);
        }
        return generatePropertiesWithUniqueIDProperty (propertiesToAdd);
    }

    /**
     * Construct PropertyDTO array with UniqueID.
     *
     * @param propertiesToAdd Array list of properties.
     * @return PropertyDTO[].
     */
    private PropertyDTO[] generatePropertiesWithUniqueIDProperty(ArrayList<PropertyDTO> propertiesToAdd) {

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setName("UniqueID");
        propertyDTO.setValue(UUID.randomUUID().toString());
        propertiesToAdd.add(propertyDTO);

        return propertiesToAdd.toArray(new PropertyDTO[0]);
    }

    /**
     * To create UserStoreResponse object for the put request.
     *
     * @param userStoreReq {@link UserStoreReq}.
     * @return UserStoreResponse.
     */
    private UserStoreResponse buildUserStoreResponseDTO(UserStoreReq userStoreReq) {

        UserStoreResponse userStoreResponseDTO = new UserStoreResponse();
        userStoreResponseDTO.setId((base64URLEncodeId(userStoreReq.getName())));
        userStoreResponseDTO.setName(userStoreReq.getName());
        userStoreResponseDTO.setTypeId(userStoreReq.getTypeId());
        userStoreResponseDTO.setTypeName(base64URLDecodeId(userStoreReq.getTypeId()));
        userStoreResponseDTO.setDescription(userStoreReq.getDescription());
        userStoreResponseDTO.setProperties(buildUserStorePropertiesRes(userStoreReq));
        userStoreResponseDTO.setClaimAttributeMappings(userStoreReq.getClaimAttributeMappings());
        return userStoreResponseDTO;
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, UserStoreConstants.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @param data   Additional data.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, UserStoreConstants.ErrorMessage error, String... data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @param data     Error data.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(UserStoreConstants.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    /**
     * To build error description.
     *
     * @param errorEnum Error Message information.
     * @param data      Error data.
     * @return ErrorDescription.
     */
    private String buildErrorDescription(UserStoreConstants.ErrorMessage errorEnum, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            if (data.length == 1) {
                errorDescription = String.format(errorEnum.getDescription(), (Object) data);
            } else {
                errorDescription = String.format(errorEnum.getDescription(), (Object[]) data);
            }
        } else {
            errorDescription = errorEnum.getDescription();
        }
        return errorDescription;
    }

    /**
     * To return error responses for the input params for the get request which are not yet supported by the server.
     *
     * @param limit  items per page.
     * @param offset to specify the offset param.
     * @param filter to specify the filtering capabilities.
     * @param sort   to specify the sorting order.
     */
    private void handleNotImplementedBehaviour(Integer limit, Integer offset, String filter, String sort) {

        UserStoreConstants.ErrorMessage errorEnum = null;

        if (limit != null) {
            errorEnum = UserStoreConstants.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (offset != null) {
            errorEnum = UserStoreConstants.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (filter != null) {
            errorEnum = UserStoreConstants.ErrorMessage.ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
        } else if (sort != null) {
            errorEnum = UserStoreConstants.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;

            throw new APIError(status, errorResponse);
        }
    }

    /**
     * To check whether API request has all user store mandatory properties or not.
     *
     * @param userStoreReq {@link UserStoreReq}
     */
    private void validateMandatoryProperties(UserStoreReq userStoreReq) {

        validateUserStoreProperty(userStoreReq);
        String userStoreType = getUserStoreType(base64URLDecodeId(userStoreReq.getTypeId()));
        if (StringUtils.isBlank(userStoreType)) {
            throw handleException(Response.Status.BAD_REQUEST,
                    UserStoreConstants.ErrorMessage.ERROR_CODE_INVALID_INPUT);
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Property[] mandatoryProperties = UserStoreManagerRegistry.
                getUserStoreProperties(userStoreType).getMandatoryProperties();
        for (org.wso2.carbon.identity.api.server.userstore.v1.model.Property property : userStoreReq.getProperties()) {
            hashMap.put(property.getName(), property.getValue());
        }
        for (Property property : mandatoryProperties) {
            if (!hashMap.containsKey(property.getName()) || (StringUtils.isEmpty(hashMap.get(property.getName())))) {
                UserStoreConstants.ErrorMessage errorEnum =
                        UserStoreConstants.ErrorMessage.ERROR_CODE_MANDATORY_PROPERTIES_NOT_FOUND;
                Response.Status status = Response.Status.NOT_FOUND;
                throw handleException(status, errorEnum);
            }
        }
    }

    /**
     * Handle handleIdentityUserStoreMgtException, ie, handle the appropriate client and server exception and set
     * proper API Error Response.
     *
     * @param exception Exception thrown
     * @param errorEnum Corresponding error enum
     * @return API Error object.
     */
    private APIError handleIdentityUserStoreMgtException(IdentityUserStoreMgtException exception,
                                                         UserStoreConstants.ErrorMessage errorEnum) {

        Response.Status status;
        ErrorResponse errorResponse;
        if (exception instanceof IdentityUserStoreServerException) {
            errorResponse = getErrorBuilder(errorEnum).build(LOG, exception, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
            return handleIdentityUserStoreException(exception, errorResponse, status);
        } else if (exception instanceof IdentityUserStoreClientException) {
            errorResponse = getErrorBuilder(errorEnum).build(LOG, exception.getMessage());
            if (ERROR_CODE_RESOURCE_LIMIT_REACHED.equals(exception.getErrorCode())) {
                return handleResourceLimitReached();
            }
            // Send client error with specific error code or as a BAD request.
            status = Response.Status.BAD_REQUEST;
            return handleIdentityUserStoreException(exception, errorResponse, status);
        } else {
            // Internal Server error
            errorResponse = getErrorBuilder(errorEnum).build(LOG, exception, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
            return new APIError(status, errorResponse);
        }
    }

    private APIError handleResourceLimitReached() {

        ErrorResponse errorResponse = getErrorBuilder(ERROR_CODE_USER_STORE_LIMIT_REACHED)
                .build(LOG, ERROR_CODE_USER_STORE_LIMIT_REACHED.getDescription());

        Response.Status status = Response.Status.FORBIDDEN;
        return new APIError(status, errorResponse);
    }

    /**
     * Handle handleIdentityUserStoreException, extract the error code and the error message from the corresponding
     * exception and set it to the API Error Response.
     *
     * @param exception     Exception thrown
     * @param errorResponse Corresponding error response
     * @return API Error object.
     */
    private APIError handleIdentityUserStoreException(IdentityUserStoreMgtException exception,
                                                      ErrorResponse errorResponse, Response.Status status) {

        if (exception.getErrorCode() != null) {
            String errorCode = exception.getErrorCode();
            errorCode = errorCode.contains(UserStoreConstants.SECONDARY_USER_STORE_PREFIX) ?
                    errorCode : UserStoreConstants.SECONDARY_USER_STORE_PREFIX + errorCode;
            errorResponse.setCode(errorCode);
        }
        errorResponse.setDescription(exception.getMessage());
        return new APIError(status, errorResponse);
    }

    /**
     * Handle ClaimManagementException, extract the error code and the error message from the corresponding
     * exception and set it to the API Error Response.
     *
     * @param exception     Exception thrown.
     * @param errorEnum     Corresponding error enum.
     * @return API Error object.
     */
    private APIError handleClaimManagementException(ClaimMetadataException exception,
                                                    UserStoreConstants.ErrorMessage errorEnum) {

        Response.Status status;
        ErrorResponse errorResponse;
        if (exception instanceof ClaimMetadataClientException) {
            errorResponse = getErrorBuilder(errorEnum).build(LOG, exception.getMessage());
            status = Response.Status.BAD_REQUEST;
            return handleClaimManagementClientException(exception, errorResponse, status);
        } else {
            // Internal Server error
            errorResponse = getErrorBuilder(errorEnum).build(LOG, exception, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
            return new APIError(status, errorResponse);
        }
    }

    /**
     * Handle ClaimManagementException, extract the error code and the error message from the corresponding
     * exception and set it to the API Error Response.
     *
     * @param exception     Exception thrown.
     * @param errorResponse Corresponding error response.
     * @param status        Corresponding status response.
     * @return API Error object.
     */
    private APIError handleClaimManagementClientException(ClaimMetadataException  exception,
                                                          ErrorResponse errorResponse, Response.Status status) {

        if (exception.getErrorCode() != null) {
            String errorCode = exception.getErrorCode();
            errorCode = errorCode.contains(UserStoreConstants.CLAIM_MANAGEMENT_PREFIX) ?
                    errorCode : UserStoreConstants.CLAIM_MANAGEMENT_PREFIX + errorCode;
            errorResponse.setCode(errorCode);
        }
        errorResponse.setDescription(exception.getMessage());
        return new APIError(status, errorResponse);
    }

    /**
     * Validate userstore update request.
     *
     * @param domainID     Userstore domain ID
     * @param userStoreReq UserStoreReq object.
     * @throws IdentityUserStoreClientException If request is invalid.
     */
    private void validateUserstoreUpdateRequest(String domainID, UserStoreReq userStoreReq)
            throws IdentityUserStoreClientException {

       validateUserStoreProperty(userStoreReq);
        if (StringUtils.isBlank(domainID)) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_ID.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_ID.getMessage());
        }
        if (userStoreReq == null) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_REQUEST_BODY_NOT_FOUND.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_REQUEST_BODY_NOT_FOUND.getMessage());
        }
        if (StringUtils.isBlank(userStoreReq.getName())) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_NAME.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_NAME.getMessage());
        }
        if (!userStoreReq.getName().equals(base64URLDecodeId(domainID))) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_DOMAIN_ID_DOES_NOT_MATCH_WITH_NAME.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_DOMAIN_ID_DOES_NOT_MATCH_WITH_NAME.getMessage());
        }
    }

    /**
     * Validate userstore domain Id.
     *
     * @param userstoreDomainId    Userstore domain ID.
     * @throws IdentityUserStoreClientException If request is invalid.
     */
    private void validateUserstore(String userstoreDomainId) throws UserStoreException,
            IdentityUserStoreClientException {

        if (StringUtils.isBlank(userstoreDomainId)) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_ID.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_EMPTY_DOMAIN_ID.getMessage());
        }
        if (!isUserStoreExists(base64URLDecodeId(userstoreDomainId))) {
            throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.ERROR_CODE_NOT_FOUND);
        }
    }

    private String extractPropertyName(String pathProp) {

        String name = null;
        String[] splittedPath = pathProp.split("/");
        if (splittedPath.length > 2) {
            name = splittedPath[2];
        }
        return name;
    }

    /**
     * Get user store attributes mappings for a given user store type id.
     *
     * @param typeId                       String user store type id.
     * @param includeIdentityClaimMappings Whether to include claim mapping for identity claims with other userstore.
     *                                     attributes.
     * @return UserStoreAttributeMapping user store attribute mappings.
     */
    public UserStoreAttributeMappingResponse getUserStoreAttributeMappings(String typeId,
                                                                           boolean includeIdentityClaimMappings) {

        Set<String> classNames;
        String userStoreName = getUserStoreType(base64URLDecodeId(typeId));
        if (StringUtils.isBlank(userStoreName)) {
            throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage.
                    ERROR_CODE_INVALID_INPUT);
        }
        try {
            classNames = userStoreConfigService.getAvailableUserStoreClasses();
            if (CollectionUtils.isEmpty(classNames) || !classNames.contains(userStoreName)) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.
                        ERROR_CODE_NOT_FOUND);
            }
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE;
            throw handleIdentityUserStoreMgtException(e, errorEnum);
        }
        UserStoreAttributeMappingResponse userStoreAttributeMappingResponse = new UserStoreAttributeMappingResponse();
        List<UserStoreAttribute> attributeMappings = getAttributeMappings(userStoreName,
                includeIdentityClaimMappings);
        userStoreAttributeMappingResponse = userStoreAttributeMappingResponse.attributeMappings(
                new AttributeMappingsToApiModel().apply(attributeMappings)).typeId(typeId).typeName(userStoreName);
        try {
            boolean isLocal = UserStoreManagerRegistry.isLocalUserStore(userStoreName);
            userStoreAttributeMappingResponse.setIsLocal(isLocal);
        } catch (UserStoreException e) {
            LOG.error(String.format("Userstore type is not found for %s", userStoreName), e);
        }
        return userStoreAttributeMappingResponse;
    }

    /**
     * Get user store attribute mappings for a given user store typeId.
     *
     * @param userStoreName                String user store name (base64 decoded user store id).
     * @param includeIdentityClaimMappings Whether to include claim mapping for identity claims with other userstore
     *                                     attributes.
     * @return List of user store attribute mappings for the given typeId.
     */
    private List<UserStoreAttribute> getAttributeMappings(String userStoreName,
                                                          boolean includeIdentityClaimMappings) {

        try {
            UserStoreAttributeMappings userStoreAttributeMappings = userStoreConfigService.
                    getUserStoreAttributeMappings();
            Map<String, Map<String, UserStoreAttribute>> mapping = userStoreAttributeMappings
                    .getUserStoreAttributeMappings();
            Map<String, UserStoreAttribute> userStoreAttributeMap =
                    userStoreAttributeMappings.getDefaultUserStoreAttributeMappings();
            if (mapping.containsKey(userStoreName)) {
                userStoreAttributeMap = mapping.get(userStoreName);
            }
            if (!includeIdentityClaimMappings) {
                // Remove identity claim mappings by iterating through all the claim mappings.
                return excludeIdentityClaims(userStoreAttributeMap);
            }
            return new ArrayList<>(userStoreAttributeMap.values());
        } catch (IdentityUserStoreMgtException e) {
            LOG.error("Error occurred while retrieving user store attribute metadata", e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, UserStoreConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_USER_STORE_ATTRIBUTE_METADATA);
        }
    }

    /**
     * Exclude the userstore attributes with identity claims.
     *
     * @param userStoreAttributeMap Userstore attribute map.
     * @return List of UserStoreAttributeDOs.
     */
    private List<UserStoreAttribute> excludeIdentityClaims(
            Map<String, UserStoreAttribute> userStoreAttributeMap) {

        List<UserStoreAttribute> userstoreMappings = new ArrayList<>();
        for (Map.Entry<String, UserStoreAttribute> entry : userStoreAttributeMap.entrySet()) {
            UserStoreAttribute userStoreAttribute = entry.getValue();
            if (!userStoreAttribute.getClaimUri().startsWith(UserCoreConstants.ClaimTypeURIs.IDENTITY_CLAIM_URI)) {
                userstoreMappings.add(userStoreAttribute);
            }
        }
        return userstoreMappings;
    }

    private FileContent generateFileFromModel(String fileType, UserStoreConfigurations userStoreConfigs)
            throws UserStoreException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsing userstore object to file content of type: " + fileType);
        }
        String mediaType = Util.getMediaType(fileType);
        switch (mediaType) {
            case MEDIA_TYPE_XML:
                return parseUserStoreToXml(userStoreConfigs);
            case MEDIA_TYPE_JSON:
                return parseUserStoreToJson(userStoreConfigs);
            case MEDIA_TYPE_YAML:
                return parseUserStoreToYaml(userStoreConfigs);
            default:
                LOG.warn(String.format("Using the default YAML parsing for requested file type: %s for export.",
                        fileType));
                return parseUserStoreToYaml(userStoreConfigs);
        }
    }

    private FileContent parseUserStoreToYaml(UserStoreConfigurations userStoreConfigs)
            throws UserStoreException {

        StringBuilder fileNameSB = new StringBuilder(userStoreConfigs.getName());
        fileNameSB.append(YAML_FILE_EXTENSION);
        Yaml yaml = new Yaml();
        try {
            return new FileContent(fileNameSB.toString(), MEDIA_TYPE_YAML, yaml.dump(userStoreConfigs));
        } catch (YAMLException e) {
            throw new UserStoreException("Error when parsing userstore to YAML file.", e);
        }
    }

    private FileContent parseUserStoreToXml(UserStoreConfigurations userStoreConfigs)
            throws UserStoreException {

        StringBuilder fileNameSB = new StringBuilder(userStoreConfigs.getName());
        fileNameSB.append(XML_FILE_EXTENSION);

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(UserStoreConfigurations.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(userStoreConfigs, stringWriter);
            return new FileContent(fileNameSB.toString(), MEDIA_TYPE_XML, stringWriter.toString());
        } catch (JAXBException e) {
            throw new UserStoreException("Error when parsing userstore to XML file.", e);
        }
    }

    private FileContent parseUserStoreToJson(UserStoreConfigurations userStoreConfigs)
            throws UserStoreException {

        StringBuilder fileNameSB = new StringBuilder(userStoreConfigs.getName());
        fileNameSB.append(JSON_FILE_EXTENSION);
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        try {
            return new FileContent(fileNameSB.toString(), MEDIA_TYPE_JSON,
                    objectMapper.writeValueAsString(userStoreConfigs));
        } catch (JsonProcessingException e) {
            throw new UserStoreException("Error when parsing userstore to JSON file.", e);
        }
    }

    private UserStoreReq getUserStoreFromFile(InputStream fileInputStream, Attachment fileDetail)
            throws UserStoreException, IdentityUserStoreClientException {

        UserStoreConfigurations userStoreConfigs;
        try {
            FileContent userStoreFileContent = new FileContent(fileDetail.getDataHandler().getName(),
                    fileDetail.getDataHandler().getContentType(),
                    IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name()));
            userStoreConfigs = generateModelFromFile(userStoreFileContent);
        } catch (IOException e) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getMessage());
        } catch (UserStoreException e) {
            throw new UserStoreException("Error when generating the userstore model from file for the userstore: " +
                    fileDetail.getDataHandler().getName(), e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }

        UserStoreReq userStoreConfigsToImport = new UserStoreReq();
        userStoreConfigsToImport.setName(userStoreConfigs.getName());
        userStoreConfigsToImport.setDescription(userStoreConfigs.getDescription());
        userStoreConfigsToImport.setTypeId(userStoreConfigs.getTypeId());
        userStoreConfigsToImport.setClaimAttributeMappings(userStoreConfigs.getClaimAttributeMappings());
        userStoreConfigsToImport.setProperties(userStoreConfigs.getProperties());

        return userStoreConfigsToImport;
    }

    private UserStoreConfigurations generateModelFromFile(FileContent fileContent)
            throws UserStoreException, IdentityUserStoreClientException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Parsing user store from file: %s of type: %s.", fileContent.getFileName(),
                    fileContent.getFileType()));
        }
        if (StringUtils.isEmpty(fileContent.getContent())) {
            throw new IdentityUserStoreClientException(
                    UserStoreConstants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getCode(),
                    UserStoreConstants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getMessage());
        }

        switch (Util.getMediaType(fileContent.getFileType())) {
            case MEDIA_TYPE_XML:
                return parseUserStoreFromXml(fileContent);
            case MEDIA_TYPE_JSON:
                return parseUserStoreFromJson(fileContent);
            case MEDIA_TYPE_YAML:
                return parseUserStoreFromYaml(fileContent);
            default:
                LOG.warn(String.format("Unsupported media type %s for file %s. Defaulting to YAML parsing.",
                        fileContent.getFileType(), fileContent.getFileName()));
                return parseUserStoreFromYaml(fileContent);
        }
    }

    private UserStoreConfigurations parseUserStoreFromXml(FileContent fileContent) throws UserStoreException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(UserStoreConfigurations.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (UserStoreConfigurations) unmarshaller.unmarshal(new StringReader(fileContent.getContent()));
        } catch (JAXBException e) {
            throw new UserStoreException(String.format("Error in reading " +
                    "XML file configuration for the userstore: %s.", fileContent.getFileName()), e);
        }
    }

    private UserStoreConfigurations parseUserStoreFromYaml(FileContent fileContent) throws UserStoreException {

        try {
            // Add trusted tags included in the Userstore YAML files.
            List<String> trustedTagList = new ArrayList<>();
            trustedTagList.add(UserStoreConfigurations.class.getName());

            LoaderOptions loaderOptions = new LoaderOptions();
            TagInspector tagInspector = new TrustedPrefixesTagInspector(trustedTagList);
            loaderOptions.setTagInspector(tagInspector);
            Yaml yaml = new Yaml(new Constructor(UserStoreConfigurations.class, loaderOptions));
            return yaml.loadAs(fileContent.getContent(), UserStoreConfigurations.class);
        } catch (YAMLException e) {
            throw new UserStoreException(String.format("Error in reading YAML file " +
                    "configuration for the userstore: %s.", fileContent.getFileName()), e);
        }
    }

    private UserStoreConfigurations parseUserStoreFromJson(FileContent fileContent) throws UserStoreException {

        try {
            return new ObjectMapper().readValue(fileContent.getContent(), UserStoreConfigurations.class);
        } catch (JsonProcessingException e) {
            throw new UserStoreException(String.format("Error in reading JSON " +
                    "file configuration for the userstore: %s.", fileContent.getFileName()), e);
        }
    }

    /**
     * Method to validate whether the user store request contains properties with invalid characters.
     *
     * @param userStoreReq User store request.
     */
    private void validateUserStoreProperty(UserStoreReq userStoreReq) {

        Pattern pattern = Pattern.compile(EXPRESSION_LANGUAGE_REGEX);
        if (userStoreReq != null) {
            if (StringUtils.isNotBlank(userStoreReq.getName())) {
                if (pattern.matcher(userStoreReq.getName()).matches() || !isValidFileName(userStoreReq.getName())) {
                    throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT);
                }
            }
            if (StringUtils.isNotBlank(userStoreReq.getDescription()) &&
                            pattern.matcher(userStoreReq.getDescription()).matches()) {
                throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                        .ERROR_CODE_INVALID_INPUT);
            } else if (userStoreReq.getProperties() != null) {
                for (org.wso2.carbon.identity.api.server.userstore.v1.model.Property property :
                        userStoreReq.getProperties()) {
                    if (!PASSWORD.equals(property.getName()) && pattern.matcher(property.getValue()).matches()) {
                        throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT);
                    }
                }
            }
        }
    }
}
