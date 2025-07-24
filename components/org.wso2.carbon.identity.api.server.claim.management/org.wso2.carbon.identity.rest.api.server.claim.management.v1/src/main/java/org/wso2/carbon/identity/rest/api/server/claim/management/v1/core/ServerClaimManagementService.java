/*
 *  Copyright (c) (2019-2025), WSO2 LLC. (http://www.wso2.org).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.core;

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
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.claim.management.common.Constant;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.FileContent;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.common.error.bulk.BulkAPIError;
import org.wso2.carbon.identity.api.server.common.error.bulk.BulkErrorResponse;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataClientException;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.AttributeMapping;
import org.wso2.carbon.identity.claim.metadata.mgt.model.Claim;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ClaimDialect;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;
import org.wso2.carbon.identity.claim.metadata.mgt.model.LocalClaim;
import org.wso2.carbon.identity.claim.metadata.mgt.util.ClaimConstants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.util.OrganizationManagementUtil;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.AttributeMappingDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.AttributeProfileDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.DataType;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.InputFormatDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LabelValueDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LinkDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ProfilesDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.PropertyDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.model.ClaimDialectConfiguration;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.model.ClaimErrorDTO;
import org.wso2.carbon.identity.scim2.common.utils.SCIMCommonUtils;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.yaml.snakeyaml.DumperOptions;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static org.wso2.carbon.identity.api.server.claim.management.common.ClaimManagementDataHolder.getClaimMetadataManagementService;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.CMT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_CLAIMS_NOT_FOUND_FOR_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_DIALECT_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_EMPTY_ATTRIBUTE_MAPPINGS;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_EMPTY_MAPPED_ATTRIBUTES_IN_LOCAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_ADDING_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_ADDING_EXTERNAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_ADDING_LOCAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_EXTERNAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_LOCAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DIALECTS;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_EXTERNAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_EXTERNAL_CLAIMS;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_LOCAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_LOCAL_CLAIMS;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_EXTERNAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_LOCAL_CLAIM;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_EXTERNAL_CLAIM_CONFLICT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_EXTERNAL_CLAIM_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_INVALID_DIALECT_ID;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_INVALID_USERSTORE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_LOCAL_CLAIM_CONFLICT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_LOCAL_CLAIM_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_UNAUTHORIZED_ORG_FOR_ATTRIBUTE_MAPPING_UPDATE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_UNAUTHORIZED_ORG_FOR_CLAIM_MANAGEMENT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_UNAUTHORIZED_ORG_FOR_CLAIM_PROPERTY_UPDATE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_UNAUTHORIZED_ORG_FOR_EXCLUDED_USER_STORES_PROPERTY_UPDATE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_USERSTORE_NOT_SPECIFIED_IN_MAPPINGS;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_SKIP_USER_STORE_UPDATE_FAILURE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_CHECKBOX;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_CHECKBOX_GROUP;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_DATE_PICKER;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_DROPDOWN;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_MULTI_SELECT_DROPDOWN;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_NUMBER_INPUT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_RADIO_GROUP;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_TEXT_INPUT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.INPUT_TYPE_TOGGLE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.LOCAL_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.LOCAL_DIALECT_PATH;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_CANONICAL_VALUES;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DATA_TYPE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DESCRIPTION;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DISPLAY_NAME;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DISPLAY_ORDER;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_INPUT_FORMAT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_SKIP_USER_STORE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_MULTI_VALUED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_PROFILES_PREFIX;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_READ_ONLY;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_REG_EX;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_REQUIRED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_SUB_ATTRIBUTES;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_SUPPORTED_BY_DEFAULT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_UNIQUENESS_SCOPE;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.RETURN_PREVIOUS_ADDITIONAL_PROPERTIES;
import static org.wso2.carbon.identity.api.server.common.Constants.JSON_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_JSON;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_XML;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_YAML;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.XML_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.Constants.YAML_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;
import static org.wso2.carbon.identity.claim.metadata.mgt.util.ClaimConstants.IS_SYSTEM_CLAIM;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ORGANIZATION_NOT_FOUND_FOR_TENANT;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Call internal osgi services to perform server claim management related operations.
 */
public class ServerClaimManagementService {

    private static final Log LOG = LogFactory.getLog(ServerClaimManagementService.class);
    private static final String REL_CLAIMS = "claims";
    private static final String IDENTITY_CLAIM_URI = "http://wso2.org/claims/identity/";
    private static final String HIDDEN_CLAIMS_IDENTITY_CONFIG = "HiddenClaims.HiddenClaim";
    private static final List<String> conflictErrorScenarios = Arrays.asList(
            ClaimConstants.ErrorMessage.ERROR_CODE_EXISTING_CLAIM_DIALECT.getCode(),
            ClaimConstants.ErrorMessage.ERROR_CODE_EXISTING_EXTERNAL_CLAIM_URI.getCode(),
            ClaimConstants.ErrorMessage.ERROR_CODE_EXISTING_LOCAL_CLAIM_URI.getCode()
    );

    private static final List<String> forbiddenErrorScenarios = Arrays.asList(
            ClaimConstants.ErrorMessage.ERROR_CODE_NO_RENAME_SYSTEM_DIALECT.getCode(),
            ClaimConstants.ErrorMessage.ERROR_CODE_NO_DELETE_SYSTEM_DIALECT.getCode(),
            ClaimConstants.ErrorMessage.ERROR_CODE_NO_DELETE_SYSTEM_CLAIM.getCode()
    );

    public static final String FALSE = "false";

    private final ClaimMetadataManagementService claimMetadataManagementService;
    private final OrganizationManager organizationManager;

    public ServerClaimManagementService(ClaimMetadataManagementService claimMetadataManagementService,
                                        OrganizationManager organizationManager) {

        this.claimMetadataManagementService = claimMetadataManagementService;
        this.organizationManager = organizationManager;
    }

    /**
     * Add a claim dialect.
     *
     * @param claimDialectReqDTO claimDialectReqDTO.
     * @return Resource identifier.
     */
    public String addClaimDialect(ClaimDialectReqDTO claimDialectReqDTO) {

        try {
            validateClaimModificationEligibility();
            getClaimMetadataManagementService().addClaimDialect(
                    createClaimDialect(claimDialectReqDTO),
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_ADDING_DIALECT,
                    claimDialectReqDTO.getDialectURI());
        }

        return getResourceId(claimDialectReqDTO.getDialectURI());
    }

    /**
     * Add a claim dialect using a dialectURI.
     *
     * @param dialectURI The dialect URI.
     * @return Resource identifier.
     */
    public String addClaimDialect(String dialectURI) {

        try {
            validateClaimModificationEligibility();
            claimMetadataManagementService.addClaimDialect(
                    createClaimDialect(dialectURI),
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_ADDING_DIALECT, dialectURI);
        }

        return getResourceId(dialectURI);
    }

    /**
     * Delete a claim dialect.
     *
     * @param dialectId dialectId.
     */
    public void deleteClaimDialect(String dialectId) {

        String claimDialectURI;
        try {
            validateClaimModificationEligibility();
            claimDialectURI = base64DecodeId(dialectId);
        } catch (Exception ignored) {
            // Ignoring the delete operation and return 204 response code, since the resource does not exist.
            return;
        }
        try {
            claimMetadataManagementService.removeClaimDialect(
                    new ClaimDialect(claimDialectURI),
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_DELETING_DIALECT, dialectId);
        }

    }

