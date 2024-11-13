/*
 *  Copyright (c) (2019-2023), WSO2 LLC. (http://www.wso2.org).
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
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataClientException;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.AttributeMapping;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ClaimDialect;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;
import org.wso2.carbon.identity.claim.metadata.mgt.model.LocalClaim;
import org.wso2.carbon.identity.claim.metadata.mgt.util.ClaimConstants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.AttributeMappingDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LinkDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.PropertyDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.model.ClaimDialectConfiguration;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.model.ClaimErrorDTO;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static org.wso2.carbon.identity.api.server.claim.management.common.ClaimManagementDataHolder.getClaimMetadataManagementService;
import static org.wso2.carbon.identity.api.server.claim.management.common.ClaimManagementDataHolder.getOrganizationManager;
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
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_UNAUTHORIZED_ORG_FOR_CLAIM_MANAGEMENT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.ErrorMessage.ERROR_CODE_USERSTORE_NOT_SPECIFIED_IN_MAPPINGS;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.LOCAL_DIALECT;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.LOCAL_DIALECT_PATH;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DESCRIPTION;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DISPLAY_NAME;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_DISPLAY_ORDER;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_READ_ONLY;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_REG_EX;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_REQUIRED;
import static org.wso2.carbon.identity.api.server.claim.management.common.Constant.PROP_SUPPORTED_BY_DEFAULT;
import static org.wso2.carbon.identity.api.server.common.Constants.JSON_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_JSON;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_XML;
import static org.wso2.carbon.identity.api.server.common.Constants.MEDIA_TYPE_YAML;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.XML_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.Constants.YAML_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;
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
            getClaimMetadataManagementService().addClaimDialect(
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
            getClaimMetadataManagementService().removeClaimDialect(
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
            List<ClaimDialect> claimDialectList = getClaimMetadataManagementService().getClaimDialects(
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
            List<ClaimDialect> claimDialectList = getClaimMetadataManagementService().getClaimDialects(
                    ContextLoader.getTenantDomainFromContext());

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
                getClaimMetadataManagementService().renameClaimDialect(
                        createClaimDialect(base64DecodeId(dialectId)),
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
            getClaimMetadataManagementService().addLocalClaim(createLocalClaim(localClaimReqDTO),
                    ContextLoader.getTenantDomainFromContext());
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
            getClaimMetadataManagementService().removeLocalClaim(
                    claimURI,
                    ContextLoader.getTenantDomainFromContext());
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
            List<LocalClaim> localClaimList = getClaimMetadataManagementService().getLocalClaims(
                    ContextLoader.getTenantDomainFromContext());

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

        handleNotImplementedCapabilities(attributes, limit, offset, filter, sort);

        try {
            List<LocalClaim> localClaimList = getClaimMetadataManagementService().getLocalClaims(
                    ContextLoader.getTenantDomainFromContext());

            if (excludeIdentityClaims != null && excludeIdentityClaims) {
                localClaimList = localClaimList.stream()
                        .filter(claim -> !claim.getClaimURI().startsWith(IDENTITY_CLAIM_URI))
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
            validateClaimModificationEligibility();
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
            getClaimMetadataManagementService().updateLocalClaim(createLocalClaim(localClaimReqDTO),
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
     * @param fileInputStream   InputStream representing the uploaded claim dialect file.
     * @param fileDetail        Attachment object with metadata about the uploaded claim dialect file.
     * @param preserveClaims    Boolean value to indicate whether to merge and preserve the existing claims
     *                          or completely replace the existing claims set.
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

        List<String> claimsToDelete =  getLocalClaimResDTOs(getClaimMetadataManagementService()
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
                List<LocalClaimResDTO> localClaimResDTOList = getLocalClaimResDTOs(getClaimMetadataManagementService()
                        .getLocalClaims(ContextLoader.getTenantDomainFromContext()));
                claimResDTOList.addAll(localClaimResDTOList);
                dialectConfiguration.setClaims(claimResDTOList);
            } else {
                List<ExternalClaim> externalClaimList = getClaimMetadataManagementService().getExternalClaims(
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

            getClaimMetadataManagementService().addExternalClaim(
                    createExternalClaim(dialectId, externalClaimReqDTO),
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
            getClaimMetadataManagementService().removeExternalClaim(
                    externalClaimDialectURI,
                    externalClaimURI,
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
            List<ExternalClaim> externalClaimList = getClaimMetadataManagementService().getExternalClaims(
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
            List<ClaimDialect> claimDialectList = getClaimMetadataManagementService().getClaimDialects(
                    ContextLoader.getTenantDomainFromContext());
            String decodedDialectId = base64DecodeId(dialectId);
            ClaimDialect claimDialect = extractDialectFromDialectList(decodedDialectId, claimDialectList);

            if (claimDialect == null) {
                throw handleClaimManagementClientError(ERROR_CODE_DIALECT_NOT_FOUND, NOT_FOUND, dialectId);
            }

            List<ExternalClaim> externalClaimList = getClaimMetadataManagementService().getExternalClaims(
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
            getClaimMetadataManagementService().updateExternalClaim(
                    createExternalClaim(dialectId, externalClaimReqDTO),
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
        String regEx = claimProperties.remove(PROP_REG_EX);
        localClaimResDTO.setRegEx(regEx);
        if (regEx == null) {
            localClaimResDTO.setRegEx(StringUtils.EMPTY);
        }
        localClaimResDTO.setRequired(Boolean.valueOf(claimProperties.remove(PROP_REQUIRED)));
        localClaimResDTO.setSupportedByDefault(Boolean.valueOf(claimProperties.remove(PROP_SUPPORTED_BY_DEFAULT)));

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

        claimProperties.put(PROP_READ_ONLY, String.valueOf(localClaimReqDTO.getReadOnly()));
        claimProperties.put(PROP_REQUIRED, String.valueOf(localClaimReqDTO.getRequired()));
        claimProperties.put(PROP_SUPPORTED_BY_DEFAULT, String.valueOf(localClaimReqDTO.getSupportedByDefault()));
        claimProperties.putAll(propertiesToMap(localClaimReqDTO.getProperties()));

        List<AttributeMapping> attributeMappings = new ArrayList<>();
        for (AttributeMappingDTO attributeMappingDTO : localClaimReqDTO.getAttributeMapping()) {
            attributeMappings.add(new AttributeMapping(attributeMappingDTO.getUserstore(),
                    attributeMappingDTO.getMappedAttribute()));
        }

        return new LocalClaim(localClaimReqDTO.getClaimURI(), attributeMappings, claimProperties);
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
                getClaimMetadataManagementService().getClaimDialects(ContextLoader.getTenantDomainFromContext());
        ClaimDialect claimDialect = extractDialectFromDialectList(base64DecodeId(dialectId), claimDialectList);

        return claimDialect != null;
    }

    private boolean isLocalClaimExist(String claimId) throws ClaimMetadataException {

        List<LocalClaim> localClaimList = getClaimMetadataManagementService().getLocalClaims(
                ContextLoader.getTenantDomainFromContext());
        LocalClaim localClaim = extractLocalClaimFromClaimList(base64DecodeId(claimId), localClaimList);

        return localClaim != null;
    }

    private boolean isExternalClaimExist(String dialectId, String claimId) throws ClaimMetadataException {

        List<ExternalClaim> externalClaimList = getClaimMetadataManagementService().getExternalClaims(
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
            String organizationId = getOrganizationManager().resolveOrganizationId(tenantDomain);
            boolean isPrimaryOrg = getOrganizationManager().isPrimaryOrganization(organizationId);
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
}
