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

package org.wso2.carbon.identity.api.server.userstore.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.userstore.common.UserStoreConfigServiceHolder;
import org.wso2.carbon.identity.api.server.userstore.common.UserStoreConstants;

import org.wso2.carbon.identity.api.server.userstore.v1.model.AddUserStorePropertiesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.AvailableUserStoreClassesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ConnectionEstablishedResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.MetaUserStoreType;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PropertiesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.RDBMSConnectionReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreConfigurationsRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreListResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStorePropertiesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreResponse;

import org.wso2.carbon.identity.user.store.configuration.UserStoreConfigService;
import org.wso2.carbon.identity.user.store.configuration.dto.PropertyDTO;
import org.wso2.carbon.identity.user.store.configuration.dto.UserStoreDTO;
import org.wso2.carbon.identity.user.store.configuration.utils.IdentityUserStoreMgtException;
import org.wso2.carbon.user.api.Properties;
import org.wso2.carbon.user.api.Property;
import org.wso2.carbon.user.core.tracker.UserStoreManagerRegistry;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Call internal osgi services to perform user store related operations.
 */
public class ServerUserStoreService {

    private static final Log LOG = LogFactory.getLog(ServerUserStoreService.class);

    /**
     * Add a userStore {@link UserStoreReq}.
     *
     * @param userStoreReq {@link UserStoreReq} to insert.
     * @return UserStoreResponse
     */
    public UserStoreResponse addUserStore(UserStoreReq userStoreReq) {

        try {
            UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
            userStoreConfigService.addUserStore(createUserStoreDTO(userStoreReq));
            return buildUserStoreResponseDTO(userStoreReq);
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_USER_STORE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Delete a userStore by its domainId.
     *
     * @param userstoreDomainId base64 encoded url value of userStore domain Id.
     */
    public void deleteUserStore(String userstoreDomainId) {

        try {
            UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.
                    getUserStoreConfigService();
            userStoreConfigService.deleteUserStore(base64URLDecodeId(userstoreDomainId));
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_USER_STORE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
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

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.
                getUserStoreConfigService();
        //domainName and typeName are not allowed to edit.
        try {
            userStoreConfigService.updateUserStoreByDomainName(base64URLDecodeId(domainId),
                    createUserStoreDTO(userStoreReq));
            return buildUserStoreResponseDTO(userStoreReq);
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_USER_STORE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * To retrieve the available user store classes.
     *
     * @return List<AvailableUserStoreClassesRes>.
     */
    public List<AvailableUserStoreClassesRes> getAvailableUserStoreTypes() {

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.
                getUserStoreConfigService();
        Set<String> classNames;
        try {
            classNames = userStoreConfigService.getAvailableUserStoreClasses();
            List<AvailableUserStoreClassesRes> propertiesToAdd = new ArrayList<>();
            for (String className : classNames) {
                AvailableUserStoreClassesRes availableUserStoreClassesResDTO = new AvailableUserStoreClassesRes();
                availableUserStoreClassesResDTO.setClassName(className);
                availableUserStoreClassesResDTO.setTypeName(getUserStoreTypeName(className));
                availableUserStoreClassesResDTO.setTypeId(base64URLEncodeId(Objects.
                        requireNonNull(getUserStoreTypeName(className))));
                propertiesToAdd.add(availableUserStoreClassesResDTO);
            }
            return propertiesToAdd;
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_RETRIEVING_USER_STORE_TYPE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
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
    public List<UserStoreListResponse> getUserStoreList(Integer limit, Integer offset, String filter, String sort) {

        handleNotImplementedBehaviour(limit, offset, filter, sort);

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
        try {
            UserStoreDTO[] userStoreDTOS = userStoreConfigService.getUserStores();
            if (userStoreDTOS == null) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.ERROR_CODE_NOT_FOUND);
            }
            return buildUserStoreListResponse(userStoreDTOS);

        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Retrieve user store by its domain id.
     *
     * @param domainId the user store domain id.
     * @return UserStoreConfigurationsRes.
     */
    public UserStoreConfigurationsRes getUserStoreByDomainId(String domainId) {

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
        List<AddUserStorePropertiesRes> propertiesTobeAdd = new ArrayList<>();
        try {
            UserStoreDTO userStoreDTO = userStoreConfigService.getUserStore(base64URLDecodeId(domainId));
            if (userStoreDTO == null) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.
                        ERROR_CODE_NOT_FOUND);
            }
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
            return userStoreConfigurations;

        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE_BY_DOMAIN_ID;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Retrieve the meta of configured user store type.
     *
     * @param typeId the user store type id.
     * @param limit  items per page.
     * @param offset to specify the offset param.
     * @param filter to specify the filtering capabilities.
     * @param sort   to specify the sorting order.
     * @return MetaUserStoreType.
     */

    public List<MetaUserStoreType> getUserStoreManagerProperties(String typeId, Integer limit, Integer offset,
                                                                 String filter, String sort) {

        handleNotImplementedBehaviour(limit, offset, filter, sort);

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
        try {
            UserStoreDTO[] userStoreDTOS = userStoreConfigService.getUserStores();
            if (userStoreDTOS == null) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.ERROR_CODE_NOT_FOUND);
            }
            List<UserStoreDTO> userStoresByTypeNameList = new ArrayList<>();
            for (UserStoreDTO userStoreDTO : userStoreDTOS) {
                if (Objects.equals(getUserStoreType(base64URLDecodeId(typeId)), userStoreDTO.getClassName())) {
                    userStoresByTypeNameList.add(userStoreDTO);
                }
            }
            return buildUserStoreMetaResponse(userStoresByTypeNameList, typeId);

        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Check the connection heath for JDBC userstores.
     *
     * @param rdBMSConnectionReq {@link RDBMSConnectionReq}.
     * @return ConnectionEstablishedResponse.
     */
    public ConnectionEstablishedResponse testRDBMSConnection(RDBMSConnectionReq rdBMSConnectionReq) {

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
        ConnectionEstablishedResponse connectionEstablishedResponse = new ConnectionEstablishedResponse();
        boolean isConnectionEstablished;
        connectionEstablishedResponse.setConnection(false);
        try {
            isConnectionEstablished = userStoreConfigService.testRDBMSConnection("",
                    rdBMSConnectionReq.getDriverName(), rdBMSConnectionReq.getConnectionURL(),
                    rdBMSConnectionReq.getUsername(), rdBMSConnectionReq.getPassword(), "");
            if (isConnectionEstablished) {
                connectionEstablishedResponse.setConnection(true);
            }
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_DATASOURCE_CONNECTION;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
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

        for (PatchDocument patch : patchDocument) {
            //Only the Replace operation supported with PATCH request
            PatchDocument.OperationEnum operation = patch.getOperation();
            if (operation == PatchDocument.OperationEnum.REPLACE) {
                return performPatchReplace(domainId, patch.getPath(), patch.getValue());
            }
        }
        return null;
    }

    /**
     * To handle the patch REPLACE request.
     *
     * @param domainId user store domain id.
     * @param path     patch operation path
     * @param value    property value
     * @return UserStoreResponse
     */
    private UserStoreResponse performPatchReplace(String domainId, String path, String value) {

        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
        try {
            UserStoreDTO userStoreDTO = userStoreConfigService.getUserStore(base64URLDecodeId(domainId));
            if (userStoreDTO == null) {
                throw handleException(Response.Status.NOT_FOUND, UserStoreConstants.ErrorMessage.ERROR_CODE_NOT_FOUND);
            }
            PropertyDTO[] propertyDTOS = userStoreDTO.getProperties();
            if (path.startsWith(UserStoreConstants.USER_STORE_PROPERTIES)) {
                String[] propertiesList = path.split("/");
                for (PropertyDTO propertyDTO : propertyDTOS) {
                    if (propertiesList[2].equals(propertyDTO.getName())) {
                        propertyDTO.setValue(value);
                    }
                }
                return buildResponseForPatchReplace(userStoreDTO, propertyDTOS);
            } else if (path.equals(UserStoreConstants.USER_STORE_DESCRIPTION)) {
                userStoreDTO.setDescription(value);
                return buildResponseForPatchReplace(userStoreDTO, propertyDTOS);
            } else {
                throw handleException(Response.Status.BAD_REQUEST, UserStoreConstants.ErrorMessage
                        .ERROR_CODE_INVALID_INPUT);
            }
        } catch (IdentityUserStoreMgtException e) {
            UserStoreConstants.ErrorMessage errorEnum =
                    UserStoreConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_USER_STORE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
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
     * Construct response list with configured user stores details.
     *
     * @param userStoreDTOS array of UserStoreDTO object.
     * @return List<UserStoreListResponse>.
     */
    private List<UserStoreListResponse> buildUserStoreListResponse(UserStoreDTO[] userStoreDTOS) {

        List<UserStoreListResponse> userStoreListResponseToAdd = new ArrayList<>();
        for (UserStoreDTO jsonObject : userStoreDTOS) {
            UserStoreListResponse userStoreList = new UserStoreListResponse();
            userStoreList.setDescription(jsonObject.getDescription());
            userStoreList.setName(jsonObject.getDomainId());
            userStoreList.setId(base64URLEncodeId(jsonObject.getDomainId()));
            userStoreList.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                            UserStoreConstants.USER_STORE_PATH_COMPONENT + "/%s",
                    base64URLEncodeId(jsonObject.getDomainId()))).toString());
            userStoreListResponseToAdd.add(userStoreList);
        }
        return userStoreListResponseToAdd;
    }

    /**
     * Construct the configured user store type's meta.
     *
     * @param userStoreDTOS the list of {@link UserStoreDTO}.
     * @param typeId        the type id of the user store.
     * @return MetaUserStoreType.
     */
    private List<MetaUserStoreType> buildUserStoreMetaResponse(List<UserStoreDTO> userStoreDTOS, String typeId) {

        List<MetaUserStoreType> metaUserStoreTypes = new ArrayList<>();
        for (UserStoreDTO userStoreDTO : userStoreDTOS) {
            Properties properties = UserStoreManagerRegistry.getUserStoreProperties(userStoreDTO.getClassName());
            MetaUserStoreType metaUserStore = new MetaUserStoreType();
            UserStorePropertiesRes userStorePropertiesRes = new UserStorePropertiesRes();
            userStorePropertiesRes.mandatory(buildPropertiesRes(properties.getMandatoryProperties()));
            userStorePropertiesRes.optional(buildPropertiesRes(properties.getOptionalProperties()));
            userStorePropertiesRes.advanced(buildPropertiesRes(properties.getAdvancedProperties()));
            metaUserStore.setProperties(userStorePropertiesRes);
            metaUserStore.setTypeId(typeId);
            metaUserStore.setName(userStoreDTO.getDomainId());
            metaUserStore.setDescription(userStoreDTO.getDescription());
            metaUserStore.setTypeName(base64URLDecodeId(typeId));
            metaUserStore.setClassName(getUserStoreType(base64URLDecodeId(typeId)));
            metaUserStoreTypes.add(metaUserStore);

        }
        return metaUserStoreTypes;
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
            propertiesToAdd.add(propertiesRes);
        }
        return propertiesToAdd;
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
            LOG.info(((Map.Entry) stringEntry).getKey() + " = " + ((Map.Entry) stringEntry).getValue());
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
            LOG.info(((Map.Entry) stringEntry).getKey() + " = " + ((Map.Entry) stringEntry).getValue());
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
        return userStoreResponseDTO;
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param e         Exception.
     * @param errorEnum Error Message information.
     * @param status    Error status code.
     * @return APIError.
     */
    private APIError handleException(Exception e, UserStoreConstants.ErrorMessage errorEnum, Response.Status status) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, e, errorEnum.getDescription());
        return new APIError(status, errorResponse);
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
}