    /**
     * Retrieve a claim dialect.
     *
     * @param dialectId dialectId.
     * @return Claim dialect.
     */
    public ClaimDialectResDTO getClaimDialect(String dialectId) {

        try {
            List<ClaimDialect> claimDialectList = claimMetadataManagementService.getClaimDialects(
                    ContextLoader.getTenantDomainFromContext());
            String decodedDialectId;
            if (StringUtils.equals(dialectId, LOCAL_DIALECT_PATH)) {
                decodedDialectId = LOCAL_DIALECT;
            } else {
                decodedDialectId = base64DecodeId(dialectId);
            }
            ClaimDialect claimDialect = extractDialectFromDialectList(decodedDialectId, claimDialectList);

            if (claimDialect == null) {
                throw handleClaimManagementClientError(ERROR_CODE_DIALECT_NOT_FOUND, NOT_FOUND, dialectId);
            }

            return getClaimDialectResDTO(claimDialect);

        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_DIALECT, dialectId);
        }
    }

    /**
     * Retrieve all claim dialects.
     *
     * @param limit  limit (optional).
     * @param offset offset (optional).
     * @param filter filter (optional).
     * @param sort   sort (optional).
     * @return List of claim dialects.
     */
    public List<ClaimDialectResDTO> getClaimDialects(Integer limit, Integer offset, String filter, String sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);
        try {
            List<ClaimDialect> claimDialectList = claimMetadataManagementService.getClaimDialects(ContextLoader
                    .getTenantDomainFromContext());

            return getClaimDialectResDTOs(claimDialectList);

        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_DIALECTS);
        }
    }

    /**
     * Update claim dialect.
     * Due to the current implementation of the resource identifier of a new resource,
     * the resource id of the claim dialect changes when an update occurs.
     *
     * @param dialectId          dialectId.
     * @param claimDialectReqDTO claimDialectReqDTO.
     * @return Resource identifier.
     */
    public String updateClaimDialect(String dialectId, ClaimDialectReqDTO claimDialectReqDTO) {

        try {
            validateClaimModificationEligibility();
            // If the old and new dialect uri is the same we don't need to do a db update.
            if (!StringUtils.equals(base64DecodeId(dialectId), claimDialectReqDTO.getDialectURI())) {
                claimMetadataManagementService.renameClaimDialect(createClaimDialect(base64DecodeId(dialectId)),
                        createClaimDialect(claimDialectReqDTO),
                        ContextLoader.getTenantDomainFromContext());
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Skipping db update as the old dialectURI and the new dialectURI is " +
                            "the same. DialectURI: %s", claimDialectReqDTO.getDialectURI()));
                }
            }
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_UPDATING_DIALECT, dialectId);
        }

        // Since the dialects identifier has changed we have to send the new identifier in the location header.
        return getResourceId(claimDialectReqDTO.getDialectURI());
    }

    /**
     * Add a local claim.
     *
     * @param localClaimReqDTO localClaimReqDTO.
     * @return Resource identifier.
     */
    public String addLocalClaim(LocalClaimReqDTO localClaimReqDTO) {

        // Validate mandatory attributes.
        if (StringUtils.isBlank(localClaimReqDTO.getClaimURI())) {
            throw handleClaimManagementClientError(Constant.ErrorMessage.ERROR_CODE_CLAIM_URI_NOT_SPECIFIED,
                    BAD_REQUEST);
        }
        if (StringUtils.isBlank(localClaimReqDTO.getDisplayName())) {
            throw handleClaimManagementClientError(Constant.ErrorMessage.ERROR_CODE_CLAIM_DISPLAY_NAME_NOT_SPECIFIED,
                    BAD_REQUEST);
        }

        try {
            validateClaimModificationEligibility();
            validateAttributeMappings(localClaimReqDTO.getAttributeMapping());
            claimMetadataManagementService.addLocalClaim(createLocalClaim(localClaimReqDTO), ContextLoader
                    .getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_ADDING_LOCAL_CLAIM,
                    localClaimReqDTO.getClaimURI());
        } catch (UserStoreException e) {
            throw handleException(e, ERROR_CODE_ERROR_ADDING_LOCAL_CLAIM, localClaimReqDTO.getClaimURI());
        }

        return getResourceId(localClaimReqDTO.getClaimURI());
    }

    /**
     * Delete a local claim.
     *
     * @param claimId claimId.
     */
    public void deleteLocalClaim(String claimId) {

        String claimURI;
        try {
            validateClaimModificationEligibility();
            claimURI = base64DecodeId(claimId);
        } catch (Exception ignored) {
            // Ignoring the delete operation and return 204 response code, since the resource does not exist.
            return;
        }
        try {
            claimMetadataManagementService.removeLocalClaim(claimURI, ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_DELETING_LOCAL_CLAIM, claimId);
        }

    }

    /**
     * Retrieve a local claim.
     *
     * @param claimId claimId.
     * @return Local claim.
     */
    public LocalClaimResDTO getLocalClaim(String claimId) {

        try {
            List<LocalClaim> localClaimList = claimMetadataManagementService.getLocalClaims(ContextLoader
                    .getTenantDomainFromContext());

            LocalClaim localClaim = extractLocalClaimFromClaimList(base64DecodeId(claimId), localClaimList);

            if (localClaim == null) {
                throw handleClaimManagementClientError(ERROR_CODE_LOCAL_CLAIM_NOT_FOUND, NOT_FOUND, claimId);
            }

            return getLocalClaimResDTO(localClaim);

        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_LOCAL_CLAIM, claimId);
        }
    }

    /**
     * Retrieve all claims belonging to the local dialect.
     *
     * @param excludeIdentityClaims Exclude identity claims in the local dialect if this is set to true.
     * @param attributes            attributes filter (optional).
     * @param limit                 limit (optional).
     * @param offset                offset (optional).
     * @param filter                filter (optional).
     * @param sort                  sort (optional).
     * @return List of local claims.
     */
    public List<LocalClaimResDTO> getLocalClaims(Boolean excludeIdentityClaims, String attributes, Integer limit,
                                                 Integer offset, String filter, String sort) {

        return getLocalClaims(excludeIdentityClaims, attributes, limit, offset, filter, sort, false, null);
    }

    /**
     * Retrieve all claims belonging to the local dialect.
     *
     * @param excludeIdentityClaims     Exclude identity claims in the local dialect if this is set to true.
     * @param attributes                attributes filter (optional).
     * @param limit                     limit (optional).
     * @param offset                    offset (optional).
     * @param filter                    filter (optional).
     * @param sort                      sort (optional).
     * @param excludeHiddenClaims   Exclude hidden claims in the local dialect if this is set to true.
     * @return List of local claims.
     */
    public List<LocalClaimResDTO> getLocalClaims(Boolean excludeIdentityClaims, String attributes, Integer limit,
                                                 Integer offset, String filter, String sort,
                                                 Boolean excludeHiddenClaims, String profile) {

        handleNotImplementedCapabilities(attributes, limit, offset, filter, sort);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<LocalClaim> localClaimList;
            if (StringUtils.isEmpty(profile)) {
                localClaimList = claimMetadataManagementService.getLocalClaims(tenantDomain);
            } else {
                localClaimList = claimMetadataManagementService.getSupportedLocalClaimsForProfile(tenantDomain,
                        profile);
            }

            if (excludeIdentityClaims != null && excludeIdentityClaims) {
                localClaimList = localClaimList.stream()
                        .filter(claim -> !claim.getClaimURI().startsWith(IDENTITY_CLAIM_URI))
                        .collect(Collectors.toList());
            }

            if (excludeHiddenClaims != null && excludeHiddenClaims) {
                List<String> hiddenClaims = IdentityUtil.getPropertyAsList(HIDDEN_CLAIMS_IDENTITY_CONFIG);
                localClaimList = localClaimList.stream()
                        .filter(claim -> !hiddenClaims.contains(claim.getClaimURI()))
                        .collect(Collectors.toList());
            }

            return getLocalClaimResDTOs(localClaimList);

        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_LOCAL_CLAIMS);
        }
    }

    /**
     * Update a local claim.
     *
     * @param claimId          claimId.
     * @param localClaimReqDTO localClaimReqDTO.
     */
    public void updateLocalClaim(String claimId, LocalClaimReqDTO localClaimReqDTO) {

        try {
            if (isSubOrganizationContext()) {
                /*
                 * For sub organizations, only attribute mappings and ExcludedUserStores are allowed to be updated.
                 * Updating any other claim properties are restricted.
                 */
                validateLocalClaimUpdate(claimId, createLocalClaim(localClaimReqDTO));
            }

            if (!StringUtils.equals(base64DecodeId(claimId), localClaimReqDTO.getClaimURI())) {
                throw handleClaimManagementClientError(ERROR_CODE_LOCAL_CLAIM_CONFLICT, CONFLICT,
                        base64DecodeId(claimId));
            }
            if (StringUtils.isBlank(localClaimReqDTO.getDisplayName())) {
                throw handleClaimManagementClientError(
                        Constant.ErrorMessage.ERROR_CODE_CLAIM_DISPLAY_NAME_NOT_SPECIFIED,
                        BAD_REQUEST);
            }
            validateAttributeMappings(localClaimReqDTO.getAttributeMapping());
            validateSubAttributeUpdate(localClaimReqDTO);
            validateSystemClaimUpdate(localClaimReqDTO, claimId);
            validateDataTypeUpdates(localClaimReqDTO);
            validateAttributeInputFormat(localClaimReqDTO);

            claimMetadataManagementService.updateLocalClaim(createLocalClaim(localClaimReqDTO),
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_UPDATING_LOCAL_CLAIM, claimId);
        } catch (UserStoreException e) {
            throw handleException(e, ERROR_CODE_ERROR_ADDING_LOCAL_CLAIM, localClaimReqDTO.getClaimURI());
        }
        getResourceId(localClaimReqDTO.getClaimURI());
    }

    /**
     * Updates a claim dialect with related claims from an uploaded file.
     *
     * @param fileInputStream InputStream representing the uploaded claim dialect file.
     * @param fileDetail      Attachment object with metadata about the uploaded claim dialect file.
     * @param preserveClaims  Boolean value to indicate whether to merge and preserve the existing claims
     *                        or completely replace the existing claims set.
     * @return a String representing the updated claim dialect's resource identifier.
     */
    public String updateClaimDialectFromFile(InputStream fileInputStream, Attachment fileDetail,
                                             Boolean preserveClaims) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Updating claim dialect from file: %s", fileDetail.getDataHandler().getName()));
        }

        preserveClaims = (preserveClaims != null) ? preserveClaims : Boolean.FALSE;
        String dialectId = null;
        try {
            ClaimDialectConfiguration dialectConfiguration = getDialectFromFile(fileInputStream, fileDetail);
            dialectId = dialectConfiguration.getId();
            if (LOCAL_DIALECT_PATH.equals(dialectId)) {
                updateLocalClaims(dialectConfiguration.getLocalClaimReqDTOList(), preserveClaims);
            } else {
                updateClaimDialect(dialectId, dialectConfiguration.getClaimDialectReqDTO());
                updateExternalClaims(dialectId, dialectConfiguration.getExternalClaimReqDTOList(), preserveClaims);
            }
            return dialectId;
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_UPDATING_DIALECT, dialectId);
        }
    }

    private void updateLocalClaims(List<LocalClaimReqDTO> localClaimReqDTOList, boolean preserveClaims)
            throws ClaimMetadataException {

        List<ClaimErrorDTO> errors = new ArrayList<>();

        for (LocalClaimReqDTO localClaimReqDTO : localClaimReqDTOList) {
            try {
                if (StringUtils.isBlank(localClaimReqDTO.getClaimURI())) {
                    throw handleClaimManagementClientError(Constant.ErrorMessage.ERROR_CODE_EMPTY_LOCAL_CLAIM_URI,
                            BAD_REQUEST);
                }
                String claimId = getResourceId(localClaimReqDTO.getClaimURI());
                if (isLocalClaimExist(claimId)) {
                    updateLocalClaim(claimId, localClaimReqDTO);
                } else {
                    addLocalClaim(localClaimReqDTO);
                }
            } catch (APIError e) {
                ClaimErrorDTO claimErrorDTO = new ClaimErrorDTO(e.getResponseEntity());
                claimErrorDTO.setClaimURI(localClaimReqDTO.getClaimURI());
                errors.add(claimErrorDTO);
            }
        }
        if (preserveClaims) {
            deleteObsoleteLocalClaims(localClaimReqDTOList, errors);
        }
        if (!errors.isEmpty()) {
            throw handleClaimManagementBulkClientError(Constant.ErrorMessage.ERROR_CODE_UPDATING_LOCAL_CLAIMS,
                    BAD_REQUEST, errors, String.valueOf(errors.size()), String.valueOf(localClaimReqDTOList.size()));
        }
    }

    private void deleteObsoleteLocalClaims(List<LocalClaimReqDTO> localClaimReqDTOList, List<ClaimErrorDTO> errors)
            throws ClaimMetadataException {

        List<String> claimsToDelete =  getLocalClaimResDTOs(claimMetadataManagementService
                    .getLocalClaims(ContextLoader.getTenantDomainFromContext())).stream()
                    .map(LocalClaimResDTO::getClaimURI)
                    .filter(claimURI -> localClaimReqDTOList.stream()
                            .noneMatch(reqDTO -> reqDTO.getClaimURI().equals(claimURI)))
                    .collect(Collectors.toList());

        for (String claimURI : claimsToDelete) {
            try {
                deleteLocalClaim(getResourceId(claimURI));
            } catch (APIError e) {
                ClaimErrorDTO claimErrorDTO = new ClaimErrorDTO(e.getResponseEntity());
                claimErrorDTO.setClaimURI(claimURI);
                errors.add(claimErrorDTO);
            }
        }
    }

    private void updateExternalClaims(String dialectId, List<ExternalClaimReqDTO> externalClaimReqDTOList,
                                      boolean preserveClaims) throws ClaimMetadataException {

        if (preserveClaims) {
            deleteClaimDialect(dialectId);
            dialectId = addClaimDialect(base64DecodeId(dialectId));
            importExternalClaims(dialectId, externalClaimReqDTOList);
            return;
        }

        List<ClaimErrorDTO> errors = new ArrayList<>();

        for (ExternalClaimReqDTO externalClaimReqDTO : externalClaimReqDTOList) {
            try {
                if (StringUtils.isBlank(externalClaimReqDTO.getClaimURI())) {
                    throw handleClaimManagementClientError(Constant.ErrorMessage.ERROR_CODE_EMPTY_EXTERNAL_CLAIM_URI,
                            BAD_REQUEST);
                }
                String claimId = getResourceId(externalClaimReqDTO.getClaimURI());
                if (isExternalClaimExist(dialectId, claimId)) {
                    updateExternalClaim(dialectId, claimId, externalClaimReqDTO);
                } else {
                    addExternalClaim(dialectId, externalClaimReqDTO);
                }
            } catch (APIError e) {
                ClaimErrorDTO claimErrorDTO = new ClaimErrorDTO(e.getResponseEntity());
                claimErrorDTO.setClaimURI(externalClaimReqDTO.getClaimURI());
                errors.add(claimErrorDTO);
            }
        }
        if (!errors.isEmpty()) {
            throw handleClaimManagementBulkClientError(Constant.ErrorMessage.ERROR_CODE_UPDATING_EXTERNAL_CLAIMS,
                    BAD_REQUEST, errors, String.valueOf(errors.size()), String.valueOf(externalClaimReqDTOList.size()));
        }
    }

    /**
     * Exports a claim dialect with related claims in the specified file type.
     *
     * @param dialectId     ID of the claim dialect to export.
     * @param fileType      Type of file to export the claim dialect to.
     * @return a FileContent object representing the exported claim dialect file.
     */
    public FileContent exportClaimDialectToFile(String dialectId, String fileType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Exporting Claim Dialect for ID %s as a %s file.", dialectId, fileType));
        }
        if (StringUtils.isBlank(fileType)) {
            throw handleClaimManagementClientError(Constant.ErrorMessage.ERROR_CODE_MISSING_MEDIA_TYPE, BAD_REQUEST,
                                                   dialectId);
        }

        ClaimDialectConfiguration dialectConfiguration = new ClaimDialectConfiguration(getClaimDialect(dialectId));
        List<ClaimResDTO> claimResDTOList = new ArrayList<>();

        try {
            if (LOCAL_DIALECT_PATH.equals(dialectId)) {
                List<LocalClaimResDTO> localClaimResDTOList = getLocalClaimResDTOs(claimMetadataManagementService
                        .getLocalClaims(ContextLoader.getTenantDomainFromContext()));
                claimResDTOList.addAll(localClaimResDTOList);
                dialectConfiguration.setClaims(claimResDTOList);
            } else {
                List<ExternalClaim> externalClaimList = claimMetadataManagementService.getExternalClaims(
                        base64DecodeId(dialectId),
                        ContextLoader.getTenantDomainFromContext());
                List<ExternalClaimResDTO>  externalClaimResDTOList = getExternalClaimResDTOs(externalClaimList);
                claimResDTOList.addAll(externalClaimResDTOList);
                dialectConfiguration.setClaims(claimResDTOList);
            }
            return generateFileFromModel(fileType, dialectConfiguration);
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_DIALECT, dialectId);
        }
    }

    private FileContent generateFileFromModel(String fileType, ClaimDialectConfiguration dialectConfiguration)
            throws ClaimMetadataException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Parsing Claim Dialect object to file content of type: %s", fileType));
        }

        String fileName = getFormattedFileName(dialectConfiguration.getDialectURI());

        String mediaType = Util.getMediaType(fileType);
        switch (mediaType) {
            case MEDIA_TYPE_XML:
                return parseClaimDialectToXml(dialectConfiguration, fileName);
            case MEDIA_TYPE_JSON:
                return parseClaimDialectToJson(dialectConfiguration, fileName);
            case MEDIA_TYPE_YAML:
                return parseClaimDialectToYaml(dialectConfiguration, fileName);
            default:
                LOG.warn(String.format("Unsupported file type: %s requested for export. Defaulting to YAML parsing.",
                        fileType));
                return parseClaimDialectToYaml(dialectConfiguration, fileName);
        }
    }

    private String getFormattedFileName(String fileName) {

        String formattedFileName = fileName.replaceAll("[^\\w\\d]+", "_");
        formattedFileName = StringUtils.abbreviate(formattedFileName, 255);
        return formattedFileName;
    }

    private FileContent parseClaimDialectToXml(ClaimDialectConfiguration dialectConfiguration, String fileName)
            throws ClaimMetadataException {

        StringBuilder fileNameSB = new StringBuilder(fileName);
        fileNameSB.append(XML_FILE_EXTENSION);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(dialectConfiguration.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(dialectConfiguration, writer);
            String xmlContent = writer.toString();
            return new FileContent(fileNameSB.toString(), MEDIA_TYPE_XML, xmlContent);
        } catch (JAXBException e) {
            throw new ClaimMetadataException(String.format(
                      Constant.ErrorMessage.ERROR_CODE_ERROR_PARSING_CLAIM_DIALECT.toString(), MEDIA_TYPE_XML), e);
        }
    }

    private FileContent parseClaimDialectToJson(ClaimDialectConfiguration dialectConfiguration, String fileName)
            throws ClaimMetadataException {

        StringBuilder fileNameSB = new StringBuilder(fileName);
        fileNameSB.append(JSON_FILE_EXTENSION);
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        try {
            return new FileContent(fileNameSB.toString(), MEDIA_TYPE_JSON,
                    objectMapper.writeValueAsString(dialectConfiguration));
        } catch (JsonProcessingException e) {
            throw new ClaimMetadataException(String.format(
                      Constant.ErrorMessage.ERROR_CODE_ERROR_PARSING_CLAIM_DIALECT.toString(), MEDIA_TYPE_JSON), e);
        }
    }

    private FileContent parseClaimDialectToYaml(ClaimDialectConfiguration dialectConfiguration, String fileName)
            throws ClaimMetadataException {

        StringBuilder fileNameSB = new StringBuilder(fileName);
        fileNameSB.append(YAML_FILE_EXTENSION);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try {
            return new FileContent(fileNameSB.toString(), MEDIA_TYPE_YAML, yaml.dump(dialectConfiguration));
        } catch (YAMLException e) {
            throw new ClaimMetadataException(String.format(
                      Constant.ErrorMessage.ERROR_CODE_ERROR_PARSING_CLAIM_DIALECT.toString(), MEDIA_TYPE_YAML), e);
        }
    }

    /**
     * Add an external claim.
     *
     * @param dialectId           dialectId.
     * @param externalClaimReqDTO externalClaimReqDTO.
     * @return Resource identifier.
     */
    public String addExternalClaim(String dialectId, ExternalClaimReqDTO externalClaimReqDTO) {

        try {
            validateClaimModificationEligibility();
            if (!isDialectExists(dialectId)) {
                throw handleClaimManagementClientError(ERROR_CODE_INVALID_DIALECT_ID, NOT_FOUND, dialectId);
            }

            claimMetadataManagementService.addExternalClaim(createExternalClaim(dialectId, externalClaimReqDTO),
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_ADDING_EXTERNAL_CLAIM,
                    externalClaimReqDTO.getClaimURI());
        }

        return getResourceId(externalClaimReqDTO.getClaimURI());
    }

    /**
     * Delete an external claim.
     *
     * @param dialectId dialectId.
     * @param claimId   claimId.
     */
    public void deleteExternalClaim(String dialectId, String claimId) {

        String externalClaimURI;
        String externalClaimDialectURI;
        try {
            validateClaimModificationEligibility();
            externalClaimURI = base64DecodeId(claimId);
            externalClaimDialectURI = base64DecodeId(dialectId);
        } catch (Exception ignored) {
            // Ignoring the delete operation and return 204 response code, since the resource does not exist.
            return;
        }

        try {
            claimMetadataManagementService.removeExternalClaim(externalClaimDialectURI, externalClaimURI,
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_DELETING_EXTERNAL_CLAIM, claimId);
        }

    }

    /**
     * Retrieve an external claim.
     *
     * @param dialectId dialectId.
     * @param claimId   claimId.
     * @return Local claim.
     */
    public ExternalClaimResDTO getExternalClaim(String dialectId, String claimId) {

        try {
            List<ExternalClaim> externalClaimList = claimMetadataManagementService.getExternalClaims(
                    base64DecodeId(dialectId),
                    ContextLoader.getTenantDomainFromContext());

            if (CollectionUtils.isEmpty(externalClaimList)) {
                throw handleClaimManagementClientError(ERROR_CODE_CLAIMS_NOT_FOUND_FOR_DIALECT, NOT_FOUND, dialectId);
            }

            ExternalClaim externalClaim = extractExternalClaimFromClaimList(base64DecodeId(claimId), externalClaimList);

            if (externalClaim == null) {
                throw handleClaimManagementClientError(ERROR_CODE_EXTERNAL_CLAIM_NOT_FOUND, NOT_FOUND, claimId,
                        dialectId);
            }

            return getExternalClaimResDTO(externalClaim);

        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_EXTERNAL_CLAIM, claimId, dialectId);
        }
    }

    /**
     * Retrieve all claims belonging to an external dialect.
     *
     * @param dialectId dialectId.
     * @param limit     limit (optional).
     * @param offset    offset (optional).
     * @param filter    filter (optional).
     * @param sort      sort (optional).
     * @return List of external claims.
     */
    public List<ExternalClaimResDTO> getExternalClaims(String dialectId, Integer limit, Integer offset,
                                                       String filter, String sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);

        try {
            List<ClaimDialect> claimDialectList = claimMetadataManagementService.getClaimDialects(ContextLoader
                    .getTenantDomainFromContext());
            String decodedDialectId = base64DecodeId(dialectId);
            ClaimDialect claimDialect = extractDialectFromDialectList(decodedDialectId, claimDialectList);

            if (claimDialect == null) {
                throw handleClaimManagementClientError(ERROR_CODE_DIALECT_NOT_FOUND, NOT_FOUND, dialectId);
            }

            List<ExternalClaim> externalClaimList = claimMetadataManagementService.getExternalClaims(
                    base64DecodeId(dialectId),
                    ContextLoader.getTenantDomainFromContext());
            return getExternalClaimResDTOs(externalClaimList);

        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_RETRIEVING_EXTERNAL_CLAIMS, dialectId);
        }
    }

    /**
     * Update an external claim.
     *
     * @param dialectId           dialectId.
     * @param claimId             claimId.
     * @param externalClaimReqDTO externalClaimReqDTO.
     */
    public void updateExternalClaim(String dialectId, String claimId, ExternalClaimReqDTO externalClaimReqDTO) {

        try {
            validateClaimModificationEligibility();
            if (!StringUtils.equals(base64DecodeId(claimId), externalClaimReqDTO.getClaimURI())) {
                throw handleClaimManagementClientError(ERROR_CODE_EXTERNAL_CLAIM_CONFLICT, CONFLICT,
                        base64DecodeId(claimId), dialectId);
            }
            claimMetadataManagementService.updateExternalClaim(createExternalClaim(dialectId, externalClaimReqDTO),
                    ContextLoader.getTenantDomainFromContext());
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, ERROR_CODE_ERROR_UPDATING_EXTERNAL_CLAIM, claimId, dialectId);
        }
        getResourceId(externalClaimReqDTO.getClaimURI());
    }

    private ClaimDialect extractDialectFromDialectList(String dialectURI, List<ClaimDialect> dialectList) {

        for (ClaimDialect dialect : dialectList) {
            if (StringUtils.equals(dialect.getClaimDialectURI(), dialectURI)) {
                return dialect;
            }
        }

        return null;
    }

    private ExternalClaim extractExternalClaimFromClaimList(String claimURI, List<ExternalClaim> claimList) {

        for (ExternalClaim claim : claimList) {
            if (StringUtils.equals(claim.getClaimURI(), claimURI)) {
                return claim;
            }
        }

        return null;
    }

    private LocalClaim extractLocalClaimFromClaimList(String claimURI, List<LocalClaim> claimList) {

        for (LocalClaim claim : claimList) {
            if (StringUtils.equals(claim.getClaimURI(), claimURI)) {
                return claim;
            }
        }

        return null;
    }

    private ClaimDialect createClaimDialect(ClaimDialectReqDTO claimDialectReqDTO) {

        return new ClaimDialect(claimDialectReqDTO.getDialectURI());
    }

    private ClaimDialect createClaimDialect(String dialectURI) {

        return new ClaimDialect(dialectURI);
    }

    private ExternalClaim createExternalClaim(String dialectId, ExternalClaimReqDTO externalClaimReqDTO)
            throws ClaimMetadataClientException {

        return new ExternalClaim(
                base64DecodeId(dialectId),
                externalClaimReqDTO.getClaimURI(),
                externalClaimReqDTO.getMappedLocalClaimURI());
    }

    private ClaimDialectResDTO getClaimDialectResDTO(ClaimDialect claimDialect) {

        ClaimDialectResDTO claimDialectResDTO = new ClaimDialectResDTO();

        String dialectId;
        if (StringUtils.equals(claimDialect.getClaimDialectURI(), LOCAL_DIALECT)) {
            dialectId = LOCAL_DIALECT_PATH;
        } else {
            dialectId = base64EncodeId(claimDialect.getClaimDialectURI());
        }
        claimDialectResDTO.setId(dialectId);
        claimDialectResDTO.setDialectURI(claimDialect.getClaimDialectURI());

        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setRel(REL_CLAIMS);
        linkDTO.setHref(buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                CMT_PATH_COMPONENT + "/%s/claims", dialectId))
                .toString());

        claimDialectResDTO.setLink(linkDTO);

        return claimDialectResDTO;
    }

    private List<ClaimDialectResDTO> getClaimDialectResDTOs(List<ClaimDialect> claimDialectList) {

        List<ClaimDialectResDTO> claimDialectResDTOList = new ArrayList<>();

        for (ClaimDialect claimDialect : claimDialectList) {
            claimDialectResDTOList.add(getClaimDialectResDTO(claimDialect));
        }

        return claimDialectResDTOList;
    }

    private ExternalClaimResDTO getExternalClaimResDTO(ExternalClaim externalClaim) {

        ExternalClaimResDTO externalClaimResDTO = new ExternalClaimResDTO();

        externalClaimResDTO.setId(base64EncodeId(externalClaim.getClaimURI()));
        externalClaimResDTO.setClaimDialectURI(externalClaim.getClaimDialectURI());
        externalClaimResDTO.setClaimURI(externalClaim.getClaimURI());
        externalClaimResDTO.setMappedLocalClaimURI(externalClaim.getMappedLocalClaim());
        externalClaimResDTO.setProperties(mapToProperties(externalClaim.getClaimProperties()));

        return externalClaimResDTO;
    }

    private List<ExternalClaimResDTO> getExternalClaimResDTOs(List<ExternalClaim> externalClaimList) {

        List<ExternalClaimResDTO> externalClaimResDTOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(externalClaimList)) {
            for (ExternalClaim externalClaim : externalClaimList) {
                externalClaimResDTOList.add(getExternalClaimResDTO(externalClaim));
            }
        }

        return externalClaimResDTOList;
    }

    /**
     * Builds the LocalClaimResDTO and handles default values for mandatory properties.
     * If any new properties are added and default value handling logic is updated in this method,
     * {@link #populateDefaultProperties(LocalClaim)} should be updated accordingly as well.
     *
     */
    private LocalClaimResDTO getLocalClaimResDTO(LocalClaim localClaim) {

        LocalClaimResDTO localClaimResDTO = new LocalClaimResDTO();

        localClaimResDTO.setId(base64EncodeId(localClaim.getClaimURI()));
        localClaimResDTO.setClaimURI(localClaim.getClaimURI());
        localClaimResDTO.setDialectURI(localClaim.getClaimDialectURI());

        Map<String, String> claimProperties = new HashMap<>(localClaim.getClaimProperties());

        String description = claimProperties.remove(PROP_DESCRIPTION);
        localClaimResDTO.setDescription(description);
        if (description == null) {
            localClaimResDTO.setDescription(StringUtils.EMPTY);
        }

        String propDisplayOrder = claimProperties.remove(PROP_DISPLAY_ORDER);
        if (StringUtils.isNumeric(propDisplayOrder)) {
            localClaimResDTO.setDisplayOrder(Integer.valueOf(propDisplayOrder));
        } else {
            localClaimResDTO.setDisplayOrder(0);
        }

        localClaimResDTO.setDisplayName(claimProperties.remove(PROP_DISPLAY_NAME));
        localClaimResDTO.setReadOnly(Boolean.valueOf(claimProperties.remove(PROP_READ_ONLY)));
        localClaimResDTO.setSkipUserStore(Boolean.valueOf(claimProperties.remove(PROP_SKIP_USER_STORE)));
        String regEx = claimProperties.remove(PROP_REG_EX);
        localClaimResDTO.setRegEx(regEx);
        if (regEx == null) {
            localClaimResDTO.setRegEx(StringUtils.EMPTY);
        }
        localClaimResDTO.setRequired(Boolean.valueOf(claimProperties.remove(PROP_REQUIRED)));
        localClaimResDTO.setSupportedByDefault(Boolean.valueOf(claimProperties.remove(PROP_SUPPORTED_BY_DEFAULT)));

        String dataType = handleAdditionalProperties(claimProperties, PROP_DATA_TYPE);
        if (StringUtils.isNotBlank(dataType)) {
            try {
                localClaimResDTO.setDataType(DataType.valueOf(dataType.toUpperCase(Locale.ENGLISH)));
            } catch (IllegalArgumentException e) {
                localClaimResDTO.setDataType(DataType.STRING);
            }
        } else {
            localClaimResDTO.setDataType(DataType.STRING);
        }

        String subAttributes = handleAdditionalProperties(claimProperties, PROP_SUB_ATTRIBUTES);
        if (StringUtils.isNotBlank(subAttributes)) {
            localClaimResDTO.setSubAttributes(subAttributes.split(" "));
        }

        String inputFormat = handleAdditionalProperties(claimProperties, PROP_INPUT_FORMAT);
        if (StringUtils.isNotEmpty(inputFormat)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                InputFormatDTO inputFormatDTO = mapper.readValue(inputFormat, InputFormatDTO.class);
                localClaimResDTO.setInputFormat(inputFormatDTO);
            } catch (IOException e) {
                LOG.error("Error while parsing inputFormat.");
            }
        } else if (localClaimResDTO.getDataType() == DataType.BOOLEAN) {
                InputFormatDTO inputFormatDTO = new InputFormatDTO();
                inputFormatDTO.setInputType(INPUT_TYPE_CHECKBOX);
                localClaimResDTO.setInputFormat(inputFormatDTO);
        }

        String canonicalValues = handleAdditionalProperties(claimProperties, PROP_CANONICAL_VALUES);
        if (StringUtils.isNotEmpty(canonicalValues)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<LabelValueDTO> list = mapper.readValue(canonicalValues, mapper.getTypeFactory()
                                .constructCollectionType(List.class, LabelValueDTO.class));
                LabelValueDTO[] canonicalValuesList = new LabelValueDTO[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    LabelValueDTO canonicalValue = new LabelValueDTO();
                    canonicalValue.setLabel(list.get(i).getLabel());
                    canonicalValue.setValue(list.get(i).getValue());
                    canonicalValuesList[i] = canonicalValue;
                }
                if (canonicalValuesList.length > 0) {
                    localClaimResDTO.setCanonicalValues(canonicalValuesList);
                }
            } catch (IOException e) {
                LOG.error("Error while parsing canonical values.");
            }
        }

        String multiValued = handleAdditionalProperties(claimProperties, PROP_MULTI_VALUED);
        localClaimResDTO.setMultiValued(Boolean.valueOf(multiValued));

        String uniquenessScope = claimProperties.remove(PROP_UNIQUENESS_SCOPE);
        if (StringUtils.isNotBlank(uniquenessScope)) {
            try {
                localClaimResDTO.setUniquenessScope(LocalClaimResDTO.UniquenessScopeEnum.valueOf(uniquenessScope));
            } catch (IllegalArgumentException e) {
                localClaimResDTO.setUniquenessScope(LocalClaimResDTO.UniquenessScopeEnum.NONE);
            }
        }

        addAttributeProfilesToLocalClaimResponse(claimProperties, localClaimResDTO);

        String sharedProfileValueResolvingMethod =
                claimProperties.remove(ClaimConstants.SHARED_PROFILE_VALUE_RESOLVING_METHOD);
        if (StringUtils.isNotBlank(sharedProfileValueResolvingMethod)) {
            try {
                localClaimResDTO.setSharedProfileValueResolvingMethod(
                        LocalClaimResDTO.SharedProfileValueResolvingMethodEnum.valueOf(
                                sharedProfileValueResolvingMethod));
            } catch (IllegalArgumentException e) {
                // If the value is not a valid enum value, treat it as null.
                localClaimResDTO.setSharedProfileValueResolvingMethod(null);
            }
        }

        List<AttributeMappingDTO> attributeMappingDTOs = new ArrayList<>();
        for (AttributeMapping attributeMapping : localClaim.getMappedAttributes()) {
            AttributeMappingDTO attributeMappingDTO = new AttributeMappingDTO();

            attributeMappingDTO.setUserstore(attributeMapping.getUserStoreDomain());
            attributeMappingDTO.setMappedAttribute(attributeMapping.getAttributeName());

            attributeMappingDTOs.add(attributeMappingDTO);
        }
        localClaimResDTO.setAttributeMapping(attributeMappingDTOs);
        localClaimResDTO.setProperties(mapToProperties(claimProperties));

        return localClaimResDTO;
    }

    /**
     * Add attribute profiles to LocalClaimResDTO.
     *
     * @param claimProperties  Claim properties.
     * @param localClaimResDTO Local claim response DTO.
     */
    private void addAttributeProfilesToLocalClaimResponse(Map<String, String> claimProperties,
                                                          LocalClaimResDTO localClaimResDTO) {

        if (MapUtils.isEmpty(claimProperties)) {
            return;
        }
        ProfilesDTO attributeProfiles = new ProfilesDTO();
        Iterator<Map.Entry<String, String>> claimPropertyIterator = claimProperties.entrySet().iterator();

        while (claimPropertyIterator.hasNext()) {
            Map.Entry<String, String> property = claimPropertyIterator.next();
            String propertyKey = property.getKey();
            String propertyValue = property.getValue();

            if (StringUtils.isBlank(propertyKey) || StringUtils.isBlank(propertyValue)) {
                continue;
            }
            if (!StringUtils.startsWithIgnoreCase(propertyKey, PROP_PROFILES_PREFIX)) {
                continue;
            }
            String[] propertyKeyArray = propertyKey.split("\\.");
            if (propertyKeyArray.length != 3) {
                continue;
            }
            String profileName = propertyKeyArray[1];
            String claimPropertyName = propertyKeyArray[2];

            AttributeProfileDTO profileAttributes =
                    attributeProfiles.computeIfAbsent(profileName, k -> new AttributeProfileDTO());

            switch (claimPropertyName) {
                case PROP_READ_ONLY:
                    claimPropertyIterator.remove();
                    profileAttributes.setReadOnly(Boolean.valueOf(propertyValue));
                    break;
                case PROP_REQUIRED:
                    claimPropertyIterator.remove();
                    profileAttributes.setRequired(Boolean.valueOf(propertyValue));
                    break;
                case PROP_SUPPORTED_BY_DEFAULT:
                    claimPropertyIterator.remove();
                    profileAttributes.setSupportedByDefault(Boolean.valueOf(propertyValue));
                    break;
                default:
                    break;
            }
        }
        localClaimResDTO.setProfiles(attributeProfiles);
    }

    private List<LocalClaimResDTO> getLocalClaimResDTOs(List<LocalClaim> localClaimList) {

        List<LocalClaimResDTO> localClaimResDTOList = new ArrayList<>();

        for (LocalClaim localClaim : localClaimList) {
            localClaimResDTOList.add(getLocalClaimResDTO(localClaim));
        }

        return localClaimResDTOList;
    }

    private LocalClaim createLocalClaim(LocalClaimReqDTO localClaimReqDTO) {

        Map<String, String> claimProperties = new HashMap<>();

        if (StringUtils.isNotBlank(localClaimReqDTO.getDisplayName())) {
            claimProperties.put(PROP_DISPLAY_NAME, localClaimReqDTO.getDisplayName());
        }

        if (StringUtils.isNotBlank(localClaimReqDTO.getDescription())) {
            claimProperties.put(PROP_DESCRIPTION, localClaimReqDTO.getDescription());
        }

        if (StringUtils.isNotBlank(localClaimReqDTO.getRegEx())) {
            claimProperties.put(PROP_REG_EX, localClaimReqDTO.getRegEx());
        }

        if (localClaimReqDTO.getDisplayOrder() != null) {
            claimProperties.put(PROP_DISPLAY_ORDER, String.valueOf(localClaimReqDTO.getDisplayOrder()));
        } else {
            claimProperties.put(PROP_DISPLAY_ORDER, "0");
        }

        if (localClaimReqDTO.getUniquenessScope() != null) {
            claimProperties.put(PROP_UNIQUENESS_SCOPE, localClaimReqDTO.getUniquenessScope().toString());
        }

        if (localClaimReqDTO.getSharedProfileValueResolvingMethod() != null) {
            claimProperties.put(ClaimConstants.SHARED_PROFILE_VALUE_RESOLVING_METHOD,
                    String.valueOf(localClaimReqDTO.getSharedProfileValueResolvingMethod()));
        }

        addAttributeProfilesToClaimProperties(localClaimReqDTO.getProfiles(), claimProperties);

        claimProperties.put(PROP_READ_ONLY, String.valueOf(localClaimReqDTO.getReadOnly()));
        claimProperties.put(PROP_SKIP_USER_STORE, String.valueOf(localClaimReqDTO.getSkipUserStore()));
        claimProperties.put(PROP_REQUIRED, String.valueOf(localClaimReqDTO.getRequired()));
        claimProperties.put(PROP_SUPPORTED_BY_DEFAULT, String.valueOf(localClaimReqDTO.getSupportedByDefault()));
        if (localClaimReqDTO.getDataType() != null) {
            claimProperties.put(PROP_DATA_TYPE, String.valueOf(localClaimReqDTO.getDataType())
                    .toLowerCase(Locale.ROOT));
        }

        if (DataType.COMPLEX.equals(localClaimReqDTO.getDataType())
                && ArrayUtils.isNotEmpty(localClaimReqDTO.getSubAttributes())) {
            claimProperties.put(PROP_SUB_ATTRIBUTES, String.join(" ", localClaimReqDTO.getSubAttributes()));
        }

        if (ArrayUtils.isNotEmpty(localClaimReqDTO.getCanonicalValues())) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(localClaimReqDTO.getCanonicalValues());
                claimProperties.put(PROP_CANONICAL_VALUES, jsonString);
            } catch (JsonProcessingException e) {
                LOG.error("Error while parsing canonical values.", e);
            }
        }

        if (localClaimReqDTO.getInputFormat() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(localClaimReqDTO.getInputFormat());
                claimProperties.put(PROP_INPUT_FORMAT, jsonString);
            } catch (JsonProcessingException e) {
                LOG.error("Error while parsing canonical values.", e);
            }
        }

        if (localClaimReqDTO.getMultiValued() != null) {
            claimProperties.put(PROP_MULTI_VALUED, String.valueOf(localClaimReqDTO.getMultiValued()));
        }

        claimProperties.putAll(propertiesToMap(localClaimReqDTO.getProperties()));

        List<AttributeMapping> attributeMappings = new ArrayList<>();
        for (AttributeMappingDTO attributeMappingDTO : localClaimReqDTO.getAttributeMapping()) {
            attributeMappings.add(new AttributeMapping(attributeMappingDTO.getUserstore(),
                    attributeMappingDTO.getMappedAttribute()));
        }

        return new LocalClaim(localClaimReqDTO.getClaimURI(), attributeMappings, claimProperties);
    }

    /**
     * Add profile attributes to claim properties.
     *
     * @param attributeProfiles - Profile attributes.
     * @param claimProperties   - Claim properties.
     */
    private void addAttributeProfilesToClaimProperties(Map<String, AttributeProfileDTO> attributeProfiles,
                                                       Map<String, String> claimProperties) {

        if (MapUtils.isEmpty(attributeProfiles)) {
            return;
        }
        attributeProfiles.forEach((profileName, profileAttributes) -> {
            addProfileAttributeValue(claimProperties, profileName, PROP_READ_ONLY, profileAttributes.getReadOnly());
            addProfileAttributeValue(claimProperties, profileName, PROP_REQUIRED, profileAttributes.getRequired());
            addProfileAttributeValue(claimProperties, profileName, PROP_SUPPORTED_BY_DEFAULT,
                    profileAttributes.getSupportedByDefault());
        });
    }

    private void addProfileAttributeValue(Map<String, String> claimProperties, String profileName, String propertyKey,
                                          Object propertyValue) {

        if (propertyValue == null) {
            return;
        }
        String claimPropertyKey = PROP_PROFILES_PREFIX + profileName + "." + propertyKey;
        claimProperties.put(claimPropertyKey, String.valueOf(propertyValue));
    }

    /**
     * Import a claim dialect with related claims from an uploaded file.
     *
     * @param fileInputStream   InputStream representing the uploaded claim dialect file.
     * @param fileDetail        Attachment object with metadata about the uploaded claim dialect file.
     * @return a String representing the ID of the imported claim dialect.
     */
    public String importClaimDialectFromFile(InputStream fileInputStream, Attachment fileDetail) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Importing claim dialect from file: %s", fileDetail.getDataHandler().getName()));
        }

        String dialectId;
        try {
            ClaimDialectConfiguration dialectConfiguration = getDialectFromFile(fileInputStream, fileDetail);
            dialectId = dialectConfiguration.getId();

            if (LOCAL_DIALECT_PATH.equals(dialectId)) {
                throw handleClaimManagementClientError(
                        Constant.ErrorMessage.ERROR_CODE_ERROR_IMPORTING_LOCAL_CLAIM_DIALECT, FORBIDDEN);
            }

            List<ExternalClaimReqDTO> externalClaimReqDTOList = dialectConfiguration.getExternalClaimReqDTOList();
            String dialectURI = addClaimDialect(dialectConfiguration.getClaimDialectReqDTO());

            importExternalClaims(dialectURI, externalClaimReqDTOList);

            return dialectId;
        } catch (ClaimMetadataException e) {
            throw handleClaimManagementException(e, Constant.ErrorMessage.ERROR_CODE_ERROR_IMPORTING_CLAIM_DIALECT);
        }
    }

    private void importExternalClaims(String dialectID, List<ExternalClaimReqDTO> externalClaimReqDTOList) {

        List<ClaimErrorDTO> errors = new ArrayList<>();

        for (ExternalClaimReqDTO externalClaimReqDTO : externalClaimReqDTOList) {
            try {
                addExternalClaim(dialectID, externalClaimReqDTO);
            } catch (APIError e) {
                ClaimErrorDTO claimErrorDTO = new ClaimErrorDTO(e.getResponseEntity());
                claimErrorDTO.setClaimURI(externalClaimReqDTO.getClaimURI());
                errors.add(claimErrorDTO);
            }
        }
        if (!errors.isEmpty()) {
            throw handleClaimManagementBulkClientError(Constant.ErrorMessage.ERROR_CODE_IMPORTING_EXTERNAL_CLAIMS,
                    BAD_REQUEST, errors, String.valueOf(errors.size()), String.valueOf(externalClaimReqDTOList.size()));
        }
    }

    private ClaimDialectConfiguration getDialectFromFile(InputStream fileInputStream, Attachment fileDetail)
            throws ClaimMetadataException {

        try {
            FileContent claimDialectFileContent = new FileContent(fileDetail.getDataHandler().getName(),
                    fileDetail.getDataHandler().getContentType(),
                    IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name()));
            return generateModelFromFile(claimDialectFileContent);
        } catch (IOException | ClaimMetadataClientException e) {
            throw new ClaimMetadataException(Constant.ErrorMessage.ERROR_CODE_INVALID_INPUT_FILE.toString(), e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    private ClaimDialectConfiguration generateModelFromFile(FileContent fileContent) throws ClaimMetadataException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Parsing Claim Dialect from file: %s of type: %s.", fileContent.getFileName(),
                    fileContent.getFileType()));
        }
        if (StringUtils.isEmpty(fileContent.getContent())) {
            throw handleClaimManagementClientError(Constant.ErrorMessage.ERROR_CODE_MISSING_FILE_CONTENT, BAD_REQUEST,
                    fileContent.getFileName());
        }

        switch (Util.getMediaType(fileContent.getFileType())) {
            case MEDIA_TYPE_XML:
                return parseClaimDialectFromXml(fileContent);
            case MEDIA_TYPE_JSON:
                return parseClaimDialectFromJson(fileContent);
            case MEDIA_TYPE_YAML:
                return parseClaimDialectFromYaml(fileContent);
            default:
                LOG.warn(String.format("Unsupported media type %s for file %s. Defaulting to YAML parsing.",
                        fileContent.getFileType(), fileContent.getFileName()));
                return parseClaimDialectFromYaml(fileContent);
        }
    }

    private ClaimDialectConfiguration parseClaimDialectFromXml(FileContent fileContent) throws ClaimMetadataException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ClaimDialectConfiguration.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (ClaimDialectConfiguration) unmarshaller.unmarshal(new StringReader(fileContent.getContent()));
        } catch (JAXBException e) {
            throw new ClaimMetadataException(String.format(
                      Constant.ErrorMessage.ERROR_CODE_ERROR_READING_FILE_CONTENT.toString(), MEDIA_TYPE_XML), e);
        }
    }

    private ClaimDialectConfiguration parseClaimDialectFromJson(FileContent fileContent) throws ClaimMetadataException {

        try {
            return new ObjectMapper().readValue(fileContent.getContent(), ClaimDialectConfiguration.class);
        } catch (JsonProcessingException e) {
            throw new ClaimMetadataException(String.format(
                      Constant.ErrorMessage.ERROR_CODE_ERROR_READING_FILE_CONTENT.toString(), MEDIA_TYPE_JSON), e);
        }
    }

    private ClaimDialectConfiguration parseClaimDialectFromYaml(FileContent fileContent) throws ClaimMetadataException {

        try {
            // Add trusted tags included in the Claims YAML files.
            List<String> trustedTagList = new ArrayList<>();
            trustedTagList.add(ClaimDialectConfiguration.class.getName());
            trustedTagList.add(ExternalClaimResDTO.class.getName());
            trustedTagList.add(LocalClaimResDTO.class.getName());

            LoaderOptions loaderOptions = new LoaderOptions();
            TagInspector tagInspector = new TrustedPrefixesTagInspector(trustedTagList);
            loaderOptions.setTagInspector(tagInspector);
            Yaml yaml = new Yaml(new Constructor(ClaimDialectConfiguration.class, loaderOptions));
            return yaml.loadAs(fileContent.getContent(), ClaimDialectConfiguration.class);
        } catch (YAMLException e) {
            throw new ClaimMetadataException(String.format(
                      Constant.ErrorMessage.ERROR_CODE_ERROR_READING_FILE_CONTENT.toString(), MEDIA_TYPE_YAML), e);
        }
    }

    private String base64EncodeId(String id) {

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(id.getBytes(StandardCharsets.UTF_8));
    }

    private String base64DecodeId(String id) throws ClaimMetadataClientException {

        try {
            return new String(
                    Base64.getUrlDecoder().decode(id),
                    StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new ClaimMetadataClientException(Constant.ErrorMessage.ERROR_CODE_INVALID_IDENTIFIER.getCode(),
                    String.format(Constant.ErrorMessage.ERROR_CODE_INVALID_IDENTIFIER.getDescription(), id));
        }
    }

    private String getResourceId(String val) {

        // TODO: Change implementation.
        return base64EncodeId(val);
    }

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

    private boolean isDialectExists(String dialectId) throws ClaimMetadataException {

        List<ClaimDialect> claimDialectList =
                claimMetadataManagementService.getClaimDialects(ContextLoader.getTenantDomainFromContext());
        ClaimDialect claimDialect = extractDialectFromDialectList(base64DecodeId(dialectId), claimDialectList);

        return claimDialect != null;
    }

    private boolean isLocalClaimExist(String claimId) throws ClaimMetadataException {

        List<LocalClaim> localClaimList = claimMetadataManagementService.getLocalClaims(ContextLoader
                .getTenantDomainFromContext());
        LocalClaim localClaim = extractLocalClaimFromClaimList(base64DecodeId(claimId), localClaimList);

        return localClaim != null;
    }

    private boolean isExternalClaimExist(String dialectId, String claimId) throws ClaimMetadataException {

        List<ExternalClaim> externalClaimList = claimMetadataManagementService.getExternalClaims(
                base64DecodeId(dialectId),
                ContextLoader.getTenantDomainFromContext());
        ExternalClaim externalClaim = extractExternalClaimFromClaimList(base64DecodeId(claimId), externalClaimList);

        return externalClaim != null;
    }

    private APIError handleClaimManagementException(ClaimMetadataException e, Constant.ErrorMessage errorEnum,
                                                    String... data) {

        if (e instanceof ClaimMetadataClientException) {
            Response.Status status = BAD_REQUEST;
            // Check for conflicting scenarios.
            if (isConflictScenario(e.getErrorCode())) {
                status = CONFLICT;
            }
            if (isForbiddenScenario(e.getErrorCode())) {
                status = FORBIDDEN;
            }
            if (StringUtils.isNotBlank(e.getErrorCode()) &&
                    e.getErrorCode().contains(Constant.CLAIM_MANAGEMENT_PREFIX)) {
                return handleClaimManagementClientError(e.getErrorCode(), e.getMessage(), status, data);
            }
            return handleClaimManagementClientError(Constant.ErrorMessage.getMappedErrorMessage(e.getErrorCode()),
                    status, data);
        }

        return handleException(e, errorEnum, data);
    }

    /**
     * Check for the conflicting (HTTP 409) request scenarios.
     *
     * @param errorCode Error code returned by the service.
     * @return TRUE if the error is a conflict (409) scenario.
     */
    private boolean isConflictScenario(String errorCode) {

        return !StringUtils.isBlank(errorCode) && conflictErrorScenarios.contains(errorCode);
    }

    private boolean isForbiddenScenario(String errorCode) {

        return !StringUtils.isBlank(errorCode) && forbiddenErrorScenarios.contains(errorCode);
    }

    private APIError handleClaimManagementClientError(Constant.ErrorMessage errorEnum, Response.Status status) {

        return handleClaimManagementClientError(errorEnum, status, StringUtils.EMPTY);
    }

    private APIError handleClaimManagementClientError(Constant.ErrorMessage errorEnum, Response.Status status,
                                                      String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data)
                .build(LOG, buildErrorDescription(errorEnum, data));

        return new APIError(status, errorResponse);
    }

    private BulkAPIError handleClaimManagementBulkClientError(Constant.ErrorMessage errorEnum, Response.Status status,
                                                              List<ClaimErrorDTO> apiErrors, String... data) {

        BulkErrorResponse bulkErrorResponse = getBulkErrorBuilder(errorEnum, apiErrors, data)
                .build(LOG, buildErrorDescription(errorEnum, data));

        return new BulkAPIError(status, bulkErrorResponse);
    }

    private APIError handleClaimManagementClientError(String errorCode, String errorMessage,
                                                      Response.Status status, String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorCode, errorMessage, data).
                build(LOG, buildErrorDescription(errorMessage, data));
        return new APIError(status, errorResponse);
    }

    private void handleNotImplementedCapabilities(Integer limit, Integer offset, String filter, String sort) {

        handleNotImplementedCapabilities(null, limit, offset, filter, sort);
    }

    private APIError handleException(Exception e, Constant.ErrorMessage errorEnum, String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data)
                .build(LOG, e, buildErrorDescription(errorEnum, data));

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        return new APIError(status, errorResponse);
    }

    private void handleNotImplementedCapabilities(String attributes, Integer limit, Integer offset, String filter,
                                                  String sort) {

        Constant.ErrorMessage errorEnum = null;

        if (limit != null) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (offset != null) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (filter != null) {
            errorEnum = ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
        } else if (sort != null) {
            errorEnum = ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        } else if (attributes != null) {
            errorEnum = ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;

            throw new APIError(status, errorResponse);
        }
    }

    private ErrorResponse.Builder getErrorBuilder(Constant.ErrorMessage errorEnum, String... data) {

        return new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withMessage(errorEnum.getMessage())
                .withDescription(buildErrorDescription(errorEnum, data));
    }

    private BulkErrorResponse.Builder getBulkErrorBuilder(Constant.ErrorMessage errorEnum, List<ClaimErrorDTO> errors,
                                                          String... data) {

        return new BulkErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withMessage(errorEnum.getMessage())
                .withDescription(buildErrorDescription(errorEnum, data))
                .withFailedOperations(errors);
    }

    private ErrorResponse.Builder getErrorBuilder(String errorCode, String errorMessage, String... data) {

        return new ErrorResponse.Builder().withCode(errorCode).withMessage(errorMessage)
                .withDescription(buildErrorDescription(errorMessage, data));
    }

    private String buildErrorDescription(Constant.ErrorMessage errorEnum, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            errorDescription = String.format(errorEnum.getDescription(), data);
        } else {
            errorDescription = errorEnum.getDescription();
        }

        return errorDescription;
    }

    private String buildErrorDescription(String errorMessage, String... data) {

        String errorDescription;
        if (ArrayUtils.isNotEmpty(data)) {
            errorDescription = String.format(errorMessage, data);
        } else {
            errorDescription = errorMessage;
        }
        return errorDescription;
    }

    private HashMap<String, String> propertiesToMap(List<PropertyDTO> propList) {

        HashMap<String, String> propMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(propList)) {
            for (PropertyDTO prop : propList) {
                propMap.put(prop.getKey(), prop.getValue());
            }
        }

        return propMap;
    }

    private List<PropertyDTO> mapToProperties(Map<String, String> propMap) {

        List<PropertyDTO> propList = new ArrayList<>();

        if (!propMap.isEmpty()) {
            propMap.forEach((k, v) -> {
                PropertyDTO propertyDTO = new PropertyDTO();

                propertyDTO.setKey(k);
                propertyDTO.setValue(v);

                propList.add(propertyDTO);
            });
        }
        return propList;
    }

    private void validateAttributeMappings(List<AttributeMappingDTO> attributeMappingDTOList)
            throws UserStoreException {

        if (attributeMappingDTOList == null) {
            throw handleClaimManagementClientError(ERROR_CODE_EMPTY_ATTRIBUTE_MAPPINGS, BAD_REQUEST);
        }
        String primaryUserstoreDomainName = IdentityUtil.getPrimaryDomainName();
        for (AttributeMappingDTO attributeMappingDTO : attributeMappingDTOList) {
            if (StringUtils.isBlank(attributeMappingDTO.getUserstore())) {
                throw handleClaimManagementClientError(ERROR_CODE_USERSTORE_NOT_SPECIFIED_IN_MAPPINGS,
                        BAD_REQUEST, attributeMappingDTO.getUserstore());
            }
            // Validating mapped attribute only the userstore is equal to the primary userstore domain name.
            if (StringUtils.isBlank(attributeMappingDTO.getMappedAttribute()) &&
                    primaryUserstoreDomainName.equalsIgnoreCase(attributeMappingDTO.getUserstore())) {
                throw handleClaimManagementClientError(ERROR_CODE_EMPTY_MAPPED_ATTRIBUTES_IN_LOCAL_CLAIM,
                        BAD_REQUEST, attributeMappingDTO.getUserstore());
            }
            if (!isUserStoreExists(attributeMappingDTO.getUserstore())) {
                throw handleClaimManagementClientError(ERROR_CODE_INVALID_USERSTORE, BAD_REQUEST,
                        attributeMappingDTO.getUserstore());
            }
        }
    }

    private void validateClaimModificationEligibility() throws ClaimMetadataClientException {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            String organizationId = organizationManager.resolveOrganizationId(tenantDomain);
            boolean isPrimaryOrg = organizationManager.isPrimaryOrganization(organizationId);
            if (!MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain) && !isPrimaryOrg) {
                throw handleClaimManagementClientError(ERROR_CODE_UNAUTHORIZED_ORG_FOR_CLAIM_MANAGEMENT, FORBIDDEN,
                        organizationId);
            }
        } catch (OrganizationManagementException e) {
            // This is to handle the scenario where the tenant is not modeled as an organization.
            if (ERROR_CODE_ORGANIZATION_NOT_FOUND_FOR_TENANT.getCode().equals(e.getErrorCode())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Organization not found for the tenant: " + tenantDomain);
                }
                return;
            }
            throw new ClaimMetadataClientException(Constant.ErrorMessage.ERROR_CODE_ERROR_RESOLVING_ORGANIZATION.
                    getCode(), Constant.ErrorMessage.ERROR_CODE_ERROR_RESOLVING_ORGANIZATION.getDescription());
        }

    }

    private void validateSystemClaimUpdate(LocalClaimReqDTO localClaimReqDTO, String claimId)
            throws ClaimMetadataClientException {

        boolean isSystemClaim = localClaimReqDTO.getProperties().stream()
                .anyMatch(property -> IS_SYSTEM_CLAIM.equals(property.getKey()) &&
                        Boolean.parseBoolean(property.getValue()));
        if (!isSystemClaim) {
            return;
        }

        LocalClaimResDTO existingClaim = getLocalClaim(claimId);

        // Validate the multivalued property is updated.
        if (Boolean.TRUE.equals(existingClaim.getMultiValued()) !=
                Boolean.TRUE.equals(localClaimReqDTO.getMultiValued())) {
            throw new ClaimMetadataClientException(Constant.ErrorMessage
                    .ERROR_CODE_SYSTEM_ATTRIBUTE_MULTIVALUED_STATE_UPDATE.getCode(),
                    Constant.ErrorMessage.ERROR_CODE_SYSTEM_ATTRIBUTE_MULTIVALUED_STATE_UPDATE.getDescription());
        }


        // Validate the dataType property is updated.
        DataType requestedDataType = localClaimReqDTO.getDataType();
        if (requestedDataType == null) {
            requestedDataType = DataType.STRING;
        }
        if (existingClaim.getDataType() != requestedDataType) {
            throw new ClaimMetadataClientException(Constant.ErrorMessage.ERROR_CODE_SYSTEM_ATTRIBUTE_DATA_TYPE_UPDATE
                    .getCode(), Constant.ErrorMessage.ERROR_CODE_SYSTEM_ATTRIBUTE_DATA_TYPE_UPDATE.getDescription());
        }

    }

    private void validateSubAttributeUpdate(LocalClaimReqDTO localClaimReqDTO) throws ClaimMetadataException {

        if (!DataType.COMPLEX.equals(localClaimReqDTO.getDataType())) {
            return;
        }
        if (ArrayUtils.isEmpty(localClaimReqDTO.getSubAttributes())) {
            throw new ClaimMetadataClientException(Constant.ErrorMessage.ERROR_CODE_SUB_ATTRIBUTES_NOT_SPECIFIED.
                    getCode(), Constant.ErrorMessage.ERROR_CODE_SUB_ATTRIBUTES_NOT_SPECIFIED.getDescription());
        }
        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        validateAttributeIsSubAttributeOfAnotherAttribute(localClaimReqDTO.getClaimURI(), tenantDomain);
        validateSubAttributeSCIMMappingPattern(localClaimReqDTO.getClaimURI(), tenantDomain,
                localClaimReqDTO.getSubAttributes());
    }

    private void validateDataTypeUpdates(LocalClaimReqDTO localClaimReqDTO) throws ClaimMetadataClientException {

        if (localClaimReqDTO.getDataType() == null) {
            return;
        }
        if (DataType.BOOLEAN.equals(localClaimReqDTO.getDataType())
                && Boolean.TRUE.equals(localClaimReqDTO.getMultiValued())) {
            String errorDescription = String.format(Constant.ErrorMessage
                    .ERROR_CODE_BOOLEAN_ATTRIBUTE_CANNOT_BE_MULTI_VALUED.getDescription(),
                    localClaimReqDTO.getClaimURI());
            throw new ClaimMetadataClientException(Constant.ErrorMessage
                    .ERROR_CODE_BOOLEAN_ATTRIBUTE_CANNOT_BE_MULTI_VALUED.getCode(), errorDescription);
        }
        if (ArrayUtils.isNotEmpty(localClaimReqDTO.getCanonicalValues()) &&
                !DataType.STRING.equals(localClaimReqDTO.getDataType())) {
            String errorDescription = String.format(Constant.ErrorMessage
                    .ERROR_CODE_CANONICAL_VALUES_NOT_SUPPORTED_FOR_NON_STRING_DATA_TYPES.getDescription(),
                    localClaimReqDTO.getClaimURI(), localClaimReqDTO.getDataType());
            throw new ClaimMetadataClientException(Constant.ErrorMessage
                    .ERROR_CODE_CANONICAL_VALUES_NOT_SUPPORTED_FOR_NON_STRING_DATA_TYPES.getCode(), errorDescription);
        }
    }

    private void validateAttributeInputFormat(LocalClaimReqDTO localClaimReqDTO) throws ClaimMetadataClientException {

        if (localClaimReqDTO.getInputFormat() == null || localClaimReqDTO.getInputFormat().getInputType() == null) {
            return; // No input format specified, no validation needed.
        }

        String inputType = localClaimReqDTO.getInputFormat().getInputType();
        DataType dataType = localClaimReqDTO.getDataType();
        if (!Constant.ALLOWED_INPUT_TYPES.contains(inputType)) {
            // The provided input type is not system defined type. Hence, validations are skipped.
            return;
        }

        switch (inputType) {
            case INPUT_TYPE_TEXT_INPUT:
                break;
            case INPUT_TYPE_DROPDOWN:
            case INPUT_TYPE_RADIO_GROUP:
            case INPUT_TYPE_MULTI_SELECT_DROPDOWN:
            case INPUT_TYPE_CHECKBOX_GROUP: {
                if (!DataType.STRING.equals(dataType)) {
                    handleInputFormatClientException("Input format: " + inputType + " not aligned with string " +
                            "data type.");
                }
                if (ArrayUtils.isEmpty(localClaimReqDTO.getCanonicalValues())) {
                    handleInputFormatClientException("Input format: " + inputType + " requires multiple options " +
                            "to be defined.");
                }
                boolean isMultiValued = Boolean.TRUE.equals(localClaimReqDTO.getMultiValued());
                if (isMultiValued && (INPUT_TYPE_DROPDOWN.equals(inputType)
                        || INPUT_TYPE_RADIO_GROUP.equals(inputType))) {
                    handleInputFormatClientException("Input format: " + inputType + " requires multi valued " +
                            "property to be false.");
                } else if (!isMultiValued && (INPUT_TYPE_MULTI_SELECT_DROPDOWN.equals(inputType)
                        || INPUT_TYPE_CHECKBOX_GROUP.equals(inputType))) {
                    handleInputFormatClientException("Input format: " + inputType + " requires multi valued " +
                            "property to be enabled.");
                }
                break;
            }
            case INPUT_TYPE_DATE_PICKER:
                if (!DataType.DATE_TIME.equals(dataType)) {
                    handleInputFormatClientException("Input format: date_picker can be used with date data type.");
                }
                break;
            case INPUT_TYPE_NUMBER_INPUT:
                if (!DataType.INTEGER.equals(dataType)) {
                    handleInputFormatClientException("Input format: number_input can be used with integer data type.");
                }
                break;
            case INPUT_TYPE_CHECKBOX:
            case INPUT_TYPE_TOGGLE:
                if (!DataType.BOOLEAN.equals(dataType)) {
                    handleInputFormatClientException("Input format: " + inputType + " should be boolean data type.");
                }
                break;
            default:
                break;
        }
    }

    private void handleInputFormatClientException(String errorDescription) throws ClaimMetadataClientException {

        throw new ClaimMetadataClientException(Constant.ErrorMessage.ERROR_CODE_UNSUPPORTED_INPUT_TYPE.getCode(),
                errorDescription);
    }

    private String getMappedSCIMClaim(String claimURI, String tenantDomain) throws ClaimMetadataException {

        String customSchemaURI = SCIMCommonUtils.getCustomSchemaURI();
        List<Claim> mappedClaims = claimMetadataManagementService.getMappedExternalClaimsForLocalClaim(claimURI,
                tenantDomain);
        Optional<Claim> customSCIMMappedClaim = mappedClaims.stream()
                .filter(mappedClaim -> customSchemaURI.equals(mappedClaim.getClaimDialectURI())).findFirst();
        if (customSCIMMappedClaim.isPresent()) {
            return customSCIMMappedClaim.get().getClaimURI();
        }
        return StringUtils.EMPTY;
    }

    private void validateSubAttributeSCIMMappingPattern(String claimURI, String tenantDomain, String[] subAttributes)
            throws ClaimMetadataException {

        String customSchemaURI = SCIMCommonUtils.getCustomSchemaURI();
        String attributeSCIMMapping = getMappedSCIMClaim(claimURI, tenantDomain)
                .replace(customSchemaURI, StringUtils.EMPTY);
        for (String subAttribute : subAttributes) {
            String subAttributeSCIMMapping = getMappedSCIMClaim(subAttribute, tenantDomain)
                    .replace(customSchemaURI, StringUtils.EMPTY);
            if (!subAttributeSCIMMapping.startsWith(attributeSCIMMapping + ".")) {
                String subAttributeName = subAttribute.replace(LOCAL_DIALECT + "/", StringUtils.EMPTY);
                String errorDescription = String.format(Constant.ErrorMessage
                        .ERROR_CODE_SUB_ATTRIBUTES_NOT_SCIM_COMPLIANT.getDescription(), subAttributeName);
                throw new ClaimMetadataClientException(Constant.ErrorMessage
                        .ERROR_CODE_SUB_ATTRIBUTES_NOT_SCIM_COMPLIANT.getCode(), errorDescription);
            }
        }
    }

    private void validateAttributeIsSubAttributeOfAnotherAttribute(String claimURI, String tenantDomain)
            throws ClaimMetadataException {

        for (LocalClaim localClaim : claimMetadataManagementService.getLocalClaims(tenantDomain)) {
            String subAttributes = localClaim.getClaimProperty(PROP_SUB_ATTRIBUTES);
            if (StringUtils.isEmpty(subAttributes)) {
                continue;
            }
            if (Arrays.stream(subAttributes.split(" "))
                    .anyMatch(subAttribute -> StringUtils.equals(subAttribute, claimURI))) {
                String errorDescription = String.format(Constant.ErrorMessage
                                .ERROR_CODE_ATTRIBUTES_MARKED_AS_SUB_ATTRIBUTES_NOT_ALLOWED_TO_HAVE_SUB_ATTRIBUTES
                                .getDescription(), localClaim.getClaimURI());
                throw new ClaimMetadataClientException(Constant.ErrorMessage
                        .ERROR_CODE_ATTRIBUTES_MARKED_AS_SUB_ATTRIBUTES_NOT_ALLOWED_TO_HAVE_SUB_ATTRIBUTES.getCode(),
                        errorDescription);
            }
        }
    }

    private boolean isSubOrganizationContext() throws ClaimMetadataClientException {

        try {
            return OrganizationManagementUtil.isOrganization(ContextLoader.getTenantDomainFromContext());
        } catch (OrganizationManagementException e) {
            throw new ClaimMetadataClientException(Constant.ErrorMessage.ERROR_CODE_ERROR_RESOLVING_ORGANIZATION.
                    getCode(), Constant.ErrorMessage.ERROR_CODE_ERROR_RESOLVING_ORGANIZATION.getDescription());
        }
    }

    private void validateLocalClaimUpdate(String claimID, LocalClaim incomingLocalClaim)
            throws ClaimMetadataException {

        Optional<LocalClaim>
                existingLocalClaim = getClaimMetadataManagementService().getLocalClaim(base64DecodeId(claimID),
                ContextLoader.getTenantDomainFromContext());

        if (!existingLocalClaim.isPresent()) {
            throw handleClaimManagementClientError(ERROR_CODE_LOCAL_CLAIM_NOT_FOUND, BAD_REQUEST, claimID);
        }

        validateLocalClaimProperties(existingLocalClaim.get(), incomingLocalClaim);

        for (AttributeMapping existingMapping : existingLocalClaim.get().getMappedAttributes()) {
            if (IdentityUtil.getPrimaryDomainName().equals(existingMapping.getUserStoreDomain())) {
                Optional<AttributeMapping> incomingAttributeMapping = incomingLocalClaim.getMappedAttributes().stream()
                        .filter(mapping -> IdentityUtil.getPrimaryDomainName().equals(mapping.getUserStoreDomain()))
                        .findFirst();
                // Not allowing to update the primary userstore attribute mapping for sub-orgs.
                if (incomingAttributeMapping.isPresent() && !StringUtils.equals(existingMapping.getAttributeName(),
                        incomingAttributeMapping.get().getAttributeName())) {
                    throw handleClaimManagementClientError(ERROR_CODE_UNAUTHORIZED_ORG_FOR_ATTRIBUTE_MAPPING_UPDATE,
                            FORBIDDEN, existingMapping.getUserStoreDomain());
                }
            }
        }
    }

    /**
     * Validates the claim properties.
     *
     * @param existingLocalClaim the existing local claim.
     * @param incomingLocalClaim the incoming local claim with updated properties.
     */
    private void validateLocalClaimProperties(LocalClaim existingLocalClaim, LocalClaim incomingLocalClaim) {

        // Populate default properties on the existing claim.
        populateDefaultProperties(existingLocalClaim);

        // Filter out allowed keys from both existing and incoming claim properties.
        Map<String, String> filteredExistingProperties = existingLocalClaim.getClaimProperties().entrySet().stream()
                .filter(entry -> !Constant.ALLOWED_PROPERTY_KEYS_FOR_SUB_ORG_UPDATE.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> filteredIncomingProperties = incomingLocalClaim.getClaimProperties().entrySet().stream()
                .filter(entry -> !Constant.ALLOWED_PROPERTY_KEYS_FOR_SUB_ORG_UPDATE.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!filteredExistingProperties.equals(filteredIncomingProperties)) {
            throw handleClaimManagementClientError(ERROR_CODE_UNAUTHORIZED_ORG_FOR_CLAIM_PROPERTY_UPDATE, FORBIDDEN);
        }

        // Validate ExcludedUserStores property.
        String existingValue = existingLocalClaim.getClaimProperties().get(Constant.PROP_EXCLUDED_USER_STORES);
        String incomingValue = incomingLocalClaim.getClaimProperties().get(Constant.PROP_EXCLUDED_USER_STORES);
        String primaryUserStoreDomain = IdentityUtil.getPrimaryDomainName();

        List<String> existingStores = Arrays.asList(
                ArrayUtils.nullToEmpty(StringUtils.split(existingValue, ",")));
        List<String> incomingStores = Arrays.asList(
                ArrayUtils.nullToEmpty(StringUtils.split(incomingValue, ",")));

        boolean existingHasPrimary = existingStores.stream()
                .anyMatch(store -> StringUtils.equalsIgnoreCase(store, primaryUserStoreDomain));
        boolean incomingHasPrimary = incomingStores.stream()
                .anyMatch(store -> StringUtils.equalsIgnoreCase(store, primaryUserStoreDomain));

        // If one contains the primary user store but not the other, the update is not allowed.
        if (existingHasPrimary != incomingHasPrimary) {
            throw handleClaimManagementClientError(ERROR_CODE_UNAUTHORIZED_ORG_FOR_EXCLUDED_USER_STORES_PROPERTY_UPDATE,
                    FORBIDDEN, primaryUserStoreDomain);
        }
    }

    private void populateDefaultProperties(LocalClaim localClaim) {

        localClaim.getClaimProperties().putIfAbsent(PROP_DISPLAY_ORDER, "0");
        localClaim.getClaimProperties().putIfAbsent(PROP_READ_ONLY, FALSE);
        localClaim.getClaimProperties().putIfAbsent(PROP_REQUIRED, FALSE);
        localClaim.getClaimProperties().putIfAbsent(PROP_SUPPORTED_BY_DEFAULT, FALSE);
        localClaim.getClaimProperties().putIfAbsent(PROP_MULTI_VALUED, FALSE);
        localClaim.getClaimProperties().putIfAbsent(PROP_DATA_TYPE, DataType.STRING.toString()
                .toLowerCase(Locale.ROOT));
    }

    private String handleAdditionalProperties(Map<String, String> claimProperties, String propertyName) {

        if (Boolean.parseBoolean(IdentityUtil.getProperty(RETURN_PREVIOUS_ADDITIONAL_PROPERTIES))) {
            return claimProperties.get(propertyName);
        }
        return claimProperties.remove(propertyName);
    }
}
